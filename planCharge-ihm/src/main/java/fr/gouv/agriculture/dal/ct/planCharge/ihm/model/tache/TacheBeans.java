package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public final class TacheBeans {

    private static final Logger LOGGER = LoggerFactory.getLogger(TacheBeans.class);


    @NotNull
    public static List<TacheDTO> toDTO(@NotNull List<TacheBean> tacheBeanList) {
        List<TacheDTO> tacheDTOList = new ArrayList<>();
        for (TacheBean tacheBean : tacheBeanList) {
            TacheDTO tacheDTO;
/*
            try {
*/
                tacheDTO = tacheBean.toDto();
/*
            } catch (BeanException e) {
                LOGGER.error("Impossible de transformer la tâche de bean en DTO.", e);
            }
*/
            tacheDTOList.add(tacheDTO);
        }
        return tacheDTOList;
    }
}
