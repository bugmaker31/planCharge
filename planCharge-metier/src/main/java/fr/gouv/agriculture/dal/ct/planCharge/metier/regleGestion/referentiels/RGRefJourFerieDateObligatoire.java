package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;

import javax.validation.constraints.NotNull;

public final class RGRefJourFerieDateObligatoire extends RegleGestion<JourFerieDTO> {

    public static final RGRefJourFerieDateObligatoire INSTANCE = new RGRefJourFerieDateObligatoire();

    private RGRefJourFerieDateObligatoire() {
        super("RG_Ref_JourFerie_DateObligatoire", "Date non définie", jourFerie -> "La date du jour férié est requise.");
    }

    @Override
    public boolean estApplicable(@NotNull JourFerieDTO jf) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull JourFerieDTO jf) {
        return jf.getDate() != null;
    }
}
