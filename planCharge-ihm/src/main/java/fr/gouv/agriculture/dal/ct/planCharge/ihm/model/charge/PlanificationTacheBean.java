package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class PlanificationTacheBean extends TacheBean {


    private static final Logger LOGGER = LoggerFactory.getLogger(PlanificationTacheBean.class);


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

    public PlanificationTacheBean(@NotNull TacheDTO tache, @NotNull Map<LocalDate, Double> calendrierDTO) {
        super(tache);

        calendrier = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
        calendrierDTO.forEach((debutPeriode, charge) -> calendrier.put(debutPeriode, new SimpleDoubleProperty(charge)));

        majChargePlanifieeTotale();
    }

    public PlanificationTacheBean(@NotNull TacheBean tacheBean) throws BeanException {
        this(tacheBean, new TreeMap<>()); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
    }

    public PlanificationTacheBean(@NotNull TacheDTO tache) {
        this(tache, new TreeMap<>()); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
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


    @NotNull
    public TacheBean getTacheBean() {
        return this;
    }

/*
    @NotNull
    public Map<LocalDate, DoubleProperty> getCalendrier() {
        return calendrier;
    }
*/

    // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
    public boolean aChargePlanifiee(@NotNull LocalDate dateDebutPeriode/*, @NotNull LocalDate datefinPeriode*/) {
        return calendrier.containsKey(dateDebutPeriode) && (calendrier.get(dateDebutPeriode) != null);
    }

    /**
     * <b>NB</b> : pas trié.
     *
     * @return
     */
    @NotNull
    public Set<LocalDate> getDebutsPeriodesPlanifiees() {
        return calendrier.keySet();
    }

    @Null
    public DoubleProperty chargePlanifieePropertyOuNull(@NotNull LocalDate dateDebutPeriode/*, @NotNull LocalDate datefinPeriode*/) {
        return calendrier.get(dateDebutPeriode);
    }

    @NotNull
    public DoubleProperty chargePlanifieeProperty(@NotNull LocalDate dateDebutPeriode/*, @NotNull LocalDate datefinPeriode*/) throws BeanException {
        if (!calendrier.containsKey(dateDebutPeriode)) {
            throw new BeanException("Pas de calendrier pour la tâche " + noTache() + " sur la période du " + dateDebutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE)
//                    + " au " + datefinPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + ".");
        }
        return calendrier.get(dateDebutPeriode);
    }

    @Null
    public Double chargePlanifiee(@NotNull LocalDate dateDebutPeriode/*, @NotNull LocalDate datefinPeriode*/) throws BeanException {
        return chargePlanifieeProperty(dateDebutPeriode).getValue();
    }

/*
    @Null
    public Double chargePlanifieeOuNull(@NotNull LocalDate dateDebutPeriode*/
/*, @NotNull LocalDate datefinPeriode*//*
) {
        DoubleProperty chargeProperty = chargePlanifieePropertyOuNull(dateDebutPeriode);
        if (chargeProperty == null) {
            return null;
        }
        return chargeProperty.getValue();
    }
*/


    /**
     * Définit la charge planifiée pour la péridoe donnée.
     * <p>Rq : Cette méthode appelle {@link #majChargePlanifieeTotale()}. Donc inutile d'appeler {@link #majChargePlanifieeTotale()}.</p>
     *
     * @param debutPeriode début de la période considérée
     * @param finPeriode   fin de la période considérée
     * @param charge       charge planifiée
     */
    public void setChargePlanifiee(@NotNull LocalDate debutPeriode/*, @NotNull LocalDate finPeriode*/, @Null Double charge) {

/*
        if (charge == null) {
            calendrier.remove(debutPeriode);
        } else {
*/

        Double ancienneCharge;

        DoubleProperty chargePlanifieeProperty;
        if (calendrier.containsKey(debutPeriode)) {
            chargePlanifieeProperty = calendrier.get(debutPeriode);
            ancienneCharge = chargePlanifieeProperty.getValue();
        } else {
            chargePlanifieeProperty = new SimpleDoubleProperty();
            calendrier.put(debutPeriode, chargePlanifieeProperty);
            ancienneCharge = null;
        }

        if (!Objects.equals(ancienneCharge, charge)) {

            chargePlanifieeProperty.setValue(charge);
            LOGGER.debug("Planification tâche {} ++ période {} : charge (j) {} -> {}.", noTache(), debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE), ancienneCharge, charge);

            if (!(
                    (fr.gouv.agriculture.dal.ct.planCharge.util.Objects.value(ancienneCharge, 0.0) == 0.0)
                            && (fr.gouv.agriculture.dal.ct.planCharge.util.Objects.value(charge, 0.0) == 0.0)
            )) { // Mathématiquement parlant, 'null' vaut zéro, donc pas besoin de recalculer.
                majChargePlanifieeTotale();
            }
        }
    }

    public double getChargePlanifieeTotale() {
        return chargePlanifieeTotale.get();
    }

    @NotNull
    public DoubleProperty chargePlanifieeTotaleProperty() {
        return chargePlanifieeTotale;
    }


    /**
     * Met à jour la charge planifiée totale (= somme des charges planifiées pour toutes les périodes couvertes par la planification de cette tâche).
     * <p>
     * Méthode à appeler à chaque fois que la planification change, pour mettre à jour la charge totale qui est planifiée pour {@link #getTacheBean() la tâche}.
     * </p>
     */
    public void majChargePlanifieeTotale() {
        chargePlanifieeTotale.setValue(chargePlanifieeTotale());
    }

    private double chargePlanifieeTotale() {
        double chargePlanifiee = calendrier.values().parallelStream()
//                .mapToDouble(ObservableDoubleValue::get)
                .filter(Objects::nonNull)
                .mapToDouble(DoubleExpression::getValue)
                .sum();
        return chargeService.chargeArrondie(chargePlanifiee);
    }


    @NotNull
    public PlanificationTacheBean copier(@NotNull PlanificationTacheBean original) throws CopieException {
        try {
            return new PlanificationTacheBean(
                    original.copier(),
                    new TreeMap<>(original.calendrier) // TreeMap au lieu de HashMap pour trier, juste afin de faciliter le débogage.calendrier
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
