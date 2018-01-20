package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;
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
public class RevisionsController extends AbstractController implements Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionsController.class);

    private static RevisionsController instance;

    public static RevisionsController instance() {
        return instance;
    }


    // Couche "vue" :

    // Les beans :

    //    @Autowired
    @NotNull
    // 'final' pour empêcher de resetter cette variable.
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // TODO FDA 2017/05 Résoudre le warning de compilation (unchecked assignement).
    @SuppressWarnings("unchecked")
    @NotNull
    // 'final' pour empêcher de resetter cette ObsevableList, ce qui enleverait les Listeners.
    private final FilteredList<TacheBean> filteredTachesBeans = new FilteredList<>((ObservableList) planChargeBean.getPlanificationsBeans());

    @Null
    private Stage stage;

    // Les tables :

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableView<TacheBean> tachesTable;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> categorieColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> sousCategorieColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> noTacheColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> noTicketIdalColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> descriptionColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> projetAppliColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> statutColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, LocalDate> debutColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, LocalDate> echeanceColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> importanceColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, Double> chargeColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> ressourceColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TacheBean, String> profilColumn;

    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableFilter<TacheBean> tableFilter;


    // Couche "métier" :

    // TODO FDA 2017/05 Résoudre le warning de compilation (unchecked assignement).
    @SuppressWarnings({"unchecked", "rawtypes"})
    @NotNull
    // 'final' pour empêcher de resetter cette ObsevableList, ce qui enleverait les Listeners.
    private final ObservableList<TacheBean> planificationsBeans = (ObservableList) planChargeBean.getPlanificationsBeans();


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RevisionsController() throws ControllerException {
        super();
        if (instance != null) {
            throw new ControllerException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws ControllerException {

        categorieColumn.setCellValueFactory(cellData -> cellData.getValue().codeCategorieProperty());
        sousCategorieColumn.setCellValueFactory(cellData -> cellData.getValue().codeSousCategorieProperty());
        noTacheColumn.setCellValueFactory(cellData -> cellData.getValue().noTacheProperty());
        noTicketIdalColumn.setCellValueFactory(cellData -> cellData.getValue().noTicketIdalProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        projetAppliColumn.setCellValueFactory(cellData -> cellData.getValue().projetAppliProperty().getValue().codeProperty());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty().getValue().codeProperty());
        debutColumn.setCellValueFactory(cellData -> cellData.getValue().debutProperty());
        echeanceColumn.setCellValueFactory(cellData -> cellData.getValue().echeanceProperty());
        importanceColumn.setCellValueFactory(cellData -> cellData.getValue().importanceProperty().getValue().codeProperty());
        chargeColumn.setCellValueFactory(cellData -> cellData.getValue().chargeProperty().asObject());
        ressourceColumn.setCellValueFactory(cellData -> cellData.getValue().ressourceProperty().getValue().codeProperty());
        profilColumn.setCellValueFactory(cellData -> cellData.getValue().profilProperty().getValue().codeProperty());

        TableViews.ensureSorting(tachesTable, filteredTachesBeans);

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //
        tableFilter = TableViews.enableFilteringOnColumns(tachesTable, Arrays.asList(categorieColumn, sousCategorieColumn, noTacheColumn, noTicketIdalColumn, descriptionColumn, projetAppliColumn, statutColumn, debutColumn, echeanceColumn, importanceColumn, ressourceColumn, chargeColumn, profilColumn));

//        TODO FDA 2018/01 Coder.
    }


    void afficherProjetAppli() {
        TacheBean tacheBean = TableViews.selectedItem(tachesTable);
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le projet/appli",
                    "Aucune tâche n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        ProjetAppliBean projetAppliBean = tacheBean.getProjetAppli();
        if (projetAppliBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le projet/appli",
                    "Aucun projet/appli n'est (encore) indiqué pour la tâche sélectionnée (" + tacheBean.noTache() + ")."
                            + "\nIndiquez d'abord un projet/appli, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        try {
            ihm.getApplicationController().afficherModuleProjetsApplis();
            TableViews.focusOnItem(ihm.getProjetsApplisController().getProjetsApplisTable(), projetAppliBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le projet/appli " + projetAppliBean.getCode() + ".", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le projet/appli '" + projetAppliBean.getCode() + "'",
                    Exceptions.causes(e),
                    400, 200
            );
        }
    }

    // Module

    @Override
    public String getTitre() {
        return "Révisions";
    }



    void show() throws ControllerException {

        if (stage == null) {
            stage = new Stage();
            stage.setTitle(PlanChargeIhm.APP_NAME + " - Liste des révisions");
            stage.getIcons().addAll(ihm.getPrimaryStage().getIcons());
            stage.setScene(new Scene(ihm.getRevisionsView()));
        }

        if (!stage.isShowing()) {
            stage.show();
        }
        if (!stage.isFocused()) {
            stage.requestFocus();
        }

        LOGGER.debug("Fenêtre de listage des révisions affichée.");
    }
}
