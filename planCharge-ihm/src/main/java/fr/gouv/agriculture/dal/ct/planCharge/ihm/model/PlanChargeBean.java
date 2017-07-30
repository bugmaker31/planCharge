package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.*;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 28/04/2017.
 *
 * @author frederic.danna
 */
public final class PlanChargeBean extends AbstractBean<PlanChargeDTO, PlanChargeBean> implements Copiable<PlanChargeBean> {

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

    @NotNull
    public ObservableList<JourFerieBean> getJoursFeriesBeans() {
        return joursFeriesBeans;
    }

    @NotNull
    public ObservableList<RessourceHumaineBean> getRessourcesHumainesBeans() {
        return ressourcesHumainesBeans;
    }


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


    @NotNull
    public PlanChargeBean fromDto(@NotNull PlanChargeDTO planCharge) {
        setDateEtat(planCharge.getDateEtat());
        joursFeriesBeans.setAll(
                planCharge.getReferentiels().getJoursFeries().stream()
                        .map(jourFerieDTO -> new JourFerieBean().fromDto(jourFerieDTO))
                        .collect(Collectors.toList())
        );
        importancesBeans.setAll(
                planCharge.getReferentiels().getImportances().stream()
                        .map(importanceDTO -> new ImportanceBean().fromDto(importanceDTO))
                        .collect(Collectors.toList())
        );
        profilsBeans.setAll(
                planCharge.getReferentiels().getProfils().stream()
                        .map(ProfilDTO::getCode)
                        .collect(Collectors.toList())
        );
        projetsApplisBeans.setAll(
                planCharge.getReferentiels().getProjetsApplis().stream()
                        .map(ProjetAppliDTO::getCode)
                        .collect(Collectors.toList())
        );
        statutsBeans.setAll(
                planCharge.getReferentiels().getStatuts().stream()
                        .map(StatutDTO::getCode)
                        .collect(Collectors.toList())
        );
        ressourcesHumainesBeans.clear();
        planCharge.getReferentiels().getRessourcesHumaines().stream()
                .filter(RessourceDTO::estHumain)
                .forEach(ressource -> ressourcesHumainesBeans.add(new RessourceHumaineBean().fromDto(ressource)));
        planificationsBeans.setAll(
                planCharge.getPlanifications().entrySet().parallelStream()
                        .map(planif -> new PlanificationBean(planif.getKey(), planif.getValue()))
                        .collect(Collectors.toList())
        );
        return this;
    }

    @NotNull
    @Override
    public PlanChargeDTO toDto() throws BeanException {
        List<JourFerieDTO> joursFeries = joursFeriesBeans.stream().map(JourFerieBean::toDTO).collect(Collectors.toList());
        List<ImportanceDTO> importances = importancesBeans.stream().map(ImportanceBean::toDTO).collect(Collectors.toList());
        List<ProfilDTO> profils = profilsBeans.stream().map(ProfilDTO::new).collect(Collectors.toList());
        List<ProjetAppliDTO> projetsApplis = projetsApplisBeans.stream().map(ProjetAppliDTO::new).collect(Collectors.toList());
        List<StatutDTO> statuts = statutsBeans.stream().map(StatutDTO::new).collect(Collectors.toList());
        List<RessourceHumaineDTO> ressourcesHumaines = ressourcesHumainesBeans.stream().map(RessourceHumaineBean::toDTO).collect(Collectors.toList());
        ReferentielsDTO referentiels = new ReferentielsDTO(joursFeries, importances, profils, projetsApplis, statuts, ressourcesHumaines);

        PlanificationsDTO planifications = toPlanificationDTOs();

        assert dateEtat != null;
        return new PlanChargeDTO(dateEtat, referentiels, planifications);
    }

    @NotNull
    public PlanificationsDTO toPlanificationDTOs() throws BeanException {
        PlanificationsDTO planifications = new PlanificationsDTO();
        for (PlanificationBean planificationBean : this.planificationsBeans) {
            TacheDTO tache = planificationBean.getTacheBean().extract();

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
