package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanification;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.AnnulationActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge.ModificationPlanification;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public final class ModificationPlanificationCharge<TB extends PlanificationTacheBean, T extends Double> extends ModificationPlanification {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationPlanificationCharge.class);

    @NotNull
    private TB tacheBean;
    @NotNull
    private BiConsumer<TB, T> fctRestauration;
    @Null
    private T valeurAvant;
    @Null
    private T valeurApres;
    @NotNull
    private String libelleAction;
    @NotNull
    private Function<T, String> toLibelle;

    @NotNull
    public TB getTacheBean() {
        return tacheBean;
    }


    public ModificationPlanificationCharge(@NotNull TB tacheBean,
                                           @NotNull BiConsumer<TB, T> fctRestauration,
                                           @Null T valeurAvant, @Null T valeurApres,
                                           @NotNull String libelleAction, @NotNull Function<T, String> toLibelle
    ) {
        super();
        this.tacheBean = tacheBean;
        this.fctRestauration = fctRestauration;
        this.valeurAvant = valeurAvant;
        this.valeurApres = valeurApres;
        this.libelleAction = libelleAction;
        this.toLibelle = toLibelle;
    }


    @Override
    public String getTexte() {
        return "la modification " + libelleAction + " de la tâche n°" + tacheBean.noTache() + texteModificationValeurs();
    }

    @NotNull
    private String texteModificationValeurs() {
        return " (de " + abbreviation(valeurAvant) + " en " + abbreviation(valeurApres) + ")";
    }

    @NotNull
    private String abbreviation(@Null T t) {
        if (t == null) {
            return "";
        }
        String s = toLibelle.apply(t);
        return StringUtils.isEmpty(s) ? "<vide>" : ("<< " + StringUtils.abbreviate(s, 50) + " >>");
    }


    @SuppressWarnings("RedundantThrowsDeclaration")
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
