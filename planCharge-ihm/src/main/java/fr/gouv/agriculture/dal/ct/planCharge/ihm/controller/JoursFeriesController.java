package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.RessourceHumaineBean;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import org.controlsfx.control.table.TableFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class JoursFeriesController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoursFeriesController.class);

    private static JoursFeriesController instance;

    public static JoursFeriesController instance() {
        return instance;
    }


    // Couche "métier" :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<JourFerieBean> joursFeriesBeans = planChargeBean.getJoursFeriesBeans();


    // Couche "vue" :

    @FXML
    private TableView<JourFerieBean> joursFeriesTable;

    @FXML
    private TableColumn<JourFerieBean, LocalDate> dateColumn;

    @FXML
    private TableColumn<JourFerieBean, String> descriptionColumn;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public JoursFeriesController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        PlanChargeIhm.symboliserChampObligatoire(dateColumn); // FIXME FDA 2017/07 N'affiche pas le symbole "donnée requise".
        dateColumn.setCellFactory(param -> {
            TableCell<JourFerieBean, LocalDate> cell = DatePickerTableCells.<JourFerieBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Date incorrecte", this::validerDateFeriee);
            return cell;
        });
//        PlanChargeIhm.symboliserChampObligatoire(descriptionColumn);
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

/*
        // Association (binding) entre la liste et la table des jours fériés :
        joursFeriesBeans.addListener((ListChangeListener<JourFerieBean>) changeListener -> {
            // Wrap the FilteredList in a SortedList.
            final SortedList<JourFerieBean> sortedBeans = new SortedList<>(joursFeriesBeans);
            // Bind the SortedList COMPARATOR_DEFAUT to the TableView COMPARATOR_DEFAUT.
            sortedBeans.comparatorProperty().bind(joursFeriesTable.comparatorProperty());
            // Add sorted data to the table.
            joursFeriesTable.setItems(sortedBeans);
        });
*/
        joursFeriesTable.setItems(joursFeriesBeans);

        TableFilter.Builder<JourFerieBean> filter = TableFilter.forTableView(joursFeriesTable);
//        filter.lazy(true); // TODO FDA 2017/07 Confirmer (ne semble rien changer).
        //noinspection unused
        TableFilter<JourFerieBean> ressourcesHumainesTableFilter = filter.apply();
        getIhm().symboliserFiltrable(dateColumn, descriptionColumn);

        definirMenuContextuel();
        definirTouches();

        LOGGER.debug("Initialisé.");
    }

    private void definirMenuContextuel() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuVoirTache = new MenuItem("Supprimer");
        menuVoirTache.setOnAction(this::supprimerJourFerie);

        contextMenu.getItems().setAll(menuVoirTache);

        joursFeriesTable.setContextMenu(contextMenu);
    }

    private void definirTouches() {
        // Cf. https://stackoverflow.com/questions/27314495/delete-javafx-table-row-with-delete-key
        joursFeriesTable.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE:
                    supprimerJourFerie(joursFeriesTable.getSelectionModel().getSelectedItem());
                    break;
                default:
                    LOGGER.debug("Touche ignorée : '" + event.getCode() + "'.");
            }
        });
    }

    @Null
    private String validerDateFeriee(@Null LocalDate dateFeriee) {
        if (dateFeriee == null) {
            return "Le date du jour fériée est obligatoire.";
        }
        return null;
    }

    @FXML
    private void ajouterJourFerie(@SuppressWarnings("unused") ActionEvent actionEvent) {
        LOGGER.debug("ajouterJourFerie...");

        JourFerieBean nouvJourFerieBean = new JourFerieBean();
        joursFeriesBeans.add(nouvJourFerieBean);
//        joursFeriesTable.refresh();

        // Positionnement sur le jour férié qu'on vient d'ajouter :
//        Platform.runLater(() -> {
            int idxLigNouvBean = joursFeriesTable.getItems().indexOf(nouvJourFerieBean);
            assert idxLigNouvBean != -1;
            joursFeriesTable.scrollTo(idxLigNouvBean);
            joursFeriesTable.getSelectionModel().clearAndSelect(idxLigNouvBean);
//        joursFeriesTable.getSelectionModel().focus(idxLigNouvBean);
            // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en mode édition de la cellule.
            joursFeriesTable.edit(idxLigNouvBean, dateColumn);
//        joursFeriesTable.refresh();
//        });
    }

    @FXML
    private void supprimerJourFerie(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        LOGGER.debug("supprimerJourFerie...");

        JourFerieBean focusedItem = joursFeriesTable.getFocusModel().getFocusedItem();
        if (focusedItem == null) {
            LOGGER.debug("Aucun item sélectionné, donc on en sait pas que supprimer, on ne fait rien.");
            return;
        }
        supprimerJourFerie(focusedItem);
    }

    private void supprimerJourFerie(@NotNull JourFerieBean focusedItem) {
        LOGGER.debug("supprimerJourFerie...");
        joursFeriesBeans.remove(focusedItem);
    }
}
