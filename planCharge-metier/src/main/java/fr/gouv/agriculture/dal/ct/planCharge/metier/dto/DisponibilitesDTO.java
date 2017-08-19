package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.disponibilite.Disponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DisponibilitesDTO extends AbstractDTO<Disponibilites, Integer, DisponibilitesDTO> {

    // Fields:

    @SuppressWarnings("NullableProblems")
    @NotNull
    private Map<RessourceHumaineDTO, Map<LocalDate, Float>> nbrsJoursAbsence;

    @SuppressWarnings("NullableProblems")
    @NotNull
    private Map<RessourceHumaineDTO, Map<LocalDate, Percentage>> pctagesDispoCT;


    // Constructors:

    private DisponibilitesDTO() {
        super();
    }

    public DisponibilitesDTO(@NotNull Map<RessourceHumaineDTO, Map<LocalDate, Float>> nbrsJoursAbsence, @NotNull Map<RessourceHumaineDTO, Map<LocalDate, Percentage>> pctagesDispoCT) {
        this();
        this.nbrsJoursAbsence = nbrsJoursAbsence;
        this.pctagesDispoCT = pctagesDispoCT;
    }


    // Getters/Setters:

    @NotNull
    public Map<RessourceHumaineDTO, Map<LocalDate, Float>> getNbrsJoursAbsence() {
        return nbrsJoursAbsence;
    }

    @NotNull
    public Map<RessourceHumaineDTO, Map<LocalDate, Percentage>> getPctagesDispoCT() {
        return pctagesDispoCT;
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
/*
        return new Disponibilites(
                getNbrsJoursAbsence().keySet().stream()
                        .collect(Collectors.toMap(
                                RessourceHumaineDTO::toEntity, ressourceHumaine -> getNbrsJoursAbsence().get(ressourceHumaine)
                        ))
        );
*/

        // Nbrs de jours d'absence :
        Map<RessourceHumaine, Map<LocalDate, Float>> nbrsJoursAbsenceEntities = new TreeMap<>(); // TreeMap pour trier, juste pour faciliter le débogage.
        //noinspection SimplifyStreamApiCallChains
        nbrsJoursAbsence.keySet().stream()
                .forEachOrdered(ressourceHumaineDTO -> nbrsJoursAbsenceEntities.put(ressourceHumaineDTO.toEntity(), nbrsJoursAbsence.get(ressourceHumaineDTO)));

        // Pctages de dispo pour la CT :
        Map<RessourceHumaine, Map<LocalDate, Percentage>> pctagesDispoCTEntities = new TreeMap<>(); // TreeMap pour trier, juste pour faciliter le débogage.
        pctagesDispoCT.keySet().stream()
                .forEachOrdered(ressourceHumaineDTO -> pctagesDispoCTEntities.put(ressourceHumaineDTO.toEntity(), pctagesDispoCT.get(ressourceHumaineDTO)));

        Disponibilites disponibilites = new Disponibilites(nbrsJoursAbsenceEntities, pctagesDispoCTEntities);
        return disponibilites;
    }

    @NotNull
    @Override
    public DisponibilitesDTO fromEntity(@NotNull Disponibilites entity) throws DTOException {
/*
        return new DisponibilitesDTO(
                entity.getNbrsJoursAbsence().keySet().stream()
                        .collect(Collectors.toMap(
                                RessourceHumaineDTO::from, ressourceHumaine -> entity.getNbrsJoursAbsence().get(ressourceHumaine)
                        ))
        );
*/
        // Nbrs de jours d'absence :
        Map<RessourceHumaineDTO, Map<LocalDate, Float>> nbrsJoursAbsenceDTO = new TreeMap<>(); // TreeMap pour trier, juste pour faciliter le débogage.
        //noinspection SimplifyStreamApiCallChains
        entity.getNbrsJoursAbsence().keySet().stream()
                .forEachOrdered(ressourceHumaine -> nbrsJoursAbsenceDTO.put(RessourceHumaineDTO.from(ressourceHumaine), entity.getNbrsJoursAbsence().get(ressourceHumaine)));

        // Pctages de dispo pour la CT :
        Map<RessourceHumaineDTO, Map<LocalDate, Percentage>> pctagesDispoCTDTO = new TreeMap<>(); // TreeMap pour trier, juste pour faciliter le débogage.
        //noinspection SimplifyStreamApiCallChains
        entity.getPctagesDispoCT().keySet().stream()
                .forEachOrdered(ressourceHumaine -> pctagesDispoCTDTO.put(RessourceHumaineDTO.from(ressourceHumaine), entity.getPctagesDispoCT().get(ressourceHumaine)));

        DisponibilitesDTO disponibilites = new DisponibilitesDTO(nbrsJoursAbsenceDTO, pctagesDispoCTDTO);
        return disponibilites;
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
