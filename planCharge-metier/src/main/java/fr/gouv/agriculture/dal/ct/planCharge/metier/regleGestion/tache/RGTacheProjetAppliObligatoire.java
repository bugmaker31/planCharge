package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheProjetAppliObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheProjetAppliObligatoire INSTANCE = new RGTacheProjetAppliObligatoire();

    private RGTacheProjetAppliObligatoire() {
        super("RG_Tache_ProjetAppliObligatoire_TODO", "Projet/Appli non défini", tache -> "Le projet/appli est requis pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getProjetAppli() != null;
    }
}
