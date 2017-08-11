package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceGeneriqueDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class RessourceGeneriqueBean extends RessourceBean<RessourceGeneriqueBean, RessourceGeneriqueDTO> {


    private RessourceGeneriqueBean() {
        super();
    }

    private RessourceGeneriqueBean(@Null String code) {
        super(code);
    }

    @NotNull
    public static RessourceGeneriqueDTO to(@NotNull RessourceGeneriqueBean ressourceGeneriqueBean) {
        return ressourceGeneriqueBean.toDto();
    }

    @NotNull
    public static RessourceGeneriqueBean from(@NotNull RessourceGeneriqueDTO dto) {
        return new RessourceGeneriqueBean().fromDto(dto);
    }

    @NotNull
    @Override
    public RessourceGeneriqueDTO toDto() {
        return new RessourceGeneriqueDTO(getCode());
    }

    @NotNull
    @Override
    public RessourceGeneriqueBean fromDto(@NotNull RessourceGeneriqueDTO dto) {
        return new RessourceGeneriqueBean(dto.getCode());
    }

}
