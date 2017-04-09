package fr.gouv.agriculture.dal.ct.planCharge.metier.modele;

import java.io.Serializable;

/**
 * Created by frederic.danna on 03/04/2017.
 */
public abstract class AbstractEntity<I extends Serializable> {

    abstract public I getIdentity();
}
