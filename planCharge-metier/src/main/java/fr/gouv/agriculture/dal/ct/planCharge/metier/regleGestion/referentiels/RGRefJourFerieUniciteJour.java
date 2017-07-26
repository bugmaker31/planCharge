package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;

public final class RGRefJourFerieUniciteJour extends RegleGestion<JourFerie> {

    public static final RGRefJourFerieUniciteJour INSTANCE = new RGRefJourFerieUniciteJour();

    private RGRefJourFerieUniciteJour() {
        super("RG_Ref_JourFerie_UniciteJour", "Unicité du jour", "Le jour férié $J apparaît plus d'une fois.");
    }

    @Override
    public boolean estApplicable(@NotNull JourFerie jf) {
        return jf.getDate() != null; // Applicable seulement si on connaît la date.
    }

    @Override
    public boolean estValide(@NotNull JourFerie jf) {
        assert jf.getDate() != null;
        Collection<JourFerie> joursFeries = getPlanCharge().getReferentiels().getJoursFeries();
        return joursFeries.parallelStream()
                .filter(jourFerie -> jourFerie.getDate() != null)
                .filter(jourFerie -> jourFerie.getDate().equals(jf.getDate()))
                .count() <= 1;
    }
}
