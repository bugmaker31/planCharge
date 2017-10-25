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

    private static final int NO_LIGNE_NO_ETAPE = 1;
    private static final int NO_LIGNE_ICONE_ETAPE = 2;


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
        // Récupération (puis suppression) des modèles des View :
        Label noEtapeCouranteLabel = controller.getNoEtapeCouranteLabel();
        FontAwesomeIconView modeleEtapeCourante = controller.getModeleEtapeCouranteView();
        FontAwesomeIconView modeleEtapeNonCourante = controller.getModeleEtapeNonCouranteView();
        FontAwesomeIconView modeleLienEntreEtapes = controller.getModeleLienEntreEtapesView();
        getChildren().removeAll(noEtapeCouranteLabel, modeleEtapeCourante, modeleEtapeNonCourante, modeleLienEntreEtapes);

        // Génération de 1 View par étape, plus 1 View pour chaque espace "inter-étape" :
        int idxColonne = 0;
        for (int cptEtape = 1; cptEtape <= nbrEtapes; cptEtape++) {

            // View "inter-étape" :
            if (cptEtape > 1) {
                FontAwesomeIconView lienEntreEtapesView = cloneIconView(modeleLienEntreEtapes);
                add(lienEntreEtapesView, idxColonne++, NO_LIGNE_ICONE_ETAPE - 1);
            }

            // Pour l'étape : le IconView + le Label) :
            FontAwesomeIconView etape;
            if (cptEtape == noEtape) {
                etape = cloneIconView(modeleEtapeCourante);
            } else {
                etape = cloneIconView(modeleEtapeNonCourante);
            }
            add(etape, idxColonne, NO_LIGNE_ICONE_ETAPE - 1);
            //
            Label noEtapeLabel = cloneLabel(noEtapeCouranteLabel);
            noEtapeLabel.setText(cptEtape + " / " + nbrEtapes);
            add(noEtapeLabel, idxColonne, NO_LIGNE_NO_ETAPE - 1);
            //
            idxColonne++;
        }
    }

    @NotNull
    private Label cloneLabel(@NotNull Label label) {
        Label clone = new Label(label.getText(), label.getGraphic());
        return clone;
    }

    @NotNull
    private FontAwesomeIconView cloneIconView(@NotNull FontAwesomeIconView iconView) {
        FontAwesomeIcon icon = FontAwesomeIcon.valueOf(iconView.getGlyphName());
        FontAwesomeIconView clone = new FontAwesomeIconView(icon, iconView.getSize());
        return clone;
    }
}
