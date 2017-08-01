package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;

import javax.validation.constraints.NotNull;

// RG_Ref_RessHum_NomObligatoire Nom obligatoire https://github.com/bugmaker31/planCharge/wiki/R%C3%A8gles-de-gestion-:-R%C3%A9f%C3%A9rentiels-:-Ressources-humaines#rg_ref_resshum_nomobligatoire-nom-obligatoire
public final class RGRefRessHumNomObligatoire extends RegleGestion<RessourceHumaineDTO> {

    public static final RGRefRessHumNomObligatoire INSTANCE = new RGRefRessHumNomObligatoire();

    private RGRefRessHumNomObligatoire() {
        super("RG_Ref_RessHum_NomObligatoire", "Nom obligatoire", ressHum -> "Un nom est requis pour " + ressHum.getTrigramme() + ".");
    }

    @Override
    public boolean estApplicable(@NotNull RessourceHumaineDTO ressHum) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull RessourceHumaineDTO ressHum) {
        return ressHum.getNom() != null;
    }
}
