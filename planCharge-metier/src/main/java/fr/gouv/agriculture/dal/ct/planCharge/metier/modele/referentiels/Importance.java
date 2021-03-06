package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * Created by frederic.danna on 26/03/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class Importance extends AbstractEntity<String, Importance> implements Comparable<Importance> {

    private static class OrdreImportanceComparator implements Comparator<Importance> {

        @Override
        public int compare(Importance i1, Importance i2) {
            if (i1 == null && i2 == null) {
                return 0;
            }
            if (i1 == null && i2 != null) {
                return -1;
            }
            if (i1 != null && i2 == null) {
                return 1;
            }
            return i2.getNoOrdre().compareTo(i1.getNoOrdre());
        }

    }

    @SuppressWarnings("WeakerAccess")
    public static final OrdreImportanceComparator COMPARATOR_ORDRE = new OrdreImportanceComparator();

    @SuppressWarnings("WeakerAccess")
    public static final Comparator<Importance> COMPARATOR_DEFAUT = COMPARATOR_ORDRE;


    @NotNull
    private final String codeInterne;
    private int noOrdre;
    @NotNull
    private final String code;


    public Importance(@NotNull int noOrdre, @NotNull String code) {
        super();
        codeInterne = noOrdre + "-" + code;
        this.noOrdre = noOrdre;
        this.code = code;
    }

    public Importance(@NotNull String codeInterne) throws ModeleException {
        super();
        this.codeInterne = codeInterne;

        if (codeInterne == null) {
            throw new ModeleException("Code non défini.");
        }

        code = codeInterne.replaceFirst("^\\d{2}-", "");
        if (code.equals(codeInterne)) {
            throw new ModeleException("Code interne invalide, pas au format 'NN-AAA...' : '" + codeInterne + "'.");
        }

        try {
            noOrdre = Integer.parseInt(codeInterne.replaceFirst("-.+$", ""));
        } catch (NumberFormatException e) {
            throw new ModeleException("Code interne invalide, pas au format 'NN-AAA...' : '" + codeInterne + "'.", e);
        }
    }


    @NotNull
    public String getCodeInterne() {
        return codeInterne;
    }

    @NotNull
    public Integer getNoOrdre() {
        return noOrdre;
    }

    @NotNull
    public String getCode() {
        return code;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @NotNull
    @Override
    public String getIdentity() {
//        return getNoOrdre() + "";
        return codeInterne;
    }


    @Override
    public int compareTo(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull Importance other) {
        return COMPARATOR_DEFAUT.compare(this, other);
    }
}
