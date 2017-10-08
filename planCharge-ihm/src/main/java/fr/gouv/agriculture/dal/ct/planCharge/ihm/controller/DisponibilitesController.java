package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.util.ObservableLists;
import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurDisponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.ProfilDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.DisponibilitesService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithTooManyFields"})
public class DisponibilitesController extends AbstractController implements Module {

    public static final PseudoClass AVANT_MISSION = PseudoClass.getPseudoClass("avantMission");
    public static final PseudoClass PENDANT_MISSION = PseudoClass.getPseudoClass("pendantMission");
    public static final PseudoClass APRES_MISSION = PseudoClass.getPseudoClass("apresMission");


    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibilitesController.class);


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
    private final CalculateurDisponibilites calculateurDisponibilites = CalculateurDisponibilites.instance(); /*new CalculateurDisponibilites();*/


    // Constructeurs :

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public DisponibilitesController() throws ControllerException {
        super();
        if (instance != null) {
            throw new ControllerException("Instanciation à plus d'1 exemplaire.");
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


    // Module

    @Override
    public String getTitre() {
        return "Disponibilités";
    }


    @FXML
    protected void initialize() throws ControllerException {
        LOGGER.debug("Initialisation...");
        try {
            Calculateur.executerSansCalculer(() -> {
                initBeans();
                initTables();
            });
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur.", e);
        }
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
            Set<RessourceHumaineBean> ressourcesHumainesARecalculer = new HashSet<>(10);
            while (change.next()) {
                if (change.wasAdded()) {
                    ressourcesHumainesARecalculer.addAll(change.getAddedSubList());
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
            if (!ressourcesHumainesARecalculer.isEmpty()) {
                for (RessourceHumaineBean ressourceHumaineBean : ressourcesHumainesARecalculer) {

                    ObjectProperty<LocalDate> debutMissionProperty = ressourceHumaineBean.debutMissionProperty();
                    debutMissionProperty.addListener((observable, oldValue, newValue) -> {
                        try {
                            calculateurDisponibilites.calculer(ressourceHumaineBean);
                        } catch (ControllerException e) {
                            //noinspection DuplicateStringLiteralInspection
                            LOGGER.error("Impossible de calculer les disponibilités de la ressource " + ressourceHumaineBean.getTrigramme() + " en fonction de sa date de début de mission.", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        }
                    });

                    ObjectProperty<LocalDate> finMissionProperty = ressourceHumaineBean.finMissionProperty();
                    finMissionProperty.addListener((observable, oldValue, newValue) -> {
                        try {
                            calculateurDisponibilites.calculer(ressourceHumaineBean);
                        } catch (ControllerException e) {
                            //noinspection DuplicateStringLiteralInspection
                            LOGGER.error("Impossible de calculer les disponibilités de la ressource " + ressourceHumaineBean.getTrigramme() + " en fonction de sa date de fin de mission.", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        }
                    });
                }
            }
        });
    }

    private void initBeansNbrsJoursOuvres() {

        planChargeBean.getJoursFeriesBeans().addListener((ListChangeListener<? super JourFerieBean>) change -> {
            boolean estARecalculer = false;
            while (change.next() /*&& !estARecalculer -> Non, pour consommer tous les changements./*/) {
                if (!change.wasPermutated()) {
                    estARecalculer = true;
                }
            }
            if (estARecalculer) {
                try {
                    calculerDisponibilites();
                } catch (ControllerException e) {
                    LOGGER.error("Impossible de calculer les disponibilités.", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                }
            }
        });
    }

    private void initBeansNbrsJoursAbsence() {
        ObservableLists.Binding binding = new ObservableLists.Binding("ressourcesHumainesBeans", "nbrsJoursAbsenceBeans");
        binding.ensureContains(
                planChargeBean.getRessourcesHumainesBeans(),
                NbrsJoursAbsenceBean::new,
                nbrsJoursAbsenceBeans
        );
    }

    private void initBeansNbrsJoursDispoMinAgri() {
        ObservableLists.Binding binding = new ObservableLists.Binding("ressourcesHumainesBeans", "nbrsJoursDispoMinAgriBeans");
        binding.ensureContains(
                planChargeBean.getRessourcesHumainesBeans(),
                NbrsJoursDispoRsrcBean::new,
                nbrsJoursDispoMinAgriBeans
        );
    }

    private void initBeansPctagesDispoCT() {
        ObservableLists.Binding binding = new ObservableLists.Binding("ressourcesHumainesBeans", "pctagesDispoCTBeans");
        binding.ensureContains(
                planChargeBean.getRessourcesHumainesBeans(),
                PctagesDispoRsrcBean::new,
                pctagesDispoCTBeans
        );

        pctagesDispoCTBeans.addListener((ListChangeListener<? super PctagesDispoRsrcBean>) change -> {
            List<RessourceHumaineBean> ressourcesHumainesARecalculer = new ArrayList<>(10);
            while (change.next()) {
                //noinspection UnclearExpression
                List<? extends PctagesDispoRsrcBean> pctagesDispoRsrcBeansModifies =
                        change.wasAdded() ? change.getAddedSubList() :
                                change.wasRemoved() ? change.getRemoved() :
                                        null;
                if (pctagesDispoRsrcBeansModifies != null) {
                    ressourcesHumainesARecalculer.addAll(
                            pctagesDispoRsrcBeansModifies.parallelStream()
                                    .map(AbstractCalendrierParRessourceBean::getRessourceBean)
                                    .collect(Collectors.toList())
                    );
                }
            }
            if (!ressourcesHumainesARecalculer.isEmpty()) {
                for (RessourceHumaineBean ressourceHumaineBean : ressourcesHumainesARecalculer) {
                    try {
                        calculateurDisponibilites.calculer(ressourceHumaineBean);
                    } catch (ControllerException e) {
                        LOGGER.error("Impossible de calculer les disponibilités de la ressource " + ressourceHumaineBean.getTrigramme() + ".", e); // TODO FDA 2017/08 Trouevr mieux que juste loguer une erreur.
                    }
                }
            }
        });
    }

    private void initBeansNbrsJoursDispoCT() {
        ObservableLists.Binding binding = new ObservableLists.Binding("ressourcesHumainesBeans", "nbrsJoursDispoCTBeans");
        binding.ensureContains(
                planChargeBean.getRessourcesHumainesBeans(),
                NbrsJoursDispoRsrcBean::new,
                nbrsJoursDispoCTBeans
        );
    }

    private void initBeansPctagesDispoMaxRsrcProfil() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<PctagesDispoRsrcProfilBean> pctagesDispoMaxRsrcProfilBeansAAjouter = new ArrayList<>(10);
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (pctagesDispoMaxRsrcProfilBeans.parallelStream().anyMatch(pctagesDispoMaxRsrcProfilBean -> pctagesDispoMaxRsrcProfilBean.getRessourceBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        try {
                            for (ProfilDTO profilDTO : referentielsService.profils()) {
                                pctagesDispoMaxRsrcProfilBeansAAjouter.add(new PctagesDispoRsrcProfilBean(ressourceHumaineBean, ProfilBean.from(profilDTO)));
                            }
                        } catch (BeanException | ServiceException e) {
                            // TODO FDA 2017/08 Trouver mieux que juste loguer l'erreur.
                            LOGGER.error("Impossible d'initialiser les pourcentages de disponibilités max. par ressource et profil.", e);
                        }
                    }
                    if (!pctagesDispoMaxRsrcProfilBeansAAjouter.isEmpty()) {
                        pctagesDispoMaxRsrcProfilBeans.addAll(pctagesDispoMaxRsrcProfilBeansAAjouter);
                    }
                }
                if (change.wasRemoved()) {
                    List<PctagesDispoRsrcProfilBean> pctagesDispoMaxRsrcProfilBeansASupprimer = new ArrayList<>(10);
                    for (RessourceBean<?, ?> ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        pctagesDispoMaxRsrcProfilBeans.parallelStream()
                                .forEach(pctagesDispoMaxRsrcProfilBean -> {
                                    if (pctagesDispoMaxRsrcProfilBean.getRessourceBean().equals(ressourceHumaineBean)) {
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
            Set<RessourceHumaineBean> ressourcesHumainesARecalculer = new HashSet<>(10);
            while (change.next()) {
                if (change.wasAdded()) {
                    for (PctagesDispoRsrcProfilBean pctagesDispoMaxRsrcProfilBean : change.getAddedSubList()) {
                        RessourceHumaineBean ressourceHumaineBean = pctagesDispoMaxRsrcProfilBean.getRessourceBean();
                        ressourcesHumainesARecalculer.add(ressourceHumaineBean);
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
            if (!ressourcesHumainesARecalculer.isEmpty()) {
                for (RessourceHumaineBean ressourceHumaineARecalculer : ressourcesHumainesARecalculer) {
                    try {
                        calculateurDisponibilites.calculer(ressourceHumaineARecalculer);
                    } catch (ControllerException e) {
                        LOGGER.error("Impossible de calculer les disponibilités max de la ressource " + ressourceHumaineARecalculer.getTrigramme() + " par profil.", e); // TODO FDA 2017/08 Trouevr mieux que juste loguer une erreur.
                    }
                }
            }
        });
    }

    @SuppressWarnings({"OverlyComplexMethod", "MethodWithMoreThanThreeNegations"})
    private void initBeansNbrsJoursDispoMaxRsrcProfil() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoRsrcProfilBean> nbrsJoursDispoMaxRsrcProfilBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (nbrsJoursDispoMaxRsrcProfilBeans.parallelStream().anyMatch(nbrsJoursDispoMaxRsrcProfilBean -> nbrsJoursDispoMaxRsrcProfilBean.getRessourceBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        try {
                            for (ProfilDTO profilDTO : referentielsService.profils()) {
                                nbrsJoursDispoMaxRsrcProfilBeansAAjouter.add(new NbrsJoursDispoRsrcProfilBean(ressourceHumaineBean, ProfilBean.from(profilDTO)));
                            }
                        } catch (BeanException | ServiceException e) {
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
                    for (RessourceBean<?, ?> ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        nbrsJoursDispoMaxRsrcProfilBeans.parallelStream()
                                .forEach(nbrsJoursDispoMaxRsrcProfilBean -> {
                                    if (nbrsJoursDispoMaxRsrcProfilBean.getRessourceBean().equals(ressourceHumaineBean)) {
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
            Set<RessourceHumaineBean> ressourcesHumainesARecalculer = new HashSet<>(10);
            boolean aRecalculerEnTotalite = false;
            while (change.next()) {
                if (change.wasAdded()) {
                    ressourcesHumainesARecalculer.addAll(
                            change.getAddedSubList().parallelStream()
                                    .map(AbstractCalendrierParRessourceBean::getRessourceBean)
                                    .collect(Collectors.toList())
                    );
                }
                if (change.wasRemoved()) {
                    aRecalculerEnTotalite = true;
                }
                // TODO FDA 2017/08 Coder les autres changements (suppression, permutations, etc. ?).
            }
            if (aRecalculerEnTotalite) {
                try {
                    calculateurDisponibilites.calculer();
                } catch (ControllerException e) {
                    LOGGER.error("Impossible de calculer les disponibilités.", e); // TODO FDA 2017/08 Trouevr mieux que juste loguer une erreur.
                }
            } else {
                for (RessourceHumaineBean ressourceHumaineARecalculer : ressourcesHumainesARecalculer) {
                    try {
                        calculateurDisponibilites.calculer(ressourceHumaineARecalculer);
                    } catch (ControllerException e) {
                        LOGGER.error("Impossible de calculer les disponibilités max de la ressource " + ressourceHumaineARecalculer.getTrigramme() + " par profil.", e); // TODO FDA 2017/08 Trouevr mieux que juste loguer une erreur.
                    }
                }
            }
        });
    }

    private void initBeansNbrsJoursDispoMaxProfil() {
        ObservableLists.Binding binding = new ObservableLists.Binding("profilsBeans", "nbrsJoursDispoMaxProfilBeans");
        binding.ensureContains(
                planChargeBean.getProfilsBeans(),
                NbrsJoursDispoProfilBean::new,
                nbrsJoursDispoMaxProfilBeans
        );
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

    // Init des tables :

    private void initTableNbrsJoursOuvres() {

        // Définition du contenu de la table (ses lignes) :
        ObservableList<NbrsJoursOuvresBean> nbrsJoursOuvresBeans = FXCollections.observableArrayList();
        nbrsJoursOuvresBeans.add(nbrsJoursOuvresBean);
        nbrsJoursOuvresTable.setItems(nbrsJoursOuvresBeans);

        nbrsJoursOuvresTable.setCalendrierColumns(Arrays.asList(
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
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursOuvresColumn.setCellValueFactory((CellDataFeatures<NbrsJoursOuvresBean, String> cell) -> new SimpleStringProperty("N/A"));
        //noinspection HardcodedFileSeparator
        profilNbrsJoursOuvresColumn.setCellValueFactory((CellDataFeatures<NbrsJoursOuvresBean, String> cell) -> new SimpleStringProperty("N/A"));
        for (int cptColonne = 1; cptColonne <= nbrsJoursOuvresTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursOuvresBean, Integer> colonne = nbrsJoursOuvresTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierNbrsJoursCellCallback<NbrsJoursOuvresBean> nbrsJoursOuvresCellCallback = new CalendrierNbrsJoursCellCallback<>(cptColonne);
            colonne.setCellValueFactory(nbrsJoursOuvresCellCallback);
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        // Cette table n'est pas éditable (données calculées à partir des référentiels).
        ihm.interdireEdition(ressourceNbrsJoursOuvresColumn, "Cette colonne ne contient pas de données (juste pour aligner avec les lignes suivantes).");
        ihm.interdireEdition(profilNbrsJoursOuvresColumn, "Cette colonne ne contient pas de données (juste pour aligner avec les lignes suivantes).");
        for (int cptColonne = 1; cptColonne <= nbrsJoursOuvresTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursOuvresBean, Integer> colonne = nbrsJoursOuvresTable.getCalendrierColumns().get(cptColonne - 1);
            EditableAwareTextFieldTableCell<NbrsJoursOuvresBean, Integer> nbrJoursOuvresCell = new EditableAwareTextFieldTableCell<>(Converters.NBRS_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours ouvrés est calculé à partir du référentiel des jours fériés."));
            colonne.setCellFactory(column -> nbrJoursOuvresCell);
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (1 seule ligne).

        TableViews.disableReagencingColumns(nbrsJoursOuvresTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursOuvresTable);
    }

    private void initTableNbrsJoursAbsence() {

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursAbsenceTable.setItems(nbrsJoursAbsenceBeans);

        nbrsJoursAbsenceTable.setCalendrierColumns(Arrays.asList(
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
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursAbsenceColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().trigrammeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursAbsenceColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        for (int cptColonne = 1; cptColonne <= nbrsJoursAbsenceTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursAbsenceBean, Float> colonne = nbrsJoursAbsenceTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierNbrsDemisJoursCellCallback<NbrsJoursAbsenceBean> fractionsJoursCellCallback = new CalendrierNbrsDemisJoursCellCallback<>(cptColonne);
            colonne.setCellValueFactory(fractionsJoursCellCallback);
        }

        // Paramétrage de la saisie des valeurs (mode "édition") et du formatage des colonnes :
        ihm.interdireEdition(ressourceNbrsJoursAbsenceColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursAbsenceColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (int cptColonne = 1; cptColonne <= nbrsJoursAbsenceTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursAbsenceBean, Float> colonne = nbrsJoursAbsenceTable.getCalendrierColumns().get(cptColonne - 1);
//            CalendrierNbrsDemisJoursParRessourceCell<RessourceHumaineBean, NbrsJoursAbsenceBean> nbrsJoursParRessourceCell = new CalendrierNbrsDemisJoursParRessourceCell<>(planChargeBean, cptColonne);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory((TableColumn<NbrsJoursAbsenceBean, Float> param) -> new CalendrierNbrsDemisJoursParRessourceCell<>(finalCptColonne));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursAbsenceTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursAbsenceTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursAbsenceTable);
    }

    private void initTableNbrsJoursDispoMinAgri() {

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoMinAgriTable.setItems(nbrsJoursDispoMinAgriBeans);

        nbrsJoursDispoMinAgriTable.setCalendrierColumns(Arrays.asList(
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
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoMinAgriColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().trigrammeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursDispoMinAgriColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        for (int cptColonne = 1; cptColonne <= nbrsJoursDispoMinAgriTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursDispoRsrcBean, Float> colonne = nbrsJoursDispoMinAgriTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierFractionsJoursCellCallback<NbrsJoursDispoRsrcBean> nbrJoursDispoRsrcCell = new CalendrierFractionsJoursCellCallback<>(cptColonne);
            colonne.setCellValueFactory(nbrJoursDispoRsrcCell);
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoMinAgriColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoMinAgriColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (int cptColonne = 1; cptColonne <= nbrsJoursDispoMinAgriTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursDispoRsrcBean, Float> colonne = nbrsJoursDispoMinAgriTable.getCalendrierColumns().get(cptColonne - 1);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory(column -> new CalendrierFractionsJoursParRessourceCell<>(finalCptColonne, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité au Ministère est calculé à partir des jours ouvrés et d'absence.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoMinAgriTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursDispoMinAgriTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursDispoMinAgriTable);
    }

    private void initTablePctagesDispoCT() {

        // Définition du contenu de la table (ses lignes) :
        pctagesDispoCTTable.setItems(pctagesDispoCTBeans);

        pctagesDispoCTTable.setCalendrierColumns(Arrays.asList(
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
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourcePctagesDispoCTColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().trigrammeProperty());
        //noinspection HardcodedFileSeparator
        profilPctagesDispoCTColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        for (int cptColonne = 1; cptColonne <= pctagesDispoCTTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<PctagesDispoRsrcBean, Percentage> colonne = pctagesDispoCTTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierPctagesCellCallback<PctagesDispoRsrcBean> pctagesDispoRsrcCell = new CalendrierPctagesCellCallback<>(cptColonne);
            colonne.setCellValueFactory(pctagesDispoRsrcCell);
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourcePctagesDispoCTColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilPctagesDispoCTColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (int cptColonne = 1; cptColonne <= pctagesDispoCTTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<PctagesDispoRsrcBean, Percentage> colonne = pctagesDispoCTTable.getCalendrierColumns().get(cptColonne - 1);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory(column -> new CalendrierPctagesParRessourceCell<>(finalCptColonne));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(pctagesDispoCTTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(pctagesDispoCTTable);
        TableViews.ensureDisplayingAllRows(pctagesDispoCTTable);
    }

    private void initTableNbrsJoursDispoCT() {

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoCTTable.setItems(nbrsJoursDispoCTBeans);

        nbrsJoursDispoCTTable.setCalendrierColumns(Arrays.asList(
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
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoCTColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().trigrammeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursDispoCTColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        for (int cptColonne = 1; cptColonne <= nbrsJoursDispoCTTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursDispoRsrcBean, Float> colonne = nbrsJoursDispoCTTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierFractionsJoursCellCallback<NbrsJoursDispoRsrcBean> nbrJoursDispoRsrcBean = new CalendrierFractionsJoursCellCallback<>(cptColonne);
            colonne.setCellValueFactory(nbrJoursDispoRsrcBean);
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoCTColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoCTColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (int cptColonne = 1; cptColonne <= nbrsJoursDispoCTTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursDispoRsrcBean, Float> colonne = nbrsJoursDispoCTTable.getCalendrierColumns().get(cptColonne - 1);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory(cell -> new CalendrierFractionsJoursParRessourceCell<>(finalCptColonne, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité à la CT est calculé à partir des pourcentages de dispo pour la CT.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoCTTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursDispoCTTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursDispoCTTable);
    }

    private void initTablePctagesDispoMaxRsrcProfil() {

        // Définition du contenu de la table (ses lignes) :
        pctagesDispoMaxRsrcProfilTable.setItems(pctagesDispoMaxRsrcProfilBeans);

        pctagesDispoMaxRsrcProfilTable.setCalendrierColumns(Arrays.asList(
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
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourcePctagesDispoMaxRsrcProfilColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().trigrammeProperty());
        profilPctagesDispoMaxRsrcProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        for (int cptColonne = 1; cptColonne <= pctagesDispoMaxRsrcProfilTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<PctagesDispoRsrcProfilBean, Percentage> colonne = pctagesDispoMaxRsrcProfilTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierPctagesCellCallback<PctagesDispoRsrcProfilBean> pctageDispoRsrcProfilCell = new CalendrierPctagesCellCallback<>(cptColonne);
            colonne.setCellValueFactory(pctageDispoRsrcProfilCell);
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourcePctagesDispoMaxRsrcProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilPctagesDispoMaxRsrcProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (int cptColonne = 1; cptColonne <= pctagesDispoMaxRsrcProfilTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<PctagesDispoRsrcProfilBean, Percentage> colonne = pctagesDispoMaxRsrcProfilTable.getCalendrierColumns().get(cptColonne - 1);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory(cell -> new CalendrierPctagesParRessourceEtProfilCell<>(finalCptColonne));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(pctagesDispoMaxRsrcProfilTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        TableViews.enableFilteringOnColumns(pctagesDispoMaxRsrcProfilTable, Arrays.asList(ressourcePctagesDispoMaxRsrcProfilColumn, profilPctagesDispoMaxRsrcProfilColumn));

        TableViews.disableReagencingColumns(pctagesDispoMaxRsrcProfilTable);
        TableViews.ensureDisplayingAllRows(pctagesDispoMaxRsrcProfilTable);
    }

    private void initTableNbrsJoursDispoMaxRsrcProfil() {

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoMaxRsrcProfilTable.setItems(nbrsJoursDispoMaxRsrcProfilBeans);

        nbrsJoursDispoMaxRsrcProfilTable.setCalendrierColumns(Arrays.asList(
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
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoMaxRsrcProfilColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().trigrammeProperty());
        profilNbrsJoursDispoMaxRsrcProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        for (int cptColonne = 1; cptColonne <= nbrsJoursDispoMaxRsrcProfilTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursDispoRsrcProfilBean, Float> colonne = nbrsJoursDispoMaxRsrcProfilTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierFractionsJoursCellCallback<NbrsJoursDispoRsrcProfilBean> nbrJoursDispoRsrcProfilCell = new CalendrierFractionsJoursCellCallback<>(cptColonne);
            colonne.setCellValueFactory(nbrJoursDispoRsrcProfilCell);
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoMaxRsrcProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoMaxRsrcProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (int cptColonne = 1; cptColonne <= nbrsJoursDispoMaxRsrcProfilTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursDispoRsrcProfilBean, Float> colonne = nbrsJoursDispoMaxRsrcProfilTable.getCalendrierColumns().get(cptColonne - 1);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory(cell -> new CalendrierFractionsJoursParRessourceEtProfilCell<>(finalCptColonne, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité max. par ressource et par profil est calculé à partir des pourcentages.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoMaxRsrcProfilTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        TableViews.enableFilteringOnColumns(nbrsJoursDispoMaxRsrcProfilTable, Arrays.asList(ressourceNbrsJoursDispoMaxRsrcProfilColumn, profilNbrsJoursDispoMaxRsrcProfilColumn));

        TableViews.disableReagencingColumns(nbrsJoursDispoMaxRsrcProfilTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursDispoMaxRsrcProfilTable);
    }

    private void initTableNbrsJoursDispoMaxProfil() {

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoMaxProfilTable.setItems(nbrsJoursDispoMaxProfilBeans);

        nbrsJoursDispoMaxProfilTable.setCalendrierColumns(Arrays.asList(
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
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursDispoMaxProfilColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        profilNbrsJoursDispoMaxProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        for (int cptColonne = 1; cptColonne <= nbrsJoursDispoMaxProfilTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursDispoProfilBean, Float> colonne = nbrsJoursDispoMaxProfilTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierFractionsJoursCellCallback<NbrsJoursDispoProfilBean> nbrJoursDispoProfilCell = new CalendrierFractionsJoursCellCallback<>(cptColonne);
            colonne.setCellValueFactory(nbrJoursDispoProfilCell);
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoMaxProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoMaxProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (int cptColonne = 1; cptColonne <= nbrsJoursDispoMaxProfilTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<NbrsJoursDispoProfilBean, Float> colonne = nbrsJoursDispoMaxProfilTable.getCalendrierColumns().get(cptColonne - 1);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory(cell -> new CalendrierNbrsDemisJoursParProfilCell<>(finalCptColonne, () -> ihm.afficherInterdictionEditer("Le nombre de jours de disponibilité max. par profil est calculé à partir des pourcentages.")));
        }


        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoMaxProfilTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

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
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Impossible de définir les valeurs du calendrier",
                    "Date d'état non définie."
            );
*/
            return;
        }

        try {

            calculerDisponibilites();

        } catch (ControllerException e) {
            LOGGER.error("Impossible de définir les valeurs du calendrier.", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible de définir les valeurs du calendrier",
                    Exceptions.causes(e)
            );
        }
    }


    public void calculerDisponibilites() throws ControllerException {
        LOGGER.debug("Calcul des disponibilités  : ");
        calculateurDisponibilites.calculer();
        tables().forEach(TableView::refresh); // Notamment pour que les cellules qui étaient vides et qui ont une valeur suite au calcul (les provisions, typiquement) soient affichées.
        LOGGER.debug("Disponibilités calculées.");
    }


    @FXML
    private <B extends AbstractCalendrierParRessourceBean<RessourceHumaineBean, ?, ?, ?>> void voirNbrJoursAbsence(@NotNull ActionEvent actionEvent) {
        TableView<B> table = tableDuMenuContextuel(actionEvent);
        B nbrsJoursDispoMinAgriSelectedBean = TableViews.selectedItem(table);
        if (nbrsJoursDispoMinAgriSelectedBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher les nombres de jours d'absence pour la ressource",
                    "Aucune ligne n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }
        try {
            RessourceHumaineBean ressourceHumaineBean = nbrsJoursDispoMinAgriSelectedBean.getRessourceBean();
            NbrsJoursAbsenceBean nbrsJoursAbsenceBean = Collections.any(nbrsJoursAbsenceBeans, (NbrsJoursAbsenceBean bean) -> bean.getRessourceBean().equals(ressourceHumaineBean), new IhmException("Impossible de retrouver la ressource '" + ressourceHumaineBean.getTrigramme() + "' dans la table des nombres de jours d'absence."));
            nbrsJoursAbsenceAccordion.setExpandedPane(nbrsJoursAbsencePane);
            TableViews.focusOnItem(nbrsJoursAbsenceTable, nbrsJoursAbsenceBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher les nombres de jours d'absence pour la ressource.", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher les nombres de jours d'absence pour la ressource",
                    Exceptions.causes(e)
            );
        }
    }

    @FXML
    private <B extends AbstractCalendrierParRessourceBean<RessourceHumaineBean, ?, ?, ?>> void voirPctagesDispoCT(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        //noinspection unchecked
        TableView<B> table = (TableView<B>) nbrsJoursDispoCTTable;
        B nbrsJoursDispoCTBean = TableViews.selectedItem(table);
        if (nbrsJoursDispoCTBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher les pourcentages de disponiblité pour l'équipe (la CT) de la ressource",
                    "Aucune ligne n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }
        try {
            RessourceHumaineBean ressourceHumaineBean = nbrsJoursDispoCTBean.getRessourceBean();
            PctagesDispoRsrcBean pctagesDispoCTBean = Collections.any(pctagesDispoCTBeans, (PctagesDispoRsrcBean bean) -> bean.getRessourceBean().equals(ressourceHumaineBean), new IhmException("Impossible de retrouver la ressource '" + ressourceHumaineBean.getTrigramme() + "' dans la table des pourcentages de disponibilité pour l'équipe (la CT)."));
            nbrsJoursAbsenceAccordion.setExpandedPane(pctagesDispoCTPane);
            TableViews.focusOnItem(pctagesDispoCTTable, pctagesDispoCTBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher les pourcentages de disponiblité pour l'équipe (la CT) de la ressource.", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher les pourcentages de disponiblité pour l'équipe (la CT) de la ressource",
                    Exceptions.causes(e)
            );
        }
    }

    @NotNull
    private <B extends AbstractCalendrierParRessourceBean> TableView<B> tableDuMenuContextuel(ActionEvent actionEvent) {
        MenuItem menuItem = (MenuItem) actionEvent.getSource();
        //noinspection unchecked
        TableView<B> table = (TableView<B>) menuItem.getUserData();
        return table;
    }
}
