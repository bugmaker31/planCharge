package fr.gouv.agriculture.dal.ct.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ClassHasNoToStringMethod")
public class AbstractCalculateur {

    private boolean estActif = false;

    public boolean estActif() {
        return estActif;
    }

    private void desactiver() {
        this.estActif = false;
    }

    private void activer() {
        this.estActif = true;
    }

    public void execSansCalculer(@NotNull ExecuteurCalculateur aExecuter) throws IhmException {
        boolean etaitActive = estActif();
        desactiver();
        {
            aExecuter.executer();
        }
        if (etaitActive) {
            activer();
        }
    }

    public void execPuisCalculer(@NotNull ExecuteurCalculateur aExecuter, @NotNull ExecuteurCalculateur calculer) throws IhmException {
        boolean etaitActive = estActif();
        activer();
        {
            execSansCalculer(aExecuter);

            calculer.executer();
        }
        if (!etaitActive) {
            desactiver();
        }
    }

}
