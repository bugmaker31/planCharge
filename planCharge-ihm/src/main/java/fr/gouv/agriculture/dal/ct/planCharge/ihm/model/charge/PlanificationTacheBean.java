package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
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
public class
PlanificationTacheBean extends TacheBean {


    //@Autowired
    @NotNull
    private ChargeService chargeService = ChargeService.instance();


    @NotNull
    private Map<LocalDate, DoubleProperty> calendrier;
    @NotNull
    private DoubleProperty chargePlanifieeTotale = new SimpleDoubleProperty();


    public PlanificationTacheBean(@NotNull TacheBean tacheBean, @NotNull Map<LocalDate, DoubleProperty> calendrier) throws BeanException {
        super(tacheBean);
        this.calendrier = calendrier;

        majChargePlanifieeTotale();
    }

/*
    public PlanificationTacheBean(
            int id, String codeCategorie, String codeSousCategorie, String noTicketIdal, String description, String codeProjetAppli, LocalDate debut, LocalDate echeance, String codeImportance, double charge, String codeRessource, String codeProfil,
            @NotNull List<Pair<LocalDate, DoubleProperty>> calendrier
    ) throws BeanException {
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

    public PlanificationTacheBean(@NotNull TacheDTO tache, @NotNull Map<LocalDate, Double> calendrier) {
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

    // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
    public boolean aChargePlanifiee(@NotNull LocalDate dateDebutPeriode, @SuppressWarnings("unused") @NotNull LocalDate datefinPeriode) {
        return calendrier.containsKey(dateDebutPeriode);
    }


    @NotNull
    public DoubleProperty chargePlanifiee(@NotNull LocalDate dateDebutPeriode, @NotNull LocalDate datefinPeriode) throws BeanException {
        if (!aChargePlanifiee(dateDebutPeriode, datefinPeriode)) {
            throw new BeanException("Pas de calendrier pour la tâche " + noTache() + " sur la période du " + dateDebutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + " au " + datefinPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".");
        }
        return calendrier.get(dateDebutPeriode);
    }

    /**
     * Définit la charge planifiée pour la péridoe donnée.
     * <p>
     * <p>Rq : Cette méthode appelle {@link #majChargePlanifieeTotale()}. Donc inutile d'appeler {@link #majChargePlanifieeTotale()}.</p>
     *
     * @param dateDebutPeriode début de la période considérée
     * @param datefinPeriode   début de la période considérée
     * @param charge           charge planifiée
     */
    public void setChargePlanifiee(@NotNull LocalDate dateDebutPeriode, @NotNull LocalDate datefinPeriode, @NotNull Double charge) {
        if (!calendrier.containsKey(dateDebutPeriode)) {
            calendrier.put(dateDebutPeriode, new SimpleDoubleProperty());
        }
        DoubleProperty chargePlanifiee = calendrier.get(dateDebutPeriode);
        chargePlanifiee.setValue(charge);

        majChargePlanifieeTotale();
    }


    /**
     * Met à jour la charge planifiée totale (= somme des charges planifiées pour toutes les périodes couvertes par la planification de cette tâche).
     * <p>
     * Méthode à appeler à chaque fois que la planification change, pour mettre à jour la charge totale qui est planifiée pour {@link #getTacheBean() la tâche}.
     * </p>     */
    public void majChargePlanifieeTotale() {
        chargePlanifieeTotale.setValue(chargePlanifieeTotale());
    }

    private double chargePlanifieeTotale() {
        double chargePlanifiee = calendrier.values().stream()
                .mapToDouble(ObservableDoubleValue::get)
                .sum();
        return chargeService.chargeArrondie(chargePlanifiee);
    }


    @NotNull
    public PlanificationTacheBean copier(@NotNull PlanificationTacheBean original) throws CopieException {
        try {
            return new PlanificationTacheBean(
                    original.copier(),
                    calendrier // FIXME FDA 2017/05 Copier les éléments de la liste aussi.
            );
        } catch (BeanException e) {
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
