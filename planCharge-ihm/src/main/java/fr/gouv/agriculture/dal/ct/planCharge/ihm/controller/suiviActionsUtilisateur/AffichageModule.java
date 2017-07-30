package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.AnnulationActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public abstract class AffichageModule extends ActionUtilisateurBase implements ActionAnnulable, ActionRetablissable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AffichageModule.class);

    //    @Autowired
    @NotNull
    private final ApplicationController applicationController = ApplicationController.instance();


    @Null
    private final ApplicationController.NomModule nomModulePrecedent;


    public AffichageModule(@Null ApplicationController.NomModule nomModulePrecedent) {
        this.nomModulePrecedent = nomModulePrecedent;
    }


    @Override
    public String getTexte() {
        return "affichage du module \"" + getNomModule().getTexte() + "\"";
    }


    @NotNull
    abstract ApplicationController.NomModule getNomModule();


    @SuppressWarnings("MethodWithMultipleReturnPoints")
    @Override
    public void annuler() throws AnnulationActionException {
        try {
            if (nomModulePrecedent == null) {
                // TODO FDA 2017/05 Quel module afficher, lorsqu'on annule l'affichage du tout 1er module affiché ? La page d'accueil ?
                LOGGER.warn("Impossible d'annuler l'affichage du tout premier module (quel autre module afficher ?).");
                return;
            }
            if (nomModulePrecedent == ApplicationController.NomModule.JOURS_FERIES) {
                applicationController.activerModuleJoursFeries();
                return;
            }
            if (nomModulePrecedent == ApplicationController.NomModule.DISPONIBILITES) {
                applicationController.activerModuleDisponibilites();
                return;
            }
            if (nomModulePrecedent == ApplicationController.NomModule.TACHES) {
                applicationController.activerModuleTaches();
                return;
            }
            if (nomModulePrecedent == ApplicationController.NomModule.CHARGES) {
                applicationController.activerModuleCharges();
                return;
            }
            throw new AnnulationActionException("Module non géré : '" + nomModulePrecedent.getTexte() + "'.");
        } catch (IhmException e) {
            throw new AnnulationActionException("Impossible d'annuler l'action '" + getTexte() + "'.", e);
        }
    }

    @Override
    public void retablir() throws RetablissementActionException {
        ApplicationController appCtrl = ApplicationController.instance();
        try {
            if (getNomModule() == ApplicationController.NomModule.JOURS_FERIES) {
                appCtrl.activerModuleJoursFeries();
                return;
            }
            if (getNomModule() == ApplicationController.NomModule.DISPONIBILITES) {
                appCtrl.activerModuleDisponibilites();
                return;
            }
            if (getNomModule() == ApplicationController.NomModule.TACHES) {
                appCtrl.activerModuleTaches();
                return;
            }
            if (getNomModule() == ApplicationController.NomModule.CHARGES) {
                appCtrl.activerModuleCharges();
                return;
            }
            throw new RetablissementActionException("Module non géré : '" + getNomModule().getTexte() + "'.");
        } catch (IhmException e) {
            throw new RetablissementActionException("Impossible d'annuler l'action '" + getTexte() + "'.", e);
        }
    }
}
