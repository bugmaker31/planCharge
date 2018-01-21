package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheImportanceObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheImportanceObligatoire INSTANCE = new RGTacheImportanceObligatoire();

    private RGTacheImportanceObligatoire() {
        super("RG_Tache_ImportanceObligatoire_TODO", "Importance non définie", tache -> "L'importance est requise pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getImportance() != null;
    }
}
