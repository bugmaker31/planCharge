package fr.gouv.agriculture.dal.ct.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ClassHasNoToStringMethod")
public abstract class AbstractCalculateur {


    private static boolean estActif = true;

    private boolean estEnCoursDeCalcul = false;


    protected AbstractCalculateur() {
        super();
    }


    public boolean estActif() {
        return estEnCoursDeCalcul || estActif;
    }


    private void desactiver() {
        estActif = false;
    }

    private void activer() {
        estActif = true;
    }


/*
    public abstract void calculer() throws IhmException;
*/

    public void executerSansCalculer(@NotNull Executeur aExecuter) throws IhmException {
        boolean etaitActive = estActif();
        desactiver();

        aExecuter.executer();

        if (etaitActive) {
            activer();
        }
    }

    public void executerPuisCalculer(@NotNull Executeur aExecuter, @NotNull Executeur aCalculer) throws IhmException {

        executerSansCalculer(aExecuter);

        boolean etaitActive = estActif();
        desactiver();
        estEnCoursDeCalcul = true;
        aCalculer.executer();
        estEnCoursDeCalcul = false;
        if (etaitActive) {
            activer();
        }
    }

}
