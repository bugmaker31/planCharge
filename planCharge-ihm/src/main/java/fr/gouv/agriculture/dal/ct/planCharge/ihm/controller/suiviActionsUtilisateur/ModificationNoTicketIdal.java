package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 28/05/2017.
 *
 * @author frederic.danna
 */
public class ModificationNoTicketIdal<TB extends TacheBean> extends ModificationTache<TB> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModificationNoTicketIdal.class);


    public ModificationNoTicketIdal(@NotNull TB tacheBeanAvant, @NotNull TB tacheBean) throws SuiviActionsUtilisateurException {
        super(tacheBeanAvant, tacheBean);
    }


    @NotNull
    @Override
    public String getTexte() {
        return "modification du n° de ticket IDAL de la tâche n° " + getTacheBeanAvant().noTache();
    }

}
