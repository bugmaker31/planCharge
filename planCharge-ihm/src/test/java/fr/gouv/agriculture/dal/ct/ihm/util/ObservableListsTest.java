package fr.gouv.agriculture.dal.ct.ihm.util;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.function.Function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObservableListsTest {

    private static final Integer ELT1_NBR = 1;
    private static final Integer ELT2_NBR = 2;
    private static final Integer ELT3_NBR = 3;
    private static final Integer ELT4_NBR = 4;
    private static final String ELT1_STR = String.valueOf(ELT1_NBR);
    private static final String ELT2_STR = String.valueOf(ELT2_NBR);
    private static final String ELT3_STR = String.valueOf(ELT3_NBR);
    private static final String ELT4_STR = String.valueOf(ELT4_NBR);

    @Test
    public void ensureSameContents_WithoutTransformFunction() throws Exception {
        ObservableList<String> l1 = FXCollections.observableArrayList();
        ObservableList<String> l2 = FXCollections.observableArrayList();

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

    @Test
    public void ensureSameContents_WithTransformFunction() throws Exception {
        ObservableList<Integer> l1 = FXCollections.observableArrayList();
        ObservableList<String> l2 = FXCollections.observableArrayList();

        ObservableLists.ensureSameContents(l1, l2);

        l1.add(ELT1_NBR);
        assertSameContents(l1, l2);
        assertTrue(l1.contains(ELT1_NBR));
        assertTrue(l2.contains(ELT1_STR));

        l2.add(ELT2_STR);
        assertSameContents(l1, l2);
        assertTrue(l1.contains(ELT2_NBR));
        assertTrue(l2.contains(ELT2_STR));

        l1.add(ELT3_NBR);
        assertSameContents(l1, l2);
        assertTrue(l1.contains(ELT3_NBR));
        assertTrue(l2.contains(ELT3_STR));

        l2.add(ELT4_STR);
        assertSameContents(l1, l2);
        assertTrue(l1.contains(ELT4_NBR));
        assertTrue(l2.contains(ELT4_STR));
    }

    @Test
    public void ensureContains() throws Exception {
        ObservableList<Integer> l1 = FXCollections.observableArrayList();
        ObservableList<String> l2 = FXCollections.observableArrayList();

        Function<Integer, String> transform = String::valueOf;

        ObservableLists.ensureContains(l1, transform, l2);

        l1.add(ELT1_NBR);
        assertContains(l1, transform, l2);
        assertTrue(l1.contains(ELT1_NBR));
        assertTrue(l2.contains(ELT1_STR));

        l2.add(ELT2_STR);
        assertContains(l1, transform, l2);
        assertTrue(l2.contains(ELT2_STR));
        assertFalse(l1.contains(ELT2_NBR));

        l1.add(ELT3_NBR);
        assertContains(l1, transform, l2);
        assertTrue(l1.contains(ELT3_NBR));
        assertTrue(l2.contains(ELT3_STR));

        l2.add(ELT4_STR);
        assertContains(l1, transform, l2);
        assertTrue(l2.contains(ELT4_STR));
        assertFalse(l1.contains(ELT4_NBR));
    }


    private <T, T1 extends T, T2 extends T> void assertSameContents(@NotNull ObservableList<T1> l1, @NotNull ObservableList<T2> l2) {
        assertTrue(l1.size() == l2.size());
        assertContains(l1, null, l2);
        assertContains(l2, null, l1);
    }

    private <T1, T2> void assertContains(@NotNull ObservableList<T1> l1, @Null Function<T1, T2> transform, @NotNull ObservableList<T2> l2) {
        assertTrue(l1.size() <= l2.size());
        for (T1 e1 : l1) {
            //noinspection unchecked
            T2 e2 = transform == null ? (T2) e1 : transform.apply(e1);
            assertTrue(l2.contains(e2));
        }
    }
}