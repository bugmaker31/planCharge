package fr.gouv.agriculture.dal.ct.ihm.view;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDAbsenceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursOuvresBean;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class TableViews {

    @Null
    public static <S> S selectedTableRow(@NotNull TableView<S> table) {
        return table.getSelectionModel().getSelectedItem();
    }

    public static <S> void focusOnTableRow(@NotNull TableView<S> table, @NotNull S row) {
        int idxTacheBean = table.getItems().indexOf(row);
        assert idxTacheBean != -1;
        // Cf. https://examples.javacodegeeks.com/desktop-java/javafx/tableview/javafx-tableview-example/
        table.requestFocus();
        table.getSelectionModel().select(idxTacheBean);
        table.getFocusModel().focus(idxTacheBean);
        // Il faut aussi scroller jusqu'à la ligne sélectionnée,
        // dans le cas où la table contient plus d'item qu'elle ne peut afficher de ligne
        // et que la ligne sélectionnée ne fasse pas partie des lignes visibles :
        table.scrollTo(idxTacheBean);
    }

    public static <S, T> void editTableCell(@NotNull TableView<S> table, @NotNull S row, @NotNull TableColumn<S, T> column) {

        focusOnTableRow(table, row);

        // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en mode édition de la (bonne) cellule.
        int idxLigNouvBean = table.getItems().indexOf(row);
        assert idxLigNouvBean != -1;
        table.edit(idxLigNouvBean, column);
//        ressourcesHumainesTable.refresh();
    }


    public static void synchronizeColumnsWidth(@NotNull TableView<?> masterTable, @NotNull TableView<?>... tables) {
        ObservableList<? extends TableColumn<?, ?>> masterColumns = masterTable.getColumns();
        for (TableView<?> table : tables) {
            ObservableList<? extends TableColumn<?, ?>> columns = table.getColumns();
            for (int columnIdx = 0; columnIdx < columns.size(); columnIdx++) {
                TableColumn<?, ?> column = columns.get(columnIdx);

                TableColumn masterColumn = masterColumns.get(columnIdx);

                column.prefWidthProperty().bind(masterColumn.widthProperty());
                column.minWidthProperty().bind(masterColumn.widthProperty());
                column.maxWidthProperty().bind(masterColumn.widthProperty());
            }
        }
    }

    public static <S> void disableColumnReorderable(@NotNull TableView<S> table) {
/*
        // table. https://stackoverflow.com/questions/10598639/how-to-disable-column-reordering-in-a-javafx2-tableview
        table.getColumns().addListener(new ListChangeListener<TableColumn<S, ?>>() {

            private boolean suspended = false;

            @Override
            public void onChanged(Change<? extends TableColumn<S, ?>> change) {
                while (change.next()) {
                    if (change.wasReplaced() && !suspended) {
                        suspended = true;
                        table.getColumns().setAll(columns);
                        suspended = false;
                    }
                }
            }
        });
*/
        // Cf. https://stackoverflow.com/questions/22202782/how-to-prevent-tableview-from-doing-tablecolumn-re-order-in-javafx-8
        TableHeaderRow header = headerRow(table);
        // TODO FDA 2017/08 Debugger (voir F I X M E dans méthode "headerRow"), puis réactiver.
//        header.setReordering(false);
    }

    public static <S> TableHeaderRow headerRow(@NotNull TableView<S> table) {
        // Cf. https://stackoverflow.com/questions/22202782/how-to-prevent-tableview-from-doing-tablecolumn-re-order-in-javafx-8
        // FIXME FDA 2017/08 Retourne null.
        return (TableHeaderRow) table.lookup("TableHeaderRow");
    }

    public static <S> void adjustHeightToRowCount(@NotNull TableView<S> table) {
/*
        table.itemsProperty().addListener(change -> {
            int rowsCount = items.size();
            double columnHeight = table.get;
            double headerRowHeight = 100; // headerRow(table).getHeight(); // TODO FDA 2017/08 Debugger (voir F I X M E dans méthode "headerRow"), puis réactiver.

            double tableHeight = headerRowHeight + (columnHeight * rowsCount);

            table.setMinHeight(tableHeight);
            table.setPrefHeight(tableHeight);
            table.setMaxHeight(tableHeight);
        });
*/
        // Cf. https://stackoverflow.com/questions/27945817/javafx-adapt-tableview-height-to-number-of-rows
/*
        assert table.getFixedCellSize() > 0; // TODO FDA 2017/08 Trouver un meilleur code pour ce contrôle.
        DoubleBinding height = new SimpleDoubleProperty(10.0); // headerRow(table).heightProperty(); // TODO FDA 2017/08 Debugger (voir F I X M E dans méthode "headerRow"), puis réactiver.
        DoubleBinding tableHeight = table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(height));
        table.minHeightProperty().bind(tableHeight);
        table.prefHeightProperty().bind(tableHeight);
        table.maxHeightProperty().bind(tableHeight);
*/
        assert table.getFixedCellSize() > 0 : "La tableView doit avoir la propriété 'fixedCellSize' définie."; // TODO FDA 2017/08 Trouver un meilleur code pour ce contrôle.
        adjustHeigth(table);
        table.getItems().addListener((Change<? extends S> change) -> {
            adjustHeigth(table);
        });
    }

    private static <S> void adjustHeigth(@NotNull TableView<S> table) {
        double headerRowHeight = table.getFixedCellSize(); // headerRow(table).getHeight(); // TODO FDA 2017/08 Debugger (voir F I X M E dans méthode "headerRow"), puis réactiver.

        double rowHeight = table.getFixedCellSize();
        int rowsCount = table.getItems().size();

        double tableHeight = headerRowHeight + (rowHeight * rowsCount);

        table.setMinHeight(tableHeight);
        table.setPrefHeight(tableHeight);
        table.setMaxHeight(tableHeight);
    }
}
