package fr.gouv.agriculture.dal.ct.planCharge.ihm;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleChargeController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleDisponibilitesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PlanChargeApplication extends javafx.application.Application {

    public static final String APP_NAME = "Plan de charge";

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeApplication.class);

    private static PlanChargeApplication APPLICATION;

    private BorderPane applicationView;
    private Region disponibilitesView;
    private Region chargeView;

    public static void main(String[] args) {
        launch(args);
    }

    public static PlanChargeApplication APPLICATION() {
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
    public PlanChargeApplication() {
        super();
    }

    @Override
    public void init() throws Exception {
        LOGGER.info("Application en cours d'initialisation...");
        super.init();
        initApplicationView();
        APPLICATION = this;
        LOGGER.info("Application initialisée.");
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
        System.exit(0);
    }

}
