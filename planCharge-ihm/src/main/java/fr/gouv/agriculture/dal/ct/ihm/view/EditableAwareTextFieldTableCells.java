package fr.gouv.agriculture.dal.ct.ihm.view;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * Created by frederic.danna on 16/07/2017.
 */
public class EditableAwareTextFieldTableCells {

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forRequiredTableColumn() {
        return (TableColumn<S, String> list) -> {
            //noinspection unchecked
            TableCell<S, String> textFieldCell = EditableAwareTextFieldTableCell.<S>forTableColumn().call(list);
//            PlanChargeIhm.symboliserColonnesObligatoires(textFieldCell);
            return textFieldCell;
        };
    }

    public static  <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
        return EditableAwareTextFieldTableCell.forTableColumn();
    }
}
