package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;

import javax.validation.constraints.NotNull;

public final class RGRefRessHumNomObligatoire extends RegleGestion<RessourceHumaineDTO> {

    public static final RGRefRessHumNomObligatoire INSTANCE = new RGRefRessHumNomObligatoire();

    private RGRefRessHumNomObligatoire() {
        super("RG_Ref_RessHum_NomObligatoire", "Nom non défini", ressHum -> "Le nom est requis pour " + ressHum.getTrigramme() + ".");
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
