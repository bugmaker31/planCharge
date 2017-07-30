package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.charge;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;

import javax.validation.constraints.NotNull;

public final class RGChargePlanReferentielsObligatoires extends RegleGestion<PlanChargeDTO> {

    public static final RGChargePlanReferentielsObligatoires INSTANCE = new RGChargePlanReferentielsObligatoires();

    private RGChargePlanReferentielsObligatoires() {
        super("RG_Charge_Plan_ReferentielsObligatoires", "Référentiels non définis", planCharge -> "Les référentiels sont requis.");
    }

    @Override
    public boolean estApplicable(@NotNull PlanChargeDTO planCharge) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull PlanChargeDTO planCharge) {
        //noinspection ConstantConditions
        return planCharge.getReferentiels() != null;
    }
}
