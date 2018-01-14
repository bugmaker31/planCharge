package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import eu.hansolo.medusa.Gauge;
import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.util.ParametresIhm;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationsReglesGestionException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.rapportProgression.RapportChargementAvecProgression;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.rapportProgression.RapportImportPlanChargeAvecProgression;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.rapportProgression.RapportMajTachesAvecProgression;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.rapportProgression.RapportSauvegardeAvecProgression;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.PlanificationTableView;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportImportPlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportImportTaches;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.util.Strings;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.File;
import java.text.DecimalFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 09/04/2017.
 *
 * @author frederic.danna
 */
public class ApplicationController extends AbstractController {

    public static int SEUIL_ALERT_RAM_PC = 10; // TODO FDA 2017/08 Permettre à l'utilisateur de paramétrer ce seuil (dans ses préférences).

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("SYSTEM");

    private static ApplicationController instance = null;

    public static ApplicationController instance() {
        return instance;
    }

    private static ParametresIhm paramsIhm = ParametresIhm.instance();
    private static ParametresMetiers paramsMetier = ParametresMetiers.instance();


    private Module moduleCourant = null;

    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean manqueMemoireDejaDetecte = false;
    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean alerteManqueMemoireAffichee = false;


    // Les menus :

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Menu menuEditer;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private MenuItem menuAnnuler;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Menu sousMenuAnnuler;
    /*
        @FXML
        @NotNull
        private SeparatorMenuItem separateurMenusAnnuler;
    */
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private MenuItem menuRetablir;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Menu sousMenuRetablir;
    /*
        @FXML
        @NotNull
        private SeparatorMenuItem separateurMenusRetablir;
    */
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private MenuItem menuRepeter;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Menu sousMenuRepeter;

    /*
        @FXML
        @NotNull
        private Menu menuDisponibilites;
        @FXML
        @NotNull
        private Menu menuTaches;
        @FXML
        @NotNull
        private Menu menuCharges;
    */

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private RadioMenuItem themeStandardRadioMenuItem;

    @NotNull
    public RadioMenuItem getThemeStandardRadioMenuItem() {
        return themeStandardRadioMenuItem;
    }

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private RadioMenuItem themeSombreRadioMenuItem;

    @NotNull
    public RadioMenuItem getThemeSombreRadioMenuItem() {
        return themeSombreRadioMenuItem;
    }

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Pane contentPane;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TitledPane parametresPane;


    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private DatePicker dateEtatPicker;


    // La barre d'état :

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private CheckBox sauvegardeRequiseCheckbox;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private CheckBox calculRequisCheckbox;

    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Gauge memoryGauge;

    private String memoryGaugeTooltipInitialText;

    private int pcMemLibrePrecedent = -1;

/*
    @FXML
    @NotNull
    private Region progressionRegion;
    @FXML
    @NotNull
    private Label avancementLabel;
    @FXML
    @NotNull
    private ProgressBar progressionBar;
*/

    @NotNull
    public Pane getContentPane() {
        return contentPane;
    }

    @NotNull
    public DatePicker getDateEtatPicker() {
        return dateEtatPicker;
    }


/*
    @Null
    private String moduleCourant;
*/

/*
    //    @Autowired
    @NotNull
    private final CalculateurDisponibilites calculateurDisponibilites = CalculateurDisponibilites.instance();
*/

    // Les services métier :

    //    @Autowired
    @NotNull
    private ChargeService planChargeService = ChargeService.instance();


    // Les données métier :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<JourFerieBean> joursFeriesBeans = planChargeBean.getJoursFeriesBeans();

    @NotNull
    private ObservableList<PlanificationTacheBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    public Module getModuleCourant() {
        return moduleCourant;
    }


