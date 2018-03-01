package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class ModificationDateEtat extends ModificationUnitairePlanCharge {

    @Null
    private LocalDate dateEtatPrecedente;

    @Null
    private LocalDate dateEtatActuelle;

    //@Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();


    public ModificationDateEtat(@Null LocalDate dateEtatPrecedente) {
        super();
        this.dateEtatPrecedente = dateEtatPrecedente;
        this.dateEtatActuelle = planChargeBean.getDateEtat();
    }


    @Override
    public String getTexte() {
        return "la modification de la date d'état (était " + Objects.value(dateEtatPrecedente, "N/C") + ")";
    }


    @Override
    public void annuler() {
        planChargeBean.setDateEtat(dateEtatPrecedente);
    }

    @Override
    public void retablir() throws RetablissementActionException {
        planChargeBean.setDateEtat(dateEtatActuelle);
    }
}
