package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.Null;
import java.util.Stack;
import java.util.stream.Stream;

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

    private String texteMenuAnnuler;
    private String texteMenuRetablir;
    private String texteMenuRepeter;


    private SuiviActionsUtilisateur() {
        this.actionsUtilisateur = new Stack<>();
        indexActionCourante = -1;
    }


    public void initialiser(MenuItem menuAnnuler, MenuItem menuRetablir, MenuItem menuRepeter) {
        this.menuAnnuler = menuAnnuler;
        this.menuRetablir = menuRetablir;
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

    public void passerActionSuivante() throws IhmException {
        if (indexActionCourante >= actionsUtilisateur.size()) {
            throw new IhmException("Impossible, déjà positionné sur la toute dernière action.");
        }
        indexActionCourante++;
        majMenus();
    }

    public void revenirActionPrecedente() throws IhmException {
        if (indexActionCourante == 0) {
            throw new IhmException("Impossible, déjà positionné sur la toute première action.");
        }
        indexActionCourante--;
        majMenus();
    }

    private void majMenus() throws IhmException {
        majMenuAnnuler();
        // TODO FDA 2017/05 Coder.
//        majMenuRetablir();
//        majMenuRepeter();
    }

    private void majMenuAnnuler() throws IhmException {
        boolean annulerPossible = indexActionCourante > 0;
        menuAnnuler.setDisable(!annulerPossible);
        menuAnnuler.setText(texteMenuAnnuler + (annulerPossible ? " " + actionCourante().getTexte() : ""));
    }

    // Délégations à #actionsUtilisateur :

    public void historiser(ActionUtilisateur item) throws IhmException {
        actionsUtilisateur.push(item);
        indexActionCourante++;
        LOGGER.debug("++ {} : {}", item.toString(), item.getTexte());
        majMenus();
    }

/*
    public ActionUtilisateur pop() {
        return actionsUtilisateur.pop();
    }
*/

/*
    public int search(Object o) {
        return actionsUtilisateur.search(o);
    }
*/

/*
    public int size() {
        return actionsUtilisateur.size();
    }
*/

    public boolean isEmpty() {
        return actionsUtilisateur.isEmpty();
    }

    public Stream<ActionUtilisateur> stream() {
        return actionsUtilisateur.stream();
    }

    public Stream<ActionUtilisateur> parallelStream() {
        return actionsUtilisateur.parallelStream();
    }

}
