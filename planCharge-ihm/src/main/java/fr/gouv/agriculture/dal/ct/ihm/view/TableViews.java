package fr.gouv.agriculture.dal.ct.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDAbsenceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursOuvresBean;
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
}
