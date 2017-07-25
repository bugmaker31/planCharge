package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;

import javax.validation.constraints.NotNull;
import java.util.Set;

public final class RGRefJourFerieUniciteJour extends RegleGestion<JourFerie> {

    public static final RGRefJourFerieUniciteJour INSTANCE = new RGRefJourFerieUniciteJour();

    private RGRefJourFerieUniciteJour() {
        super("RG_Ref_JourFerie_UniciteJour", "Unicité du jour", "Le jour férié $J apparaît plus d'une fois.");
    }

    @Override
    public boolean estValide(@NotNull JourFerie jf) {
        assert jf.getDate() != null;
        Set<JourFerie> joursFeries = getPlanCharge().getReferentiels().getJoursFeries();
        return joursFeries.parallelStream()
                .filter(jourFerie -> jourFerie.getDate() == null)
                .filter(jourFerie -> jourFerie.getDate().equals(jf.getDate()))
                .count() <= 1;
    }
}
