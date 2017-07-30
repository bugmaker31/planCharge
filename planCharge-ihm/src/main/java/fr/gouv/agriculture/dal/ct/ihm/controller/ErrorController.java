//Cf. http://stackoverflow.com/questions/26361559/general-exception-handling-in-javafx-8

package fr.gouv.agriculture.dal.ct.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Created by frederic.danna on 17/04/2017.
 */
public class ErrorController extends AbstractController {

    @FXML
    private Label errorMessage;

    public void setErrorText(String text) {
        errorMessage.setText(text);
    }

    @FXML
    private void close() {
        errorMessage.getScene().getWindow().hide();
    }

    @Override
    protected void initialize() throws IhmException {
        // Rien... pour l'instant.
    }
}
