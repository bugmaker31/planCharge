package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff.Difference;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff.StatutDifference;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
public class PlanCharge extends AbstractEntity<LocalDate> {

    @NotNull
    private LocalDate dateEtat;
    @NotNull
    private Planifications planifications;

    public PlanCharge(@NotNull LocalDate dateEtat, @NotNull Planifications planifications) {
        this.dateEtat = dateEtat;
        this.planifications = planifications;
    }

    public PlanCharge(LocalDate dateEtat) {
        this.dateEtat = dateEtat;
        this.planifications = new Planifications();
    }

    @Override
    public LocalDate getIdentity() {
        return dateEtat;
    }

    @NotNull
    public LocalDate getDateEtat() {
        return dateEtat;
    }

    @NotNull
    public Planifications getPlanifications() {
        return planifications;
    }

    @NotNull
    public Set<Difference> differencesAvec(@NotNull PlanCharge autrePlan) throws TacheSansPlanificationException {
        Set<Difference> differences = new HashSet<>();

        // On fusionne les 2 ensembles de tâches, pour supprimer les doublons,
        // en faisant bien attention de commencer par ses données plutôt que les
        // données de "l'autre", afin de conserver nos données plutôt que celles de "l'autre" :
        Set<Tache> taches = new HashSet<>();
        taches.addAll(this.getPlanifications().taches());
        taches.addAll(autrePlan.getPlanifications().taches());

        // On précise/qualifie la différence, pour chaque tâche :
        for (Tache tache : taches) {

            Double autreCharge = autrePlan.getPlanifications().chargePlanifiee(tache);
            Double cetteCharge = this.getPlanifications().chargePlanifiee(tache);

            Tache autreTache = autrePlan.getPlanifications().tache(tache.getId());
            Tache cetteTache = this.getPlanifications().tache(tache.getId());

            if (autrePlan.getPlanifications().taches().contains(tache) && !this.getPlanifications().taches().contains(tache)) {
                differences.add(new Difference(tache, StatutDifference.TacheRetiree, autreCharge, cetteCharge, autreTache.getEcheance(), null));
                continue;
            }

            LocalDate cetteEcheance = cetteTache.getEcheance();

            if (!autrePlan.getPlanifications().taches().contains(tache) && this.getPlanifications().taches().contains(tache)) {
                if (this.getPlanifications().chargePlanifiee(tache) > 0) {
                    differences.add(new Difference(tache, StatutDifference.TachePlanifiee, autreCharge, cetteCharge, null, cetteEcheance));
                } else {
                    differences.add(new Difference(tache, StatutDifference.TacheAjoutee, autreCharge, cetteCharge, null, cetteEcheance));
                }
                continue;
            }

            LocalDate autreEcheance = autreTache.getEcheance();

            assert autrePlan.getPlanifications().taches().contains(tache) && this.getPlanifications().taches().contains(tache);
            if (autrePlan.getPlanifications().chargePlanifiee(tache) > 0) {
                if (this.getPlanifications().chargePlanifiee(tache) > 0) {
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
