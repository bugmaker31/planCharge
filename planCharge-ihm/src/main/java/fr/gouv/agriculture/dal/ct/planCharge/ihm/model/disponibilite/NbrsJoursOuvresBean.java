package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierBean;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursOuvresBean extends AbstractCalendrierBean<AbstractDTO, NbrsJoursOuvresBean, IntegerProperty> {

    // Fields :

    // Constructors :

    public NbrsJoursOuvresBean() {
        super();
    }

    public NbrsJoursOuvresBean(@NotNull Map<LocalDate, IntegerProperty> calendrier) throws IhmException {
        super(calendrier);
    }

    // Implementation of AbstractBean :

    // Utilitie's methods:
}
