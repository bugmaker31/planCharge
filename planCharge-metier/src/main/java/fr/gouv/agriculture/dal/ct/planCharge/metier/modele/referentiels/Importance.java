package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;

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
    private final String codeInterne;
    @NotNull
    private final String code;
    @NotNull
    private int order;

    public Importance(@NotNull String codeInterne) throws ModeleException {
        this.codeInterne = codeInterne;

        code = codeInterne.replaceFirst("^\\d{2}-", "");
        if (code.equals(codeInterne)) {
            throw new ModeleException("Code interne invalide, pas au format 'NN-AAA...' : '" + codeInterne + "'.");
        }

        try {
            order = Integer.parseInt(codeInterne.replaceFirst("-.+$", ""));
        } catch (NumberFormatException e) {
            throw new ModeleException("Code interne invalide, pas au format 'NN-AAA...' : '" + codeInterne + "'.");
        }
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
