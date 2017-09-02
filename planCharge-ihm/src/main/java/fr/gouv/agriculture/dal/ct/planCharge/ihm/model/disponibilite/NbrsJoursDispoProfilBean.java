package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursDispoProfilBean extends AbstractCalendrierProfilBean<AbstractDTO, NbrsJoursDispoProfilBean, FloatProperty> {

    // Fields :


    // Constructors :

    public NbrsJoursDispoProfilBean(@NotNull ProfilBean profilBean) {
        super(profilBean);
    }

    public NbrsJoursDispoProfilBean(@NotNull ProfilBean profilBean, @NotNull Map<LocalDate, FloatProperty> calendrier) {
        super(profilBean, calendrier);
    }


    // Getters/Setters:

    // Implementation of AbstractBean :

    // Utilitie's methods:

}
