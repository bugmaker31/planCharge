package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

// Cf. https://controlsfx.bitbucket.io/org/controlsfx/dialog/Wizard.html
public class RevueWizardController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevueWizardController.class);

    @Null
    private Stage wizardStage;

    @Null
    private Wizard wizard;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeDefinirDateEtatPane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeMajDisponibilitesPane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeMajTachesPane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeMajPlanChargePane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeDiffuserPane;


    public RevueWizardController() {
        super();
    }


    @Override
    protected void initialize() throws ControllerException {
        // Rien, pour l'instant.
    }

    void show() {

//        if (wizardStage == null) {
        wizardStage = new Stage();
        wizardStage.setTitle(PlanChargeIhm.APP_NAME + " - Assistant de revue");
        wizardStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/planCharge-logo.png")));
//        }

//        if (wizard == null) {
        wizard = new Wizard(wizardStage.getOwner());
        wizard.setFlow(new Wizard.LinearFlow(
                etapeDefinirDateEtatPane,
                etapeMajDisponibilitesPane,
                etapeMajTachesPane,
                etapeMajPlanChargePane,
                etapeDiffuserPane
        ));
        wizard.resultProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Assistant de revue terminé (résultat = {}).", wizard.getResult().getButtonData());
            wizardStage.hide();
            LOGGER.debug("Assistant de revue masqué.");
        });
//        }

/*
        wizard.showAndWait().ifPresent(result -> {
            if (Objects.equals(result, ButtonType.FINISH)) {
                prendreEnCompte();
            }
        });
*/
//        wizard.show();
        wizardStage.setScene(wizard.getScene());
        wizardStage.show();

        LOGGER.debug("Assistant de revue affiché.");
    }

    @FXML
    private void afficherModuleRessourcesHumaines(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleRessourcesHumaines();
    }

    @FXML
    private void afficherModuleDisponibilites(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleDisponibilites();
    }

    @FXML
    private void afficherModuleTaches(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleTaches();
    }

    @FXML
    private void afficherModuleCharges(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().afficherModuleCharges();
    }

    @FXML
    private void deplierAccordeonParametres(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ihm.getApplicationController().deplierAccordeonParametres();
        ihm.getPrimaryStage().requestFocus();
        ihm.getApplicationController().getDateEtatPicker().show();
    }
}
