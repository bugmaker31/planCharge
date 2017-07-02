package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class JourFerie extends AbstractEntity<String> implements Comparable<JourFerie> {

    @NotNull
    private LocalDate date;
    @NotNull
    private String description;

    public JourFerie(@NotNull LocalDate date, @NotNull String description) {
        this.date = date;
        this.description = description;
    }

    @NotNull
    public LocalDate getDate() {
        return date;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @Override
    public String getIdentity() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    public int compareTo(JourFerie o) {
        return date.compareTo(o.getDate());
    }

    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + ('"' + description + '"');
    }
}
