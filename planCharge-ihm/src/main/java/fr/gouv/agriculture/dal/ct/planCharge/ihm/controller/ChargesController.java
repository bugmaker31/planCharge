package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.PlanificationChargeCellFactory;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
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
public class ChargesController extends AbstractTachesController<PlanificationBean> {

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
    private PlanChargeService planChargeService = PlanChargeService.instance();

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<PlanificationBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    /*
     La couche "View" :
      */

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    // Les paramètres (TabedPane "Paramètres") :

    // Les filtres (TabedPane "Filtres")) :
    // Ajouter ici les filtres spécifiques des charges : Charge planifiée, Charge  planifiée dans le mois, Planifiée dans le mois ?, Tâche doublon ?, Reste à planifier, N° sem échéance, Échéance tenue ?, Durée restante, Charge / semaine, Charge / T

    // La Table :
    @NotNull
    @FXML
    private TableView<PlanificationBean> planificationsTable;
    // Les colonnes spécifiques du calendrier des tâches :
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine1Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine2Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine3Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine4Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine5Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine6Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine7Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine8Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine9Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine10Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine11Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine12Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> chargePlanifieeColumn;


    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine1Column() {
        return semaine1Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine2Column() {
        return semaine2Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine3Column() {
        return semaine3Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine4Column() {
        return semaine4Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine5Column() {
        return semaine5Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine6Column() {
        return semaine6Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine7Column() {
        return semaine7Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine8Column() {
        return semaine8Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine9Column() {
        return semaine9Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine10Column() {
        return semaine10Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine11Column() {
        return semaine11Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine12Column() {
        return semaine12Column;
    }


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
    ObservableList<PlanificationBean> getTachesBeans() {
        return planificationsBeans;
    }

    @NotNull
    @SuppressWarnings("SuspiciousGetterSetter")
    @Override
    TableView<PlanificationBean> getTachesTable() {
        return planificationsTable;
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

//        ihm.getApplicationController().getDateEtatPicker().setValue(planChargeBean.getDateEtat());

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //noinspection ClassHasNoToStringMethod
        class ChargeSemaineCellCallback implements Callback<TableColumn.CellDataFeatures<PlanificationBean, Double>, ObservableValue<Double>> {
            private final int noSemaine;

            public ChargeSemaineCellCallback(int noSemaine) {
                this.noSemaine = noSemaine;
            }

            @Null
            @Override
            public ObservableValue<Double> call(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") TableColumn.CellDataFeatures<PlanificationBean, Double> cell) {
                try {
                    PlanificationBean planifBean = cell.getValue();
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    if (!planifBean.aChargePlanifiee(debutPeriode)) {
                        // TODO FDA 2017/06 Gérér les périodes trimestrielles aussi.
                        return null;
                    }
                    DoubleProperty nouvelleCharge = planifBean.chargePlanifiee(debutPeriode);
                    return nouvelleCharge.getValue().equals(0.0) ? null : nouvelleCharge.asObject();
                } catch (IhmException e) {
                    LOGGER.error("Impossible de formatter la cellule contenant la charge de la semaine n°{} pour la tâche {}.", noSemaine, cell.getValue().noTache(), e);
                    return null;
                }
            }
        }
        semaine1Column.setCellValueFactory(new ChargeSemaineCellCallback(1));
        semaine2Column.setCellValueFactory(new ChargeSemaineCellCallback(2));
        semaine3Column.setCellValueFactory(new ChargeSemaineCellCallback(3));
        semaine4Column.setCellValueFactory(new ChargeSemaineCellCallback(4));
        semaine5Column.setCellValueFactory(new ChargeSemaineCellCallback(5));
        semaine6Column.setCellValueFactory(new ChargeSemaineCellCallback(6));
        semaine7Column.setCellValueFactory(new ChargeSemaineCellCallback(7));
        semaine8Column.setCellValueFactory(new ChargeSemaineCellCallback(8));
        semaine9Column.setCellValueFactory(new ChargeSemaineCellCallback(9));
        semaine10Column.setCellValueFactory(new ChargeSemaineCellCallback(10));
        semaine11Column.setCellValueFactory(new ChargeSemaineCellCallback(11));
        semaine12Column.setCellValueFactory(new ChargeSemaineCellCallback(12));
        chargePlanifieeColumn.setCellValueFactory(cellData -> cellData.getValue().chargePlanifieeTotaleProperty().asObject());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        //
        // Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/
        //noinspection OverlyComplexAnonymousInnerClass
        getChargeColumn().setCellFactory(column -> new TableCell<PlanificationBean, Double>() {
            @Override
            protected void updateItem(Double charge, boolean empty) {
                super.updateItem(charge, empty);

                setText("");
                getStyleClass().clear();

                if ((charge == null) || empty) {
                    setStyle(null);
                    return;
                }

                // Format :
                setText(FORMAT_CHARGE.format(charge));

                // Style with a different color:
                Double chargePlanifiee = chargePlanifieeColumn.getCellData(this.getIndex());
                if (chargePlanifiee != null) {
                    if (chargePlanifiee < charge) {
                        getStyleClass().add("chargeNonPlanifiee");
                    }
                    if (chargePlanifiee > charge) {
                        getStyleClass().add("incoherence");
                    }
                }
            }
        });
        //noinspection OverlyComplexAnonymousInnerClass
        chargePlanifieeColumn.setCellFactory(column -> new TableCell<PlanificationBean, Double>() {
            @Override
            protected void updateItem(Double chargePlanifiee, boolean empty) {
                super.updateItem(chargePlanifiee, empty);

                setText("");
                getStyleClass().clear();

                if ((chargePlanifiee == null) || empty) {
                    setStyle(null);
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
        //noinspection ClassHasNoToStringMethod
        final class ChargeSemaineEditHandler implements EventHandler<TableColumn.CellEditEvent<PlanificationBean, Double>> {

            private final int noSemaine;

            private ChargeSemaineEditHandler(int noSemaine) {
                this.noSemaine = noSemaine;
            }

            @Override
            public void handle(TableColumn.CellEditEvent<PlanificationBean, Double> event) {

                PlanificationBean planifBean = event.getRowValue();
                try {
                    LocalDate dateDebutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7);// FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    planifBean.chargePlanifiee(dateDebutPeriode).setValue(event.getNewValue());
                } catch (IhmException e) {
                    LOGGER.error("Impossible de gérer l'édition d'une cellule conternant la charge d'une semaine.", e);
                }

                planifBean.majChargePlanifieeTotale();
            }
        }
        semaine1Column.setOnEditCommit(new ChargeSemaineEditHandler(1));
        semaine2Column.setOnEditCommit(new ChargeSemaineEditHandler(2));
        semaine3Column.setOnEditCommit(new ChargeSemaineEditHandler(3));
        semaine4Column.setOnEditCommit(new ChargeSemaineEditHandler(4));
        semaine5Column.setOnEditCommit(new ChargeSemaineEditHandler(5));
        semaine6Column.setOnEditCommit(new ChargeSemaineEditHandler(6));
        semaine7Column.setOnEditCommit(new ChargeSemaineEditHandler(7));
        semaine8Column.setOnEditCommit(new ChargeSemaineEditHandler(8));
        semaine9Column.setOnEditCommit(new ChargeSemaineEditHandler(9));
        semaine10Column.setOnEditCommit(new ChargeSemaineEditHandler(10));
        semaine11Column.setOnEditCommit(new ChargeSemaineEditHandler(11));
        semaine12Column.setOnEditCommit(new ChargeSemaineEditHandler(12));
        //
        semaine1Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 1));
        semaine2Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 2));
        semaine3Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 3));
        semaine4Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 4));
        semaine5Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 5));
        semaine6Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 6));
        semaine7Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 7));
        semaine8Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 8));
        semaine9Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 9));
        semaine10Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 10));
        semaine11Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 11));
        semaine12Column.setCellFactory(col -> new PlanificationChargeCellFactory(planChargeBean, 12));

        LOGGER.debug("Initialisé.");
    }

    @Override
    public void definirMenuContextuel() {
        MenuItem menuVoirTache = new MenuItem("Voir le détail de la tâche");
        menuVoirTache.setOnAction(event -> afficherTache());

        MenuItem menuVoirOutilTicketing = new MenuItem("Voir dans l'outil de ticketing");
        menuVoirOutilTicketing.setOnAction(event -> afficherTacheDansOutilTicketing());

        tachesTableContextMenu.getItems().setAll(menuVoirTache, menuVoirOutilTicketing);
    }

    @FXML
    @NotNull
    protected PlanificationBean ajouterTache(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("ajouterTache...");
        try {
            if (planChargeBean.getDateEtat() == null) {
                throw new IhmException("Impossible d'ajouter une tâche car la date d'état n'est pas définie. Précisez une date auparavant.");
            }

            PlanificationBean planifBean = super.ajouterTache();

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new AjoutTache<>(planifBean, planChargeBean.getPlanificationsBeans()));

            ihm.getApplicationController().majBarreEtat();

            return planifBean;
        } catch (IhmException e) {
            throw new Exception("Impossible d'ajouter une tâche.", e);
        }
    }

    @Override
    PlanificationBean nouveauBean() throws IhmException {

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

        Map<LocalDate, DoubleProperty> calendrier = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
        LocalDate debutPeriode = planChargeBean.getDateEtat();
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calendrier.put(debutPeriode, new SimpleDoubleProperty(0.0));
            debutPeriode = debutPeriode.plusDays(7); // FIXME FDA 2017/07 Ne fonctionne que pour des périodes hebdomadaire, pas trimestrielle.
        }

        return new PlanificationBean(tacheBean, calendrier);
    }

    public void afficherPlanification() {

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification",
                    "Date d'état non définie."
            );
            return;
        }
        assert dateEtat != null;

        try {
            afficherPlanification(dateEtat);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher la planification.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification",
                    Exceptions.causes(e)
            );
        }
    }

    private void afficherPlanification(@NotNull LocalDate dateEtat) throws IhmException {
        LOGGER.debug("Affichage de la planification : ");
        PlanificationsDTO planificationsInitiales = planChargeBean.toPlanificationDTOs();
        PlanificationsDTO planifications = planChargeService.replanifier(planificationsInitiales, dateEtat);
        planifications
                .forEach((tache, planifTache) -> {
                    Optional<PlanificationBean> planifBeanOpt = planChargeBean.getPlanificationsBeans().parallelStream()
                            .filter(planificationBean -> planificationBean.getId() == tache.getId())
                            .findAny();
                    assert planifBeanOpt.isPresent();
                    PlanificationBean planifBean = planifBeanOpt.get();
                    Map<LocalDate, DoubleProperty> calendrierTache = planifBean.getCalendrier();
                    calendrierTache.clear();
                    planifTache.forEach((debutPeriode, charge) -> calendrierTache.put(debutPeriode, new SimpleDoubleProperty(charge)));
                });
        planificationsTable.refresh();
        LOGGER.debug("Planification affichée.");
    }

    @FXML
    private void filtrerTachesNonPlanifieesDansLeMois(@SuppressWarnings("unused") ActionEvent actionEvent) {
        // TODO FDA 2017/07 Coder.
    }


    @FXML
    private void afficherTache(@SuppressWarnings("unused") ActionEvent actionEvent) {
        afficherTache();
    }

    private void afficherTache() {
        PlanificationBean tacheBean = tacheSelectionnee();
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
            ihm.getTachesController().mettreFocusSurTache(tacheBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher la tâche " + tacheBean.getId() + ".", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la tâche",
                    Exceptions.causes(e),
                    400, 200
            );
        }
    }

}
