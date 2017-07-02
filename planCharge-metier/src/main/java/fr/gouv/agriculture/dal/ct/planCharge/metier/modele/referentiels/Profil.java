package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class Profil extends AbstractEntity<String> implements Comparable<Profil> {

    public static final Profil TOUS = new Profil("*");


    @NotNull
    private final String code;

    public Profil(@NotNull String code) {
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
    public int compareTo(@NotNull Profil o) {
        return code.compareTo(o.getCode());
    }
}
