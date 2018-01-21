package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheRessourceObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheRessourceObligatoire INSTANCE = new RGTacheRessourceObligatoire();

    private RGTacheRessourceObligatoire() {
        super("RG_Tache_RessourceObligatoire_TODO", "Ressource non définie", tache -> "La ressource est requise pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getRessource() != null;
    }
}
