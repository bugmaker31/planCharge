package fr.gouv.agriculture.dal.ct.planCharge.metier.modele;

import fr.gouv.agriculture.dal.ct.planCharge.metier.MetierException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.Controlable;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.ViolationRegleGestion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by frederic.danna on 03/04/2017.
 */
public abstract class AbstractEntity<I extends Serializable, T extends AbstractEntity<I, T>> implements Controlable<T> {


    protected AbstractEntity() {
        super();
    }


    /* TODO FDA 2017/07 Faire fonctionner cette méthode dans cette classe, plutôt que la descendre dans chaque classe fille.
        @NotNull
        @Override
        public int compareTo(@NotNull AbstractEntity<I> o) {
            return getIdentity().compareTo(o.getIdentity());
        }
    */

    @NotNull
    abstract public I getIdentity();

    public boolean equals(@Null T obj) {
        if (obj == null) return false;
        return getIdentity().equals(obj.getIdentity());
    }


    @Override
    @NotNull
    public List<ViolationRegleGestion<T>> controlerReglesGestion() throws MetierException {
        List<ViolationRegleGestion<T>> violations = new ArrayList<>();

        for (RegleGestion<T> regleGestion : getReglesGestion()) {
            if (!regleGestion.estValide((T) this)) {
                violations.add(new ViolationRegleGestion(regleGestion, this));
            }
        }

        return violations;
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + getIdentity();
    }
}
