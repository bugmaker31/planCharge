package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;

import javax.validation.constraints.NotNull;

// RG_Ref_RessHum_TrigrammeObligatoire Trigramme obligatoire https://github.com/bugmaker31/planCharge/wiki/R%C3%A8gles-de-gestion-:-R%C3%A9f%C3%A9rentiels-:-Ressources-humaines#rg_ref_resshum_trigrammeobligatoire-trigramme-obligatoire
public final class RGRefRessHumTrigrammeObligatoire extends RegleGestion<RessourceHumaineDTO> {

    public static final RGRefRessHumTrigrammeObligatoire INSTANCE = new RGRefRessHumTrigrammeObligatoire();

    private RGRefRessHumTrigrammeObligatoire() {
        super("RG_Ref_RessHum_TrigrammeObligatoire", "Trigramme obligatoire", ressHum -> "Un trigramme est requis.");
    }

    @Override
    public boolean estApplicable(@NotNull RessourceHumaineDTO ressHum) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull RessourceHumaineDTO ressHum) {
        return ressHum.getTrigramme() != null;
    }
}
