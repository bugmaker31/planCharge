package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class ModificationDateEtat extends ModificationUnitairePlanCharge implements ActionAnnulable, ActionRetablissable {

    private LocalDate dateEtatPrecedente;

    //@Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();


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
        planChargeBean.setDateEtat(dateEtatPrecedente);
    }

    @Override
    public void retablir() throws RetablissementActionException {
        throw new NotImplementedException();
    }
}
