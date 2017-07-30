package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collections;
import java.util.List;

/**
 * Created by frederic.danna on 24/06/2017.
 */
public class StatutDTO extends AbstractDTO<Statut, String, StatutDTO> implements Comparable<StatutDTO> {

    public static final StatutDTO PROVISION = new StatutDTO("80-Récurrente");

    @Null
    private String code;


    private StatutDTO() {
        super();
    }

    public StatutDTO(@NotNull String code) {
        this();
        this.code = code;
    }

    @Null
    public String getCode() {
        return code;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @Null
    @Override
    public String getIdentity() {
        return code;
    }


    @NotNull
    @Override
    public Statut toEntity() {
        return new Statut(code);
    }

    @NotNull
    @Override
    public StatutDTO fromEntity(@NotNull Statut entity) {
        return new StatutDTO(entity.getCode());
    }

    @NotNull
    static public StatutDTO from(@NotNull Statut entity) {
        return new StatutDTO().fromEntity(entity);
    }


    @NotNull
    @Override
    public List<RegleGestion<StatutDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @Override
    public int compareTo(@NotNull StatutDTO o) {
        return code.compareTo(o.getCode());
    }
}
