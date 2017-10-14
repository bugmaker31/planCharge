package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCell;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class JoursFeriesController extends AbstractController implements Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoursFeriesController.class);

    private static JoursFeriesController instance;

    public static JoursFeriesController instance() {
        return instance;
    }


    // Couche "métier" :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<JourFerieBean> joursFeriesBeans = planChargeBean.getJoursFeriesBeans();

    //    @Autowired
    @NotNull
    private final ReferentielsService referentielsService = ReferentielsService.instance();


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
    public JoursFeriesController() throws ControllerException {
        super();
        if (instance != null) {
            throw new ControllerException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    // Module

    @Override
    public String getTitre() {
        return "Jours fériés";
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws ControllerException {
        try {

//            joursFeriesTable.setItems(joursFeriesBeans);

            // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
            dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

            // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
            TableViews.decorateMandatoryColumns(dateColumn);
            dateColumn.setCellFactory(param -> {
                DatePickerTableCell<JourFerieBean> cell = (DatePickerTableCell<JourFerieBean>) DatePickerTableCells.<JourFerieBean>forRequiredTableColumn().call(param);
                ihm.controler(cell, /*cell.valueProperty(),*/"Date incorrecte", this::validerDateFeriee);
                return cell;
            });
//        PlanChargeIhm.decorateMandatoryColumns(descriptionColumn);
            descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            TableViews.ensureSorting(joursFeriesTable, joursFeriesBeans);

            TableViews.enableFilteringOnColumns(joursFeriesTable, Arrays.asList(dateColumn, descriptionColumn));

            definirMenuContextuel();

            definirRaccourcisClaviers();

        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur du module 'Jours fériés'.", e);
        }
    }

    //    TODO FDA 2017/09 Déporter dans le FXML.
    private void definirMenuContextuel() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuVoirTache = new MenuItem("Supprimer");
        menuVoirTache.setOnAction(this::supprimerJourFerie);

        contextMenu.getItems().setAll(menuVoirTache);

        joursFeriesTable.setContextMenu(contextMenu);
    }

    private void definirRaccourcisClaviers() {
        // Cf. https://stackoverflow.com/questions/27314495/delete-javafx-table-row-with-delete-key
        joursFeriesTable.setOnKeyPressed(event -> {
            //noinspection EnumSwitchStatementWhichMissesCases
            switch (event.getCode()) {
                case DELETE:
                    supprimerJourFerie();
                    break;
                default:
                    LOGGER.debug("Touche ignorée : '{}'.", event.getCode());
            }
        });
    }

    @Null
    private String validerDateFeriee(@Null LocalDate dateFeriee) {
        if (dateFeriee == null) {
            return "La date du jour férié est obligatoire.";
        }
        return null;
    }

    @FXML
    private void ajouterJourFerie(@SuppressWarnings("unused") ActionEvent actionEvent) {
        LOGGER.debug("ajouterJourFerie...");

        JourFerieBean nouvJourFerieBean = new JourFerieBean();
        joursFeriesBeans.add(nouvJourFerieBean);

        // Positionnement sur le jour férié qu'on vient d'ajouter :
        TableViews.editCell(joursFeriesTable, nouvJourFerieBean, dateColumn);
    }

    @FXML
    private void supprimerJourFerie(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        LOGGER.debug("supprimerJourFerie...");
        supprimerJourFerie();
    }

    private void supprimerJourFerie() {
        JourFerieBean focusedItem = TableViews.selectedItem(joursFeriesTable);
        if (focusedItem == null) {
            LOGGER.debug("Aucun item sélectionné, donc on en sait pas que supprimer, on ne fait rien.");
            return;
        }
        supprimerJourFerie(focusedItem);
    }

    private void supprimerJourFerie(@NotNull JourFerieBean focusedItem) {
        joursFeriesBeans.remove(focusedItem);
    }
}
