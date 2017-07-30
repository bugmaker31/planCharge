package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public final class RGRefJourFerieUniciteJour extends RegleGestion<JourFerieDTO> {

    public static final RGRefJourFerieUniciteJour INSTANCE = new RGRefJourFerieUniciteJour();

    private RGRefJourFerieUniciteJour() {
        super("RG_Ref_JourFerie_UniciteJour", "Unicité du jour",
                jourFerie -> "Le jour férié " + jourFerie.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE)
                        + (jourFerie.getDescription() == null ? "" : (" (" + jourFerie.getDescription() + ")"))
                        + " apparaît plus d'une fois."
        );
    }

    @Override
    public boolean estApplicable(@NotNull JourFerieDTO jf) {
        return jf.getDate() != null; // Applicable seulement si on connaît la date.
    }

    @Override
    public boolean estValide(@NotNull JourFerieDTO jf) {
        assert jf.getDate() != null;
        Collection<JourFerieDTO> joursFeries = getPlanChargeDTO().getReferentiels().getJoursFeries();
        return joursFeries.parallelStream()
                .filter(jourFerie -> jourFerie.getDate() != null)
                .filter(jourFerie -> jourFerie.getDate().equals(jf.getDate()))
                .count() <= 1;
    }
}
