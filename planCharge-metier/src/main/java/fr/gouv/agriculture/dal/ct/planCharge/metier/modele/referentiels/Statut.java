package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * Created by frederic.danna on 24/06/2017.
 */
public class Statut extends AbstractEntity<String, Statut> implements Comparable<Statut> {

    public static final Statut PROVISION = new Statut("80-Récurrente");

    @NotNull
    private final String code;

    public Statut(@NotNull String code) {
        super();
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
