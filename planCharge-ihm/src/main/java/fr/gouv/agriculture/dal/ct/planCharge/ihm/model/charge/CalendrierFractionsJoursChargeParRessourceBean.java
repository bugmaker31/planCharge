package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class CalendrierFractionsJoursChargeParRessourceBean extends AbstractCalendrierParRessourceBean<RessourceBean, AbstractDTO, CalendrierFractionsJoursChargeParRessourceBean, FloatProperty> {


    // Fields :


    // Constructors :

    public CalendrierFractionsJoursChargeParRessourceBean(@NotNull RessourceBean ressourceBean, @NotNull Map<LocalDate, FloatProperty> calendrier) throws BeanException {
        super(ressourceBean, calendrier);
    }

    public CalendrierFractionsJoursChargeParRessourceBean(@NotNull RessourceBean ressourceBean) throws BeanException {
        super(ressourceBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
