package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurTache;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge.ModificationPlanification;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class AjoutTache<TB extends TacheBean> extends ModificationPlanification<PlanChargeBean, TB> implements ActionAnnulable, ActionRetablissable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AjoutTache.class);


    @NotNull
    private TB tacheBean;

    @NotNull
    private final ObservableList<TB> tachesBeans;


    public AjoutTache(@NotNull TB tacheBean, @NotNull ObservableList<TB> tachesBeans) {
        this.tacheBean = tacheBean;
        this.tachesBeans = tachesBeans;
    }


    @NotNull
    @Override
    public String getTexte() {
        return "ajout de la tâche n° " + tacheBean.noTache();
    }


    @Override
    public void annuler() {
//        LOGGER.debug("Annulation de l'action : {}", getTexte());
        //noinspection SuspiciousMethodCalls
        tachesBeans.remove(tacheBean);
        LOGGER.debug("Action annulée : {}", getTexte());
    }

    @Override
    public void retablir() throws RetablissementActionException{
//        LOGGER.debug("Rétablissement de l'action : {}", getTexte());
        tachesBeans.add(tacheBean);
        LOGGER.debug("Action rétablie : {}", getTexte());
    }

}
