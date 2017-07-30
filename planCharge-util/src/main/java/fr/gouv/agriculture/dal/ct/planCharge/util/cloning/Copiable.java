package fr.gouv.agriculture.dal.ct.planCharge.util.cloning;

import javax.validation.constraints.NotNull;

/**
 * Inspiré de {@link Cloneable}.
 *
 * <p>Created by frederic.danna on 21/05/2017.</p>
 *
 * @author frederic.danna
 */
public interface Copiable<T> {

    @NotNull
    T copier() throws CopieException;

}
