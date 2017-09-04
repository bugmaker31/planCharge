package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursParRessourceBean extends AbstractCalendrierParRessourceBean<RessourceBean, AbstractDTO, NbrsJoursParRessourceBean, FloatProperty> {


    // Fields :


    // Constructors :

    public NbrsJoursParRessourceBean(@NotNull RessourceBean ressourceBean, @NotNull Map<LocalDate, FloatProperty> calendrier) throws BeanException {
        super(ressourceBean, calendrier);
    }

    public NbrsJoursParRessourceBean(@NotNull RessourceBean ressourceBean) throws BeanException {
        super(ressourceBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
