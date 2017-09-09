package fr.gouv.agriculture.dal.ct.ihm.util;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public final class ObservableLists {

    private ObservableLists() {
    }

    /**
     * Ensures that both lists always have the same elements:
     * every element added to {@code l1 first list} will automatically be added to {@code l2 second list},
     * every element removed from {@code l1 first list} will automatically be removed from {@code l2 second list} too,
     * and <i>vice versa</i>.
     *
     * <p>
     * Note than the order of the elements is not ensured to be the same.
     * </p>
     *
     * @param l1 first list
     * @param l2 seconde list
     * @param <T> type of elements of the two lists
     */
    public static <T> void ensureSameContents(@NotNull ObservableList<T> l1, @NotNull ObservableList<T> l2) {
/*
        ensureSameContentsThan(l1, l2);
        ensureSameContentsThan(l2, l1);
*/
        Bindings.bindContentBidirectional(l1, l2);
    }

    /**
     * Ensures that {@code l2 first list} always contains same elements than {@code l1 second list}:
     * every element added to {@code l1 first list} will automatically be added to {@code l2 second list},
     * and every element removed from {@code l1 first list} will automatically be removed from {@code l2 second list} too.
     *
     * <p>
     * Note than the order of the elements is not ensured to be the same.
     * </p>
     *
     * @param l1 first list
     * @param l2 seconde list
     * @param <T> type of elements of the two lists
     */
    public static <T> void ensureSameContentsThan(@NotNull ObservableList<T> l1, @NotNull ObservableList<T> l2) {
        l1.addListener((ListChangeListener<? super T>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<T> eltsInL1ButNotInL2 = new ArrayList<>();
                    for (T eltAddedToL1 : change.getAddedSubList()) {
                        if (!l2.contains(eltAddedToL1)) {
                            eltsInL1ButNotInL2.add(eltAddedToL1);
                        }
                    }
                    if (!eltsInL1ButNotInL2.isEmpty()) {
                        l2.addAll(eltsInL1ButNotInL2);
                    }
                }
                if (change.wasRemoved()) {
                    List<T> eltsNotInL1ButInL2 = new ArrayList<>();
                    for (T eltRemovedFromL1 : change.getRemoved()) {
                        if (l2.contains(eltRemovedFromL1)) {
                            eltsNotInL1ButInL2.add(eltRemovedFromL1);
                        }
                    }
                    if (!eltsNotInL1ButInL2.isEmpty()) {
                        l2.removeAll(eltsNotInL1ButInL2);
                    }
                }
            }
        });
    }
}
