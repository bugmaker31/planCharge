package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurTache;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.function.BiConsumer;

/**
 * Created by frederic.danna on 28/05/2017.
 *
 * @author frederic.danna
 */
public class ModificationNoTicketIdal<TB extends TacheBean> extends ModificationTache<TB, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationNoTicketIdal.class);


    public ModificationNoTicketIdal(@NotNull TB tacheBean, @Null String valeurAvant, @Null String valeurApres) {
        super(tacheBean, TacheBean::setNoTicketIdal, valeurAvant, valeurApres);
    }


    @NotNull
    @Override
    public String getTexte() {
        return "modification du n° de ticket IDAL de la tâche n° " + getTacheBean().noTache();
    }

}
