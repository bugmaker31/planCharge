package fr.gouv.agriculture.dal.ct.ihm.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.LocalDateStringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Inspiré de http://physalix.com/javafx8-render-a-datepicker-cell-in-a-tableview/
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithoutLogger"})
public class DatePickerTableCell<S> extends TextFieldTableCell<S, LocalDate> {

    @NotNull
    private final String prompt;
    @NotNull
    private final DatePicker datePicker;


    public DatePickerTableCell(@NotNull String format, @NotNull String promptText) {
        super(new LocalDateStringConverter(DateTimeFormatter.ofPattern(format), DateTimeFormatter.ofPattern(format)));
        this.prompt = promptText;

        this.datePicker = createDatePicker();
    }


    public BooleanProperty showWeekNumbersProperty() {
        return datePicker.showWeekNumbersProperty();
    }

    public void setShowWeekNumbers(boolean value) {
        datePicker.setShowWeekNumbers(value);
    }

    @NotNull
    public ObjectProperty<LocalDate> valueProperty() {
        return datePicker.valueProperty();
    }


    private DatePicker createDatePicker() {
        DatePicker newDatePicker = new DatePicker();
        newDatePicker.setPromptText(prompt);
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
    public void updateItem(@Null LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        datePicker.setValue(getItem());
        formater();
    }

    private void formater() {
        if ((getItem() == null) || isEmpty()) {
            setText(null);
            setGraphic(null);
            return;
        }
        setText(getConverter().toString(getItem()));
    }

    @Override
    public void startEdit() {
        super.startEdit();
        showDatePicker();
    }

    @Override
    public void commitEdit(@Null LocalDate newValue) {
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