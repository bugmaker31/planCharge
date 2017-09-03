package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class JourFerie extends AbstractEntity<LocalDate, JourFerie> implements Comparable<JourFerie> {


    private static class DateJourFerieComparator implements Comparator<JourFerie> {

        @Override
        public int compare(@NotNull JourFerie jf1, @NotNull JourFerie jf2) {
/*
            if ((jf1.getDate() == null) && (jf2.getDate() == null)) {
                return 0;
            }
            if ((jf1.getDate() == null) && (jf2.getDate() != null)) {
                return 1;
            }
            if ((jf1.getDate() != null) && (jf2.getDate() == null)) {
                return -1;
            }
*/
            return jf1.getDate().compareTo(jf2.getDate());
        }

    }

    @SuppressWarnings("WeakerAccess")
    public static final DateJourFerieComparator COMPARATOR_DATE = new DateJourFerieComparator();

    @SuppressWarnings("WeakerAccess")
    public static final Comparator<JourFerie> COMPARATOR_DEFAUT = COMPARATOR_DATE;


    @NotNull
    private LocalDate date;
    @Null
    private String description;

    public JourFerie(@NotNull LocalDate date, @Null String description) {
        super();
        this.date = date;
        this.description = description;
    }

    @NotNull
    public LocalDate getDate() {
        return date;
    }

    @Null
    public String getDescription() {
        return description;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @NotNull
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
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                + (" " + (description == null ? "N/C" : ('"' + description + '"')));
    }
}
