package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanificationBean {

    private final TacheBean tache;
    private final List<Pair<LocalDate, DoubleProperty>> matrice;
    private DoubleProperty chargePlanifiee = new SimpleDoubleProperty();

    public PlanificationBean(TacheBean tache, List<Pair<LocalDate, DoubleProperty>> matrice) {
        super();
        this.tache = tache;
        this.matrice = matrice;

        majChargePlanifiee();
    }

    public PlanificationBean(Tache tache, Map<LocalDate, Double> matrice) {
        this.tache = new TacheBean(tache);
        this.matrice = new ArrayList<>();
        matrice.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> this.matrice.add(new Pair<LocalDate, DoubleProperty>(entry.getKey(), new SimpleDoubleProperty(entry.getValue()))));

        majChargePlanifiee();
    }

    public TacheBean getTache() {
        return tache;
    }

    public List<Pair<LocalDate, DoubleProperty>> getMatrice() {
        return matrice;
    }

    public double getChargePlanifiee() {
        return chargePlanifiee.get();
    }

    public DoubleProperty chargePlanifieeProperty() {
        return chargePlanifiee;
    }

    public Pair<LocalDate, DoubleProperty> chargePlanifiee(int noSemaine) throws IhmException {
        if (noSemaine < 1) {
            throw new IhmException("Le n° de semaine doit être supérieur ou égal à 1.");
        }
        if (noSemaine > matrice.size()) {
            throw new IhmException("Pas de planification pour la semaine n°" + noSemaine + ".");
        }
        return matrice.get(noSemaine - 1);
    }

    private double chargePlanifiee() throws IhmException {
        final double[] chargePlanifiee = {0.0};
        matrice.parallelStream().forEach(elt -> chargePlanifiee[0] += (elt.getValue().get()));
        return chargePlanifiee[0];
    }

    public DoubleProperty charge(int noSemaine) {
        return chargePlanifiee(noSemaine).getValue();
    }

    public void majChargePlanifiee() {
        chargePlanifiee.setValue(chargePlanifiee());
    }

    // Pour déboguer, uniquement.
    @Override
    public String toString() {
        return tache.toString() + " : " + chargePlanifiee.get();
    }
}
