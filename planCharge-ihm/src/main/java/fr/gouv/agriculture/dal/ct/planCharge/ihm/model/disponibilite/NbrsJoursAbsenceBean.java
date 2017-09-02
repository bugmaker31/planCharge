package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class NbrsJoursAbsenceBean extends AbstractCalendrierParRessourceBean<RessourceHumaineBean, AbstractDTO, NbrsJoursAbsenceBean, FloatProperty> {


    // Fields:

    // Constructors:

    public NbrsJoursAbsenceBean(@NotNull RessourceHumaineBean ressourceHumaineBean) throws BeanException {
        super(ressourceHumaineBean, new TreeMap<>()); // TreeMap juste pour trier afin de faciliter le débogage.
    }

    public NbrsJoursAbsenceBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, FloatProperty> calendrier) throws BeanException {
        super(ressourceHumaineBean, calendrier);
    }


    // Getters/Setters:


    // Methods:


    // Implementation of AbstractBean:


    @NotNull
    @Override
    public AbstractDTO toDto() throws BeanException {
        return super.toDto();
    }

    @NotNull
    @Override
    public NbrsJoursAbsenceBean fromDto(@NotNull AbstractDTO dto) throws BeanException {
        return super.fromDto(dto);
    }
}
