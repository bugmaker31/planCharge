package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;

public final class RGRefJourFerieDateObligatoire extends RegleGestion<JourFerie> {

    public static final RGRefJourFerieDateObligatoire INSTANCE = new RGRefJourFerieDateObligatoire();

    private RGRefJourFerieDateObligatoire() {
        super("RG_REF_JF001_DateObligatoire", "Date obligatoire", "Le jour férié est requis.");
    }

    @Override
    public boolean estApplicable(@NotNull JourFerie jf) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull JourFerie jf) {
        return jf.getDate() != null;
    }
}
