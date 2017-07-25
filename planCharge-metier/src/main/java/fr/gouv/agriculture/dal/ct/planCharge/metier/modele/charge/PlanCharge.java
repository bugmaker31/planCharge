package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.MetierException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff.Difference;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff.StatutDifference;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Referentiels;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.ViolationRegleGestion;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
public class PlanCharge extends AbstractEntity<LocalDate, PlanCharge> {

    @NotNull
    private LocalDate dateEtat;
    @NotNull
    private Referentiels referentiels;
    @NotNull
    private Planifications planifications;


    public PlanCharge(@NotNull LocalDate dateEtat, @NotNull Referentiels referentiels, @NotNull Planifications planifications) {
        super();
        this.dateEtat = dateEtat;
        this.referentiels = referentiels;
        this.planifications = planifications;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @NotNull
    @Override
    public LocalDate getIdentity() {
        return dateEtat;
    }

    @NotNull
    public LocalDate getDateEtat() {
        return dateEtat;
    }

    @NotNull
    public Referentiels getReferentiels() {
        return referentiels;
    }

    @NotNull
    public Planifications getPlanifications() {
        return planifications;
    }


    @NotNull
    @Override
    protected Set<RegleGestion<PlanCharge>> getReglesGestion() {
        Set<RegleGestion<PlanCharge>> regles = new HashSet<>();
        // TODO FDA 2017/07 Spécifier les règles de gestion.
        return regles;
    }

    @NotNull
    @Override
    public List<ViolationRegleGestion> controlerReglesGestion() throws MetierException {
        List<ViolationRegleGestion> violations = super.controlerReglesGestion();
        violations.addAll(referentiels.controlerReglesGestion());
        violations.addAll(planifications.controlerReglesGestion());
        return violations;
    }

    @NotNull
    public Set<Difference> differencesAvec(@NotNull PlanCharge autrePlan) throws TacheSansPlanificationException {

        // On fusionne les 2 ensembles de tâches, pour supprimer les doublons,
        // en faisant bien attention de commencer par ses données plutôt que les
        // données de "l'autre", afin de conserver nos données plutôt que celles de "l'autre" :
        Set<Tache> taches = new HashSet<>();
        taches.addAll(this.getPlanifications().taches());
        taches.addAll(autrePlan.getPlanifications().taches());

        // On précise/qualifie la différence, pour chaque tâche :
        Set<Difference> differences = new HashSet<>();
        for (Tache tache : taches) {

            Double autreCharge = autrePlan.getPlanifications().chargePlanifiee(tache);
            Double cetteCharge = this.getPlanifications().chargePlanifiee(tache);

            Tache autreTache = autrePlan.getPlanifications().tache(tache.getId());
            assert autreTache != null;
            Tache cetteTache = this.getPlanifications().tache(tache.getId());
            assert cetteTache != null;

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
