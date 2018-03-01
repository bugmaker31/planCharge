package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurTache;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 01/03/2018.
 *
 * @author frederic.danna
 */
public class ModificationStatutRevision<TB extends TacheBean> extends ModificationTache<TB, StatutRevision> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationStatutRevision.class);


    public ModificationStatutRevision(@NotNull TB tacheBean, @Null StatutRevision valeurAvant, @Null StatutRevision valeurApres) {
        super(tacheBean, TacheBean::setStatutRevision, valeurAvant, valeurApres);
    }


    @NotNull
    @Override
    public String getTexte() {
        return "modification du statut de la révision de la tâche n° " + getTacheBean().noTache();
    }

}
