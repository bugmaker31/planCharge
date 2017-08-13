package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class TableViewAvecCalendrier<S, T> extends TableView<S> {

    @NotNull
    private List<TableColumn<S, T>> calendrierColumns;

    @NotNull
    public List<TableColumn<S, T>> getCalendrierColumns() {
        return calendrierColumns;
    }

    public void setCalendrierColumns(@NotNull List<TableColumn<S, T>> calendrierColumns) {
        this.calendrierColumns = calendrierColumns;
    }

    @SafeVarargs
    public final void setCalendrierColumns(@NotNull TableColumn<S, T>... calendrierColumns) {
        this.calendrierColumns = Arrays.asList(calendrierColumns);
    }
}
