package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import eu.hansolo.medusa.Gauge;
import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.util.ParametresIhm;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationsReglesGestionException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur.CalculateurDisponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.rapportProgression.RapportChargementAvecProgression;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.rapportProgression.RapportImportPlanChargeAvecProgression;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.rapportProgression.RapportImportTachesAvecProgression;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.rapportProgression.RapportSauvegardeAvecProgression;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.TableViewAvecCalendrier;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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


    public enum NomModule {
        // Référentiels :
        JOURS_FERIES("Jours fériés"),
        RESSOURCES_HUMAINES("Ressources humaines"),
        // Gestion :
        DISPONIBILITES("Disponibilités"),
        TACHES("Tâches"),
        CHARGES("Charges");

        private String texte;

        NomModule(String texte) {
            this.texte = texte;
        }

        public String getTexte() {
            return texte;
        }
    }

    private NomModule nomModuleCourant = null;

    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean manqueMemoireDejaDetecte = false;
    @SuppressWarnings("BooleanVariableAlwaysNegated")
    private boolean alerteManqueMemoireAffichee = false;


    // Les menus :

    @FXML
    @NotNull
    private Menu menuEditer;
    @FXML
    @NotNull
    private MenuItem menuAnnuler;
    @FXML
    @NotNull
    private Menu sousMenuAnnuler;
    /*
        @FXML
        @NotNull
        private SeparatorMenuItem separateurMenusAnnuler;
    */
    @FXML
    @NotNull
    private MenuItem menuRetablir;
    @FXML
    @NotNull
    private Menu sousMenuRetablir;
    /*
        @FXML
        @NotNull
        private SeparatorMenuItem separateurMenusRetablir;
    */
    @FXML
    @NotNull
    private MenuItem menuRepeter;
    @FXML
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
    private Pane contentPane;

    @FXML
    @NotNull
    private DatePicker dateEtatPicker;


    // La barre d'état :

    @FXML
    @NotNull
    private CheckBox sauvegardeRequiseCheckbox;

    @FXML
    @NotNull
    private Label nbrTachesField;

    @FXML
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

    //    @Autowired
    @NotNull
    private final CalculateurDisponibilites calculateurDisponibilites = new CalculateurDisponibilites();

    // Les services métier :

    //    @Autowired
    @NotNull
    private PlanChargeService planChargeService = PlanChargeService.instance();


    // Les données métier :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<JourFerieBean> joursFeriesBeans = planChargeBean.getJoursFeriesBeans();

    @NotNull
    private ObservableList<PlanificationTacheBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    public ApplicationController.NomModule getNomModuleCourant() {
        return nomModuleCourant;
    }


    public ApplicationController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    public void initialize() throws IhmException {
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

        memoryGauge.setMaxValue(Runtime.getRuntime().maxMemory()); // Cf. https://stackoverflow.com/questions/3571203/what-are-runtime-getruntime-totalmemory-and-freememory?answertab=votes#tab-top
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
                double usedMem = Runtime.getRuntime().maxMemory() - Runtime.getRuntime().freeMemory();
                memoryGauge.setValue(usedMem);
            }
        };
        memoryGauge.valueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

            long maxMem = Runtime.getRuntime().maxMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            long freeMem = Runtime.getRuntime().freeMemory() + (maxMem - totalMem);
            long usedMem = maxMem - freeMem; // // Cf. https://stackoverflow.com/questions/3571203/what-are-runtime-getruntime-totalmemory-and-freememory?answertab=votes#tab-top
            int pcMemLibre = new Double(((freeMem * 100) / maxMem)).intValue();

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
                    ihm.afficherPopUp(
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


    public void majTitre() {
        String titre = PlanChargeIhm.APP_NAME;
        //noinspection HardcodedFileSeparator
        titre += " - " + fr.gouv.agriculture.dal.ct.planCharge.util.Objects.value(planChargeBean.getDateEtat(), localDate -> localDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")), "N/C");
        //noinspection HardcodedFileSeparator
        titre += " - " + fr.gouv.agriculture.dal.ct.planCharge.util.Objects.value(nomModuleCourant, nomModule -> nomModuleCourant.getTexte(), "N/C");
        ihm.definirTitre(titre);
    }

    public void majBarreEtat() {
        LOGGER.debug("majBarreEtat...");
        sauvegardeRequiseCheckbox.setSelected(planChargeBean.aBesoinEtreSauvegarde());
        definirNbrTachesDansBarreEtat();
    }


    private void definirNbrTachesDansBarreEtat() {
        nbrTachesField.setText(String.valueOf(planChargeBean.getPlanificationsBeans().size()));
    }


    /*
    Menu "Fichier" :
     */

    @FXML
    private void nouveau(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Nouveau");

        if (!perteDonneesAceeptee()) {
            return;
        }

        try {
            reinitPlanCharge();
        } catch (IhmException e) {
            LOGGER.error("Impossible de ré-initialiser un nouveau plan de charge.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de ré-initialiser un nouveau plan de charge",
                    "Impossible de ré-initialiser un nouveau plan de charge : \n" + Exceptions.causes(e),
                    400, 200
            );
        }
    }

    private void reinitPlanCharge() throws IhmException {
        definirDateEtat((LocalDate) null);
        ObservableList[] listesBeans = {
                // Référentiels :
                planChargeBean.getJoursFeriesBeans(),
                planChargeBean.getJoursFeriesBeans(),
                planChargeBean.getImportancesBeans(),
                planChargeBean.getProfilsBeans(),
                planChargeBean.getStatutsBeans(),
                planChargeBean.getRessourcesBeans(),
                // Disponibilités :
                planChargeBean.getNbrsJoursAbsenceBeans(),
                // Tâches + Charge
                planChargeBean.getPlanificationsBeans()
        };
        for (ObservableList listeDonnees : listesBeans) {
            listeDonnees.clear();
        }

        majTitre();
        majBarreEtat();
    }

    @FXML
    private void charger(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Charger");

        if (!perteDonneesAceeptee()) {
            return;
        }

        try {
            charger();
        } catch (IhmException e) {
            LOGGER.error("Impossible de charger le plan de charge.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de charger le plan de charge",
                    "Impossible de charger le plan de charge : \n" + Exceptions.causes(e),
                    400, 200
            );
        }
    }

    private void charger() throws IhmException {

        File ficCalc;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Indiquez le fichier XML qui contient un plan de charge : ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichier XML", "*.xml")
        );
        try {
            fileChooser.setInitialDirectory(new File(paramsMetier.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE)));
        } catch (KernelException e) {
            throw new IhmException("Impossible de déterminer le répertoire de persistance du plan de charge (fichiers XML).", e);
        }
        ficCalc = fileChooser.showOpenDialog(ihm.getPrimaryStage());
        if (ficCalc == null) {
            ihm.afficherPopUp(
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
        } catch (Exception e) {
            LOGGER.error("Impossible de charger le plan de charge.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de charger le plan de charge",
                    Exceptions.causes(e),
                    800, 300
            );
        }
    }

    private void charger(@NotNull File ficPlanCharge) throws IhmException {

        RapportChargementAvecProgression rapport = new RapportChargementAvecProgression();

        Task<RapportChargementAvecProgression> chargerPlanCharge = new Task<RapportChargementAvecProgression>() {

            @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
            @Override
            protected RapportChargementAvecProgression call() throws Exception {

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.intValue(), rapport.getProgressionMax()));

                PlanChargeBean planChargeBeanAvantChargement = planChargeBean.copier();

                PlanChargeDTO planCharge = planChargeService.charger(ficPlanCharge, rapport);

                planChargeBean.fromDto(planCharge);

                planChargeBean.vientDEtreCharge();
                getSuiviActionsUtilisateur().historiser(new ChargementPlanCharge(planChargeBeanAvantChargement));

/*
                // TODO FDA 2017/08 La liste contenant les référentiels devraient être chargées au démarrage de l'appli, mais tant que les référentiels seront bouchonnés on n'a pas le choix.
                rapport.setAvancement("Alimentation des référentiels...");
                ihm.getTachesController().populerReferentiels();
                ihm.getChargesController().populerReferentiels();

                // TODO FDA 2017/08 Les listes des filtres devraient être chargées au démarrage de l'appli, mais tant que les référentiels seront bouchonnés on n'a pas le choix.
                rapport.setAvancement("Alimentation des filtres...");
                ihm.getTachesController().populerFiltres();
                ihm.getChargesController().populerFiltres();
*/

                return rapport;
            }
        };

        try {
            calculateurDisponibilites.executerPuisCalculer(
                    () -> {
                        try {

                    /*RapportChargementAvecProgression rapportFinal = */
                            ihm.afficherProgression("Chargement du plan de charge...", chargerPlanCharge);
//                  assert rapportFinal != null;

                            definirDateEtat(planChargeBean.getDateEtat());

                            ihm.getTachesController().razFiltres();
                            ihm.getChargesController().razFiltres();

                            ihm.afficherNotification(
                                    "Chargement terminé",
                                    "Le chargement est terminé :"
                                            + "\n- date d'état : " + planChargeBean.getDateEtat()
                                            + "\n- " + planChargeBean.getPlanificationsBeans().size() + " tâches"
                            );

                            afficherModuleCharges(); // Rq : Simule une action de l'utilisateur (l'action peut être "undone" (Ctrl+Z), etc.).

                            majBarreEtat();

                        } catch (ViolationsReglesGestionException e) {
                            ihm.afficherViolationsReglesGestion(
                                    "Impossible de sauver le plan de charge.", e.getLocalizedMessage(),
                                    e.getViolations()
                            );
                        }
                    },
                    () -> calculateurDisponibilites.calculer()
            );
        } catch (IhmException e) {
            throw new IhmException("Impossible de charger le plan de charge depuis le fichier '" + ficPlanCharge.getAbsolutePath() + "'.", e);
        }
    }


    // TODO FDA 23017/02 Afficher une "progress bar".
    @FXML
    private void sauver(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Sauver");

        if ((planChargeBean.getDateEtat() == null) || planChargeBean.getPlanificationsBeans().isEmpty()) {
            LOGGER.warn("Impossible de sauver un plan de charge non défini.");
            ihm.afficherPopUp(
                    Alert.AlertType.WARNING,
                    "Impossible de sauver le plan de charge",
                    "Impossible de sauver un plan de charge sans date d'état, ou sans planification.",
                    500, 200
            );
            return;
        }

        final RapportSauvegardeAvecProgression rapport = new RapportSauvegardeAvecProgression();

        Task<RapportSauvegardeAvecProgression> sauvegarder = new Task<RapportSauvegardeAvecProgression>() {

            @Override
            protected RapportSauvegardeAvecProgression call() throws Exception {

                rapport.setProgressionMax(1);

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.intValue(), rapport.getProgressionMax()));

                PlanChargeDTO planCharge = planChargeBean.toDto();

                planChargeService.sauver(planCharge, rapport);

                rapport.setProgressionCourante(1);

                return rapport;
            }
        };

        try {

            RapportSauvegarde rapportFinal = ihm.afficherProgression("Sauvegarde...", sauvegarder);
            assert rapportFinal != null;

            planChargeBean.vientDEtreSauvegarde();
            getSuiviActionsUtilisateur().historiser(new SauvegardePlanCharge());

            //noinspection unused
            File ficPlanCharge = planChargeService.fichierPersistancePlanCharge(planChargeBean.getDateEtat());
            ihm.afficherNotification("Sauvegarde effectuée.",
                    "Le plan de charge"
                            + " en date du " + planChargeBean.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                            + " a été sauvegardé"
//                            +" (dans le fichier '" + ficPlanCharge.getAbsolutePath() + "')"
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
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de sauver le plan de charge.",
                    Exceptions.causes(e),
                    500, 200
            );
        }
    }

    @FXML
    private void importerTachesDepuisCalc(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Importer > Taches depuis Calc");
        try {
            importerTachesDepuisCalc();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'importer les tâches.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'importer les tâches",
                    "Impossible d'importer les tâches : \n" + Exceptions.causes(e),
                    400, 200
            );
        }
    }

    private void importerTachesDepuisCalc() throws IhmException {
        File ficCalc;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Indiquez le fichier Calc (LibreOffice) qui contient les tâches ('suivi des demandes'') : ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("LibreOffice Calc", "*.ods")
        );
        String nomRepFicCalc;
        try {
            // TODO FDA 2017/05 C'est le répertoire des XML, pas forcément des ODS. Plutôt regarder dans les préférences de l'utilisateur ?
            nomRepFicCalc = paramsMetier.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE);
        } catch (KernelException e) {
            throw new IhmException("Impossible de déterminer le répertoire de persistance.", e);
        }
        fileChooser.setInitialDirectory(new File(nomRepFicCalc));
        ficCalc = fileChooser.showOpenDialog(ihm.getPrimaryStage());
        if (ficCalc == null) {
            ihm.afficherPopUp(
                    Alert.AlertType.INFORMATION,
                    "MàJ annulée",
                    "La mise à jour des tâches depuis un fichier Calc a été annulé par l'utilisateur.",
                    400, 200
            );
            return;
        }

        importerTachesDepuisCalc(ficCalc);
    }

    private void importerTachesDepuisCalc(@NotNull File ficCalc) throws ControllerException {

        final RapportImportTachesAvecProgression rapport = new RapportImportTachesAvecProgression();

        Task<RapportImportTachesAvecProgression> importerTachesDepuisCalc = new Task<RapportImportTachesAvecProgression>() {

            @Override
            protected RapportImportTachesAvecProgression call() throws Exception {

                rapport.setProgressionMax(1);

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.intValue(), rapport.getProgressionMax()));

                PlanChargeDTO planCharge = planChargeBean.toDto();

                planChargeService.majTachesDepuisCalc(planCharge, ficCalc, rapport);

                planChargeBean.fromDto(planCharge);

/*
                // TODO FDA 2017/08 La liste contenant les référentiels devraient être chargées au démarrage de l'appli, mais tant que les référentiels seront bouchonnés on n'a pas le choix.
                rapport.setAvancement("Alimentation des référentiels...");
                ihm.getTachesController().populerReferentiels();
                ihm.getChargesController().populerReferentiels();

                // TODO FDA 2017/08 Les listes des filtres devraient être chargées au démarrage de l'appli, mais tant que les référentiels seront bouchonnés on n'a pas le choix.
                rapport.setAvancement("Alimentation des filtres...");
                ihm.getTachesController().populerFiltres();
                ihm.getChargesController().populerFiltres();
*/

                return rapport;
            }
        };

        try {

            RapportImportTaches rapportFinal = ihm.afficherProgression("Import des tâches", importerTachesDepuisCalc);
            assert rapport != null;

            definirDateEtat(planChargeBean.getDateEtat());

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ImportTaches());

            ihm.afficherNotification("Tâches mises à jour importées",
                    "Les tâches ont été mises à jour : "
                            + "\n- depuis le fichier : " + ficCalc.getAbsolutePath()
                            + "\n- nombre de tâches initial : " + rapportFinal.getNbrTachesPlanifiees()
                            + "\n- nombre de lignes importées : " + rapportFinal.getNbrTachesImportees()
                            + "\n- nombre de tâches mises à jour : " + rapportFinal.getNbrTachesMisesAJour()
                            + "\n- nombre de tâches ajoutées : " + rapportFinal.getNbrTachesAjoutees()
                            + "\n- nombre de tâches supprimées : " + rapportFinal.getNbrTachesSupprimees()
                            + "\n- nombre de tâches au final : " + planChargeBean.getPlanificationsBeans().size()
            );

            afficherModuleTaches();

            majBarreEtat();
        } catch (ViolationsReglesGestionException e) {
            ihm.afficherViolationsReglesGestion(
                    "Impossible de sauver le plan de charge.", e.getLocalizedMessage(),
                    e.getViolations()
            );
        } catch (IhmException e) {
            throw new ControllerException("Impossible de mettre à jour les tâches depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        }
    }

    @FXML
    private void importerPlanChargeDepuisCalc(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Importer > Plan charge depuis Calc");

        if (!perteDonneesAceeptee()) {
            return;
        }

        try {
            importerPlanChargeDepuisCalc();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'importer le plan de charge.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'importer le plan de charge",
                    "Impossible d'importer le plan de charge : \n" + Exceptions.causes(e),
                    400, 200
            );
        }
    }

    private void importerPlanChargeDepuisCalc() throws IhmException {
        File ficCalc;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Indiquez le fichier Calc (LIbreOffice) qui contient un plan de charge : ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("LibreOffice Calc", "*.ods")
        );
        String nomRepFicCalc;
        try {
            // TODO FDA 2017/05 C'est le répertoire des XML, pas forcément des ODS. Plutôt regarder dans les préférences de l'utilisateur ?
            nomRepFicCalc = paramsMetier.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE);
        } catch (KernelException e) {
            throw new IhmException("Impossible de déterminer le répertoire de persistance du plan de charge.", e);
        }
        // TODO FDA 2017/05 Vérifier que le répertoire existe. Si non, le créer ?
        fileChooser.setInitialDirectory(new File(nomRepFicCalc));
        ficCalc = fileChooser.showOpenDialog(ihm.getPrimaryStage());
        if (ficCalc == null) {
            ihm.afficherPopUp(
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

        final RapportImportPlanChargeAvecProgression rapport = new RapportImportPlanChargeAvecProgression();

        Task<RapportImportPlanChargeAvecProgression> importerPlanChargeDepuisCalc = new Task<RapportImportPlanChargeAvecProgression>() {

            @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
            @Override
            protected RapportImportPlanChargeAvecProgression call() throws Exception {

                rapport.setProgressionMax(1);

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.intValue(), rapport.getProgressionMax()));

                rapport.setAvancement("Import depuis Calc...");
                PlanChargeDTO planCharge = planChargeService.importerDepuisCalc(ficCalc, rapport);

                planChargeBean.fromDto(planCharge);

/*
                // TODO FDA 2017/08 La liste contenant les référentiels devraient être chargées au démarrage de l'appli, mais tant que les référentiels seront bouchonnés on n'a pas le choix.
                rapport.setAvancement("Alimentation des référentiels...");
                ihm.getTachesController().populerReferentiels();
                ihm.getChargesController().populerReferentiels();

                // TODO FDA 2017/08 Les listes des filtres devraient être chargées au démarrage de l'appli, mais tant que les référentiels seront bouchonnés on n'a pas le choix.
                rapport.setAvancement("Alimentation des filtres...");
                ihm.getTachesController().populerFiltres();
                ihm.getChargesController().populerFiltres();
*/

                return rapport;
            }
        };

        try {

            RapportImportPlanCharge rapportFinal = ihm.afficherProgression("Import du plan de charge", importerPlanChargeDepuisCalc);
            assert rapportFinal != null;

            definirDateEtat(planChargeBean.getDateEtat());

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ImportPlanCharge());

            ihm.afficherNotification("Données importées",
                    "Le plan de charge a été importé : "
                            + "\n- depuis le fichier : " + ficCalc.getAbsolutePath()
                            + "\n- date d'état : " + planChargeBean.getDateEtat()
                            + "\n- nombre de lignes/tâches importées :" + planChargeBean.getPlanificationsBeans().size()
            );

            afficherModuleCharges();

            majBarreEtat();
        } catch (ViolationsReglesGestionException e) {
            ihm.afficherViolationsReglesGestion(
                    "Impossible de sauver le plan de charge.", e.getLocalizedMessage(),
                    e.getViolations()
            );
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'importer le plan de charge depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        }
    }

    @FXML
    private void quitter(@SuppressWarnings("unused") ActionEvent event) {
        LOGGER.debug("> Fichier > Quitter");

        if (!perteDonneesAceeptee()) {
            return;
        }

        try {
            ihm.stop();
        } catch (Exception e) {
            LOGGER.error("Impossible de stopper l'application.", e);
            ihm.afficherPopUp(
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
        } catch (IhmException e) {
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
        } catch (IhmException e) {
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
        } catch (IhmException e) {
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
        } catch (IhmException e) {
            throw new Exception("Impossible de supprimer.", e);
        }
    }

    private void supprimer() throws IhmException {
        // TODO FDA 2017/04 Coder.
        throw new NotImplementedException();
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

        //noinspection HardcodedFileSeparator
        ihm.afficherPopUp(
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
    private void afficherModuleJoursFeries(@SuppressWarnings("unused") ActionEvent actionEvent) {
        try {
            afficherModuleJoursFeries();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le module des jours fériés.", e);
            ihm.afficherPopUp(Alert.AlertType.ERROR, "Impossible d'afficher le module des jours fériés", Exceptions.causes(e));
        }
    }

    public void afficherModuleJoursFeries() throws IhmException {
        LOGGER.debug("> [...] > Module \"Jours fériés\"");

        if (nomModuleCourant == ApplicationController.NomModule.JOURS_FERIES) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        final NomModule nomModulePrecedent = nomModuleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'nomModuleCourant', donc il faut le mémoriser avant.
        activerModuleJoursFeries();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleJoursFeries(nomModulePrecedent));
    }

    public void activerModuleJoursFeries() throws IhmException {
        nomModuleCourant = NomModule.JOURS_FERIES;
        contentPane.getChildren().setAll(ihm.getJoursFeriesView());
//        ihm.getJoursFeriesController().fireActivation();
        majTitre();
    }

    @FXML
    private void afficherModuleRessourcesHumaines(@SuppressWarnings("unused") ActionEvent actionEvent) {
        try {
            afficherModuleRessourcesHumaines();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le module des ressrouces humaines.", e);
            ihm.afficherPopUp(Alert.AlertType.ERROR, "Impossible d'afficher le module des ressources humaines", Exceptions.causes(e));
        }
    }

    public void afficherModuleRessourcesHumaines() throws IhmException {
        LOGGER.debug("> [...] > Module \"Ressources humaines\"");

        if (nomModuleCourant == NomModule.RESSOURCES_HUMAINES) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        final NomModule nomModulePrecedent = nomModuleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'nomModuleCourant', donc il faut le mémoriser avant.
        activerModuleRessourcesHumaines();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleJoursFeries(nomModulePrecedent));
    }

    public void activerModuleRessourcesHumaines() throws IhmException {
        nomModuleCourant = NomModule.RESSOURCES_HUMAINES;
        contentPane.getChildren().setAll(ihm.getRessourcesHumainesView());
//        ihm.getRessourcesHumainesController().fireActivation();
        majTitre();
    }


    //    @FXML
    public void afficherModuleDisponibilites(@SuppressWarnings("unused") ActionEvent event) {
        try {
            afficherModuleDisponibilites();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le module des disponibilités.", e);
            ihm.afficherPopUp(Alert.AlertType.ERROR, "Impossible d'afficher le module des disponibilités", Exceptions.causes(e));
        }
    }

    public void afficherModuleDisponibilites() throws IhmException {
        LOGGER.debug("> [...] > Module \"Disponibilités\"");

        if (nomModuleCourant == NomModule.DISPONIBILITES) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        final NomModule nomModulePrecedent = nomModuleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'nomModuleCourant', donc il faut le mémoriser avant.
        activerModuleDisponibilites();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleDisponibilites(nomModulePrecedent));
    }

    public void activerModuleDisponibilites() throws IhmException {
        nomModuleCourant = ApplicationController.NomModule.DISPONIBILITES;
        contentPane.getChildren().setAll(ihm.getDisponibilitesView());
//        ihm.getDisponibilitesController().fireActivation();
        majTitre();
    }

    //    @FXML
    public void afficherModuleTaches(@SuppressWarnings("unused") ActionEvent event) {
        try {
            afficherModuleTaches();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le module des tâches.", e);
            ihm.afficherPopUp(Alert.AlertType.ERROR, "Impossible d'afficher le module des tâches", Exceptions.causes(e));
        }
    }

    public void afficherModuleTaches() throws IhmException {
        LOGGER.debug("> [...] > Module \"Tâches\"");

        if (nomModuleCourant == ApplicationController.NomModule.TACHES) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        final NomModule nomModulePrecedent = nomModuleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'nomModuleCourant', donc il faut le mémoriser avant.
        activerModuleTaches();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleTaches(nomModulePrecedent));
    }

    public void activerModuleTaches() throws IhmException {
        nomModuleCourant = NomModule.TACHES;
//        ihm.getTachesController().definirMenuContextuel();
        contentPane.getChildren().setAll(ihm.getTachesView());
//        ihm.getTachesController().fireActivation();
        majTitre();
    }

    //    @FXML
    public void afficherModuleCharges(@SuppressWarnings("unused") ActionEvent event) {
        try {
            afficherModuleCharges();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le module des charges.", e);
            ihm.afficherPopUp(Alert.AlertType.ERROR, "Impossible d'afficher le module des charges", Exceptions.causes(e));
        }
    }

    public void afficherModuleCharges() throws IhmException {
        LOGGER.debug("> [...] > Module \"Charges\"");

        if (nomModuleCourant == NomModule.CHARGES) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        final NomModule nomModulePrecedent = nomModuleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'nomModuleCourant', donc il faut le mémoriser avant.
        activerModuleCharges();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleCharges(nomModulePrecedent));
    }

    public void activerModuleCharges() throws IhmException {
        nomModuleCourant = ApplicationController.NomModule.CHARGES;
//        ihm.getChargesController().definirMenuContextuel();
        contentPane.getChildren().setAll(ihm.getChargesView());
//        ihm.getChargesController().fireActivation();
        majTitre();
    }


    @FXML
    private void definirDateEtat(@SuppressWarnings("unused") ActionEvent event) throws Exception {
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

        } catch (IhmException e) {
            throw new Exception("Impossible de définir la date d'état.", e);
        }
    }

    public void definirDateEtat(@Null LocalDate dateEtat) throws IhmException {
        if (dateEtat != null) {
            if (dateEtat.getDayOfWeek() != DayOfWeek.MONDAY) {
                dateEtat = dateEtat.plusDays((7 - dateEtat.getDayOfWeek().getValue()) + 1);
            }
        }
        assert (dateEtat == null) || (dateEtat.getDayOfWeek() == DayOfWeek.MONDAY);

        if ((dateEtat == null) || !dateEtat.equals(planChargeBean.getDateEtat())) {
            planChargeBean.setDateEtat(dateEtat);
        }
        if ((dateEtat == null) || !dateEtat.equals(dateEtatPicker.getValue())) {
            dateEtatPicker.setValue(dateEtat);
        }

        majCalendriers();
        majTitre();
    }


    @Null
    private LocalDate dateEtatPrecedentePourMajCalendriers = null;

    private void majCalendriers() throws IhmException {
        LocalDate dateEtat = planChargeBean.getDateEtat();
        if ((dateEtatPrecedentePourMajCalendriers == null) || (dateEtat == null) || !dateEtat.equals(dateEtatPrecedentePourMajCalendriers)) {
            definirNomsPeriodes();
            definirValeursCalendriers();
        }
        if (dateEtat != null) {
            dateEtatPrecedentePourMajCalendriers = LocalDate.of(dateEtat.getYear(), dateEtat.getMonth(), dateEtat.getDayOfMonth());
        }
    }

    private void definirNomsPeriodes() throws IhmException {

/*
        // Format = "S " + n° de semaine dans l'année + abréviation du nom du jour ("lun", "mar", etc.) + retour à la ligne + jour au format 'JJ/MM'.
        //noinspection HardcodedLineSeparator
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("'S 'w E\ndd/MM");
*/
        // Format = "S " + n° de semaine dans l'année + retour à la ligne + jour au format 'JJ/MM'.
        //noinspection HardcodedLineSeparator
        DateTimeFormatter noSemaineFormatter = DateTimeFormatter.ofPattern("'S 'w");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

        // Rq : Plus nécessaire de redéfinir les colonnes pour les TableView de getDisponibilitesController() autres que "*NbrsJoursOuvresTable",
        //      maintenant qu'on n'affiche plus les TableHeaderRow de ces TableView.

        List<TableViewAvecCalendrier> tables = new ArrayList<>();
        // Disponibilites :
        tables.addAll(ihm.getDisponibilitesController().tables());
        // Charges :
        tables.add(ihm.getChargesController().getTachesTable());
        //
        for (TableViewAvecCalendrier table : tables) {
            //noinspection unchecked
            List<TableColumn> calendrierColumns = table.getCalendrierColumns();

            LocalDate dateDebutPeriode = null;
            LocalDate dateEtat = planChargeBean.getDateEtat();
            if (dateEtat != null) {
                dateDebutPeriode = LocalDate.of(dateEtat.getYear(), dateEtat.getMonth(), dateEtat.getDayOfMonth());
            }

            for (TableColumn calendrierColumn : calendrierColumns) {
                String titreColonne;
                if (dateDebutPeriode == null) {
                    //noinspection HardcodedFileSeparator
                    titreColonne = "N/C";
                } else {
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
                    dateEtat = dateEtat.minusDays((7 - dateEtat.getDayOfWeek().getValue()) + 1);
                }
            } else {
                assert planChargeBean.getDateEtat().getDayOfWeek() == DayOfWeek.MONDAY;
                dateEtat = planChargeBean.getDateEtat().minusDays(7);
            }
            assert dateEtat != null;
            assert dateEtat.getDayOfWeek() == DayOfWeek.MONDAY;

            definirDateEtat(dateEtat);

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ModificationDateEtat(dateEtatPrec));

            majBarreEtat();
        } catch (IhmException e) {
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
                    dateEtat = dateEtat.plusDays((7 - dateEtat.getDayOfWeek().getValue()) + 1);
                }
            } else {
                assert planChargeBean.getDateEtat().getDayOfWeek() == DayOfWeek.MONDAY;
                dateEtat = planChargeBean.getDateEtat().plusDays(7);
            }
            assert dateEtat.getDayOfWeek() == DayOfWeek.MONDAY;

            definirDateEtat(dateEtat);

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ModificationDateEtat(dateEtatPrec));

            majBarreEtat();
        } catch (IhmException e) {
            throw new Exception("Impossible de se positionner au lundi suivant.", e);
        }
    }


    private boolean perteDonneesAceeptee() {
        if (!planChargeBean.aBesoinEtreSauvegarde()) {
            return true;
        }
        Optional<ButtonType> result = ihm.afficherPopUp(
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


    public void calculer(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        try {
            ihm.getDisponibilitesController().calculerDisponibilites();
        } catch (IhmException e) {
            LOGGER.error("Impossible de calculer.", e);
            ihm.afficherPopUp(Alert.AlertType.ERROR, "Impossible de calculer.", Exceptions.causes(e));
        }
    }

}
