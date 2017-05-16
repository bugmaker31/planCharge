package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 28/04/2017.
 */
public class PlanChargeBean {

    private static PlanChargeBean instance;

    public static PlanChargeBean instance() {
        if (instance == null) {
            instance = new PlanChargeBean();
        }
        return instance;
    }

    @NotNull
    private boolean isModifie;

    @Null
    private LocalDate dateEtat;

    @Null
    public LocalDate getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(@Null LocalDate dateEtat) {
        this.dateEtat = dateEtat;
    }

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @Null
    private final ObservableList<PlanificationBean> planificationsBeans;

    @Null
    public ObservableList<PlanificationBean> getPlanificationsBeans() {
        return planificationsBeans;
    }

/*
Personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    public void setPlanificationsBeans(ObservableList<PlanificationBean> planificationsBeans) {
        this.planificationsBeans = planificationsBeans;
    }
*/

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private PlanChargeBean() {
        super();
        dateEtat = null;
        planificationsBeans = FXCollections.observableArrayList();
        isModifie = false;

/*
        planificationsBeans.addListener(
                (ListChangeListener<? super PlanificationBean>) change -> necessiteEtreSauvegarde()
        );
*/
    }

    public void vientDEtreCharge() {
        this.isModifie = false;
    }

    public void vientDEtreSauvegarde() {
        this.isModifie = false;
    }

    public void vientDEtreModifie() {
        this.isModifie = true;
    }

    public boolean necessiteEtreSauvegarde() {
        return isModifie;
    }

    public void init(PlanCharge planCharge) {
        dateEtat = planCharge.getDateEtat();
        planificationsBeans.setAll(
                planCharge.getPlanifications().entrySet().stream()
                        .map(planif -> new PlanificationBean(planif.getKey(), planif.getValue()))
                        .collect(Collectors.toList())
        );
    }

    public PlanCharge extract() throws IhmException {

        Planifications planifications = new Planifications();
        for (PlanificationBean planificationBean : planificationsBeans) {
            Tache tache = planificationBean.getTacheBean().extract();
            Map<LocalDate, Double> calendrier = new HashMap<>();
            List<Pair<LocalDate, DoubleProperty>> ligne = planificationBean.getCalendrier();
            ligne.forEach(semaine -> calendrier.put(semaine.getKey(), semaine.getValue().doubleValue()));

            planifications.ajouter(tache, calendrier);
        }

        PlanCharge planCharge = new PlanCharge(dateEtat, planifications);
        return planCharge;
    }
}
