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
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurCharges;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.StatutBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithTooManyFields", "OverlyComplexClass", "AnonymousInnerClass"})
public class ChargesController extends AbstractTachesController<PlanificationTacheBean> implements Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargesController.class);


    private static ChargesController instance;

    public static ChargesController instance() {
        return instance;
    }


    public static final DecimalFormat FORMAT_CHARGE = new DecimalFormat("0", DecimalFormatSymbols.getInstance());

    static {
        FORMAT_CHARGE.setMinimumFractionDigits(0); // Les divisions de nbrs entiers par 8 tombent parfois juste (pas de décimale).
        FORMAT_CHARGE.setMaximumFractionDigits(3); // Les divisions de nbrs entiers par 8 se terminent par ".125", "0.25", ".325", ".5", ".625", ".75" ou ".825".
    }


    public static final PseudoClass CHARGE_NON_PLANIFIEE = PseudoClass.getPseudoClass("chargeNonPlanifiee");
    public static final PseudoClass INCOHERENCE = PseudoClass.getPseudoClass("incoherence");
    public static final PseudoClass SURCHARGE = PseudoClass.getPseudoClass("surcharge");
    public static final PseudoClass ECHEANCE_NON_TENUE = PseudoClass.getPseudoClass("echeanceNonTenue");


    /*
     La couche métier :
      */

    //    @Autowired
    @NotNull
    // 'final' pour éviter que quiconque resette cette variable.
    private final ChargeService planChargeService = ChargeService.instance();

    //    @Autowired
    @NotNull
    // 'final' pour éviter que quiconque resette cette variable.
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<PlanificationTacheBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<NbrsJoursParRessourceBean> nbrsJoursChargeRsrcBeans = FXCollections.observableArrayList();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<NbrsJoursParProfilBean> nbrsJoursChargeProfilBeans = FXCollections.observableArrayList();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<NbrsJoursParRessourceBean> nbrsJoursDispoCTRestanteRsrcBeans = FXCollections.observableArrayList();

    @NotNull
    // 'final' pour éviter que quiconque resette cette liste et ne détruise les listeners enregistrés dessus.
    private final ObservableList<NbrsJoursParProfilBean> nbrsJoursDispoCTMaxRestanteProfilBeans = FXCollections.observableArrayList();

    /*
     La couche "View" :
      */

/*
    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();
*/

    // Les paramètres (TabedPane "Paramètres") :

