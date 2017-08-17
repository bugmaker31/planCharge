package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.DoubleProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursDAbsenceBean extends AbstractDisponibilitesRessourceBean<AbstractDTO, NbrsJoursDAbsenceBean, DoubleProperty> {


    // Fields:

    // Constructors:

    public NbrsJoursDAbsenceBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, DoubleProperty> calendrier) {
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
    public NbrsJoursDAbsenceBean fromDto(@NotNull AbstractDTO dto) throws BeanException {
        return super.fromDto(dto);
    }
}
