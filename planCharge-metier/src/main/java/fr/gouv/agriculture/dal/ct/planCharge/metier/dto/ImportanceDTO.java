package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by frederic.danna on 26/03/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class ImportanceDTO extends AbstractDTO<Importance, String, ImportanceDTO> implements Comparable<ImportanceDTO> {

    private static class OrdreImportanceComparator implements Comparator<ImportanceDTO> {

        @Override
        public int compare(ImportanceDTO i1, ImportanceDTO i2) {
            if ((i1 == null) && (i2 == null)) {
                return 0;
            }
            //noinspection ConstantConditions
            if ((i1 == null) && (i2 != null)) {
                return -1;
            }
            //noinspection ConstantConditions
            if ((i1 != null) && (i2 == null)) {
                return 1;
            }
            return i2.getOrdre().compareTo(i1.getOrdre());
        }

    }

    @SuppressWarnings("WeakerAccess")
    public static final OrdreImportanceComparator COMPARATOR_ORDRE = new OrdreImportanceComparator();

    @SuppressWarnings("WeakerAccess")
    public static final Comparator<ImportanceDTO> COMPARATOR_DEFAUT = COMPARATOR_ORDRE;


    @NotNull
    private String codeInterne;
    private int ordre;
    @NotNull
    private String code;


    private ImportanceDTO() {
        super();
    }

    public ImportanceDTO(int ordre, @NotNull String code) {
        this();
        this.codeInterne = ordre + "-" + code;
        this.ordre = ordre;
        this.code = code;
    }

    public ImportanceDTO(@NotNull String codeInterne) throws DTOException {
        super();
        this.codeInterne = codeInterne;

        if (codeInterne == null) {
            throw new DTOException("Code non défini.");
        }

        code = codeInterne.replaceFirst("^\\d{2}-", "");
        if (code.equals(codeInterne)) {
            throw new DTOException("Code interne invalide, pas au format 'NN-AAA...' : '" + codeInterne + "'.");
        }

        //noinspection UnusedCatchParameter
        try {
            String s = codeInterne.replaceFirst("-.+$", "");
            ordre = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new DTOException("Code interne invalide, pas au format 'NN-AAA...' : '" + codeInterne + "'.");
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


    @SuppressWarnings("SuspiciousGetterSetter")
    @Null
    @Override
    public String getIdentity() {
//        return getOrdre() + "";
        return codeInterne;
    }


    @NotNull
    @Override
    public Importance toEntity() {
        assert code != null;
        return new Importance(ordre, code);
    }

    @NotNull
    @Override
    public ImportanceDTO fromEntity(@NotNull Importance entity) {
        return new ImportanceDTO(entity.getOrdre(), entity.getCode());
    }

    @SuppressWarnings("MissortedModifiers")
    @NotNull
    static public ImportanceDTO from(@NotNull Importance entity) {
        return new ImportanceDTO().fromEntity(entity);
    }


    @NotNull
    @Override
    public List<RegleGestion<ImportanceDTO>> getReglesGestion() {
        return new ArrayList<>(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @Override
    public int compareTo(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull ImportanceDTO other) {
        return COMPARATOR_DEFAUT.compare(this, other);
    }
}