/*
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TextField nbrLignesTablePlanificationsField;
*/

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private Spinner<Integer> nbrLignesMaxTablePlanificationsSpinner;

    // Les filtres (TabedPane "Filtres")) :
    // Ajouter ici les filtres spécifiques des charges : Charge planifiée, Charge  planifiée dans le mois, Planifiée dans le mois ?, Tâche doublon ?, Reste à planifier, N° sem échéance, Échéance tenue ?, Durée restante, Charge / semaine, Charge / T

    /*
        @SuppressWarnings("NullableProblems")
        @NotNull
        @FXML
        private TitledPane planChargePane;

        @SuppressWarnings("NullableProblems")
        @NotNull
        @FXML
        private VBox planificationsTableVBox;
    */
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifToutToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifDemandeeDansSemestreToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifInfChargeToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifSupChargeToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtrePlanifDansMoisToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    public ToggleButton filtreSurchargeToutToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    public ToggleButton filtreSurchargeRessourceToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    public ToggleButton filtreSurchargeProfilToggleButton;


    // Les TableView :

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableViewAvecCalendrier<PlanificationTacheBean, Double> planificationsTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, ?> tacheColumn;
    // Les colonnes spécifiques du calendrier des tâches :
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine1Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine2Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine3Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine4Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine5Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine6Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine7Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine8Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine9Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine10Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine11Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> semaine12Column;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PlanificationTacheBean, Double> chargePlanifieeTotaleColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursParRessourceBean, Float> nbrsJoursChargeRsrcTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, ?> espaceNbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, String> ressourceNbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, String> profilNbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine1NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine2NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine3NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine4NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine5NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine6NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine7NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine8NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine9NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine10NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine11NbrsJoursChargeRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine12NbrsJoursChargeRsrcColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursParProfilBean, Float> nbrsJoursChargeProfilTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, ?> espaceNbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, String> ressourceNbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, String> profilNbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine1NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine2NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine3NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine4NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine5NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine6NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine7NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine8NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine9NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine10NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine11NbrsJoursChargeProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine12NbrsJoursChargeProfilColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursParRessourceBean, Float> nbrsJoursDispoCTRestanteRsrcTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, ?> espaceNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, String> ressourceNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, String> profilNbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine1NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine2NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine3NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine4NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine5NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine6NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine7NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine8NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine9NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine10NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine11NbrsJoursDispoCTRestanteRsrcColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParRessourceBean, Float> semaine12NbrsJoursDispoCTRestanteRsrcColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursParProfilBean, Float> nbrsJoursDispoCTMaxRestanteProfilTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, ?> espaceNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, String> ressourceNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, String> profilNbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine1NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine2NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine3NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine4NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine5NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine6NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine7NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine8NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine9NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine10NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine11NbrsJoursDispoCTMaxRestanteProfilColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursParProfilBean, Float> semaine12NbrsJoursDispoCTMaxRestanteProfilColumn;

    /*
    La couche "Controller" :
     */

    //    @Autowired
    @NotNull
    private final CalculateurCharges calculateurCharges = CalculateurCharges.instance();


    // Constructors:

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ChargesController() throws ControllerException {
        super();
        if (instance != null) {
            throw new ControllerException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    // Getters/Setters:

    @NotNull
    @SuppressWarnings("SuspiciousGetterSetter")
    @Override
    ObservableList<PlanificationTacheBean> getTachesBeans() {
        return planificationsBeans;
    }

    @NotNull
    @SuppressWarnings("SuspiciousGetterSetter")
    @Override
    TableViewAvecCalendrier<PlanificationTacheBean, Double> getTachesTable() {
        return planificationsTable;
    }

    @NotNull
    public ObservableList<NbrsJoursParRessourceBean> getNbrsJoursChargeRsrcBeans() {
        return nbrsJoursChargeRsrcBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursParProfilBean> getNbrsJoursChargeProfilBeans() {
        return nbrsJoursChargeProfilBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursParRessourceBean> getNbrsJoursDispoCTRestanteRsrcBeans() {
        return nbrsJoursDispoCTRestanteRsrcBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursParProfilBean> getNbrsJoursDispoCTMaxRestanteProfilBeans() {
        return nbrsJoursDispoCTMaxRestanteProfilBeans;
    }

    // Module

    @Override
    public String getTitre() {
        return "Charge";
    }


    // Methods:

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @SuppressWarnings("OverlyLongMethod")
    @FXML
    @Override
    protected void initialize() throws ControllerException {
        try {
            Calculateur.executerSansCalculer(() -> {

                super.initialize(); // TODO FDA 2017/05 Très redondant (le + gros est déjà initialisé par le ModuleTacheController) => améliorer le code.

                initBeans();
                initTables();

            });
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur.", e);
        }
    }

/* Le menu contextuel est défini dans le fichier FXML.
    void definirMenuContextuel() {
        super.definirMenuContextuel();

        if (menuVoirTache == null) {
            menuVoirTache = new MenuItem("Voir le détail de la tâche");
            menuVoirTache.setOnAction(event -> afficherTache());
        }
        if (!tachesTableContextMenu.getItems().contains(menuVoirTache)) {
            tachesTableContextMenu.getItems().add(menuVoirTache);
        }

    }
*/

    private void initBeans() {
        initBeansPlanifications();
        initBeansNbrsJoursChargeRsrc();
        initBeansNbrsJoursChargeProfil();
        initBeansNbrsJoursDispoCTRestanteRsrc();
        initBeansNbrsJoursDispoCTMaxRestanteProfil();

/* TODO FDA 2017/10 Calculer les charges pour la RH ajoutée.
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
                            calculateurCharges.calculer(ressourceHumaineBean);
                        } catch (ControllerException e) {
                            //noinspection DuplicateStringLiteralInspection
                            LOGGER.error("Impossible de calculer les charges de la ressource " + ressourceHumaineBean.getTrigramme() + " en fonction de sa date de début de mission.", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        }
                    });

                    ObjectProperty<LocalDate> finMissionProperty = ressourceHumaineBean.finMissionProperty();
                    finMissionProperty.addListener((observable, oldValue, newValue) -> {
                        try {
                            calculateurCharges.calculer(ressourceHumaineBean);
                        } catch (ControllerException e) {
                            //noinspection DuplicateStringLiteralInspection
                            LOGGER.error("Impossible de calculer les charges de la ressource " + ressourceHumaineBean.getTrigramme() + " en fonction de sa date de fin de mission.", e); // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        }
                    });
                }
            }
        });
*/
    }

    private void initBeansPlanifications() {
        // Rien de plus... pour l'instant.
    }

    private void initBeansNbrsJoursChargeRsrc() {
        ObservableLists.Binding binding = new ObservableLists.Binding("ressourcesBeans", "nbrsJoursChargeRsrcBeans");
        binding.ensureContains(
                planChargeBean.getRessourcesBeans(),
                ressourceBean -> new NbrsJoursParRessourceBean(ressourceBean, new TreeMap<>()), // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
                nbrsJoursChargeRsrcBeans
        );
    }

    private void initBeansNbrsJoursChargeProfil() {
        ObservableLists.Binding binding = new ObservableLists.Binding("profilsBeans", "nbrsJoursChargeProfilBeans");
        binding.ensureContains(
                planChargeBean.getProfilsBeans(),
                profilBean -> new NbrsJoursParProfilBean(profilBean, new TreeMap<>()), // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
                nbrsJoursChargeProfilBeans
        );
    }

    private void initBeansNbrsJoursDispoCTRestanteRsrc() {
        ObservableLists.Binding binding = new ObservableLists.Binding("ressourcesBeans", "nbrsJoursDispoCTRestanteRsrcBeans");
        binding.ensureContains(
                planChargeBean.getRessourcesBeans(),
                ressourceHumaineBean -> new NbrsJoursParRessourceBean(ressourceHumaineBean, new TreeMap<>()), // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
                nbrsJoursDispoCTRestanteRsrcBeans
        );
    }

    private void initBeansNbrsJoursDispoCTMaxRestanteProfil() {
        ObservableLists.Binding binding = new ObservableLists.Binding("profilsBeans", "nbrsJoursDispoCTMaxRestanteProfilBeans");
        binding.ensureContains(
                planChargeBean.getProfilsBeans(),
                profilBean -> new NbrsJoursParProfilBean(profilBean, new TreeMap<>()), // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
                nbrsJoursDispoCTMaxRestanteProfilBeans
        );
    }


    public List<TableViewAvecCalendrier<?, ?>> tables() {
        return Arrays.asList(new TableViewAvecCalendrier<?, ?>[]{
                planificationsTable,
                nbrsJoursChargeRsrcTable,
                nbrsJoursChargeProfilTable,
                nbrsJoursDispoCTRestanteRsrcTable,
                nbrsJoursDispoCTMaxRestanteProfilTable
        });
    }

    private void initTables() {
        initTablePlanifications();
        initTableNbrsJoursChargeRsrc();
        initTableNbrsJoursChargeProfil();
        initTableNbrsJoursDispoCTRestanteRsrc();
        initTableNbrsJoursDispoCTMaxRestanteProfilTable();

        synchroniserLargeurColonnes();
    }

    private void initTablePlanifications() {

        // Définition du contenu de la table (ses lignes) :
//        planificationsTable.setItems(planificationsBeans); Non, sinon le filtre global ne fontionnera plus (défini par la classe mère lors "super.initialize()").

        planificationsTable.setCalendrierColumns(Arrays.asList(
                semaine1Column,
                semaine2Column,
                semaine3Column,
                semaine4Column,
                semaine5Column,
                semaine6Column,
                semaine7Column,
                semaine8Column,
                semaine9Column,
                semaine10Column,
                semaine11Column,
                semaine12Column
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        for (int cptColonne = 1; cptColonne <= planificationsTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<PlanificationTacheBean, Double> colonne = planificationsTable.getCalendrierColumns().get(cptColonne - 1);
            CalendrierChargeCellCallback calendrierChargeCellCallback = new CalendrierChargeCellCallback(cptColonne);
            colonne.setCellValueFactory(calendrierChargeCellCallback);
        }
        chargePlanifieeTotaleColumn.setCellValueFactory(cellData -> cellData.getValue().chargePlanifieeTotaleProperty().asObject());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") et
        // du formatage qui symbolise les incohérences/surcharges/etc. (Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/) :
        //
        getDebutColumn().setOnEditCommit(event -> { // On ajoute un Handler sur "onCommit" plutôt que un CellFactory pour ne pas écraser la CellFactory définie par la classe mère.
            PlanificationTacheBean planificationTacheBean = event.getTableView().getItems().get(event.getTablePosition().getRow());
            planificationTacheBean.setDebut(event.getNewValue());

//            planificationsTable.refresh(); // Pour que les styles CSS soient re-appliqués (notamment ceux des colonnes du calendrier).
        });
        //noinspection OverlyComplexAnonymousInnerClass
        getEcheanceColumn().setCellFactory(param -> new EcheanceCell<PlanificationTacheBean>(PlanChargeIhm.PATRON_FORMAT_DATE, PlanChargeIhm.PROMPT_FORMAT_DATE, planificationsTable) {

            @Override
            protected void styler() {
                super.styler();

                // Réinit du style de la cellule :
                pseudoClassStateChanged(ECHEANCE_NON_TENUE, false);

                // Stop, si cellule vide :
                if (isEmpty() || (getItem() == null)) {
                    return;
                }

                // Récupération des infos sur la cellule :
                PlanificationTacheBean planifBean = tacheBean();
                if (planifBean == null) {
                    return;
                }

                // Formatage du style (CSS) de la cellule :
                boolean estEcheanceTenue = estEcheanceTenue(planifBean);
                if (!estEcheanceTenue) {
                    pseudoClassStateChanged(ECHEANCE_NON_TENUE, true);
                }
            }

            // TODO FDA 2017/09 Coder cette RG dans la couche métier.
            private boolean estEcheanceTenue(@NotNull PlanificationTacheBean planifBean) {
                LocalDate echeance = planifBean.getEcheance();
                if (echeance == null) {
                    return true;
                }
                LocalDate debutDernierePeriodePlanifiee = Dates.max(planifBean.getDebutsPeriodesPlanifiees());
                if (debutDernierePeriodePlanifiee == null) {
                    return false;
                }
                LocalDate finDernierePeriodePlanifiee = debutDernierePeriodePlanifiee.plusDays(7L); // TODO [issue#26:PeriodeHebdo/Trim]
                return !finDernierePeriodePlanifiee.isAfter(echeance);
            }

        });
        //noinspection OverlyComplexAnonymousInnerClass
        getChargeColumn().setCellFactory(column -> new TextFieldTableCell<PlanificationTacheBean, Double>(Converters.CHARGE_STRING_CONVERTER) {// TODO FDA 2017/08 Mieux formater.

            @Override
            public void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
//                formater();
                styler();
            }

            private void formater() {
                if ((getItem() == null) || isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(FORMAT_CHARGE.format(getItem()));
            }

            private void styler() {

                // Réinit du texte et du style de la cellule :
                pseudoClassStateChanged(INCOHERENCE, false);
                pseudoClassStateChanged(CHARGE_NON_PLANIFIEE, false);

                if ((getItem() == null) || isEmpty()) {
                    return;
                }

                // Style with a different color:
                Double chargePlanifiee = chargePlanifieeTotaleColumn.getCellData(getIndex());
                if (chargePlanifiee != null) {
                    if (chargePlanifiee < getItem()) {
                        pseudoClassStateChanged(CHARGE_NON_PLANIFIEE, true);
                    }
                    if (chargePlanifiee > getItem()) {
                        pseudoClassStateChanged(INCOHERENCE, true);
                    }
                }
            }
        });
        //noinspection OverlyComplexAnonymousInnerClass
        getRessourceColumn().setCellFactory(param -> new RessourceCell<PlanificationTacheBean>(planChargeBean.getRessourcesBeans(), this::afficherRessourceHumaine, this::filtrerSurRessource) {

            @Override
            public void updateItem(RessourceBean<?, ?> item, boolean empty) {
                super.updateItem(item, empty);
//                formater();
                styler();
            }

            private void formater() {
                if (isEmpty() || (getItem() == null)) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(getConverter().toString(getItem()));
            }

            private void styler() {
                pseudoClassStateChanged(SURCHARGE, false);

                if (isEmpty() || (getItem() == null)) {
                    return;
                }

                // Récupération des infos sur la cellule :
                //noinspection unchecked
                TableRow<PlanificationTacheBean> tableRow = getTableRow();
                if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
                    return;
                }
                PlanificationTacheBean planifBean = tableRow.getItem();
                if (planifBean == null) {
                    return;
                }

                if (estRessourceSurchargee(planifBean)) {
                    pseudoClassStateChanged(SURCHARGE, true);
                }
            }
        });
        //noinspection OverlyComplexAnonymousInnerClass
        getProfilColumn().setCellFactory(param -> new ComboBoxTableCell<PlanificationTacheBean, ProfilBean>(Converters.PROFIL_BEAN_CONVERTER, planChargeBean.getProfilsBeans()) {
            @Override
            public void updateItem(ProfilBean item, boolean empty) {
                super.updateItem(item, empty);
//                formater();
                styler();
            }

            private void formater() {
                if (isEmpty() || (getItem() == null)) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(getConverter().toString(getItem()));
            }

            private void styler() {
                pseudoClassStateChanged(SURCHARGE, false);

                if (isEmpty() || (getItem() == null)) {
                    return;
                }

                // Récupération des infos sur la cellule :
                //noinspection unchecked
                TableRow<PlanificationTacheBean> tableRow = getTableRow();
                if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
                    return;
                }
                PlanificationTacheBean planifBean = tableRow.getItem();
                if (planifBean == null) {
                    return;
                }

                if (estProfilSurcharge(planifBean)) {
                    pseudoClassStateChanged(SURCHARGE, true);
                }
            }
        });
        for (int cptColonne = 1; cptColonne <= planificationsTable.getCalendrierColumns().size(); cptColonne++) {
            TableColumn<PlanificationTacheBean, Double> colonne = planificationsTable.getCalendrierColumns().get(cptColonne - 1);
            int finalCptColonne = cptColonne;
            colonne.setCellFactory(param -> new PlanificationChargeCell(finalCptColonne));
        }
        //noinspection OverlyComplexAnonymousInnerClass
        chargePlanifieeTotaleColumn.setCellFactory(column -> new EditableAwareTextFieldTableCell<PlanificationTacheBean, Double>(Converters.CHARGE_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Cette colonne n'est pas saisissable, elle est calculée.")) {

            @Override
            public void updateItem(@Null Double item, boolean empty) {
                super.updateItem(item, empty);
//                formater();
                styler();
            }

            private void formater() {
                if ((getItem() == null) || isEmpty()) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                setText(getConverter().toString(getItem()));
            }

            private void styler() {

                pseudoClassStateChanged(INCOHERENCE, false);

                if ((getItem() == null) || isEmpty()) {
                    return;
                }

                // Style with a different color:
                Double charge = getChargeColumn().getCellData(getIndex());
                if (getItem() > charge) {
                    pseudoClassStateChanged(INCOHERENCE, true);
                }
            }
        });

//        planificationsTable.getSelectionModel().setCellSelectionEnabled(true); Fait dans le FXML.

//        FIXME FDA 2017/10 Les colonnes sont toujours réagençables.
        TableViews.disableReagencingColumns(planificationsTable);

        TableViews.ensureDisplayingRows(planificationsTable, 30);
        nbrLignesMaxTablePlanificationsSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            Integer nbrlignes = Objects.value(newValue, 30);
            TableViews.ensureDisplayingRows(planificationsTable, nbrlignes);
        });

    }

    private void initTableNbrsJoursChargeRsrc() {

        nbrsJoursChargeRsrcTable.setCalendrierColumns(Arrays.asList(
                semaine1NbrsJoursChargeRsrcColumn,
                semaine2NbrsJoursChargeRsrcColumn,
                semaine3NbrsJoursChargeRsrcColumn,
                semaine4NbrsJoursChargeRsrcColumn,
                semaine5NbrsJoursChargeRsrcColumn,
                semaine6NbrsJoursChargeRsrcColumn,
                semaine7NbrsJoursChargeRsrcColumn,
                semaine8NbrsJoursChargeRsrcColumn,
                semaine9NbrsJoursChargeRsrcColumn,
                semaine10NbrsJoursChargeRsrcColumn,
                semaine11NbrsJoursChargeRsrcColumn,
                semaine12NbrsJoursChargeRsrcColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursChargeRsrcColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().codeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursChargeRsrcColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParRessourceBean, Float> column : nbrsJoursChargeRsrcTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursChargeRsrcColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursChargeRsrcColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            for (TableColumn<NbrsJoursParRessourceBean, Float> column : nbrsJoursChargeRsrcTable.getCalendrierColumns()) {
                column.setCellFactory(owningColumn -> new EditableAwareTextFieldTableCell<>(Converters.FRACTION_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de charge par ressource est calculé.")));
            }
        }

        // Paramétrage des ordres de tri :
        TableViews.ensureSorting(nbrsJoursChargeRsrcTable, nbrsJoursChargeRsrcBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursChargeRsrcTable);

        TableViews.ensureDisplayingAllRows(nbrsJoursChargeRsrcTable);
    }

    private void initTableNbrsJoursChargeProfil() {

        nbrsJoursChargeProfilTable.setCalendrierColumns(Arrays.asList(
                semaine1NbrsJoursChargeProfilColumn,
                semaine2NbrsJoursChargeProfilColumn,
                semaine3NbrsJoursChargeProfilColumn,
                semaine4NbrsJoursChargeProfilColumn,
                semaine5NbrsJoursChargeProfilColumn,
                semaine6NbrsJoursChargeProfilColumn,
                semaine7NbrsJoursChargeProfilColumn,
                semaine8NbrsJoursChargeProfilColumn,
                semaine9NbrsJoursChargeProfilColumn,
                semaine10NbrsJoursChargeProfilColumn,
                semaine11NbrsJoursChargeProfilColumn,
                semaine12NbrsJoursChargeProfilColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursChargeProfilColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        profilNbrsJoursChargeProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParProfilBean, Float> column : nbrsJoursChargeProfilTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursChargeProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursChargeProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            for (TableColumn<NbrsJoursParProfilBean, Float> column : nbrsJoursChargeProfilTable.getCalendrierColumns()) {
                column.setCellFactory(owningColumn -> new EditableAwareTextFieldTableCell<>(Converters.FRACTION_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de charge par profil est calculé.")));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursChargeProfilTable, nbrsJoursChargeProfilBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursChargeProfilTable);

        TableViews.ensureDisplayingAllRows(nbrsJoursChargeProfilTable);
    }

    private void initTableNbrsJoursDispoCTRestanteRsrc() {

        nbrsJoursDispoCTRestanteRsrcTable.setCalendrierColumns(Arrays.asList(
                semaine1NbrsJoursDispoCTRestanteRsrcColumn,
                semaine2NbrsJoursDispoCTRestanteRsrcColumn,
                semaine3NbrsJoursDispoCTRestanteRsrcColumn,
                semaine4NbrsJoursDispoCTRestanteRsrcColumn,
                semaine5NbrsJoursDispoCTRestanteRsrcColumn,
                semaine6NbrsJoursDispoCTRestanteRsrcColumn,
                semaine7NbrsJoursDispoCTRestanteRsrcColumn,
                semaine8NbrsJoursDispoCTRestanteRsrcColumn,
                semaine9NbrsJoursDispoCTRestanteRsrcColumn,
                semaine10NbrsJoursDispoCTRestanteRsrcColumn,
                semaine11NbrsJoursDispoCTRestanteRsrcColumn,
                semaine12NbrsJoursDispoCTRestanteRsrcColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoCTRestanteRsrcColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().codeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursDispoCTRestanteRsrcColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParRessourceBean, Float> column : nbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoCTRestanteRsrcColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoCTRestanteRsrcColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (TableColumn<NbrsJoursParRessourceBean, Float> column : nbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns()) {
            column.setCellFactory(owningColumn -> new ChargePlanifieeCell<>(() -> ihm.afficherInterdictionEditer("Le nombre de jours de dispo. CT restante par ressource est calculé.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoCTRestanteRsrcTable, nbrsJoursDispoCTRestanteRsrcBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursDispoCTRestanteRsrcTable);

        TableViews.ensureDisplayingAllRows(nbrsJoursDispoCTRestanteRsrcTable);

        definirMenuContextuelColonneRessource(nbrsJoursDispoCTRestanteRsrcTable, ressourceNbrsJoursDispoCTRestanteRsrcColumn);
    }

    private void definirMenuContextuelColonneRessource(@NotNull TableViewAvecCalendrier<NbrsJoursParRessourceBean, Float> table, @NotNull TableColumn<NbrsJoursParRessourceBean, String> ressourceColumn) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Filtrer les tâches sur la ressource");
        menuItem.setOnAction(event -> {

            NbrsJoursParRessourceBean nbrsJoursParRessourceBean = TableViews.selectedItem(table);
            if (nbrsJoursParRessourceBean == null) {
                ihm.afficherDialog(
                        Alert.AlertType.WARNING,
                        "Aucune ressource sélectionnée",
                        "Sélectionnez d'abord une ligne, puis recliquez.",
                        400, 100
                );
                return;
            }

            RessourceBean ressourceBean = nbrsJoursParRessourceBean.getRessourceBean();
            assert ressourceBean != null;

            filtrerSurRessource(ressourceBean);
        });
        contextMenu.getItems().add(menuItem);
        ressourceColumn.setContextMenu(contextMenu);
    }

    private void initTableNbrsJoursDispoCTMaxRestanteProfilTable() {

        nbrsJoursDispoCTMaxRestanteProfilTable.setCalendrierColumns(Arrays.asList(
                semaine1NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine2NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine3NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine4NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine5NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine6NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine7NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine8NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine9NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine10NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine11NbrsJoursDispoCTMaxRestanteProfilColumn,
                semaine12NbrsJoursDispoCTMaxRestanteProfilColumn
        ));

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursDispoCTMaxRestanteProfilColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        profilNbrsJoursDispoCTMaxRestanteProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParProfilBean, Float> column : nbrsJoursDispoCTMaxRestanteProfilTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoCTMaxRestanteProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoCTMaxRestanteProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (TableColumn<NbrsJoursParProfilBean, Float> column : nbrsJoursDispoCTMaxRestanteProfilTable.getCalendrierColumns()) {
            column.setCellFactory(owningColumn -> new ChargePlanifieeCell<>(() -> ihm.afficherInterdictionEditer("Le nombre de jours de dispo. max. CT restante par profil est calculé.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoCTMaxRestanteProfilTable, nbrsJoursDispoCTMaxRestanteProfilBeans);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableReagencingColumns(nbrsJoursDispoCTMaxRestanteProfilTable);

        TableViews.ensureDisplayingAllRows(nbrsJoursDispoCTMaxRestanteProfilTable);
    }

    // TODO FDA 2017/09 Mettre cette RG dans la couche métier.
    private boolean estRessourceSurchargee(@NotNull PlanificationTacheBean planifBean) {
        if ((planifBean.getTacheBean().getRessource() == null) || !planifBean.getTacheBean().getRessource().estHumain()) {
            return false;
        }
        NbrsJoursParRessourceBean nbrsJoursDispoRestanteBean = Collections.any(
                nbrsJoursDispoCTRestanteRsrcBeans,
                nbrsJoursParRessourceBean -> nbrsJoursParRessourceBean.getRessourceBean().equals(planifBean.getTacheBean().getRessource())
        );
        if (nbrsJoursDispoRestanteBean == null) {
            return false;
        }
        for (Map.Entry<LocalDate, FloatProperty> entry : nbrsJoursDispoRestanteBean.entrySet()) {
            FloatProperty nbrJoursDispoRestantePeriode = entry.getValue();
            if (nbrJoursDispoRestantePeriode.get() < 0) {
                return true;
            }
        }
        return false;
    }

    // TODO FDA 2017/09 Mettre cette RG dans la couche métier.
    private boolean estProfilSurcharge(@NotNull PlanificationTacheBean planifBean) {
        if (planifBean.getTacheBean().getProfil() == null) {
            return false;
        }
        NbrsJoursParProfilBean nbrsJoursDispoRestanteBean = Collections.any(
                nbrsJoursDispoCTMaxRestanteProfilBeans,
                nbrsJoursParRessourceBean -> nbrsJoursParRessourceBean.getProfilBean().equals(planifBean.getTacheBean().getProfil())
        );
        if (nbrsJoursDispoRestanteBean == null) {
            return false;
        }
        for (Map.Entry<LocalDate, FloatProperty> entry : nbrsJoursDispoRestanteBean.entrySet()) {
            FloatProperty nbrJoursDispoRestantePeriode = entry.getValue();
            if (nbrJoursDispoRestantePeriode.get() < 0) {
                return true;
            }
        }
        return false;
    }


    private void synchroniserLargeurColonnes() {

        // Synchronisation de certaines colonnes de la 1ère table et de celles de la 2nde table :
        TableViews.synchronizeColumnsWidth(
                espaceNbrsJoursChargeRsrcColumn,
                Arrays.asList(
                        getCategorieColumn(),
                        getSousCategorieColumn(),
                        getSousCategorieColumn(),
                        getNoTacheColumn(),
                        getNoTicketIdalColumn(),
                        getDescriptionColumn(),
                        getProjetAppliColumn(),
                        getStatutColumn(),
                        getDebutColumn(),
                        getEcheanceColumn(),
                        getImportanceColumn(),
                        getChargeColumn()
/*
                        getRessourceColumn(),
                        getProfilColumn()
*/
                )
        );
        TableViews.synchronizeColumnsWidth(getRessourceColumn(), ressourceNbrsJoursChargeRsrcColumn);
        TableViews.synchronizeColumnsWidth(getProfilColumn(), profilNbrsJoursChargeRsrcColumn);
        TableViews.synchronizeColumnsWidth(planificationsTable.getCalendrierColumns(), nbrsJoursChargeRsrcTable.getCalendrierColumns());


        // Synchronisation de toutes les colonnes de la 2nde table avec les colonnes des tables affichées en dessous :
        List<TableView<?>> tablesSuivantes = new ArrayList<>(tables());
        tablesSuivantes.remove(planificationsTable);
        tablesSuivantes.remove(nbrsJoursChargeRsrcTable);
        TableViews.synchronizeColumnsWidth(nbrsJoursChargeRsrcTable, tablesSuivantes);
    }

    @NotNull
    protected PlanificationTacheBean ajouterTache() throws ControllerException {
        LOGGER.debug("ajouterTache...");
        if (planChargeBean.getDateEtat() == null) {
            throw new ControllerException("Impossible d'ajouter une tâche car la date d'état n'est pas définie. Précisez une date auparavant.");
        }
        try {

            PlanificationTacheBean planifBean = super.ajouterTache();

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new AjoutTache<>(planifBean, planChargeBean.getPlanificationsBeans()));

            ihm.getApplicationController().majBarreEtat();

            return planifBean;
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'ajouter une tâche.", e);
        }
    }

    @NotNull
    @Override
    PlanificationTacheBean nouveauBean() throws ControllerException {

        // TODO FDA 2017/09 Déplacer cette RG dans la couche métier (service, DTO et/ou Entity).
        TacheBean tacheBean = new TacheBean(
                idTacheSuivant(),
                null,
                null,
                "(pas de ticket IDAL)",
                null,
                null,
                StatutBean.from(StatutDTO.NOUVEAU),
                null,
                null,
                null,
                0.0,
                null,
                null
        );

        // TODO FDA 2017/09 Bloc à supprimer, sans doute.
        Map<LocalDate, DoubleProperty> calendrier = new TreeMap<>(); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
        if (planChargeBean.getDateEtat() != null) {
            LocalDate debutSemaine = planChargeBean.getDateEtat();
            for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
                calendrier.put(debutSemaine, new SimpleDoubleProperty(0.0));
                debutSemaine = debutSemaine.plusDays(7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
            }
        }

        try {
            return new PlanificationTacheBean(tacheBean, calendrier);
        } catch (BeanException e) {
            throw new ControllerException("Impossible d'instancier un nouvelle tâche du plan de charge.", e);
        }
    }


    public void definirValeursCalendrier() {

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
/*
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification (valeurs du calendrier)",
                    "Date d'état non définie."
            );
*/
            return;
        }

        try {
            definirValeursCalendrier(dateEtat);
        } catch (ControllerException e) {
            LOGGER.error("Impossible de màj la planification (valeurs du calendrier).", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible de mettre à jour la planification (valeurs du calendrier)",
                    Exceptions.causes(e)
            );
        }
    }

    private void definirValeursCalendrier(@NotNull LocalDate dateEtat) throws ControllerException {
        try {
            LOGGER.debug("Màj de la planification : ");

            PlanificationsDTO planificationsInitiales = planChargeBean.toPlanificationDTOs();
            PlanificationsDTO planifications = planChargeService.replanifier(planificationsInitiales, dateEtat);

            planChargeBean.fromPlanificationDTOs(planifications);

//            planificationsTable.refresh();
            LOGGER.debug("Planification màj.");
        } catch (BeanException | ServiceException e) {
            throw new ControllerException("Impossible de màj la planification.", e);
        }
    }


/* TODO FDA 2017/08 Coder.
    @FXML
    private void filtrerTachesNonPlanifieesDansLeMois(@SuppressWarnings("unused") ActionEvent actionEvent) {
        // TODO FDA 2017/07 Coder.
    }

    @FXML
    private void filtrerTachesNonPlanifieesEntierement(@SuppressWarnings("unused") ActionEvent actionEvent) {
        filtrerTachesNonPlanifieesEntierement();
    }

    private void filtrerTachesNonPlanifieesEntierement() {
        getFilteredTachesBeans().setPredicate(tacheBean -> (tacheBean.getCharge() == null) || (tacheBean.getCharge() != tacheBean.getChargePlanifieeTotale()));
    }
*/

    @SuppressWarnings("unused")
    @FXML
    private void afficherTache(@SuppressWarnings("unused") ActionEvent actionEvent) {
        afficherTache();
    }

    private void afficherTache() {
        PlanificationTacheBean tacheBean = TableViews.selectedItem(planificationsTable);
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la tâche",
                    "Aucune ligne du plan de charge n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        try {
            ihm.getApplicationController().afficherModuleTaches();
            TableViews.focusOnItem(ihm.getTachesController().getTachesTable(), tacheBean);
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'afficher la tâche " + tacheBean.noTache() + ".", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la tâche" + tacheBean.noTache(),
                    Exceptions.causes(e),
                    400, 200
            );
        }
    }


    @Override
    protected boolean estTacheAvecAutreFiltreAVoir(@NotNull PlanificationTacheBean tache) throws ControllerException {
        if (estTacheAvecFiltrePlanifAVoir(tache)) {
            return true;
        }
        if (estTacheAvecFiltreSurchargeAVoir(tache)) {
            return true;
        }
        return false;
    }

    private boolean estTacheAvecFiltrePlanifAVoir(@NotNull PlanificationTacheBean tache) throws ControllerException {
        if (filtrePlanifToutToggleButton.isSelected()) {
            return true;
        }
        if (filtrePlanifDemandeeDansSemestreToggleButton.isSelected()) {
            if ((planChargeBean.getDateEtat() == null) || (tache.getDebut() == null)) {
                return true;
            }
            if (tache.getDebut().isBefore(planChargeBean.getDateEtat().plusMonths(6L))) {
                return true;
            }
        }
        if (filtrePlanifInfChargeToggleButton.isSelected()) {
            if (tache.getCharge() == null) {
                return true;
            }
            if (tache.getChargePlanifieeTotale() < tache.getCharge()) { // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
                return true;
            }
        }
        if (filtrePlanifSupChargeToggleButton.isSelected()) {
            if (tache.getCharge() == null) {
                return true;
            }
            if (tache.getChargePlanifieeTotale() > tache.getCharge()) { // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
                return true;
            }
        }
        if (filtrePlanifDansMoisToggleButton.isSelected()) {
            if (planChargeBean.getDateEtat() == null) {
                return true;
            }
            LocalDate debutPeriode, finPeriode;
            debutPeriode = planChargeBean.getDateEtat();
            finPeriode = debutPeriode.plusMonths(1L);
            DoubleProperty chargePlanifiee;
            try {
                // FIXME FDA 2017/09 Calcule la charge sur 1 p&riode, donc actuellement 1 semaine, pas sur le mois.
                chargePlanifiee = tache.chargePlanifieeProperty(debutPeriode/*, finPeriode*/)/*.getValue()*/;
            } catch (BeanException e) {
                throw new ControllerException("Impossible de déterminer la charge planifiée de la tâche " + tache.noTache() + " pour la période de " + debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + " à " + finPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".", e);
            }
            if ((chargePlanifiee.getValue() != null) && (chargePlanifiee.getValue() > 0.0)) { // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
                return true;
            }
        }
        return false;
    }

    private boolean estTacheAvecFiltreSurchargeAVoir(@NotNull PlanificationTacheBean tache) throws ControllerException {
        if (filtreSurchargeToutToggleButton.isSelected()) {
            return true;
        }
        if (filtreSurchargeRessourceToggleButton.isSelected()) {
            RessourceBean<?, ?> ressourceBean = tache.getRessource();
            if ((ressourceBean != null) && ressourceBean.estHumain()) {
                NbrsJoursParRessourceBean nbrsJoursParRessourceBean = Collections.any(
                        nbrsJoursDispoCTRestanteRsrcBeans,
                        nbrsJoursBean -> nbrsJoursBean.getRessourceBean().equals(ressourceBean),
                        new ControllerException("Impossible de retrouver la ressource '" + ressourceBean.getCode() + "' dans la liste des nombres de jours de disponibilité CT restante par ressource.")
                );
                FloatProperty unNbrJoursDispoNegatif = Collections.any(
                        nbrsJoursParRessourceBean.values(),
                        nbrJourDispoRestante -> (nbrJourDispoRestante != null) && (nbrJourDispoRestante.getValue() != null) && (nbrJourDispoRestante.getValue() < 0.0)
                );
                //noinspection VariableNotUsedInsideIf
                if (unNbrJoursDispoNegatif != null) {// Au moins 1 période de surchargée pour cette ressource.
                    return true;
                }
            }
        }
        if (filtreSurchargeProfilToggleButton.isSelected()) {
            ProfilBean profilBean = tache.getProfil();
            if (profilBean != null) {
                NbrsJoursParProfilBean nbrsJoursParProfilBean = Collections.any(
                        nbrsJoursDispoCTMaxRestanteProfilBeans,
                        nbrsJoursBean -> nbrsJoursBean.getProfilBean().equals(profilBean),
                        new ControllerException("Impossible de retrouver le profil '" + profilBean.getCode() + "' dans la liste des nombres de jours de disponibilité CT max restante par profil.")
                );
                FloatProperty unNbrJoursDispoNegatif = Collections.any(
                        nbrsJoursParProfilBean.values(),
                        nbrJourDispoRestante -> (nbrJourDispoRestante != null) && (nbrJourDispoRestante.getValue() != null) && (nbrJourDispoRestante.getValue() < 0.0)
                );
                //noinspection VariableNotUsedInsideIf
                if (unNbrJoursDispoNegatif != null) {// Au moins 1 période de surchargée pour ce profil.
                    return true;
                }
            }
        }
        return false;
    }


    public void calculerCharges() throws ControllerException {
        LOGGER.debug("Calcul des charges  : ");
        calculateurCharges.calculer();
        tables().forEach(TableView::refresh); // Notamment pour que les cellules qui étaient vides et qui ont une valeur suite au calcul (les provisions, typiquement) soient affichées.
        LOGGER.debug("Charges calculées.");
    }


    @FXML
    private void filtrerSurRessourceTableNbrsJoursDispoCTRestanteRsrc(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        filtrerSurRessourceTableNbrsJoursDispoCTRestanteRsrc();
    }

    private void filtrerSurRessourceTableNbrsJoursDispoCTRestanteRsrc() {

        NbrsJoursParRessourceBean nbrsJoursParRessourceBean = TableViews.selectedItem(nbrsJoursDispoCTRestanteRsrcTable);
        if (nbrsJoursParRessourceBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucune ressource sélectionnée",
                    "Sélectionnez d'abord une ligne, puis recliquez.",
                    400, 100
            );
            return;
        }

        RessourceBean ressourceBean = nbrsJoursParRessourceBean.getRessourceBean();
        assert ressourceBean != null;

        filtrerSurRessource(ressourceBean);
    }

    @FXML
    private void filtrerSurProfilTableNbrsJoursChargeProfil(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        filtrerSurProfilTableNbrsJoursChargeProfil();
    }

    private void filtrerSurProfilTableNbrsJoursChargeProfil() {

        NbrsJoursParProfilBean nbrsJoursParProfilBean = TableViews.selectedItem(nbrsJoursChargeProfilTable);
        if (nbrsJoursParProfilBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucun profil sélectionné",
                    "Sélectionnez d'abord une ligne, puis recliquez.",
                    400, 100
            );
            return;
        }

        ProfilBean profilBean = nbrsJoursParProfilBean.getProfilBean();
        assert profilBean != null;

        filtrerSurProfil(profilBean);
    }

    @FXML
    private void filtrerSurRessourceTableNbrsJoursChargeRsrc(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        filtrerSurRessourceTableNbrsJoursChargeRsrc();
    }

    private void filtrerSurRessourceTableNbrsJoursChargeRsrc() {

        NbrsJoursParRessourceBean nbrsJoursParRessourceBean = TableViews.selectedItem(nbrsJoursChargeRsrcTable);
        if (nbrsJoursParRessourceBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucune ressource sélectionnée",
                    "Sélectionnez d'abord une ligne, puis recliquez.",
                    400, 100
            );
            return;
        }

        RessourceBean ressourceBean = nbrsJoursParRessourceBean.getRessourceBean();
        assert ressourceBean != null;

        filtrerSurRessource(ressourceBean);
    }

    @FXML
    private void filtrerSurProfilTableNbrsJoursDispoCTMaxRestanteProfil(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        filtrerSurProfilTableNbrsJoursDispoCTMaxRestanteProfil();
    }

    private void filtrerSurProfilTableNbrsJoursDispoCTMaxRestanteProfil() {

        NbrsJoursParProfilBean nbrsJoursParProfilBean = TableViews.selectedItem(nbrsJoursDispoCTMaxRestanteProfilTable);
        if (nbrsJoursParProfilBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucune ressource sélectionnée",
                    "Sélectionnez d'abord une ligne, puis recliquez.",
                    400, 100
            );
            return;
        }

        ProfilBean profilBean = nbrsJoursParProfilBean.getProfilBean();
        assert profilBean != null;

        filtrerSurProfil(profilBean);
    }

}
