package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.*;

/**
 * Created by frederic.danna on 24/06/2017.
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithOnlyPrivateConstructors", "ClassWithTooManyFields", "ClassWithoutLogger", "ComparableImplementedButEqualsNotOverridden"})
public class StatutDTO extends AbstractDTO<Statut, String, StatutDTO> {

    public static final StatutDTO NOUVEAU = init(Statut.NOUVEAU);
    public static final StatutDTO EN_COURS = init(Statut.EN_COURS);
    public static final StatutDTO EN_ATTENTE = init(Statut.EN_ATTENTE);
    public static final StatutDTO RECURRENT = init(Statut.RECURRENT);
    public static final StatutDTO REPORTE = init(Statut.REPORTE);
    public static final StatutDTO ANNULE = init(Statut.ANNULE);
    public static final StatutDTO DOUBLON = init(Statut.DOUBLON);
    public static final StatutDTO TERMINE = init(Statut.TERMINE);
    public static final StatutDTO A_VENIR = init(Statut.A_VENIR);
    //
    public static final StatutDTO PROVISION = init(Statut.PROVISION);

    public static final Set<StatutDTO> VALUES = new HashSet<>(20);

    static {
        Collections.addAll(VALUES,
                NOUVEAU,
                EN_COURS,
                EN_ATTENTE,
                RECURRENT,
                REPORTE,
                ANNULE,
                DOUBLON,
                TERMINE,
                A_VENIR,
                //
                PROVISION
        );
    }

    @NotNull
    public static StatutDTO valueOf(@NotNull String code) throws ModeleException {
        return VALUES.parallelStream()
                .filter(statut -> statut.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new ModeleException("Statut non géré : '" + code + "'."));
    }

    private static StatutDTO init(@NotNull Statut statut) {
        return new StatutDTO(statut);
    }


    @NotNull
    private final String code;


    private StatutDTO(@NotNull Statut statut) {
        super();
        this.code = statut.getCode();
    }


    @NotNull
    public String getCode() {
        return code;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    @Null
    @Override
    public String getIdentity() {
        return code;
    }


    @NotNull
    @Override
    public Statut toEntity() throws DTOException {
        return to(this);
    }

    @NotNull
    @Override
    public StatutDTO fromEntity(@NotNull Statut entity) throws DTOException {
        return from(entity);
    }

    @NotNull
    public static StatutDTO from(@NotNull Statut entity) throws DTOException {
        try {
            return valueOf(entity.getCode());
        } catch (ModeleException e) {
            throw new DTOException("Statut non géré : '" + entity.getCode() + "'.", e);
        }
    }

    @NotNull
    public static Statut to(@NotNull StatutDTO dto) throws DTOException {
        try {
            return Statut.valueOf(dto.getCode());
        } catch (ModeleException e) {
            throw new DTOException("Statut non géré : '" + dto.getCode() + "'.", e);
        }
    }

    public static StatutDTO safeFrom(@NotNull Statut entity) {
        try {
            return from(entity);
        } catch (DTOException e) {
//            TODO FDA 2017/11 Trouver mieux comme code.
            throw new RuntimeException(e);
        }
    }

    public static Statut safeTo(@NotNull StatutDTO dto) {
        try {
            return to(dto);
        } catch (DTOException e) {
//            TODO FDA 2017/11 Trouver mieux comme code.
            throw new RuntimeException(e);
        }
    }


    @NotNull
    @Override
    public List<RegleGestion<StatutDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @Override
    public int compareTo(@NotNull StatutDTO o) {
        return code.compareTo(o.getCode());
    }

}
