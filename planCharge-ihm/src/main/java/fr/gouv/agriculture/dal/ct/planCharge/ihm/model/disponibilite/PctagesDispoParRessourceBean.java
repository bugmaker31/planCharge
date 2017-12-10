package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class PctagesDispoParRessourceBean extends AbstractCalendrierParRessourceBean<RessourceHumaineBean, AbstractDTO, PctagesDispoParRessourceBean, PercentageProperty> {


    // Fields :


    // Constructors :

    public PctagesDispoParRessourceBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, PercentageProperty> calendrier) /*throws BeanException*/ {
        super(ressourceHumaineBean, calendrier);
    }

    public PctagesDispoParRessourceBean(@NotNull RessourceHumaineBean ressourceHumaineBean) /*throws BeanException*/ {
        super(ressourceHumaineBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
