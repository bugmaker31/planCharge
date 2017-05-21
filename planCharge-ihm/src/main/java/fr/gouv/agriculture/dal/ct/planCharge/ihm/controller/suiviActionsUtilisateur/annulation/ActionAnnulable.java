package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation;

/**
 * Created by frederic.danna on 21/05/2017.
 *
 * @author frederic.danna
 */
public interface ActionAnnulable {

    void annuler() throws AnnulationActionException;

}
