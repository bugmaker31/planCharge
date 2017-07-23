package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.Controlable;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RGRefJourFerieDateObligatoire;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class JourFerie extends AbstractEntity<LocalDate, JourFerie> implements Comparable<JourFerie> {


    private static class DateJourFerieComparator implements Comparator<JourFerie> {

        @Override
        public int compare(@NotNull JourFerie jf1, @NotNull JourFerie jf2) {
            return jf1.getDate().compareTo(jf2.getDate());
        }

    }

    public static final DateJourFerieComparator COMPARATOR_DATE = new DateJourFerieComparator();

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

    @NotNull
    public String getDescription() {
        return description;
    }


    @Override
    public LocalDate getIdentity() {
        return date;
    }


    @NotNull
    public Set<RegleGestion<JourFerie>> getReglesGestion() {
        Set<RegleGestion<JourFerie>> regles = new HashSet<>();
        regles.add(RGRefJourFerieDateObligatoire.INSTANCE);
        // TODO FDA 2017/07 Ajouter les autres règles de gestion.
        return regles;
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
