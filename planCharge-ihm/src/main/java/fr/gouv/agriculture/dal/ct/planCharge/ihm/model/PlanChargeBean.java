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
    private final ObservableList<RessourceHumaineBean> ressourcesHumainesBeans;

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
        ressourcesHumainesBeans = FXCollections.observableArrayList();
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
                        .map(JourFerieBean::new)
                        .collect(Collectors.toList())
        );
        importancesBeans.setAll(
                planCharge.getReferentiels().getImportances().stream()
                        .map(ImportanceBean::new)
                        .collect(Collectors.toList())
        );
        profilsBeans.setAll(
                planCharge.getReferentiels().getProfils().stream()
                        .map(Profil::getCode)
                        .collect(Collectors.toList())
        );
        projetsApplisBeans.setAll(
                planCharge.getReferentiels().getProjetsApplis().stream()
                        .map(ProjetAppli::getCode)
                        .collect(Collectors.toList())
        );
        statutsBeans.setAll(
                planCharge.getReferentiels().getStatuts().stream()
                        .map(Statut::getCode)
                        .collect(Collectors.toList())
        );
        ressourcesHumainesBeans.clear();
        planCharge.getReferentiels().getRessourcesHumaines().stream()
                .filter(Ressource::estHumain)
                .forEach(ressource -> ressourcesHumainesBeans.add(new RessourceHumaineBean((RessourceHumaine) ressource)));
        planificationsBeans.setAll(
                planCharge.getPlanifications().entrySet().parallelStream()
                        .map(planif -> new PlanificationBean(planif.getKey(), planif.getValue()))
                        .collect(Collectors.toList())
        );
    }

    public PlanCharge extract() throws IhmException {
        Set<JourFerie> joursFeries = joursFeriesBeans.stream().map(JourFerieBean::extract).collect(Collectors.toSet());
        Set<Importance> importances = importancesBeans.stream().map(ImportanceBean::extract).collect(Collectors.toSet());
        Set<Profil> profils = profilsBeans.stream().map(Profil::new).collect(Collectors.toSet());
        Set<ProjetAppli> projetsApplis = projetsApplisBeans.stream().map(ProjetAppli::new).collect(Collectors.toSet());
        Set<Statut> statuts = statutsBeans.stream().map(Statut::new).collect(Collectors.toSet());
        Set<RessourceHumaine> ressourcesHumaines = ressourcesHumainesBeans.stream().map(RessourceHumaineBean::extract).collect(Collectors.toSet());
        Referentiels referentiels = new Referentiels(joursFeries, importances, profils, projetsApplis, statuts, ressourcesHumaines);

        Planifications planifications = extractPlanifications();

        assert dateEtat != null;
        return new PlanCharge(dateEtat, referentiels, planifications);
    }

    @NotNull
    public Planifications extractPlanifications() throws IhmException {
        Planifications planifications = new Planifications();
        for (PlanificationBean planificationBean : this.planificationsBeans) {
            Tache tache = planificationBean.getTacheBean().extract();

            Map<LocalDate, Double> calendrier = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
            Map<LocalDate, DoubleProperty> ligne = planificationBean.getCalendrier();
            ligne.forEach((date, charge) -> calendrier.put(date, charge.getValue()));

            planifications.ajouter(tache, calendrier);
        }
        return planifications;
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


    public static void copier(@NotNull PlanChargeBean planChargeOriginal, @NotNull PlanChargeBean copiePlanCharge) throws CopieException {
        LocalDate dateEtatOriginale = planChargeOriginal.getDateEtat();
        copiePlanCharge.setDateEtat((dateEtatOriginale == null) ? null : LocalDate.of(dateEtatOriginale.getYear(), dateEtatOriginale.getMonth(), dateEtatOriginale.getDayOfMonth()));
        copiePlanCharge.getPlanificationsBeans().setAll(planChargeOriginal.getPlanificationsBeans()); // FIXME FDA 2017/05 Créer des copies des TacheBean.
        copiePlanCharge.setModifie(planChargeOriginal.estModifie());
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
