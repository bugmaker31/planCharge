package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.DoubleProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursDispoCTBean extends AbstractDisponibilitesRessourceBean<AbstractDTO, NbrsJoursDispoCTBean, DoubleProperty> {


    // Fields :


    // Constructors :

    public NbrsJoursDispoCTBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, DoubleProperty> calendrier) {
        super(ressourceHumaineBean, calendrier);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
