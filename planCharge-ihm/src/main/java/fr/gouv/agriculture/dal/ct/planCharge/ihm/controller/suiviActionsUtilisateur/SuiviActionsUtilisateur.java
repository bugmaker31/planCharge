package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class SuiviActionsUtilisateur {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuiviActionsUtilisateur.class);

    private static final SuiviActionsUtilisateur INSTANCE = new SuiviActionsUtilisateur();

    public static SuiviActionsUtilisateur instance() {
        return INSTANCE;
    }


    private Stack<ActionUtilisateur> actionsUtilisateur;

    private int indexActionCourante;

    private MenuItem menuAnnuler;
    private MenuItem menuRetablir;
    private MenuItem menuRepeter;

    private Menu sousMenuAnnuler;
    private Menu sousMenuRetablir;
    private Menu sousMenuRepeter;

    private String texteMenuAnnuler;
    private String texteMenuRetablir;
    private String texteMenuRepeter;


    private SuiviActionsUtilisateur() {
        this.actionsUtilisateur = new Stack<>();
        indexActionCourante = -1;
    }


    @SuppressWarnings({"MethodWithTooManyParameters", "ParameterHidesMemberVariable"})
    public void initialiser(MenuItem menuAnnuler, Menu sousMenuAnnuler,
                            MenuItem menuRetablir, Menu sousMenuRetablir,
                            MenuItem menuRepeter) {
        this.menuAnnuler = menuAnnuler;
        this.sousMenuAnnuler = sousMenuAnnuler;
        //
        this.menuRetablir = menuRetablir;
        this.sousMenuRetablir = sousMenuRetablir;
        //
        this.menuRepeter = menuRepeter;

        this.texteMenuAnnuler = menuAnnuler.getText();
        this.texteMenuRetablir = menuRetablir.getText();
        this.texteMenuRepeter = menuRepeter.getText();
    }

    public boolean estAuDebut() {
        return indexActionCourante == 0;
    }

    public boolean estALaFin() {
        return indexActionCourante == (actionsUtilisateur.size() - 1);
    }

    public boolean estVide() {
        return indexActionCourante == -1;
    }


    public int nbrActionPrecedentes() {
        return estVide() ? 0 : indexActionCourante;
    }

    public int nbrActionSuivantes() {
        return estVide() ? 0 : (actionsUtilisateur.size() - indexActionCourante - 1);
    }

    public int nbrActionAnnulables() {
        return nbrActionPrecedentes();
    }

    public int nbrActionRepetables() {
        return nbrActionSuivantes();
    }


    @Null
    public ActionUtilisateur actionPrecedente() throws SuiviActionsUtilisateurException {
        if (nbrActionPrecedentes() == 0) {
            return null;
        }
        assert indexActionCourante >= 1;
        return actionsUtilisateur.get(indexActionCourante - 1);
    }

    @Null
    public ActionUtilisateur actionCourante() throws SuiviActionsUtilisateurException {
        if (estVide()) {
//            throw new IhmException("Pas positionné sur une action (aucune action faite par l'utilisateur, encore ?).");
            return null;
        }
        assert (indexActionCourante >= 0) && (indexActionCourante < actionsUtilisateur.size());
        return actionsUtilisateur.get(indexActionCourante);
    }

    @Null
    public ActionUtilisateur actionSuivante() throws SuiviActionsUtilisateurException {
        if (nbrActionSuivantes() == 0) {
            return null;
        }
        assert indexActionCourante < actionsUtilisateur.size();
        return actionsUtilisateur.get(indexActionCourante + 1);
    }


    public void passerActionPrecedente() throws SuiviActionsUtilisateurException {
        if (nbrActionPrecedentes() == 0) {
            throw new SuiviActionsUtilisateurException("Impossible, pas d'action précédente (aucune action, ou déjà au début).");
        }
        indexActionCourante--;
        majMenus();
    }

    public void passerActionSuivante() throws SuiviActionsUtilisateurException {
        if (nbrActionSuivantes() == 0) {
            throw new SuiviActionsUtilisateurException("Impossible, pas d'action suivante (aucune action, ou déjà à la fin).");
        }
        indexActionCourante++;
        majMenus();
    }


    private void majMenus() throws SuiviActionsUtilisateurException {
        majMenuAnnuler();
        majMenuRetablir();
        // TODO FDA 2017/05 Coder.
//        majMenuRepeter();
    }

    private void majMenuAnnuler() throws SuiviActionsUtilisateurException {

        boolean rienAAnnuler = (nbrActionAnnulables() == 0);
        boolean plusDUneAnnulationPossible = (nbrActionAnnulables() >= 2);

        menuAnnuler.setDisable(rienAAnnuler);

        if (rienAAnnuler) {
            menuAnnuler.setText(texteMenuAnnuler);
            return;
        }

        ActionUtilisateur actionCourante = actionCourante();
        assert actionCourante != null;

        menuAnnuler.setText(texteMenuAnnuler + " " + actionCourante.getTexte());

        // Ajout des sous-menus, 1 pour chacune des 10 dernières actions annulables :
        sousMenuAnnuler.setVisible(plusDUneAnnulationPossible);
//        separateurMenusAnnuler.setVisible(plusDUneAnnulationPossible);
        if (plusDUneAnnulationPossible) {
            List<MenuItem> menusAnnuler = new ArrayList<>();
            for (int i = indexActionCourante; i >= 0; i--) {
                ActionUtilisateur actionUtilisateur = actionsUtilisateur.get(i);
                MenuItem menuAnnulerAction = new MenuItem(i + ") " + actionUtilisateur.getTexte());
                menusAnnuler.add(menuAnnulerAction);
            }
            sousMenuAnnuler.getItems().setAll(menusAnnuler);
        }
    }

    private void majMenuRetablir() throws SuiviActionsUtilisateurException {
        boolean rienARetablir = (nbrActionRepetables() == 0);
        boolean plusDUnRetablissementPossible = (nbrActionRepetables() >= 2);

        menuRetablir.setDisable(rienARetablir);

        if (rienARetablir) {
            menuRetablir.setText(texteMenuRetablir);
            return;
        }

        ActionUtilisateur actionSuivante = actionSuivante();
        assert actionSuivante != null;

        menuRetablir.setText(texteMenuRetablir + " " + actionSuivante.getTexte());

        // Ajout des sous-menus, 1 pour chacune des 10 dernières actions rétablissables :
        sousMenuRetablir.setVisible(plusDUnRetablissementPossible);
//        separateurMenusRetablir.setVisible(plusDUnRetablissementPossible);
        if (plusDUnRetablissementPossible) {
            List<MenuItem> menusRetablir = new ArrayList<>();
            for (int i = indexActionCourante + 2; i < actionsUtilisateur.size(); i++) {
                ActionUtilisateur actionUtilisateur = actionsUtilisateur.get(i);
                MenuItem menuRetablirAction = new MenuItem(i + ") " + actionUtilisateur.getTexte());
                menusRetablir.add(menuRetablirAction);
            }
            sousMenuRetablir.getItems().setAll(menusRetablir);
        }
    }


    public void historiser(ActionUtilisateur item) throws SuiviActionsUtilisateurException {
        LOGGER.debug("Historisation de l'action '{}' : {}", item.toString(), item.getTexte());

        /*
        Dès que l'utilisateur fait une action, les éventuelles actions Annulable's deviennent obsolètes.
        Il faut donc les supprimer ("oublier").
         */
        oublierActionsNonRetablies();

        actionsUtilisateur.push(item);
        indexActionCourante++;
        LOGGER.debug("Action '{}' historisée : {}", item.toString(), item.getTexte());

        majMenus();
    }

    private void oublierActionsNonRetablies() {
        LOGGER.debug("Suppression des actions devenues obsolètes suite à l'action de l'utilisateur :");
        for (int indexActionAOublier = actionsUtilisateur.size() - 1; indexActionAOublier > indexActionCourante; indexActionAOublier--) {
            ActionUtilisateur actionAOublier = actionsUtilisateur.get(indexActionAOublier);
            LOGGER.debug("- action '{}' : {}", actionAOublier.toString(), actionAOublier.getTexte());
            actionsUtilisateur.removeElementAt(indexActionAOublier);
        }
    }


    /**
     * Annule {@link #actionCourante() l'action courante}.
     *
     * @throws SuiviActionsUtilisateurException
     */
    public void annulerAction() throws SuiviActionsUtilisateurException {
        ActionUtilisateur actionCourante = actionCourante();
        if (actionCourante == null) {
            throw new SuiviActionsUtilisateurException("Impossible d'annuler, pas d'action courante.");
        }
        actionCourante.annuler();
        passerActionPrecedente();
    }

    /**
     * Rétablit {@link #actionCourante() l'action courante}.
     *
     * @throws SuiviActionsUtilisateurException
     */
    public void retablirAction() throws SuiviActionsUtilisateurException {
        ActionUtilisateur actionSuivante = actionSuivante();
        if (actionSuivante == null) {
            throw new SuiviActionsUtilisateurException("Impossible de rétablir, pas d'action suivante.");
        }
        actionSuivante.retablir();
        passerActionSuivante();
    }
}
