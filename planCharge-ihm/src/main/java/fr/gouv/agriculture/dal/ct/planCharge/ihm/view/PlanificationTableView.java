package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.HeaderHidableTableView;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithoutLogger"})
public class PlanificationTableView<S, T> extends HeaderHidableTableView<S> {

    @NotNull
    private List<TableColumn<S, T>> calendrierColumns;


    /* Constructeur requis par JavaFX */
    public PlanificationTableView() {
        super();
        calendrierColumns = new ArrayList<>(PlanChargeIhm.NBR_SEMAINES_PLANIFIEES);

        // Une table avec un calendrier ne doit pas voir ses colonnes retaillées.
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }


    @NotNull
    public List<TableColumn<S, T>> getCalendrierColumns() {
        return calendrierColumns;
    }

    public void setCalendrierColumns(@NotNull List<TableColumn<S, T>> calendrierColumns) {
        this.calendrierColumns = calendrierColumns;
    }
}
