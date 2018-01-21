package fr.gouv.agriculture.dal.ct.ihm.view;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 15/07/2017.
 */
public class UpperCaseTextFieldTableCell<S> extends TextFieldTableCell<S, String> {

    private static final StringConverter<String> UPPERCASE_STRING_CONVERTER = new DefaultStringConverter() {
        @Null
        @Override
        public String fromString(String value) {
            return ((value == null) ? null : value.toUpperCase());
        }
    };

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
        return NotEditableTextFieldTableCell.forTableColumn(UPPERCASE_STRING_CONVERTER);
    }

    public UpperCaseTextFieldTableCell() {
        super(UPPERCASE_STRING_CONVERTER);
    }
}
