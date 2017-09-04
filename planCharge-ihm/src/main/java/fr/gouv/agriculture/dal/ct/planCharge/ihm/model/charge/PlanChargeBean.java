package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursAbsenceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.PctagesDispoRsrcBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.PctagesDispoRsrcProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.*;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.Copiable;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private final ObservableList<RessourceBean<?, ?>> ressourcesBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<RessourceHumaineBean> ressourcesHumainesBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableMap, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableMap, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<PctagesDispoRsrcBean> pctagesDispoCTBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableMap, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<PctagesDispoRsrcProfilBean> pctagesDispoMaxRsrcProfilBeans = FXCollections.observableArrayList();
    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<PlanificationTacheBean> planificationsBeans = FXCollections.observableArrayList();
    @Null
    private LocalDate dateEtat = null;

    private BooleanProperty estModifie = new SimpleBooleanProperty(false);

    private BooleanProperty aBesoinEtreCalcule = new SimpleBooleanProperty(false);

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private PlanChargeBean() {
        super();

        ressourcesBeans.addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<RessourceHumaineBean> ressourcesHumainesBeansToAdd = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        ressourcesHumainesBeansToAdd.add(ressourceHumaineBean);
                    }
                    if (!ressourcesHumainesBeansToAdd.isEmpty()) {
                        ressourcesHumainesBeans.addAll(ressourcesHumainesBeansToAdd);
                    }
                }
                if (change.wasRemoved()) {
                    List<RessourceHumaineBean> ressourcesHumainesBeansToRemove = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        ressourcesHumainesBeansToRemove.add(ressourceHumaineBean);
                    }
                    if (!ressourcesHumainesBeansToRemove.isEmpty()) {
                        ressourcesHumainesBeans.removeAll(ressourcesHumainesBeansToRemove);
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
    public LocalDate dateEtat() throws BeanException {
        if (dateEtat == null) {
            throw new BeanException("Date d'état non définie.");
        }
        return dateEtat;
    }

    public void setDateEtat(@Null LocalDate dateEtat) {
        this.dateEtat = dateEtat;
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
    public ObservableList<RessourceBean<?, ?>> getRessourcesBeans() {
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
    public ObservableList<PctagesDispoRsrcBean> getPctagesDispoCTBeans() {
        return pctagesDispoCTBeans;
    }

    @NotNull
    public ObservableList<PctagesDispoRsrcProfilBean> getPctagesDispoMaxRsrcProfilBeans() {
        return pctagesDispoMaxRsrcProfilBeans;
    }


    @NotNull
    public ObservableList<PlanificationTacheBean> getPlanificationsBeans() {
        return planificationsBeans;
    }


    public boolean estModifie() {
        return estModifie.getValue();
    }

    public BooleanProperty estModifieProperty() {
        return estModifie;
    }

    private void setModifie(boolean estModifie) {
        this.estModifie.set(estModifie);
    }

    public void vientDEtreCharge() {
        this.estModifie.setValue(false);
        this.aBesoinEtreCalcule.setValue(true);
    }

    public void vientDEtreSauvegarde() {
        this.estModifie.setValue(false);
    }

    public void vientDEtreModifie() {
        this.estModifie.setValue(true);
        this.aBesoinEtreCalcule.setValue(true);
    }

    public void vientDEtreCalcule() {
        this.aBesoinEtreCalcule.setValue(false);
    }

    public boolean aBesoinEtreSauvegarde() {
        return estModifie.getValue();
    }

    @FXML
    public BooleanProperty aBesoinEtreSauvegardeProperty() {
        return estModifie;
    }

    @FXML
    public BooleanProperty getABesoinEtreSauvegardeProperty() {
        return estModifie;
    }

    public boolean aBesoinEtreCalcule() {
        return aBesoinEtreCalcule.getValue();
    }

    @FXML
    public BooleanProperty aBesoinEtreCalculeProperty() {
        return aBesoinEtreCalcule;
    }

    @FXML
    public BooleanProperty getABesoinEtreCalculeProperty() {
        return aBesoinEtreCalcule;
    }


    @NotNull
    public PlanChargeBean fromDto(@NotNull PlanChargeDTO planCharge) throws BeanException {
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
        // Ressources :
        {
            List<RessourceBean<?, ?>> ressourceBeanList = new ArrayList<>(); // On utilise une List intermédiaire pour optimiser et ne pas ajouter les élts à la ObservableList un par un.
            ressourceBeanList.addAll(
                    planCharge.getReferentiels().getRessourcesHumaines().stream()
                            .map(RessourceHumaineBean::from)
                            .collect(Collectors.toList())
            );
            ressourceBeanList.addAll(
                    Arrays.stream(RessourceGeneriqueDTO.values())
                            .map(RessourceGeneriqueBean::from)
                            .collect(Collectors.toList())
            );
            ressourcesBeans.setAll(ressourceBeanList);
        }
        // Disponibilités :
        { // Nbrs de jours d'absence / rsrc :
            List<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeanList = new ArrayList<>(); // On utilise une List intermédiaire pour optimiser et ne pas ajouter les élts à la ObservableList un par un.
            Map<RessourceHumaineDTO, Map<LocalDate, Float>> absencesDTO = planCharge.getDisponibilites().getNbrsJoursAbsence();
            for (RessourceHumaineDTO ressourceHumaineDTO : absencesDTO.keySet()) {
                Map<LocalDate, FloatProperty> calendrierAbsences = absencesDTO.get(ressourceHumaineDTO).keySet().stream()
                        .collect(Collectors.toMap(locaDate -> locaDate, localDate -> new SimpleFloatProperty(absencesDTO.get(ressourceHumaineDTO).get(localDate)))); // Rq : Collectors.toMap "dé-trie" car instancie une HashMap.
                nbrsJoursAbsenceBeanList.add(new NbrsJoursAbsenceBean(RessourceHumaineBean.from(ressourceHumaineDTO), calendrierAbsences));
            }
            nbrsJoursAbsenceBeans.setAll(nbrsJoursAbsenceBeanList);
        }
        { // Pctages de dispo pour la CT / rsrc :
            List<PctagesDispoRsrcBean> pctagesDispoCTBeanList = new ArrayList<>(); // On utilise une List intermédiaire pour optimiser et ne pas ajouter les élts à la ObservableList un par un.
            Map<RessourceHumaineDTO, Map<LocalDate, Percentage>> pctagesDispoCT = planCharge.getDisponibilites().getPctagesDispoCT();
            for (RessourceHumaineDTO ressourceHumaineDTO : pctagesDispoCT.keySet()) {
                Map<LocalDate, PercentageProperty> calendrierAbsences = pctagesDispoCT.get(ressourceHumaineDTO).keySet().stream()
                        .collect(Collectors.toMap(locaDate -> locaDate, localDate -> new PercentageProperty(pctagesDispoCT.get(ressourceHumaineDTO).get(localDate).floatValue()))); // Rq : Collectors.toMap "dé-trie" car instancie une HashMap.
                pctagesDispoCTBeanList.add(new PctagesDispoRsrcBean(RessourceHumaineBean.from(ressourceHumaineDTO), calendrierAbsences));
            }
            pctagesDispoCTBeans.setAll(pctagesDispoCTBeanList);
        }
        { // Pctages de dispo max / rsrc / profil :
            List<PctagesDispoRsrcProfilBean> pctagesDispoMaxRsrcProfilBeanList = new ArrayList<>(); // On utilise une List intermédiaire pour optimiser et ne pas ajouter les élts à la ObservableList un par un.
            Map<RessourceHumaineDTO, Map<ProfilDTO, Map<LocalDate, Percentage>>> pctagesDispoMaxRsrcProfil = planCharge.getDisponibilites().getPctagesDispoMaxRsrcProfil();
            for (RessourceHumaineDTO ressourceHumaineDTO : pctagesDispoMaxRsrcProfil.keySet()) {
                for (ProfilDTO profilDTO : pctagesDispoMaxRsrcProfil.get(ressourceHumaineDTO).keySet()) {
                    Map<LocalDate, PercentageProperty> calendrierAbsences = pctagesDispoMaxRsrcProfil.get(ressourceHumaineDTO).get(profilDTO).keySet().stream()
                            .collect(Collectors.toMap(locaDate -> locaDate, localDate -> new PercentageProperty(pctagesDispoMaxRsrcProfil.get(ressourceHumaineDTO).get(profilDTO).get(localDate).floatValue()))); // Rq : Collectors.toMap "dé-trie" car instancie une HashMap.
                    pctagesDispoMaxRsrcProfilBeanList.add(new PctagesDispoRsrcProfilBean(RessourceHumaineBean.from(ressourceHumaineDTO), ProfilBean.from(profilDTO), calendrierAbsences));
                }
            }
            pctagesDispoMaxRsrcProfilBeans.setAll(pctagesDispoMaxRsrcProfilBeanList); // TODO FDA 2017/08 Améliorer la perf, prend pratiquement 1 minute ! Sans doute car contient des Property (gestion de leurs Listeners)
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
        assert dateEtat != null;
        ReferentielsDTO referentiels = toReferentielsDTO();
        DisponibilitesDTO disponibilites = toDisponibilitesDTO();
        PlanificationsDTO planifications = toPlanificationDTOs();
        return new PlanChargeDTO(dateEtat, referentiels, disponibilites, planifications);
    }

    @SuppressWarnings("WeakerAccess")
    public ReferentielsDTO toReferentielsDTO() {
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
        return new ReferentielsDTO(joursFeries, importances, profils, projetsApplis, statuts, ressourcesHumaines);
    }

    @NotNull
    public DisponibilitesDTO toDisponibilitesDTO() {

        // Nbrs de jours d'absence / rsrc :
        Map<RessourceHumaineDTO, Map<LocalDate, Float>> nbrsJoursAbsence = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
        for (NbrsJoursAbsenceBean nbrsJoursAbsenceBean : nbrsJoursAbsenceBeans) {
            RessourceHumaineDTO ressourceHumaine = nbrsJoursAbsenceBean.getRessourceBean().toDto();
            Map<LocalDate, Float> calendrier = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
            for (LocalDate debutPeriode : nbrsJoursAbsenceBean.keySet()) {
                FloatProperty nbrJoursAbsenceProperty = nbrsJoursAbsenceBean.get(debutPeriode);
                calendrier.put(debutPeriode, nbrJoursAbsenceProperty.get());
            }
            nbrsJoursAbsence.put(ressourceHumaine, calendrier);
        }

        // % de dispo pour la CT / rsrc
        Map<RessourceHumaineDTO, Map<LocalDate, Percentage>> pctagesDispoCT = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
        for (PctagesDispoRsrcBean pctagesDispoCTBean : pctagesDispoCTBeans) {
            RessourceHumaineDTO ressourceHumaine = pctagesDispoCTBean.getRessourceBean().toDto();
            Map<LocalDate, Percentage> calendrier = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
            for (LocalDate debutPeriode : pctagesDispoCTBean.keySet()) {
                PercentageProperty pctageDispoCTProperty = pctagesDispoCTBean.get(debutPeriode);
                calendrier.put(debutPeriode, pctageDispoCTProperty.getValue());
            }
            pctagesDispoCT.put(ressourceHumaine, calendrier);
        }

        // % de dispo max / rsrc / profil
        Map<RessourceHumaineDTO, Map<ProfilDTO, Map<LocalDate, Percentage>>> pctagesDispoMaxProfil = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
        for (PctagesDispoRsrcProfilBean pctagesDispoMaxProfilBean : pctagesDispoMaxRsrcProfilBeans) {

            RessourceHumaineDTO ressourceHumaine = pctagesDispoMaxProfilBean.getRessourceBean().toDto();
            if (!pctagesDispoMaxProfil.containsKey(ressourceHumaine)) {
                pctagesDispoMaxProfil.put(ressourceHumaine, new TreeMap<>()); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
            }
            Map<ProfilDTO, Map<LocalDate, Percentage>> pctagesDispoMaxRessHum = pctagesDispoMaxProfil.get(ressourceHumaine);

            ProfilDTO profil = pctagesDispoMaxProfilBean.getProfilBean().toDto();

            Map<LocalDate, Percentage> calendrier = new TreeMap<>(); // TreeMap (au lieu de HashMap) pour trier, juste pour faciliter le débogage.
            for (LocalDate debutPeriode : pctagesDispoMaxProfilBean.keySet()) {
                PercentageProperty pctageDispoCTProperty = pctagesDispoMaxProfilBean.get(debutPeriode);
                calendrier.put(debutPeriode, pctageDispoCTProperty.getValue());
            }

            pctagesDispoMaxRessHum.put(profil, calendrier);
        }

        DisponibilitesDTO disponibilites = new DisponibilitesDTO(nbrsJoursAbsence, pctagesDispoCT, pctagesDispoMaxProfil);
        return disponibilites;
    }

    @NotNull
    public PlanificationsDTO toPlanificationDTOs() throws BeanException {
        PlanificationsDTO planifications = new PlanificationsDTO();
        for (PlanificationTacheBean planificationBean : planificationsBeans) {
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
        //noinspection StringConcatenationMissingWhitespace,HardcodedFileSeparator
        return "[" + (dateEtat == null ? "N/A" : dateEtat.format(DateTimeFormatter.ISO_DATE)) + "]"
                + " " + planificationsBeans.size() + " tâches"
                + " (" + (estModifie() ? "" : "non ") + "modifié)";
    }

}
