package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class PlanificationBean extends TacheBean {

    @NotNull
    private Map<LocalDate, DoubleProperty> calendrier;
    @NotNull
    private DoubleProperty chargePlanifieeTotale = new SimpleDoubleProperty();

    public PlanificationBean(@NotNull TacheBean tacheBean, @NotNull Map<LocalDate, DoubleProperty> calendrier) throws IhmException {
        super(tacheBean);
        this.calendrier = calendrier;

        majChargePlanifieeTotale();
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

        majChargePlanifieeTotale();
    }
*/

    public PlanificationBean(@NotNull TacheDTO tache, @NotNull Map<LocalDate, Double> calendrier) {
        super(tache);

        this.calendrier = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
        calendrier.forEach((debutPeriode, charge) -> this.calendrier.put(debutPeriode, new SimpleDoubleProperty(charge)));

        majChargePlanifieeTotale();
    }


    @NotNull
    public TacheBean getTacheBean() {
        return this;
    }


    @NotNull
    public Map<LocalDate, DoubleProperty> getCalendrier() {
        return calendrier;
    }


    public double getChargePlanifieeTotale() {
        return chargePlanifieeTotale.get();
    }

    @NotNull
    public DoubleProperty chargePlanifieeTotaleProperty() {
        return chargePlanifieeTotale;
    }

    @NotNull
    public boolean aChargePlanifiee(@NotNull LocalDate dateDebutPeriode) {
        return calendrier.containsKey(dateDebutPeriode);
    }

    @NotNull
    public DoubleProperty chargePlanifiee(@NotNull LocalDate dateDebutPeriode) throws IhmException {
        if (!aChargePlanifiee(dateDebutPeriode)) {
            throw new IhmException("Pas de calendrier pour la tâche " + noTache() + " sur la période qui commence le " + dateDebutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".");
        }
        return calendrier.get(dateDebutPeriode);
    }

    private double chargePlanifieeTotale() {
        return calendrier.values().stream()
                .mapToDouble(ObservableDoubleValue::get)
                .sum();
    }

    /**
     * Méthode à appeler à chaque fois que la planification change, pour mettre à jour la charge totale qui est planifiée pour {@link #getTacheBean() la tâche}.
     */
    public void majChargePlanifieeTotale() {
        chargePlanifieeTotale.setValue(chargePlanifieeTotale());
    }


    @NotNull
    public PlanificationBean copier(@NotNull PlanificationBean original) throws CopieException {
        try {
            return new PlanificationBean(
                    original.copier(),
                    getCalendrier() // FIXME FDA 2017/05 Copier les éléments de la liste aussi.
            );
        } catch (IhmException e) {
            throw new CopieException("Impossible de copier.", e);
        }
    }


    // Pour déboguer, uniquement.
    @NotNull
    @Override
    public String toString() {
        return super.toString() + " : " + chargePlanifieeTotale.get();
    }
}
