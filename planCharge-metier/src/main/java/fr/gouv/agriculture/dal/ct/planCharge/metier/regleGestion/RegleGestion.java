package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

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
    private Function<T, String> messageErreur;

    @NotNull
    private PlanCharge planCharge;


    public RegleGestion(@NotNull String code, @NotNull String libelle, @NotNull Function<T, String> messageErreur) {
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
    public Function<T, String> getMessageErreur() {
        return messageErreur;
    }


    @NotNull
    public PlanCharge getPlanCharge() {
        return planCharge;
    }

    public void setPlanCharge(@NotNull PlanCharge planCharge) {
        this.planCharge = planCharge;
    }


    abstract public boolean estApplicable(@NotNull T entity);

    abstract public boolean estValide(@NotNull T entity);


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return "[" + code + "]" +
                " " + libelle;
    }
}
