package fr.gouv.agriculture.dal.ct.planCharge.metier.modele;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;

/**
 * Created by frederic.danna on 03/04/2017.
 */
public abstract class AbstractEntity<I extends Serializable> {

    @NotNull
    abstract public I getIdentity();
}
