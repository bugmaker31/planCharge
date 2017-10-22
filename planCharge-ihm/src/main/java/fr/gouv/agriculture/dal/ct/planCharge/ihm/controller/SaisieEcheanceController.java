package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.controlsfx.dialog.Wizard;
import org.controlsfx.dialog.WizardPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Optional;

// Cf. https://controlsfx.bitbucket.io/org/controlsfx/dialog/Wizard.html
public class SaisieEcheanceController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaisieEcheanceController.class);

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private DatePicker echeanceDatePicker;

    @NotNull
    public DatePicker getEcheanceDatePicker() {
        return echeanceDatePicker;
    }

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private Button annulerButton;

    @NotNull
    public Button getAnnulerButton() {
        return annulerButton;
    }

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private Button validerButton;

    @NotNull
    public Button getValiderButton() {
        return validerButton;
    }


    public SaisieEcheanceController() {
        super();
    }


    @Override
    protected void initialize() throws ControllerException {
        // Rien... pour l'instant.
    }

}
