package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.DoubleProperty;
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

public class NbrsJoursDispoMinAgriBean extends AbstractDisponibilitesRessourceBean<AbstractDTO, NbrsJoursDispoMinAgriBean, DoubleProperty> {


    // Fields :


    // Constructors :

    public NbrsJoursDispoMinAgriBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, DoubleProperty> calendrier) {
        super(ressourceHumaineBean, calendrier);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
