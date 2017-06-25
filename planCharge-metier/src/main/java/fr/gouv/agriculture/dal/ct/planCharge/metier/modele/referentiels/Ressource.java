package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

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
    @Override
    public int compareTo(@NotNull Ressource o) {
        return trigramme.compareTo(o.getTrigramme());
    }
}
