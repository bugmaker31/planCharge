package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class AjoutTache extends ModificationPlanification implements ActionAnnulable {

    private TacheBean tacheBean;

    //@Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    public AjoutTache(TacheBean tacheBean) {
        this.tacheBean = tacheBean;
    }

    @Override
    public String getTexte() {
        return "ajout de la tâche n° " + tacheBean.noTache();
    }

    @Override
    public void annuler() {
        planChargeBean.getPlanificationsBeans().remove(tacheBean);
    }
}
