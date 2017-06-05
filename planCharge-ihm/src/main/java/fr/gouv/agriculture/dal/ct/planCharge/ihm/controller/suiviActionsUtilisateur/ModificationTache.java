package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.AnnulationActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public abstract class ModificationTache<TB extends TacheBean> extends ModificationPlanification {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationTache.class);


    @NotNull
    private TB tacheBean;
    @NotNull
    private TB tacheBeanAvant;
    @Null
    private TB sauvegardeTacheBean;

    @NotNull
    TB getTacheBean() {
        return tacheBean;
    }
    @NotNull
    TB getTacheBeanAvant() {
        return tacheBeanAvant;
    }


    ModificationTache(@NotNull TB tacheBeanAvant, @NotNull TB tacheBean) throws SuiviActionsUtilisateurException {
        this.tacheBean = tacheBean;
        this.tacheBeanAvant = tacheBeanAvant;
        this.sauvegardeTacheBean = null;
    }


    @NotNull
    @Override
    public String getTexte() {
        return "modification de la tâche n° " + tacheBeanAvant.noTache();
    }


    @Override
    public void annuler() throws AnnulationActionException {
        try {
            // TODO FDA 2017/05 Trouver mieux que "caster".
            sauvegardeTacheBean = (TB) tacheBean.copier();
            tacheBean.copier(tacheBeanAvant);
            LOGGER.debug("Action annulée : {}", getTexte());
        } catch (CopieException e) {
            throw new AnnulationActionException("Impossible d'annuler l'action.", e);
        }
    }

    @Override
    public void retablir() throws RetablissementActionException {
        try {
            // TODO FDA 2017/05 Trouver mieux que "caster".
            tacheBean.copier((TB) sauvegardeTacheBean);
            LOGGER.debug("Action rétablie : {}", getTexte());
        } catch (CopieException e) {
            throw new RetablissementActionException("Impossible de rétablir l'action.", e);
        }
    }

}
