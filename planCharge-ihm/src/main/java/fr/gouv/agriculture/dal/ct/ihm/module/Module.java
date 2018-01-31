package fr.gouv.agriculture.dal.ct.ihm.module;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ActionUtilisateur;
import javafx.scene.Node;

import javax.validation.constraints.NotNull;

public interface Module {

    String getTitre();

    Node getView();

    @NotNull
    ActionUtilisateur actionUtilisateurAffichageModule(@NotNull Module modulePrecedent);

}