    public ApplicationController() throws ControllerException {
        super();
        if (instance != null) {
            throw new ControllerException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    public void initialize() throws ControllerException {
//        super.initialize();

/*
        // Cf. http://stackoverflow.com/questions/10315774/javafx-2-0-activating-a-menu-like-a-menuitem
        if (menuDisponibilites.getItems().size() == 1) {
            menuDisponibilites.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    menuDisponibilites.getItems().get(0).fire();
                }
            });
        }
        if (menuTaches.getItems().size() == 1) {
            menuTaches.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    menuTaches.getItems().get(0).fire();
                }
            });
        }
        if (menuCharges.getItems().size() == 1) {
            menuCharges.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    menuCharges.getItems().get(0).fire();
                }
            });
        }
*/

/*
        // Cf. http://stackoverflow.com/questions/26915259/javafx-setting-a-tab-mnemonics
        {
            Button fakeLabel = new Button("_" + disponibilitesTab.getText());
            disponibilitesTab.setText("");
            fakeLabel.setMnemonicParsing(true);
            fakeLabel.getStyleClass().clear();
            disponibilitesTab.setGraphic(fakeLabel);
            fakeLabel.setOnAction(ev -> {
                gestionTabPane.getSelectionModel().select(disponibilitesTab);
            });
        }
        {
            Button fakeLabel = new Button("_" + tachesTab.getText());
            tachesTab.setText("");
            fakeLabel.setMnemonicParsing(true);
            fakeLabel.getStyleClass().clear();
            tachesTab.setGraphic(fakeLabel);
            fakeLabel.setOnAction(ev -> {
                gestionTabPane.getSelectionModel().select(tachesTab);
            });
        }
        {
            Button fakeLabel = new Button("_" + chargesTab.getText());
            chargesTab.setText("");
            fakeLabel.setMnemonicParsing(true);
            fakeLabel.getStyleClass().clear();
            chargesTab.setGraphic(fakeLabel);
            fakeLabel.setOnAction(ev -> {
                gestionTabPane.getSelectionModel().select(chargesTab);
            });
        }
*/

        getSuiviActionsUtilisateur().initialiser(
                menuAnnuler, sousMenuAnnuler,
                menuRetablir, sousMenuRetablir,
                menuRepeter, sousMenuRepeter
        );

/*
        planChargeBean.getPlanificationsBeans().addListener((ListChangeListener<? super PlanificationTacheBean>) change ->
                definirNbrTachesDansBarreEtat()
        );
*/

        memoryGauge.setMaxValue((double) Runtime.getRuntime().maxMemory()); // Cf. https://stackoverflow.com/questions/3571203/what-are-runtime-getruntime-totalmemory-and-freememory?answertab=votes#tab-top
        DecimalFormat ramFormat = new DecimalFormat("0");
        memoryGauge.getTooltip().setText(
                memoryGauge.getTooltip().getText()
                        .replaceAll("\\$maxMem", Strings.humanReadable(Runtime.getRuntime().maxMemory(), ramFormat))
        );
        memoryGaugeTooltipInitialText = memoryGauge.getTooltip().getText();
        // Cf. https://stackoverflow.com/questions/42811673/javafx-progressbar-showing-cpu-and-memory-usage
        AnimationTimer memoryGaugeUpdater = new AnimationTimer() {

            @Override
            public void handle(long now) {
                long maxMem = Runtime.getRuntime().maxMemory();
                long totalMem = Runtime.getRuntime().totalMemory();
                long freeMem = Runtime.getRuntime().freeMemory() + (maxMem - totalMem);
                long usedMem = maxMem - freeMem; // // Cf. https://stackoverflow.com/questions/3571203/what-are-runtime-getruntime-totalmemory-and-freememory?answertab=votes#tab-top
                memoryGauge.setValue(usedMem);
            }
        };
        memoryGauge.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

            long maxMem = Runtime.getRuntime().maxMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory() + (maxMem - totalMem);
            long usedMem = maxMem - freeMem; // // Cf. https://stackoverflow.com/questions/3571203/what-are-runtime-getruntime-totalmemory-and-freememory?answertab=votes#tab-top
            int pcMemLibre = new Double((double) ((freeMem * 100L) / maxMem)).intValue();

            if (pcMemLibrePrecedent == pcMemLibre) {
                return;
            }
            pcMemLibrePrecedent = pcMemLibre;

            SYSTEM_LOGGER.info("Mémoire == free={} ({}%), used={}, total={}, max={}", Strings.humanReadable(freeMem, ramFormat), pcMemLibre, Strings.humanReadable(usedMem, ramFormat), Strings.humanReadable(totalMem, ramFormat), Strings.humanReadable(maxMem, ramFormat));

            memoryGauge.setBarColor(
                    (pcMemLibre <= 30) ? Color.RED :
                            ((pcMemLibre <= 40) ? Color.SALMON :
                                    ((pcMemLibre <= 50) ? Color.ORANGE :
                                            Color.GREEN))
            );

            memoryGauge.getTooltip().setText(
                    memoryGaugeTooltipInitialText
                            .replaceAll("\\$usedMem", Strings.humanReadable(newValue.longValue(), ramFormat))
                            .replaceAll("\\$freeMemPc", String.valueOf(pcMemLibre))
            );

            if ((pcMemLibre < SEUIL_ALERT_RAM_PC) && !alerteManqueMemoireAffichee && !manqueMemoireDejaDetecte) {
                LOGGER.warn("Alerte manque de mémoire (RAM) : reste moins de {}% ({} oct. libres sur {} oct., soit {}%). Augmenter la mémoire allouée à l'appli ('java ... -Xmx...'.)", SEUIL_ALERT_RAM_PC, freeMem, maxMem, pcMemLibre);
                SYSTEM_LOGGER.warn("Alerte manque de mémoire (RAM) : reste moins de {}% ({} oct. libres sur {} oct., soit {}%). Augmenter la mémoire allouée à l'appli ('java ... -Xmx...'.)", SEUIL_ALERT_RAM_PC, freeMem, maxMem, pcMemLibre);
                manqueMemoireDejaDetecte = true;
                alerteManqueMemoireAffichee = true;
                Platform.runLater(() -> {
                    //noinspection HardcodedLineSeparator
                    ihm.afficherDialog(
                            Alert.AlertType.WARNING,
                            "Manque de mémoire",
                            "On frôle le crash à cause d'un manque de mémoire (plus que " + pcMemLibre + "% de RAM libre)."
                                    + "\nPensez à sauvegarder dans un premier temps, puis à demander à augmenter la mémoire allouée à cette application (java ... -Xmx...).",
                            500, 200
                    );
                    alerteManqueMemoireAffichee = false;
                });
            }
        });
        memoryGauge.setOnMouseClicked(event -> {
            //noinspection CallToSystemGC
            Runtime.getRuntime().gc();
        });
        memoryGaugeUpdater.start();
    }


