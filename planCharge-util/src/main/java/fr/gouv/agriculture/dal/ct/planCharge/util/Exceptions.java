package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 19/05/2017.
 *
 * @author frederic.danna
 */
public class Exceptions {

/*
    @NotNull
    public static Throwable causeOriginelle(@NotNull Throwable e) {
        return ((e.getCause() == null) ? e : causeOriginelle(e.getCause()));
    }
*/

    @NotNull
    public static String causes(@NotNull Throwable e, @NotNull String separateur) {
        if (e.getCause() == null) {
            return e.getLocalizedMessage();
        } else {
            //noinspection StringConcatenationMissingWhitespace
            return e.getLocalizedMessage() + separateur + causes(e.getCause(), separateur);
        }
    }

    public static String causes(@NotNull Throwable e) {
        //noinspection HardcodedLineSeparator
        return causes(e, "\nCause : ");
    }
}
