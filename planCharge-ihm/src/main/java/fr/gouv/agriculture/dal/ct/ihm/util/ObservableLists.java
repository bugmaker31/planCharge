package fr.gouv.agriculture.dal.ct.ihm.util;

import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"FinalClass", "UtilityClass"})
public final class ObservableLists {

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
    public static <T1, T2> void ensureSameContents(@NotNull ObservableList<T1> l1, @NotNull ObservableList<T2> l2) {
        new Binding().ensureSameContents(l1, l2);
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
    public static <T1, T2> void ensureSameContents(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transformFct, @NotNull ObservableList<T2> l2) {
        new Binding().ensureSameContents(l1, transformFct, l2);
    }

    public static <T1, T2> void ensureSameContents(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transformT1IntoT2, @Null Function<T2, T1> transformT2IntoT1, @NotNull ObservableList<T2> l2) {
        new Binding().ensureSameContents(l1, transformT1IntoT2, transformT2IntoT1, l2);
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
        new Binding().ensureContains(l1, transformFct, l2);
    }

    public static class Binding {

        @SuppressWarnings("InstanceVariableNamingConvention")
        private /*static*/ final Logger LOGGER = LoggerFactory.getLogger(Binding.class);


        private boolean isLogDebugWanted;

        @Null
        private String list1Name;

        @Null
        private String list2Name;


        public Binding() {
            super();
            this.isLogDebugWanted = false;
            this.list1Name = null;
            this.list2Name = null;
        }

        public Binding(@NotNull String list1Name, @NotNull String list2Name) {
            this();
            this.list1Name = list1Name;
            this.list2Name = list2Name;
        }


        public boolean isLogDebugWanted() {
            return isLogDebugWanted;
        }

        public void setLogDebugWanted(boolean logDebugWanted) {
            isLogDebugWanted = logDebugWanted;
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
        public <T1, T2> void ensureSameContents(@NotNull ObservableList<T1> l1, @NotNull ObservableList<T2> l2) {

//        Bindings.bindContentBidirectional((ObservableList<T>)l1, (ObservableList<T>)l2);

            ensureContains(l1, null, l2);
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
        public <T1, T2> void ensureSameContents(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transformFct, @NotNull ObservableList<T2> l2) {
            ensureSameContents(l1, transformFct, null, l2);
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
         * @param l1                First list
         * @param transformT1IntoT2 Function used to transform every {@code l1} element into {@code l2} element.
         *                          May be {@code null}; in this case, no function will be applied, thus {@code T1} must be assignable from {@code T2}.
         *                          If {@code transformT1IntoT2} returns {@code null}, the {@code l1} element will not be transfered to {@code l2} at all.
         * @param transformT2IntoT1 Function used to transform every {@code l2} element into {@code l1} element.
         *                          May be {@code null}; in this case, no function will be applied, thus {@code T2} must be assignable from {@code T1} .
         *                          If {@code transformT2IntoT1} returns {@code null}, the {@code l2} element will not be transfered to {@code l1} at all.
         * @param l2                Second list
         * @param <T1>              Type of elements of the first list
         * @param <T2>              Type of elements of the second list
         */
        public <T1, T2> void ensureSameContents(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transformT1IntoT2, @Null Function<T2, T1> transformT2IntoT1, @NotNull ObservableList<T2> l2) {
            ensureContains(l1, transformT1IntoT2, l2);
            ensureContains(l2, transformT2IntoT1, l1);
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
        public <T1, T2> void ensureContains(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transformFct, @NotNull ObservableList<T2> l2) {
            l1.addListener((ListChangeListener<? super T1>) (Change<? extends T1> change) -> {
//                LOGGER.debug("- Changes just made to list {}, reporting them into list {}", name(l1), name(l2));
                while (change.next()) {
                    if (change.wasAdded()) {
                        debugIfWanted("- {} elements just added to list {}, adding them to list {}", change.getAddedSubList().size(), name(l1, list1Name), name(l2, list2Name));
                        List<T2> eltsInL1ButNotInL2 = new ArrayList<>(change.getAddedSubList().size());
                        for (T1 eltAddedToL1 : change.getAddedSubList()) {
                            debugIfWanted("   - element added to list {} : {}", name(l1, list1Name), eltAddedToL1);
                            T2 eltForL2 = getEltForT2(transformFct, eltAddedToL1);
                            debugIfWanted("     - transformed element to add to list {} : {}", name(l2, list2Name), eltForL2);
                            if (eltForL2 == null) {
                                debugIfWanted("     - transformed element is null, skipped");
                                continue;
                            }
                            if (!l2.contains(eltForL2)) {
                                debugIfWanted("     - transformed element does not already belong to list {}, planned for addition", name(l2, list2Name));
                                eltsInL1ButNotInL2.add(eltForL2);
                            } else {
                                debugIfWanted("     - transformed element already belongs to list {}, skipped", name(l2, list2Name));
                            }
                        }
                        if (!eltsInL1ButNotInL2.isEmpty()) {
                            LOGGER.debug("{} elements added to list '{}', adding {} of them to list '{}'", change.getAddedSubList().size(), name(l1, list1Name), eltsInL1ButNotInL2.size(), name(l2, list2Name));
                            l2.addAll(eltsInL1ButNotInL2);
                            debugIfWanted("   - {} elements added to list {}", eltsInL1ButNotInL2.size(), name(l2, list2Name));
                        }
                    }
                    if (change.wasRemoved()) {
                        LOGGER.debug("- {} elements just removed from {}, removing them of {}.", change.getRemoved().size(), name(l1, list1Name), name(l2, list2Name));
                        List<T2> eltsNotInL1ButInL2 = new ArrayList<>(change.getRemoved().size());
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
                            LOGGER.debug("{} elements removed to list '{}', removing {} of them from list '{}'", change.getRemoved().size(), name(l1, list1Name), eltsNotInL1ButInL2.size(), name(l2, list2Name));
                            l2.removeAll(eltsNotInL1ButInL2);
                            debugIfWanted("   - {} elements removed from list {}", eltsNotInL1ButInL2.size(), name(l2, list2Name));
                        }
                    }
                }
            });
            LOGGER.debug("List '{}' is ensured to contains at least '{}' elements.", name(l2, list2Name), name(l1, list1Name));
        }

        @NotNull
        private String name(@NotNull ObservableList<?> list, @Null String listName) {
            return Objects.value(listName, Objects.idInstance(list));
        }

        @Null
        private <T1, T2> T2 getEltForT2(@Null Function<T1, T2> transformFct, T1 eltOfL1) {
            T2 eltForL2;
            if (transformFct == null) { // If no transform function has been set,
                // Keep element, if can be kept :
                try {
                    //noinspection unchecked
                    eltForL2 = (T2) eltOfL1;
                } catch (ClassCastException e) { // The element can't be kept.
                    LOGGER.error("Element {} can't be kept in list (not compatible).", eltOfL1, e);
                    return null;
                }
            } else { // Si on a une fonction de transformation à appliquer,
                eltForL2 = transformFct.apply(eltOfL1); // alors on considère le résultat de cette fonction à la place de la valeur par défaut.
            }
            return eltForL2;
        }

        private void debugIfWanted(@NotNull String message, @NotNull Object... args) {
            if (isLogDebugWanted)
                LOGGER.debug(message, args);
        }
    }

    private ObservableLists() {
        super();
    }
}
