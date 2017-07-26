package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ViolationsReglesGestionException extends Exception {

    @NotNull
    private final List<ViolationRegleGestion> violations;

    public ViolationsReglesGestionException(@NotNull String message, @NotNull List<ViolationRegleGestion> violations) {
        super(message);
        this.violations = violations;
    }

    @NotNull
    public List<ViolationRegleGestion> getViolations() {
        return violations;
    }
}
