package fr.gouv.agriculture.dal.ct.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

/**
 * Created by frederic.danna on 16/07/2017.
 */
public class TextFieldTableCells {

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, String>> forRequiredTableColumn() {
        return (TableColumn<S, T> list) -> {
            //noinspection unchecked
            TableCell<S, String> textFieldCell = TextFieldTableCell.<S>forTableColumn().call((TableColumn<S, String>) list);
            PlanChargeIhm.symboliserChampObligatoire(textFieldCell);
            return textFieldCell;
        };
    }
}
