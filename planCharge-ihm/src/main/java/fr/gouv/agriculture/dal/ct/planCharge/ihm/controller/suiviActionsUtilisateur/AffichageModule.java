package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.AnnulationActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Objects;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public abstract class AffichageModule extends ActionUtilisateurBase implements ActionAnnulable, ActionRetablissable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AffichageModule.class);

    //    @Autowired
    @NotNull
    private final PlanChargeIhm ihm = PlanChargeIhm.instance();

    //    @Autowired
    @NotNull
    private final ApplicationController applicationController = ApplicationController.instance();


    @Null
    private final Module modulePrecedent;


    public AffichageModule(@Null Module modulePrecedent) {
        this.modulePrecedent = modulePrecedent;
    }


    @Override
    public String getTexte() {
        return "affichage du module \"" + getModule().getTitre() + "\"";
    }


    @NotNull
    abstract Module getModule();


    @SuppressWarnings("MethodWithMultipleReturnPoints")
    @Override
    public void annuler() throws AnnulationActionException {
        try {
            if (modulePrecedent == null) {
                // TODO FDA 2017/05 Quel module afficher, lorsqu'on annule l'affichage du tout 1er module affiché ? La page d'accueil ?
                LOGGER.warn("Impossible d'annuler l'affichage du tout premier module (quel autre module afficher ?).");
                return;
            }
            ihm.getApplicationController().activerModule(modulePrecedent);
        } catch (ControllerException e) {
            throw new AnnulationActionException("Impossible d'annuler l'action '" + getTexte() + "'.", e);
        }
    }

    @Override
    public void retablir() throws RetablissementActionException {
        try {
            ihm.getApplicationController().activerModule(getModule());
        } catch (ControllerException e) {
            throw new RetablissementActionException("Impossible de rétablir l'action '" + getTexte() + "'.", e);
        }
    }

}
