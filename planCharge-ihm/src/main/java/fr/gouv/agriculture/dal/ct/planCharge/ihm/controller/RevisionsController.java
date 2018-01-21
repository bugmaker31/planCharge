package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
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
public class RevisionsController extends AbstractTachesController<TacheBean> implements Module {

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

    // Les tables :

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableView<TacheBean> tachesTable;

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
            Calculateur.executerSansCalculer(super::initialize);
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur.", e);
        }
    }

    @NotNull
    @Override
    TableView<TacheBean> getTachesTable() {
        return tachesTable;
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
        {
            MenuItem menuItemSupprimer = new MenuItem("Voir le détail de la tâche " + tacheBean.noTache());
            menuItemSupprimer.setOnAction(event -> {
                afficherTache(tacheBean);
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

/*
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
*/
}
