package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
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

public class NbrsJoursOuvresBean extends AbstractDisponibilitesBean<AbstractDTO, NbrsJoursOuvresBean, IntegerProperty> {

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
