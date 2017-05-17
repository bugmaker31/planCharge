package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ModificationDateEtat;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.PlanificationChargeCellFactory;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleChargesController extends AbstractTachesController<PlanificationBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleChargesController.class);

    private static ModuleChargesController instance;

    public static ModuleChargesController instance() {
        return instance;
    }

    private static final DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance());

    static {
        df.setMaximumFractionDigits(3);
    }


    /*
     La couche métier :
      */

/*
//    @Autowired
    @NotNull
    private PlanChargeService planChargeService = PlanChargeService.instance();
*/

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

    @FXML
    @NotNull
    private DatePicker dateEtatPicker;

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


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleChargesController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    @Override
    ObservableList<PlanificationBean> getTachesBeans() {
        return planificationsBeans;
    }

    @Override
    TableView<PlanificationBean> getTachesTable() {
        return planificationsTable;
    }

    @NotNull
    public DatePicker getDateEtatPicker() {
        return dateEtatPicker;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    @Override
    void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");

        super.initialize(); // TODO FDA 2017/05 Très redondant (le + gros est déjà initialisé par le ModuleTacheController) => améliorer le code.

        dateEtatPicker.setValue(planChargeBean.getDateEtat());

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        class ChargeSemaineCellCallback implements Callback<TableColumn.CellDataFeatures<PlanificationBean, Double>, ObservableValue<Double>> {
            private final int noSemaine;

            private ChargeSemaineCellCallback(int noSemaine) {
                this.noSemaine = noSemaine;
            }

            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<PlanificationBean, Double> cell) {
                try {
                    PlanificationBean planifBean = cell.getValue();
                    Double nouvelleCharge = planifBean.charge(noSemaine).doubleValue();
                    return nouvelleCharge.equals(0.0) ? null : new SimpleDoubleProperty(nouvelleCharge).asObject();
                } catch (IhmException e) {
                    LOGGER.error("Impossible de formatter la cellule contenant la charge d'une semaine.", e);
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
        chargePlanifieeColumn.setCellValueFactory(cellData -> cellData.getValue().chargePlanifieeProperty().asObject());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        //
        // Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/
        getChargeColumn().setCellFactory(column -> new TableCell<PlanificationBean, Double>() {
            @Override
            protected void updateItem(Double charge, boolean empty) {
                super.updateItem(charge, empty);

                if ((charge == null) || empty) {
                    setStyle(null);
                    return;
                }

                // Format :
                setText(df.format(charge));

                // Style with a different color:
                Double chargePlanifiee = chargePlanifieeColumn.getCellData(this.getIndex());
                if (chargePlanifiee != null) {
                    if (chargePlanifiee < charge) {
                        getStyleClass().setAll("chargeNonPlanifiee");
                    }
                    if (chargePlanifiee > charge) {
                        getStyleClass().setAll("incoherence");
                        // TODO FDA 2017/05 Afficher la cellule "Charge planifiée" en incohérence aussi (les incohérences vont tjs par paire).
                    }
                }
            }
        });
        class ChargeSemaineEditHandler implements EventHandler<TableColumn.CellEditEvent<PlanificationBean, Double>> {

            private final int noSemaine;

            public ChargeSemaineEditHandler(int noSemaine) {
                this.noSemaine = noSemaine;
            }

            @Override
            public void handle(TableColumn.CellEditEvent<PlanificationBean, Double> event) {

                PlanificationBean planifBean = event.getRowValue();
                try {
                    planifBean.charge(noSemaine).setValue(event.getNewValue());
                } catch (IhmException e) {
                    LOGGER.error("Impossible de gérer l'édition d'une cellule conternant la charge d'une semaine.", e);
                }

                planifBean.majChargePlanifiee();
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
        semaine1Column.setCellFactory(col -> new PlanificationChargeCellFactory(1));
        semaine2Column.setCellFactory(col -> new PlanificationChargeCellFactory(2));
        semaine3Column.setCellFactory(col -> new PlanificationChargeCellFactory(3));
        semaine4Column.setCellFactory(col -> new PlanificationChargeCellFactory(4));
        semaine5Column.setCellFactory(col -> new PlanificationChargeCellFactory(5));
        semaine6Column.setCellFactory(col -> new PlanificationChargeCellFactory(6));
        semaine7Column.setCellFactory(col -> new PlanificationChargeCellFactory(7));
        semaine8Column.setCellFactory(col -> new PlanificationChargeCellFactory(8));
        semaine9Column.setCellFactory(col -> new PlanificationChargeCellFactory(9));
        semaine10Column.setCellFactory(col -> new PlanificationChargeCellFactory(10));
        semaine11Column.setCellFactory(col -> new PlanificationChargeCellFactory(11));
        semaine12Column.setCellFactory(col -> new PlanificationChargeCellFactory(12));

        LOGGER.debug("Initialisé.");
    }

    private void definirNomsPeriodes() {

        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

        LocalDate date = planChargeBean.getDateEtat();

        semaine1Column.setText("S+1\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine2Column.setText("S+2\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine3Column.setText("S+3\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine4Column.setText("S+4\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine5Column.setText("S+5\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine6Column.setText("S+6\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine7Column.setText("S+7\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine8Column.setText("S+8\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine9Column.setText("S+9\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine10Column.setText("S+10\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine11Column.setText("S+11\n" + date.format(dateFormatter));
        date = date.plusDays(7);

        semaine12Column.setText("S+12\n" + date.format(dateFormatter));
    }

    @FXML
    private void definirDateEtat(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("definirDateEtat...");

        LocalDate dateEtat = dateEtatPicker.getValue();
        if (dateEtat.getDayOfWeek() != DayOfWeek.MONDAY) {
            dateEtat = dateEtat.plusDays((7 - dateEtat.getDayOfWeek().getValue()) + 1);
            dateEtatPicker.setValue(dateEtat);
        }

        ihm.definirDateEtat(dateEtat);
        definirNomsPeriodes();

        planChargeBean.vientDEtreModifie();
        getSuiviActionsUtilisateur().push(new ModificationDateEtat());

        ihm.majBarreEtat();
    }

    @FXML
    private void positionnerDateEtatAuLundiSuivant(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("positionnerDateEtatAuLundiSuivant...");

        LocalDate dateEtat;
        if (planChargeBean.getDateEtat() == null) {
            dateEtat = LocalDate.now();
            if (dateEtat.getDayOfWeek() != DayOfWeek.MONDAY) {
                dateEtat = dateEtat.plusDays((7 - dateEtat.getDayOfWeek().getValue()) + 1);
            }
        } else {
            assert planChargeBean.getDateEtat().getDayOfWeek() == DayOfWeek.MONDAY;
            dateEtat = planChargeBean.getDateEtat().plusDays(7);
        }
        assert dateEtat.getDayOfWeek() == DayOfWeek.MONDAY;

        ihm.definirDateEtat(dateEtat);
        definirNomsPeriodes();

        planChargeBean.vientDEtreModifie();
        getSuiviActionsUtilisateur().push(new ModificationDateEtat());

        ihm.majBarreEtat();
    }

    @FXML
    @Override
    protected void ajouterTache(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("ajouterTache...");

        if (planChargeBean.getDateEtat() == null) {
            LOGGER.warn("Impossible d'ajouter une tâche car la date d'état n'est pas définie.");
            ihm.afficherPopUp(
                    Alert.AlertType.WARNING,
                    "Impossible d'ajouter une tâche",
                    "Impossible d'ajouter une tâche car la date d'état n'est pas définie. Précisez une date auparavant.",
                    500, 200
            );
            return;
        }

        /*PlanificationBean planifBean = */
        super.ajouterTache(event);

        planChargeBean.vientDEtreModifie();
        getSuiviActionsUtilisateur().push(new AjoutTache());

        ihm.majBarreEtat();
//        return planifBean;
    }

    @Override
    PlanificationBean nouveauBean() throws IhmException {

        TacheBean tacheBean = ihm.getTachesController().nouveauBean();

        assert planChargeBean.getDateEtat() != null;
        List<Pair<LocalDate, DoubleProperty>> calendrier = new ArrayList<>(Planifications.NBR_SEMAINES_PLANIFIEES);
        LocalDate dateSemaine = planChargeBean.getDateEtat();
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calendrier.add(new Pair<>(dateSemaine, new SimpleDoubleProperty(0.0)));
            dateSemaine = dateSemaine.plusDays(7);
        }

        return new PlanificationBean(tacheBean, calendrier);
    }
}
