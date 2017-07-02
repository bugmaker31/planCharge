package fr.gouv.agriculture.dal.ct.planCharge.metier.modele;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by frederic.danna on 03/04/2017.
 */
public abstract class AbstractEntity<I extends Serializable> /* implements Comparable<I> */ {

    @NotNull
    abstract public I getIdentity();


/* TODO FDA 2017/07 Faire fonctionner dans cette calssse, plutîot que dans chaque classe fille.
    @NotNull
    @Override
    public int compareTo(@NotNull AbstractEntity<I> o) {
        return getIdentity().compareTo(o.getIdentity());
    }
*/


    public boolean equals(AbstractEntity<I> obj) {
        return getIdentity().equals(obj.getIdentity());
    }


    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " " + getIdentity();
    }
}
