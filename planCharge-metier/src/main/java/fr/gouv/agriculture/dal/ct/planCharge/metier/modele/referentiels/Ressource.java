package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class Ressource extends AbstractEntity<String> implements Comparable<Ressource> {

    @NotNull
    private final String trigramme;


    public Ressource(@NotNull String trigramme) {
        this.trigramme = trigramme;
    }


    @NotNull
    public String getTrigramme() {
        return trigramme;
    }


    @NotNull
    @Override
    public String getIdentity() {
        return getTrigramme();
    }


    @NotNull
    public boolean estHumain() {
        return this instanceof RessourceHumaine;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ressource ressource = (Ressource) o;

        return trigramme.equals(ressource.trigramme);
    }

    @Override
    public int hashCode() {
        return trigramme.hashCode();
    }


    @NotNull
    @Override
    public int compareTo(@NotNull Ressource o) {
        return trigramme.compareTo(o.getTrigramme());
    }


    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + trigramme + "]");
    }
}
