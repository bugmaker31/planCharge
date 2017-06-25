package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.util.ParametresIhm;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.*;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by frederic.danna on 09/04/2017.
 *
 * @author frederic.danna
 */
public class ApplicationController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    private static ApplicationController instance = null;

    public static ApplicationController instance() {
        return instance;
    }

    private static ParametresIhm paramsIhm = ParametresIhm.instance();
    private static ParametresMetiers paramsMetier = ParametresMetiers.instance();

    public enum NomModule {
        disponibilites("Disponibilités"),
        taches("Tâches"),
        charges("Charges");

        private String texte;

        NomModule(String texte) {
            this.texte = texte;
        }

        public String getTexte() {
            return texte;
        }
    }

    private NomModule nomModuleCourant = null;

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
    private DatePicker dateEtatPicker;


    // Les onglets :

    @FXML
    @NotNull
    private TabPane gestionTabPane;
    @FXML
    @NotNull
    private Tab disponibilitesTab;
    @FXML
    @NotNull
    private Tab tachesTab;
    @FXML
    @NotNull
    private Tab chargesTab;


    // La barre d'état :

    @FXML
    @NotNull
    private CheckBox sauvegardeRequiseCheckbox;

    @FXML
    @NotNull
    private Label nbrTachesField;

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


    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

/*
    @Null
    private String moduleCourant;
*/


    // Les services métier :

    //    @Autowired
    @NotNull
    private PlanChargeService planChargeService = PlanChargeService.instance();


    // Les données métier :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<PlanificationBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    public NomModule getNomModuleCourant() {
        return nomModuleCourant;
    }

    @NotNull
    public DatePicker getDateEtatPicker() {
        return dateEtatPicker;
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

        // Cf. https://stackoverflow.com/questions/17522686/javafx-tabpane-how-to-listen-to-selection-changes
        gestionTabPane.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (oldValue != null) {
                        assert !oldValue.equals(newValue);
                    }
                    try {
                        if (newValue.equals(disponibilitesTab)) {
                            afficherModuleDisponibilites();
                            return;
                        }
                        if (newValue.equals(tachesTab)) {
                            afficherModuleTaches();
                            return;
                        }
                        if (newValue.equals(chargesTab)) {
                            afficherModuleCharges();
                            return;
                        }
                        throw new IhmException("Tab non géré : '" + newValue.getText() + "'.");
                    } catch (IhmException e) {
                        LOGGER.error("Impossible d'affichager le module {}.", disponibilitesTab.getText(), e);
                    }
                }
        );

