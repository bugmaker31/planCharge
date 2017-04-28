package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.function.*;

/**
 * Created by frederic.danna on 20/03/2017.
 */
public class Planifications implements Map<Tache, Map<LocalDate, Double>> {

    public static final int NBR_SEMAINES_PLANIFIEES = 12;

    @NotNull
    private Map<Tache, Map<LocalDate, Double>> plan;

    public Planifications() {
        this.plan = new HashMap<>();
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

    @NotNull
    public Tache tache(@NotNull int idTache) {
        // TODO FDA 2017/03 Trouver une meilleure façon de faire (pas codé à la main, plus rapide, etc.)
        for (Tache tache : plan.keySet()) {
            if (tache.getId() == idTache) {
                return tache;
            }
        }
        return null;
    }

    @NotNull
    public Map<LocalDate, Double> calendrier(@NotNull Tache tache) throws TacheSansPlanificationException {
        if (!plan.containsKey(tache)) {
            throw new TacheSansPlanificationException(tache);
        }
        return plan.get(tache);
    }

    public void ajouter(@NotNull Tache tache, LocalDate dateEtat) {

        Map<LocalDate, Double> planification = new HashMap<>(NBR_SEMAINES_PLANIFIEES);
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

    public boolean containsKey(Object key) {
        return plan.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return plan.containsValue(value);
    }

    public Map<LocalDate, Double> get(Object key) {
        return plan.get(key);
    }

    public Map<LocalDate, Double> put(Tache key, Map<LocalDate, Double> value) {
        return plan.put(key, value);
    }

    public Map<LocalDate, Double> remove(Object key) {
        return plan.remove(key);
    }

    public void putAll(Map<? extends Tache, ? extends Map<LocalDate, Double>> m) {
        plan.putAll(m);
    }

    public void clear() {
        plan.clear();
    }

    public Set<Tache> keySet() {
        return plan.keySet();
    }

    public Collection<Map<LocalDate, Double>> values() {
        return plan.values();
    }

    public Set<Map.Entry<Tache, Map<LocalDate, Double>>> entrySet() {
        return plan.entrySet();
    }

    public Map<LocalDate, Double> getOrDefault(Object key, Map<LocalDate, Double> defaultValue) {
        return plan.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super Tache, ? super Map<LocalDate, Double>> action) {
        plan.forEach(action);
    }

    public void replaceAll(BiFunction<? super Tache, ? super Map<LocalDate, Double>, ? extends Map<LocalDate, Double>> function) {
        plan.replaceAll(function);
    }

    public Map<LocalDate, Double> putIfAbsent(Tache key, Map<LocalDate, Double> value) {
        return plan.putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return plan.remove(key, value);
    }

    public boolean replace(Tache key, Map<LocalDate, Double> oldValue, Map<LocalDate, Double> newValue) {
        return plan.replace(key, oldValue, newValue);
    }

    public Map<LocalDate, Double> replace(Tache key, Map<LocalDate, Double> value) {
        return plan.replace(key, value);
    }

    public Map<LocalDate, Double> computeIfAbsent(Tache key, Function<? super Tache, ? extends Map<LocalDate, Double>> mappingFunction) {
        return plan.computeIfAbsent(key, mappingFunction);
    }

    public Map<LocalDate, Double> computeIfPresent(Tache key, BiFunction<? super Tache, ? super Map<LocalDate, Double>, ? extends Map<LocalDate, Double>> remappingFunction) {
        return plan.computeIfPresent(key, remappingFunction);
    }

    public Map<LocalDate, Double> compute(Tache key, BiFunction<? super Tache, ? super Map<LocalDate, Double>, ? extends Map<LocalDate, Double>> remappingFunction) {
        return plan.compute(key, remappingFunction);
    }

    public Map<LocalDate, Double> merge(Tache key, Map<LocalDate, Double> value, BiFunction<? super Map<LocalDate, Double>, ? super Map<LocalDate, Double>, ? extends Map<LocalDate, Double>> remappingFunction) {
        return plan.merge(key, value, remappingFunction);
    }
}
