package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

// Cf. https://controlsfx.bitbucket.io/org/controlsfx/dialog/Wizard.html
public class RevueWizardController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevueWizardController.class);

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
    private WizardPane etapeResoudreSurchargesPane;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private WizardPane etapeDiffuserPane;



    @Override
    protected void initialize() throws ControllerException {
        // Rien, pour l'instant.
    }

    void showAndWait() {

        Stage wizardStage = new Stage();
        wizardStage.setTitle(PlanChargeIhm.APP_NAME + " - Assistant de revue");
        wizardStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/planCharge-logo.png")));

        // create and assign the flow
        Wizard wizard = new Wizard(wizardStage.getOwner());
        wizard.setFlow(new Wizard.LinearFlow(
                etapeDefinirDateEtatPane,
                etapeMajDisponibilitesPane,
                etapeMajTachesPane,
                etapeMajPlanChargePane,
                etapeResoudreSurchargesPane,
                etapeDiffuserPane
        ));
        wizard.resultProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Assistant de revue terminé (résultat = {}).", wizard.getResult().getButtonData());
            prendreEnCompte();
            wizardStage.close();
            LOGGER.debug("Assistant de revue masqué.");
        });
        // show wizard and wait for response
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

    private void prendreEnCompte() {
        // TODO FDA 2017/10 Coder.
    }
}
