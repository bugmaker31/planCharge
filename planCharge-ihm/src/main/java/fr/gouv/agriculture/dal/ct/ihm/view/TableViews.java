package fr.gouv.agriculture.dal.ct.ihm.view;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.table.TableFilter.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.*;

public final class TableViews {

    private static final Logger LOGGER = LoggerFactory.getLogger(TableViews.class);

    private TableViews() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public static <S> int itemIndex(@NotNull TableView<? extends S> table, @NotNull S item) {
        return table.getItems().indexOf(item);
    }

    @Null
    public static <S> S selectedItem(@NotNull TableView<S> table) {
        return table.getSelectionModel().getSelectedItem();
    }

    public static <S> void clearSelection(TableView<S> table) {
        table.getSelectionModel().clearSelection();
    }

    public static <S> void focusOnItem(@NotNull TableView<? extends S> table, @NotNull S item) {

        int itemIdx = itemIndex(table, item);
        assert itemIdx != -1;

        // Cf. https://examples.javacodegeeks.com/desktop-java/javafx/tableview/javafx-tableview-example/
        table.requestFocus();
        table.getSelectionModel().clearAndSelect(itemIdx);
        table.getFocusModel().focus(itemIdx);

        // Il faut aussi scroller jusqu'à la ligne sélectionnée,
        // dans le cas où la table contient plus d'item qu'elle ne peut afficher de ligne
        // et que la ligne sélectionnée ne fasse pas partie des lignes visibles :
        table.scrollTo(itemIdx);
    }


