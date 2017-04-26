package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by frederic.danna on 20/03/2017.
 */
public class Planifications {

    public static final int NBR_SEMAINES_PLANIFIEES = 12;

    @NotNull
    private Map<Tache, Map<LocalDate, Double>> matrice;

    public Planifications() {
        this.matrice = new HashMap<>();
    }

    public Planifications(@NotNull Map<Tache, Map<LocalDate, Double>> matrice) {
        this.matrice = matrice;
    }

    @NotNull
    public Set<Tache> taches() {
        return matrice.keySet();
    }

    @NotNull
    public double chargePlanifiee(@NotNull Tache tache) throws TacheSansPlanificationException {
        double chargePlanifiee;

        Map<LocalDate, Double> ligne = planification(tache);
        if (ligne == null) {
            chargePlanifiee = 0.0;
        } else {
            chargePlanifiee = 0.0;
            for (LocalDate date : ligne.keySet()) {
                Double charge = ligne.get(date);
                chargePlanifiee += charge;
            }
        }

        return chargePlanifiee;
    }

    @NotNull
    public Tache tache(@NotNull int idTache) {
        // TODO FDA 2017/03 Trouver une meilleure façon de faire (pas codé à la main, plus rapide, etc.)
        for (Tache tache : matrice.keySet()) {
            if (tache.getId() == idTache) {
                return tache;
            }
        }
        return null;
    }

    @NotNull
    public Map<LocalDate, Double> planification(@NotNull Tache tache) throws TacheSansPlanificationException {
        if (!matrice.containsKey(tache)) {
            throw new TacheSansPlanificationException(tache);
        }
        return matrice.get(tache);
    }

    public void ajouter(@NotNull Tache tache, LocalDate dateEtat) {
        Map<LocalDate, Double> ligne = new HashMap<>(NBR_SEMAINES_PLANIFIEES);
        LocalDate dateSemaine = dateEtat;
        for (int noSemaine = 1; noSemaine <= NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            ligne.put(dateSemaine, 0.0);
            dateSemaine = dateSemaine.plusDays(7);
        }
        matrice.put(tache, ligne);
    }
}
