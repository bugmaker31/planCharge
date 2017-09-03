package fr.gouv.agriculture.dal.ct.metier.modele;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * Created by frederic.danna on 03/04/2017.
 */
public abstract class AbstractEntity<I extends Serializable, T extends AbstractEntity<I, T>> implements Comparable<T> {


    protected AbstractEntity() {
        super();
    }


    @NotNull
    public abstract I getIdentity();


    // 'final' pour éviter que les classe filles ne surchargent (mal) cette méthode.
    @Override
    public final boolean equals(@Null Object obj) {
        //noinspection SimplifiableIfStatement
        if (obj == null) return false;
        if (!(obj instanceof AbstractEntity)) return false;
        //noinspection unchecked
        T other = (T) obj;
        return getIdentity().equals(other.getIdentity());
    }


    // 'final' pour éviter que les classe filles ne surchargent (mal) cette méthode.
    @Override
    public final int hashCode() {
        return getIdentity().hashCode();
    }


    // Pour faciliter le débogage uniquement.
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + getIdentity();
    }
}
