package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.FiltreGlobalTachesController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@SuppressWarnings("ClassHasNoToStringMethod")
public class FiltreGlobalTachesComponent extends HBox {

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private FiltreGlobalTachesController controller;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private Accordion accordion;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TitledPane titledPane;


    public FiltreGlobalTachesComponent() {
        super();

        // Cf. http://docs.oracle.com/javase/8/javafx/fxml-tutorial/custom_control.htm#BABDAAHE
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/component/FiltreGlobalTachesComponent.fxml"));
        fxmlLoader.setRoot(this);
//        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception); // FDA 2017/08 Trouver mieux.
        }
        controller = fxmlLoader.getController();
    }


    @NotNull
    public Accordion getAccordion() {
        return accordion;
    }

    public void setAccordion(@NotNull Accordion accordion) {
        this.accordion = accordion;
    }

    @NotNull
    public TitledPane getTitledPane() {
        return titledPane;
    }

    public void setTitledPane(@NotNull TitledPane titledPane) {
        this.titledPane = titledPane;
    }


    @SuppressWarnings("WeakerAccess")
    @NotNull
    public TextField getFiltreGlobalField() {
        return controller.getFiltreGlobalField();
    }


    public void show() {
        accordion.setExpandedPane(titledPane);
    }

    public void requestFocus() {
        getFiltreGlobalField().requestFocus();
    }

}
