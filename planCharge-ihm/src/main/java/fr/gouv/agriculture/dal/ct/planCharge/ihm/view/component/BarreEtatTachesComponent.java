package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component;

import fr.gouv.agriculture.dal.ct.ihm.controller.Executeur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.BarreEtatTachesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import javax.validation.constraints.NotNull;
import java.io.IOException;

@SuppressWarnings("ClassHasNoToStringMethod")
public class BarreEtatTachesComponent<TB extends TacheBean> extends HBox {


    @NotNull
    private final BarreEtatTachesController<TB> controller;

    public BarreEtatTachesComponent() {
        super();

        // Cf. http://docs.oracle.com/javase/8/javafx/fxml-tutorial/custom_control.htm#BABDAAHE
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/component/BarreEtatTaches.fxml"));
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

    public void initialize(@NotNull ObservableList<TB> tachesBeans, @NotNull TableView<TB> tachesTable, @NotNull Executeur fonctionAjouteTache) {
        controller.setTachesBeans(tachesBeans);
        controller.setTachesTable(tachesTable);
        controller.setFonctionAjouterTache(fonctionAjouteTache);
        controller.prepare();
    }
}
