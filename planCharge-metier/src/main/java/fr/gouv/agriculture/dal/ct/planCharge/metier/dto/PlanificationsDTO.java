package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Referentiels;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Inspiré de {@link Map}.
 *
 * Created by frederic.danna on 20/03/2017.
 */
public class PlanificationsDTO extends AbstractDTO<Planifications, Serializable, PlanificationsDTO> {

    public static final int NBR_SEMAINES_PLANIFIEES = 12;

    @NotNull
    private Map<TacheDTO, Map<LocalDate, Double>> plan;


    public PlanificationsDTO() {
        super();
        this.plan = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
    }

    public PlanificationsDTO(@NotNull Map<TacheDTO, Map<LocalDate, Double>> plan) {
        super();
        this.plan = plan;
    }


    @Null
    @Override
    public Serializable getIdentity() {
        return null; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @Override
    public int compareTo(PlanificationsDTO o) {
        return 0; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @NotNull
    public Set<TacheDTO> taches() {
        return plan.keySet();
    }

    @Null
    public TacheDTO tache(int idTache) {
        Optional<TacheDTO> optTache = plan.keySet().parallelStream()
                .filter(tache -> tache.getId() == idTache)
                .findAny();
        return (optTache.orElse(null));
    }


    @NotNull
    public double chargePlanifiee(@NotNull TacheDTO tache) throws TacheSansPlanificationException {
        double chargePlanifiee;

        Map<LocalDate, Double> calendrier = calendrier(tache);
        if (calendrier == null) {
            chargePlanifiee = 0.0;
        } else {
            chargePlanifiee = 0.0;
            for (LocalDate dateSemaine : calendrier.keySet()) {
                Double chargeSemaine = calendrier.get(dateSemaine);
                chargePlanifiee += chargeSemaine;
            }
        }

        return chargePlanifiee;
    }


    @NotNull
    public Map<LocalDate, Double> calendrier(@NotNull TacheDTO tache) throws TacheSansPlanificationException {
        if (!plan.containsKey(tache)) {
            throw new TacheSansPlanificationException(tache);
        }
        return plan.get(tache);
    }


    public void ajouter(@NotNull TacheDTO tache, @NotNull LocalDate dateEtat) {

        Map<LocalDate, Double> planification = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
        LocalDate dateSemaine = dateEtat;
        for (int noSemaine = 1; noSemaine <= NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            planification.put(dateSemaine, 0.0);
            dateSemaine = dateSemaine.plusDays(7);
        }

        ajouter(tache, planification);
    }

    public void ajouter(@NotNull TacheDTO tache, Map<LocalDate, Double> calendrier) {
        plan.put(tache, calendrier);
    }


    @NotNull
    @Override
    public Planifications toEntity() throws DTOException {
        Map<Tache, Map<LocalDate, Double>> planif = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
        for (TacheDTO tacheDTO : plan.keySet()) {
            Tache tache = tacheDTO.toEntity();
            Map<LocalDate, Double> calendrierTache = plan.get(tacheDTO);
            planif.put(tache, calendrierTache);
        }
        return new Planifications(planif);
    }

    @NotNull
    @Override
    public PlanificationsDTO fromEntity(@NotNull Planifications planifications) throws DTOException {
        Map<TacheDTO, Map<LocalDate, Double>> planifDTOsMap = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
        for (Tache tache : planifications.taches()) {
            TacheDTO tacheDTO = TacheDTO.from(tache);
            try {
                planifDTOsMap.put(tacheDTO, planifications.calendrier(tache));
            } catch (TacheSansPlanificationException e) {
                // Ne peut pas arriver, par construction.
                throw new DTOException("Impossible de transformer les planification d'entité en DTO.", e);
            }
        }
        PlanificationsDTO planificationsDTO = new PlanificationsDTO(planifDTOsMap);
        return planificationsDTO;
    }

    @NotNull
    public static PlanificationsDTO from(@NotNull Planifications planifications) throws DTOException {
        return new PlanificationsDTO().fromEntity(planifications);
    }


    @NotNull
    @Override
    protected List<RegleGestion<PlanificationsDTO>> getReglesGestion() {
        return Collections.emptyList(); // TODO FDA 2017/07 Coder les RGs.
    }


    public int size() {
        return plan.size();
    }

    public boolean isEmpty() {
        return plan.isEmpty();
    }

    public boolean containsKey(TacheDTO key) {
        return plan.containsKey(key);
    }

    public Map<LocalDate, Double> get(TacheDTO key) {
        return plan.get(key);
    }

    public Map<LocalDate, Double> put(TacheDTO key, Map<LocalDate, Double> value) {
        return plan.put(key, value);
    }

    public Map<LocalDate, Double> remove(TacheDTO key) {
        return plan.remove(key);
    }

    public void clear() {
        plan.clear();
    }

    public Collection<Map<LocalDate, Double>> values() {
        return plan.values();
    }

    public Set<Map.Entry<TacheDTO, Map<LocalDate, Double>>> entrySet() {
        return plan.entrySet();
    }

    public boolean remove(TacheDTO key, Map<LocalDate, Double> value) {
        return plan.remove(key, value);
    }

    public Set<TacheDTO> keySet() {
        return plan.keySet();
    }

    public void forEach(BiConsumer<? super TacheDTO, ? super Map<LocalDate, Double>> action) {
        plan.forEach(action);
    }

    public void putAll(Map<? extends TacheDTO, ? extends Map<LocalDate, Double>> m) {
        plan.putAll(m);
    }

}
