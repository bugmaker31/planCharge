package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class PctagesDispoCTBean extends AbstractDisponibilitesRessourceBean<AbstractDTO, PctagesDispoCTBean, PercentageProperty> {


    // Fields :


    // Constructors :

    public PctagesDispoCTBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, PercentageProperty> calendrier) {
        super(ressourceHumaineBean, calendrier);
    }

    public PctagesDispoCTBean(@NotNull RessourceHumaineBean ressourceHumaineBean) {
        super(ressourceHumaineBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
