package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.tache;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;

import javax.validation.constraints.NotNull;

public final class RGTacheProfilObligatoire extends RegleGestion<TacheDTO> {

    public static final RGTacheProfilObligatoire INSTANCE = new RGTacheProfilObligatoire();

    private RGTacheProfilObligatoire() {
        super("RG_Tache_ProfilObligatoire_TODO", "Profil non défini", tache -> "Le profil est requis pour la tâche " + tache.noTache() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull TacheDTO tache) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull TacheDTO tache) {
        return tache.getProfil() != null;
    }
}
