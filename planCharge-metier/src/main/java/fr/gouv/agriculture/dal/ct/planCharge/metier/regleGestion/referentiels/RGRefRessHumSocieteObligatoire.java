package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;

import javax.validation.constraints.NotNull;

// RG_Ref_RessHum_SocieteObligatoire Société obligatoire https://github.com/bugmaker31/planCharge/wiki/R%C3%A8gles-de-gestion-:-R%C3%A9f%C3%A9rentiels-:-Ressources-humaines#rg_ref_resshum_societeobligatoire-soci%C3%A9t%C3%A9-obligatoire
public final class RGRefRessHumSocieteObligatoire extends RegleGestion<RessourceHumaineDTO> {

    public static final RGRefRessHumSocieteObligatoire INSTANCE = new RGRefRessHumSocieteObligatoire();

    private RGRefRessHumSocieteObligatoire() {
        super("RG_Ref_RessHum_SocieteObligatoire", "Société obligatoire", ressHum -> "La société est requise pour "+ ressHum.getTrigramme()+".");
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
