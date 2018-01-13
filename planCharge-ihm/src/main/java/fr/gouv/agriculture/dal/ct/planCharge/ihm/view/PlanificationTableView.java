package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.HeaderHidableTableView;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithoutLogger"})
public class PlanificationTableView<S, T> extends HeaderHidableTableView<S> {

    @NotNull
    private List<TableColumn<S, T>> calendrierColumns;

    private boolean aggregate = false;


    /* Constructeur requis par JavaFX */
    public PlanificationTableView() {
        super();
        calendrierColumns = new ArrayList<>(PlanChargeIhm.NBR_SEMAINES_PLANIFIEES);

        // Une table avec un calendrier ne doit pas voir ses colonnes retaillées.
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        getStyleClass().add("planification-table-view");
    }


    @NotNull
    public List<TableColumn<S, T>> getCalendrierColumns() {
        return calendrierColumns;
    }

    public void setCalendrierColumns(@NotNull List<TableColumn<S, T>> calendrierColumns) {
        this.calendrierColumns = calendrierColumns;
    }


    public boolean isAggregate() {
        return aggregate;
    }

    public void setAggregate(boolean aggregate) {
        this.aggregate = aggregate;
    }

}
