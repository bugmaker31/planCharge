package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.charge;

import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;

import javax.validation.constraints.NotNull;

public final class RGChargePlanPlanificationsObligatoires extends RegleGestion<PlanChargeDTO> {

    public static final RGChargePlanPlanificationsObligatoires INSTANCE = new RGChargePlanPlanificationsObligatoires();

    private RGChargePlanPlanificationsObligatoires() {
        super("RG_Charge_Plan_PlanificationsObligatoires", "Planifications non définies", planCharge -> "Les planifications sont requises.");
    }

    @Override
    public boolean estApplicable(@NotNull PlanChargeDTO planCharge) {
        return true; // Toujours applicable.
    }

    @Override
    public boolean estValide(@NotNull PlanChargeDTO planCharge) {
        //noinspection ConstantConditions
        return planCharge.getPlanifications() != null;
    }
}
