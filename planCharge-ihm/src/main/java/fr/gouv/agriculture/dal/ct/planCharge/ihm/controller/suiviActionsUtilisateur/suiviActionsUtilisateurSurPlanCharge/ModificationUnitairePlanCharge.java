package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings({"ClassWithoutConstructor", "ClassWithoutLogger"})
public abstract class ModificationUnitairePlanCharge<D, T> extends ModificationPlanCharge<D, T> implements ActionAnnulable, ActionRetablissable {
}
