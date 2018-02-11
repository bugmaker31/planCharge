package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ActionUtilisateur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AffichageModuleTaches;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.revision.ExportRevisions;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.ValidateurRevision;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class RevisionsController extends AbstractTachesController<TacheBean> implements Module {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevisionsController.class);

    private static RevisionsController instance;

    public static RevisionsController instance() {
        return instance;
    }


    // Couche "vue" :

    @Override
    public Node getView() {
        return ihm.getRevisionsView();
    }

    @NotNull
    @Override
    public ActionUtilisateur actionUtilisateurAffichageModule(@Null Module modulePrecedent) {
        return new AffichageModuleTaches(modulePrecedent);
    }

    // Les beans :

    //    @Autowired
    @NotNull
    // 'final' pour empêcher de resetter cette variable.
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // Les tables :

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableView<TacheBean> revisionsTachesTable;

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableColumn<TacheBean, StatutRevision> statutRevisionColumn;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableColumn<TacheBean, ValidateurRevision> validateurRevisionColumn;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableColumn<TacheBean, String> commentaireRevisionColumn;


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


    // AbstractTachesController

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws ControllerException {
        try {
            Calculateur.executerSansCalculer(() -> {

                super.initialize();

                initBeans();
                initTables();

                // Ajouter ici du code spécifique à l'initialisation de ce Controller. Cf. TachesController et ChargesController.
            });
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur.", e);
        }
    }

    private void initBeans() {
        // Rien... pour l'instant.
    }

    private void initTables() {

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        //
        statutRevisionColumn.setCellValueFactory(param -> param.getValue().statutRevisionProperty());
        validateurRevisionColumn.setCellValueFactory(param -> param.getValue().validateurRevisionProperty());
        commentaireRevisionColumn.setCellValueFactory(param -> param.getValue().commentaireRevisionProperty());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") et
        // du formatage qui symbolise les incohérences/surcharges/etc. (Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/) :
        //
        statutRevisionColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Converters.STATUT_REVISION_STRING_CONVERTER, StatutRevision.values()));
        validateurRevisionColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Converters.VALIDATEUR_REVISION_STRING_CONVERTER, ValidateurRevision.values()));
        commentaireRevisionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    @NotNull
    @Override
    TableView<TacheBean> getTachesTable() {
        return revisionsTachesTable;
    }

    @NotNull
    @Override
    ObservableList<TacheBean> getTachesBeans() {
        return planificationsBeans;
    }


    @NotNull
    @Override
    TacheBean nouveauBean() throws ControllerException {
        return ihm.getChargesController().nouveauBean();
    }

    @Override
    protected boolean estTacheAvecAutreFiltreAVoir(@NotNull TacheBean tache) throws ControllerException {
        return false;
    }

    @Override
    MenuButton menuActions(@NotNull TableColumn.CellDataFeatures<TacheBean, MenuButton> cellData) {
        MenuButton menuActions = super.menuActions(cellData);
        TacheBean tacheBean = cellData.getValue();
        assert tacheBean != null;
        {
            MenuItem menuItemSupprimer = new MenuItem("Voir le détail de la tâche " + tacheBean.noTache());
            menuItemSupprimer.setOnAction(event -> {
                ihm.getTachesController().afficherTache(tacheBean);
            });
            menuActions.getItems().add(menuItemSupprimer);
        }
        return menuActions;
    }


    // Module

    @Override
    public String getTitre() {
        return "Révisions";
    }


    @Null
    private Stage stage;

    void show() throws ControllerException {

        if (stage == null) {
            stage = new Stage();
            stage.setTitle(PlanChargeIhm.APP_NAME + " - Liste des révisions");
            stage.getIcons().addAll(ihm.getPrimaryStage().getIcons());
            stage.setScene(new Scene(ihm.getRevisionsView()));

            stage.setMaximized(true);

            int noEcran = Screen.getScreens().size() - ihm.noEcranParDefaut();
            ihm.positionnerSurEcran(stage, noEcran);
        }

        if (!stage.isShowing()) {
            stage.show();
        }
        if (!stage.isFocused()) {
            stage.requestFocus();
        }

        LOGGER.debug("Fenêtre de listage des révisions affichée.");
    }

    @FXML
    private void copierRevisions(@SuppressWarnings("unused") ActionEvent actionEvent) throws ControllerException {
        LOGGER.debug("Copie des révisions...");

        ExportRevisions exportRevision = new ExportRevisions(planChargeBean.getDateEtat(), revisionsTachesTable.getItems());

        String revisionsEnHtml = exporterRevisionsEnHtml(exportRevision);

        Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.clear();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putHtml(revisionsEnHtml);
        clipboard.setContent(clipboardContent);
    }

    @SuppressWarnings("MethodMayBeStatic")
    private String exporterRevisionsEnHtml(@NotNull ExportRevisions tacheTable) throws ControllerException {
        try {

            Template template = PlanChargeIhm.getFreeMarkerConfig().getTemplate("export_revisions.ftlh");
            Writer htmlWriter = new StringWriter();
            template.process(tacheTable, htmlWriter);
            return htmlWriter.toString();

        } catch (IOException | TemplateException e) {
            throw new ControllerException("Impossible de générer le HTML des révisions.", e);
        }
    }

}
