package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class JourFerieBean {

    @NotNull
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object;

    @NotNull
    private StringProperty description = new SimpleStringProperty();


    @NotNull
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    @NotNull
    public StringProperty descriptionProperty() {
        return description;
    }

    @Override
    public String toString() {
        return date.getValue().format(DateTimeFormatter.BASIC_ISO_DATE)
                + " " + description.getValue();
    }
}
