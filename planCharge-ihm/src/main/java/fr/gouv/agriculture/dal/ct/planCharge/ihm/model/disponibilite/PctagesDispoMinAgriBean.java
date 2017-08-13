package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PctagesDispoMinAgriBean extends AbstractDisponibilitesRessourceBean<AbstractDTO, PctagesDispoMinAgriBean, PercentageProperty> {


    // Fields :


    // Constructors :

    public PctagesDispoMinAgriBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, PercentageProperty> calendrier) {
        super(ressourceHumaineBean, calendrier);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
