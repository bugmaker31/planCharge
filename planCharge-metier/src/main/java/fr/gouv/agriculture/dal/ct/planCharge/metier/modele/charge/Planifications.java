package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.function.*;

/**
 * Inspiré de {@link Map}.
 *
 * Created by frederic.danna on 20/03/2017.
 */
@SuppressWarnings({"ClassWithoutLogger", "ClassHasNoToStringMethod"})
public class Planifications extends AbstractEntity<Serializable, Planifications> {

    public static final int NBR_SEMAINES_PLANIFIEES = 12; // 12 semaines, donc 3 mois.

    @NotNull
    private final Map<Tache, Map<LocalDate, Double>> plan;


    public Planifications() {
        super();
        this.plan = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
    }

    public Planifications(@NotNull Map<Tache, Map<LocalDate, Double>> plan) {
        this.plan = plan;
    }


    @NotNull
    @Override
    public Serializable getIdentity() {
        return null; // TODO FDA 2017/07 Trouver mieux comme code.
    }

    @Override
    public int compareTo(@NotNull Planifications o) {
        return 0; // TODO FDA 2017/07 Trouver mieux comme code.
    }


    @NotNull
    public Set<Tache> taches() {
        return plan.keySet();
    }

    public double chargePlanifiee(@NotNull Tache tache) throws TacheSansPlanificationException {
        double chargePlanifiee;

        Map<LocalDate, Double> calendrier = calendrier(tache);
        if (calendrier == null) {
            chargePlanifiee = 0.0;
        } else {
            chargePlanifiee = calendrier.values().parallelStream()
                    .mapToDouble(Double::doubleValue)
                    .sum();
        }

        return chargePlanifiee;
    }

    @Null
    public Tache tache(int idTache) {
        Optional<Tache> optTache = plan.keySet().parallelStream()
                .filter(tache -> tache.getId() == idTache)
                .findAny();
        return (optTache.orElse(null));
    }

    @Null
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
            dateSemaine = dateSemaine.plusDays(7); // TODO [issue#26:PeriodeHebdo/Trim]
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

}
