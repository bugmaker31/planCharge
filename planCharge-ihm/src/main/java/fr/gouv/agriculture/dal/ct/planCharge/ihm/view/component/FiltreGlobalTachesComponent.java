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
    private Accordion filtresAccordion;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TitledPane filtresTitledPane;


    public FiltreGlobalTachesComponent() {
        super();

        // Cf. http://docs.oracle.com/javase/8/javafx/fxml-tutorial/custom_control.htm#BABDAAHE
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/component/FiltreGlobalTachesComponent.fxml"));
        fxmlLoader.setRoot(this);
//        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception); // TODO FDA 2017/08 Trouver mieux.
        }
        controller = fxmlLoader.getController();
    }


    @NotNull
    public Accordion getFiltresAccordion() {
        return filtresAccordion;
    }

    public void setFiltresAccordion(@NotNull Accordion filtresAccordion) {
        this.filtresAccordion = filtresAccordion;
    }

    @NotNull
    public TitledPane getFiltresTitledPane() {
        return filtresTitledPane;
    }

    public void setFiltresTitledPane(@NotNull TitledPane filtresTitledPane) {
        this.filtresTitledPane = filtresTitledPane;
    }


    @SuppressWarnings("WeakerAccess")
    @NotNull
    public TextField getFiltreGlobalField() {
        return controller.getFiltreGlobalField();
    }


    public void show() {
        filtresAccordion.setExpandedPane(filtresTitledPane);
    }

    public void requestFocus() {
        getFiltreGlobalField().requestFocus();
    }

}
