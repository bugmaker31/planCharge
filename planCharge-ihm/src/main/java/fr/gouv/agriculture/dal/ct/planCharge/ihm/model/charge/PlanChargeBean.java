package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursAbsenceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.PctagesDispoCTBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.*;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.Copiable;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

    // Statics :

    private static final PlanChargeBean INSTANCE = new PlanChargeBean();

    public static PlanChargeBean instance() {
        return INSTANCE;
    }

    // Fields :

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<ProfilBean> profilsBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<ProjetAppliBean> projetsApplisBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<StatutBean> statutsBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<JourFerieBean> joursFeriesBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<ImportanceBean> importancesBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<RessourceBean> ressourcesBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<RessourceHumaineBean> ressourcesHumainesBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableMap, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableMap, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<PctagesDispoCTBean> pctagesDispoCTBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<PlanificationTacheBean> planificationsBeans = FXCollections.observableArrayList();
    @Null
    private LocalDate dateEtat = null;
    private boolean modifie = false;


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private PlanChargeBean() {
        super();

        ressourcesBeans.addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        ressourcesHumainesBeans.add(ressourceHumaineBean);
                    }
                }
                if (change.wasRemoved()) {
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        ressourcesHumainesBeans.remove(ressourceHumaineBean);
                    }
                }
            }
        });
    }

    @NotNull
    public static PlanChargeBean copier(@NotNull PlanChargeBean original) throws CopieException {
        PlanChargeBean copie = new PlanChargeBean();
        copier(original, copie);
        return copie;
    }

    /*
    NB : Tout attribut ajouté à cette classe doit être répercuté dans la méthode {@link #copier(PlanChargeBean, PlanChargeBean)}.
    */

    public static void copier(@NotNull PlanChargeBean planChargeOriginal, @NotNull PlanChargeBean copiePlanCharge) throws CopieException {
        LocalDate dateEtatOriginale = planChargeOriginal.getDateEtat();
        copiePlanCharge.setDateEtat((dateEtatOriginale == null) ? null : LocalDate.of(dateEtatOriginale.getYear(), dateEtatOriginale.getMonth(), dateEtatOriginale.getDayOfMonth()));
        copiePlanCharge.getPlanificationsBeans().setAll(planChargeOriginal.getPlanificationsBeans()); // FIXME FDA 2017/05 Créer des copies des TacheBean.
        copiePlanCharge.setModifie(planChargeOriginal.estModifie());
        /* Ajouter ici la copie des nouveaux attributs. */
    }

    @Null
    public LocalDate getDateEtat() {
        return dateEtat;
    }

    @NotNull
    public LocalDate dateEtat() throws IhmException {
        if (dateEtat == null) {
            throw new IhmException("Date d'état non définie.");
        }
        return dateEtat;
    }

    public void setDateEtat(@Null LocalDate dateEtat) {
        this.dateEtat = dateEtat;
    }

    public boolean estModifie() {
        return modifie;
    }

    @NotNull
    public ObservableList<JourFerieBean> getJoursFeriesBeans() {
        return joursFeriesBeans;
    }

    @NotNull
    public ObservableList<ImportanceBean> getImportancesBeans() {
        return importancesBeans;
    }

    @NotNull
    public ObservableList<ProfilBean> getProfilsBeans() {
        return profilsBeans;
    }

    @NotNull
    public ObservableList<ProjetAppliBean> getProjetsApplisBeans() {
        return projetsApplisBeans;
    }

    @NotNull
    public ObservableList<StatutBean> getStatutsBeans() {
        return statutsBeans;
    }

    @NotNull
    public ObservableList<RessourceBean> getRessourcesBeans() {
        return ressourcesBeans;
    }

    @NotNull
    public ObservableList<RessourceHumaineBean> getRessourcesHumainesBeans() {
        return ressourcesHumainesBeans;
    }

    @NotNull
    public ObservableList<NbrsJoursAbsenceBean> getNbrsJoursAbsenceBeans() {
        return nbrsJoursAbsenceBeans;
    }

    @NotNull
    public ObservableList<PlanificationTacheBean> getPlanificationsBeans() {
        return planificationsBeans;
    }

    public boolean isModifie() {
        return modifie;
    }

    private void setModifie(boolean estModifie) {
        this.modifie = estModifie;
    }

    public void vientDEtreCharge() {
        this.modifie = false;
    }

    public void vientDEtreSauvegarde() {
        this.modifie = false;
    }

    public void vientDEtreModifie() {
        this.modifie = true;
    }

    public boolean aBesoinEtreSauvegarde() {
        return modifie;
    }

    @NotNull
    public PlanChargeBean fromDto(@NotNull PlanChargeDTO planCharge) {
        setDateEtat(planCharge.getDateEtat());
        // Référentiels :
        joursFeriesBeans.setAll(
                planCharge.getReferentiels().getJoursFeries().stream()
                        .map(JourFerieBean::from)
                        .collect(Collectors.toList())
        );
        importancesBeans.setAll(
                planCharge.getReferentiels().getImportances().stream()
                        .map(ImportanceBean::from)
                        .collect(Collectors.toList())
        );
        profilsBeans.setAll(
                planCharge.getReferentiels().getProfils().stream()
                        .map(ProfilBean::from)
                        .collect(Collectors.toList())
        );
        projetsApplisBeans.setAll(
                planCharge.getReferentiels().getProjetsApplis().stream()
                        .map(ProjetAppliBean::from)
                        .collect(Collectors.toList())
        );
        statutsBeans.setAll(
                planCharge.getReferentiels().getStatuts().stream()
                        .map(StatutBean::from)
                        .collect(Collectors.toList())
        );
        ressourcesBeans.setAll(
                planCharge.getReferentiels().getRessourcesHumaines().stream()
                        .map(RessourceHumaineBean::from)
                        .collect(Collectors.toList())
        );
        ressourcesBeans.addAll(
                Arrays.stream(RessourceGeneriqueDTO.values())
                        .map(RessourceGeneriqueBean::from)
                        .collect(Collectors.toList())
        );
        // Disponibilités :
        { // Nbrs de jours d'absence :
            nbrsJoursAbsenceBeans.clear();
            Map<RessourceHumaineDTO, Map<LocalDate, Float>> absencesDTO = planCharge.getDisponibilites().getNbrsJoursAbsence();
            for (RessourceHumaineDTO ressourceHumaineDTO : absencesDTO.keySet()) {
                Map<LocalDate, FloatProperty> calendrierAbsences = absencesDTO.get(ressourceHumaineDTO).keySet().stream()
                        .collect(Collectors.toMap(locaDate -> locaDate, localDate -> new SimpleFloatProperty(absencesDTO.get(ressourceHumaineDTO).get(localDate))));
                nbrsJoursAbsenceBeans.add(new NbrsJoursAbsenceBean(RessourceHumaineBean.from(ressourceHumaineDTO), calendrierAbsences));
            }
        }
        { // Pctages de dispo pour la CT :
            pctagesDispoCTBeans.clear();
            Map<RessourceHumaineDTO, Map<LocalDate, Percentage>> pctagesDispoCT = planCharge.getDisponibilites().getPctagesDispoCT();
            for (RessourceHumaineDTO ressourceHumaineDTO : pctagesDispoCT.keySet()) {
                Map<LocalDate, PercentageProperty> calendrierAbsences = pctagesDispoCT.get(ressourceHumaineDTO).keySet().stream()
                        .collect(Collectors.toMap(locaDate -> locaDate, localDate -> new PercentageProperty(pctagesDispoCT.get(ressourceHumaineDTO).get(localDate).floatValue())));
                pctagesDispoCTBeans.add(new PctagesDispoCTBean(RessourceHumaineBean.from(ressourceHumaineDTO), calendrierAbsences));
            }
        }
        // Tâches + Charge :
        planificationsBeans.setAll(
                planCharge.getPlanifications().entrySet().parallelStream()
                        .map(planif -> new PlanificationTacheBean(planif.getKey(), planif.getValue()))
                        .collect(Collectors.toList())
        );
        return this;
    }

    @NotNull
    @Override
    public PlanChargeDTO toDto() throws BeanException {

        // Référentiels :
        List<JourFerieDTO> joursFeries = joursFeriesBeans.stream().map(JourFerieBean::to).collect(Collectors.toList());
        List<ImportanceDTO> importances = importancesBeans.stream().map(ImportanceBean::to).collect(Collectors.toList());
        List<ProfilDTO> profils = profilsBeans.stream().map(ProfilBean::to).collect(Collectors.toList());
        List<ProjetAppliDTO> projetsApplis = projetsApplisBeans.stream().map(ProjetAppliBean::to).collect(Collectors.toList());
        List<StatutDTO> statuts = statutsBeans.stream().map(StatutBean::to).collect(Collectors.toList());
        List<RessourceHumaineDTO> ressourcesHumaines = ressourcesBeans.stream()
                .filter(ressourceBean -> ressourceBean instanceof RessourceHumaineBean)
                .map(ressourceBean -> (RessourceHumaineBean) ressourceBean)
                .map(RessourceHumaineBean::to)
                .collect(Collectors.toList());
        ReferentielsDTO referentiels = new ReferentielsDTO(joursFeries, importances, profils, projetsApplis, statuts, ressourcesHumaines);

        // Disponibilités :
        //
        // Nbrs de jours d'absence :
        Map<RessourceHumaineDTO, Map<LocalDate, Float>> nbrsJoursAbsence = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
        for (NbrsJoursAbsenceBean nbrsJoursAbsenceBean : nbrsJoursAbsenceBeans) {
            RessourceHumaineDTO ressourceHumaine = nbrsJoursAbsenceBean.getRessourceHumaineBean().toDto();
            Map<LocalDate, Float> calendrier = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
            for (LocalDate debutPeriode : nbrsJoursAbsenceBean.keySet()) {
                FloatProperty nbrJoursAbsenceProperty = nbrsJoursAbsenceBean.get(debutPeriode);
                calendrier.put(debutPeriode, nbrJoursAbsenceProperty.get());
            }
            nbrsJoursAbsence.put(ressourceHumaine, calendrier);
        }
        // % de dispo pour la CT
        Map<RessourceHumaineDTO, Map<LocalDate, Percentage>> pctagesDispoCT = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
        for (PctagesDispoCTBean pctagesDispoCTBean : pctagesDispoCTBeans) {
            RessourceHumaineDTO ressourceHumaine = pctagesDispoCTBean.getRessourceHumaineBean().toDto();
            Map<LocalDate, Percentage> calendrier = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
            for (LocalDate debutPeriode : pctagesDispoCTBean.keySet()) {
                PercentageProperty pctageDispoCTProperty = pctagesDispoCTBean.get(debutPeriode);
                calendrier.put(debutPeriode, pctageDispoCTProperty.getValue());
            }
            pctagesDispoCT.put(ressourceHumaine, calendrier);
        }
        //
        DisponibilitesDTO disponibilites = new DisponibilitesDTO(nbrsJoursAbsence, pctagesDispoCT);

        // Tâches + Charge :
        PlanificationsDTO planifications = toPlanificationDTOs();

        assert dateEtat != null;
        return new PlanChargeDTO(dateEtat, referentiels, disponibilites, planifications);
    }

    @NotNull
    public PlanificationsDTO toPlanificationDTOs() throws BeanException {
        PlanificationsDTO planifications = new PlanificationsDTO();
        for (PlanificationTacheBean planificationBean : this.planificationsBeans) {
            TacheDTO tache = planificationBean.getTacheBean().extract();

            Map<LocalDate, Double> calendrier = new TreeMap<>(); // TreeMap juste pour faciliter le débogage en triant les entrées sur la key.
            Map<LocalDate, DoubleProperty> ligne = planificationBean.getCalendrier();
            ligne.forEach((date, charge) -> calendrier.put(date, charge.getValue()));

            planifications.ajouter(tache, calendrier);
        }
        return planifications;
    }

    @Override
    public PlanChargeBean copier() throws CopieException {
        return copier(this);
    }

    // Pour déboguer, uniquement.
    @Override
    public String toString() {
        //noinspection StringConcatenationMissingWhitespace
        return "[" + (dateEtat == null ? "N/A" : dateEtat.format(DateTimeFormatter.ISO_DATE)) + "]"
                + " " + getPlanificationsBeans().size() + " tâches"
                + " (" + (modifie ? "" : "non ") + "modifié)";
    }

}
