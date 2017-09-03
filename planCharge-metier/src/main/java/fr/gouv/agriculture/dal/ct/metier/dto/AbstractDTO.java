package fr.gouv.agriculture.dal.ct.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.MetierException;
import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.Controlable;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDTO<E extends AbstractEntity, I extends Serializable, T extends AbstractDTO<E, I, T>>
        implements Controlable<T>, Comparable<T> {

    @NotNull
    public abstract I getIdentity();


    @NotNull
    public abstract E toEntity() throws DTOException;

    @NotNull
    public abstract T fromEntity(@NotNull E entity) throws DTOException;


    // Controlable<T>

    @NotNull
    @Override
    public List<ViolationRegleGestion> controlerReglesGestion() throws MetierException {
        List<ViolationRegleGestion> violations = new ArrayList<>();

        for (RegleGestion<T> regleGestion : getReglesGestion()) {

            if (!regleGestion.estApplicable((T) this)) {
                continue;
            }

            if (!regleGestion.estValide((T) this)) {
                violations.add(new ViolationRegleGestion(regleGestion, this));
            }
        }

        return violations;
    }

    /**
     * @return La liste des {@link RegleGestion règles de gestion}, dans l'ordre où elles doivent être vérifiées.
     */
    @NotNull
    protected abstract List<RegleGestion<T>> getReglesGestion();


    // 'final' pour éviter que les classe filles ne surchargent (mal) cette méthode.
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractDTO)) return false;
        //noinspection rawtypes,QuestionableName
        AbstractDTO other = (AbstractDTO) o;
        return getIdentity().equals(other.getIdentity());
    }

    // 'final' pour éviter que les classe filles ne surchargent (mal) cette méthode.
    @Override
    public final int hashCode() {
        return getIdentity().hashCode();
    }


    @Override
    public String toString() {
        return String.valueOf(getIdentity());
    }
}
