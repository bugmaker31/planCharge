package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
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


    public JourFerieBean() {
        super();
    }

    public JourFerieBean(@NotNull JourFerie jourFerie) {
        this.date.set(jourFerie.getDate());
        this.description.set(jourFerie.getDescription());
    }


    @NotNull
    public LocalDate getDate() {
        return date.get();
    }

    @NotNull
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(@NotNull LocalDate date) {
        this.date.set(date);
    }

    @NotNull
    public String getDescription() {
        return description.get();
    }

    @NotNull
    public StringProperty descriptionProperty() {
        return description;
    }


    @NotNull
    public JourFerie extract() {
        return new JourFerie(date.get(), description.get());
    }


    @Override
    public String toString() {
        return date.get().format(DateTimeFormatter.BASIC_ISO_DATE)
                + " " + description.get();
    }
}
