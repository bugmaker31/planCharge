package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 24/06/2017.
 */
public class Statut extends AbstractEntity<String, Statut> {

    public static final Statut PROVISION = new Statut("80-Récurrente");
    public static final Statut REPORTEE = new Statut("85-Reportée");

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


    @SuppressWarnings("SuspiciousGetterSetter")
    @NotNull
    @Override
    public String getIdentity() {
        return code;
    }


    @Override
    public int compareTo(@NotNull Statut o) {
        return code.compareTo(o.getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statut statut = (Statut) o;

        return code.equals(statut.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return code;
    }
}
