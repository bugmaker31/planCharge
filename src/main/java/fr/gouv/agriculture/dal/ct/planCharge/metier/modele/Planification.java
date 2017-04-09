package fr.gouv.agriculture.dal.ct.planCharge.metier.modele;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/**
 * Created by frederic.danna on 20/03/2017.
 */
public class Planification {

    Map<Tache, Map<LocalDate, Double>> matrice;

    public Planification(Map<Tache, Map<LocalDate, Double>> matrice) {
        this.matrice = matrice;
    }

    public Set<Tache> taches() {
        return matrice.keySet();
    }

    public Double chargePlanifiee(Tache tache) {
        Double chargePlanifiee;

        Map<LocalDate, Double> ligne = matrice.get(tache);
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

    public Tache tache(int idTache) {
        // TODO FDA 2017/03 Trouver une meilleure façon de faire (pas codé à la main, plus rapide, etc.)
        for (Tache tache : matrice.keySet()) {
            if (tache.getId() == idTache) {
                return tache;
            }
        }
        return null;
    }
}
