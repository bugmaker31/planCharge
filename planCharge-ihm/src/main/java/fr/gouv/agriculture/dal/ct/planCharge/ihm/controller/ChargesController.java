package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurCharges;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.NbrsJoursParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.StatutBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
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
    private Spinner<Integer> nbrLignesTablePlanificationsSpinner;

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
    private TableColumn<PlanificationTacheBean, Double> chargePlanifieeColumn;

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

    /*
    La couche "Controller" :
     */

    //    @Autowired
    @NotNull
    private final CalculateurCharges calculateurCharges = new CalculateurCharges();


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
        LOGGER.debug("Initialisation...");
        try {
            Calculateur.executerSansCalculer(() -> {

                super.initialize(); // TODO FDA 2017/05 Très redondant (le + gros est déjà initialisé par le ModuleTacheController) => améliorer le code.

                initBeans();
                initTables();
            });
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur.", e);
        }
        LOGGER.info("Initialisé.");
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
    }

    private void initBeansPlanifications() {
        // Rien de plus... pour l'instant.
    }

    private void initBeansNbrsJoursChargeRsrc() {
        planChargeBean.getRessourcesBeans().addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursParRessourceBean> nbrsJoursChargeBeansAAjouter = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (Collections.any(nbrsJoursChargeRsrcBeans, nbrsJoursBean -> nbrsJoursBean.getRessourceBean().equals(ressourceBean)) != null) {
                            continue;
                        }
                        try {
                            Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                            nbrsJoursChargeBeansAAjouter.add(new NbrsJoursParRessourceBean(ressourceBean, calendrier));
                        } catch (BeanException e) {
                            // TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
                            LOGGER.error("Impossible d'ajouter la ressource " + ressourceBean.getCode() + ".", e);
                        }
                    }
                    nbrsJoursChargeRsrcBeans.addAll(nbrsJoursChargeBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursParRessourceBean> nbrsJoursChargeBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursChargeRsrcBeans.parallelStream().noneMatch(nbrsJoursBean -> nbrsJoursBean.getRessourceBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        try {
                            Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                            nbrsJoursChargeBeansASupprimer.add(new NbrsJoursParRessourceBean(ressourceHumaineBean, calendrier));
                        } catch (BeanException e) {
                            // TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
                            LOGGER.error("Impossible d'ajouter la ressource " + ressourceBean.getCode() + ".", e);
                        }
                    }
                    if (!nbrsJoursChargeBeansASupprimer.isEmpty()) {
                        nbrsJoursChargeRsrcBeans.removeAll(nbrsJoursChargeBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }

    private void initBeansNbrsJoursChargeProfil() {
        planChargeBean.getProfilsBeans().addListener((ListChangeListener<? super ProfilBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursParProfilBean> nbrsJoursChargeBeansAAjouter = new ArrayList<>();
                    for (ProfilBean profilBean : change.getAddedSubList()) {
                        if (Collections.any(nbrsJoursChargeProfilBeans, nbrsJoursBean -> nbrsJoursBean.getProfilBean().equals(profilBean)) != null) {
                            continue;
                        }
                        try {
                            Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                            nbrsJoursChargeBeansAAjouter.add(new NbrsJoursParProfilBean(profilBean, calendrier));
                        } catch (BeanException e) {
                            // TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
                            LOGGER.error("Impossible d'ajouter la ressource " + profilBean.getCode() + ".", e);
                        }
                    }
                    nbrsJoursChargeProfilBeans.addAll(nbrsJoursChargeBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursParProfilBean> nbrsJoursChargeBeansASupprimer = new ArrayList<>();
                    for (ProfilBean profilBean : change.getRemoved()) {
                        if (nbrsJoursChargeProfilBeans.parallelStream().noneMatch(nbrsJoursBean -> nbrsJoursBean.getProfilBean().equals(profilBean))) {
                            continue;
                        }
                        try {
                            Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                            nbrsJoursChargeBeansASupprimer.add(new NbrsJoursParProfilBean(profilBean, calendrier));
                        } catch (BeanException e) {
                            // TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
                            LOGGER.error("Impossible d'ajouter le profil " + profilBean.getCode() + ".", e);
                        }
                    }
                    if (!nbrsJoursChargeBeansASupprimer.isEmpty()) {
                        nbrsJoursChargeProfilBeans.removeAll(nbrsJoursChargeBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }

    private void initBeansNbrsJoursDispoCTRestanteRsrc() {
        planChargeBean.getRessourcesBeans().addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursParRessourceBean> nbrsJoursChargeBeansAAjouter = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (Collections.any(nbrsJoursDispoCTRestanteRsrcBeans, nbrsJoursBean -> nbrsJoursBean.getRessourceBean().equals(ressourceBean)) != null) {
                            continue;
                        }
                        try {
                            Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                            nbrsJoursChargeBeansAAjouter.add(new NbrsJoursParRessourceBean(ressourceBean, calendrier));
                        } catch (BeanException e) {
                            // TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
                            LOGGER.error("Impossible d'ajouter la ressource " + ressourceBean.getCode() + ".", e);
                        }
                    }
                    nbrsJoursDispoCTRestanteRsrcBeans.addAll(nbrsJoursChargeBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursParRessourceBean> nbrsJoursChargeBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDispoCTRestanteRsrcBeans.parallelStream().noneMatch(nbrsJoursBean -> nbrsJoursBean.getRessourceBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        try {
                            Map<LocalDate, FloatProperty> calendrier = new TreeMap<>();
                            nbrsJoursChargeBeansASupprimer.add(new NbrsJoursParRessourceBean(ressourceHumaineBean, calendrier));
                        } catch (BeanException e) {
                            // TODO FDA 2017/09 Trouver mieux que juste loguer une erreur.
                            LOGGER.error("Impossible d'ajouter la ressource " + ressourceBean.getCode() + ".", e);
                        }
                    }
                    if (!nbrsJoursChargeBeansASupprimer.isEmpty()) {
                        nbrsJoursDispoCTRestanteRsrcBeans.removeAll(nbrsJoursChargeBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                    }
                }
                // TODO FDA 2017/08 Coder les autres changements (permutations, etc. ?).
            }
        });
    }


    public List<TableViewAvecCalendrier<?, ?>> tables() {
        return Arrays.asList(new TableViewAvecCalendrier<?, ?>[]{
                planificationsTable,
                nbrsJoursChargeRsrcTable,
                nbrsJoursChargeProfilTable,
                nbrsJoursDispoCTRestanteRsrcTable
        });
    }

    private void initTables() {
        initTablePlanifications();
        initTableNbrsJoursChargeRsrc();
        initTableNbrsJoursChargeProfil();
        initTableNbrsJoursDispoCTRestanteRsrc();

        synchroniserLargeurColonnes();
    }

    private void initTablePlanifications() {

        planificationsTable.setCalendrierColumns(
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
        );

//        ihm.getApplicationController().getDateEtatPicker().setValue(planChargeBean.getDateEtat());

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            TableColumn<PlanificationTacheBean, Double> column = planificationsTable.getCalendrierColumns().get(noSemaine - 1);
            int finalNoSemaine = noSemaine;
            //noinspection OverlyComplexAnonymousInnerClass
            column.setCellValueFactory(new Callback<CellDataFeatures<PlanificationTacheBean, Double>, ObservableValue<Double>>() {

                @Null
                @Override
                public ObservableValue<Double> call(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") CellDataFeatures<PlanificationTacheBean, Double> cell) {
                    if (planChargeBean.getDateEtat() == null) {
                        return null;
                    }
                    try {
                        PlanificationTacheBean planifBean = cell.getValue();
                        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays(((long) finalNoSemaine - 1L) * 7L); // // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                        LocalDate finPeriode = debutPeriode.plusDays(7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                        if (!planifBean.aChargePlanifiee(debutPeriode, finPeriode)) {
                            // TODO FDA 2017/06 Gérér les périodes trimestrielles aussi.
                            return null;
                        }
                        DoubleProperty nouvelleCharge = planifBean.chargePlanifiee(debutPeriode, finPeriode);
                        return nouvelleCharge.getValue().equals(0.0) ? null : nouvelleCharge.asObject();
                    } catch (BeanException e) {
                        LOGGER.error("Impossible de formatter la cellule contenant la charge de la semaine n°" + finalNoSemaine + " pour la tâche " + cell.getValue().noTache() + ".", e);
                        return null;
                    }
                }
            });
        }
        chargePlanifieeColumn.setCellValueFactory(cellData -> cellData.getValue().chargePlanifieeTotaleProperty().asObject());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") et
        // du formatage qui symbolise les incohérences/surcharges/etc. (Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/) :
        //
        //noinspection OverlyComplexAnonymousInnerClass
        getChargeColumn().setCellFactory(column -> new TextFieldTableCell<PlanificationTacheBean, Double>() {

            @Override
            public void updateItem(Double item, boolean empty) {
                if (getConverter() == null) {
                    setConverter(new DoubleStringConverter()); // TODO FDA 2017/08 Mieux formater.
                }
                super.updateItem(item, empty);

                styler(item);

/*
                if ((item == null) || empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
*/
            }

            private void styler(@Null Double item) {
                setText("");
                getStyleClass().removeAll("chargeNonPlanifiee", "incoherence");

                if (item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                // Format :
                setText(FORMAT_CHARGE.format(item));

                // Style with a different color:
                Double chargePlanifiee = chargePlanifieeColumn.getCellData(getIndex());
                if (chargePlanifiee != null) {
                    if (chargePlanifiee < item) {
                        getStyleClass().add("chargeNonPlanifiee");
                    }
                    if (chargePlanifiee > item) {
                        getStyleClass().add("incoherence");
                    }
                }
            }
        });
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            TableColumn<PlanificationTacheBean, Double> column = planificationsTable.getCalendrierColumns().get(noSemaine - 1);
            int finalNoSemaine = noSemaine;
            column.setCellFactory(col -> new PlanificationChargeCell(planChargeBean, finalNoSemaine));
        }
        ihm.interdireEdition(chargePlanifieeColumn, "Cette colonne n'est pas saisissable, elle est calculée.");
        //noinspection OverlyComplexAnonymousInnerClass
        chargePlanifieeColumn.setCellFactory(column -> new TableCell<PlanificationTacheBean, Double>() {
            @Override
            protected void updateItem(Double chargePlanifiee, boolean empty) {
                super.updateItem(chargePlanifiee, empty);

                setText("");
                getStyleClass().removeAll("incoherence");

                if ((chargePlanifiee == null) || empty) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                // Format :
                setText(FORMAT_CHARGE.format(chargePlanifiee));

                // Style with a different color:
                Double charge = getChargeColumn().getCellData(this.getIndex());
                if (chargePlanifiee > charge) {
                    getStyleClass().add("incoherence");
                }
            }
        });

//        planificationsTable.getSelectionModel().setCellSelectionEnabled(true);

        TableViews.ensureDisplayingRows(planificationsTable, 30);

        nbrLignesTablePlanificationsSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(oldValue, newValue)) {
                return;
            }
            if (newValue == null) {
                newValue = 30;
            }
            TableViews.ensureDisplayingRows(planificationsTable, newValue);
        });
    }

    private void initTableNbrsJoursChargeRsrc() {

        nbrsJoursChargeRsrcTable.setCalendrierColumns(
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
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursChargeRsrcColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().codeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursChargeRsrcColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParRessourceBean, Float> column : nbrsJoursChargeRsrcTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(planChargeBean, cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursChargeRsrcColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursChargeRsrcColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParRessourceBean, Float> column : nbrsJoursChargeRsrcTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellFactory(cell -> new EditableAwareTextFieldTableCell<>(Converters.FRACTION_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de charge par ressource est calculé.")));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursChargeRsrcTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursChargeRsrcTable.setItems(nbrsJoursChargeRsrcBeans);

        TableViews.disableReagencingColumns(nbrsJoursChargeRsrcTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursChargeRsrcTable);
    }

    private void initTableNbrsJoursChargeProfil() {

        nbrsJoursChargeProfilTable.setCalendrierColumns(
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
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection HardcodedFileSeparator
        ressourceNbrsJoursChargeProfilColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        profilNbrsJoursChargeProfilColumn.setCellValueFactory(cell -> cell.getValue().getProfilBean().codeProperty());
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParProfilBean, Float> column : nbrsJoursChargeProfilTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(planChargeBean, cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursChargeProfilColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursChargeProfilColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParProfilBean, Float> column : nbrsJoursChargeProfilTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellFactory(cell -> new EditableAwareTextFieldTableCell<>(Converters.FRACTION_JOURS_STRING_CONVERTER, () -> ihm.afficherInterdictionEditer("Le nombre de jours de charge par profil est calculé.")));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursChargeProfilTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursChargeProfilTable.setItems(nbrsJoursChargeProfilBeans);

        TableViews.disableReagencingColumns(nbrsJoursChargeProfilTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursChargeProfilTable);
    }

    private void initTableNbrsJoursDispoCTRestanteRsrc() {

        nbrsJoursDispoCTRestanteRsrcTable.setCalendrierColumns(
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
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        ressourceNbrsJoursDispoCTRestanteRsrcColumn.setCellValueFactory(cell -> cell.getValue().getRessourceBean().codeProperty());
        //noinspection HardcodedFileSeparator
        profilNbrsJoursDispoCTRestanteRsrcColumn.setCellValueFactory(cell -> new SimpleStringProperty("N/A"));
        {
            int cptColonne = 0;
            for (TableColumn<NbrsJoursParRessourceBean, Float> column : nbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns()) {
                cptColonne++;
                column.setCellValueFactory(new CalendrierFractionsJoursCellCallback<>(planChargeBean, cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(ressourceNbrsJoursDispoCTRestanteRsrcColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        ihm.interdireEdition(profilNbrsJoursDispoCTRestanteRsrcColumn, "Cette colonne reprend les profils (ajouter un profil pour ajouter une ligne dans cette table).");
        for (TableColumn<NbrsJoursParRessourceBean, Float> column : nbrsJoursDispoCTRestanteRsrcTable.getCalendrierColumns()) {
            column.setCellFactory(cell -> new ChargePlanifieeCell<>(() -> ihm.afficherInterdictionEditer("Le nombre de jours de dispo. CT restante  est calculé.")));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).
        TableViews.ensureSorting(nbrsJoursDispoCTRestanteRsrcTable);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Définition du contenu de la table (ses lignes) :
        nbrsJoursDispoCTRestanteRsrcTable.setItems(nbrsJoursDispoCTRestanteRsrcBeans);

        TableViews.disableReagencingColumns(nbrsJoursDispoCTRestanteRsrcTable);
        TableViews.ensureDisplayingAllRows(nbrsJoursDispoCTRestanteRsrcTable);
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

        assert planChargeBean.getDateEtat() != null;

        Map<LocalDate, DoubleProperty> calendrier = new TreeMap<>(); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
        LocalDate debutSemaine = planChargeBean.getDateEtat();
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calendrier.put(debutSemaine, new SimpleDoubleProperty(0.0));
            debutSemaine = debutSemaine.plusDays(7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
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
            ihm.afficherPopUp(
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
            ihm.afficherPopUp(
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
            planifications
                    .forEach((tache, planifTache) -> {
                        Optional<PlanificationTacheBean> planifBeanOpt = planChargeBean.getPlanificationsBeans().parallelStream()
                                .filter(planificationBean -> planificationBean.getId() == tache.getId())
                                .findAny();
                        assert planifBeanOpt.isPresent();
                        PlanificationTacheBean planifBean = planifBeanOpt.get();
                        Map<LocalDate, DoubleProperty> calendrierTache = planifBean.getCalendrier();
                        calendrierTache.clear();
                        planifTache.forEach((debutPeriode, charge) -> calendrierTache.put(debutPeriode, new SimpleDoubleProperty(charge)));
                    });
            planificationsTable.refresh();
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
            ihm.afficherPopUp(
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
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la tâche" + tacheBean.noTache(),
                    Exceptions.causes(e),
                    400, 200
            );
        }
    }


    @Override
    protected boolean estTacheAvecAutreFiltreAVoir(@NotNull PlanificationTacheBean tache) throws BeanException {
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
            if (tache.chargePlanifiee(debutPeriode, finPeriode).getValue() > 0.0) { // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
                return true;
            }
        }
        return false;
    }


    public void calculerCharges() throws ControllerException {
        LOGGER.debug("Calcul des disponibilités  : ");
        calculateurCharges.calculer();
        tables().forEach(TableView::refresh); // Notamment pour que les cellules qui étaient vides et qui ont une valeur suite au calcul (les provisions, typiquement) soient affichées.
        LOGGER.debug("Disponibilités calculées.");
    }
}
