package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.JourFerieDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

// RG_Ref_RessHum_UniciteTrigramme Unicité du trigramme https://github.com/bugmaker31/planCharge/wiki/R%C3%A8gles-de-gestion-:-R%C3%A9f%C3%A9rentiels-:-Ressources-humaines#rg_ref_resshum_unicitetrigramme-unicit%C3%A9-du-trigramme
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
                .filter(rh -> rh.getTrigramme() != null)
                .filter(rh -> {
                    assert rh.getTrigramme() != null; // Par construction, vu le filtre précédent.
                    return rh.getTrigramme().equals(ressHum.getTrigramme());
                })
                .count() <= 1;
    }
}
