package fr.gouv.agriculture.dal.ct.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.Executeur;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ClassHasNoToStringMethod")
public abstract class Calculateur {


    private static boolean estActive = true;

    private static boolean estEnCoursDeCalcul = false;


    protected Calculateur() {
        super();
    }


    public static boolean estActive() {
        return estEnCoursDeCalcul || estActive;
    }


    private static void desactiver() {
        estActive = false;
    }

    private static void activer() {
        estActive = true;
    }


/*
    public abstract void calculer() throws IhmException;
*/

    public static void executerSansCalculer(@NotNull Executeur aExecuter) throws IhmException {
        boolean etaitActive = estActive();
        desactiver();

        aExecuter.executer();

        if (etaitActive) {
            activer();
        }
    }

    public static void executerPuisCalculer(@NotNull Executeur aExecuter, @NotNull Executeur aCalculer) throws IhmException {

        executerSansCalculer(aExecuter);

        boolean etaitActive = estActive();
        desactiver();
        estEnCoursDeCalcul = true;
        aCalculer.executer();
        estEnCoursDeCalcul = false;
        if (etaitActive) {
            activer();
        }
    }

}
