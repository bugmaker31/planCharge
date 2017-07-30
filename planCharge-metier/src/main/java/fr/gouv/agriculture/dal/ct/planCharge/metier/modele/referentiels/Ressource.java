package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public abstract class Ressource<T extends Ressource<T>> extends AbstractEntity<String, T> implements Comparable<T> {

    @NotNull
    private final String code;


    public Ressource(@NotNull String code) {
        this.code = code;
    }


    @NotNull
    public String getCode() {
        return code;
    }


    @NotNull
    @Override
    public String getIdentity() {
        return getCode();
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

        return code.equals(ressource.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }


    @NotNull
    @Override
    public int compareTo(@NotNull Ressource o) {
        return code.compareTo(o.getCode());
    }


    // Juste pour faciliter le debogage.
    @Override
    public String toString() {
        return ("[" + code + "]");
    }
}
