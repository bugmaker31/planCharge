package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collections;
import java.util.List;

/**
 * Created by frederic.danna on 24/06/2017.
 */
public class StatutDTO extends AbstractDTO<Statut, String, StatutDTO> implements Comparable<StatutDTO> {

    public static final StatutDTO NOUVEAU = StatutDTO.from(Statut.NOUVEAU);
    public static final StatutDTO EN_COURS = StatutDTO.from(Statut.EN_COURS);
    public static final StatutDTO EN_ATTENTE = StatutDTO.from(Statut.EN_ATTENTE);
    public static final StatutDTO RECURRENT = StatutDTO.from(Statut.RECURRENT);
    public static final StatutDTO REPORTE = StatutDTO.from(Statut.REPORTE);
    public static final StatutDTO ANNULE = StatutDTO.from(Statut.ANNULE);
    public static final StatutDTO DOUBLON = StatutDTO.from(Statut.DOUBLON);
    public static final StatutDTO TERMINE = StatutDTO.from(Statut.TERMINE);
    public static final StatutDTO A_VENIR = StatutDTO.from(Statut.A_VENIR);
    //
    public static final StatutDTO PROVISION = StatutDTO.from(Statut.PROVISION);

    @Null
    private String code;


    private StatutDTO() {
        super();
    }

    public StatutDTO(@Null String code) {
        this();
        this.code = code;
    }

    @Null
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
    public Statut toEntity() {
        return new Statut(code);
    }

    @NotNull
    @Override
    public StatutDTO fromEntity(@NotNull Statut entity) {
        return new StatutDTO(entity.getCode());
    }

    @NotNull
    static public StatutDTO from(@NotNull Statut entity) {
        return new StatutDTO().fromEntity(entity);
    }


    @NotNull
    @Override
    public List<RegleGestion<StatutDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les règles de gestion.
    }


    @Override
    public int compareTo(@NotNull StatutDTO o) {
        return (code == null) ? -1 : ((o.getCode() == null) ? 1 : code.compareTo(o.getCode()));
    }

}
