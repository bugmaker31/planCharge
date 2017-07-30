package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;

import javax.validation.constraints.NotNull;

public final class RGRefRessHumPrenomObligatoire extends RegleGestion<RessourceHumaineDTO> {

    public static final RGRefRessHumPrenomObligatoire INSTANCE = new RGRefRessHumPrenomObligatoire();

    private RGRefRessHumPrenomObligatoire() {
        super("RG_Ref_RessHum_PrenomObligatoire", "Prénom non défini", ressHum -> "Le prénom est requis pour '" + ressHum.getTrigramme() + "'.");
    }

    @Override
    public boolean estApplicable(@NotNull RessourceHumaineDTO ressHum) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull RessourceHumaineDTO ressHum) {
        return ressHum.getPrenom() != null;
    }
}