    @SuppressWarnings("WeakerAccess")
    public void majTitre() {
        String titre = PlanChargeIhm.APP_NAME;
        //noinspection HardcodedFileSeparator
        titre += " - " + fr.gouv.agriculture.dal.ct.planCharge.util.Objects.value(planChargeBean.getDateEtat(), localDate -> localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), "N/C");
        //noinspection HardcodedFileSeparator
        titre += " - " + fr.gouv.agriculture.dal.ct.planCharge.util.Objects.value(moduleCourant, nomModule -> moduleCourant.getTitre(), "N/C");
        ihm.definirTitre(titre);
    }

    @SuppressWarnings("WeakerAccess")
    public void majBarreEtat() {
        LOGGER.debug("MàJ de la barre d'état : ");
        sauvegardeRequiseCheckbox.setSelected(planChargeBean.aBesoinEtreSauvegarde());
        calculRequisCheckbox.setSelected(planChargeBean.aBesoinEtreCalcule());
        LOGGER.debug("Barre d'état à jour.");
    }


    /*
    Menu "Fichier" :
     */

    @FXML
    private void nouveau(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Nouveau");

        if (!estAcceptableDePerdreDesDonnees()) {
            return;
        }

        try {
            reinitPlanCharge();
        } catch (ControllerException e) {
            LOGGER.error("Impossible de ré-initialiser un nouveau plan de charge.", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible de ré-initialiser un nouveau plan de charge",
                    "Impossible de ré-initialiser un nouveau plan de charge : \n" + Exceptions.causes(e),
                    400, 200
            );
        }
    }

    private void reinitPlanCharge() throws ControllerException {
        definirDateEtat((LocalDate) null);
        Arrays.stream(planChargeBean.listesBeans())
                .forEach(List::clear);
        majTitre();
        majBarreEtat();
    }

    @FXML
    private void charger(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Charger");

        if (!estAcceptableDePerdreDesDonnees()) {
            return;
        }

        try {
            charger();
        } catch (ControllerException e) {
            LOGGER.error("Impossible de charger le plan de charge.", e);
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible de charger le plan de charge",
                    "Impossible de charger le plan de charge : \n" + Exceptions.causes(e),
                    400, 200
            );
        }
    }

    private void charger() throws ControllerException {

        File ficCalc;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Indiquez le fichier XML qui contient un plan de charge : ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier XML du plan de charge", "planCharge_*.xml"),
                new FileChooser.ExtensionFilter("Fichier XML", "*.xml")
        );
        File repPersistance;
        try {
            String nomRepPersistance = paramsMetier.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE);
            repPersistance = new File(nomRepPersistance);
            if (!repPersistance.exists()) {
                throw new ControllerException("Le répertoire de stockage des fichiers XML (" + nomRepPersistance + ") n'existe pas.");
            }
        } catch (KernelException e) {
            throw new ControllerException("Impossible de déterminer le répertoire de persistance du plan de charge (fichiers XML).", e);
        }
        fileChooser.setInitialDirectory(repPersistance);
        ficCalc = fileChooser.showOpenDialog(ihm.getPrimaryStage());
        if (ficCalc == null) {
            ihm.afficherDialog(
                    Alert.AlertType.INFORMATION,
                    "Chargement annulé",
                    "Le chargement a été annulé par l'utilisateur.",
                    400, 200
            );
            return;
        }

        charger(ficCalc);
    }

    public void charger(@NotNull LocalDate dateEtat) {
        try {
            File ficCalc = planChargeService.fichierPersistancePlanCharge(dateEtat);
            charger(ficCalc);
        } catch (ServiceException | ControllerException e) {
            LOGGER.error("Impossible de charger le plan de charge.", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible de charger le plan de charge",
                    Exceptions.causes(e),
                    800, 300
            );
        }
    }

    private void charger(@NotNull File ficPlanCharge) throws ControllerException {

        RapportChargementAvecProgression rapport = new RapportChargementAvecProgression();

        Task<RapportChargementAvecProgression> chargerPlanCharge = new Task<RapportChargementAvecProgression>() {

            @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
            @Override
            protected RapportChargementAvecProgression call() throws Exception {

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.longValue(), rapport.getProgressionMax()));

//                PlanChargeBean planChargeBeanAvantChargement = planChargeBean.copier();

                PlanChargeDTO planChargeDTO = planChargeService.charger(ficPlanCharge, rapport);

                planChargeBean.fromDto(planChargeDTO);

                planChargeBean.vientDEtreCharge();
//                getSuiviActionsUtilisateur().historiser(new ChargementPlanCharge(planChargeBeanAvantChargement));

                return rapport;
            }
        };

        try {
            Calculateur.executerSansCalculer(() -> {
                try {

                    RapportChargementAvecProgression rapportFinal =
                            ihm.afficherProgression("Chargement du plan de charge...", chargerPlanCharge);

                    definirDateEtat(planChargeBean.getDateEtat());

                } catch (ViolationsReglesGestionException e) {
                    ihm.afficherViolationsReglesGestion(
                            "Impossible de charger le plan de charge.", e.getLocalizedMessage(),
                            e.getViolations()
                    );
                }
            });

//            rapport.setAvancement("Calcul..."); Sans effet, le Dialog qui affiche l'avancement ayant été fermé avec la fin de la Task "chargerPlanCharge".
            calculer();

            ihm.getTachesController().filtrerRien();
            ihm.getChargesController().filtrerRien();

            //noinspection HardcodedLineSeparator
            ihm.afficherNotificationInfo(
                    "Chargement terminé",
                    "Le chargement est terminé :"
                            + "\n- date d'état : " + planChargeBean.getDateEtat()
                            + "\n- " + planChargeBean.getPlanificationsBeans().size() + " tâches"
            );

            afficherModuleCharges(); // Rq : Simule une action de l'utilisateur (l'action peut être "undone" (Ctrl+Z), etc.).

            majBarreEtat();
        } catch (IhmException e) {
            throw new ControllerException("Impossible de charger le plan de charge depuis le fichier '" + ficPlanCharge.getAbsolutePath() + "'.", e);
        }
    }


    // TODO FDA 23017/02 Afficher une "progress bar".
    @FXML
    private void sauver(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Sauver");

        if ((planChargeBean.getDateEtat() == null) || planChargeBean.getPlanificationsBeans().isEmpty()) {
            LOGGER.warn("Impossible de sauver un plan de charge non défini.");
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Impossible de sauver le plan de charge",
                    "Impossible de sauver un plan de charge sans date d'état, ou sans planification.",
                    500, 200
            );
            return;
        }

        RapportSauvegardeAvecProgression rapport = new RapportSauvegardeAvecProgression();

        Task<RapportSauvegardeAvecProgression> sauvegarder = new Task<RapportSauvegardeAvecProgression>() {

            @Override
            protected RapportSauvegardeAvecProgression call() throws Exception {

                rapport.setProgressionMax(1);

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.longValue(), rapport.getProgressionMax()));

                PlanChargeDTO planCharge = planChargeBean.toDto();

                planChargeService.sauver(planCharge, rapport);

                rapport.setProgressionCourante(1);

                return rapport;
            }
        };

        //noinspection OverlyBroadCatchBlock
        try {

            RapportSauvegarde rapportFinal = ihm.afficherProgression("Sauvegarde...", sauvegarder);
            assert rapportFinal != null;

            planChargeBean.vientDEtreSauvegarde();
            getSuiviActionsUtilisateur().historiser(new SauvegardePlanCharge());

            //noinspection unused
            File ficPlanCharge = planChargeService.fichierPersistancePlanCharge(planChargeBean.getDateEtat());
            //noinspection HardcodedLineSeparator
            ihm.afficherNotificationInfo("Sauvegarde effectuée.",
                    "Le plan de charge"
                            + " en date du " + planChargeBean.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                            + " a été sauvegardé"
                            + "\n(dans le fichier '" + ficPlanCharge.getAbsolutePath() + "')"
                            + "."
            );

            majBarreEtat();

        } catch (ViolationsReglesGestionException e) {
            ihm.afficherViolationsReglesGestion(
                    "Impossible de sauver le plan de charge.",
                    e.getLocalizedMessage(),
                    e.getViolations()
            );
            // TODO FDA 2017/05 Positionner l'affichage sur la 1ère ligne/colonne en erreur.
        } catch (IhmException | ServiceException e) {
            LOGGER.warn("Impossible de sauver le plan de charge.", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible de sauver le plan de charge.",
                    Exceptions.causes(e),
                    500, 200
            );
        }
    }

    @FXML
    private void majTachesDepuisCalc(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Importer > Taches depuis Calc");
        try {
            majTachesDepuisCalc();
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'importer les tâches.", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'importer les tâches",
                    "Impossible d'importer les tâches : \n" + Exceptions.causes(e),
                    400, 200
            );
        }
    }

    void majTachesDepuisCalc() throws ControllerException {
        File ficCalc;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Indiquez le fichier Calc (LibreOffice) qui contient les tâches ('suivi des demandes'') : ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier LOCalc de suivi des demandes", "DAL-CT_14_PIL_Suivi des demandes_*.ods"),
                new FileChooser.ExtensionFilter("LibreOffice Calc", "*.ods")
        );
        String nomRepFicCalc;
        try {
            // TODO FDA 2017/05 C'est le répertoire des XML, pas forcément des ODS. Plutôt regarder dans les préférences de l'utilisateur ?
            nomRepFicCalc = paramsMetier.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE);
        } catch (KernelException e) {
            throw new ControllerException("Impossible de déterminer le répertoire de persistance.", e);
        }
        fileChooser.setInitialDirectory(new File(nomRepFicCalc));
        ficCalc = fileChooser.showOpenDialog(ihm.getPrimaryStage());
        if (ficCalc == null) {
            ihm.afficherDialog(
                    Alert.AlertType.INFORMATION,
                    "MàJ annulée",
                    "La mise à jour des tâches depuis un fichier Calc a été annulé par l'utilisateur.",
                    400, 200
            );
            return;
        }

        majTachesDepuisCalc(ficCalc);
    }

    public void majTachesDepuisCalc(@NotNull File ficCalc) throws ControllerException {

        RapportMajTachesAvecProgression rapport = new RapportMajTachesAvecProgression();

        PlanChargeDTO[] planCharge = new PlanChargeDTO[1];

        Task<RapportImportTaches> majTachesDepuisCalc = new Task<RapportImportTaches>() {

            @Override
            protected RapportImportTaches call() throws Exception {

                rapport.setProgressionMax(1);

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.longValue(), rapport.getProgressionMax()));

//                planChargeBeanAvantMaj[0] = planChargeBean.copier();

                planCharge[0] = planChargeBean.toDto();

                RapportImportTaches rapportMajTaches = planChargeService.majTachesDepuisCalc(planCharge[0], ficCalc, rapport);

                return rapportMajTaches;
            }
        };

        try {
            Calculateur.executerSansCalculer(() -> {
                try {
                    RapportImportTaches rapportFinal = ihm.afficherProgression("Import des tâches", majTachesDepuisCalc);
                    assert rapport != null;

                    planChargeBean.fromDto(planCharge[0]);

                    definirDateEtat(planChargeBean.getDateEtat());

                    planChargeBean.vientDEtreModifie();
//                            getSuiviActionsUtilisateur().historiser(new ImportTaches(planChargeBeanAvantChargement[0]));

                    //noinspection HardcodedLineSeparator
                    ihm.afficherDialog(Alert.AlertType.INFORMATION,
                            "Tâches mises à jour importées",
                            "Les tâches ont été mises à jour : "
                                    + "\n- depuis le fichier : " + ficCalc.getAbsolutePath()
                                    + "\n- nombre de tâches initial : " + rapportFinal.getNbrTachesPlanifiees()
                                    + "\n- nombre de lignes importées : " + rapportFinal.getNbrTachesImportees()
                                    + "\n- nombre de tâches mises à jour : " + rapportFinal.getNbrTachesMisesAJour()
                                    + "\n- nombre de tâches ajoutées : " + rapportFinal.getNbrTachesAjoutees()
                                    + "\n- nombre de tâches supprimées : " + rapportFinal.getNbrTachesSupprimees()
                                    + "\n- nombre de tâches au final : " + planChargeBean.getPlanificationsBeans().size(),
                            800, 300
                    );

                    afficherModuleTaches();

                    majBarreEtat();
                } catch (ViolationsReglesGestionException e) {
                    ihm.afficherViolationsReglesGestion(
                            "Impossible de sauver le plan de charge.", e.getLocalizedMessage(),
                            e.getViolations()
                    );
                } catch (ControllerException | BeanException | SuiviActionsUtilisateurException e) {
                    throw new ControllerException("Impossible de mettre à jour les tâches depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
                }
            });

//            rapport.setAvancement("Calcul..."); Sans effet, le Dialog qui affiche l'avancement ayant été fermé avec la fin de la Task "importerPlanChargeDepuisCalc".
            calculer();

        } catch (IhmException e) {
            throw new ControllerException("Impossible de mettre à jour les tâches depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        }
    }

    @FXML
    private void importerPlanChargeDepuisCalc(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Importer > Plan charge depuis Calc");

        if (!estAcceptableDePerdreDesDonnees()) {
            return;
        }

        try {
            importerPlanChargeDepuisCalc();
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'importer le plan de charge.", e);
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'importer le plan de charge",
                    "Impossible d'importer le plan de charge : \n" + Exceptions.causes(e),
                    400, 200
            );
        }
    }

    private void importerPlanChargeDepuisCalc() throws ControllerException {
        File ficCalc;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Indiquez le fichier Calc (LIbreOffice) qui contient un plan de charge : ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier LOCalc du plan de charge", "DAL-CT_11_PIL_Plan de charge_*.ods"),
                new FileChooser.ExtensionFilter("LibreOffice Calc", "*.ods")
        );
        String nomRepFicCalc;
        try {
            // TODO FDA 2017/05 C'est le répertoire des XML, pas forcément des ODS. Plutôt regarder dans les préférences de l'utilisateur ?
            nomRepFicCalc = paramsMetier.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE);
        } catch (KernelException e) {
            throw new ControllerException("Impossible de déterminer le répertoire de persistance du plan de charge.", e);
        }
        // TODO FDA 2017/05 Vérifier que le répertoire existe. Si non, le créer ?
        fileChooser.setInitialDirectory(new File(nomRepFicCalc));
        ficCalc = fileChooser.showOpenDialog(ihm.getPrimaryStage());
        if (ficCalc == null) {
            ihm.afficherDialog(
                    Alert.AlertType.INFORMATION,
                    "Import annulé",
                    "L'import depuis un fichier Calc a été annulé par l'utilisateur.",
                    400, 200
            );
            return;
        }

        importerPlanChargeDepuisCalc(ficCalc);
    }

    public void importerPlanChargeDepuisCalc(@NotNull File ficCalc) throws ControllerException {

        RapportImportPlanChargeAvecProgression rapport = new RapportImportPlanChargeAvecProgression();
        PlanChargeDTO[] planCharge = new PlanChargeDTO[1];

        Task<RapportImportPlanChargeAvecProgression> importerPlanChargeDepuisCalc = new Task<RapportImportPlanChargeAvecProgression>() {

            @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
            @Override
            protected RapportImportPlanChargeAvecProgression call() throws Exception {

                rapport.setProgressionMax(1);

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.longValue(), rapport.getProgressionMax()));

//                planChargeBeanAvantChargement[0] = planChargeBean.copier();

                rapport.setAvancement("Import depuis Calc...");
                planCharge[0] = planChargeService.importerDepuisCalc(ficCalc, rapport);

                return rapport;
            }
        };

        try {
            Calculateur.executerSansCalculer(() -> {
                try {

                    RapportImportPlanCharge rapportFinal =
                            ihm.afficherProgression("Import du plan de charge", importerPlanChargeDepuisCalc);
                    assert rapportFinal != null;
                    assert planCharge[0] != null;

                    planChargeBean.fromDto(planCharge[0]);

                    planChargeBean.vientDEtreModifie();
//                            getSuiviActionsUtilisateur().historiser(new ImportPlanCharge(planChargeBeanAvantChargement[0]));

                    definirDateEtat(planChargeBean.getDateEtat());

                    ihm.getTachesController().filtrerRien();
                    ihm.getChargesController().filtrerRien();

                    //noinspection HardcodedLineSeparator,HardcodedFileSeparator
                    ihm.afficherNotificationInfo("Données importées",
                            "Le plan de charge a été importé : "
                                    + "\n- depuis le fichier : " + ficCalc.getAbsolutePath()
                                    + "\n- date d'état : " + planChargeBean.getDateEtat()
                                    + "\n- nombre de lignes/tâches importées :" + planChargeBean.getPlanificationsBeans().size()
                    );

                    afficherModuleCharges(); // Rq : Simule une action de l'utilisateur (l'action peut être "undone" (Ctrl+Z), etc.).

                    majBarreEtat();

                } catch (ViolationsReglesGestionException e) {
                    ihm.afficherViolationsReglesGestion(
                            "Impossible d'importer le plan de charge.", e.getLocalizedMessage(),
                            e.getViolations()
                    );
                }
            });

//            rapport.setAvancement("Calcul..."); Sans effet, le Dialog qui affiche l'avancement ayant été fermé avec la fin de la Task "importerPlanChargeDepuisCalc".
            calculer();

        } catch (IhmException e) {
            throw new ControllerException("Impossible d'importer le plan de charge depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        }
    }

    @FXML
    private void quitter(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Quitter");

        if (!estAcceptableDePerdreDesDonnees()) {
            return;
        }

        try {
            ihm.stop();
        } catch (Exception e) {
            LOGGER.error("Impossible de stopper l'application.", e);
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible de stopper l'application",
                    "Erreur interne : \n" + Exceptions.causes(e)
            );
        }
    }

    @FXML
    private void afficherPreferences(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Préférences");
        // TODO FDA 2017/04 Coder.
    }


    /*
    Menu "Editer" :
     */

    /**
     * Annuler (undo)
     *
     * @param event
     */
    @FXML
    private void annuler(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("> Editer > Annuler");
        try {
            getSuiviActionsUtilisateur().annulerAction();
        } catch (SuiviActionsUtilisateurException e) {
            throw new Exception("Impossible d'annuler l'action de l'utilisateur.", e);
        }
    }

    /**
     * Rétablir (redo)
     *
     * @param event
     */
    @FXML
    private void retablir(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("> Editer > Rétablir");
        try {
            getSuiviActionsUtilisateur().retablirAction();
        } catch (SuiviActionsUtilisateurException e) {
            throw new Exception("Impossible de rétablir l'action de l'utilisateur.", e);
        }
    }

    /**
     * Répéter (repeat)
     *
     * @param event
     */
    @FXML
    private void repeter(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("> Editer > Répéter");
        try {
            getSuiviActionsUtilisateur().repeterAction();
        } catch (SuiviActionsUtilisateurException e) {
            throw new Exception("Impossible de répéter l'action de l'utilisateur.", e);
        }
    }

    /**
     * Supprimer
     *
     * @param event
     */
    @FXML
    private void supprimer(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("> Editer > Supprimer");
        try {
            supprimer();
        } catch (ControllerException e) {
            throw new Exception("Impossible de supprimer.", e);
        }
    }

    @SuppressWarnings("RedundantThrowsDeclaration")
    private void supprimer() throws ControllerException {
        // TODO FDA 2017/04 Coder.
        throw new NotImplementedException();
//        moduleCourant.supprimer();
    }


    /*
    Menu "Gérer" :
     */
    /*


    @FXML
    private void afficherModuleDisponibilites(ActionEvent event) {
        LOGGER.debug("> Disponibilites");
        afficherModuleDisponibilites();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleDisponibilites());
    }

    @FXML
    private void afficherModuleTaches(ActionEvent event) {
        LOGGER.debug("> Tâches");
        afficherModuleTaches();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleTaches());
    }

    @FXML
    private void afficherModuleCharges(ActionEvent event) {
        afficherModuleCharges();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleCharges());
    }

    private void afficherModuleCharges() {
        LOGGER.debug("> Charges");
        afficherModuleCharges();
    }
*/

    /*
    Menu "Aide" :
     */

    @FXML
    private void aPropos(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Aide > A propos");

        String versionApp = null;
        try {
            versionApp = paramsIhm.getParametrage("application.version");
        } catch (KernelException e) {
            LOGGER.error("Impossible de récupérer la version de l'application.", e);
        }

        //noinspection HardcodedFileSeparator,HardcodedLineSeparator
        ihm.afficherDialog(
                Alert.AlertType.INFORMATION,
                "A propos de l'application \"" + PlanChargeIhm.APP_NAME + "\"",
                "Fonctionnalité : Gestion du plan de charge d'une équipe d'un centre de service."
                        + "\n" + "Version : " + (versionApp == null ? "N/C" : versionApp)
                        + "\nAuteur : Frédéric Danna",
                500, 200
        );
    }


    /*
    Modules :
     */


    @FXML
    private void afficherModuleJoursFeries(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        try {
            afficherModuleJoursFeries();
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'afficher le module des jours fériés.", e);
            ihm.afficherDialog(Alert.AlertType.ERROR, "Impossible d'afficher le module des jours fériés", Exceptions.causes(e));
        }
    }

    public void afficherModuleJoursFeries() throws ControllerException {
        LOGGER.debug("> [...] > Module \"Jours fériés\"");

        //noinspection ObjectEquality,EqualityOperatorComparesObjects
        if (moduleCourant == ihm.getJoursFeriesController()) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        Module modulePrecedent = moduleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'moduleCourant', donc il faut le mémoriser avant.
        activerModuleJoursFeries();
        try {
            getSuiviActionsUtilisateur().historiser(new AffichageModuleJoursFeries(modulePrecedent));
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'historiser l'action de l'utilisateur.", e);
        }
    }

    public void activerModuleJoursFeries() throws ControllerException {
        moduleCourant = ihm.getJoursFeriesController();
        contentPane.getChildren().setAll(ihm.getJoursFeriesView());
//        ihm.getJoursFeriesController().fireActivation();
        majTitre();
    }

    @FXML
    private void afficherModuleRessourcesHumaines(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        try {
            afficherModuleRessourcesHumaines();
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'afficher le module des ressources humaines.", e);
            ihm.afficherDialog(Alert.AlertType.ERROR, "Impossible d'afficher le module des ressources humaines", Exceptions.causes(e));
        }
    }

    public void afficherModuleRessourcesHumaines() throws ControllerException {
        LOGGER.debug("> [...] > Module \"Ressources humaines\"");

        //noinspection ObjectEquality,EqualityOperatorComparesObjects
        if (moduleCourant == ihm.getRessourcesHumainesController()) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        Module modulePrecedent = moduleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'moduleCourant', donc il faut le mémoriser avant.
        activerModuleRessourcesHumaines();
        try {
            getSuiviActionsUtilisateur().historiser(new AffichageModuleJoursFeries(modulePrecedent));
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'historiser l'action de l'utilisateur.", e);
        }
    }

    public void activerModuleRessourcesHumaines() throws ControllerException {
        moduleCourant = ihm.getRessourcesHumainesController();
        contentPane.getChildren().setAll(ihm.getRessourcesHumainesView());
//        ihm.getRessourcesHumainesController().fireActivation();
        majTitre();
    }


    @FXML
    private void afficherModuleProjetsApplis(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        try {
            afficherModuleProjetsApplis();
        } catch (ControllerException e) {
            //noinspection HardcodedFileSeparator
            LOGGER.error("Impossible d'afficher le module des projets/applis.", e);
            //noinspection HardcodedFileSeparator
            ihm.afficherDialog(Alert.AlertType.ERROR, "Impossible d'afficher le module des projets/applis", Exceptions.causes(e));
        }
    }

    public void afficherModuleProjetsApplis() throws ControllerException {
        //noinspection HardcodedFileSeparator
        LOGGER.debug("> [...] > Module \"Projets / Applis\"");

        //noinspection ObjectEquality,EqualityOperatorComparesObjects
        if (moduleCourant == ihm.getProjetsApplisController()) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        Module modulePrecedent = moduleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'moduleCourant', donc il faut le mémoriser avant.
        activerModuleProjetsApplis();
        try {
            getSuiviActionsUtilisateur().historiser(new AffichageModuleJoursFeries(modulePrecedent));
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'historiser l'action de l'utilisateur.", e);
        }
    }

    public void activerModuleProjetsApplis() throws ControllerException {
        moduleCourant = ihm.getProjetsApplisController();
        contentPane.getChildren().setAll(ihm.getProjetsApplisView());
//        ihm.getProjetsApplisController().fireActivation();
        majTitre();
    }


    @FXML
    private void afficherModuleDisponibilites(@SuppressWarnings("unused") @NotNull ActionEvent event) {
        try {
            afficherModuleDisponibilites();
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'afficher le module des disponibilités.", e);
            ihm.afficherDialog(Alert.AlertType.ERROR, "Impossible d'afficher le module des disponibilités", Exceptions.causes(e));
        }
    }

    public void afficherModuleDisponibilites() throws ControllerException {
        LOGGER.debug("> [...] > Module \"Disponibilités\"");

        //noinspection ObjectEquality,EqualityOperatorComparesObjects
        if (moduleCourant == ihm.getDisponibilitesController()) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        Module modulePrecedent = moduleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'moduleCourant', donc il faut le mémoriser avant.
        activerModuleDisponibilites();
        try {
            getSuiviActionsUtilisateur().historiser(new AffichageModuleDisponibilites(modulePrecedent));
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'historiser l'action de l'utilisateur.", e);
        }
    }

    public void activerModuleDisponibilites() throws ControllerException {
        moduleCourant = ihm.getDisponibilitesController();
        contentPane.getChildren().setAll(ihm.getDisponibilitesView());
//        ihm.getDisponibilitesController().fireActivation();
        majTitre();
    }

    @FXML
    private void afficherModuleTaches(@SuppressWarnings("unused") @NotNull ActionEvent event) {
        try {
            afficherModuleTaches();
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'afficher le module des tâches.", e);
            ihm.afficherDialog(Alert.AlertType.ERROR, "Impossible d'afficher le module des tâches", Exceptions.causes(e));
        }
    }

    public void afficherModuleTaches() throws ControllerException {
        LOGGER.debug("> [...] > Module \"Tâches\"");

        //noinspection ObjectEquality,EqualityOperatorComparesObjects
        if (moduleCourant == ihm.getTachesController()) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        Module modulePrecedent = moduleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'moduleCourant', donc il faut le mémoriser avant.
        activerModuleTaches();
        try {
            getSuiviActionsUtilisateur().historiser(new AffichageModuleTaches(modulePrecedent));
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'historiser l'action de l'utilisateur.", e);
        }
    }

    public void activerModuleTaches() throws ControllerException {
        moduleCourant = ihm.getTachesController();
//        ihm.getTachesController().definirMenuContextuel();
        contentPane.getChildren().setAll(ihm.getTachesView());
//        ihm.getTachesController().fireActivation();
        majTitre();
    }

    @FXML
    private void afficherModuleCharges(@SuppressWarnings("unused") @NotNull ActionEvent event) {
        try {
            afficherModuleCharges();
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'afficher le module des charges.", e);
            ihm.afficherDialog(Alert.AlertType.ERROR, "Impossible d'afficher le module des charges", Exceptions.causes(e));
        }
    }

    public void afficherModuleCharges() throws ControllerException {
        LOGGER.debug("> [...] > Module \"Charges\"");

        //noinspection ObjectEquality,EqualityOperatorComparesObjects
        if (moduleCourant == ihm.getChargesController()) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        Module modulePrecedent = moduleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'moduleCourant', donc il faut le mémoriser avant.
        activerModuleCharges();
        try {
            getSuiviActionsUtilisateur().historiser(new AffichageModuleCharges(modulePrecedent));
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'historiser l'action de l'utilisateur.", e);
        }
    }

    public void activerModuleCharges() throws ControllerException {
        moduleCourant = ihm.getChargesController();
//        ihm.getChargesController().definirMenuContextuel();
        contentPane.getChildren().setAll(ihm.getChargesView());
//        ihm.getChargesController().fireActivation();
        majTitre();
    }


    public void deplierParametresPane() {
        parametresPane.setExpanded(true);
    }

    public void replierParametresPane() {
        parametresPane.setExpanded(false);
    }


    @FXML
    private void afficherAssistantDeRevue(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        afficherAssistantRevue();
    }

    public void afficherAssistantRevue() {
        LOGGER.debug("> [...] > Assistant \"Revue\"");

        ihm.getRevueWizardController().show();
    }


    @FXML
    private void afficherFenetreTracerRevision(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        afficherFenetreTracerRevision();
    }

    public void afficherFenetreTracerRevision() throws ControllerException {
        LOGGER.debug("> [...] > Fenêtre \"Tracer la révision\"");

        ihm.getTracerRevisionController().show();
    }

    @FXML
    private void afficherFenetreListerRevisions(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        afficherFenetreListerRevisions();
    }

    @FXML
    public void afficherFenetreListerRevisions() throws ControllerException {
        LOGGER.debug("> [...] > Fenêtre \"Lister les révisions\"");

        ihm.getListerRevisionsController().show();
    }


    @FXML
    private void definirDateEtat(@SuppressWarnings("unused") @NotNull ActionEvent event) throws Exception {
        LOGGER.debug("definirDateEtat...");
        try {
            LocalDate dateEtatActuelle = planChargeBean.getDateEtat();
            LocalDate dateEtatNouvelle = dateEtatPicker.getValue();

            if (Objects.equals(dateEtatActuelle, dateEtatNouvelle)) {
                return;
            }

            definirDateEtat(dateEtatNouvelle);

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ModificationDateEtat(dateEtatActuelle));

            majBarreEtat();

        } catch (ControllerException e) {
            throw new Exception("Impossible de définir la date d'état.", e);
        }
    }

    @SuppressWarnings({"MethodWithMoreThanThreeNegations", "WeakerAccess"})
    public void definirDateEtat(@Null LocalDate dateEtat) throws ControllerException {
        if (dateEtat != null) {
            if (dateEtat.getDayOfWeek() != DayOfWeek.MONDAY) {
                //noinspection AssignmentToMethodParameter
                dateEtat = dateEtat.plusDays((7L - (long) dateEtat.getDayOfWeek().getValue()) + 1L);// TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
            }
        }
        assert (dateEtat == null) || (dateEtat.getDayOfWeek() == DayOfWeek.MONDAY);

        boolean majEstRequise = false;
        if (!Objects.equals(dateEtat, planChargeBean.getDateEtat())) {
            planChargeBean.setDateEtat(dateEtat);
            majEstRequise = true;
        }
        if (!Objects.equals(dateEtat, dateEtatPicker.getValue())) {
            dateEtatPicker.setValue(dateEtat);
            majEstRequise = true;
        }

        if (majEstRequise) {

            majCalendriers();
            majTitre();

            // pour réappliquer les styles CSS, notamment pour faire afficher en violet les échéances < à la nouvelle date d'état.
            ihm.getTachesController().getTachesTable().refresh();
            ihm.getChargesController().getTachesTable().refresh();
        }
    }


