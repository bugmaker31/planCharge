package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public final class RGRefRessHumUniciteTrigramme extends RegleGestion<RessourceHumaineDTO> {

    public static final RGRefRessHumUniciteTrigramme INSTANCE = new RGRefRessHumUniciteTrigramme();

    private RGRefRessHumUniciteTrigramme() {
        super("RG_Ref_RessHum_UniciteTrigramme", "Trigramme en double", ressHum -> "Le trigramme '" + ressHum.getTrigramme() + "' apparaît plus d'une fois.");
    }

    @Override
    public boolean estApplicable(@NotNull RessourceHumaineDTO ressHum) {
        return ressHum.getTrigramme() != null; // Applicable seulement si on connaît le trigramme.
    }

    @Override
    public boolean estValide(@NotNull RessourceHumaineDTO ressHum) {
        assert ressHum.getTrigramme() != null;
        Collection<RessourceHumaineDTO> ressourcesHumaines = getPlanChargeDTO().getReferentiels().getRessourcesHumaines();
        assert ressourcesHumaines != null; // TODO FDA 2017/07 Citer la RG qui n'est pas respectée.
        return ressourcesHumaines.parallelStream()
                .filter(rh -> {
                    assert rh.getTrigramme() != null; // TODO FDA 2017/07 Citer la RG qui n'est pas respectée.
                    return rh.getTrigramme().equals(ressHum.getTrigramme());
                })
                .count() <= 1;
    }
}
