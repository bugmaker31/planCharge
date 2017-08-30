package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.ihm.view.PercentageStringConverter;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurDisponibilites;
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
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
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

    // Les accordéons :
    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private Accordion nbrsJoursAbsenceAccordion;
    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private Accordion pctagesDispoCTAccordion;

    // Les TabbedPane :
    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TitledPane nbrsJoursAbsencePane;
    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TitledPane pctagesDispoCTPane;

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
    private TableViewAvecCalendrier<NbrsJoursAbsenceBean, Float> nbrsJoursAbsenceTable;
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

    /*
    La couche "Controller" :
     */

    //    @Autowired
    @NotNull
    private final CalculateurDisponibilites calculateurDisponibilites = new CalculateurDisponibilites();


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

/*
    @NotNull
    public TableViewAvecCalendrier<NbrsJoursOuvresBean, Integer> getNbrsJoursOuvresTable() {
        return nbrsJoursOuvresTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursAbsenceBean, Float> getNbrsJoursDAbsenceTable() {
        return nbrsJoursAbsenceTable;
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
*/

    @NotNull
    public NbrsJoursOuvresBean getNbrsJoursOuvresBean() {
        return nbrsJoursOuvresBean;
    }

    @NotNull
    public ObservableList<NbrsJoursAbsenceBean> getNbrsJoursAbsenceBeans() {
        return nbrsJoursAbsenceBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursDispoRsrcBean> getNbrsJoursDispoMinAgriBeans() {
        return nbrsJoursDispoMinAgriBeans;
    }

    @NotNull
    public ObservableList<PctagesDispoRsrcBean> getPctagesDispoCTBeans() {
        return pctagesDispoCTBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursDispoRsrcBean> getNbrsJoursDispoCTBeans() {
        return nbrsJoursDispoCTBeans;
    }

    @NotNull
    public ObservableList<PctagesDispoRsrcProfilBean> getPctagesDispoMaxRsrcProfilBeans() {
        return pctagesDispoMaxRsrcProfilBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursDispoRsrcProfilBean> getNbrsJoursDispoMaxRsrcProfilBeans() {
        return nbrsJoursDispoMaxRsrcProfilBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursDispoProfilBean> getNbrsJoursDispoMaxProfilBeans() {
        return nbrsJoursDispoMaxProfilBeans;
    }

// Méthodes :

    @FXML
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");
        calculateurDisponibilites.executerSansCalculer(() -> {
            initBeans();
            initTables();
        });
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
        initBeansNbrsJoursDispoMaxProfil();

        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {

                        ObjectProperty<LocalDate> debutMissionProperty = ressourceHumaineBean.debutMissionProperty();
                        debutMissionProperty.addListener((observable, oldValue, newValue) -> {
                            try {
                                calculateurDisponibilites.calculer(ressourceHumaineBean);
                            } catch (IhmException e) {
                                LOGGER.error("Impossible de calculer les disponibilités de la ressource " + ressourceHumaineBean.getTrigramme() + ".", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                            }
                        });

                        ObjectProperty<LocalDate> finMissionProperty = ressourceHumaineBean.finMissionProperty();
                        finMissionProperty.addListener((observable, oldValue, newValue) -> {
                            try {
                                calculateurDisponibilites.calculer(ressourceHumaineBean);
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
                            calculateurDisponibilites.calculer(ressourceHumaineBean);
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
                    if (!nbrsJoursDispoCTBeansASupprimer.isEmpty()) {
                        nbrsJoursDispoCTBeans.removeAll(nbrsJoursDispoCTBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                    }
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
                    List<PctagesDispoRsrcProfilBean> pctagesDispoMaxRsrcProfilBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        pctagesDispoMaxRsrcProfilBeans.parallelStream()
                                .forEach(pctagesDispoMaxRsrcProfilBean -> {
                                    if (pctagesDispoMaxRsrcProfilBean.getRessourceHumaineBean().equals(ressourceHumaineBean)) {
                                        pctagesDispoMaxRsrcProfilBeansASupprimer.add(pctagesDispoMaxRsrcProfilBean);
                                    }
                                });
                    }
                    if (!pctagesDispoMaxRsrcProfilBeansASupprimer.isEmpty()) {
                        pctagesDispoMaxRsrcProfilBeans.removeAll(pctagesDispoMaxRsrcProfilBeansASupprimer);
                    }
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
                                calculateurDisponibilites.calculer(ressourceHumaineBean);
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

    @SuppressWarnings("OverlyComplexMethod")
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
                    List<NbrsJoursDispoRsrcProfilBean> nbrsJoursDispoMaxRsrcProfilBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        nbrsJoursDispoMaxRsrcProfilBeans.parallelStream()
                                .forEach(nbrsJoursDispoMaxRsrcProfilBean -> {
                                    if (nbrsJoursDispoMaxRsrcProfilBean.getRessourceHumaineBean().equals(ressourceHumaineBean)) {
                                        nbrsJoursDispoMaxRsrcProfilBeansASupprimer.add(nbrsJoursDispoMaxRsrcProfilBean);
                                    }
                                });
                    }
                    if (!nbrsJoursDispoMaxRsrcProfilBeansASupprimer.isEmpty()) {
                        nbrsJoursDispoMaxRsrcProfilBeans.removeAll(nbrsJoursDispoMaxRsrcProfilBeansASupprimer);
                    }
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
                                calculateurDisponibilites.calculer(ressourceHumaineBean);
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

    private void initBeansNbrsJoursDispoMaxProfil() {
        planChargeBean.getProfilsBeans().addListener((ListChangeListener<? super ProfilBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoProfilBean> nbrsJoursDispoMaxProfilBeansAAjouter = new ArrayList<>();
                    for (ProfilBean profilBean : change.getAddedSubList()) {
                        if (nbrsJoursDispoMaxProfilBeans.parallelStream().anyMatch(nbrsJoursDispoProfilBean -> nbrsJoursDispoProfilBean.getProfilBean().equals(profilBean))) {
                            continue;
                        }
                        Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                        nbrsJoursDispoMaxProfilBeansAAjouter.add(new NbrsJoursDispoProfilBean(profilBean, calendrier));
                    }
                    nbrsJoursDispoMaxProfilBeans.addAll(nbrsJoursDispoMaxProfilBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursDispoProfilBean> nbrsJoursDispoCTBeansASupprimer = new ArrayList<>();
                    for (ProfilBean profilBean : change.getRemoved()) {
                        nbrsJoursDispoMaxProfilBeans.parallelStream()
                                .forEach(dispoRsrcProfilBean -> {
                                    if (dispoRsrcProfilBean.getProfilBean().equals(profilBean)) {
                                        nbrsJoursDispoCTBeansASupprimer.add(dispoRsrcProfilBean);
                                    }
                                });
                    }
                    nbrsJoursDispoMaxProfilBeans.removeAll(nbrsJoursDispoCTBeansASupprimer);
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }


    public List<TableViewAvecCalendrier<?, ?>> tables() {
        return Arrays.asList(new TableViewAvecCalendrier<?, ?>[]{
                nbrsJoursOuvresTable,
                nbrsJoursAbsenceTable,
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
        initTablePctagesDispoCT();
        initTableNbrsJoursDispoCT();
        initTablePctagesDispoMaxRsrcProfil();
        initTableNbrsJoursDispoMaxRsrcProfil();
        initTableNbrsJoursDispoMaxProfil();

        synchroniserLargeurColonnes();
    }

    // Cells :

    private static abstract class AbstractDispoRrsrcCell<S extends AbstractDisponibilitesRessourceBean, T> extends EditableAwareTextFieldTableCell<S, T> {

        private PlanChargeBean planChargeBean;
        private int noSemaine;

        AbstractDispoRrsrcCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
            super(stringConverter, cantEditErrorDisplayer);
            this.planChargeBean = planChargeBean;
            this.noSemaine = noSemaine;
        }

        AbstractDispoRrsrcCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter) {
            this(planChargeBean, noSemaine, stringConverter, null);
        }

        PlanChargeBean getPlanChargeBean() {
            return planChargeBean;
        }

        int getNoSemaine() {
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

    private static abstract class AbstractDispoProfilCell<S extends AbstractDisponibilitesProfilBean, T> extends EditableAwareTextFieldTableCell<S, T> {

        private PlanChargeBean planChargeBean;
        private int noSemaine;

        AbstractDispoProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
            super(stringConverter, cantEditErrorDisplayer);
            this.planChargeBean = planChargeBean;
            this.noSemaine = noSemaine;
        }

        AbstractDispoProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter) {
            this(planChargeBean, noSemaine, stringConverter, null);
        }

        PlanChargeBean getPlanChargeBean() {
            return planChargeBean;
        }

        int getNoSemaine() {
            return noSemaine;
        }

/* Rien de spécial... pour l'instant.
        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            styler();
        }

        private void styler() {

            // Réinit des style spécifiques :
            getStyleClass().removeAll("avantMission", "pendantMission", "apresMission");

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
*/

    }

    private final class FractionsJoursDispoRsrcCell<T extends AbstractDisponibilitesRessourceBean<AbstractDTO, T, FloatProperty>> extends AbstractDispoRrsrcCell<T, Float> {

        FractionsJoursDispoRsrcCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
            super(planChargeBean, noSemaine, HUITIEMES_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
        }

        FractionsJoursDispoRsrcCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
            this(planChargeBean, noSemaine, null);
        }

        @Override
        public void commitEdit(Float newValue) {
            super.commitEdit(newValue);

            //noinspection unchecked
            TableRow<T> tableRow = getTableRow();
            T nbrJoursAbsenceBean = tableRow.getItem();
            if (nbrJoursAbsenceBean == null) {
                return;
            }
            if (getPlanChargeBean().getDateEtat() == null) {
                LOGGER.warn("Date d'état non définie !?");
                return;
            }
            LocalDate debutPeriode = getPlanChargeBean().getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            if (!nbrJoursAbsenceBean.containsKey(debutPeriode)) {
                nbrJoursAbsenceBean.put(debutPeriode, new SimpleFloatProperty());
            }
            FloatProperty nbrJoursDAbsencePeriode = nbrJoursAbsenceBean.get(debutPeriode);
            nbrJoursDAbsencePeriode.set(newValue);

            try {
                calculateurDisponibilites.calculer(nbrJoursAbsenceBean.getRessourceHumaineBean(), getNoSemaine());
            } catch (IhmException e) {
                // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                LOGGER.error("Impossible de màj les disponibilités.", e);
            }
        }
    }

    private final class PctagesDispoRsrcCell<T extends AbstractDisponibilitesRessourceBean<AbstractDTO, T, PercentageProperty>> extends AbstractDispoRrsrcCell<PctagesDispoRsrcBean, Percentage> {

        PctagesDispoRsrcCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
            super(planChargeBean, noSemaine, new PercentageStringConverter(), cantEditErrorDisplayer);
        }

        PctagesDispoRsrcCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
            this(planChargeBean, noSemaine, null);
        }

        @Override
        public void commitEdit(Percentage newValue) {
            super.commitEdit(newValue);

            //noinspection unchecked
            TableRow<T> tableRow = getTableRow();
            T pctagesDispoCTBean = tableRow.getItem();
            if (pctagesDispoCTBean == null) {
                return;
            }
            if (getPlanChargeBean().getDateEtat() == null) {
                LOGGER.warn("Date d'état non définie !?");
                return;
            }
            LocalDate debutPeriode = getPlanChargeBean().getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            if (!pctagesDispoCTBean.containsKey(debutPeriode)) {
                pctagesDispoCTBean.put(debutPeriode, new PercentageProperty(DisponibilitesService.PCTAGE_DISPO_RSRC_DEFAUT.floatValue()));
            }
            PercentageProperty pctageDispoCTPeriodeProperty = pctagesDispoCTBean.get(debutPeriode);
            pctageDispoCTPeriodeProperty.setValue(newValue);

            try {
                calculateurDisponibilites.calculer(pctagesDispoCTBean.getRessourceHumaineBean(), getNoSemaine());
            } catch (IhmException e) {
                // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                LOGGER.error("Impossible de màj les disponibilités.", e);
            }
        }
    }

    private final class PctagesDispoRsrcProfilCell<T extends AbstractDisponibilitesRessourceProfilBean<AbstractDTO, T, PercentageProperty>> extends AbstractDispoRrsrcCell<PctagesDispoRsrcProfilBean, Percentage> {

        PctagesDispoRsrcProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
            super(planChargeBean, noSemaine, new PercentageStringConverter(), cantEditErrorDisplayer);
        }

        PctagesDispoRsrcProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
            this(planChargeBean, noSemaine, null);
        }

        @Override
        public void commitEdit(Percentage newValue) {
            super.commitEdit(newValue);

            //noinspection unchecked
            TableRow<T> tableRow = getTableRow();
            T pctagesDispoRsrcProfilBean = tableRow.getItem();
            if (pctagesDispoRsrcProfilBean == null) {
                return;
            }
            if (getPlanChargeBean().getDateEtat() == null) {
                LOGGER.warn("Date d'état non définie !?");
                return;
            }
            LocalDate debutPeriode = getPlanChargeBean().getDateEtat().plusDays((getNoSemaine() - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            if (!pctagesDispoRsrcProfilBean.containsKey(debutPeriode)) {
                pctagesDispoRsrcProfilBean.put(debutPeriode, new PercentageProperty(DisponibilitesService.PCTAGE_DISPO_RSRC_DEFAUT.floatValue()));
            }
            PercentageProperty pctageDispoCTPeriodeProperty = pctagesDispoRsrcProfilBean.get(debutPeriode);
            pctageDispoCTPeriodeProperty.setValue(newValue);

            try {
                calculateurDisponibilites.calculer(pctagesDispoRsrcProfilBean.getRessourceHumaineBean(), getNoSemaine());
            } catch (IhmException e) {
                // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                LOGGER.error("Impossible de màj les disponibilités.", e);
            }
        }
    }

    private final class FractionsJoursDispoRsrcProfilCell<T extends AbstractDisponibilitesRessourceProfilBean<AbstractDTO, T, FloatProperty>> extends AbstractDispoRrsrcCell<T, Float> {

        FractionsJoursDispoRsrcProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
            super(planChargeBean, noSemaine, HUITIEMES_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
        }

        FractionsJoursDispoRsrcProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
            this(planChargeBean, noSemaine, null);
        }

    }

    private final class FractionsJoursDispoProfilCell<T extends AbstractDisponibilitesProfilBean<AbstractDTO, T, FloatProperty>> extends AbstractDispoProfilCell<NbrsJoursDispoProfilBean, Float> {

        FractionsJoursDispoProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
            super(planChargeBean, noSemaine, HUITIEMES_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
        }

        FractionsJoursDispoProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
            this(planChargeBean, noSemaine, null);
        }

    }

    // Callbacks :

    private static final class NbrJoursCellCallback<T extends AbstractDisponibilitesBean<AbstractDTO, T, IntegerProperty>> implements Callback<CellDataFeatures<T, Integer>, ObservableValue<Integer>> {

        private PlanChargeBean planChargeBean;
        private final int noSemaine;

        NbrJoursCellCallback(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
            super();
            this.planChargeBean = planChargeBean;
            this.noSemaine = noSemaine;
        }

        @Null
        @Override
        public ObservableValue<Integer> call(CellDataFeatures<T, Integer> cell) {
            if (cell == null) {
                return null;
            }
            if (planChargeBean.getDateEtat() == null) {
                LOGGER.warn("Date d'état non définie !?");
                return null;
            }
            T nbrsJoursPeriodeBean = cell.getValue();
            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            if (!nbrsJoursPeriodeBean.containsKey(debutPeriode)) {
                nbrsJoursPeriodeBean.put(debutPeriode, new SimpleIntegerProperty());
            }
            IntegerProperty nbrJoursPeriodeProperty = nbrsJoursPeriodeBean.get(debutPeriode);
            return nbrJoursPeriodeProperty.asObject();
        }
    }

    private static final class FractionsJoursCellCallback<T extends AbstractDisponibilitesBean<AbstractDTO, T, FloatProperty>> implements Callback<CellDataFeatures<T, Float>, ObservableValue<Float>> {

        private PlanChargeBean planChargeBean;
        private final int noSemaine;

        FractionsJoursCellCallback(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
            super();
            this.planChargeBean = planChargeBean;
            this.noSemaine = noSemaine;
        }

        @Null
        @Override
        public ObservableValue<Float> call(CellDataFeatures<T, Float> cell) {
            if (cell == null) {
                return null;
            }
            if (planChargeBean.getDateEtat() == null) {
                LOGGER.warn("Date d'état non définie !?");
                return null;
            }
            T fractionJoursPeriodeBean = cell.getValue();
            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            if (!fractionJoursPeriodeBean.containsKey(debutPeriode)) {
                fractionJoursPeriodeBean.put(debutPeriode, new SimpleFloatProperty());
            }
            FloatProperty fractionJourPeriodeProperty = fractionJoursPeriodeBean.get(debutPeriode);
            return fractionJourPeriodeProperty.asObject();
        }
    }

    private static final class PctagesDispoCallback<T extends AbstractDisponibilitesBean<AbstractDTO, T, PercentageProperty>> implements Callback<CellDataFeatures<T, Percentage>, ObservableValue<Percentage>> {

        private PlanChargeBean planChargeBean;
        private final int noSemaine;

        private PctagesDispoCallback(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
            super();
            this.planChargeBean = planChargeBean;
            this.noSemaine = noSemaine;
        }

        @Null
        @Override
        public ObservableValue<Percentage> call(CellDataFeatures<T, Percentage> cell) {
            if (cell == null) {
                return null;
            }
            T pctagesDispoBean = cell.getValue();
            if (planChargeBean.getDateEtat() == null) {
                LOGGER.warn("Date d'état non définie !?");
                return null;
            }
            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            if (!pctagesDispoBean.containsKey(debutPeriode)) {
                pctagesDispoBean.put(debutPeriode, new PercentageProperty(0));
            }
            PercentageProperty pctageDispoProperty = pctagesDispoBean.get(debutPeriode);
            return pctageDispoProperty;
        }
    }

    // Init des tables :

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
            int cptColonne = 0;
            for (TableColumn<NbrsJoursOuvresBean, Integer> nbrsJoursOuvresColumn : nbrsJoursOuvresTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursCellCallback<>(planChargeBean, cptColonne));
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
        nbrsJoursOuvresTable.getItems().addListener((ListChangeListener<? super NbrsJoursOuvresBean>) change -> {
            SortedList<NbrsJoursOuvresBean> sortedBeans = new SortedList<>(nbrsJoursOuvresBeans);
            sortedBeans.comparatorProperty().bind(nbrsJoursOuvresTable.comparatorProperty());
            nbrsJoursOuvresTable.setItems(sortedBeans);
        });

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (1 seule ligne).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursOuvresTable.setItems(nbrsJoursOuvresBeans);

        TableViews.disableReagencingColumns(nbrsJoursOuvresTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursOuvresTable);
    }

    private void initTableNbrsJoursAbsence() {

        nbrsJoursAbsenceTable.setCalendrierColumns(
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
        //noinspection HardcodedFileSeparator
        profilNbrsJoursAbsenceColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursAbsenceBean, Float> nbrsJoursDAbsenceColumn : nbrsJoursAbsenceTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDAbsenceColumn.setCellValueFactory(new FractionsJoursCellCallback<>(planChargeBean, cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursAbsenceColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursAbsenceColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursAbsenceBean, Float> nbrsJoursDAbsenceColumn : nbrsJoursAbsenceTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                nbrsJoursDAbsenceColumn.setCellFactory(param -> new FractionsJoursDispoRsrcCell<>(planChargeBean, finalCptColonne));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursAbsenceTable);


        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursAbsenceTable.setItems(nbrsJoursAbsenceBeans);

        TableViews.disableReagencingColumns(nbrsJoursAbsenceTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursAbsenceTable);

        // Définition du menu contextuel :
        ContextMenu menuCtxt = new ContextMenu();
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
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoMinAgriColumn : nbrsJoursDispoMinAgriTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoMinAgriColumn.setCellValueFactory(new FractionsJoursCellCallback<>(planChargeBean, cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoMinAgriColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoMinAgriColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoMinAgriColumn : nbrsJoursDispoMinAgriTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                nbrsJoursDispoMinAgriColumn.setCellFactory(cell -> new FractionsJoursDispoRsrcCell<>(planChargeBean, finalCptColonne, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité au Ministère est calculé à partir des jours ouvrés et d'absence.")));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoMinAgriTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoMinAgriTable.setItems(nbrsJoursDispoMinAgriBeans);

        TableViews.disableReagencingColumns(nbrsJoursDispoMinAgriTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursDispoMinAgriTable);
    }

    private void initTablePctagesDispoCT() {

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
            int cptColonne = 0;
            for (TableColumn<PctagesDispoRsrcBean, Percentage> pctagesDispoMinAgriColumn : pctagesDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                pctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoCallback<>(planChargeBean, cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourcePctagesDispoCTColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilPctagesDispoCTColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            int cptColonne = 0;
            for (TableColumn<PctagesDispoRsrcBean, Percentage> pctagesDispoCTColumn : pctagesDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                pctagesDispoCTColumn.setCellFactory(cell -> new PctagesDispoRsrcCell<>(planChargeBean, finalCptColonne));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(pctagesDispoCTTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        pctagesDispoCTTable.setItems(pctagesDispoCTBeans);

        TableViews.disableReagencingColumns(pctagesDispoCTTable);
        TableViews.ensureDisplayingAllRows(pctagesDispoCTTable);
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
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoCTColumn : nbrsJoursDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoCTColumn.setCellValueFactory(new FractionsJoursCellCallback<>(planChargeBean, cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoCTColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoCTColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        int cptColonne = 0;
        for (TableColumn<NbrsJoursDispoRsrcBean, Float> nbrsJoursDispoCTColumn : nbrsJoursDispoCTTable.getCalendrierColumns()) {
            cptColonne++;
            int finalCptColonne = cptColonne;
            nbrsJoursDispoCTColumn.setCellFactory(cell -> new FractionsJoursDispoRsrcCell<>(planChargeBean, finalCptColonne, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité à la CT est calculé à partir des pourcentages de dispo pour la CT.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoCTTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoCTTable.setItems(nbrsJoursDispoCTBeans);

        TableViews.disableReagencingColumns(nbrsJoursDispoCTTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursDispoCTTable);
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
            int cptColonne = 0;
            for (TableColumn<PctagesDispoRsrcProfilBean, Percentage> pctagesDispoMaxRsrcProfilColumn : pctagesDispoMaxRsrcProfilTable.getCalendrierColumns()) {
                cptColonne++;
                pctagesDispoMaxRsrcProfilColumn.setCellValueFactory(new PctagesDispoCallback<>(planChargeBean, cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourcePctagesDispoMaxRsrcProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilPctagesDispoMaxRsrcProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            int cptColonne = 0;
            for (TableColumn<PctagesDispoRsrcProfilBean, Percentage> pctagesDispoMaxRsrcProfilColumn : pctagesDispoMaxRsrcProfilTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                pctagesDispoMaxRsrcProfilColumn.setCellFactory(cell -> new PctagesDispoRsrcProfilCell<PctagesDispoRsrcProfilBean>(planChargeBean, finalCptColonne));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(pctagesDispoMaxRsrcProfilTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        pctagesDispoMaxRsrcProfilTable.setItems(pctagesDispoMaxRsrcProfilBeans);

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        TableViews.enableFilteringOnColumns(pctagesDispoMaxRsrcProfilTable, ressourcePctagesDispoMaxRsrcProfilColumn, profilPctagesDispoMaxRsrcProfilColumn);

        TableViews.disableReagencingColumns(pctagesDispoMaxRsrcProfilTable);
        TableViews.ensureDisplayingAllRows(pctagesDispoMaxRsrcProfilTable);
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
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoRsrcProfilBean, Float> nbrsJoursDispoMaxRsrcProfilColumn : nbrsJoursDispoMaxRsrcProfilTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoMaxRsrcProfilColumn.setCellValueFactory(new FractionsJoursCellCallback<>(planChargeBean, cptColonne));
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
                nbrsJoursDispoMaxRsrcProfilColumn.setCellFactory(cell -> new FractionsJoursDispoRsrcProfilCell<>(planChargeBean, finalCptColonne, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité max. par ressource et par profil est calculé à partir des pourcentages.")));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoMaxRsrcProfilTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoMaxRsrcProfilTable.setItems(nbrsJoursDispoMaxRsrcProfilBeans);

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        TableViews.enableFilteringOnColumns(nbrsJoursDispoMaxRsrcProfilTable, ressourceNbrsJoursDispoMaxRsrcProfilColumn, profilNbrsJoursDispoMaxRsrcProfilColumn);

        TableViews.disableReagencingColumns(nbrsJoursDispoMaxRsrcProfilTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursDispoMaxRsrcProfilTable);

    }

    private void initTableNbrsJoursDispoMaxProfil() {
        nbrsJoursDispoMaxProfilTable.setCalendrierColumns(
                semaine1NbrsJoursDispoMaxProfilColumn,
                semaine2NbrsJoursDispoMaxProfilColumn,
                semaine3NbrsJoursDispoMaxProfilColumn,
                semaine4NbrsJoursDispoMaxProfilColumn,
                semaine5NbrsJoursDispoMaxProfilColumn,
                semaine6NbrsJoursDispoMaxProfilColumn,
                semaine7NbrsJoursDispoMaxProfilColumn,
                semaine8NbrsJoursDispoMaxProfilColumn,
                semaine9NbrsJoursDispoMaxProfilColumn,
                semaine10NbrsJoursDispoMaxProfilColumn,
                semaine11NbrsJoursDispoMaxProfilColumn,
                semaine12NbrsJoursDispoMaxProfilColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursDispoMaxProfilColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        profilNbrsJoursDispoMaxProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoProfilBean, Float> nbrsJoursDispoMaxProfilColumn : nbrsJoursDispoMaxProfilTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                nbrsJoursDispoMaxProfilColumn.setCellValueFactory(new FractionsJoursCellCallback<>(planChargeBean, finalCptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoMaxProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoMaxProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoProfilBean, Float> nbrsJoursDispoMaxProfilColumn : nbrsJoursDispoMaxProfilTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                nbrsJoursDispoMaxProfilColumn.setCellFactory(cell -> new FractionsJoursDispoProfilCell<>(planChargeBean, finalCptColonne, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité max. par profil est calculé à partir des pourcentages.")));
            }
        }


        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoMaxProfilTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoMaxProfilTable.setItems(nbrsJoursDispoMaxProfilBeans);

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran.

        TableViews.disableReagencingColumns(nbrsJoursDispoMaxProfilTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursDispoMaxProfilTable);
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

            calculateurDisponibilites.calculer();

            // Les tables ont besoin d'être réactualisées dans certains cas, par exemple quand on change la date d'état.
            nbrsJoursOuvresTable.refresh();
            nbrsJoursAbsenceTable.refresh();
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


    public void calculerDisponibilites() throws ControllerException {
//        LOGGER.debug("Calcul des disponibilités  : ");
        calculateurDisponibilites.calculer();
//        LOGGER.debug("Disponibilités calculées.");
    }


    @FXML
    private <B extends AbstractDisponibilitesRessourceBean> void voirNbrJoursAbsence(@NotNull ActionEvent actionEvent) {
        TableView<B> table = tableDuMenuContextuel(actionEvent);
        B nbrsJoursDispoMinAgriSelectedBean = TableViews.<B>selectedItem(table);
        if (nbrsJoursDispoMinAgriSelectedBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher les nombres de jours d'absence pour la ressource",
                    "Aucune ligne n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }
        try {
            RessourceHumaineBean ressourceHumaineBean = nbrsJoursDispoMinAgriSelectedBean.getRessourceHumaineBean();
            NbrsJoursAbsenceBean nbrsJoursAbsenceBean = Collections.any(nbrsJoursAbsenceBeans, (NbrsJoursAbsenceBean bean) -> bean.getRessourceHumaineBean().equals(ressourceHumaineBean), new IhmException("Impossible de retrouver la ressource '" + ressourceHumaineBean.getTrigramme() + "' dans la table des nombres de jours d'absence."));
            nbrsJoursAbsenceAccordion.setExpandedPane(nbrsJoursAbsencePane);
            TableViews.focusOnItem(nbrsJoursAbsenceTable, nbrsJoursAbsenceBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher les nombres de jours d'absence pour la ressource.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher les nombres de jours d'absence pour la ressource",
                    Exceptions.causes(e)
            );
        }
    }

    @FXML
    private <B extends AbstractDisponibilitesRessourceBean> void voirPctagesDispoCT(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        TableView<B> table = (TableView<B>) nbrsJoursDispoCTTable;
        B nbrsJoursDispoCTBean = TableViews.selectedItem(table);
        if (nbrsJoursDispoCTBean == null) {
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher les pourcentages de disponiblité pour l'équipe (la CT) de la ressource",
                    "Aucune ligne n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }
        try {
            RessourceHumaineBean ressourceHumaineBean = nbrsJoursDispoCTBean.getRessourceHumaineBean();
            PctagesDispoRsrcBean pctagesDispoCTBean = Collections.any(pctagesDispoCTBeans, (PctagesDispoRsrcBean bean) -> bean.getRessourceHumaineBean().equals(ressourceHumaineBean), new IhmException("Impossible de retrouver la ressource '" + ressourceHumaineBean.getTrigramme() + "' dans la table des pourcentages de disponibilité pour l'équipe (la CT)."));
            nbrsJoursAbsenceAccordion.setExpandedPane(pctagesDispoCTPane);
            TableViews.focusOnItem(pctagesDispoCTTable, pctagesDispoCTBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher les pourcentages de disponiblité pour l'équipe (la CT) de la ressource.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher les pourcentages de disponiblité pour l'équipe (la CT) de la ressource",
                    Exceptions.causes(e)
            );
        }
    }

    @NotNull
    private <B extends AbstractDisponibilitesRessourceBean> TableView<B> tableDuMenuContextuel(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        //noinspection unchecked
        TableView<B> table = (TableView<B>) menuItem.getUserData();
        return table;
    }


}
