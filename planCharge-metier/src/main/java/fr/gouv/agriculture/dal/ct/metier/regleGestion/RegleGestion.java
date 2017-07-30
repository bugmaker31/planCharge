package fr.gouv.agriculture.dal.ct.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

public abstract class RegleGestion<T extends AbstractDTO> {

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
    private Function<T, String> formateurMessage;

    @NotNull
    private PlanChargeDTO planChargeDTO;


    public RegleGestion(@NotNull String code, @NotNull String libelle, @NotNull Function<T, String> formateurMessage) {
        this.code = code;
        this.libelle = libelle;
        this.formateurMessage = formateurMessage;
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
    public Function<T, String> getFormateurMessage() {
        return formateurMessage;
    }


    @NotNull
    public PlanChargeDTO getPlanChargeDTO() {
        return planChargeDTO;
    }

    public void setPlanCharge(@NotNull PlanChargeDTO planChargeDTO) {
        this.planChargeDTO = planChargeDTO;
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
