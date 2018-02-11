package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProjetAppliBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.ValidateurRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.value.ChangeListener;
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
public class TracerRevisionController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TracerRevisionController.class);

    private static TracerRevisionController instance;

    public static TracerRevisionController instance() {
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
    private ComboBox<ValidateurRevision> validateurRevisionField;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TextArea commentaireRevisionField;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public TracerRevisionController() throws ControllerException {
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

        validateurRevisionField.getItems().setAll(ValidateurRevision.values());
        validateurRevisionField.setConverter(Converters.VALIDATEUR_REVISION_STRING_CONVERTER);
    }


    void show() throws ControllerException {

        if (stage == null) {
            stage = new Stage();
            stage.setTitle(PlanChargeIhm.APP_NAME + " - Traçage des révisions");
            stage.getIcons().addAll(ihm.getPrimaryStage().getIcons());
            stage.setScene(new Scene(ihm.getTracageRevisionView()));
        }

        if (!stage.isShowing()) {
            stage.show();
        }
        if (!stage.isFocused()) {
            stage.requestFocus();
        }

        LOGGER.debug("Fenêtre de traçage des révisions affichée.");
    }


    @Null
    private ChangeListener<StatutRevision> statutRevisionListener = null;
    @Null
    private ChangeListener<ValidateurRevision> validateurRevisionListener = null;
    @Null
    private ChangeListener<String> commentaireRevisionListener = null;

    public <TB extends TacheBean> void afficher(@NotNull TB tacheBean) {

        if (noTacheField.getText().equals(tacheBean.noTache())) {
            return;
        }

        // ITache :
        noTacheField.setText(tacheBean.noTache());
        noTicketIdalTacheField.setText(tacheBean.getNoTicketIdal());
        codeProjetAppliTacheField.setText(Objects.value(tacheBean.projetAppliProperty().getValue(), ProjetAppliBean::getCode));
        descriptionTacheField.setText(tacheBean.getDescription());

        // Revisable :
        if (statutRevisionListener != null) {
            statutRevisionField.getSelectionModel().selectedItemProperty().removeListener(statutRevisionListener);
        }
        statutRevisionField.getSelectionModel().select(tacheBean.getStatutRevision());
        statutRevisionListener = ((observable, oldValue, newValue) -> tacheBean.setStatutRevision(newValue));
        statutRevisionField.getSelectionModel().selectedItemProperty().addListener(statutRevisionListener);
        //
        if (validateurRevisionListener != null) {
            validateurRevisionField.getSelectionModel().selectedItemProperty().removeListener(validateurRevisionListener);
        }
        validateurRevisionField.getSelectionModel().select(tacheBean.getValidateurRevision());
        validateurRevisionListener = (observable, oldValue, newValue) -> tacheBean.setValidateurRevision(newValue);
        validateurRevisionField.getSelectionModel().selectedItemProperty().addListener(validateurRevisionListener);
        //
        if (commentaireRevisionListener != null) {
            commentaireRevisionField.textProperty().removeListener(commentaireRevisionListener);
        }
        commentaireRevisionField.setText(tacheBean.getCommentaireRevision());
        commentaireRevisionListener = (observable, oldValue, newValue) -> tacheBean.setCommentaireRevision(newValue);
        commentaireRevisionField.textProperty().addListener(commentaireRevisionListener);
    }
}
