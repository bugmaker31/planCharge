package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursDispoCTBean extends AbstractDisponibilitesRessourceBean<AbstractDTO, NbrsJoursDispoCTBean, FloatProperty> {


    // Fields :


    // Constructors :

    public NbrsJoursDispoCTBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, FloatProperty> calendrier) {
        super(ressourceHumaineBean, calendrier);
    }

    public NbrsJoursDispoCTBean(@NotNull RessourceHumaineBean ressourceHumaineBean) {
        super(ressourceHumaineBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
