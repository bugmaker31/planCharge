package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.repetition.ActionRepetable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

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
                            MenuItem menuRepeter, Menu sousMenuRepeter) {
        this.menuAnnuler = menuAnnuler;
        this.sousMenuAnnuler = sousMenuAnnuler;
        //
        this.menuRetablir = menuRetablir;
        this.sousMenuRetablir = sousMenuRetablir;
        //
        this.menuRepeter = menuRepeter;
        this.sousMenuRepeter = sousMenuRepeter;

        this.texteMenuAnnuler = menuAnnuler.getText();
        this.texteMenuRetablir = menuRetablir.getText();
        this.texteMenuRepeter = menuRepeter.getText();
    }


    private boolean estAuDebut() {
        return indexActionCourante == 0;
    }

    private boolean estALaFin() {
        return indexActionCourante == (actionsUtilisateur.size() - 1);
    }

    private boolean estVide() {
        return indexActionCourante == -1;
    }


    private int nbrActionPrecedentes() {
        return estVide() ? 0 : indexActionCourante;
    }

    private int nbrActionSuivantes() {
        return estVide() ? 0 : (actionsUtilisateur.size() - indexActionCourante - 1);
    }


    @NotNull
    private List<ActionAnnulable> actionsAnnulables() {
        List<ActionAnnulable> actionsAnnulables = new ArrayList<>();
        for (int indexAction = indexActionCourante; indexAction >= 0; indexAction--) {
            ActionUtilisateur action = actionsUtilisateur.get(indexAction);
            if (action.estAnnulable()) {
                actionsAnnulables.add((ActionAnnulable) action);
            }
        }
        return actionsAnnulables;
    }

    @NotNull
    private List<ActionRetablissable> actionsRetablissables() {
        List<ActionRetablissable> actionsRetablissables = new ArrayList<>();
        for (int indexAction = indexActionCourante + 1; indexAction < actionsUtilisateur.size(); indexAction++) {
            ActionUtilisateur action = actionsUtilisateur.get(indexAction);
            if (action.estRetablissable()) {
                actionsRetablissables.add((ActionRetablissable) action);
            }
        }
        return actionsRetablissables;
    }

    @NotNull
    private List<ActionRepetable> actionsRepetables() {
        List<ActionRepetable> actionsRepetables = new ArrayList<>();
        for (int indexAction = indexActionCourante + 1; indexAction < actionsUtilisateur.size(); indexAction++) {
            ActionUtilisateur action = actionsUtilisateur.get(indexAction);
            if (action.estRepetable()) {
                actionsRepetables.add((ActionRepetable) action);
            }
        }
        return actionsRepetables;
    }


    @Null
    private ActionUtilisateur actionPrecedente() throws SuiviActionsUtilisateurException {
        if (nbrActionPrecedentes() == 0) {
            return null;
        }
        assert indexActionCourante >= 1;
        return actionsUtilisateur.get(indexActionCourante - 1);
    }

    @Null
    private ActionUtilisateur actionCourante() throws SuiviActionsUtilisateurException {
        if (estVide()) {
//            throw new IhmException("Pas positionné sur une action (aucune action faite par l'utilisateur, encore ?).");
            return null;
        }
        assert (indexActionCourante >= 0) && (indexActionCourante < actionsUtilisateur.size());
        return actionsUtilisateur.get(indexActionCourante);
    }

    @Null
    private ActionUtilisateur actionSuivante() throws SuiviActionsUtilisateurException {
        if (nbrActionSuivantes() == 0) {
            return null;
        }
        assert indexActionCourante < actionsUtilisateur.size();
        return actionsUtilisateur.get(indexActionCourante + 1);
    }


    private void passerActionPrecedente() throws SuiviActionsUtilisateurException {
        if (nbrActionPrecedentes() == 0) {
            throw new SuiviActionsUtilisateurException("Impossible, pas d'action précédente (aucune action, ou déjà au début).");
        }
        indexActionCourante--;
        majMenus();
    }

    private void passerActionSuivante() throws SuiviActionsUtilisateurException {
        if (nbrActionSuivantes() == 0) {
            throw new SuiviActionsUtilisateurException("Impossible, pas d'action suivante (aucune action, ou déjà à la fin).");
        }
        indexActionCourante++;
        majMenus();
    }


    private void majMenus() throws SuiviActionsUtilisateurException {
        majMenuAnnuler();
        majMenuRetablir();
        majMenuRepeter();
    }

    private void majMenuAnnuler() throws SuiviActionsUtilisateurException {
        List<ActionAnnulable> actionsAnnulables = actionsAnnulables();

        boolean rienAAnnuler = (actionsAnnulables.isEmpty());
        boolean plusDUneAnnulationPossible = false; // (actionsAnnulables.size() >= 2);

        menuAnnuler.setDisable(rienAAnnuler);
        sousMenuAnnuler.setDisable(!plusDUneAnnulationPossible);
        sousMenuAnnuler.setVisible(plusDUneAnnulationPossible);

        if (rienAAnnuler) {
            menuAnnuler.setText(texteMenuAnnuler);
            return;
        }

        ActionAnnulable actionAnnulable = actionsAnnulables.get(0);
        assert actionAnnulable != null;

        menuAnnuler.setText(texteMenuAnnuler + " " + actionAnnulable.getTexte());

/* TODO FDA 2017/05 Réactiver, une fois terminé de codé (évo pas priorisée).
        // Ajout des sous-menus, 1 pour chacune des dernières actions annulables :
//        separateurMenusAnnuler.setVisible(plusDUneAnnulationPossible);
        if (plusDUneAnnulationPossible) {
            final int[] indexActionAnnulable = {0};
            List<MenuItem> menusAnnuler = actionsAnnulables().stream()
                    .skip(1) // On ne retient pas la 1ère action, car déjà affichée dans le menu ci-dessus.
                    .map(action -> new MenuItem((++(indexActionAnnulable[0])) + ") " + action.getTexte()))
                    .collect(Collectors.toList());
            sousMenuAnnuler.getItems().setAll(menusAnnuler);
        }
*/
    }

    private void majMenuRetablir() throws SuiviActionsUtilisateurException {
        List<ActionRetablissable> actionRetablissables = actionsRetablissables();

        boolean rienARetablir = (actionRetablissables.isEmpty());
        boolean plusDUnRetablissementPossible = false; //(actionRetablissables.size() >= 2);

        menuRetablir.setDisable(rienARetablir);
        sousMenuRetablir.setDisable(!plusDUnRetablissementPossible);
        sousMenuRetablir.setVisible(plusDUnRetablissementPossible);

        if (rienARetablir) {
            menuRetablir.setText(texteMenuRetablir);
            return;
        }

        ActionRetablissable actionSuivante = actionRetablissables.get(0);
        assert actionSuivante != null;

        menuRetablir.setText(texteMenuRetablir + " " + actionSuivante.getTexte());

/* TODO FDA 2017/05 Réactiver, une fois terminé de codé (évo pas priorisée).
        // Ajout des sous-menus, 1 pour chacune des dernières actions rétablissables :
//        separateurMenusRetablir.setVisible(plusDUnRetablissementPossible);
        if (plusDUnRetablissementPossible) {
            final int[] indexActionRetablissable = {0};
            List<MenuItem> menusRetablir = actionRetablissables.stream()
                    .skip(1) // On ne retient pas la 1ère action, car déjà affichée dans le menu ci-dessus.
                    .map(action -> new MenuItem((++(indexActionRetablissable[0])) + ") " + action.getTexte()))
                    .collect(Collectors.toList());
            sousMenuRetablir.getItems().setAll(menusRetablir);
        }
*/
    }

    private void majMenuRepeter() throws SuiviActionsUtilisateurException {
        List<ActionRepetable> actionsRepetables = actionsRepetables();

        boolean rienARepeter = (actionsRepetables.isEmpty());

        menuRepeter.setDisable(rienARepeter);
        // TODO Supprimer du code (si confirmé).
        sousMenuRepeter.setDisable(false); // Jamais actif, n'aurait pas de sens.
        sousMenuRepeter.setVisible(false); // Jamais affiché, n'aurait pas de sens.

        if (rienARepeter) {
            menuRepeter.setText(texteMenuRepeter);
            return;
        }

        ActionRepetable actionRepetable = actionsRepetables.get(0);
        assert actionRepetable != null;

        menuRepeter.setText(texteMenuRepeter + " " + actionRepetable.getTexte());

/* TODO FDA 2017/05 Réactiver, une fois terminé de codé (évo pas priorisée).
        // Ajout des sous-menus, 1 pour chacune des dernières actions rétablissables :
//        separateurMenusRetablir.setVisible(plusDUnRetablissementPossible);
*/
    }


    public void historiser(ActionUtilisateur item) throws SuiviActionsUtilisateurException {
        LOGGER.debug("Historisation de l'action '{}' : {}", item.toString(), item.getTexte());

        /*
        Dès que l'utilisateur fait une action, les éventuelles actions Annulable's deviennent obsolètes.
        Il faut donc les supprimer ("oublier").
         */
        oublierActionsNonRetablies();

        // TODO FDA 2017/05 Mettre une limite à l'historisation (paramétrable par l'utilisateur), e.g. les 20 dernières actions.

        actionsUtilisateur.push(item);
        indexActionCourante++;

        majMenus();

        LOGGER.debug("Action '{}' historisée : {}", item.toString(), item.getTexte());
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
     * Annule {@link ActionAnnulable l'action annulable} en cours.
     *
     * @throws SuiviActionsUtilisateurException
     */
    public void annulerAction() throws SuiviActionsUtilisateurException {
        List<ActionAnnulable> actionAnnulables = actionsAnnulables();
        if (actionAnnulables.isEmpty()) {
            throw new SuiviActionsUtilisateurException("Impossible d'annuler, pas d'action annulable.");
        }
        ActionAnnulable actionAnnulable = actionAnnulables.get(0);
        actionAnnulable.annuler();
        passerActionPrecedente();
    }

    /**
     * Rétablit {@link ActionRetablissable l'action rétablissable} en cours.
     *
     * @throws SuiviActionsUtilisateurException
     */
    public void retablirAction() throws SuiviActionsUtilisateurException {
        List<ActionRetablissable> actionRetablissables = actionsRetablissables();
        if (actionRetablissables.isEmpty()) {
            throw new SuiviActionsUtilisateurException("Impossible de rétablir, pas d'action rétablissables.");
        }
        ActionRetablissable actionRetablissable = actionRetablissables.get(0);
        actionRetablissable.retablir();
        passerActionSuivante();
    }

    /**
     * Répète {@link ActionRepetable l'action répétable} en cours.
     *
     * @throws SuiviActionsUtilisateurException
     */
    public void repeterAction() throws SuiviActionsUtilisateurException {
        List<ActionRepetable> actionsRepetables = actionsRepetables();
        if (actionsRepetables.isEmpty()) {
            throw new SuiviActionsUtilisateurException("Impossible de répéter, pas d'action répétables.");
        }
        ActionRepetable actionRepetable = actionsRepetables.get(0);
        actionRepetable.repeter();
        passerActionSuivante();
    }
}
