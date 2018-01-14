package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProjetAppliBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.ValidateurRevision;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class ListerRevisionsController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListerRevisionsController.class);

    private static ListerRevisionsController instance;

    public static ListerRevisionsController instance() {
        return instance;
    }


    @Null
    private Stage stage;


    // Couche "métier" :

/*
    @Null
    private PlanificationTacheBean planificationTacheAfficheeBean;
*/

/*
    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();
*/


    // Couche "vue" :



    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ListerRevisionsController() throws ControllerException {
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
//        TODO FDA 2018/01 Coder.
    }


    void show() throws ControllerException {

        if (stage == null) {
            stage = new Stage();
            stage.setTitle(PlanChargeIhm.APP_NAME + " - Liste des révisions");
            stage.getIcons().addAll(ihm.getPrimaryStage().getIcons());
            stage.setScene(new Scene(ihm.getListerRevisionsView()));
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
