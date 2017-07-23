package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

public abstract class RegleGestion<T extends AbstractEntity> {

    @NotNull
    private String code;
    @NotNull
    private String libelle;
    @NotNull
    private String messageErreur;


    public RegleGestion(@NotNull String code, @NotNull String libelle, @NotNull String messageErreur) {
        this.code = code;
        this.libelle = libelle;
        this.messageErreur = messageErreur;
    }


    @NotNull
    public String getCode() {
        return code;
    }

    @NotNull
    public String getLibelle() {
        return libelle;
    }

    @NotNull
    public String getMessageErreur() {
        return messageErreur;
    }


    abstract public boolean estValide(@NotNull T entity);
}
