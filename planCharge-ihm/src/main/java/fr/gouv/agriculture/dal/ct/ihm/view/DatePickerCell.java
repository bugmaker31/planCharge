package fr.gouv.agriculture.dal.ct.ihm.view;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;

// Cf. http://physalix.com/javafx8-render-a-datepicker-cell-in-a-tableview/
@SuppressWarnings("ClassHasNoToStringMethod")
public class DatePickerCell<S> extends TableCell<S, LocalDate> {

    @NotNull
    private final String format;
    @NotNull
    private final BiConsumer<S, LocalDate> dateSetter;
    @NotNull
    private final DateTimeFormatter dateFormatter;

    @NotNull
    private DatePicker datePicker;


    public DatePickerCell(@NotNull String format, @NotNull BiConsumer<S, LocalDate> dateSetter) {

        super();

        this.format = format;
        this.dateSetter = dateSetter;

        this.dateFormatter = DateTimeFormatter.ofPattern(format);

//        if (datePicker == null) {
        datePicker = createDatePicker();
//        }
        setGraphic(datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        Platform.runLater(() -> datePicker.requestFocus());
    }


    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || (item == null)) {
            setText(null);
            setGraphic(null);
            return;
        }

        if (isEditing()) {
            setContentDisplay(ContentDisplay.TEXT_ONLY);
            return;
        }

        datePicker.setValue(item);
        setText(dateFormatter.format(item));
        setGraphic(this.datePicker);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    private DatePicker createDatePicker() {
        DatePicker newDatePicker = new DatePicker();
        newDatePicker.setPromptText(format);
        newDatePicker.setEditable(true);

        newDatePicker.setOnAction(t -> {
            LocalDate date = newDatePicker.getValue();
            commitEdit(date);
        });

        setAlignment(Pos.CENTER);

        return newDatePicker;
    }

    @Override
    public void startEdit() {
        super.startEdit();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setContentDisplay(ContentDisplay.TEXT_ONLY);
    }
}