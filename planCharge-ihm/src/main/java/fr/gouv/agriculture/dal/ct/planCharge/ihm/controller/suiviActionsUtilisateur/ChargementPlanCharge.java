package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.AnnulationActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class ChargementPlanCharge extends ModificationEnMassePlanCharge {

    private PlanChargeBean planChargeBeanPrecedent;

    //@Autowired
    @SuppressWarnings("unused")
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();


    public ChargementPlanCharge(PlanChargeBean planChargeBeanPrecedent) throws SuiviActionsUtilisateurException {
        super();
        try {
            this.planChargeBeanPrecedent = planChargeBeanPrecedent.copier(); // TODO FDA 2017/05 Vraiment besoin de mémoriser tout le plan de charge ?
        } catch (CopieException e) {
            throw new SuiviActionsUtilisateurException("Impossible de mémoriser le plan de charge.", e);
        }
    }


    @Override
    public String getTexte() {
        return "le chargement du plan de charge";
    }


    @Override
    public void annuler() throws AnnulationActionException {
        try {
            PlanChargeBean.copier(planChargeBeanPrecedent, planChargeBean);
        } catch (CopieException e) {
            throw new AnnulationActionException("Impossible d'annuler l'action {}.", getTexte(), e);
        }
    }

    @Override
    public void retablir() throws RetablissementActionException {
        throw new NotImplementedException();
    }

}
