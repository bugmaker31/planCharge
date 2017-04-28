package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class Importance extends AbstractEntity<String> implements Comparable<Importance> {

    public static class ImportanceComparator implements Comparator<Importance> {

        @Override
        public int compare(@NotNull Importance i1, @NotNull Importance i2) {
            return i1.getOrder().compareTo(i2.getOrder());
        }
    }
    public static final ImportanceComparator COMPARATOR = new ImportanceComparator();

    @NotNull
    private final String internalCode;
    @NotNull
    private final String code;
    @NotNull
    private int order;

    public Importance(@NotNull String internalCode) {
        this.internalCode = internalCode;

        code = internalCode.replaceFirst("^\\d{2}_", "");
        order =  Integer.parseInt(internalCode.replaceFirst("_.+$", ""));
    }

    @NotNull
    public String getCode() {
        return code;
    }

    @NotNull
    public Integer getOrder() {
        return order;
    }

    @Override
    public String getIdentity() {
        return getCode();
    }


    @Override
    public int compareTo(@NotNull Importance other) {
        return COMPARATOR.compare(this, other);
    }
}
