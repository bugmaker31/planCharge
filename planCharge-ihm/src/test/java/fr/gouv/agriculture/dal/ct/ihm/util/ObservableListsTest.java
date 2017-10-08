package fr.gouv.agriculture.dal.ct.ihm.util;


import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("LocalVariableNamingConvention")
public class ObservableListsTest {

    @Test
    public void ensureSameContents_WithoutTransformFunction() throws Exception {

        //noinspection ClassWithoutConstructor,LimitedScopeInnerClass,ClassNamingConvention
        class C {
        }

        C ELT1_STR = new C();
        C ELT2_STR = new C();
        C ELT3_STR = new C();
        C ELT4_STR = new C();

        ObservableList<C> l1 = FXCollections.observableArrayList();
        ObservableList<C> l2 = FXCollections.observableArrayList();

        ObservableLists.ensureSameContents(l1, l2);

        l1.add(ELT1_STR);
        assertSameContents(l1, l2);
        l2.add(ELT2_STR);
        assertSameContents(l1, l2);
        l1.add(ELT3_STR);
        assertSameContents(l1, l2);
        l2.add(ELT4_STR);
        assertSameContents(l1, l2);
    }

    @SuppressWarnings({"UnnecessaryLocalVariable", "MessageMissingOnJUnitAssertion"})
    @Test
    public void ensureSameContents_WithTransformFunction() throws Exception {

        //noinspection LimitedScopeInnerClass,ClassWithoutLogger
        class NamedData {
            @SuppressWarnings("InstanceVariableNamingConvention")
            @NotNull
            private final String id;

            NamedData(@NotNull String id) {
                super();
                this.id = id;
            }

            @Override
            public String toString() {
                //noinspection MagicCharacter
                return "C1" + "=" + id;
            }
        }

        //noinspection ClassWithoutConstructor,LimitedScopeInnerClass,ClassNamingConvention,ClassWithoutLogger,EmptyClass,ClassWithOnlyPrivateConstructors
        class C1 extends NamedData {
            private C1(String id) {
                super(id);
            }
        }

        //noinspection ClassWithoutConstructor,LimitedScopeInnerClass,ClassNamingConvention,ClassWithoutLogger,EmptyClass,ClassWithOnlyPrivateConstructors,ClassTooDeepInInheritanceTree
        class C2 extends C1 {
            private C2(String id) {
                super(id);
            }
        }

        C2 elt2_1 = new C2("elt1");
        C1 elt1_1 = elt2_1;

        C2 elt2_2 = new C2("elt2");
        C1 elt1_2 = elt2_2;

        //noinspection ClassWithOnlyPrivateConstructors,LimitedScopeInnerClass,ClassWithoutLogger
        class NamedObservableList<E> extends ObservableListWrapper<E> {
            private final String id;

            private NamedObservableList(@NotNull String id) {
                super(new ArrayList<>(0));
                this.id = id;
            }

            @Override
            public String toString() {
                return id + "=" + super.toString();
            }
        }

        NamedObservableList<C1> l1 = new NamedObservableList<>("list1");
        NamedObservableList<C2> l2 = new NamedObservableList<>("list2");

        Function<C1, C2> transform = elt1 -> (elt1 instanceof C2) ? (C2) elt1 : null;

        ObservableLists.ensureSameContents(l1, transform, l2);

        l1.add(elt1_1);
        assertSameContents(l1, l2);
        Assert.assertThat(l1, hasItem(elt1_1));
        Assert.assertThat(l2, hasItem(elt2_1));

        l1.add(elt1_2);
        assertSameContents(l1, l2);
        Assert.assertThat(l1, hasItem(elt1_2));
        Assert.assertThat(l2, hasItem(elt2_2));

        l2.add(elt2_1);
        assertSameContents(l1, l2);
        Assert.assertThat(l1, hasItem(elt1_1));
        Assert.assertThat(l2, hasItem(elt2_1));

        l2.add(elt2_2);
        assertSameContents(l1, l2);
        Assert.assertThat(l1, hasItem(elt2_2));
        Assert.assertThat(l2, hasItem(elt2_2));
    }

    @SuppressWarnings({"TooBroadScope", "MessageMissingOnJUnitAssertion"})
    @Test
    public void ensureContains() throws Exception {
        Integer ELT1_NBR = 1;
        Integer ELT2_NBR = 2;
        Integer ELT3_NBR = 3;
        Integer ELT4_NBR = 4;
        String ELT1_STR = String.valueOf(ELT1_NBR);
        String ELT2_STR = String.valueOf(ELT2_NBR);
        String ELT3_STR = String.valueOf(ELT3_NBR);
        String ELT4_STR = String.valueOf(ELT4_NBR);

        ObservableList<Integer> l1 = FXCollections.observableArrayList();
        ObservableList<String> l2 = FXCollections.observableArrayList();

        Function<Integer, String> transform = String::valueOf;

        ObservableLists.ensureContains(l1, transform, l2);

        l1.add(ELT1_NBR);
        assertContains(l1, transform, l2);
        Assert.assertThat(l1, hasItem(ELT1_NBR));
        Assert.assertThat(l2, hasItem(ELT1_STR));

        l2.add(ELT2_STR);
        assertContains(l1, transform, l2);
        Assert.assertThat(l2, hasItem(ELT2_STR));
        Assert.assertThat(l1, not(hasItem(ELT2_NBR)));

        l1.add(ELT3_NBR);
        assertContains(l1, transform, l2);
        Assert.assertThat(l1, hasItem(ELT3_NBR));
        Assert.assertThat(l2, hasItem(ELT3_STR));

        l2.add(ELT4_STR);
        assertContains(l1, transform, l2);
        Assert.assertThat(l2, hasItem(ELT4_STR));
        Assert.assertThat(l1, not(hasItem(ELT4_NBR)));
    }


    @SuppressWarnings("MessageMissingOnJUnitAssertion")
    private static <T, T1 extends T, T2 extends T> void assertSameContents(@NotNull ObservableList<T1> l1, @NotNull ObservableList<T2> l2) {
//        Assert.assertThat(l1.size(), is(l2.size()));
        assertContains(l1, null, l2);
        assertContains(l2, null, l1);
    }

    @SuppressWarnings("MessageMissingOnJUnitAssertion")
    private static <T1, T2> void assertContains(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transform, @NotNull ObservableList<T2> l2) {
        //noinspection MigrateAssertToMatcherAssert
//        assertTrue(l1.size() <= l2.size());
        for (T1 e1 : l1) {
            //noinspection unchecked
            T2 e2 = (transform == null) ? (T2) e1 : transform.apply(e1);
            Assert.assertThat(l2, hasItem(e2));
        }
    }
}