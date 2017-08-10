package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.function.Function;

public class Objects {

    @NotNull
    public static <T, R> R value(@Null R value, @NotNull R defaultValue) {
        return ((value == null) ? defaultValue : value);
    }

    @NotNull
    public static <T, R> R value(@Null T object, @NotNull Function<T, R> fct, @NotNull R defaultValue) {
        return ((object == null) ? defaultValue : fct.apply(object));
    }

    @Null
    public static <T, R> R ifNotNull(@Null T object, @NotNull Function<T, R> function) {
        return ((object == null) ? null : function.apply(object));
    }
}
