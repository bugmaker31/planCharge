package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class JourFerieBean extends AbstractBean<JourFerieDTO, JourFerieBean> implements Comparable<JourFerieBean> {

    @NotNull
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object;

    @NotNull
    private StringProperty description = new SimpleStringProperty();


    public JourFerieBean() {
        super();
    }

    public JourFerieBean(@NotNull JourFerieDTO jourFerie) {
        this.date.set(jourFerie.getDate());
        this.description.set(jourFerie.getDescription());
    }

    public JourFerieBean(@NotNull LocalDate date, @NotNull String description) {
        this.date.set(date);
        this.description.set(description);
    }

    @NotNull
    public static JourFerieBean from(@NotNull JourFerieDTO jourFerieDTO) {
        return new JourFerieBean().fromDto(jourFerieDTO);
    }

    @NotNull
    public static JourFerieDTO to(@NotNull JourFerieBean jourFerieBean) {
        return jourFerieBean.toDto();
    }

    @Null
    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(@NotNull LocalDate date) {
        this.date.set(date);
    }

    @NotNull
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    @Null
    public String getDescription() {
        return description.get();
    }

    @NotNull
    public StringProperty descriptionProperty() {
        return description;
    }

    @NotNull
    @Override
    public JourFerieBean fromDto(@NotNull JourFerieDTO dto) {
        return new JourFerieBean(dto);
    }

    @NotNull
    public JourFerieDTO toDto() {
        return new JourFerieDTO(date.get(), description.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        JourFerieBean that = (JourFerieBean) o;

        return (getDate() != null) ? getDate().equals(that.getDate()) : (that.getDate() == null);
    }

    @Override
    public int hashCode() {
        return (getDate() != null) ? getDate().hashCode() : 0;
    }


    @Override
    public int compareTo(@NotNull JourFerieBean o) {
        return (
                (this.getDate() == null) && (o.getDate() == null)) ? 0
                : (((this.getDate() == null) && (o.getDate() != null)) ? -1
                : (((this.getDate() != null) && (o.getDate() == null)) ? 1
                : this.getDate().compareTo(o.getDate()))
        );
    }

    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return "Jour férié"
                + " " + Objects.value(date, dateProperty -> dateProperty.get().format(DateTimeFormatter.ISO_LOCAL_DATE), "N/C")
                + " " + Objects.value(description.get(), "N/C");
    }

}