    public static <S> void disableReagencingColumns(@NotNull TableView<S> table) {
        // Cf. https://stackoverflow.com/questions/10598639/how-to-disable-column-reordering-in-a-javafx2-tableview
        //noinspection OverlyComplexAnonymousInnerClass,AnonymousInnerClass
        table.getColumns().addListener(new ListChangeListener<TableColumn<S, ?>>() {

            @SuppressWarnings("BooleanVariableAlwaysNegated")
            private boolean suspended = false;

            @NotNull
            private final TableColumn[] orginalColumnsOrder = table.getColumns().toArray(new TableColumn[table.getColumns().size()]);

            @Override
            public void onChanged(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") Change<? extends TableColumn<S, ?>> change) {
                while (change.next()) {
                    if (change.wasReplaced() && !suspended) {
                        suspended = true;
                        //noinspection unchecked
                        table.getColumns().setAll(orginalColumnsOrder);
                        suspended = false;
                    }
                }
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    @Null
    public static TableHeaderRow headerRow(@NotNull TableView<?> table) {
        // Cf. https://stackoverflow.com/questions/22202782/how-to-prevent-tableview-from-doing-tablecolumn-re-order-in-javafx-8
        TableHeaderRow tableHeaderRow = (TableHeaderRow) table.lookup("TableHeaderRow");
//        assert tableHeaderRow != null : "Impossible de retrouver le 'selector' \"TableHeaderRow\". CSS pas encore appliquée ?";
        return tableHeaderRow;
    }


    // Displaying :

    public static void synchronizeColumnsWidth(@NotNull TableView<?> masterTable, @NotNull List<? extends TableView<?>> tables) {
        ObservableList<? extends TableColumn<?, ?>> masterColumns = masterTable.getColumns();
        for (TableView<?> table : tables) {
            ObservableList<? extends TableColumn<?, ?>> columns = table.getColumns();
            for (int columnIdx = 0; columnIdx < columns.size(); columnIdx++) {
                TableColumn<?, ?> column = columns.get(columnIdx);

                TableColumn<?, ?> masterColumn = masterColumns.get(columnIdx);
                synchronizeColumnsWidth(masterColumn, column);
            }
        }
    }

    public static void synchronizeColumnsWidth(@NotNull List<? extends TableColumn<?, ?>> masterColumns, @NotNull List<? extends TableColumn<?, ?>> slaveColumns) {
        assert masterColumns.size() == slaveColumns.size();
        for (int idxCol = 0; idxCol < masterColumns.size(); idxCol++) {
            TableColumn<?, ?> masterColumn = masterColumns.get(idxCol);
            TableColumn<?, ?> slaveColumn = slaveColumns.get(idxCol);
            synchronizeColumnsWidth(masterColumn, slaveColumn);
        }
    }

    public static void synchronizeColumnsWidth(@NotNull TableColumn<?, ?> slaveColumn, @NotNull List<? extends TableColumn<?, ?>> masterColumns) {
        DoubleBinding masterColumnsWidthSumProperty = Bindings.selectDouble(new SimpleDoubleProperty(0));
        for (TableColumn<?, ?> masterColumn : masterColumns) {
            DoubleBinding masterColumnWidth = synchronizedColumnWidth(masterColumn);
            masterColumnsWidthSumProperty = masterColumnsWidthSumProperty.add(masterColumnWidth);
        }
        synchronizeColumnWidth(slaveColumn, masterColumnsWidthSumProperty);
    }

    public static void synchronizeColumnsWidth(@NotNull TableColumn<?, ?> masterColumn, @NotNull TableColumn<?, ?> slaveColumn) {
        DoubleBinding masterColumnWidthProperty = synchronizedColumnWidth(masterColumn);
        synchronizeColumnWidth(slaveColumn, masterColumnWidthProperty);
    }

    public static void synchronizeColumnWidth(@NotNull TableColumn<?, ?> column, @NotNull DoubleBinding columnWidthProperty) {
        column.prefWidthProperty().bind(columnWidthProperty);
        column.minWidthProperty().bind(columnWidthProperty);
        column.maxWidthProperty().bind(columnWidthProperty);
    }

    private static DoubleBinding synchronizedColumnWidth(TableColumn<?, ?> masterColumn) {
        return masterColumn.widthProperty().multiply(Bindings.when(masterColumn.visibleProperty()).then(1).otherwise(0));
    }


/*
    private static final Map<String, List<ChangeListener>> ensureDisplayingRowsChangeListeners = new HashMap<>(10);
    private static final Map<String, IntegerBinding> ensureDisplayingRowsBindings = new HashMap<>(10);
*/

    public static <S> void ensureDisplayingAllRows(@NotNull TableView<S> table) {
        ensureDisplayingRows(table, null);
    }

    public static <S> void ensureDisplayingRows(@NotNull TableView<S> table, @Null Integer rowCount) {
        // Cf. https://stackoverflow.com/questions/27945817/javafx-adapt-tableview-height-to-number-of-rows
/*
        assert table.getFixedCellSize() > 0; // _TODO FDA 2017/08 Trouver un meilleur code pour ce contrôle.
        DoubleBinding height = new SimpleDoubleProperty(10.0); // headerRow(table).heightProperty(); // _TODO FDA 2017/08 Debugger (voir F I X M E dans méthode "headerRow"), puis réactiver.
        DoubleBinding tableHeight = table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(height));
        table.minHeightProperty().bind(tableHeight);
        table.prefHeightProperty().bind(tableHeight);
        table.maxHeightProperty().bind(tableHeight);
*/
/*
        adjustHeight(table, rowCount);

        String tableId = table.getId();
        assert tableId != null;

        if (ensureDisplayingRowsBindings.containsKey(tableId)) {
            IntegerBinding binding = ensureDisplayingRowsBindings.get(tableId);
            assert ensureDisplayingRowsChangeListeners.containsKey(tableId);
            //noinspection rawtypes
            List<ChangeListener> changeListeners = ensureDisplayingRowsChangeListeners.get(tableId);
            //noinspection rawtypes
            for (ChangeListener changeListener : changeListeners) {
                //noinspection unchecked
                binding.removeListener(changeListener);
            }
        }
        if (ensureDisplayingRowsChangeListeners.containsKey(tableId)) {
            //noinspection rawtypes
            List<ChangeListener> changeListeners = ensureDisplayingRowsChangeListeners.get(tableId);
            //noinspection rawtypes
            for (ChangeListener changeListener : changeListeners) {
                //noinspection unchecked
                table.skinProperty().removeListener(changeListener);
                //noinspection unchecked
                table.fixedCellSizeProperty().removeListener(changeListener);
            }
        }

        ensureDisplayingRowsChangeListeners.put(tableId, new ArrayList<>(0));
        // On ajuste la hauteur de la table lorsque la Skin de la TableView est Settée notamment pour récupérer la hauteur de l'entête de la table (pas tout compris, copié/collé d'Internet).
        {
            ChangeListener<Skin<?>> changeListener = (observable, oldValue, newValue) -> {
                if (!java.util.Objects.equals(oldValue, newValue)) {
                    adjustHeight(table, rowCount);
                }
            };
            //noinspection unchecked
            table.skinProperty().addListener(changeListener);
            ensureDisplayingRowsChangeListeners.get(tableId).add(changeListener);
        }

        {
            ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> {
                if (!java.util.Objects.equals(oldValue, newValue)) {
                    adjustHeight(table, rowCount);
                }
            };
            //noinspection unchecked
            table.fixedCellSizeProperty().addListener(changeListener);
            ensureDisplayingRowsChangeListeners.get(tableId).add(changeListener);
        }
        {
            IntegerBinding itemsSizeBinding = Bindings.size(table.getItems());
            ensureDisplayingRowsBindings.put(tableId, itemsSizeBinding);
            ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> {
                if (!java.util.Objects.equals(oldValue, newValue)) {
                    adjustHeight(table, Objects.<Integer, Integer>value(rowCount, rowCountValue -> Math.min(rowCountValue, newValue.intValue()), newValue.intValue()));
                }
            };
            itemsSizeBinding.addListener(changeListener);
            ensureDisplayingRowsChangeListeners.get(tableId).add(changeListener);
        }
*/

        IntegerBinding rowCountBinding = (IntegerBinding) Bindings.max(
                1, // On assure d'avoir au moins 1 ligne pour que le message std de JavaFX "Aucun contenu dans la table" soit visible par l'utilisateur.
                (rowCount != null) ? new IntegerBinding() {
                    @Override
                    protected int computeValue() {
                        return rowCount;
                    }
                } : Bindings.size(table.getItems()) // NB : table.getItems() peut ne pas encore contenir les nouveaux items, mais encore les anciens items.
        );

        assert table.getFixedCellSize() > 0.0 : "La tableView doit avoir la propriété 'fixedCellSize' définie."; // TODO FDA 2017/08 Trouver un meilleur code pour ce contrôle.

        // Cf. https://stackoverflow.com/questions/26298337/tableview-adjust-number-of-visible-rows
        DoubleBinding tableHeightBinding = rowCountBinding.multiply(table.getFixedCellSize()).add(40.0);// Additional pixels are for tableview's header heght.

        table.prefHeightProperty().unbind();
        table.prefHeightProperty().bind(tableHeightBinding);

        LOGGER.debug("Table height ensured for table {}.", table.getId());
    }

/*
    private static void adjustHeight(@NotNull TableView<?> table, @Null Integer rowCount) {

        assert table.getFixedCellSize() > 0.0 : "La tableView doit avoir la propriété 'fixedCellSize' définie."; // TODO FDA 2017/08 Trouver un meilleur code pour ce contrôle.

        TableHeaderRow headerRow = headerRow(table);
        // Le TableHeaderRow n'est pas défini tant que la CSS n'a pas été évaluée (pas tout compris, copié/collé d'Internet).
        if (headerRow == null) {
            return;
        }
        double headerRowHeight = headerRow.getHeight();
        if (headerRowHeight == 0.0) {
            LOGGER.warn("Table {} without header (TableRowHeader.height==0)!?", table.getId());
            headerRowHeight = table.getFixedCellSize(); // Approximation.
        }

        double dataRowHeight = table.getFixedCellSize();

        int actualRowCount = Math.max(
                1, // On assure d'avoir au moins 1 ligne pour que le message std de JavaFX "Aucun contenu dans la table" soit visible par l'utilisateur.
                Objects.value(rowCount, table.getItems().size()) // NB : table.getItems() peut ne encore contenir les nouveaux items, mais encore les anciens items.
        );

        double dataRowsHeith = dataRowHeight * (double) actualRowCount;

        double tableHeight = headerRowHeight + dataRowsHeith + Math.max(15.0, (1.0 * (double) actualRowCount)); // TODO FDA 2017/08 Comprendre pourquoi il faut ajouter un peu d'espace (1 pt par ligne) en plus.

        table.setMinHeight(tableHeight);
        table.setPrefHeight(tableHeight);
        table.setMaxHeight(tableHeight);

        LOGGER.debug("Table height set to {} for table {}.", tableHeight, table.getId());
    }
*/


    // Input :

    public static void decorateMandatoryColumns(@NotNull TableColumn<?, ?>... columns) throws IhmException {
        for (TableColumn<?, ?> column : columns) {
            // TODO FDA 2017/07 Confirmer qu'utiliser un Label (vide) est une bonne façon de faire.
            Label label = new Label();
            PlanChargeIhm.symboliserChampsObligatoires(label);
            column.setGraphic(label);
        }
    }

    public static <S, T> void editCell(@NotNull TableView<S> table, @NotNull S item, @NotNull TableColumn<S, T> column) {

        focusOnItem(table, item);

        int itemIdx = itemIndex(table, item);
        assert itemIdx != -1;
        table.edit(itemIdx, column); // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en mode édition de la (bonne) cellule.
    }


    // Filtering :

    public static <S> void enableFilteringOnColumns(@NotNull TableView<S> table, @NotNull List<TableColumn<S, ?>> columns) {
        Builder<S> filterBuilder = TableFilter.forTableView(table);
//        filter.lazy(true); // FDA 2017/07 Confirmer (ne semble rien changer).
        filterBuilder.apply();

        decorateFilterableColumns(columns);
    }

    @SuppressWarnings("WeakerAccess")
    public static <S> void decorateFilterableColumns(@NotNull Collection<TableColumn<S, ?>> columns) {
//        Platform.runLater(() -> { // TODO FDA 2017/07 Supprimer si non nécessaire/utile.
            for (TableColumn<?, ?> column : columns) {
                if (column.getGraphic() == null) {
                    Label label = new Label();
                    column.setGraphic(label);
                }
                try {
                    PlanChargeIhm.symboliserNoeudsFiltrables(column.getGraphic());
                } catch (IhmException e) {
                    // TODO FDA 2017/07 Trouver mieux que thrower une loguer et/ou une RuntimeException.
                    LOGGER.error("Impossible de symboliser le caractère filtrable d'une colonne de la table.", e);
                    throw new RuntimeException("Impossible de symboliser le caractère filtrable d'une colonne de la table.", e);
                }
            }
//        });
    }

    public static <S> void ensureSorting(@NotNull TableView<S> table) {
//        table.getItems().addListener((ListChangeListener<? super S>) change -> {
        SortedList<S> sortedBeans = new SortedList<>(originalUnsortedItems(table.getItems()));
        sortedBeans.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedBeans);
//        });
    }

    private static <S> ObservableList<S> originalUnsortedItems(@NotNull ObservableList<S> list) {
        if (!(list instanceof SortedList)) {
            return list;
        }
        //noinspection unchecked
        return TableViews.<S>originalUnsortedItems(((SortedList)list).getSource());
    }
}
