package fr.gouv.agriculture.dal.ct.planCharge.util.cloning;

/**
 * Inspiré de {@link Cloneable}.
 *
 * <p>Created by frederic.danna on 21/05/2017.</p>
 *
 * @author frederic.danna
 */
public interface Copiable<T> {

    T copier() throws CopieException;

}
