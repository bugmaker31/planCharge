package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;

import javax.validation.constraints.NotNull;

public final class RGRefRessHumSocieteObligatoire extends RegleGestion<RessourceHumaineDTO> {

    public static final RGRefRessHumSocieteObligatoire INSTANCE = new RGRefRessHumSocieteObligatoire();

    private RGRefRessHumSocieteObligatoire() {
        super("RG_Ref_RessHum_SocieteObligatoire", "Société non définie", ressHum -> "La société est requise pour "+ ressHum.getTrigramme()+".");
    }

    @Override
    public boolean estApplicable(@NotNull RessourceHumaineDTO ressHum) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull RessourceHumaineDTO ressHum) {
        return ressHum.getSociete() != null;
    }
}
