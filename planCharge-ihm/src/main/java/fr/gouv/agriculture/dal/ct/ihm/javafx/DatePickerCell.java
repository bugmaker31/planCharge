package fr.gouv.agriculture.dal.ct.ihm.javafx;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Created by frederic.danna on 11/05/2017.
 * <p>
 * Inspiré de james-d/TableViewSampleWithBirthday.java (https://gist.github.com/james-d/9776485).
 *
 * @author frederic.danna
 */
public class DatePickerCell<T> extends TableCell<T, String> {

    private final DateTimeFormatter formatter;
    private final DatePicker datePicker;

    public DatePickerCell(String dateTimePattern) {

        formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        datePicker = new DatePicker();

        // Commit edit on Enter and cancel on Escape.
        // Note that the default behavior consumes key events, so we must
        // register this as an event filter to capture it.
        // Consequently, with Enter, the datePicker's value won't yet have been updated,
        // so commit will sent the wrong value. So we must update it ourselves from the
        // editor's text value.

        datePicker.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if ((event.getCode() == KeyCode.ENTER) || (event.getCode() == KeyCode.TAB)) {
                datePicker.setValue(datePicker.getConverter().fromString(datePicker.getEditor().getText()));
                LocalDate date = datePicker.getValue();
                commitEdit(date.format(formatter));
            }
            if (event.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        // Modify default mouse behavior on date picker:
        // Don't hide popup on single click, just set date
        // On double-click, hide popup and commit edit for editor
        // Must consume event to prevent default hiding behavior, so
        // must update date picker value ourselves.

        // Modify key behavior so that enter on a selected cell commits the edit
        // on that cell's date.

        datePicker.setDayCellFactory(picker -> {
            DateCell cell = new DateCell();
            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                LocalDate date = cell.getItem();
                datePicker.setValue(date);
                if (event.getClickCount() == 2) {
                    datePicker.hide();
                    commitEdit(date.format(formatter));
                }
                event.consume();
            });
            cell.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    LocalDate date = datePicker.getValue();
                    commitEdit(date.format(formatter));
                }
            });
            return cell;
        });

        contentDisplayProperty().bind(Bindings.when(editingProperty())
                .then(ContentDisplay.GRAPHIC_ONLY)
                .otherwise(ContentDisplay.TEXT_ONLY));
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item);
            setGraphic(datePicker);
        }
    }

    @Override
    public void startEdit() {
        super.startEdit();

        String dateStr = getItem();
        if (emptyProperty().get() || isEmpty() || (dateStr == null)) {
            return;
        }

        try {
            datePicker.setValue(LocalDate.parse(dateStr, formatter));
        } catch (DateTimeParseException e) {
            // TODO FDA 2017/05 Trouver mieux qu'un RuntimeException.
            throw new RuntimeException("Can't parse date '" + dateStr + "', thus can't set DatePicker value.", e);
        }
    }

}
