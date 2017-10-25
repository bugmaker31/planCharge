package fr.gouv.agriculture.dal.ct.ihm.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.AbstractController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javax.validation.constraints.NotNull;

/**
 * @author frederic.danna (created on 25/10/17)
 */
public class WizardStepsController extends AbstractController {

    @SuppressWarnings("NullableProblems")
    @NotNull
    private Integer nbrEtapes;

    public void setNbrEtapes(@NotNull Integer nbrEtapes) {
        this.nbrEtapes = nbrEtapes;
    }

    @SuppressWarnings("NullableProblems")
    @NotNull
    private Integer noEtape;

    public void setNoEtape(@NotNull Integer noEtape) {
        this.noEtape = noEtape;
    }

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private FontAwesomeIconView modeleEtapeCouranteView;

    @NotNull
    public FontAwesomeIconView getModeleEtapeCouranteView() {
        return modeleEtapeCouranteView;
    }

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private FontAwesomeIconView modeleEtapeNonCouranteView;

    @NotNull
    public FontAwesomeIconView getModeleEtapeNonCouranteView() {
        return modeleEtapeNonCouranteView;
    }

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Label noEtapeCouranteLabel;

    @NotNull
    public Label getNoEtapeCouranteLabel() {
        return noEtapeCouranteLabel;
    }

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private FontAwesomeIconView modeleLienEntreEtapesView;

    @NotNull
    public FontAwesomeIconView getModeleLienEntreEtapesView() {
        return modeleLienEntreEtapesView;
    }

    public void setModeleLienEntreEtapesView(@NotNull FontAwesomeIconView modeleLienEntreEtapesView) {
        this.modeleLienEntreEtapesView = modeleLienEntreEtapesView;
    }


    @Override
    protected void initialize() throws ControllerException {
        // Rien, pour l'instant.
    }
}
