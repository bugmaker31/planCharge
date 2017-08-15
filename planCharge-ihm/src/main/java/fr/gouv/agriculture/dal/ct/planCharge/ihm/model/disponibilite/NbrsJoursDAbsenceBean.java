package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.IntegerProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class NbrsJoursDAbsenceBean extends AbstractDisponibilitesRessourceBean<AbstractDTO, NbrsJoursDAbsenceBean, IntegerProperty> {


    // Fields :

    // Constructors :

    public NbrsJoursDAbsenceBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, IntegerProperty> calendrier) {
        super(ressourceHumaineBean, calendrier);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


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
