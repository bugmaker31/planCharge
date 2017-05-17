package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import java.util.Stack;
import java.util.stream.Stream;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class SuiviActionsUtilisateur {

    private static final SuiviActionsUtilisateur INSTANCE = new SuiviActionsUtilisateur();

    public static SuiviActionsUtilisateur instance() {
        return INSTANCE;
    }


    private Stack<ActionUtilisateur> actionsUtilisateur;


    private SuiviActionsUtilisateur() {
        this.actionsUtilisateur = new Stack<>();
    }


    // Délégations à #actionsUtilisateur :

    public ActionUtilisateur push(ActionUtilisateur item) {
        return actionsUtilisateur.push(item);
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
