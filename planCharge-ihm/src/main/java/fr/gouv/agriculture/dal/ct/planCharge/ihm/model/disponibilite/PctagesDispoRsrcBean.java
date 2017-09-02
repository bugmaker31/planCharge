package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class PctagesDispoRsrcBean extends AbstractCalendrierParRessourceBean<RessourceHumaineBean, AbstractDTO, PctagesDispoRsrcBean, PercentageProperty> {


    // Fields :


    // Constructors :

    public PctagesDispoRsrcBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, PercentageProperty> calendrier) throws BeanException {
        super(ressourceHumaineBean, calendrier);
    }

    public PctagesDispoRsrcBean(@NotNull RessourceHumaineBean ressourceHumaineBean) throws BeanException {
        super(ressourceHumaineBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
