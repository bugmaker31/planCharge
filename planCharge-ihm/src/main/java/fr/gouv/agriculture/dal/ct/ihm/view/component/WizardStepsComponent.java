package fr.gouv.agriculture.dal.ct.ihm.view.component;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import fr.gouv.agriculture.dal.ct.ihm.controller.WizardStepsController;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author frederic.danna (created on 25/10/17)
 */
public class WizardStepsComponent extends GridPane {

    @FXML
    @NotNull
    private WizardStepsController controller;


    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private Integer noEtape;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private Integer nbrEtapes;


    public WizardStepsComponent(@NamedArg("nbrEtapes") int nbrEtapes, @NamedArg("noEtape") int noEtape) {
        super();

        this.nbrEtapes = nbrEtapes;
        this.noEtape = noEtape;

        // Cf. http://docs.oracle.com/javase/8/javafx/fxml-tutorial/custom_control.htm#BABDAAHE
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/gouv/agriculture/dal/ct/ihm/view/component/WizardSteps.fxml"));
//        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception); // TODO FDA 2017/08 Trouver mieux.
        }
        controller = fxmlLoader.getController();

        Label noEtapeCouranteLabel = controller.getNoEtapeCouranteLabel();
        GridPane.setColumnIndex(noEtapeCouranteLabel, noEtape - 1);

        FontAwesomeIconView modeleEtapeCourante = controller.getModeleEtapeCourante();
        FontAwesomeIconView modeleEtapeNonCourante = controller.getModeleEtapeNonCourante();
        for (int cptEtape = 1; cptEtape <= nbrEtapes; cptEtape++) {
            FontAwesomeIconView etape;
            if (cptEtape == noEtape) {
                etape = cloneEtape(modeleEtapeCourante);
            } else {
                etape = cloneEtape(modeleEtapeNonCourante);
            }
            add(etape, cptEtape - 1, 0);
        }
        modeleEtapeCourante.setVisible(false);
        modeleEtapeNonCourante.setVisible(false);
    }

    @NotNull
    private FontAwesomeIconView cloneEtape(@NotNull FontAwesomeIconView modeleEtape) {
        FontAwesomeIconView etape = new FontAwesomeIconView(modeleEtape.getIcon(), modeleEtape.getSize());
        return etape;
    }
}
