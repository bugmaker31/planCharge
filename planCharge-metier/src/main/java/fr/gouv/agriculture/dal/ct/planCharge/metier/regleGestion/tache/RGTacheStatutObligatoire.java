package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheStatutObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheStatutObligatoire INSTANCE = new RGTacheStatutObligatoire();

    private RGTacheStatutObligatoire() {
        super("RG_Tache_StatutObligatoire_TODO", "Statut non défini", tache -> "Le statut est requis pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getStatut() != null;
    }
}
