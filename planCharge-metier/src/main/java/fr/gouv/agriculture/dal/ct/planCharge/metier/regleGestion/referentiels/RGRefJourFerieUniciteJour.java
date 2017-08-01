package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

// RG_Ref_JourFerie_UniciteJour Unicité du jour https://github.com/bugmaker31/planCharge/wiki/R%C3%A8gles-de-gestion-:-R%C3%A9f%C3%A9rentiels-:-Jours-f%C3%A9ri%C3%A9s#rg_ref_jourferie_unicitejour-unicit%C3%A9-du-jour
public final class RGRefJourFerieUniciteJour extends RegleGestion<JourFerieDTO> {

    public static final RGRefJourFerieUniciteJour INSTANCE = new RGRefJourFerieUniciteJour();

    private RGRefJourFerieUniciteJour() {
        super("RG_Ref_JourFerie_UniciteJour", "Jour en double",
                jourFerie -> "Le jour férié "
                        + (jourFerie.getDate() == null ? "" : jourFerie.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
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
        assert joursFeries != null; // TODO FDA 2017/07 Citer la RG qui n'est pas respectée.
        return joursFeries.parallelStream()
                .filter(jourFerie -> {
                    assert jourFerie.getDate() != null; // TODO FDA 2017/07 Citer la RG qui n'est pas respectée.
                    return jourFerie.getDate().equals(jf.getDate());
                })
                .count() <= 1;
    }
}
