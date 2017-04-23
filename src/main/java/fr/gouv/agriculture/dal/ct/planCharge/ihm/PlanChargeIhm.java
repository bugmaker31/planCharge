package fr.gouv.agriculture.dal.ct.planCharge.ihm;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import fr.gouv.agriculture.dal.ct.planCharge.PlanChargeApplication;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ErrorController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleChargeController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleDisponibilitesController;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
    private Region chargeView;

    private ApplicationController applicationContoller;
    private ModuleDisponibilitesController disponibilitesController;
    private ModuleChargeController chargeController;

    // Les données métier :
    private PlanCharge planCharge;

    // Les services métier :
    private PlanChargeService planChargeService;

    public static void main(String[] args) {
        launch(args);
    }

    public BorderPane getApplicationView() {
        return applicationView;
    }

    public Region getDisponibilitesView() {
        return disponibilitesView;
    }

    public Region getChargeView() {
        return chargeView;
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

        inject();

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
            dispoLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleDisponibilitesView.fxml"));
            disponibilitesView = dispoLoader.load();
            disponibilitesController = dispoLoader.getController();
        }
        {
            FXMLLoader chargeLoader = new FXMLLoader();
            chargeLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleChargeView.fxml"));
            chargeView = chargeLoader.load();
            chargeController = chargeLoader.getController();
        }
    }

    private void inject() {

        // On utilise Spring IOC pour l'injection, principalement :
        ApplicationContext context = new ClassPathXmlApplicationContext("kernel-conf-ioc.xml");

        // On mémorise les données que Spring ne peut gérer :
        PlanChargeApplication planChargeApp = context.getBean(PlanChargeApplication.class);
        planChargeApp.setIhm(this);
        planChargeApp.setContext(context);

        // Les beans utilisés dans cette classe ne peuvent être injectés par Spring, car cette classe n'est pas instanciée par Spring.
        // Il faut donc l'injecter soi-même, en récupérant l'instance créée à l'instant par Spring.
        planChargeService = context.getBean(PlanChargeService.class);
    }

    private static void showError(Thread thread, Throwable throwable) {
        LOGGER.error("An error occurred in thread " + thread + ".", throwable);
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(throwable);
        }
    }

    private static void showErrorDialog(Throwable t) {
        StringWriter errorMsg = new StringWriter();
        t.printStackTrace(new PrintWriter(errorMsg));
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(PlanChargeIhm.class.getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ErrorView.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController) loader.getController()).setErrorText(errorMsg.toString());
            dialog.setScene(new Scene(root, 800, 400));
            dialog.show();
        } catch (IOException e) {
            LOGGER.error("Impossible d'afficher l'erreur.", e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Application en cours de démarrage...");

        this.primaryStage = primaryStage;

        primaryStage.setTitle(APP_NAME);
        primaryStage.setScene(new Scene(applicationView));
        primaryStage.show();

        // TODO FDA 2017/04 Pour accélérer les tests. A supprimer avant de livrer.
        applicationView.setCenter(getChargeView());

        // Chargement des données utilisées dernièrement :
        LocalDate dateEtatPrec = LocalDate.of(2016, 11, 13); // TODO FDA 2017/04 Récupérer la dernière date d'état dynamiquement (pas une constante !).
        if (dateEtatPrec != null) {
            chargeDonnees(dateEtatPrec);
        }

        LOGGER.info("Application démarrée.");
    }

    private void chargeDonnees(LocalDate dateEtat) {
        try {
            planCharge = planChargeService.load(dateEtat);
        } catch (ServiceException e) {
            LOGGER.error("Impossible de lire les données en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger le plan de charge");
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
        @NotNull Preferences prefs = Preferences.userNodeForPackage(PlanChargeIhm.class);
        String clefPrefFic = clefPrefPlanifCharge(dateEtat);
        if (file != null) {
            prefs.put(clefPrefFic, file.getPath());

            // Update the stage title.
            String complementTitreFenetre = dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            primaryStage.setTitle(APP_NAME + " - " + complementTitreFenetre);
        } else {
            prefs.remove(clefPrefFic);

            // Update the stage title.
            primaryStage.setTitle(APP_NAME);
        }
    }

}
