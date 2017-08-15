package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.disponibilite.Disponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DisponibilitesDTO extends AbstractDTO<Disponibilites, Integer, DisponibilitesDTO> {

    // Fields:

    @SuppressWarnings("NullableProblems")
    @NotNull
    private Map<RessourceHumaineDTO, Map<LocalDate, Integer>> absences;


    // Constructors:

    private DisponibilitesDTO() {
        super();
    }

    public DisponibilitesDTO(@NotNull Map<RessourceHumaineDTO, Map<LocalDate, Integer>> absences) {
        this();
        this.absences = absences;
    }


    // Getters/Setters:

    @NotNull
    public Map<RessourceHumaineDTO, Map<LocalDate, Integer>> getAbsences() {
        return absences;
    }


    // Implementation de AbstractDTO :

    @Null
    @Override
    public Integer getIdentity() {
        return null; // TODO FDA 2017/08 Trouver mieux comme code.
    }

    @Override
    public int compareTo(DisponibilitesDTO o) {
        return 0; // TODO FDA 2017/08 Trouver mieux comme code.
    }

    @NotNull
    @Override
    public Disponibilites toEntity() throws DTOException {
        return new Disponibilites(
                getAbsences().keySet().stream()
                        .collect(Collectors.toMap(RessourceHumaineDTO::toEntity, ressourceHumaine -> getAbsences().get(ressourceHumaine)))
        );
    }

    @NotNull
    @Override
    public DisponibilitesDTO fromEntity(@NotNull Disponibilites entity) throws DTOException {
        return new DisponibilitesDTO(
                entity.getAbsences().keySet().stream()
                        .collect(Collectors.toMap(RessourceHumaineDTO::from, ressourceHumaine -> entity.getAbsences().get(ressourceHumaine)))
        );
    }

    @NotNull
    public static DisponibilitesDTO from(@NotNull Disponibilites entity) throws DTOException {
        return new DisponibilitesDTO().fromEntity(entity);
    }

    @NotNull
    @Override
    protected List<RegleGestion<DisponibilitesDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/08 Coder les RG.
    }

}
