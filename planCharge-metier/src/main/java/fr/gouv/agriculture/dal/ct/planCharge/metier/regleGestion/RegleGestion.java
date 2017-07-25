package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;

import javax.validation.constraints.NotNull;

public abstract class RegleGestion<T extends AbstractEntity> {

    /**
     * Nom de l'attribut (statique) qui permet de récupérer dynamiquement l'instance (singleton) de chaque classe qui
     * étend {@link RegleGestion}.
     */
    public static final String INSTANCE_FIELD_NAME = "INSTANCE";


    @NotNull
    private String code;
    @NotNull
    private String libelle;
    @NotNull
    private String messageErreur;
    @NotNull
    private PlanCharge planCharge;


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

    @NotNull
    public PlanCharge getPlanCharge() {
        return planCharge;
    }

    public void setPlanCharge(@NotNull PlanCharge planCharge) {
        this.planCharge = planCharge;
    }


    abstract public boolean estValide(@NotNull T entity);
}
