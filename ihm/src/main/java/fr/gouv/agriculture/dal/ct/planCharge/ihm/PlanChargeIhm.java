package fr.gouv.agriculture.dal.ct.planCharge.ihm;

import fr.gouv.agriculture.dal.ct.planCharge.PlanChargeApplication;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ServiceException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;

public class PlanChargeIhm extends javafx.application.Application {

    public static final String APP_NAME = "Plan de charge";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeIhm.class);

    private Stage primaryStage;
    private BorderPane applicationView;
    private Region disponibilitesView;
    private Region tachesView;
    private Region chargeView;

    private ApplicationController applicationContoller;
    private ModuleDisponibilitesController disponibiliteController;
    private ModuleTacheController tacheController;
    private ModuleChargeController chargeController;

    private String moduleCourant;

    // Les données métier :
    private PlanCharge planCharge;

    // Les services métier :
    private PlanChargeService planChargeService;

    public static void main(String[] args) {
        launch(args);
    }

    public PlanCharge getPlanCharge() {
        return planCharge;
    }

    public void setPlanCharge(PlanCharge planCharge) {
        this.planCharge = planCharge;
    }

    /**
     * Constructor
     */
    public PlanChargeIhm() {
        super();
    }

    @Override
    public void init() throws Exception {
        LOGGER.info("Application en cours d'initialisation...");

        super.init();

        // Cf. http://stackoverflow.com/questions/26361559/general-exception-handling-in-javafx-8
        Thread.setDefaultUncaughtExceptionHandler(PlanChargeIhm::showError);

        initApplicationView();

        injecter();

        LOGGER.info("Application initialisée.");
    }

    private void initApplicationView() throws IOException {
        {
            FXMLLoader appLoader = new FXMLLoader();
            appLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ApplicationView.fxml"));
            applicationView = appLoader.load();
            applicationContoller = appLoader.getController();
        }
        {
            FXMLLoader dispoLoader = new FXMLLoader();
            dispoLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleDisponibiliteView.fxml"));
            disponibilitesView = dispoLoader.load();
            disponibiliteController = dispoLoader.getController();
        }
        {
            FXMLLoader tachesLoader = new FXMLLoader();
            tachesLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleTacheView.fxml"));
            tachesView = tachesLoader.load();
            tacheController = tachesLoader.getController();
        }
        {
            FXMLLoader chargeLoader = new FXMLLoader();
            chargeLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleChargeView.fxml"));
            chargeView = chargeLoader.load();
            chargeController = chargeLoader.getController();
        }
    }

    private void injecter() {

        // On utilise Spring IOC pour l'injection, principalement :
        ApplicationContext context = new ClassPathXmlApplicationContext("ihm-conf-ioc.xml");

        // On mémorise les données que Spring ne peut gérer :
        PlanChargeApplication planChargeApp = context.getBean(PlanChargeApplication.class);
        planChargeApp.setIhm(this);
        planChargeApp.setContext(context);

        // Les beans Spring (métier) utilisés dans les classes JavaFX (IHM) ne peuvent être injectés par Spring,
        // car les classes JavaFX ne sont pas instanciées par Spring.
        // Il faut donc les injecter soi-même, en récupérant les instances créées par Spring of course :
        planChargeService = context.getBean(PlanChargeService.class);

        // Certaines classes ne peuvent être injectées par Spring car ne sont pas instanciables par Spring (elles sont instanciées
        // par JavaFX, etc.). Donc on les "injecte" soi-même :
        applicationContoller.setApplication(planChargeApp);
        disponibiliteController.setApplication(planChargeApp);
        tacheController.setApplication(planChargeApp);
        tacheController.setPlanChargeService(context.getBean(PlanChargeService.class));
        chargeController.setApplication(planChargeApp);
    }

    private static void showError(Thread thread, Throwable throwable) {
        LOGGER.error("An error occurred in thread " + thread + ".", throwable);
        if (Platform.isFxApplicationThread()) {
            StringWriter errorMsg = new StringWriter();
            throwable.printStackTrace(new PrintWriter(errorMsg));
            showErrorDialog(errorMsg.toString());
        }
    }

    private static void showErrorDialog(String errorMsg) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(PlanChargeIhm.class.getResource("/ihm/src/main/java/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ErrorView.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController) loader.getController()).setErrorText(errorMsg);
            dialog.setScene(new Scene(root, 800, 400));
            dialog.show();
        } catch (IOException e) {
            LOGGER.error("Impossible d'afficher la boîte de dialogue avec l'erreur.", e);
        }
    }

    public void erreur(String message) {
        LOGGER.error("Erreur : " + message);
        showErrorDialog(message);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Application en cours de démarrage...");

        this.primaryStage = primaryStage;

        primaryStage.setTitle(APP_NAME);
        primaryStage.setScene(new Scene(applicationView));
        primaryStage.show();

        // Chargement des données utilisées dernièrement (if any) :
        LocalDate dateEtatPrec = dateEtatPrecedente();
        if (dateEtatPrec != null) {
            chargeDonnees(dateEtatPrec);
        } else {
            planCharge = null;
        }

        // TODO FDA 2017/04 Pour accélérer les tests. A supprimer avant de livrer.
//        afficherModuleDisponibilites();
//        afficherModuleTaches();
        afficherModuleCharge();

        LOGGER.info("Application démarrée.");
    }

    private LocalDate dateEtatPrecedente() {
        return null; //LocalDate.of(2016, 11, 13); // TODO FDA 2017/04 Récupérer la dernière date d'état dynamiquement (pas une constante !).;
    }

    private void chargeDonnees(LocalDate dateEtat) {
        try {
            planCharge = planChargeService.charger(dateEtat);
        } catch (ServiceException e) {
            LOGGER.error("Impossible de lire les données en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de load le plan de charge");
            alert.setContentText("Impossible de lire les données en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".");
            alert.showAndWait();
        }

/* TODO FDA 2017/04 Charger les données au lancement de l'appli.
        PlanCharge planCharge = application.getPlanCharge();
        planifications.addAll(
                planCharge.getPlanifications().taches()
                        .stream()
                        .map(tache -> {
                            try {
                                return new PlanificationBean(tache, planCharge.getPlanifications().planification(tache));
                            } catch (TacheSansPlanificationException e) {
                                throw new ControllerException("Impossible de définir le plan de charge, pour la tâche " + tache.noTache() + ".", e);
                            }
                        })
                        .collect(Collectors.toList())
        );
        //setFichierPlanificationsCharge(datePlanif); TODO FDA 2017/04 Coder.
*/
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Application en cours d'arrêt...");
        super.stop();
        LOGGER.info("Application arrêtée.");
//        Platform.exit();
        System.exit(0);
    }


    private static final String PREF_KEY_FIC_PLANIF_CHARGE = "PREF_KEY_FIC_PLANIF_CHARGE";

    private String clefPrefPlanifCharge(LocalDate dateEtat) {
        return PREF_KEY_FIC_PLANIF_CHARGE + "-" + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @param datePlanif Date de la planification.
     * @return
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    @Nullable
    public File getFichierPlanificationsCharge(LocalDate datePlanif) {
        Preferences prefs = Preferences.userNodeForPackage(PlanChargeIhm.class);
        String filePath = prefs.get(clefPrefPlanifCharge(datePlanif), null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file, or null to remove the path
     */
    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    public void setFichierPlanificationsCharge(@Nullable File file, @Nullable LocalDate dateEtat) {
        /*@NotNull*/ Preferences prefs = Preferences.userNodeForPackage(PlanChargeIhm.class);
        String clefPrefFic = clefPrefPlanifCharge(dateEtat);
        if (file != null) {
            prefs.put(clefPrefFic, file.getPath());

            // Update the stage title.
            majTitre();
        } else {
            prefs.remove(clefPrefFic);

            // Update the stage title.
            majTitre();
        }
    }

    public void afficherModuleDisponibilites() {
        applicationView.setCenter(disponibilitesView);
        moduleCourant = "Dispo.";
        majTitre();
    }

    public void afficherModuleTaches() {
        applicationView.setCenter(tachesView);
        moduleCourant = "Tâches";
        majTitre();
    }

    public void afficherModuleCharge() {
        applicationView.setCenter(chargeView);
        moduleCourant = "Charge";
        majTitre();
    }

    private void majTitre() {
        String titre = APP_NAME;
        if (planCharge != null && planCharge.getDateEtat() != null) {
            titre += (" - " + planCharge.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }
        if (moduleCourant != null) {
            titre += (" - " + moduleCourant);
        }
        primaryStage.setTitle(titre);
    }
}