//        planChargeBean.getPlanificationsBeans().addListener(
//                (ListChangeListener<? super PlanificationBean>) change -> nbrTachesField.setText(change.getList().size() + "")
//        );
    }


    public void majTitre() {
        String titre = PlanChargeIhm.APP_NAME;
        if (planChargeBean.getDateEtat() != null) {
            titre += (" - " + planChargeBean.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }
        if (nomModuleCourant != null) {
            titre += (" - " + nomModuleCourant.getTexte());
        }
        ihm.definirTitre(titre);
    }

    public void majBarreEtat() {
        LOGGER.debug("majBarreEtat...");
        sauvegardeRequiseCheckbox.setSelected(planChargeBean.aBesoinEtreSauvegarde());
        nbrTachesField.setText(String.valueOf(planChargeBean.getPlanificationsBeans().size()));
    }


    /*
    Menu "Fichier" :
     */

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
                    400, 200
            );
        }
    }

    private void charger(@NotNull File ficPlanCharge) throws IhmException {

        RapportChargementAvecProgression rapport = new RapportChargementAvecProgression();

        Task<RapportChargementAvecProgression> chargerPlanCharge = new Task<RapportChargementAvecProgression>() {

            @SuppressWarnings("ValueOfIncrementOrDecrementUsed")
            @Override
            protected RapportChargementAvecProgression call() throws Exception {

                rapport.setProgressionMax(8);
                int cptEtapeSauvegarde = 0;

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.intValue(), rapport.getProgressionMax()));

                PlanChargeBean planChargeBeanAvantChargement = planChargeBean.copier();
                rapport.setProgressionCourante(++cptEtapeSauvegarde);

                PlanCharge planCharge = planChargeService.charger(ficPlanCharge, rapport);
                rapport.setProgressionCourante(++cptEtapeSauvegarde);

                planChargeBean.init(planCharge);
                rapport.setProgressionCourante(++cptEtapeSauvegarde);

                planChargeBean.vientDEtreCharge();
                getSuiviActionsUtilisateur().historiser(new ChargementPlanCharge(planChargeBeanAvantChargement));
                rapport.setProgressionCourante(++cptEtapeSauvegarde);

                // TODO FDA 2017/08 La liste contenant les référentiels devraient être chargées au démarrage de l'appli, mais tant que les référentiels seront bouchonnés on n'a pas le choix.
                ihm.getTachesController().populerReferentiels();
                rapport.setProgressionCourante(++cptEtapeSauvegarde);
                ihm.getChargesController().populerReferentiels();
                rapport.setProgressionCourante(++cptEtapeSauvegarde);

                // TODO FDA 2017/08 Les listes des filtres devraient être chargées au démarrage de l'appli, mais tant que les référentiels seront bouchonnés on n'a pas le choix.
                ihm.getTachesController().populerFiltres();
                rapport.setProgressionCourante(++cptEtapeSauvegarde);
                ihm.getChargesController().populerFiltres();
                rapport.setProgressionCourante(++cptEtapeSauvegarde);

                assert cptEtapeSauvegarde == rapport.getProgressionMax();

                return rapport;
            }
        };

        try {

            /*RapportChargementAvecProgression rapportFinal = */
            ihm.afficherProgression("Chargement du plan de charge...", chargerPlanCharge);
//            assert rapportFinal != null;

            definirDateEtat(planChargeBean.getDateEtat());

            ihm.getTachesController().razFiltres();
            ihm.getChargesController().razFiltres();

/*
            ihm.afficherPopUp(
                    Alert.AlertType.INFORMATION,
                    "Chargement terminé",
                    "Le chargement est terminé :"
                            + "\n- date d'état : " + planChargeBean.getDateEtat()
                            + "\n- " + planChargeBean.getPlanificationsBeans().size() + " tâches",
                    400, 200
            );
*/

            afficherModuleCharges(); // Rq : Simule une action de l'utilisateur (l'action peut être "undone" (Ctrl+Z), etc.).

            majBarreEtat();

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

                PlanCharge planCharge = planChargeBean.extract();
                planChargeService.sauver(planCharge, rapport);

                rapport.setProgressionCourante(1);

                return rapport;
            }
        };

        try {

            //noinspection unused
            RapportSauvegarde rapportFinal = ihm.afficherProgression("Sauvegarde...", sauvegarder);
            assert rapport != null;

            planChargeBean.vientDEtreSauvegarde();
            getSuiviActionsUtilisateur().historiser(new SauvegardePlanCharge());

            File ficPlanCharge = planChargeService.fichierPersistancePlanCharge(planChargeBean.getDateEtat());
            ihm.afficherPopUp(
                    Alert.AlertType.INFORMATION,
                    "Sauvegarde terminée",
                    "Les " + planChargeBean.getPlanificationsBeans().size() + " lignes du plan de charge"
                            + " en date du " + planChargeBean.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                            + " ont été sauvées (dans le fichier '" + ficPlanCharge.getAbsolutePath() + "').",
                    500, 300
            );

            majBarreEtat();

        } catch (IhmException | ServiceException e) {
            LOGGER.warn("Impossible de sauver le plan de charge.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de sauver le plan de charge.",
                    Exceptions.causes(e),
                    500, 200
            );
            // TODO FDA 2017/05 Positionner l'affichage sur la 1ère ligne/colonne en erreur.
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

                PlanCharge planCharge = planChargeBean.extract();

                planChargeService.majTachesDepuisCalc(planCharge, ficCalc, rapport);

                planChargeBean.init(planCharge);

                return rapport;
            }
        };

        try {

            RapportImportTaches rapportFinal = ihm.afficherProgression("Import des tâches", importerTachesDepuisCalc);
            assert rapport != null;

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ImportTaches());

            ihm.afficherPopUp(
                    Alert.AlertType.INFORMATION,
                    "Tâches mises à jour importées",
                    "Les tâches ont été mises à jour : "
                            + "\n- depuis le fichier : " + ficCalc.getAbsolutePath()
                            + "\n- nombre de tâches initial : " + rapportFinal.getNbrTachesPlanifiees()
                            + "\n- nombre de lignes importées : " + rapportFinal.getNbrTachesImportees()
                            + "\n- nombre de tâches mises à jour : " + rapportFinal.getNbrTachesMisesAJour()
                            + "\n- nombre de tâches ajoutées : " + rapportFinal.getNbrTachesAjoutees()
                            + "\n- nombre de tâches supprimées : " + rapportFinal.getNbrTachesSupprimees()
                            + "\n- nombre de tâches au final : " + planChargeBean.getPlanificationsBeans().size(),
                    700, 300
            );

            afficherModuleTaches();

            majBarreEtat();
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

    // TODO FDA 23017/02 Afficher une "progress bar".
    private void importerPlanChargeDepuisCalc(@NotNull File ficCalc) throws ControllerException {

        final RapportImportPlanChargeAvecProgression rapport = new RapportImportPlanChargeAvecProgression();

        Task<RapportImportPlanChargeAvecProgression> importerPlanChargeDepuisCalc = new Task<RapportImportPlanChargeAvecProgression>() {

            @Override
            protected RapportImportPlanChargeAvecProgression call() throws Exception {

                rapport.setProgressionMax(1);

                rapport.avancementProperty().addListener((observable, oldValue, newValue) -> updateMessage(newValue));
                rapport.progressionCouranteProperty().addListener((observable, oldValue, newValue) -> updateProgress(newValue.intValue(), rapport.getProgressionMax()));

                PlanCharge planCharge = planChargeService.importerDepuisCalc(ficCalc, rapport);

                planChargeBean.setDateEtat(planCharge.getDateEtat());

                List<PlanificationBean> planifBeans = new ArrayList<>();
                for (Tache tache : planCharge.getPlanifications().taches()) {
                    Map<LocalDate, Double> calendrier;
                    try {
                        calendrier = planCharge.getPlanifications().calendrier(tache);
                    } catch (TacheSansPlanificationException e) {
                        throw new ControllerException("Impossible de définir la planification de la tâche " + tache.noTache() + ".", e);
                    }
                    planifBeans.add(new PlanificationBean(tache, calendrier));
                }
                planificationsBeans.setAll(planifBeans);

                return rapport;
            }
        };

        try {

            RapportImportPlanCharge rapportFinal = ihm.afficherProgression("Import du plan de charge", importerPlanChargeDepuisCalc);
            assert rapportFinal != null;

            definirDateEtat(planChargeBean.getDateEtat());

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new ImportPlanCharge());

            ihm.afficherPopUp(
                    Alert.AlertType.INFORMATION,
                    "Données importées",
                    "Le plan de charge a été importé : "
                            + "\n- depuis le fichier : " + ficCalc.getAbsolutePath()
                            + "\n- date d'état : " + planChargeBean.getDateEtat()
                            + "\n- nombre de lignes/tâches importées :" + planChargeBean.getPlanificationsBeans().size(),
                    700, 300
            );

            afficherModuleCharges();

            majBarreEtat();
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

/*
    */
/*
    Menu "Gérer" :
     *//*


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

        ihm.afficherPopUp(
                Alert.AlertType.INFORMATION,
                "A propos de l'application \"" + PlanChargeIhm.APP_NAME + "\"",
                "Version : " + (versionApp == null ? "N/C" : versionApp)
                        + "\n" + "Fonctionnalité : Gestion du plan de charge d'une équipe d'un centre de service."
                        + "\nAuteur : Frédéric Danna - (C)2017",
                500, 200
        );
    }


    /*
    Modules :
     */

    //    @FXML
    public void afficherModuleDisponibilites() throws IhmException {
        LOGGER.debug("> [...] > Module \"Disponibilités\"");

        if (nomModuleCourant == NomModule.disponibilites) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }

        final NomModule nomModulePrecedent = nomModuleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'nomModuleCourant', donc il faut le mémoriser avant.
        activerModuleDisponibilites();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleDisponibilites(nomModulePrecedent));
    }

    /**
     * NB : Simule une action de l'utilisateur.
     */
    public void activerModuleDisponibilites() {
        nomModuleCourant = NomModule.disponibilites;

//        applicationView.setCenter(disponibilitesView);
        if (!gestionTabPane.getSelectionModel().getSelectedItem().equals(disponibilitesTab)) {
            // Cf. http://stackoverflow.com/questions/6902377/javafx-tabpane-how-to-set-the-selected-tab
            gestionTabPane.getSelectionModel().select(disponibilitesTab); // Rq : Va déclencher cette méthode 'activermodule...', donc il faut valuer 'nomModuleCourant' avant, pour éviter les boucles.
        }

        majTitre();
    }

    //    @FXML
    public void afficherModuleTaches() throws IhmException {
        LOGGER.debug("> [...] > Module \"Tâches\"");

        if (nomModuleCourant == NomModule.taches) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }
        final NomModule nomModulePrecedent = nomModuleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'nomModuleCourant', donc il faut le mémoriser avant.
        activerModuleTaches();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleTaches(nomModulePrecedent));
    }

    /**
     * NB : Simule une action de l'utilisateur.
     */
    public void activerModuleTaches() {
        nomModuleCourant = NomModule.taches;

//        applicationView.setCenter(tachesView);
        if (!gestionTabPane.getSelectionModel().getSelectedItem().equals(tachesTab)) {
            // Cf. http://stackoverflow.com/questions/6902377/javafx-tabpane-how-to-set-the-selected-tab
            gestionTabPane.getSelectionModel().select(tachesTab); // Rq : Va déclencher cette méthode 'activermodule...', donc il faut valuer 'nomModuleCourant' avant, pour éviter les boucles.
        }

        majTitre();
    }

    //    @FXML
    public void afficherModuleCharges() throws IhmException {
        LOGGER.debug("> [...] > Module \"Charges\"");

        if (nomModuleCourant == NomModule.charges) {
            LOGGER.debug("Déjà le module affiché, rien à faire.");
            return;
        }
        final NomModule nomModulePrecedent = nomModuleCourant; // Rq : La méthode 'activerModule...' va modifier la valeur de 'nomModuleCourant', donc il faut le mémoriser avant.
        activerModuleCharges();
        getSuiviActionsUtilisateur().historiser(new AffichageModuleCharges(nomModulePrecedent));
    }

    /**
     * NB : Simule une action de l'utilisateur.
     */
    public void activerModuleCharges() {
        nomModuleCourant = NomModule.charges;

//        applicationView.setCenter(chargesView);
        if (!gestionTabPane.getSelectionModel().getSelectedItem().equals(chargesTab)) {
            // Cf. http://stackoverflow.com/questions/6902377/javafx-tabpane-how-to-set-the-selected-tab
            gestionTabPane.getSelectionModel().select(chargesTab); // Rq : Va déclencher cette méthode 'activermodule...', donc il faut valuer 'nomModuleCourant' avant, pour éviter les boucles.
        }

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

    public void definirDateEtat(@Null LocalDate dateEtat) {
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

        majPlanificationCharge();
        majTitre();
    }


    private void majPlanificationCharge() {
        definirNomsPeriodes();
        majPlanification();
    }

    private void definirNomsPeriodes() {

        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

        LocalDate dateDebutPeriode = planChargeBean.getDateEtat();
        ihm.getChargesController().getSemaine1Column().setText("S+1\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine2Column().setText("S+2\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine3Column().setText("S+3\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine4Column().setText("S+4\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine5Column().setText("S+5\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine6Column().setText("S+6\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine7Column().setText("S+7\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine8Column().setText("S+8\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine9Column().setText("S+9\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine10Column().setText("S+10\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine11Column().setText("S+11\n" + dateDebutPeriode.format(dateFormatter));

        dateDebutPeriode = dateDebutPeriode.plusDays(7);
        ihm.getChargesController().getSemaine12Column().setText("S+12\n" + dateDebutPeriode.format(dateFormatter));
    }

    private void majPlanification() {
        ihm.getChargesController().afficherPlanification();
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
        if (result.get().equals(ButtonType.CANCEL)) {
            LOGGER.info("Action annulée par l'utilisateur, pour éviter de perdre des données.");
            return false;
        }
        return true;
    }

}
