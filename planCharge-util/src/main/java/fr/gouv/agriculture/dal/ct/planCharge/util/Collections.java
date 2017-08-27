package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public class Collections {

    @NotNull
    public static final <T, E extends Exception> T any(@NotNull Collection<T> collection, @NotNull Predicate<T> pred, @NotNull E exception) throws E {

        T elt = any(collection, pred);

        if (elt == null) {
            throw exception;
        } else {
            return elt;
        }
    }

    @Null
    public static final <T> T any(@NotNull Collection<T> collection, @NotNull Predicate<T> pred) {
        Optional<T> eltOpt = collection.parallelStream().filter(pred).findAny();
        if (eltOpt.isPresent()) {
            T elt = eltOpt.get();
            return elt;
        } else {
            return null;
        }
    }

}
