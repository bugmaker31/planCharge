package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursDispoParProfilBean extends AbstractCalendrierParProfilBean<AbstractDTO, NbrsJoursDispoParProfilBean, FloatProperty> {

    // Fields :


    // Constructors :

    public NbrsJoursDispoParProfilBean(@NotNull ProfilBean profilBean) {
        super(profilBean);
    }

    public NbrsJoursDispoParProfilBean(@NotNull ProfilBean profilBean, @NotNull Map<LocalDate, FloatProperty> calendrier) {
        super(profilBean, calendrier);
    }


    // Getters/Setters:

    // Implementation of AbstractBean :

    // Utilitie's methods:

}
