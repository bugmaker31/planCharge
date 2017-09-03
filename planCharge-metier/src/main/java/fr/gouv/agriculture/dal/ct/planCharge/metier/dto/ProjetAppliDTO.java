package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.ProjetAppli;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collections;
import java.util.List;

/**
 * Created by frederic.danna on 25/03/2017.
 */
public class ProjetAppliDTO extends AbstractDTO<ProjetAppli, String, ProjetAppliDTO> implements Comparable<ProjetAppliDTO> {

    @Null
    private String code;


    public ProjetAppliDTO() {
        super();
    }

    public ProjetAppliDTO(@NotNull String code) {
        this();
        this.code = code;
    }


    @Null
    public String getCode() {
        return code;
    }


    @Null
    @Override
    public String getIdentity() {
        return getCode();
    }


    @NotNull
    @Override
    public ProjetAppli toEntity() {
        assert code != null;
        return new ProjetAppli(code);
    }

    @NotNull
    @Override
    public ProjetAppliDTO fromEntity(@NotNull ProjetAppli entity) {
        return new ProjetAppliDTO(entity.getCode());
    }

    @NotNull
    static public ProjetAppliDTO from(@NotNull ProjetAppli entity) {
        return new ProjetAppliDTO().fromEntity(entity);
    }


    @NotNull
    @Override
    public List<RegleGestion<ProjetAppliDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @Override
    public int compareTo(@NotNull ProjetAppliDTO o) {
        return code.compareTo(o.getCode());
    }


    // pour déboguer, uniquement.
    @Override
    public String toString() {
        return code;
    }

}
