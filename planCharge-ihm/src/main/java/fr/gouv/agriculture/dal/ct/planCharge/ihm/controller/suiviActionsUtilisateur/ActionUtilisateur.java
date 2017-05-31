package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.repetition.ActionRepetable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;

/**
 * Une action faite par l'utilisateur.
 *
 * <p>Chaque classe fille doit choisir d'implémenter {@link ActionAnnulable}, {@link ActionRetablissable} et/ou {@link ActionRepetable}.</p>
 *
 * <p>Created by frederic.danna on 24/05/2017.</p>
 *
 * @author frederic.danna
 */
public interface ActionUtilisateur {

    boolean estAnnulable();
    boolean estRetablissable();
    boolean estRepetable();

    /**
     * Texte affiché dans les libellés des menus "Annuler", "Rétablir" et "Répéter.
     * <p>Gagne à être surchargé par chaque sous-classe.</p>
     * @return Le texte décrivant cette action.
     */
    String getTexte();


}
