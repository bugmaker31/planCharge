package fr.gouv.agriculture.dal.ct.ihm.util;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class ObservableLists {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObservableLists.class);

    private ObservableLists() {
        super();
    }

    /**
     * Ensures that both lists always have the same elements:
     * <ul>
     * <li>every element added to {@code l1} will automatically be added to {@code l2},</li>
     * <li>every element removed from {@code l1} will automatically be removed from {@code l2} too,</li>
     * <li>and <i>vice versa</i>.</li>
     * </ul>
     * <p>
     * Note than the order of the elements is not ensured to be the same.
     *
     * @param l1   First list
     * @param l2   Second list
     * @param <T1> Type of elements of the first list
     * @param <T2> Type of elements of the second list
     */
    public static <T, T1 extends T, T2 extends T> void ensureSameContents(@NotNull ObservableList<T1> l1, @NotNull ObservableList<T2> l2) {

//        Bindings.bindContentBidirectional((ObservableList<T>)l1, (ObservableList<T>)l2);

        ensureContains(l1, null, l2);
        //noinspection unchecked
        ensureContains(l2, null, l1);
    }

    /**
     * Ensures that both lists always have the same elements:
     * <ul>
     * <li>every element added to {@code l1} will automatically be added to {@code l2},</li>
     * <li>every element removed from {@code l1} will automatically be removed from {@code l2} too,</li>
     * <li>and <i>vice versa</i>.</li>
     * </ul>
     * <p>
     * Note than the order of the elements is not ensured to be the same.
     *
     * @param l1           First list
     * @param transformFct Function used to transform every {@code l1} element into {@code l2} element.
     *                     May be {@code null}; in this case, no function will be applied, thus {@code T1} and {@code T2} must be equal.
     *                     If {@code transformFct} returns {@code null}, the {@code l1} element will not be transfered to {@code l2} at all.
     * @param l2           Second list
     * @param <T1>         Type of elements of the first list
     * @param <T2>         Type of elements of the second list
     */
    public static <T, T1 extends T, T2 extends T> void ensureSameContents(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transformFct, @NotNull ObservableList<T2> l2) {
        ensureContains(l1, transformFct, l2);
        ensureContains(l2, null, l1);
    }

    /**
     * Ensures that {@code l2} contains (at least) all {@code l1}' elements:
     * <ul>
     * <li>every element added to {@code l1} will automatically be added to {@code l2},</li>
     * <li>and every element removed from {@code l1} will automatically be removed from {@code l2} too.</li>
     * </ul>
     * <p>
     * Note than the order of the elements is not ensured to be the same.
     *
     * @param l1           First list
     * @param transformFct Eventual function to be used to transform every {@code l1} element into {@code l2} element. May be {@code null}.
     * @param l2           Second list
     * @param <T1>         Type of elements of the first list
     * @param <T2>         Type of elements of the second list
     */
    public static <T1, T2> void ensureContains(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transformFct, @NotNull ObservableList<T2> l2) {
        l1.addListener((ListChangeListener<? super T1>) (ListChangeListener.Change<? extends T1> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<T2> eltsInL1ButNotInL2 = new ArrayList<>();
                    for (T1 eltAddedToL1 : change.getAddedSubList()) {
                        T2 eltForL2 = getEltForT2(transformFct, eltAddedToL1);
                        if (eltForL2 == null) {
                            continue;
                        }
                        if (!l2.contains(eltForL2)) {
                            eltsInL1ButNotInL2.add(eltForL2);
                        }
                    }
                    if (!eltsInL1ButNotInL2.isEmpty()) {
                        l2.addAll(eltsInL1ButNotInL2);
                    }
                }
                if (change.wasRemoved()) {
                    List<T2> eltsNotInL1ButInL2 = new ArrayList<>();
                    for (T1 eltRemovedFromL1 : change.getRemoved()) {
                        T2 eltForL2 = getEltForT2(transformFct, eltRemovedFromL1);
                        if (eltForL2 == null) {
                            continue;
                        }
                        if (l2.contains(eltForL2)) {
                            eltsNotInL1ButInL2.add(eltForL2);
                        }
                    }
                    if (!eltsNotInL1ButInL2.isEmpty()) {
                        l2.removeAll(eltsNotInL1ButInL2);
                    }
                }
            }
        });
    }

    @Null
    private static <T1, T2> T2 getEltForT2(@Null Function<T1, T2> transformFct, T1 eltOfL1) {
        T2 eltForL2;
        if (transformFct == null) { // If no transform function has been set,
            // Keep element, if can be kept :
            try {
                //noinspection unchecked
                eltForL2 = (T2) eltOfL1;
            } catch (ClassCastException e) { // The element can't be kept.
                LOGGER.error("Element " + eltOfL1 + " can't be kept in list (not compatible).", e);
                return null;
            }
        } else { // Si on a une fonction de transformation à appliquer,
            eltForL2 = transformFct.apply(eltOfL1); // alors on considère le résultat de cette fonction à la place de la valeur par défaut.
        }
        return eltForL2;
    }
}
