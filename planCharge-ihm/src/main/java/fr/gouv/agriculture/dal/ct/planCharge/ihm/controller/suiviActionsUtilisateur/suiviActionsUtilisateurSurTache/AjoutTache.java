package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurTache;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge.ModificationPlanification;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
// TODO FDA 2018/03 Tester.
@SuppressWarnings("ClassHasNoToStringMethod")
public class AjoutTache<TB extends TacheBean> extends ModificationPlanification {

    private static final Logger LOGGER = LoggerFactory.getLogger(AjoutTache.class);


    @NotNull
    private TB tacheBean;

    @NotNull
    private final ObservableList<TB> tachesBeans = FXCollections.observableArrayList();


    public AjoutTache(@NotNull TB tacheBean, @NotNull ObservableList<TB> tachesBeans) {
        this.tacheBean = tacheBean;
        this.tachesBeans.setAll(tachesBeans);
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
