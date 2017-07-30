package fr.gouv.agriculture.dal.ct.metier.regleGestion;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.MetierException;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface Controlable<T extends AbstractDTO> {

/*
    @NotNull
    Set<RegleGestion<T>> getReglesGestion();

    @NotNull
    List<ViolationRegleGestion<T>> controlerReglesGestion() throws MetierException;
*/

    @NotNull
    List<ViolationRegleGestion> controlerReglesGestion() throws MetierException;

}
