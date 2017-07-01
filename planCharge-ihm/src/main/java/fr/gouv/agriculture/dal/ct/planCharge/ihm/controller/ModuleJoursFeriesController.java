package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.javafx.DatePickerCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.JourFerieBean;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class ModuleJoursFeriesController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleJoursFeriesController.class);

    private static ModuleJoursFeriesController instance;

    @NotNull
    private ObservableList<JourFerieBean> joursFeriesBeans = FXCollections.observableArrayList();

    @FXML
    private TableView<JourFerieBean> joursFeriesTable;

    @FXML
    private TableColumn<JourFerieBean, String> dateColumn;

    @FXML
    private TableColumn<JourFerieBean, String> descriptionColumn;

    public static ModuleJoursFeriesController instance() {
        return instance;
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleJoursFeriesController() throws IhmException {
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
    void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        dateColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().dateProperty().isNull().get()) {
                //noinspection ReturnOfNull
                return null;
            }
            LocalDate debut = cellData.getValue().dateProperty().get();
            return new SimpleStringProperty((debut == null) ? "" : debut.format(DateTimeFormatter.ofPattern(PlanChargeIhm.FORMAT_DATE)));
        });
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        dateColumn.setCellFactory(cell -> new DatePickerCell<>(PlanChargeIhm.FORMAT_DATE));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Association (binding) entre la liste et la table des jours fériés :
        joursFeriesBeans.addListener((ListChangeListener<JourFerieBean>) changeListener -> {
            joursFeriesTable.setItems(joursFeriesBeans);
        });

        LOGGER.debug("Initialisé.");
    }

    @FXML
    private void ajouterJourFerie(@SuppressWarnings("unused") ActionEvent actionEvent) {
        LOGGER.debug("ajouterJourFerie...");

        JourFerieBean nouvJourFerie = new JourFerieBean();
        joursFeriesBeans.add(nouvJourFerie);

        // Positionnement sur la tâche qu'on vient d'ajouter :
        int noLigNouvBean = joursFeriesTable.getItems().size();
        joursFeriesTable.scrollTo(noLigNouvBean - 1);
        joursFeriesTable.scrollToColumn(dateColumn);
        joursFeriesTable.getSelectionModel().select(noLigNouvBean - 1);
        // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en modé édition de la cellule.
//            joursFeriesTable.getSelectionModel().select(noLigNouvBean - 1, descriptionColumn);
//            joursFeriesTable.edit(joursFeriesTable.getSelectionModel().getFocusedIndex(), descriptionColumn);
        joursFeriesTable.edit(noLigNouvBean - 1, dateColumn);
    }
}