/*
    @Null
    private LocalDate dateEtatPrecedentePourMajCalendriers = null;
*/

    private void majCalendriers() throws ControllerException {
/*
        LocalDate dateEtat = planChargeBean.getDateEtat();
        if ((dateEtatPrecedentePourMajCalendriers == null) || (dateEtat == null) || !dateEtat.equals(dateEtatPrecedentePourMajCalendriers)) {
*/
        definirNomsPeriodes();
        definirValeursCalendriers();
/*
        }
        if (dateEtat == null) {
            dateEtatPrecedentePourMajCalendriers = null;
        } else {
            dateEtatPrecedentePourMajCalendriers = LocalDate.of(dateEtat.getYear(), dateEtat.getMonth(), dateEtat.getDayOfMonth());
        }
*/
    }

    private void definirNomsPeriodes() throws ControllerException {

/*
        // Format = "S " + n° de semaine dans l'année + abréviation du nom du jour ("lun", "mar", etc.) + retour à la ligne + jour au format 'JJ/MM'.
        //noinspection HardcodedLineSeparator
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("'S 'w E\ndd/MM");
*/
        // Format = "S " + n° de semaine dans l'année.
        DateTimeFormatter noSemaineFormatter = DateTimeFormatter.ofPattern("'S 'w");
        // Format = jour au format 'JJ/MM'
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

        // Rq : Plus nécessaire de redéfinir les colonnes pour les TableView de getDisponibilitesController() autres que "*NbrsJoursOuvresTable",
        //      maintenant qu'on n'affiche plus les TableHeaderRow de ces TableView.

        //noinspection rawtypes
        List<PlanificationTableView> tables = new ArrayList<>(20);
        // Disponibilites :
        //noinspection CollectionAddAllCanBeReplacedWithConstructor
        tables.addAll(ihm.getDisponibilitesController().tables());
        // Charges :
        tables.addAll(ihm.getChargesController().tables());
        //
        //noinspection rawtypes
        for (PlanificationTableView table : tables) {
            //noinspection unchecked,rawtypes
            List<TableColumn> calendrierColumns = table.getCalendrierColumns();

            LocalDate dateDebutPeriode = null;
            LocalDate dateEtat = planChargeBean.getDateEtat();
            if (dateEtat != null) {
                dateDebutPeriode = LocalDate.of(dateEtat.getYear(), dateEtat.getMonth(), dateEtat.getDayOfMonth());
            }

            //noinspection rawtypes
            for (TableColumn calendrierColumn : calendrierColumns) {
                String titreColonne;
                if (dateDebutPeriode == null) {
                    //noinspection HardcodedFileSeparator
                    titreColonne = "N/C";
                } else {
                    //noinspection HardcodedLineSeparator
                    titreColonne = noSemaineFormatter.format(dateDebutPeriode)
                            + "\n" + ('[' + dateFormatter.format(dateDebutPeriode)
/*
                            + "\n.."
                            + "\n" + dateFormatter.format(dateFinPeriode)
*/
                            + ".."
                    );
                    dateDebutPeriode = dateDebutPeriode.plusDays(7); // TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
                }
                calendrierColumn.setText(titreColonne);
            }
        }
    }

    private void definirValeursCalendriers() {
        ihm.getDisponibilitesController().definirValeursCalendrier();
        ihm.getChargesController().definirValeursCalendrier();
    }


    @FXML
    private void positionnerDateEtatAuLundiPrecedent(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("positionnerDateEtatAuLundiPrecedent...");

        try {
            LocalDate dateEtatPrec = planChargeBean.getDateEtat();

            LocalDate dateEtat;
            if (planChargeBean.getDateEtat() == null) {
                dateEtat = LocalDate.now();
                if (dateEtat.getDayOfWeek() != DayOfWeek.MONDAY) {
                    dateEtat = dateEtat.minusDays((7L - (long) dateEtat.getDayOfWeek().getValue()));// TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
                }
            } else {
                assert planChargeBean.getDateEtat().getDayOfWeek() == DayOfWeek.MONDAY;
                dateEtat = planChargeBean.getDateEtat().minusDays(7L);// TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
            }
            assert dateEtat != null;
            assert dateEtat.getDayOfWeek() == DayOfWeek.MONDAY;

            definirDateEtat(dateEtat);

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ModificationDateEtat(dateEtatPrec));

            majBarreEtat();
        } catch (ControllerException e) {
            throw new Exception("Impossible de se positionner au lundi précédent.", e);
        }
    }

    @FXML
    private void positionnerDateEtatAuLundiSuivant(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("positionnerDateEtatAuLundiSuivant...");

        try {
            LocalDate dateEtatPrec = planChargeBean.getDateEtat();

            LocalDate dateEtat;
            if (planChargeBean.getDateEtat() == null) {
                dateEtat = LocalDate.now();
                if (dateEtat.getDayOfWeek() != DayOfWeek.MONDAY) {
                    dateEtat = dateEtat.plusDays((7L - (long) dateEtat.getDayOfWeek().getValue()) + 1L);// TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
                }
            } else {
                assert planChargeBean.getDateEtat().getDayOfWeek() == DayOfWeek.MONDAY;
                dateEtat = planChargeBean.getDateEtat().plusDays(7L);// TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
            }
            assert dateEtat.getDayOfWeek() == DayOfWeek.MONDAY;

            definirDateEtat(dateEtat);

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ModificationDateEtat(dateEtatPrec));

            majBarreEtat();
        } catch (ControllerException e) {
            throw new Exception("Impossible de se positionner au lundi suivant.", e);
        }
    }


    private boolean estAcceptableDePerdreDesDonnees() {
        if (!planChargeBean.aBesoinEtreSauvegarde()) {
            return true;
        }
        //noinspection HardcodedLineSeparator
        Optional<ButtonType> result = ihm.afficherDialog(
                Alert.AlertType.CONFIRMATION,
                "Perdre les modifications ?",
                "Des données ont été modifiées. Si vous continuez, ces modifications seront perdues."
                        + "\nContinuez-vous tout de même (en perdant les modifications) ?",
                400, 200,
                ButtonType.CANCEL, ButtonType.CANCEL
        );
/*
        if (!result.isPresent()) {
            // Ne devrait jamais arriver (je pense).
            return;
        }
*/
        //noinspection ConstantConditions
        if (result.get().equals(ButtonType.CANCEL)) {
            LOGGER.info("Action annulée par l'utilisateur, pour éviter de perdre des données.");
            return false;
        }
        return true;
    }


    @FXML
    private void calculer(@SuppressWarnings("unused") @NotNull Event event) {
        try {
            calculer();
        } catch (ControllerException e) {
            LOGGER.error("Impossible de calculer.", e);
            ihm.afficherDialog(Alert.AlertType.ERROR, "Impossible de calculer.", Exceptions.causes(e));
        }
    }

    public void calculer() throws ControllerException {
        LOGGER.debug("Calculs en cours...");

        // TODO FDA 2017/08 Afficher une barre de progression.
        ihm.getDisponibilitesController().calculerDisponibilites();
        ihm.getChargesController().calculerCharges();
        // Ajouter ici les calculs.

        planChargeBean.vientDEtreCalcule();
        majBarreEtat();

        ihm.afficherNotificationInfo(
                "Calcul terminé",
                "Les données (disponibilités, surcharges, etc.) ont été calculées."
        );
        LOGGER.debug("Calculs faits.");
    }


    public void appliquerThemeStandard(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        ihm.appliquerTheme(PlanChargeIhm.Theme.STANDARD);
    }

    public void appliquerThemeSombre(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        ihm.appliquerTheme(PlanChargeIhm.Theme.SOMBRE);
    }

}
