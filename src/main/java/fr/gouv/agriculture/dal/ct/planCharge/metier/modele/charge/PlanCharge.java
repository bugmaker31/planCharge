package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff.Difference;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff.StatutDifference;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
public class PlanCharge {

    private LocalDate dateEtat;
    private Planification planification;

    public PlanCharge(LocalDate dateEtat, Planification planification) {
        this.dateEtat = dateEtat;
        this.planification = planification;
    }

    public LocalDate getDateEtat() {
        return dateEtat;
    }

    public Planification getPlanification() {
        return planification;
    }

    public Set<Difference> differencesAvec(PlanCharge autrePlan) {
        Set<Difference> differences = new HashSet<>();

        // On fusionne les 2 ensembles de tâches, pour supprimer les doublons,
        // en faisant bien attention de commencer par ses données plutôt que les
        // données de "l'autre", afin de conserver nos données plutôt que celles de "l'autre" :
        Set<Tache> taches = new HashSet<>();
        taches.addAll(this.getPlanification().taches());
        taches.addAll(autrePlan.getPlanification().taches());

        // On précise/qualifie la différence, pour chaque tâche :
        for (Tache tache : taches) {

            Double autreCharge = autrePlan.getPlanification().chargePlanifiee(tache);
            Double cetteCharge = this.getPlanification().chargePlanifiee(tache);

            Tache autreTache = autrePlan.getPlanification().tache(tache.getId());
            Tache cetteTache = this.getPlanification().tache(tache.getId());

            if (autrePlan.getPlanification().taches().contains(tache) && !this.getPlanification().taches().contains(tache)) {
                differences.add(new Difference(tache, StatutDifference.TacheRetiree, autreCharge, cetteCharge, autreTache.getEcheance(), null));
                continue;
            }

            LocalDate cetteEcheance = cetteTache.getEcheance();

            if (!autrePlan.getPlanification().taches().contains(tache) && this.getPlanification().taches().contains(tache)) {
                if (this.getPlanification().chargePlanifiee(tache) > 0) {
                    differences.add(new Difference(tache, StatutDifference.TachePlanifiee, autreCharge, cetteCharge, null, cetteEcheance));
                } else {
                    differences.add(new Difference(tache, StatutDifference.TacheAjoutee, autreCharge, cetteCharge, null, cetteEcheance));
                }
                continue;
            }

            LocalDate autreEcheance = autreTache.getEcheance();

            assert autrePlan.getPlanification().taches().contains(tache) && this.getPlanification().taches().contains(tache);
            if (autrePlan.getPlanification().chargePlanifiee(tache) > 0) {
                if (this.getPlanification().chargePlanifiee(tache) > 0) {
                    differences.add(new Difference(tache, StatutDifference.TacheMaintenue, autreCharge, cetteCharge, autreEcheance, cetteEcheance));
                } else {
                    differences.add(new Difference(tache, StatutDifference.TacheDeplanifiee, autreCharge, cetteCharge, autreEcheance, cetteEcheance));
                }
            } else {
                differences.add(new Difference(tache, StatutDifference.TachePlanifiee, autreCharge, cetteCharge, autreEcheance, cetteEcheance));
            }
        }

        return differences;
    }
}
