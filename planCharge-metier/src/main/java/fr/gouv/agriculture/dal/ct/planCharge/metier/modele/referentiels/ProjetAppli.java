package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class ProjetAppli extends AbstractEntity<String, ProjetAppli> implements Comparable<ProjetAppli> {

    @NotNull
    private final String code;


    public ProjetAppli(@NotNull String code) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        ProjetAppli that = (ProjetAppli) o;

        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }


    @NotNull
    @Override
    public int compareTo(@NotNull ProjetAppli o) {
        return code.compareTo(o.getCode());
    }


    // pour déboguer, uniquement.
    @Override
    public String toString() {
        return code;
    }

}
