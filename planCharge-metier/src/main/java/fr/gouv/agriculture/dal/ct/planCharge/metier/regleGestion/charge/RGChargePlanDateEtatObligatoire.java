package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.charge;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;

import javax.validation.constraints.NotNull;

public final class RGChargePlanDateEtatObligatoire extends RegleGestion<PlanChargeDTO> {

    public static final RGChargePlanDateEtatObligatoire INSTANCE = new RGChargePlanDateEtatObligatoire();

    private RGChargePlanDateEtatObligatoire() {
        super("RG_Charge_Plan_DateObligatoire", "Date d'état non définie", planCharge -> "La date d'état est requise.");
    }

    @Override
    public boolean estApplicable(@NotNull PlanChargeDTO planCharge) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull PlanChargeDTO planCharge) {
        return planCharge.getDateEtat() != null;
    }
}
