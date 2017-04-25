package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import com.sun.istack.internal.NotNull;
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

    @NotNull
    private final TacheBean tache;
    @NotNull
    private final List<Pair<LocalDate, DoubleProperty>> matrice;
    @NotNull
    private DoubleProperty chargePlanifiee = new SimpleDoubleProperty();

    public PlanificationBean(@NotNull TacheBean tache, @NotNull List<Pair<LocalDate, DoubleProperty>> matrice) {
        super();
        this.tache = tache;
        this.matrice = matrice;

        majChargePlanifiee();
    }

    public PlanificationBean(@NotNull Tache tache, @NotNull Map<LocalDate, Double> matrice) {
        this.tache = new TacheBean(tache);
        this.matrice = new ArrayList<>();
        matrice.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> this.matrice.add(new Pair<LocalDate, DoubleProperty>(entry.getKey(), new SimpleDoubleProperty(entry.getValue()))));

        majChargePlanifiee();
    }

    @NotNull
    public TacheBean getTache() {
        return tache;
    }

    @NotNull
    public List<Pair<LocalDate, DoubleProperty>> getMatrice() {
        return matrice;
    }

    @NotNull
    public double getChargePlanifiee() {
        return chargePlanifiee.get();
    }

    @NotNull
    public DoubleProperty chargePlanifieeProperty() {
        return chargePlanifiee;
    }

    @NotNull
    public Pair<LocalDate, DoubleProperty> chargePlanifiee(@NotNull int noSemaine) throws IhmException {
        if (noSemaine < 1) {
            throw new IhmException("Le n° de semaine doit être supérieur ou égal à 1.");
        }
        if (noSemaine > matrice.size()) {
            throw new IhmException("Pas de planification pour la semaine n°" + noSemaine + ".");
        }
        return matrice.get(noSemaine - 1);
    }

    @NotNull
    private double chargePlanifiee() throws IhmException {
        return matrice.stream().mapToDouble(elt -> elt.getValue().get()).sum();
    }

    @NotNull
    public DoubleProperty charge(@NotNull int noSemaine) {
        return chargePlanifiee(noSemaine).getValue();
    }

    @NotNull
    public void majChargePlanifiee() {
        chargePlanifiee.setValue(chargePlanifiee());
    }

    // Pour déboguer, uniquement.
    @Override
    public String toString() {
        return tache.toString() + " : " + chargePlanifiee.get();
    }
}
