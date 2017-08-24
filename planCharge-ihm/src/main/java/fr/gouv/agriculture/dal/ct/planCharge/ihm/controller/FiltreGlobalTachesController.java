package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ClassHasNoToStringMethod")
public class FiltreGlobalTachesController extends AbstractController {

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TextField filtreGlobalField;

    @SuppressWarnings("WeakerAccess")
    @NotNull
    public TextField getFiltreGlobalField() {
        return filtreGlobalField;
    }

    @Override
    protected void initialize() throws IhmException {
        // Rien... pour l'instant.
    }
}
