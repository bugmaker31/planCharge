package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Pair;

import javax.validation.constraints.NotNull;
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
    private final TacheBean tacheBean;
    @NotNull
    private final List<Pair<LocalDate, DoubleProperty>> calendrier;
    @NotNull
    private DoubleProperty chargePlanifiee = new SimpleDoubleProperty();

    public PlanificationBean(@NotNull TacheBean tacheBean, @NotNull List<Pair<LocalDate, DoubleProperty>> calendrier) {
        super();
        this.tacheBean = tacheBean;
        this.calendrier = calendrier;

        majChargePlanifiee();
    }

    public PlanificationBean(@NotNull Tache tacheBean, @NotNull Map<LocalDate, Double> calendrier) {
        this.tacheBean = new TacheBean(tacheBean);
        this.calendrier = new ArrayList<>();
        calendrier.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> {
                    LocalDate dateSemaine = entry.getKey();
                    Double charge = entry.getValue();
                    this.calendrier.add(new Pair<>(dateSemaine, new SimpleDoubleProperty(charge)));
                });

        majChargePlanifiee();
    }

    @NotNull
    public TacheBean getTacheBean() {
        return tacheBean;
    }

    @NotNull
    public List<Pair<LocalDate, DoubleProperty>> getCalendrier() {
        return calendrier;
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
        if (noSemaine > calendrier.size()) {
            throw new IhmException("Pas de calendrier pour la semaine n°" + noSemaine + ".");
        }
        return calendrier.get(noSemaine - 1);
    }

    @NotNull
    private double chargePlanifiee() throws IhmException {
        return calendrier.stream().mapToDouble(elt -> elt.getValue().get()).sum();
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
        return tacheBean.toString() + " : " + chargePlanifiee.get();
    }
}
