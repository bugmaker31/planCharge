package fr.gouv.agriculture.dal.ct.ihm.view;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.BiConsumer;

// Inspiré de http://physalix.com/javafx8-render-a-datepicker-cell-in-a-tableview/
@SuppressWarnings("ClassHasNoToStringMethod")
public class DatePickerTableCell<S> extends TextFieldTableCell<S, LocalDate> {

    @NotNull
    private final String format;
    @NotNull
    private DatePicker datePicker;


    public DatePickerTableCell(@NotNull String format) {
        super();

        this.format = format;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        setConverter(new LocalDateStringConverter(formatter, formatter));

        datePicker = createDatePicker();

        Platform.runLater(() -> {

        });
    }


    public BooleanProperty showWeekNumbersProperty() {
        return datePicker.showWeekNumbersProperty();
    }

    public void setShowWeekNumbers(boolean value) {
        datePicker.setShowWeekNumbers(value);
    }


    private DatePicker createDatePicker() {
        DatePicker newDatePicker = new DatePicker();
        newDatePicker.setPromptText(format);
        newDatePicker.setEditable(true);
        newDatePicker.setShowWeekNumbers(true); // Par défaut.

//        this.itemProperty().bindBidirectional(newDatePicker.valueProperty());
        newDatePicker.setOnAction(t -> {
            LocalDate date = newDatePicker.getValue();
//            setItem(date);  No, cause effectively changes the cell value, thus 'cancel' won't have the "rollback effect" any longer.
            commitEdit(date);
        });

        return newDatePicker;
    }


    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        datePicker.setValue(item);
    }


    @Override
    public void startEdit() {
        super.startEdit();
        showDatePicker();
    }

    @Override
    public void commitEdit(LocalDate newValue) {
        super.commitEdit(newValue);
        hideDatePicker();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        hideDatePicker();
    }


    private void showDatePicker() {
        setGraphic(datePicker);
//        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//        setAlignment(Pos.CENTER);
    }

    private void hideDatePicker() {
        setGraphic(null);
//        setContentDisplay(ContentDisplay.TEXT_ONLY);
//        setAlignment(originalAlignment);
    }



}