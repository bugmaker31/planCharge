package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;

import javax.validation.constraints.NotNull;

// RG_Ref_JourFerie_DateObligatoire Date obligatoire https://github.com/bugmaker31/planCharge/wiki/R%C3%A8gles-de-gestion-%3A-R%C3%A9f%C3%A9rentiels-%3A-Jours-f%C3%A9ri%C3%A9s#rg_ref_jourferie_dateobligatoire-date-obligatoire
public final class RGRefJourFerieDateObligatoire extends RegleGestion<JourFerieDTO> {

    public static final RGRefJourFerieDateObligatoire INSTANCE = new RGRefJourFerieDateObligatoire();

    private RGRefJourFerieDateObligatoire() {
        super("RG_Ref_JourFerie_DateObligatoire", "Date obligatoire", jourFerie -> "La date du jour férié est requise.");
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
