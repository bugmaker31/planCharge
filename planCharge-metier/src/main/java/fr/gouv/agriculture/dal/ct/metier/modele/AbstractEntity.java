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


    public boolean equals(@Null T obj) {
        //noinspection SimplifiableIfStatement
        if (obj == null) return false;
        return getIdentity().equals(obj.getIdentity());
    }




    // Pour faciliter le débogage uniquement.
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + getIdentity();
    }
}
