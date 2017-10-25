package fr.gouv.agriculture.dal.ct.ihm.view.component;

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

        init();
    }

    private void init() {
        {
            Label noEtapeCouranteLabel = controller.getNoEtapeCouranteLabel();
            noEtapeCouranteLabel.setText(noEtape + " / " + nbrEtapes);
            GridPane.setColumnIndex(noEtapeCouranteLabel, noEtape - 1);
            GridPane.setRowIndex(noEtapeCouranteLabel, 0);
        }
        {
            FontAwesomeIconView modeleEtapeCourante = controller.getModeleEtapeCouranteView();
            FontAwesomeIconView modeleEtapeNonCourante = controller.getModeleEtapeNonCouranteView();
            FontAwesomeIconView modeleLienEntreEtapes = controller.getModeleLienEntreEtapesView();
            int idxColonne = 0;
            for (int cptEtape = 1; cptEtape <= nbrEtapes; cptEtape++) {
                if (cptEtape > 1) {
                    FontAwesomeIconView lienEntreEtapesView = cloneIconView(modeleLienEntreEtapes);
                    add(lienEntreEtapesView, idxColonne++, 0);
                }
                FontAwesomeIconView etape;
                if (cptEtape == noEtape) {
                    etape = cloneIconView(modeleEtapeCourante);
                } else {
                    etape = cloneIconView(modeleEtapeNonCourante);
                }
                add(etape, idxColonne++, 0);
            }
            modeleEtapeCourante.setVisible(false);
            modeleEtapeNonCourante.setVisible(false);
            modeleLienEntreEtapes.setVisible(false);
        }
    }

    @NotNull
    private FontAwesomeIconView cloneIconView(@NotNull FontAwesomeIconView modeleEtape) {
        FontAwesomeIcon icon = FontAwesomeIcon.valueOf(modeleEtape.getGlyphName());
        FontAwesomeIconView etape = new FontAwesomeIconView(icon, modeleEtape.getSize());
        return etape;
    }
}
