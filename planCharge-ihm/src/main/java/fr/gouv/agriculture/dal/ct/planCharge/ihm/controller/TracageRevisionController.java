package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProjetAppliBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.ResponsablePriorisation;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
public class TracageRevisionController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TracageRevisionController.class);

    private static TracageRevisionController instance;

    public static TracageRevisionController instance() {
        return instance;
    }


    @Null
    private Stage stage;


    // Couche "métier" :

    @Null
    private PlanificationTacheBean planificationTacheAfficheeBean;

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();


    // Couche "vue" :

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TextField noTacheField;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TextField noTicketIdalTacheField;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TextField codeProjetAppliTacheField;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TextField descriptionTacheField;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private ComboBox<StatutRevision> statutRevisionField;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private ComboBox<ResponsablePriorisation> validateurField;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TextArea commentaireField;


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

        statutRevisionField.getItems().setAll(StatutRevision.values());
        statutRevisionField.setConverter(Converters.STATUT_REVISION_STRING_CONVERTER);

        validateurField.getItems().setAll(ResponsablePriorisation.values());
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


    public void afficher(@NotNull PlanificationTacheBean planifBean) {
        planificationTacheAfficheeBean = planifBean;

        noTacheField.textProperty().bind(planifBean.noTacheProperty());
        noTicketIdalTacheField.textProperty().bind(planifBean.noTicketIdalProperty());
        codeProjetAppliTacheField.textProperty().bind(planifBean.projetAppliProperty().getValue().codeProperty());
        descriptionTacheField.textProperty().bind(planifBean.descriptionProperty());
    }
}
