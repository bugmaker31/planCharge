package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursParProfilBean extends AbstractCalendrierParProfilBean<AbstractDTO, NbrsJoursParProfilBean, FloatProperty> {


    // Fields :


    // Constructors :

    public NbrsJoursParProfilBean(@NotNull ProfilBean profilBean, @NotNull Map<LocalDate, FloatProperty> calendrier) throws BeanException {
        super(profilBean, calendrier);
    }

    public NbrsJoursParProfilBean(@NotNull ProfilBean profilBean) throws BeanException {
        super(profilBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
