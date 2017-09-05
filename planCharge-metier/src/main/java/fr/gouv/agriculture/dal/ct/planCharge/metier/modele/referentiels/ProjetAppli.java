package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class ProjetAppli extends AbstractEntity<String, ProjetAppli> implements Comparable<ProjetAppli> {

    @NotNull
    private String code;

    @Null
    private String nom;

    @Null
    private String trigrammeCPI;


    public ProjetAppli(@NotNull String code, @Null String nom, @Null String trigrammeCPI) {
        super();
        this.code = code;
        this.nom = nom;
        this.trigrammeCPI = trigrammeCPI;
    }


    @NotNull
    public String getCode() {
        return code;
    }

    @Null
    public String getNom() {
        return nom;
    }

    @Null
    public String getTrigrammeCPI() {
        return trigrammeCPI;
    }


    @NotNull
    @Override
    public String getIdentity() {
        return code;
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
