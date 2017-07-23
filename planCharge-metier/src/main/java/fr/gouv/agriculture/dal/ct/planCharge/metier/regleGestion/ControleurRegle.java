package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.planCharge.metier.MetierException;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ControleurRegle {

    public static List<ViolationRegleGestion<?>> violations() throws MetierException {
        List<ViolationRegleGestion<?>> violationsRegles = new ArrayList<>();

        List<RegleGestion> reglesGestion = reglesGestion();
        List<Controlable> instancesControlables = instancesControlables();
        for (Controlable instanceControlable : instancesControlables) {
            violationsRegles.addAll(instanceControlable.controlerReglesGestion());
        }

        return violationsRegles;
    }

    @NotNull
    private static List<RegleGestion> reglesGestion() {
        return new ArrayList<>();
    }

    @NotNull
    private static List<Controlable> instancesControlables() {
        return new ArrayList<>();
    }
}
