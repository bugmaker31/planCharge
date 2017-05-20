package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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

    private Menu menuEditer;
    private MenuItem menuAnnuler;
    private SeparatorMenuItem separateurMenusAnnuler;
    private MenuItem menuRetablir;
    private SeparatorMenuItem separateurMenusRetablir;
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


    public void initialiser(Menu menuEditer,
                            MenuItem menuAnnuler, Menu sousMenuAnnuler, SeparatorMenuItem separateurMenusAnnuler,
                            MenuItem menuRetablir, Menu sousMenuRetablir, SeparatorMenuItem separateurMenusRetablir,
                            MenuItem menuRepeter) {
        this.menuEditer = menuEditer;

        this.menuAnnuler = menuAnnuler;
        this.sousMenuAnnuler = sousMenuAnnuler;
        this.separateurMenusAnnuler = separateurMenusAnnuler;
        //
        this.menuRetablir = menuRetablir;
        this.sousMenuRetablir = sousMenuRetablir;
        this.separateurMenusRetablir = separateurMenusRetablir;
        //
        this.menuRepeter = menuRepeter;

        this.texteMenuAnnuler = menuAnnuler.getText();
        this.texteMenuRetablir = menuRetablir.getText();
        this.texteMenuRepeter = menuRepeter.getText();
    }

    public boolean auDebut() {
        return indexActionCourante == 0;
    }

    public boolean aLaFin() {
        return indexActionCourante == (actionsUtilisateur.size() - 1);
    }

    @Null
    public ActionUtilisateur actionCourante() throws IhmException {
        if (indexActionCourante == -1) {
//            throw new IhmException("Pas positionné sur une action (aucune action faite par l'utilisateur, encore ?).");
            return null;
        }
        assert (indexActionCourante >= 0) && (indexActionCourante < actionsUtilisateur.size());
        return actionsUtilisateur.get(indexActionCourante);
    }

    public ActionUtilisateur actionSuivante() throws IhmException {
        if (indexActionCourante >= actionsUtilisateur.size()) {
            throw new IhmException("Impossible, déjà positionné après la toute dernière action (if any).");
        }
        indexActionCourante++;
        majMenus();
        return actionCourante();
    }

    public ActionUtilisateur actionPrecedente() throws IhmException {
        if (indexActionCourante == -1) {
            throw new IhmException("Impossible, déjà positionné avant la toute première action (if any).");
        }
        indexActionCourante--;
        majMenus();
        return actionCourante();
    }

    private void majMenus() throws IhmException {
        majMenuAnnuler();
        majMenuRetablir();
        // TODO FDA 2017/05 Coder.
//        majMenuRepeter();
    }

    private void majMenuAnnuler() throws IhmException {

        boolean rienAAnnuler = indexActionCourante < 0;
        boolean plusDUneAnnulationPossible = indexActionCourante >= 1;

        menuAnnuler.setDisable(rienAAnnuler);

        if (rienAAnnuler) {
            menuAnnuler.setText(texteMenuAnnuler);
            return;
        }

        menuAnnuler.setText(texteMenuAnnuler + " " + actionCourante().getTexte());

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

    private void majMenuRetablir() throws IhmException {
        boolean rienARetablir = (indexActionCourante == -1) || (indexActionCourante == (actionsUtilisateur.size() - 1));
        boolean plusDUnRetablissementPossible = indexActionCourante <= (actionsUtilisateur.size() - 2);

        menuRetablir.setDisable(rienARetablir);

        if (rienARetablir) {
            menuRetablir.setText(texteMenuRetablir);
            return;
        }

        menuRetablir.setText(texteMenuRetablir + " " + actionCourante().getTexte());

        // Ajout des sous-menus, 1 pour chacune des 10 dernières actions rétablissables :
        sousMenuRetablir.setVisible(plusDUnRetablissementPossible);
//        separateurMenusRetablir.setVisible(plusDUnRetablissementPossible);
        if (plusDUnRetablissementPossible) {
            List<MenuItem> menusRetablir = new ArrayList<>();
            for (int i = indexActionCourante + 1; i < actionsUtilisateur.size(); i++) {
                ActionUtilisateur actionUtilisateur = actionsUtilisateur.get(i);
                MenuItem menuRetablirAction = new MenuItem(i + ") " + actionUtilisateur.getTexte());
                menusRetablir.add(menuRetablirAction);
            }
            sousMenuRetablir.getItems().setAll(menusRetablir);
        }
    }

    // Délégations à #actionsUtilisateur :

    public void historiser(ActionUtilisateur item) throws IhmException {
        actionsUtilisateur.push(item);
        indexActionCourante++;
        LOGGER.debug("++ {} : {}", item.toString(), item.getTexte());
        majMenus();
    }

}
