package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurTache;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.ValidateurRevision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 01/03/2018.
 *
 * @author frederic.danna
 */
public class ModificationValidateurRevision<TB extends TacheBean> extends ModificationTache<TB, ValidateurRevision> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationValidateurRevision.class);


    public ModificationValidateurRevision(@NotNull TB tacheBean, @Null ValidateurRevision valeurAvant, @Null ValidateurRevision valeurApres) {
        super(tacheBean, TacheBean::setValidateurRevision, valeurAvant, valeurApres);
    }


    @NotNull
    @Override
    public String getTexte() {
        return "modification du validateur de la révision de la tâche n° " + getTacheBean().noTache();
    }

}
