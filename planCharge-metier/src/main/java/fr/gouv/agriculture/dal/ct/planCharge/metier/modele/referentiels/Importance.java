package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class Importance extends AbstractEntity<String> implements Comparable<Importance> {

    private static class OrdreImportanceComparator implements Comparator<Importance> {

        @Override
        public int compare(@NotNull Importance i1, @NotNull Importance i2) {
            return i1.getOrdre().compareTo(i2.getOrdre());
        }

    }

    public static final Comparator<Importance> COMPARATOR_DEFAUT = new OrdreImportanceComparator();

    @NotNull
    private final String codeInterne;
    @NotNull
    private int ordre;
    @NotNull
    private final String code;

    public Importance(@NotNull int ordre, @NotNull String code) {
        this.codeInterne = ordre + "-" + code;
        this.ordre = ordre;
        this.code = code;
    }

    public Importance(@NotNull String codeInterne) throws ModeleException {
        this.codeInterne = codeInterne;

        if (codeInterne == null) {
            throw new ModeleException("Code non défini.");
        }

        code = codeInterne.replaceFirst("^\\d{2}-", "");
        if (code.equals(codeInterne)) {
            throw new ModeleException("Code interne invalide, pas au format 'NN-AAA...' : '" + codeInterne + "'.");
        }

        try {
            ordre = Integer.parseInt(codeInterne.replaceFirst("-.+$", ""));
        } catch (NumberFormatException e) {
            throw new ModeleException("Code interne invalide, pas au format 'NN-AAA...' : '" + codeInterne + "'.");
        }
    }

    @NotNull
    public String getCodeInterne() {
        return codeInterne;
    }

    @NotNull
    public Integer getOrdre() {
        return ordre;
    }

    @NotNull
    public String getCode() {
        return code;
    }

    @Override
    public String getIdentity() {
//        return getOrdre() + "";
        return codeInterne;
    }

    @Override
    public int compareTo(@NotNull Importance other) {
        return COMPARATOR_DEFAUT.compare(this, other);
    }
}
