package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheEcheanceObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheEcheanceObligatoire INSTANCE = new RGTacheEcheanceObligatoire();

    private RGTacheEcheanceObligatoire() {
        super("RG_Tache_EcheanceObligatoire_TODO", "Echeance non définie", tache -> "L'écheance est requise pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getEcheance() != null;
    }
}
