package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class Ressource extends AbstractEntity<String> implements Comparable<Ressource> {

    private final String trigramme;

    public Ressource(String trigramme) {
        this.trigramme = trigramme;
    }

    public String getTrigramme() {
        return trigramme;
    }

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
