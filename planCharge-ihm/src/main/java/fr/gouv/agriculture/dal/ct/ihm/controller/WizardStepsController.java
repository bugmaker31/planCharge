package fr.gouv.agriculture.dal.ct.ihm.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.gouv.agriculture.dal.ct.ihm.view.component.WizardStepsComponent;
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
    private FontAwesomeIconView modeleEtapeCourante;

    @NotNull
    public FontAwesomeIconView getModeleEtapeCourante() {
        return modeleEtapeCourante;
    }

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private FontAwesomeIconView modeleEtapeNonCourante;

    @NotNull
    public FontAwesomeIconView getModeleEtapeNonCourante() {
        return modeleEtapeNonCourante;
    }

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Label noEtapeCouranteLabel;

    @NotNull
    public Label getNoEtapeCouranteLabel() {
        return noEtapeCouranteLabel;
    }

    @Override
    protected void initialize() throws ControllerException {
        // Rien, pour l'instant.
    }


}
