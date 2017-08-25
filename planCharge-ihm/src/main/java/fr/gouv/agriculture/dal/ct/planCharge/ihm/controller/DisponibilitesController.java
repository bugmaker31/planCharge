package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.ihm.view.PercentageStringConverter;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.TableViewAvecCalendrier;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ProfilDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.DisponibilitesService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithTooManyFields"})
public class DisponibilitesController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibilitesController.class);

    private static final StringConverter<Integer> NBRS_JOURS_STRING_CONVERTER = new StringConverter<Integer>() {

        private final DecimalFormat FORMATEUR = new DecimalFormat("#");

        @Null
        @Override
        public String toString(Integer f) {
            if (f == null) {
                return null;
            }
            if (f.equals(0)) {
                return "-";
            }
            return FORMATEUR.format(f);
        }

        @Null
        @Override
        public Integer fromString(String s) {
            if (s == null) {
                return null;
            }
            if (s.equals("-")) {
                return 0;
            }
            try {
                return FORMATEUR.parse(s).intValue();
            } catch (ParseException e) {
                LOGGER.error("Impossible de décoder une valeur décimale dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
                return null;
            }
        }
    };

    private static final StringConverter<Float> HUITIEMES_JOURS_STRING_CONVERTER = new StringConverter<Float>() {

        private final DecimalFormat FORMATEUR = new DecimalFormat("#.###");

        @Null
        @Override
        public String toString(Float f) {
            if (f == null) {
                return null;
            }
            if (f == 0) {
                return "-";
            }
            return FORMATEUR.format(f);
        }

        @Null
        @Override
        public Float fromString(String s) {
            if (s == null) {
                return null;
            }
            if (s.equals("-")) {
                return 0f;
            }
            try {
                return FORMATEUR.parse(s).floatValue();
            } catch (ParseException e) {
                LOGGER.error("Impossible de décoder une valeur décimale dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
                return null;
            }
        }
    };


    private static DisponibilitesController instance;

    public static DisponibilitesController instance() {
        return instance;
    }


    /*
     La couche métier :
      */

    //    @Autowired
    @NotNull
    private final DisponibilitesService disponibilitesService = DisponibilitesService.instance();

    //    @Autowired
    @NotNull
    private final ReferentielsService referentielsService = ReferentielsService.instance();

    //    @Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

/*
    @NotNull
    private final ObservableList<PlanificationTacheBean> planificationsBeans = planChargeBean.getPlanificationsBeans();
*/

    @NotNull
    private final NbrsJoursOuvresBean nbrsJoursOuvresBean = new NbrsJoursOuvresBean();

    @NotNull
    private final ObservableList<NbrsJoursOuvresBean> nbrsJoursOuvresBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeans = planChargeBean.getNbrsJoursAbsenceBeans();

    @NotNull
    private final ObservableList<NbrsJoursDispoRsrcBean> nbrsJoursDispoMinAgriBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<PctagesDispoRsrcBean> pctagesDispoCTBeans = planChargeBean.getPctagesDispoCTBeans();

    @NotNull
    private final ObservableList<NbrsJoursDispoRsrcBean> nbrsJoursDispoCTBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<PctagesDispoRsrcProfilBean> pctagesDispoMaxRsrcProfilBeans = planChargeBean.getPctagesDispoMaxRsrcProfilBeans();

    @NotNull
    private final ObservableList<NbrsJoursDispoRsrcProfilBean> nbrsJoursDispoMaxRsrcProfilBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<NbrsJoursDispoProfilBean> nbrsJoursDispoMaxProfilBeans = FXCollections.observableArrayList();


    /*
     La couche "View" :
      */

    // Les tables/colonnes :
    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursOuvresBean, Integer> nbrsJoursOuvresTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, String> ressourceNbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, String> profilNbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine1NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine2NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine3NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine4NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine5NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine6NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine7NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine8NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine9NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine10NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine11NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine12NbrsJoursOuvresColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursAbsenceBean, Float> nbrsJoursDAbsenceTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, /*RessourceHumaineBean*/String> ressourceNbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, String> profilNbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine1NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine2NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine3NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine4NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine5NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine6NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine7NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine8NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine9NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine10NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine11NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Float> semaine12NbrsJoursAbsenceColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoMinAgriTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, /*RessourceHumaineBean*/String> ressourceNbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, String> profilNbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine1NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine2NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine3NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine4NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine5NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine6NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine7NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine8NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine9NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine10NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine11NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine12NbrsJoursDispoMinAgriColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<PctagesDispoRsrcBean, Percentage> pctagesDispoCTTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, /*RessourceHumaineBean*/String> ressourcePctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, String> profilPctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine1PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine2PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine3PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine4PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine5PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine6PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine7PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine8PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine9PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine10PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine11PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcBean, Percentage> semaine12PctagesDispoCTColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoCTTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, /*RessourceHumaineBean*/String> ressourceNbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, String> profilNbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine1NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine2NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine3NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine4NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine5NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine6NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine7NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine8NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine9NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine10NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine11NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcBean, Float> semaine12NbrsJoursDispoCTColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<PctagesDispoRsrcProfilBean, Percentage> pctagesDispoMaxRsrcProfilTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, /*RessourceHumaineBean*/String> ressourcePctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, /*ProfilBean*/String> profilPctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine1PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine2PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine3PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine4PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine5PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine6PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine7PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine8PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine9PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine10PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine11PctagesDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoRsrcProfilBean, Percentage> semaine12PctagesDispoMaxRsrcProfilColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursDispoRsrcProfilBean, Float> nbrsJoursDispoMaxRsrcProfilTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, /*RessourceHumaineBean*/String> ressourceNbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, /*ProfilBean*/String> profilNbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine1NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine2NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine3NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine4NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine5NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine6NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine7NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine8NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine9NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine10NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine11NbrsJoursDispoMaxRsrcProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoRsrcProfilBean, Float> semaine12NbrsJoursDispoMaxRsrcProfilColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursDispoProfilBean, Float> nbrsJoursDispoMaxProfilTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, /*RessourceHumaineBean*/String> ressourceNbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, /*ProfilBean*/String> profilNbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine1NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine2NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine3NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine4NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine5NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine6NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine7NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine8NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine9NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine10NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine11NbrsJoursDispoMaxProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoProfilBean, Float> semaine12NbrsJoursDispoMaxProfilColumn;


    // Constructeurs :

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public DisponibilitesController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    // Getters/Setters:

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursOuvresBean, Integer> getNbrsJoursOuvresTable() {
        return nbrsJoursOuvresTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursAbsenceBean, Float> getNbrsJoursDAbsenceTable() {
        return nbrsJoursDAbsenceTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursDispoRsrcBean, Float> getNbrsJoursDispoMinAgriTable() {
        return nbrsJoursDispoMinAgriTable;
    }

    @NotNull
    public TableViewAvecCalendrier<PctagesDispoRsrcBean, Percentage> getPctagesDispoCTTable() {
        return pctagesDispoCTTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursDispoRsrcBean, Float> getNbrsJoursDispoCTTable() {
        return nbrsJoursDispoCTTable;
    }

    @NotNull
    public TableViewAvecCalendrier<PctagesDispoRsrcProfilBean, Percentage> getPctagesDispoMaxRsrcProfilTable() {
        return pctagesDispoMaxRsrcProfilTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursDispoRsrcProfilBean, Float> getNbrsJoursDispoMaxRsrcProfilTable() {
        return nbrsJoursDispoMaxRsrcProfilTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursDispoProfilBean, Float> getNbrsJoursDispoMaxProfilTable() {
        return nbrsJoursDispoMaxProfilTable;
    }


    // Méthodes :

    @FXML
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");
        initBeans();
        initTables();
        LOGGER.info("Initialisé.");
    }

    private void initBeans() {
        initBeansNbrsJoursOuvres();
        initBeansNbrsJoursAbsence();
        initBeansNbrsJoursDispoMinAgri();
        initBeansPctagesDispoCT();
        initBeansNbrsJoursDispoCT();
        initBeansPctagesDispoCT();
        initBeansPctagesDispoMaxRsrcProfil();
        initBeansNbrsJoursDispoMaxRsrcProfil();
//        initBeansNbrsJoursDispoMaxProfil(); FIXME FDA 2017/08 Coder.

        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {

                        ObjectProperty<LocalDate> debutMissionProperty = ressourceHumaineBean.debutMissionProperty();
                        debutMissionProperty.addListener((observable, oldValue, newValue) -> {
                            try {
                                calculerDisponibilites(ressourceHumaineBean);
                            } catch (IhmException e) {
                                LOGGER.error("Impossible de calculer les disponibilités de la ressource " + ressourceHumaineBean.getTrigramme() + ".", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                            }
                        });

                        ObjectProperty<LocalDate> finMissionProperty = ressourceHumaineBean.finMissionProperty();
                        finMissionProperty.addListener((observable, oldValue, newValue) -> {
                            try {
                                calculerDisponibilites(ressourceHumaineBean);
                            } catch (IhmException e) {
                                LOGGER.error("Impossible de calculer les disponibilités de la ressource " + ressourceHumaineBean.getTrigramme() + ".", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                            }
                        });
                    }
                }
            }
        });
    }

    private void initBeansNbrsJoursOuvres() {

        nbrsJoursOuvresBeans.setAll(nbrsJoursOuvresBean);

        planChargeBean.getJoursFeriesBeans().addListener((ListChangeListener<? super JourFerieBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    try {
                        calculerDisponibilites();
                    } catch (IhmException e) {
                        LOGGER.error("Impossible de calculer les disponibilités.", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }

    private void initBeansNbrsJoursAbsence() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (nbrsJoursAbsenceBeans.parallelStream().anyMatch(nbrsJoursAbsenceBean -> nbrsJoursAbsenceBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                        nbrsJoursAbsenceBeansAAjouter.add(new NbrsJoursAbsenceBean(ressourceHumaineBean, calendrier));
                    }
                    if (!nbrsJoursAbsenceBeansAAjouter.isEmpty()) {
                        nbrsJoursAbsenceBeans.addAll(nbrsJoursAbsenceBeansAAjouter);
                    }
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeansASupprimer = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getRemoved()) {
                        if (nbrsJoursAbsenceBeans.parallelStream().noneMatch(nbrsJoursAbsenceBean -> nbrsJoursAbsenceBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                        nbrsJoursAbsenceBeansASupprimer.add(new NbrsJoursAbsenceBean(ressourceHumaineBean, calendrier));
                    }
                    if (!nbrsJoursAbsenceBeansASupprimer.isEmpty()) {
                        nbrsJoursAbsenceBeans.removeAll(nbrsJoursAbsenceBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }

    private void initBeansNbrsJoursDispoMinAgri() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoRsrcBean> nbrsJoursDispoMinAgriBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (nbrsJoursDispoMinAgriBeans.parallelStream().anyMatch(nbrsJoursDispoMinAgriBean -> nbrsJoursDispoMinAgriBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                        nbrsJoursDispoMinAgriBeansAAjouter.add(new NbrsJoursDispoRsrcBean(ressourceHumaineBean, calendrier));
                    }
                    if (!nbrsJoursDispoMinAgriBeansAAjouter.isEmpty()) {
                        nbrsJoursDispoMinAgriBeans.addAll(nbrsJoursDispoMinAgriBeansAAjouter);
                    }
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursDispoRsrcBean> nbrsJoursDispoMinAgriBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDispoMinAgriBeans.parallelStream().noneMatch(nbrsJoursDispoMinAgriBean -> nbrsJoursDispoMinAgriBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                        nbrsJoursDispoMinAgriBeansASupprimer.add(new NbrsJoursDispoRsrcBean(ressourceHumaineBean, calendrier));
                    }
                    if (!nbrsJoursDispoMinAgriBeansASupprimer.isEmpty()) {
                        nbrsJoursDispoMinAgriBeans.removeAll(nbrsJoursDispoMinAgriBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }

    private void initBeansPctagesDispoCT() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<PctagesDispoRsrcBean> pctagesDispoCTBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (pctagesDispoCTBeans.parallelStream().anyMatch(pctagesDispoCTBean -> pctagesDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, PercentageProperty> calendrier = new TreeMap<>();
                        pctagesDispoCTBeansAAjouter.add(new PctagesDispoRsrcBean(ressourceHumaineBean, calendrier));
                    }
                    if (!pctagesDispoCTBeansAAjouter.isEmpty()) {
                        pctagesDispoCTBeans.addAll(pctagesDispoCTBeansAAjouter);
                    }
                }
                if (change.wasRemoved()) {
                    List<PctagesDispoRsrcBean> pctagesDispoCTBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (pctagesDispoCTBeans.parallelStream().noneMatch(pctagesDispoCTBean -> pctagesDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, PercentageProperty> calendrier = new TreeMap<>();
                        pctagesDispoCTBeansASupprimer.add(new PctagesDispoRsrcBean(ressourceHumaineBean, calendrier));
                    }
                    if (!pctagesDispoCTBeansASupprimer.isEmpty()) {
                        pctagesDispoCTBeans.removeAll(pctagesDispoCTBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });

        pctagesDispoCTBeans.addListener((ListChangeListener<? super PctagesDispoRsrcBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (PctagesDispoRsrcBean pctagesDispoCTBean : change.getAddedSubList()) {
                        RessourceHumaineBean ressourceHumaineBean = pctagesDispoCTBean.getRessourceHumaineBean();
                        try {
                            calculerDisponibilites(ressourceHumaineBean);
                        } catch (IhmException e) {
                            LOGGER.error("Impossible de calculer les disponibilités de la ressource " + ressourceHumaineBean.getTrigramme() + ".", e); // TODO FDA 2017/08 Trouevr mieux que juste loguer une erreur.
                        }
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }

    private void initBeansNbrsJoursDispoCT() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoRsrcBean> nbrsJoursDispoCTBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (nbrsJoursDispoCTBeans.parallelStream().anyMatch(nbrsJoursDispoCTBean -> nbrsJoursDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                        nbrsJoursDispoCTBeansAAjouter.add(new NbrsJoursDispoRsrcBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDispoCTBeans.addAll(nbrsJoursDispoCTBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursDispoRsrcBean> nbrsJoursDispoCTBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDispoCTBeans.parallelStream().noneMatch(nbrsJoursDispoCTBean -> nbrsJoursDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                        nbrsJoursDispoCTBeansASupprimer.add(new NbrsJoursDispoRsrcBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDispoCTBeans.removeAll(nbrsJoursDispoCTBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }

    private void initBeansPctagesDispoMaxRsrcProfil() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<PctagesDispoRsrcProfilBean> pctagesDispoMaxRsrcProfilBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (pctagesDispoMaxRsrcProfilBeans.parallelStream().anyMatch(pctagesDispoMaxRsrcProfilBean -> pctagesDispoMaxRsrcProfilBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        try {
                            for (ProfilDTO profilDTO : referentielsService.profils()) {
                                Map<LocalDate, PercentageProperty> calendrier = new TreeMap<>(); // TreeMap afin de trier, juste pour faciliter le débogage.
                                pctagesDispoMaxRsrcProfilBeansAAjouter.add(new PctagesDispoRsrcProfilBean(ressourceHumaineBean, ProfilBean.from(profilDTO), calendrier));
                            }
                        } catch (ServiceException e) {
                            // TODO FDA 2017/08 Trouver mieux que juste loguer l'erreur.
                            LOGGER.error("Impossible d'initialiser les pourcentages de disponibilités max. par ressource et profil.", e);
                        }
                    }
                    if (!pctagesDispoMaxRsrcProfilBeansAAjouter.isEmpty()) {
                        pctagesDispoMaxRsrcProfilBeans.addAll(pctagesDispoMaxRsrcProfilBeansAAjouter);
                    }
                }
                if (change.wasRemoved()) {
                    List<RessourceHumaineBean> pctagesDispoMaxRsrcProfilBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (pctagesDispoMaxRsrcProfilBeans.parallelStream().noneMatch(pctagesDispoMaxRsrcProfilBean -> pctagesDispoMaxRsrcProfilBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        pctagesDispoMaxRsrcProfilBeansASupprimer.add(ressourceHumaineBean);
                    }
                    pctagesDispoMaxRsrcProfilBeans.setAll(
                            pctagesDispoMaxRsrcProfilBeans.stream()
                                    .filter(pctagesDispoMaxRsrcProfilBean -> pctagesDispoMaxRsrcProfilBeansASupprimer.contains(pctagesDispoMaxRsrcProfilBean.getRessourceHumaineBean()))
                                    .collect(Collectors.toList())
                    );
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });

        pctagesDispoMaxRsrcProfilBeans.addListener((ListChangeListener<? super PctagesDispoRsrcProfilBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    Set<RessourceHumaineBean> ressourceHumaineBeansAvecDispoCalculees = new HashSet<>();
                    for (PctagesDispoRsrcProfilBean pctagesDispoMaxRsrcProfilBean : change.getAddedSubList()) {
                        RessourceHumaineBean ressourceHumaineBean = pctagesDispoMaxRsrcProfilBean.getRessourceHumaineBean();
                        if (!ressourceHumaineBeansAvecDispoCalculees.contains(ressourceHumaineBean)) {
                            try {
                                calculerDisponibilites(ressourceHumaineBean);
                            } catch (IhmException e) {
                                LOGGER.error("Impossible de calculer les disponibilités max de la ressource " + ressourceHumaineBean.getTrigramme() + " par profil.", e); // TODO FDA 2017/08 Trouevr mieux que juste loguer une erreur.
                            }
                            ressourceHumaineBeansAvecDispoCalculees.add(ressourceHumaineBean);
                        }
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }

    private void initBeansNbrsJoursDispoMaxRsrcProfil() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoRsrcProfilBean> nbrsJoursDispoMaxRsrcProfilBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (nbrsJoursDispoMaxRsrcProfilBeans.parallelStream().anyMatch(nbrsJoursDispoMaxRsrcProfilBean -> nbrsJoursDispoMaxRsrcProfilBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        try {
                            for (ProfilDTO profilDTO : referentielsService.profils()) {
                                Map<LocalDate, FloatProperty> calendrier = new TreeMap<>(); // TreeMap afin de trier, juste pour faciliter le débogage.
                                nbrsJoursDispoMaxRsrcProfilBeansAAjouter.add(new NbrsJoursDispoRsrcProfilBean(ressourceHumaineBean, ProfilBean.from(profilDTO), calendrier));
                            }
                        } catch (ServiceException e) {
                            // TODO FDA 2017/08 Trouver mieux que juste loguer l'erreur.
                            LOGGER.error("Impossible d'initialiser les pourcentages de disponibilités max. par ressource et profil.", e);
                        }
                    }
                    if (!nbrsJoursDispoMaxRsrcProfilBeansAAjouter.isEmpty()) {
                        nbrsJoursDispoMaxRsrcProfilBeans.addAll(nbrsJoursDispoMaxRsrcProfilBeansAAjouter);
                    }
                }
                if (change.wasRemoved()) {
                    List<RessourceHumaineBean> nbrsJoursDispoMaxRsrcProfilBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDispoMaxRsrcProfilBeans.parallelStream().noneMatch(nbrsJoursDispoMaxRsrcProfilBean -> nbrsJoursDispoMaxRsrcProfilBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        nbrsJoursDispoMaxRsrcProfilBeansASupprimer.add(ressourceHumaineBean);
                    }
                    nbrsJoursDispoMaxRsrcProfilBeans.setAll(
                            nbrsJoursDispoMaxRsrcProfilBeans.stream()
                                    .filter(nbrsJoursDispoMaxRsrcProfilBean -> nbrsJoursDispoMaxRsrcProfilBeansASupprimer.contains(nbrsJoursDispoMaxRsrcProfilBean.getRessourceHumaineBean()))
                                    .collect(Collectors.toList())
                    );
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });

        nbrsJoursDispoMaxRsrcProfilBeans.addListener((ListChangeListener<? super NbrsJoursDispoRsrcProfilBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    Set<RessourceHumaineBean> ressourceHumaineBeansAvecDispoCalculees = new HashSet<>();
                    for (NbrsJoursDispoRsrcProfilBean nbrsJoursDispoMaxRsrcProfilBean : change.getAddedSubList()) {
                        RessourceHumaineBean ressourceHumaineBean = nbrsJoursDispoMaxRsrcProfilBean.getRessourceHumaineBean();
                        if (!ressourceHumaineBeansAvecDispoCalculees.contains(ressourceHumaineBean)) {
                            try {
                                calculerDisponibilites(ressourceHumaineBean);
                            } catch (IhmException e) {
                                LOGGER.error("Impossible de calculer les disponibilités max de la ressource " + ressourceHumaineBean.getTrigramme() + " par profil.", e); // TODO FDA 2017/08 Trouevr mieux que juste loguer une erreur.
                            }
                            ressourceHumaineBeansAvecDispoCalculees.add(ressourceHumaineBean);
                        }
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }


    public List<TableViewAvecCalendrier<?, ?>> tables() {
        return Arrays.asList(new TableViewAvecCalendrier<?, ?>[]{
                nbrsJoursOuvresTable,
                nbrsJoursDAbsenceTable,
                nbrsJoursDispoMinAgriTable,
                pctagesDispoCTTable,
                nbrsJoursDispoCTTable,
                pctagesDispoMaxRsrcProfilTable,
                nbrsJoursDispoMaxRsrcProfilTable,
                nbrsJoursDispoMaxProfilTable
        });
    }

    private void initTables() throws IhmException {
        initTableNbrsJoursOuvres();
        initTableNbrsJoursAbsence();
        initTableNbrsJoursDispoMinAgri();
        initTablePctagesDispoMinAgri();
        initTableNbrsJoursDispoCT();
        initTablePctagesDispoMaxRsrcProfil();
        initTableNbrsJoursDispoMaxRsrcProfil();
//        initTableNbrsJoursDispoMaxProfil(); FIXME FDA 2017/08 Coder.

        synchroniserLargeurColonnes();
    }

    private static class DisponibilitesRessourceHumaineCell<S extends AbstractDisponibilitesRessourceBean, T> extends EditableAwareTextFieldTableCell<S, T> {

        private PlanChargeBean planChargeBean;
        private int noSemaine;

        public DisponibilitesRessourceHumaineCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
            super(stringConverter, cantEditErrorDisplayer);
            this.planChargeBean = planChargeBean;
            this.noSemaine = noSemaine;
        }

        public DisponibilitesRessourceHumaineCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter) {
            this(planChargeBean, noSemaine, stringConverter, null);
        }

        public int getNoSemaine() {
            return noSemaine;
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            styler();
        }

        private void styler() {

            // Réinit des style spécifiques :
            getStyleClass().removeAll("avantMission", "pendantMission", "apresMission");

            /* Non, surtout pas, sinon les cellules vides, ne seront pas stylées/décorées.
            // Stop, si cellule vide :
            if (empty || (item == null)) {
                return;
            }
            */

            // Récupération des infos sur la cellule :
            //noinspection unchecked
            TableRow<? extends S> tableRow = getTableRow();
            S dispoBean = tableRow.getItem();
            if (dispoBean == null) {
                return;
            }
            LocalDate debutMission = dispoBean.getRessourceHumaineBean().getDebutMission();
            LocalDate finMission = dispoBean.getRessourceHumaineBean().getFinMission();

            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            LocalDate finPeriode = debutPeriode.plusDays(7);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]

            // Formatage du style (CSS) de la cellule :
            if (debutMission != null) {
                if (debutPeriode.isBefore(debutMission)) {
                    getStyleClass().add("avantMission");
                    return;
                }
            }
            if (finMission != null) {
                if (finPeriode.isAfter(finMission.plusDays(7))) {
                    getStyleClass().add("apresMission");
                    return;
                }
            }
            getStyleClass().add("pendantMission");
        }

    }

    private void initTableNbrsJoursOuvres() {

        nbrsJoursOuvresTable.setCalendrierColumns(
                semaine1NbrsJoursOuvresColumn,
                semaine2NbrsJoursOuvresColumn,
                semaine3NbrsJoursOuvresColumn,
                semaine4NbrsJoursOuvresColumn,
                semaine5NbrsJoursOuvresColumn,
                semaine6NbrsJoursOuvresColumn,
                semaine7NbrsJoursOuvresColumn,
                semaine8NbrsJoursOuvresColumn,
                semaine9NbrsJoursOuvresColumn,
                semaine10NbrsJoursOuvresColumn,
                semaine11NbrsJoursOuvresColumn,
                semaine12NbrsJoursOuvresColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursOuvresColumn.setCellValueFactory((CellDataFeatures<NbrsJoursOuvresBean, String> cell) -> new SimpleStringProperty("N/A"));
        profilNbrsJoursOuvresColumn.setCellValueFactory((CellDataFeatures<NbrsJoursOuvresBean, String> cell) -> new SimpleStringProperty("N/A"));
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrJoursOuvresCellCallback implements Callback<CellDataFeatures<NbrsJoursOuvresBean, Integer>, ObservableValue<Integer>> {
                private final int noSemaine;

                public NbrJoursOuvresCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Integer> call(CellDataFeatures<NbrsJoursOuvresBean, Integer> cell) {
                    if (cell == null) {
                        return null;
                    }
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    NbrsJoursOuvresBean nbrsJoursOuvresRsrcHumPeriodeBean = cell.getValue();
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    if (!nbrsJoursOuvresRsrcHumPeriodeBean.containsKey(debutPeriode)) {
                        nbrsJoursOuvresRsrcHumPeriodeBean.put(debutPeriode, new SimpleIntegerProperty());
                    }
                    IntegerProperty nbrJoursOuvresPeriode = nbrsJoursOuvresRsrcHumPeriodeBean.get(debutPeriode);
                    return nbrJoursOuvresPeriode.asObject();
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursOuvresBean, Integer> nbrsJoursOuvresColumn : nbrsJoursOuvresTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        // Cette table n'est pas éditable (données calculées à partir des référentiels).
        ihm.interdireEdition(ressourceNbrsJoursOuvresColumn, "Cette colonne ne contient pas de données (juste pour aligner avec les lignes suivantes).");
        ihm.interdireEdition(profilNbrsJoursOuvresColumn, "Cette colonne ne contient pas de données (juste pour aligner avec les lignes suivantes).");
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursOuvresBean, Integer> nbrsJoursOuvresColumn : nbrsJoursOuvresTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                nbrsJoursOuvresColumn.setCellFactory(cell -> new EditableAwareTextFieldTableCell<>(NBRS_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours ouvrés est calculé à partir du référentiel des jours fériés.")));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (1 seule ligne).

        TableViews.disableReagencingColumns(nbrsJoursOuvresTable);
        TableViews.adjustHeightToRowCount(nbrsJoursOuvresTable);

        SortedList<NbrsJoursOuvresBean> sortedBeans = new SortedList<>(nbrsJoursOuvresBeans);
        sortedBeans.comparatorProperty().bind(nbrsJoursOuvresTable.comparatorProperty());

        nbrsJoursOuvresTable.setItems(sortedBeans);
    }

    private void initTableNbrsJoursAbsence() throws IhmException {

        nbrsJoursDAbsenceTable.setCalendrierColumns(
                semaine1NbrsJoursAbsenceColumn,
                semaine2NbrsJoursAbsenceColumn,
                semaine3NbrsJoursAbsenceColumn,
                semaine4NbrsJoursAbsenceColumn,
                semaine5NbrsJoursAbsenceColumn,
                semaine6NbrsJoursAbsenceColumn,
                semaine7NbrsJoursAbsenceColumn,
                semaine8NbrsJoursAbsenceColumn,
                semaine9NbrsJoursAbsenceColumn,
                semaine10NbrsJoursAbsenceColumn,
                semaine11NbrsJoursAbsenceColumn,
                semaine12NbrsJoursAbsenceColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursAbsenceColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        profilNbrsJoursAbsenceColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrJoursDAbsenceCellCallback implements Callback<CellDataFeatures<NbrsJoursAbsenceBean, Float>, ObservableValue<Float>> {
                private final int noSemaine;

                public NbrJoursDAbsenceCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Float> call(CellDataFeatures<NbrsJoursAbsenceBean, Float> cell) {
                    if (cell == null) {
                        return null;
                    }
                    NbrsJoursAbsenceBean nbrsJoursAbsenceBean = cell.getValue();
                    RessourceHumaineBean ressourceHumaineBean = nbrsJoursAbsenceBean.getRessourceHumaineBean();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    FloatProperty nbrJoursDAbsencePeriode = nbrsJoursAbsenceBean.get(debutPeriode);
                    if (nbrJoursDAbsencePeriode == null) { // Pas d'absence prévue pour cette ressource humaine sur cette période.
                        return null;
                    }
                    return nbrJoursDAbsencePeriode.asObject();
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursAbsenceBean, Float> nbrsJoursDAbsenceColumn : nbrsJoursDAbsenceTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        // Rq : 1ère colonne (ressource) non éditable.
        ihm.interdireEdition(ressourceNbrsJoursAbsenceColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursAbsenceColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrsJoursAbsenceCell extends DisponibilitesRessourceHumaineCell<NbrsJoursAbsenceBean, Float> {

                public NbrsJoursAbsenceCell(int noSemaine) {
                    super(planChargeBean, noSemaine, HUITIEMES_JOURS_STRING_CONVERTER);
                }

                @Override
                public void commitEdit(Float newValue) {
                    super.commitEdit(newValue);

                    //noinspection unchecked
                    TableRow<NbrsJoursAbsenceBean> tableRow = getTableRow();
                    NbrsJoursAbsenceBean nbrJoursAbsenceBean = tableRow.getItem();
                    if (nbrJoursAbsenceBean == null) {
                        return;
                    }
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    if (!nbrJoursAbsenceBean.containsKey(debutPeriode)) {
                        nbrJoursAbsenceBean.put(debutPeriode, new SimpleFloatProperty());
                    }
                    FloatProperty nbrJoursDAbsencePeriode = nbrJoursAbsenceBean.get(debutPeriode);
                    nbrJoursDAbsencePeriode.set(newValue);

                    try {
                        calculerDisponibilites(nbrJoursAbsenceBean.getRessourceHumaineBean(), getNoSemaine());
                    } catch (IhmException e) {
                        // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        LOGGER.error("Impossible de màj les disponibilités.", e);
                    }
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursAbsenceBean, Float> nbrsJoursDAbsenceColumn : nbrsJoursDAbsenceTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                nbrsJoursDAbsenceColumn.setCellFactory(param -> new NbrsJoursAbsenceCell(finalCptColonne));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        nbrsJoursDAbsenceTable.setItems(nbrsJoursAbsenceBeans);

        TableViews.disableReagencingColumns(nbrsJoursDAbsenceTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDAbsenceTable);

        SortedList<NbrsJoursAbsenceBean> sortedBeans = new SortedList<>(nbrsJoursAbsenceBeans);
        sortedBeans.comparatorProperty().bind(nbrsJoursDAbsenceTable.comparatorProperty());
    }

    private void initTableNbrsJoursDispoMinAgri() {

        nbrsJoursDispoMinAgriTable.setCalendrierColumns(
                semaine1NbrsJoursDispoMinAgriColumn,
                semaine2NbrsJoursDispoMinAgriColumn,
                semaine3NbrsJoursDispoMinAgriColumn,
                semaine4NbrsJoursDispoMinAgriColumn,
                semaine5NbrsJoursDispoMinAgriColumn,
                semaine6NbrsJoursDispoMinAgriColumn,
                semaine7NbrsJoursDispoMinAgriColumn,
                semaine8NbrsJoursDispoMinAgriColumn,
                semaine9NbrsJoursDispoMinAgriColumn,
                semaine10NbrsJoursDispoMinAgriColumn,
                semaine11NbrsJoursDispoMinAgriColumn,
                semaine12NbrsJoursDispoMinAgriColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoMinAgriColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        profilNbrsJoursDispoMinAgriColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrJoursDispoMinAgriCellCallback implements Callback<CellDataFeatures<NbrsJoursDispoRsrcBean, Float>, ObservableValue<Float>> {
                private final int noSemaine;

                private NbrJoursDispoMinAgriCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Float> call(CellDataFeatures<NbrsJoursDispoRsrcBean, Float> cell) {
                    if (cell == null) {
                        return null;
                    }
                    NbrsJoursDispoRsrcBean nbrsJoursDispoMinAgriBean = cell.getValue();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    if (!nbrsJoursDispoMinAgriBean.containsKey(debutPeriode)) {
                        nbrsJoursDispoMinAgriBean.put(debutPeriode, new SimpleFloatProperty());
                    }
                    FloatProperty nbrsJoursDispoMinAgriProperty = nbrsJoursDispoMinAgriBean.get(debutPeriode);
                    return nbrsJoursDispoMinAgriProperty.asObject();
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoMinAgriColumn : nbrsJoursDispoMinAgriTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoMinAgriColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoMinAgriColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        int cptColonne = 0;
        for (TableColumn<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoMinAgriColumn : nbrsJoursDispoMinAgriTable.getCalendrierColumns()) {
            cptColonne++;
            int finalCptColonne = cptColonne;
            nbrsJoursDispoMinAgriColumn.setCellFactory(cell -> new DisponibilitesRessourceHumaineCell<>(planChargeBean, finalCptColonne, HUITIEMES_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité au Ministère est calculé à partir des jours ouvrés et d'absence.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursDispoMinAgriTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDispoMinAgriTable);

        // Tri de la liste :
        nbrsJoursDispoMinAgriTable.getItems().addListener((ListChangeListener<? super NbrsJoursDispoRsrcBean>) change -> {
            SortedList<NbrsJoursDispoRsrcBean> sortedBeans = new SortedList<>(nbrsJoursDispoMinAgriBeans);
            sortedBeans.comparatorProperty().bind(nbrsJoursDispoMinAgriTable.comparatorProperty());
        });

        nbrsJoursDispoMinAgriTable.setItems(nbrsJoursDispoMinAgriBeans);
    }

    private void initTablePctagesDispoMinAgri() {

        pctagesDispoCTTable.setCalendrierColumns(
                semaine1PctagesDispoCTColumn,
                semaine2PctagesDispoCTColumn,
                semaine3PctagesDispoCTColumn,
                semaine4PctagesDispoCTColumn,
                semaine5PctagesDispoCTColumn,
                semaine6PctagesDispoCTColumn,
                semaine7PctagesDispoCTColumn,
                semaine8PctagesDispoCTColumn,
                semaine9PctagesDispoCTColumn,
                semaine10PctagesDispoCTColumn,
                semaine11PctagesDispoCTColumn,
                semaine12PctagesDispoCTColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourcePctagesDispoCTColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        profilPctagesDispoCTColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class PctagesDispoMinAgriCellCallback implements Callback<CellDataFeatures<PctagesDispoRsrcBean, Percentage>, ObservableValue<Percentage>> {
                private final int noSemaine;

                private PctagesDispoMinAgriCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Percentage> call(CellDataFeatures<PctagesDispoRsrcBean, Percentage> cell) {
                    if (cell == null) {
                        return null;
                    }
                    PctagesDispoRsrcBean pctagesDispoCTBean = cell.getValue();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    PercentageProperty percentageProperty = pctagesDispoCTBean.get(debutPeriode);
                    return percentageProperty;
                }
            }
            int cptColonne = 0;
            for (TableColumn<PctagesDispoRsrcBean, Percentage> pctagesDispoMinAgriColumn : pctagesDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                pctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourcePctagesDispoCTColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilPctagesDispoCTColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class PctagesDispoCTCell extends DisponibilitesRessourceHumaineCell<PctagesDispoRsrcBean, Percentage> {

                private PctagesDispoCTCell(int noSemaine) {
                    super(planChargeBean, noSemaine, new PercentageStringConverter());
                }

                @Override
                public void commitEdit(Percentage newValue) {
                    super.commitEdit(newValue);

                    //noinspection unchecked
                    TableRow<PctagesDispoRsrcBean> tableRow = getTableRow();
                    PctagesDispoRsrcBean pctagesDispoCTBean = tableRow.getItem();
                    if (pctagesDispoCTBean == null) {
                        return;
                    }
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    if (!pctagesDispoCTBean.containsKey(debutPeriode)) {
                        pctagesDispoCTBean.put(debutPeriode, new PercentageProperty(DisponibilitesService.PCTAGE_DISPO_CT_MIN.floatValue()));
                    }
                    PercentageProperty pctageDispoCTPeriodeProperty = pctagesDispoCTBean.get(debutPeriode);
                    pctageDispoCTPeriodeProperty.setValue(newValue);

                    try {
                        calculerDisponibilites(pctagesDispoCTBean.getRessourceHumaineBean(), getNoSemaine());
                    } catch (IhmException e) {
                        // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        LOGGER.error("Impossible de màj les disponibilités.", e);
                    }
                }
            }
            int cptColonne = 0;
            for (TableColumn<PctagesDispoRsrcBean, Percentage> pctagesDispoCTColumn : pctagesDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                pctagesDispoCTColumn.setCellFactory(cell -> new PctagesDispoCTCell(finalCptColonne));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(pctagesDispoCTTable);
        TableViews.adjustHeightToRowCount(pctagesDispoCTTable);

        // Tri de la liste :
        pctagesDispoCTTable.getItems().addListener((ListChangeListener<? super PctagesDispoRsrcBean>) change -> {
            SortedList<PctagesDispoRsrcBean> sortedBeans = new SortedList<>(pctagesDispoCTBeans);
            sortedBeans.comparatorProperty().bind(pctagesDispoCTTable.comparatorProperty());
        });

        pctagesDispoCTTable.setItems(pctagesDispoCTBeans);
    }

    private void initTableNbrsJoursDispoCT() {

        nbrsJoursDispoCTTable.setCalendrierColumns(
                semaine1NbrsJoursDispoCTColumn,
                semaine2NbrsJoursDispoCTColumn,
                semaine3NbrsJoursDispoCTColumn,
                semaine4NbrsJoursDispoCTColumn,
                semaine5NbrsJoursDispoCTColumn,
                semaine6NbrsJoursDispoCTColumn,
                semaine7NbrsJoursDispoCTColumn,
                semaine8NbrsJoursDispoCTColumn,
                semaine9NbrsJoursDispoCTColumn,
                semaine10NbrsJoursDispoCTColumn,
                semaine11NbrsJoursDispoCTColumn,
                semaine12NbrsJoursDispoCTColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoCTColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        profilNbrsJoursDispoCTColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrJoursDispoCTCellCallback implements Callback<CellDataFeatures<NbrsJoursDispoRsrcBean, Float>, ObservableValue<Float>> {
                private final int noSemaine;

                private NbrJoursDispoCTCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Float> call(CellDataFeatures<NbrsJoursDispoRsrcBean, Float> cell) {
                    if (cell == null) {
                        return null;
                    }
                    NbrsJoursDispoRsrcBean nbrsJoursDispoCTBean = cell.getValue();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    if (!nbrsJoursDispoCTBean.containsKey(debutPeriode)) {
                        nbrsJoursDispoCTBean.put(debutPeriode, new SimpleFloatProperty());
                    }
                    FloatProperty nbrsJoursDispoCTProperty = nbrsJoursDispoCTBean.get(debutPeriode);
                    return nbrsJoursDispoCTProperty.asObject();
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoCTColumn : nbrsJoursDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoCTColumn.setCellValueFactory(new NbrJoursDispoCTCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoCTColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoCTColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        int cptColonne = 0;
        for (TableColumn<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoCTColumn : nbrsJoursDispoCTTable.getCalendrierColumns()) {
            cptColonne++;
            int finalCptColonne = cptColonne;
            nbrsJoursDispoCTColumn.setCellFactory(cell -> new DisponibilitesRessourceHumaineCell<>(planChargeBean, finalCptColonne, HUITIEMES_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité à la CT est calculé à partir des pourcentages de dispo pour la CT.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursDispoCTTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDispoCTTable);

        // Tri de la liste :
        nbrsJoursDispoCTTable.getItems().addListener((ListChangeListener<? super NbrsJoursDispoRsrcBean>) change -> {
            SortedList<NbrsJoursDispoRsrcBean> sortedBeans = new SortedList<>(nbrsJoursDispoCTBeans);
            sortedBeans.comparatorProperty().bind(nbrsJoursDispoCTTable.comparatorProperty());
        });

        nbrsJoursDispoCTTable.setItems(nbrsJoursDispoCTBeans);
    }

    private void initTablePctagesDispoMaxRsrcProfil() {
        pctagesDispoMaxRsrcProfilTable.setCalendrierColumns(
                semaine1PctagesDispoMaxRsrcProfilColumn,
                semaine2PctagesDispoMaxRsrcProfilColumn,
                semaine3PctagesDispoMaxRsrcProfilColumn,
                semaine4PctagesDispoMaxRsrcProfilColumn,
                semaine5PctagesDispoMaxRsrcProfilColumn,
                semaine6PctagesDispoMaxRsrcProfilColumn,
                semaine7PctagesDispoMaxRsrcProfilColumn,
                semaine8PctagesDispoMaxRsrcProfilColumn,
                semaine9PctagesDispoMaxRsrcProfilColumn,
                semaine10PctagesDispoMaxRsrcProfilColumn,
                semaine11PctagesDispoMaxRsrcProfilColumn,
                semaine12PctagesDispoMaxRsrcProfilColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourcePctagesDispoMaxRsrcProfilColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        profilPctagesDispoMaxRsrcProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class PctagesDispoMaxRsrcProfilCallback implements Callback<CellDataFeatures<PctagesDispoRsrcProfilBean, Percentage>, ObservableValue<Percentage>> {
                private final int noSemaine;

                private PctagesDispoMaxRsrcProfilCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Percentage> call(CellDataFeatures<PctagesDispoRsrcProfilBean, Percentage> cell) {
                    if (cell == null) {
                        return null;
                    }
                    PctagesDispoRsrcProfilBean pctagesDispoMaxRsrcProfilBean = cell.getValue();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    PercentageProperty percentageProperty = pctagesDispoMaxRsrcProfilBean.get(debutPeriode);
                    return percentageProperty;
                }
            }
            int cptColonne = 0;
            for (TableColumn<PctagesDispoRsrcProfilBean, Percentage> pctagesDispoMaxRsrcProfilColumn : pctagesDispoMaxRsrcProfilTable.getCalendrierColumns()) {
                cptColonne++;
                pctagesDispoMaxRsrcProfilColumn.setCellValueFactory(new PctagesDispoMaxRsrcProfilCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourcePctagesDispoMaxRsrcProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilPctagesDispoMaxRsrcProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class PctagesDispoMaxRsrcProfilCell extends DisponibilitesRessourceHumaineCell<PctagesDispoRsrcProfilBean, Percentage> {

                private PctagesDispoMaxRsrcProfilCell(int noSemaine) {
                    super(planChargeBean, noSemaine, new PercentageStringConverter());
                }

                @Override
                public void commitEdit(Percentage newValue) {
                    super.commitEdit(newValue);

                    //noinspection unchecked
                    TableRow<PctagesDispoRsrcProfilBean> tableRow = getTableRow();
                    PctagesDispoRsrcProfilBean pctagesDispoMaxRsrcProfilBean = tableRow.getItem();
                    if (pctagesDispoMaxRsrcProfilBean == null) {
                        return;
                    }
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    if (!pctagesDispoMaxRsrcProfilBean.containsKey(debutPeriode)) {
                        pctagesDispoMaxRsrcProfilBean.put(debutPeriode, new PercentageProperty(DisponibilitesService.PCTAGE_DISPO_CT_MIN.floatValue()));
                    }
                    PercentageProperty pctageDispoCTPeriodeProperty = pctagesDispoMaxRsrcProfilBean.get(debutPeriode);
                    pctageDispoCTPeriodeProperty.setValue(newValue);

                    try {
                        calculerDisponibilites(pctagesDispoMaxRsrcProfilBean.getRessourceHumaineBean(), getNoSemaine());
                    } catch (IhmException e) {
                        // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        LOGGER.error("Impossible de màj les disponibilités.", e);
                    }
                }
            }
            int cptColonne = 0;
            for (TableColumn<PctagesDispoRsrcProfilBean, Percentage> pctagesDispoMaxRsrcProfilColumn : pctagesDispoMaxRsrcProfilTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                pctagesDispoMaxRsrcProfilColumn.setCellFactory(cell -> new PctagesDispoMaxRsrcProfilCell(finalCptColonne));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        pctagesDispoMaxRsrcProfilTable.setItems(pctagesDispoMaxRsrcProfilBeans);

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        TableViews.enableFilteringOnColumns(pctagesDispoMaxRsrcProfilTable, ressourcePctagesDispoMaxRsrcProfilColumn, profilPctagesDispoMaxRsrcProfilColumn);

        TableViews.disableReagencingColumns(pctagesDispoMaxRsrcProfilTable);
        TableViews.adjustHeightToRowCount(pctagesDispoMaxRsrcProfilTable);

        // Tri de la liste :
        pctagesDispoMaxRsrcProfilTable.getItems().addListener((ListChangeListener<? super PctagesDispoRsrcProfilBean>) change -> {
            SortedList<PctagesDispoRsrcProfilBean> sortedBeans = new SortedList<>(pctagesDispoMaxRsrcProfilBeans);
            sortedBeans.comparatorProperty().bind(pctagesDispoMaxRsrcProfilTable.comparatorProperty());
        });
    }

    private void initTableNbrsJoursDispoMaxRsrcProfil() {
        nbrsJoursDispoMaxRsrcProfilTable.setCalendrierColumns(
                semaine1NbrsJoursDispoMaxRsrcProfilColumn,
                semaine2NbrsJoursDispoMaxRsrcProfilColumn,
                semaine3NbrsJoursDispoMaxRsrcProfilColumn,
                semaine4NbrsJoursDispoMaxRsrcProfilColumn,
                semaine5NbrsJoursDispoMaxRsrcProfilColumn,
                semaine6NbrsJoursDispoMaxRsrcProfilColumn,
                semaine7NbrsJoursDispoMaxRsrcProfilColumn,
                semaine8NbrsJoursDispoMaxRsrcProfilColumn,
                semaine9NbrsJoursDispoMaxRsrcProfilColumn,
                semaine10NbrsJoursDispoMaxRsrcProfilColumn,
                semaine11NbrsJoursDispoMaxRsrcProfilColumn,
                semaine12NbrsJoursDispoMaxRsrcProfilColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoMaxRsrcProfilColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        profilNbrsJoursDispoMaxRsrcProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrsJoursDispoMaxRsrcProfilCallback implements Callback<CellDataFeatures<NbrsJoursDispoRsrcProfilBean, Float>, ObservableValue<Float>> {
                private final int noSemaine;

                private NbrsJoursDispoMaxRsrcProfilCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Float> call(CellDataFeatures<NbrsJoursDispoRsrcProfilBean, Float> cell) {
                    if (cell == null) {
                        return null;
                    }
                    NbrsJoursDispoRsrcProfilBean nbrsJoursDispoMaxRsrcProfilBean = cell.getValue();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    FloatProperty nbrJoursProperty = nbrsJoursDispoMaxRsrcProfilBean.get(debutPeriode);
                    if (nbrJoursProperty == null) {
                        nbrJoursProperty = new SimpleFloatProperty();
                        nbrsJoursDispoMaxRsrcProfilBean.put(debutPeriode, nbrJoursProperty);
                    }
                    return nbrJoursProperty.asObject();
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoRsrcProfilBean, Float> nbrsJoursDispoMaxRsrcProfilColumn : nbrsJoursDispoMaxRsrcProfilTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoMaxRsrcProfilColumn.setCellValueFactory(new NbrsJoursDispoMaxRsrcProfilCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoMaxRsrcProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoMaxRsrcProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoRsrcProfilBean, Float> nbrsJoursDispoMaxRsrcProfilColumn : nbrsJoursDispoMaxRsrcProfilTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                nbrsJoursDispoMaxRsrcProfilColumn.setCellFactory(cell -> new DisponibilitesRessourceHumaineCell<>(planChargeBean, finalCptColonne, HUITIEMES_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité max. par ressrouce et par profil est calculé à partir des pourcentages.")));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        nbrsJoursDispoMaxRsrcProfilTable.setItems(nbrsJoursDispoMaxRsrcProfilBeans);

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        TableViews.enableFilteringOnColumns(nbrsJoursDispoMaxRsrcProfilTable, ressourceNbrsJoursDispoMaxRsrcProfilColumn, profilNbrsJoursDispoMaxRsrcProfilColumn);

        TableViews.disableReagencingColumns(nbrsJoursDispoMaxRsrcProfilTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDispoMaxRsrcProfilTable);

        // Tri de la liste :
        nbrsJoursDispoMaxRsrcProfilTable.getItems().addListener((ListChangeListener<? super NbrsJoursDispoRsrcProfilBean>) change -> {
            SortedList<NbrsJoursDispoRsrcProfilBean> sortedBeans = new SortedList<>(nbrsJoursDispoMaxRsrcProfilBeans);
            sortedBeans.comparatorProperty().bind(nbrsJoursDispoMaxRsrcProfilTable.comparatorProperty());
        });
    }


    private void synchroniserLargeurColonnes() {
        List<TableView<?>> tablesSuivantes = new ArrayList<>(tables());
        tablesSuivantes.remove(nbrsJoursOuvresTable);

        TableViews.synchronizeColumnsWidth(nbrsJoursOuvresTable, tablesSuivantes);
    }


    public void definirValeursCalendrier() {

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
/*
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de définir les valeurs du calendrier",
                    "Date d'état non définie."
            );
*/
            return;
        }

        try {

            calculerDisponibilites(dateEtat);

            // Les tables ont besoin d'être réactualisées dans certains cas, par exemple quand on change la date d'état.
            nbrsJoursOuvresTable.refresh();
            nbrsJoursDAbsenceTable.refresh();
            nbrsJoursDispoMinAgriTable.refresh();
            pctagesDispoCTTable.refresh();
            nbrsJoursDispoCTTable.refresh();
            pctagesDispoMaxRsrcProfilTable.refresh();

        } catch (IhmException e) {
            LOGGER.error("Impossible de définir les valeurs du calendrier.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de définir les valeurs du calendrier",
                    Exceptions.causes(e)
            );
        }
    }

    private void calculerDisponibilites() throws IhmException {
//        LOGGER.debug("Définition des valeurs du calendrier : ");
        LocalDate dateEtat = planChargeBean.dateEtat();
        calculerDisponibilites(dateEtat);
//        LOGGER.debug("Valeurs du calendrier définies.");
    }

    private void calculerDisponibilites(@NotNull LocalDate dateEtat) throws IhmException {
//        LOGGER.debug("Définition des valeurs du calendrier : ");
        for (int noSemaine = 1; noSemaine <= PlanChargeIhm.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerDisponibilites(noSemaine);
        }
//        LOGGER.debug("Valeurs du calendrier définies.");
    }

    private void calculerDisponibilites(int noSemaine) throws IhmException {
//        LOGGER.debug("Définition des valeurs du calendrier pour la semaine {} : ", noSemaine);
        for (RessourceHumaineBean ressourceHumaineBean : planChargeBean.getRessourcesHumainesBeans()) {
            calculerDisponibilites(ressourceHumaineBean, noSemaine);
        }
        LOGGER.debug("Valeurs du calendrier définies  pour la semaine {}.", noSemaine);
    }

    private void calculerDisponibilites(@NotNull RessourceHumaineBean ressHumBean) throws IhmException {
//        LOGGER.debug("Définition des valeurs du calendrier pour la ressource {} : ", ressHumBean);
        for (int noSemaine = 1; noSemaine <= PlanChargeIhm.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerDisponibilites(ressHumBean, noSemaine);
        }
        LOGGER.debug("Valeurs du calendrier définies pour la ressource {}.", ressHumBean);
    }

    private void calculerDisponibilites(@NotNull RessourceHumaineBean ressHumBean, int noSemaine) throws IhmException {
//        LOGGER.debug("Définition des valeurs du calendrier pour la ressource {} et la semaine n° {} : ", ressHumBean, noSemaine);

        LocalDate debutPeriode = planChargeBean.dateEtat().plusDays(7 * (noSemaine - 1)); // TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7); // TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]

        LocalDate debutMission = Objects.value(ressHumBean.debutMissionProperty(), ObjectProperty::get);
        LocalDate finMission = Objects.value(ressHumBean.finMissionProperty(), ObjectProperty::get);

        // Nbr de jours ouvrés :
        int nbrJoursOuvresPeriode;
        {
            try {
                nbrJoursOuvresPeriode = disponibilitesService.nbrJoursOuvres(debutPeriode, finPeriode);
            } catch (ServiceException e) {
                throw new IhmException("Impossible de calculer le nombre de jours ouvrés.", e);
            }
            if (!nbrsJoursOuvresBean.containsKey(debutPeriode)) {
                nbrsJoursOuvresBean.put(debutPeriode, new SimpleIntegerProperty());
            }
            IntegerProperty nbrJoursOuvresPeriodeProperty = nbrsJoursOuvresBean.get(debutPeriode);
            nbrJoursOuvresPeriodeProperty.set(nbrJoursOuvresPeriode);
        }

        // Nbr de jours d'absence :
        float nbrJoursDispoMinAgriPeriode;
        {
            NbrsJoursAbsenceBean nbrsJoursAbsenceBean = Collections.getFirst(nbrsJoursAbsenceBeans, bean -> bean.getRessourceHumaineBean().equals(ressHumBean)/*, new IhmException("Impossible de retrouver la ressource humaine '" + rsrcHumBean.getTrigramme() + "'.")*/);
            if (nbrsJoursAbsenceBean == null) {
                nbrsJoursAbsenceBean = new NbrsJoursAbsenceBean(ressHumBean);
                nbrsJoursAbsenceBeans.add(nbrsJoursAbsenceBean);
            }
            FloatProperty nbrsJoursAbsencePeriodeProperty = nbrsJoursAbsenceBean.get(debutPeriode);
            float nbrsJoursAbsencePeriode = Objects.<FloatProperty, Float>value(nbrsJoursAbsencePeriodeProperty, FloatProperty::getValue, 0f);

            // Nbr de jours de dispo pour le Ministère :
            NbrsJoursDispoRsrcBean nbrsJoursDispoMinAgriBean = Collections.fetchFirst(nbrsJoursDispoMinAgriBeans, bean -> bean.getRessourceHumaineBean().equals(ressHumBean), new IhmException("Impossible de retrouver la ressource humaine '" + ressHumBean.getTrigramme() + "'."));
            if (!nbrsJoursDispoMinAgriBean.containsKey(debutPeriode)) {
                nbrsJoursDispoMinAgriBean.put(debutPeriode, new SimpleFloatProperty());
            }
            FloatProperty nbrJoursDispoMinAgriPeriodeProperty = nbrsJoursDispoMinAgriBean.get(debutPeriode);
            // TODO FDA 2017/08 Gérer le cas où la mission commence/s'arrête en milieu de période (semaine).
            nbrJoursDispoMinAgriPeriode =
                    estHorsMission(debutPeriode, debutMission, finMission) ? 0 : Math.max(nbrJoursOuvresPeriode - nbrsJoursAbsencePeriode, 0f);
            nbrJoursDispoMinAgriPeriodeProperty.set(nbrJoursDispoMinAgriPeriode);
        }

        // % dispo CT  :
        Percentage percentageDispoCTPeriode;
        {
            PctagesDispoRsrcBean pctagesDispoCTBean = Collections.getFirst(pctagesDispoCTBeans, bean -> bean.getRessourceHumaineBean().equals(ressHumBean)/*, new IhmException("Impossible de retrouver la ressource humaine '" + ressHumBean.getTrigramme() + "'.")*/);
            if (pctagesDispoCTBean == null) {
                pctagesDispoCTBean = new PctagesDispoRsrcBean(ressHumBean);
                pctagesDispoCTBeans.add(pctagesDispoCTBean);
            }
            if (!pctagesDispoCTBean.containsKey(debutPeriode)) {
                PercentageProperty percentageDispoCTPeriodeProperty = new PercentageProperty(estHorsMission(debutPeriode, debutMission, finMission) ? 0 : DisponibilitesService.PCTAGE_DISPO_CT_MIN.floatValue());
                pctagesDispoCTBean.put(debutPeriode, percentageDispoCTPeriodeProperty);
            }
            PercentageProperty percentageDispoCTPeriodeProperty = pctagesDispoCTBean.get(debutPeriode);
            percentageDispoCTPeriode = percentageDispoCTPeriodeProperty.getValue();
        }

        // Nbr de jours de dispo pour la CT :
        float nbrJoursDispoCTPeriode;
        {
            NbrsJoursDispoRsrcBean nbrsJoursDispoCTBean = Collections.getFirst(nbrsJoursDispoCTBeans, bean -> bean.getRessourceHumaineBean().equals(ressHumBean)/*, new IhmException("Impossible de retrouver la ressource humaine '" + ressHumBean.getTrigramme() + "'.")*/);
            if (nbrsJoursDispoCTBean == null) {
                nbrsJoursDispoCTBean = new NbrsJoursDispoRsrcBean(ressHumBean);
                nbrsJoursDispoCTBeans.add(nbrsJoursDispoCTBean);
            }
            if (!nbrsJoursDispoCTBean.containsKey(debutPeriode)) {
                nbrsJoursDispoCTBean.put(debutPeriode, new SimpleFloatProperty());
            }
            nbrJoursDispoCTPeriode = (nbrJoursDispoMinAgriPeriode * percentageDispoCTPeriode.floatValue()) / 100;
            FloatProperty nbrJoursDispoCTPeriodeProperty = nbrsJoursDispoCTBean.get(debutPeriode);
            nbrJoursDispoCTPeriodeProperty.set(nbrJoursDispoCTPeriode);
        }

/* TODO FDA 2017/08 Coder.
        // Dispo. maxi. / rsrc / profil (%)
        {
            PctagesDispoRsrcProfilBean pctagesDispoMaxRsrcProfilBean = Collections.getFirst(pctagesDispoMaxRsrcProfilBeans, bean -> bean.getRessourceHumaineBean().equals(ressHumBean), new IhmException("Impossible de retrouver la ressource humaine '" + ressHumBean.getTrigramme() + "'."));
            if (pctagesDispoMaxRsrcProfilBean == null) {
                pctagesDispoMaxRsrcProfilBean = new PctagesDispoRsrcProfilBean(ressHumBean);
                pctagesDispoMaxRsrcProfilBeans.add(pctagesDispoMaxRsrcProfilBean);
            }
            if (!pctagesDispoMaxRsrcProfilBean.containsKey(debutPeriode)) {
                PercentageProperty percentageDispoMaxRsrcProfilPeriodeProperty = new PercentageProperty(0);
                pctagesDispoMaxRsrcProfilBean.put(debutPeriode, percentageDispoMaxRsrcProfilPeriodeProperty);
            }
            PercentageProperty percentageDispoMaxRsrcProfilPeriodeProperty = pctagesDispoMaxRsrcProfilBean.get(debutPeriode);
            if (estHorsMission(debutPeriode, debutMission, finMission)) {
                percentageDispoMaxRsrcProfilPeriodeProperty.setValue(new Percentage(0));
            }
            Percentage percentageDispoMaxRsrcProfilPeriode = percentageDispoMaxRsrcProfilPeriodeProperty.getValue();
        }
*/

        // FIXME FDA 2017/08 Coder les autres tables (en cascade).

//        LOGGER.debug("Valeurs du calendrier définies pour la ressource {} et la semaine n° {}.", ressHumBean, noSemaine);
    }

    private boolean estHorsMission(@NotNull LocalDate debutPeriode, @Null LocalDate debutMission, @Null LocalDate finMission) {
        return estAvantLaMission(debutPeriode, debutMission) || estApresLaMission(debutPeriode, finMission);
    }

    private boolean estAvantLaMission(@NotNull LocalDate debutPeriode, @Null LocalDate debutMission) {
        return (debutMission != null) && debutMission.isAfter(debutPeriode);
    }

    private boolean estApresLaMission(@NotNull LocalDate debutPeriode, @Null LocalDate finMission) {
        return ((finMission != null) && finMission.isBefore(debutPeriode));
    }

}
