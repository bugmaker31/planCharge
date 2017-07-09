package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class JourFerie extends AbstractEntity<LocalDate> implements Comparable<JourFerie> {


    private static class DateJourFerieComparator implements Comparator<JourFerie> {

        @Override
        public int compare(@NotNull JourFerie jf1, @NotNull JourFerie jf2) {
            return jf1.getDate().compareTo(jf2.getDate());
        }

    }

    public static final DateJourFerieComparator COMPARATOR_DATE = new DateJourFerieComparator();

    public static final Comparator<JourFerie> COMPARATOR_DEFAUT = COMPARATOR_DATE;



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
    public LocalDate getIdentity() {
        return date;
    }


    @Override
    public int compareTo(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull JourFerie other) {
        return COMPARATOR_DEFAUT.compare(this, other);
    }


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + ('"' + description + '"');
    }
}
