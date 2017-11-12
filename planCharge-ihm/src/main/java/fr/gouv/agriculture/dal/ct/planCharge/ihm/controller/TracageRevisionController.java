package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCell;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.controlsfx.dialog.Wizard;
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
public class TracageRevisionController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TracageRevisionController.class);

    private static TracageRevisionController instance;

    public static TracageRevisionController instance() {
        return instance;
    }


    @Null
    private Stage stage;


    // Couche "métier" :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();


    // Couche "vue" :


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TracageRevisionController() throws ControllerException {
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
        // Rien, pour l'instant.
    }


    void show() {

        if ((stage != null) && stage.isShowing()) {
            LOGGER.debug("Fenêtre déjà affichée, rien à faire.");
            return;
        }

        if (stage == null) {
            stage = new Stage();
            stage.setTitle(PlanChargeIhm.APP_NAME + " - Traçage des révisions");
            stage.getIcons().addAll(ihm.getPrimaryStage().getIcons());
            stage.setScene(new Scene(ihm.getTracageRevisionView()));
        }

        stage.show();

        LOGGER.debug("Fenêtre de traçage des révisions affichée.");
    }
}
