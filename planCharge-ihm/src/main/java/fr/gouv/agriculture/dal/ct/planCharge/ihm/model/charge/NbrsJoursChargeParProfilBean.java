package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursChargeParProfilBean extends AbstractCalendrierParProfilBean<AbstractDTO, NbrsJoursChargeParProfilBean, FloatProperty> {


    // Fields :


    // Constructors :

    public NbrsJoursChargeParProfilBean(@NotNull ProfilBean profilBean, @NotNull Map<LocalDate, FloatProperty> calendrier) {
        super(profilBean, calendrier);
    }

    public NbrsJoursChargeParProfilBean(@NotNull ProfilBean profilBean) {
        super(profilBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
