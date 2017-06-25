package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 24/06/2017.
 */
public class Statut  extends AbstractEntity<String> implements Comparable<Statut> {

    @NotNull
    private final String code;

    public Statut(@NotNull String code) {
        this.code = code;
    }

    @NotNull
    public String getCode() {
        return code;
    }

    @NotNull
    @Override
    public String getIdentity() {
        return code;
    }

    @NotNull
    @Override
    public int compareTo(@NotNull Statut o) {
        return code.compareTo(o.getCode());
    }
}
