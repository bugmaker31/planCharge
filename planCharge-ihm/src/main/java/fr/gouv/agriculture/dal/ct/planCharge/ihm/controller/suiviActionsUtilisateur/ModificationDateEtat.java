package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import java.time.LocalDate;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class ModificationDateEtat extends ModificationUnitairePlanCharge {

    private LocalDate dateEtat;

    public ModificationDateEtat(LocalDate dateEtat) {
        super();
        this.dateEtat = dateEtat;
    }

    @Override
    public String getTexte() {
        return "la modification de la date d'état (était " + dateEtat + ")";
    }
}
