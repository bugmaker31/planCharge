package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.function.Consumer;
import java.util.function.Function;

public class Objects {

    @NotNull
    public static <T, R> R value(@Null R value, @NotNull R defaultValue) {
        return (value == null) ? defaultValue : value;
    }

    @NotNull
    public static <T, R> R value(@Null T object, @NotNull Function<T, R> fct, @NotNull R defaultValue) {
        return (object == null) ? defaultValue : fct.apply(object);
    }

    @Null
    public static <T, R> R value(@Null T object, @NotNull Function<T, R> fct) {
        return (object == null) ? null : fct.apply(object);
    }

    public static String idInstance(@Null Object object) {
        //noinspection StringConcatenation
        return value(object, o -> o.getClass().getSimpleName() + "@" + o.hashCode(), "null");
    }

    public static <T> void exec(@Null T instance, @NotNull Consumer<T> function) {
        if (instance == null) {
            return;
        }
        function.accept(instance);
    }
}
