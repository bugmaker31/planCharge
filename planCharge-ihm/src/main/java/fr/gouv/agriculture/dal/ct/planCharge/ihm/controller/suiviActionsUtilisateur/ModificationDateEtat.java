package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;

import java.time.LocalDate;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class ModificationDateEtat extends ModificationUnitairePlanCharge {

    private LocalDate dateEtatPrecedente;

    public ModificationDateEtat(LocalDate dateEtatPrecedente) {
        super();
        this.dateEtatPrecedente = dateEtatPrecedente;
    }

    @Override
    public String getTexte() {
        return "la modification de la date d'état (était " + dateEtatPrecedente + ")";
    }

    @Override
    public void annuler() {
        throw new NotImplementedException();
    }

}
