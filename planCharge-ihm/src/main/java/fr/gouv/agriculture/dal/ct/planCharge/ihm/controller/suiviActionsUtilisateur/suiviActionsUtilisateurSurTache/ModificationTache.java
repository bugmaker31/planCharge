package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurTache;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge.ModificationPlanification;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.AnnulationActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public final class ModificationTache<TB extends TacheBean, T> extends ModificationPlanification {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationTache.class);

    @NotNull
    private TB tacheBean; /*TODO FDA 2018/02 Ne mémoriser que les infos nécessaires, pour économiser la mémoire.*/
    @NotNull
    private BiConsumer<TB, T> fctRestauration;
    @Null
    private T valeurAvant;
    @Null
    private T valeurApres;
    @Null
    private final String libelleAction;

    @NotNull
    public TB getTacheBean() {
        return tacheBean;
    }

    @Null
    T getValeurAvant() {
        return valeurAvant;
    }

    @Null
    T getValeurApres() {
        return valeurApres;
    }


    public ModificationTache(@NotNull TB tacheBean, @NotNull BiConsumer<TB, T> fctRestauration, @Null T valeurAvant, @Null T valeurApres, @NotNull String libelleAction) {
        super();
        this.tacheBean = tacheBean;
        this.fctRestauration = fctRestauration;
        this.valeurAvant = valeurAvant;
        this.valeurApres = valeurApres;
        this.libelleAction = libelleAction;
    }


    @Override
    public void annuler() throws AnnulationActionException {
/*
        try {
*/
        fctRestauration.accept(tacheBean, valeurAvant);
        LOGGER.debug("Action annulée : {}", getTexte());
/*
        } catch (CopieException e) {
            throw new AnnulationActionException("Impossible d'annuler l'action.", e);
        }
*/
    }

    @Override
    public void retablir() throws RetablissementActionException {
/*
        try {
*/
            fctRestauration.accept(tacheBean, valeurApres);
            LOGGER.debug("Action rétablie : {}", getTexte());
/*
        } catch (CopieException e) {
            throw new RetablissementActionException("Impossible de rétablir l'action.", e);
        }
*/
    }

}
