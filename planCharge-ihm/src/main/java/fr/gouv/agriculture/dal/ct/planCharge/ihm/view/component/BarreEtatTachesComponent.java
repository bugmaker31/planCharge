package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.Executeur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.BarreEtatTachesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("ClassHasNoToStringMethod")
public class BarreEtatTachesComponent<TB extends TacheBean> extends HBox {


    @NotNull
    private final BarreEtatTachesController<TB> controller;


    @FXML
    private final ObservableList<Button> buttons = FXCollections.observableArrayList();

    public final ObservableList<Button> getButtons() {
        return buttons;
    }


    public BarreEtatTachesComponent() {
        super();

        // Cf. http://docs.oracle.com/javase/8/javafx/fxml-tutorial/custom_control.htm#BABDAAHE
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/component/BarreEtatTachesView.fxml"));
        //noinspection ThisEscapedInObjectConstruction
        fxmlLoader.setRoot(this);
//        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception); // TODO FDA 2017/08 Trouver mieux.
        }
        controller = fxmlLoader.getController();
    }

    public void initialize(@NotNull ObservableList<TB> tachesBeans, @NotNull TableView<TB> tachesTable) {
        controller.setTachesBeans(tachesBeans);
        controller.setTachesTable(tachesTable);
        controller.setActions(buttons);
        controller.prepare();
    }
}
