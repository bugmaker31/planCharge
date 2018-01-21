package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.FiltreGlobalTachesController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@SuppressWarnings("ClassHasNoToStringMethod")
public class FiltreGlobalTachesComponent extends HBox {

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private FiltreGlobalTachesController controller;


    public FiltreGlobalTachesComponent() {
        super();

        // Cf. http://docs.oracle.com/javase/8/javafx/fxml-tutorial/custom_control.htm#BABDAAHE
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/component/FiltreGlobalTachesView.fxml"));
        fxmlLoader.setRoot(this);
//        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception); // TODO FDA 2017/08 Trouver mieux.
        }
        controller = fxmlLoader.getController();
    }


    @SuppressWarnings("WeakerAccess")
    @NotNull
    public TextField getFiltreGlobalField() {
        return controller.getFiltreGlobalField();
    }
}
