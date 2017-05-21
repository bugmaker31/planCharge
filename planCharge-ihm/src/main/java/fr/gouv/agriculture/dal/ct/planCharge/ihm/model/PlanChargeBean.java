package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.Copiable;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 28/04/2017.
 *
 * @author frederic.danna
 */
public final class PlanChargeBean implements Copiable<PlanChargeBean> {

    private final static PlanChargeBean INSTANCE = new PlanChargeBean();
    public static PlanChargeBean instance() {
        return INSTANCE;
    }


    private boolean isModifie;

    public boolean isModifie() {
        return isModifie;
    }

    private void setModifie(boolean modifie) {
        isModifie = modifie;
    }


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
    @NotNull
    private final ObservableList<PlanificationBean> planificationsBeans;

    @NotNull
    public ObservableList<PlanificationBean> getPlanificationsBeans() {
        return planificationsBeans;
    }


    /*
    NB : Tout attribut ajouté à cette classe doit être répercuté dans la méthode {@link #copier(PlanChargeBean, PlanChargeBean)}.
    */


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private PlanChargeBean() {
        super();
        dateEtat = null;
        planificationsBeans = FXCollections.observableArrayList();
        isModifie = false;
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
        setDateEtat(planCharge.getDateEtat());
        planificationsBeans.setAll(
                planCharge.getPlanifications().entrySet().parallelStream()
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

        assert dateEtat != null;
        return new PlanCharge(dateEtat, planifications);
    }


    @Override
    @NotNull
    public PlanChargeBean copier() throws CopieException {
        PlanChargeBean copie = new PlanChargeBean();
        copier(this, copie);
        return copie;
    }

    static public void copier(@NotNull PlanChargeBean pcbSource, @NotNull PlanChargeBean pcbDest) throws CopieException {
        pcbDest.setDateEtat(pcbSource.getDateEtat());
        pcbDest.getPlanificationsBeans().setAll(pcbSource.getPlanificationsBeans());
        pcbDest.setModifie(pcbSource.isModifie());
        /* Ajouter ici la copie des nouveaux attributs. */
    }


    // Pour déboguer, uniquement.
    @Override
    public String toString() {
        //noinspection StringConcatenationMissingWhitespace
        return "[" + (dateEtat == null ? "N/A" : dateEtat.format(DateTimeFormatter.ISO_DATE)) + "]"
                + " " + getPlanificationsBeans().size() + " tâches"
                + " (" + (isModifie ? "" : "non ") + "modifié)";
    }

}
