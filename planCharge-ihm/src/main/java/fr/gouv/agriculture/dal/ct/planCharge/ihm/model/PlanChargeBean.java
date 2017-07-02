package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.Copiable;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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


    private boolean estModifie;

    public boolean estModifie() {
        return estModifie;
    }

    private void setModifie(boolean estModifie) {
        this.estModifie = estModifie;
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
    private final ObservableList<JourFerieBean> joursFeriesBeans;

    @NotNull
    public ObservableList<JourFerieBean> getJoursFeriesBeans() {
        return joursFeriesBeans;
    }

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<ImportanceBean> importancesBeans;

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<String> profilsBeans;

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<String> projetsApplisBeans;

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<String> statutsBeans;

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<RessourceHumaineBean> ressourcesBeans;

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
        joursFeriesBeans = FXCollections.observableArrayList();
        importancesBeans = FXCollections.observableArrayList();
        profilsBeans = FXCollections.observableArrayList();
        projetsApplisBeans = FXCollections.observableArrayList();
        statutsBeans = FXCollections.observableArrayList();
        ressourcesBeans = FXCollections.observableArrayList();
        planificationsBeans = FXCollections.observableArrayList();
        estModifie = false;
    }


    public void vientDEtreCharge() {
        this.estModifie = false;
    }

    public void vientDEtreSauvegarde() {
        this.estModifie = false;
    }

    public void vientDEtreModifie() {
        this.estModifie = true;
    }

    public boolean aBesoinEtreSauvegarde() {
        return estModifie;
    }


    public void init(PlanCharge planCharge) {
        setDateEtat(planCharge.getDateEtat());
        joursFeriesBeans.setAll(
                planCharge.getReferentiels().getJoursFeries().stream()
                        .map(jourFerie -> new JourFerieBean(jourFerie))
                        .collect(Collectors.toSet())
        );
        planificationsBeans.setAll(
                planCharge.getPlanifications().entrySet().parallelStream()
                        .map(planif -> new PlanificationBean(planif.getKey(), planif.getValue()))
                        .collect(Collectors.toList())
        );
    }

    public PlanCharge extract() throws IhmException {
        Set<JourFerie> joursFeries = joursFeriesBeans.parallelStream().map(JourFerieBean::extract).collect(Collectors.toSet());
        Set<Importance> importances = importancesBeans.parallelStream().map(ImportanceBean::extract).collect(Collectors.toSet());
        Set<Profil> profils = profilsBeans.parallelStream().map(Profil::new).collect(Collectors.toSet());
        Set<ProjetAppli> projetsApplis = projetsApplisBeans.parallelStream().map(ProjetAppli::new).collect(Collectors.toSet());
        Set<Statut> statuts = statutsBeans.parallelStream().map(Statut::new).collect(Collectors.toSet());
        Set<Ressource> ressources = ressourcesBeans.parallelStream().map(RessourceHumaineBean::extract).collect(Collectors.toSet());
        Referentiels referentiels = new Referentiels(joursFeries, importances, profils, projetsApplis, statuts, ressources);

        Planifications planifications = new Planifications();
        for (PlanificationBean planificationBean : planificationsBeans) {
            Tache tache = planificationBean.getTacheBean().extract();

            Map<LocalDate, Double> calendrier = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
            Map<LocalDate, DoubleProperty> ligne = planificationBean.getCalendrier();
            ligne.forEach((date, charge) -> calendrier.put(date, charge.getValue()));

            planifications.ajouter(tache, calendrier);
        }

        assert dateEtat != null;
        return new PlanCharge(dateEtat, referentiels, planifications);
    }


    @NotNull
    public static PlanChargeBean copier(PlanChargeBean original) throws CopieException {
        PlanChargeBean copie = new PlanChargeBean();
        copier(original, copie);
        return copie;
    }

    @Override
    public PlanChargeBean copier() throws CopieException {
        return copier(this);
    }

    public static void copier(@NotNull PlanChargeBean original, @NotNull PlanChargeBean copie) throws CopieException {
        copie.setDateEtat(original.getDateEtat()); // FIXME FDA 2017/05 Créer une copie de la LocalDate.
        copie.getPlanificationsBeans().setAll(original.getPlanificationsBeans()); // FIXME FDA 2017/05 Créer des copies des TacheBean aussi.
        copie.setModifie(original.estModifie());
        /* Ajouter ici la copie des nouveaux attributs. */
    }


    // Pour déboguer, uniquement.
    @Override
    public String toString() {
        //noinspection StringConcatenationMissingWhitespace
        return "[" + (dateEtat == null ? "N/A" : dateEtat.format(DateTimeFormatter.ISO_DATE)) + "]"
                + " " + getPlanificationsBeans().size() + " tâches"
                + " (" + (estModifie ? "" : "non ") + "modifié)";
    }

}
