package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.MetierException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.Controlable;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.ViolationRegleGestion;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.*;
import java.util.function.*;

/**
 * Inspiré de {@link Map}.
 *
 * Created by frederic.danna on 20/03/2017.
 */
public class Planifications implements Controlable {

    public static final int NBR_SEMAINES_PLANIFIEES = 12;

    @NotNull
    private Map<Tache, Map<LocalDate, Double>> plan;

    public Planifications() {
        this.plan = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
    }

    public Planifications(@NotNull Map<Tache, Map<LocalDate, Double>> plan) {
        this.plan = plan;
    }

    @NotNull
    public Set<Tache> taches() {
        return plan.keySet();
    }

    @NotNull
    public double chargePlanifiee(@NotNull Tache tache) throws TacheSansPlanificationException {
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

    @Null
    public Tache tache(@NotNull int idTache) {
        Optional<Tache> optTache = plan.keySet().parallelStream()
                .filter(tache -> tache.getId() == idTache)
                .findAny();
        return (optTache.orElse(null));
    }

    @NotNull
    public Map<LocalDate, Double> calendrier(@NotNull Tache tache) throws TacheSansPlanificationException {
        if (!plan.containsKey(tache)) {
            throw new TacheSansPlanificationException(tache);
        }
        return plan.get(tache);
    }

    public void ajouter(@NotNull Tache tache, LocalDate dateEtat) {

        Map<LocalDate, Double> planification = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
        LocalDate dateSemaine = dateEtat;
        for (int noSemaine = 1; noSemaine <= NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            planification.put(dateSemaine, 0.0);
            dateSemaine = dateSemaine.plusDays(7);
        }

        ajouter(tache, planification);
    }

    public void ajouter(@NotNull Tache tache, Map<LocalDate, Double> calendrier) {
        plan.put(tache, calendrier);
    }


    public int size() {
        return plan.size();
    }

    public boolean isEmpty() {
        return plan.isEmpty();
    }

    public boolean containsKey(Tache key) {
        return plan.containsKey(key);
    }

    public Map<LocalDate, Double> get(Tache key) {
        return plan.get(key);
    }

    public Map<LocalDate, Double> put(Tache key, Map<LocalDate, Double> value) {
        return plan.put(key, value);
    }

    public Map<LocalDate, Double> remove(Tache key) {
        return plan.remove(key);
    }

    public void clear() {
        plan.clear();
    }

    public Collection<Map<LocalDate, Double>> values() {
        return plan.values();
    }

    public Set<Map.Entry<Tache, Map<LocalDate, Double>>> entrySet() {
        return plan.entrySet();
    }

    public boolean remove(Tache key, Map<LocalDate, Double> value) {
        return plan.remove(key, value);
    }

    public Set<Tache> keySet() {
        return plan.keySet();
    }

    public void forEach(BiConsumer<? super Tache, ? super Map<LocalDate, Double>> action) {
        plan.forEach(action);
    }

    public void putAll(Map<? extends Tache, ? extends Map<LocalDate, Double>> m) {
        plan.putAll(m);
    }


    @NotNull
    @Override
    public List<ViolationRegleGestion> controlerReglesGestion() throws MetierException {
        List<ViolationRegleGestion> violations = new ArrayList<>();
        // TODO FDA 2017/07 Coder les RG.
        return violations;
    }
}
