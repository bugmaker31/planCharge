package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
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
 * @author frederic.danna
 */
public class PlanificationBean extends TacheBean {

    @NotNull
    private final List<Pair<LocalDate, DoubleProperty>> calendrier;
    @NotNull
    private DoubleProperty chargePlanifiee = new SimpleDoubleProperty();

    public PlanificationBean(@NotNull TacheBean tacheBean, @NotNull List<Pair<LocalDate, DoubleProperty>>calendrier) throws IhmException {
        super(tacheBean);
        this.calendrier = calendrier;

        majChargePlanifiee();
    }

/*
    public PlanificationBean(
            int id, String codeCategorie, String codeSousCategorie, String noTicketIdal, String description, String codeProjetAppli, LocalDate debut, LocalDate echeance, String codeImportance, double charge, String codeRessource, String codeProfil,
            @NotNull List<Pair<LocalDate, DoubleProperty>> calendrier
    ) throws IhmException {
        super(
                id,
                codeCategorie,
                codeSousCategorie,
                noTicketIdal,
                description,
                codeProjetAppli,
                debut,
                echeance,
                codeImportance,
                charge,
                codeRessource,
                codeProfil
        );
        this.calendrier = calendrier;

        majChargePlanifiee();
    }
*/

    public PlanificationBean(@NotNull Tache tache, @NotNull Map<LocalDate, Double> calendrier) {
        super(tache);
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
        return this;
    }

    @NotNull
    public List<Pair<LocalDate, DoubleProperty>> getCalendrier() {
        return calendrier;
    }

    public double getChargePlanifiee() {
        return chargePlanifiee.get();
    }

    @NotNull
    public DoubleProperty chargePlanifieeProperty() {
        return chargePlanifiee;
    }

    @NotNull
    public Pair<LocalDate, DoubleProperty> chargePlanifiee(int noSemaine) throws IhmException {
        if (noSemaine < 1) {
            throw new IhmException("Le n° de semaine doit être supérieur ou égal à 1.");
        }
        if (noSemaine > calendrier.size()) {
            throw new IhmException("Pas de calendrier pour la semaine n°" + noSemaine + ".");
        }
        return calendrier.get(noSemaine - 1);
    }

    private double chargePlanifiee() {
        return calendrier.stream().mapToDouble(elt -> elt.getValue().get()).sum();
    }

    @NotNull
    public DoubleProperty charge(int noSemaine) throws IhmException {
        return chargePlanifiee(noSemaine).getValue();
    }

    /**
     * Méthode à appeler à chaque fois que la planification change, pour mettre à jour la charge qui est planifiée pour {@link #getTacheBean() la tâche}.
     */
    public void majChargePlanifiee() {
        chargePlanifiee.setValue(chargePlanifiee());
    }

    // Pour déboguer, uniquement.
    @NotNull
    @Override
    public String toString() {
        return super.toString() + " : " + chargePlanifiee.get();
    }
}
