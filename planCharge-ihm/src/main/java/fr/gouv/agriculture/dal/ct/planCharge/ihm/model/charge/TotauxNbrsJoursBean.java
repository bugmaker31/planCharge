package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class TotauxNbrsJoursBean extends AbstractCalendrierBean<AbstractDTO, TotauxNbrsJoursBean, FloatProperty> {


    // Fields :


    // Constructors :


    public TotauxNbrsJoursBean() /*throws BeanException */{
        super(new TreeMap<>()); // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.
    }

    public TotauxNbrsJoursBean(@NotNull Map<LocalDate, FloatProperty> calendrier) /*throws BeanException */{
        super(calendrier);
    }

    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
