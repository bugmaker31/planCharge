package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleChargesController extends AbstractTachesController<PlanificationBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleChargesController.class);


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

    @NotNull
    public DatePicker getDateEtatPicker() {
        return dateEtatPicker;
    }


    /*
     La couche "View" :
      */

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    // Les paramètres (TabedPane "Paramètres") :
    @FXML
    private DatePicker dateEtatPicker;

    // Les filtres (TabedPane "Filtres")) :
    // Ajouter ici les filtres spécifiques des charges : Charge planifiée, Charge  planifiée dans le mois, Planifiée dans le mois ?, Tâche doublon ?, Reste à planifier, N° sem échéance, Échéance tenue ?, Durée restante, Charge / semaine, Charge / T

    // La Table :
    // Les colonnes spécifiques du calendrier des tâches :
    @FXML
    private TableColumn<PlanificationBean, Double> semaine1Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine2Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine3Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine4Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine5Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine6Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine7Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine8Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine9Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine10Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine11Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine12Column;
    @FXML
    private TableColumn<PlanificationBean, Double> chargePlanifieeColumn;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleChargesController() {
        super();
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    @Override
    void initialize() {
        setTachesBeans(planificationsBeans);

        super.initialize();

        dateEtatPicker.setValue(planChargeBean.getDateEtat());

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        class ChargeSemaineCellCallback implements Callback<TableColumn.CellDataFeatures<PlanificationBean, Double>, ObservableValue<Double>> {
            private final int noSemaine;

            private ChargeSemaineCellCallback(int noSemaine) {
                this.noSemaine = noSemaine;
            }

            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<PlanificationBean, Double> cell) {
                PlanificationBean planifBean = cell.getValue();
                Double nouvelleCharge = planifBean.charge(noSemaine).doubleValue();
                return nouvelleCharge.equals(0.0) ? null : new SimpleDoubleProperty(nouvelleCharge).asObject();
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

                if (charge == null || empty) {
                    setText(null);
                    setStyle("");
                } else {

                    // Format.
                    setText(charge.toString()); // TODO FDA 2017/04 Mieux formater les charges ?

                    // Style with a different color.
                    //            TODO FDA 2017/04 Terminer.
                    String styleBackgroundColour;
                    double chargePlanifiee = planificationsBeans.get(this.getIndex()).getChargePlanifiee();
                    if (charge < chargePlanifiee) {
                        setTextFill(Color.WHITE);
                        styleBackgroundColour = "maroon";
                    } else {
                        setTextFill(Color.BLACK);
                        styleBackgroundColour = "black";
                    }
                    setStyle("-fx-background-color: " + styleBackgroundColour + "; -fx-alignment: center-right");
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
                planifBean.charge(noSemaine).setValue(event.getNewValue());

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
        semaine1Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine2Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine3Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine4Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine5Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine6Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine7Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine8Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine9Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine10Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine11Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine12Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        definirNomsColonnesSemaine();
    }

    private void definirNomsColonnesSemaine() {
        // TODO FDA 2017/04 Coder.
    }

    @FXML
    private void definirDateEtat(ActionEvent event) {
        LOGGER.debug("definirDateEtat...");
        LocalDate dateEtat = dateEtatPicker.getValue();
        ihm.definirDateEtat(dateEtat);
    }

    @FXML
    private void positionnerDateEtatAuProchainLundi(ActionEvent event) {
        LOGGER.debug("positionnerDateEtatAuProchainLundi...");

        LocalDate dateEtat = LocalDate.now();
        if (dateEtat.getDayOfWeek() != DayOfWeek.MONDAY) {
            dateEtat = dateEtat.plusDays(7 - dateEtat.getDayOfWeek().getValue() + 1);
        }

        ihm.definirDateEtat(dateEtat);
    }

    @FXML
    @Override
    protected void ajouterTache(ActionEvent event) {
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

        // TODO FDA 2017/05 Scroller la PlanifTable jusqu'à la tâche qu'on vient d'ajouter.

//        return planifBean;
    }

    @Override
    PlanificationBean nouveauBean() {

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
                0.0,
                null,
                null
        );

        assert planChargeBean.getDateEtat() != null;
        List<Pair<LocalDate, DoubleProperty>> calendrier = new ArrayList<>(Planifications.NBR_SEMAINES_PLANIFIEES);
        LocalDate dateSemaine = planChargeBean.getDateEtat();
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calendrier.add(new Pair(dateSemaine, new SimpleDoubleProperty(0.0)));
            dateSemaine = dateSemaine.plusDays(7);
        }

        return new PlanificationBean(tacheBean, calendrier);
    }
}
