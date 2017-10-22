package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;

@SuppressWarnings("ClassHasNoToStringMethod")
public class FiltreGlobalTachesController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FiltreGlobalTachesController.class);
    
    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private CustomTextField filtreGlobalField;

    @SuppressWarnings("WeakerAccess")
    @NotNull
    public TextField getFiltreGlobalField() {
        return filtreGlobalField;
    }

    @Override
    protected void initialize() throws ControllerException {
        try {
            // Cf. https://bitbucket.org/controlsfx/controlsfx/issues/330/making-textfieldssetupclearbuttonfield
            setupClearableButtonField(filtreGlobalField);
        } catch (Exception e) {
            LOGGER.error("Impossible d'activer l'option 'clearable' du CustomTextField (de ControlsFx", e);
        }
    }

    private void setupClearableButtonField(CustomTextField customTextField) throws Exception {
        Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
        m.setAccessible(true);
        m.invoke(null, customTextField, customTextField.rightProperty());
    }
}
