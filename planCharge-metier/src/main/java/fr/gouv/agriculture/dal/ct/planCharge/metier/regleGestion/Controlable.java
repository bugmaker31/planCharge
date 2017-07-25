package fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.planCharge.metier.MetierException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

public interface Controlable<T extends AbstractEntity> {

/*
    @NotNull
    Set<RegleGestion<T>> getReglesGestion();

    @NotNull
    List<ViolationRegleGestion<T>> controlerReglesGestion() throws MetierException;
*/

    @NotNull
    List<ViolationRegleGestion> controlerReglesGestion() throws MetierException;

}
