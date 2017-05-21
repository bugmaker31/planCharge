package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class AjoutTache extends ModificationPlanification implements ActionAnnulable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AjoutTache.class);


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
//        LOGGER.debug("Annulation de l'action : {}", getTexte());
        //noinspection SuspiciousMethodCalls
        planChargeBean.getPlanificationsBeans().remove(tacheBean);
        LOGGER.debug("Action annulée : {}", getTexte());
    }
}
