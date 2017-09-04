package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParRessourceEtProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class PctagesDispoRsrcProfilBean extends AbstractCalendrierParRessourceEtProfilBean<RessourceHumaineBean, AbstractDTO, PctagesDispoRsrcProfilBean, PercentageProperty> {

    // Fields :


    // Constructors :

    public PctagesDispoRsrcProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull ProfilBean profilBean, @NotNull Map<LocalDate, PercentageProperty> calendrier) throws BeanException {
        super(ressourceHumaineBean, profilBean, calendrier);
    }

    public PctagesDispoRsrcProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull ProfilBean profilBean) throws BeanException {
        super(ressourceHumaineBean, profilBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
