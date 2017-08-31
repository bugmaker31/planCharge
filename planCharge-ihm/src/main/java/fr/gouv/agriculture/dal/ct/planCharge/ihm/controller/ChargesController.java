package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurCharges;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.PlanificationChargeCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.TableViewAvecCalendrier;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

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
    private ChargeService planChargeService = ChargeService.instance();

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<PlanificationTacheBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    /*
     La couche "View" :
      */

/*
    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();
*/

    // Les paramètres (TabedPane "Paramètres") :

    // Les filtres (TabedPane "Filtres")) :
    // Ajouter ici les filtres spécifiques des charges : Charge planifiée, Charge  planifiée dans le mois, Planifiée dans le mois ?, Tâche doublon ?, Reste à planifier, N° sem échéance, Échéance tenue ?, Durée restante, Charge / semaine, Charge / T

    // La Table :
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableViewAvecCalendrier<PlanificationTacheBean, Double> planificationsTable;
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


    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine1Column() {
        return semaine1Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine2Column() {
        return semaine2Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine3Column() {
        return semaine3Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine4Column() {
        return semaine4Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine5Column() {
        return semaine5Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine6Column() {
        return semaine6Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine7Column() {
        return semaine7Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine8Column() {
        return semaine8Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine9Column() {
        return semaine9Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine10Column() {
        return semaine10Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine11Column() {
        return semaine11Column;
    }

    @NotNull
    public TableColumn<PlanificationTacheBean, Double> getSemaine12Column() {
        return semaine12Column;
    }

    /*
    La couche "Controller" :
     */

    //    @Autowired
    @NotNull
    private final CalculateurCharges calculateurCharges = new CalculateurCharges();


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ChargesController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


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


    // Module

    @Override
    public String getTitre() {
        return "Charge";
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @SuppressWarnings("OverlyLongMethod")
    @FXML
    @Override
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");

        super.initialize(); // TODO FDA 2017/05 Très redondant (le + gros est déjà initialisé par le ModuleTacheController) => améliorer le code.

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
                        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((finalNoSemaine - 1) * 7); // // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                        LocalDate finPeriode = debutPeriode.plusDays(7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                        if (!planifBean.aChargePlanifiee(debutPeriode, finPeriode)) {
                            // TODO FDA 2017/06 Gérér les périodes trimestrielles aussi.
                            return null;
                        }
                        DoubleProperty nouvelleCharge = planifBean.chargePlanifiee(debutPeriode, finPeriode);
                        return nouvelleCharge.getValue().equals(0.0) ? null : nouvelleCharge.asObject();
                    } catch (IhmException e) {
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

    @FXML
    @NotNull
    protected PlanificationTacheBean ajouterTache(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("ajouterTache...");
        try {
            if (planChargeBean.getDateEtat() == null) {
                throw new IhmException("Impossible d'ajouter une tâche car la date d'état n'est pas définie. Précisez une date auparavant.");
            }

            PlanificationTacheBean planifBean = super.ajouterTache();

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new AjoutTache<>(planifBean, planChargeBean.getPlanificationsBeans()));

            ihm.getApplicationController().majBarreEtat();

            return planifBean;
        } catch (IhmException e) {
            throw new Exception("Impossible d'ajouter une tâche.", e);
        }
    }

    @Override
    PlanificationTacheBean nouveauBean() throws IhmException {

        TacheBean tacheBean = new TacheBean(
                idTacheSuivant(),
                null,
                null,
                "(pas de ticket IDAL)",
                null,
                null,
                null,
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

        return new PlanificationTacheBean(tacheBean, calendrier);
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
        } catch (IhmException e) {
            LOGGER.error("Impossible de màj la planification (valeurs du calendrier).", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de mettre à jour la planification (valeurs du calendrier)",
                    Exceptions.causes(e)
            );
        }
    }

    private void definirValeursCalendrier(@NotNull LocalDate dateEtat) throws IhmException {
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
        } catch (ServiceException e) {
            throw new IhmException("Impossible de màj la planification.", e);
        }
    }


    @FXML
    private void filtrerTachesNonPlanifieesDansLeMois(@SuppressWarnings("unused") ActionEvent actionEvent) {
        // TODO FDA 2017/07 Coder.
    }


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


    public void calculerCharges() throws ControllerException {
        calculateurCharges.calculer();
    }
}
