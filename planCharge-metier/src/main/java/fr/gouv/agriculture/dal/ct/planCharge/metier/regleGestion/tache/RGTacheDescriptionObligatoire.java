package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheDescriptionObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheDescriptionObligatoire INSTANCE = new RGTacheDescriptionObligatoire();

    private RGTacheDescriptionObligatoire() {
        super("RG_Tache_DescriptionObligatoire_TODO", "Description non définie", tache -> "La description est requise pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getDescription() != null;
    }
}
