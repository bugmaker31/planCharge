package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheCategorieObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheCategorieObligatoire INSTANCE = new RGTacheCategorieObligatoire();

    private RGTacheCategorieObligatoire() {
        super("RG_Tache_CategorieObligatoire_TODO", "Catégorie non définie", tache -> "La catégorie est requise pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getCategorie() != null;
    }
}
