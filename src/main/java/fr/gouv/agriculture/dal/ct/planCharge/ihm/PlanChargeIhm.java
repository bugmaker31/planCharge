package fr.gouv.agriculture.dal.ct.planCharge.ihm;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ErrorController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleChargeController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleDisponibilitesController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class PlanChargeIhm extends javafx.application.Application {

    public static final String APP_NAME = "Plan de charge";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeIhm.class);

    private static PlanChargeIhm APPLICATION;

    private BorderPane applicationView;
    private Region disponibilitesView;
    private Region chargeView;

    public static void main(String[] args) {
        launch(args);
    }

    public static PlanChargeIhm APPLICATION() {
        return APPLICATION;
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

        initApplicationView();

        // Cf. http://stackoverflow.com/questions/26361559/general-exception-handling-in-javafx-8
        Thread.setDefaultUncaughtExceptionHandler(PlanChargeIhm::showError);

        APPLICATION = this;
        LOGGER.info("Application initialisée.");
    }

    private static void showError(Thread thread, Throwable throwable) {
        LOGGER.error("An unexpected error occurred in " + thread + ".", throwable);
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
            ((ErrorController)loader.getController()).setErrorText(errorMsg.toString());
            dialog.setScene(new Scene(root, 800, 400));
            dialog.show();
        } catch (IOException e) {
            LOGGER.error("Impossible d'afficher l'erreur.", e);
        }
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        LOGGER.info("Application en cours de démarrage...");
        primaryStage.setTitle(APP_NAME);
        primaryStage.setScene(new Scene(applicationView));
        primaryStage.show();

        // TODO FDA 2017/04 Pour accélérer les tests. A supprimer avant de livrer.
        applicationView.setCenter(getChargeView());

        LOGGER.info("Application démarrée.");
    }

    private void initApplicationView() throws IOException {
        {
            FXMLLoader appLoader = new FXMLLoader();
            appLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ApplicationView.fxml"));
            applicationView = appLoader.load();
            // Give the controller access to the main app.
            ((ApplicationController) appLoader.getController()).setApplication(this);
        }
        {
            FXMLLoader dispoLoader = new FXMLLoader();
            dispoLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleDisponibilitesView.fxml"));
            disponibilitesView = dispoLoader.load();
            // Give the controller access to the main app.
            ((ModuleDisponibilitesController) dispoLoader.getController()).setApplication(this);
        }
        {
            FXMLLoader chargeLoader = new FXMLLoader();
            chargeLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleChargeView.fxml"));
            chargeView = chargeLoader.load();
            // Give the controller access to the main app.
            ((ModuleChargeController) chargeLoader.getController()).setApplication(this);
        }
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Application en cours d'arrêt...");
        super.stop();
        LOGGER.info("Application arrêtée.");
//        Platform.exit();
        System.exit(0);
    }

}
