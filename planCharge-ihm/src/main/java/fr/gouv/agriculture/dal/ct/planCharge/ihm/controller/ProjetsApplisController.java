package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.util.ObservableLists;
import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.ihm.view.UpperCaseTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProjetAppliBean;
import javafx.collections.FXCollections;
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

/**
 * Created by frederic.danna on 13/07/2017.
 *
 * @author frederic.danna
 */
public class ProjetsApplisController extends AbstractController implements Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjetsApplisController.class);

    private static ProjetsApplisController instance;

    public static ProjetsApplisController instance() {
        return instance;
    }


    // Couche "métier" :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<ProjetAppliBean> projetsApplisBeans = FXCollections.observableArrayList();

/*
    //    @Autowired
    @NotNull
    private final ReferentielsService referentielsService = ReferentielsService.instance();
*/


    // Couche "vue" :

    /*
        //    @Autowired
        @NotNull
        private PlanChargeIhm ihm = PlanChargeIhm.instance();
    */
    @FXML
    @NotNull
    private TableView<ProjetAppliBean> projetsApplisTable;
    @FXML
    @NotNull
    private TableColumn<ProjetAppliBean, String> codeColumn;
    @FXML
    @NotNull
    private TableColumn<ProjetAppliBean, String> nomColumn;
    @FXML
    @NotNull
    private TableColumn<ProjetAppliBean, String> trigrammeCPIColumn;


    // Constructeurs :

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ProjetsApplisController() throws ControllerException {
        super();
        if (instance != null) {
            throw new ControllerException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    // Getters/Setters

    @NotNull
    public TableView<ProjetAppliBean> getProjetsApplisTable() {
        return projetsApplisTable;
    }

    @NotNull
    public ObservableList<ProjetAppliBean> getProjetsApplisBeans() {
        return projetsApplisBeans;
    }


    // Méthodes :

    // Module

    @Override
    public String getTitre() {
        //noinspection HardcodedFileSeparator
        return "Projets / Applis";
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws ControllerException {
        try {
            LOGGER.debug("Initialisation...");

            ObservableLists.ensureSameContents(planChargeBean.getProjetsApplisBeans(), projetsApplisBeans);

            // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
            codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
            nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
            trigrammeCPIColumn.setCellValueFactory(cellData -> cellData.getValue().trigrammeCPIProperty());

            // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
            TableViews.decorateMandatoryColumns(codeColumn);
            codeColumn.setCellFactory(param -> {
                TextFieldTableCell<ProjetAppliBean, String> codeCell = new UpperCaseTextFieldTableCell<>();
//            PlanChargeIhm.decorateMandatoryColumns(codeCell);
                ihm.controler(codeCell, "Code incorrect", this::validerCode);
                return codeCell;
            });
//            TableViews.decorateMandatoryColumns(nomColumn);
            nomColumn.setCellFactory(param -> {
                TableCell<ProjetAppliBean, String> cell = EditableAwareTextFieldTableCells.<ProjetAppliBean>forRequiredTableColumn().call(param);
                ihm.controler(cell, "Nom incorrect", this::validerNom);
                return cell;
            });
//            TableViews.decorateMandatoryColumns(trigrammeCPIColumn);
            trigrammeCPIColumn.setCellFactory(param -> {
                TableCell<ProjetAppliBean, String> cell = EditableAwareTextFieldTableCells.<ProjetAppliBean>forRequiredTableColumn().call(param);
                ihm.controler(cell, "Trigramme du CPI incorrect", this::validerTrigrammeCPI);
                return cell;
            });

            SortedList<ProjetAppliBean> sortedBeans = new SortedList<>(projetsApplisBeans);
            sortedBeans.comparatorProperty().bind(projetsApplisTable.comparatorProperty());

            projetsApplisTable.setItems(sortedBeans);

            TableViews.enableFilteringOnColumns(projetsApplisTable, codeColumn, nomColumn, trigrammeCPIColumn);

            definirMenuContextuel();

            definirTouches();

            LOGGER.info("Initialisé.");
        } catch (IhmException e) {
            //noinspection HardcodedFileSeparator
            throw new ControllerException("Impossible d'initialiser le contrôleur du module 'Projets / Applis'.", e);
        }
    }

    private void definirMenuContextuel() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuSupprimer = new MenuItem("Supprimer");
        menuSupprimer.setOnAction(this::supprimerProjetAppli);

        contextMenu.getItems().setAll(menuSupprimer);

        projetsApplisTable.setContextMenu(contextMenu);
    }

    private void definirTouches() {
        // Cf. https://stackoverflow.com/questions/27314495/delete-javafx-table-row-with-delete-key
        projetsApplisTable.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE:
                    supprimerProjetAppli(projetsApplisTable.getSelectionModel().getSelectedItem());
                    break;
                default:
                    LOGGER.debug("Touche ignorée : '{}'.", event.getCode());
            }
        });
    }


    @Null
    private String validerCode(@Null String codeProjetAppli) {
        if ((codeProjetAppli == null) || codeProjetAppli.isEmpty()) {
            //noinspection HardcodedFileSeparator
            return "Un code projet/appli est obligatoire.";
        }
        long nbrOccurrencesCode = projetsApplisBeans.parallelStream()
                .map(ProjetAppliBean::getCode)
                .filter(s -> (s != null) && s.equalsIgnoreCase(codeProjetAppli))
                .count();
        if (nbrOccurrencesCode >= 2L) {
            //noinspection HardcodedFileSeparator
            return "Deux projets/applis ne peuvent avoir le même code.";
        }
        return null;
    }

    @Null
    private String validerNom(@Null String nom) {
/*
        if ((nom == null) || nom.trim().isEmpty()) {
            return "Un nom est obligatoire.";
        }
*/
        return null;
    }

    @Null
    private String validerTrigrammeCPI(@Null String trigrammeCPI) {
/*
        if ((trigrammeCPI == null) || trigrammeCPI.trim().isEmpty()) {
            return "Le trigramme du CPI est obligatoire.";
        }
*/
        return null;
    }


    @FXML
    private void ajouterProjetAppli(@SuppressWarnings("unused") ActionEvent actionEvent) {
        LOGGER.debug("ajouterProjetAppli...");

        ProjetAppliBean nouvBean = new ProjetAppliBean();
        projetsApplisBeans.add(nouvBean);

        // Positionnement sur la ligne qu'on vient d'ajouter :
        TableViews.editCell(projetsApplisTable, nouvBean, codeColumn);
    }


    @FXML
    private void supprimerProjetAppli(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        LOGGER.debug("supprimerProjetAppli...");

        ProjetAppliBean focusedItem = TableViews.selectedItem(projetsApplisTable);
        if (focusedItem == null) {
            LOGGER.debug("Aucun item sélectionné, donc on en sait pas que supprimer, on ne fait rien.");
            return;
        }
        supprimerProjetAppli(focusedItem);
    }

    private void supprimerProjetAppli(@NotNull ProjetAppliBean focusedItem) {
        LOGGER.debug("supprimerProjetAppli...");
        projetsApplisBeans.remove(focusedItem);
    }

}
