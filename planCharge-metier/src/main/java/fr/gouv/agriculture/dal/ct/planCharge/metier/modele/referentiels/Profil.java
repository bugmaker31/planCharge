package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.MetierException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.ViolationRegleGestion;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class Profil extends AbstractEntity<String, Profil> implements Comparable<Profil> {

    public static final Profil TOUS = new Profil("*");


    @NotNull
    private final String code;

    public Profil(@NotNull String code) {
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
        return getCode();
    }


    @NotNull
    @Override
    public Set<RegleGestion<Profil>> getReglesGestion() {
        return new HashSet<>(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @NotNull
    @Override
    public int compareTo(@NotNull Profil o) {
        return code.compareTo(o.getCode());
    }
}
