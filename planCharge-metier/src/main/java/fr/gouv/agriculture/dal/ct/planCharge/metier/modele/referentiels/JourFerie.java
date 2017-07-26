package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels.RGRefJourFerieDateObligatoire;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels.RGRefJourFerieUniciteJour;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class JourFerie extends AbstractEntity<LocalDate, JourFerie> implements Comparable<JourFerie> {


    private static class DateJourFerieComparator implements Comparator<JourFerie> {

        @Override
        public int compare(@NotNull JourFerie jf1, @NotNull JourFerie jf2) {
            if ((jf1.getDate() == null) && (jf2.getDate() == null)) {
                return 0;
            }
            if ((jf1.getDate() == null) && (jf2.getDate() != null)) {
                return 1;
            }
            if ((jf1.getDate() != null) && (jf2.getDate() == null)) {
                return -1;
            }
            return jf1.getDate().compareTo(jf2.getDate());
        }

    }

    @SuppressWarnings("WeakerAccess")
    public static final DateJourFerieComparator COMPARATOR_DATE = new DateJourFerieComparator();

    @SuppressWarnings("WeakerAccess")
    public static final Comparator<JourFerie> COMPARATOR_DEFAUT = COMPARATOR_DATE;


    @Null
    private LocalDate date;
    @Null
    private String description;

    public JourFerie(@Null LocalDate date, @Null String description) {
        super();
        this.date = date;
        this.description = description;
    }

    @Null
    public LocalDate getDate() {
        return date;
    }

    @Null
    public String getDescription() {
        return description;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @Null
    @Override
    public LocalDate getIdentity() {
        return date;
    }


    @NotNull
    public List<RegleGestion<JourFerie>> getReglesGestion() {
        return Arrays.asList(
                RGRefJourFerieDateObligatoire.INSTANCE,
                RGRefJourFerieUniciteJour.INSTANCE
                // Ajouter ici les futures règles de gestion.
        );
    }


    @Override
    public int compareTo(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull JourFerie other) {
        return COMPARATOR_DEFAUT.compare(this, other);
    }


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return (date == null ? "N/C" : date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                + " " + (description == null ? "N/C" : ('"' + description + '"'));
    }
}
