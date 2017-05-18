package fr.gouv.agriculture.dal.ct.planCharge.ihm;

import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresApplicatifs;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleDisponibilitesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleTachesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class PlanChargeIhm extends Application {

    @NotNull
    public static final String APP_NAME = "Plan de charge";

    @NotNull
    public static final String FORMAT_DATE = "dd/MM/yy";

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeIhm.class);

    private static PlanChargeIhm instance;

    public static PlanChargeIhm instance() {
        return instance;
    }

    //    private static Contexte contexte = Contexte.instance();
    private static ParametresApplicatifs params = ParametresApplicatifs.instance();

    @NotNull
    private Stage primaryStage;

    /*
        @NotNull
        private BorderPane errorView;
    */
    @NotNull
    private BorderPane applicationView;

    /*
        @NotNull
        private ErrorController errorController;
    */
    @NotNull
    private ApplicationController applicationController;
    @NotNull
    private ModuleDisponibilitesController disponibilitesController;
    @NotNull
    private ModuleTachesController tachesController;
    @NotNull
    private ModuleChargesController chargesController;

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ApplicationController getApplicationController() {
        return applicationController;
    }

    public ModuleDisponibilitesController getDisponibilitesController() {
        return disponibilitesController;
    }

    public ModuleTachesController getTachesController() {
        return tachesController;
    }

    public ModuleChargesController getChargesController() {
        return chargesController;
    }

    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();


    public static void main(String[] args) {
        launch(args);
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
        instance = this;

        super.init();

        chargerParametresApplicatifs();

        // Cf. http://stackoverflow.com/questions/26361559/general-exception-handling-in-javafx-8
        Thread.setDefaultUncaughtExceptionHandler(this::showError);

//        injecter();

        initialiserViewsEtControllers();

        LOGGER.info("Application initialisée.");
    }

    private void chargerParametresApplicatifs() throws Exception {
        try {
            params.init();
        } catch (KernelException e) {
            throw new Exception("Impossible de charger les paramètres applicatifs.", e);
        }
    }

/*
    private void injecter() {

        // On utilise Spring IOC pour l'injection, principalement :
        contexte = new ClassPathXmlApplicationContext("ihm-conf-ioc.xml");

        // Les beans Spring (métier) utilisés dans les classes JavaFX (IHM) ne peuvent être injectés par Spring,
        // car les classes JavaFX ne sont pas instanciées par Spring.
        // Il faut donc les injecter soi-même :
        planChargeService = contexte.getBean(PlanChargeService.class);
        planChargeBean = contexte.getBean(PlanChargeBean.class);

        // Certaines classes ne peuvent être injectées par Spring car ne sont pas instanciables par Spring (elles sont instanciées
        // par JavaFX, etc.). Donc on les "injecte" soi-même :
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) contexte).getBeanFactory();
        beanFactory.registerSingleton(this.getClass().getCanonicalName(), this);
    }
*/

    private void initialiserViewsEtControllers() throws IOException {
/*
        {
            FXMLLoader errorLoader = new FXMLLoader();
            errorLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ErrorView.fxml"));
            errorView = errorLoader.load();
            errorController = errorLoader.getController();
        }
*/
        {
            FXMLLoader appLoader = new FXMLLoader();
            appLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ApplicationView.fxml"));
            applicationView = appLoader.load();
            applicationController = appLoader.getController();
        }
/*
        {
            FXMLLoader disponibilitesLoader = new FXMLLoader();
            disponibilitesLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleDisponibilitesView.fxml"));
            disponibilitesView = disponibilitesLoader.load();
            disponibilitesController = disponibilitesLoader.getController();
        }
*/
        disponibilitesController = ModuleDisponibilitesController.instance();
/*
        {
            FXMLLoader tachesLoader = new FXMLLoader();
            tachesLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleTachesView.fxml"));
            tachesView = tachesLoader.load();
            tachesController = tachesLoader.getController();
        }
*/
        tachesController = ModuleTachesController.instance();
/*
        {
            FXMLLoader chargesLoader = new FXMLLoader();
            chargesLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleChargesView.fxml"));
            chargesView = chargesLoader.load();
            chargesController = chargesLoader.getController();
        }
*/
        chargesController = ModuleChargesController.instance();
    }

    public void showError(Thread thread, Throwable throwable) {
        LOGGER.error("An (uncaught) error occurred in thread " + thread + ".", throwable);
        if (Platform.isFxApplicationThread()) {

            StringWriter errorMsg = new StringWriter();
            throwable.printStackTrace(new PrintWriter(errorMsg));

            afficherPopUp(Alert.AlertType.ERROR, "Erreur interne", errorMsg.toString());
        }
    }

    public Optional<ButtonType> afficherPopUp(Alert.AlertType type, String titre, String message) {
        return afficherPopUp(type,
                titre, message,
                Math.max(100, primaryStage.getWidth() - 100), Math.max(primaryStage.getHeight() - 100, 100),
                null
        );
    }

    public Optional<ButtonType> afficherPopUp(Alert.AlertType type, String titre, String message, ButtonType boutonParDefaut, ButtonType... bouttons) {
        return afficherPopUp(type,
                titre, message,
                Math.max(100, primaryStage.getWidth() - 100), Math.max(primaryStage.getHeight() - 100, 100),
                boutonParDefaut, bouttons
        );
    }

    public Optional<ButtonType> afficherPopUp(Alert.AlertType type, String titre, String message, double width, double height) {
        return afficherPopUp(type,
                titre, message,
                width, height,
                null
        );
    }

    public Optional<ButtonType> afficherPopUp(Alert.AlertType type, String titre, String message, double width, double height, ButtonType boutonParDefaut, ButtonType... buttons) {
        Alert popUp = new Alert(type);

        popUp.setTitle(type.name());
        popUp.setHeaderText(titre);
        popUp.setContentText(message);
/*
        // Cf. http://stackoverflow.com/questions/29738083/javafx-alert-dialog-html
        WebView webView = new WebView();
        webView.getEngine().loadContent("<html>" + message + "</html>");
        webView.setPrefSize(width, height);
        popUp.getDialogPane().setContent(webView);
*/

        popUp.getButtonTypes().setAll(ButtonType.OK);
        popUp.getButtonTypes().addAll(buttons);
        popUp.getButtonTypes()
                .forEach(buttonType -> {
                    Button bouton = (Button) popUp.getDialogPane().lookupButton(buttonType);
                    bouton.setDefaultButton(buttonType == boutonParDefaut);
                });

        popUp.getDialogPane().setPrefWidth(width);
        popUp.getDialogPane().setPrefHeight(height);
        popUp.setResizable(true);

        Stage popUpStage = (Stage) popUp.getDialogPane().getScene().getWindow();
        popUpStage.getIcons().addAll(primaryStage.getIcons());

        return popUp.showAndWait();
    }

/*
    private void showErrorDialog(@NotNull String titre,  String errorMsg) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(PlanChargeIhm.class.getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ErrorView.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController) loader.getController()).setErrorText(errorMsg);
            dialog.setScene(new Scene(root, 800, 400));
            dialog.show();
        } catch (IOException e) {
            LOGGER.error("Impossible d'afficher la boîte de dialogue avec l'erreur.", e);
        }
    }
*/

    @Override
    public void start(@SuppressWarnings("ParameterHidesMemberVariable") @NotNull Stage primaryStage) throws Exception {
        try {
            LOGGER.info("Application en cours de démarrage...");

            this.primaryStage = primaryStage;

            primaryStage.setTitle(APP_NAME);
            primaryStage.setScene(new Scene(applicationView));
            primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/planCharge-logo.png")));
            primaryStage.show();

            // Chargement des données utilisées dernièrement (if any) :
            LocalDate dateEtatPrec = dateEtatPrecedente();
            if (dateEtatPrec != null) {
                applicationController.charger(dateEtatPrec);
            }

            // TODO FDA 2017/04 Pour accélérer les tests. A supprimer avant de livrer.
//        afficherModuleDisponibilites();
//        afficherModuleTaches();
//        afficherModuleCharges();
            //
//        chargesController.importerDepuisCalc(new File("D:\\Dvlpt\\_MAAP\\workspace_IDEA\\planCharge\\donnees\\DAL-CT_11_PIL_Plan de charge_2017s16_t3.18.ods"));

            LOGGER.info("Application démarrée.");
        } catch (Throwable e) {
            String erreur = "Impossible de démarrer l'IHM.";
            LOGGER.error(erreur, e);
            throw new Exception(erreur, e);
        }
    }

    @Null
    private LocalDate dateEtatPrecedente() {
        return LocalDate.of(2017, 4, 17); // TODO FDA 2017/04 Récupérer la dernière date d'état dans les préférences de l'utilisateur (pas une constante !).;
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Application en cours d'arrêt...");
        super.stop();
        LOGGER.info("Application arrêtée.");
        Platform.exit();
    }

/*

    private static final String PREF_KEY_FIC_PLANIF_CHARGE = "PREF_KEY_FIC_PLANIF_CHARGE";

    private String clefPrefPlanifCharge(LocalDate dateEtat) {
        return PREF_KEY_FIC_PLANIF_CHARGE + "-" + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    // TODO FDA 2017/04 Coder pour enregistrer la date de dernière planif que l'utilisateur a travaillé.

    */
///**
// * Returns the file preference, i.e. the file that was last opened.
// * The preference is read from the OS specific registry. If no such
// * preference can be found, null is returned.
// *
// * @param datePlanif Date de la planification.
// * @return
// */
/*

    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    @Null
    public File getFichierPlanificationsCharge(LocalDate datePlanif) {
        Preferences prefs = Preferences.userNodeForPackage(PlanChargeIhm.class);
        String filePath = prefs.get(clefPrefPlanifCharge(datePlanif), null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    */

    //    /**
//     * Sets the file path of the currently loaded file. The path is persisted in
//     * the OS specific registry.
//     *
//     * @param file the file, or null to remove the path
//     */
/*

    // Cf. http://code.makery.ch/library/javafx-8-tutorial/fr/part5/
    public void setFichierPlanificationsCharge(@Null File file, @Null LocalDate dateEtat) {
        */
/*@NotNull*//*

        Preferences prefs = Preferences.userNodeForPackage(PlanChargeIhm.class);
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
*/
    public void majTitre() {
        String titre = PlanChargeIhm.APP_NAME;
        if (planChargeBean.getDateEtat() != null) {
            titre += (" - " + planChargeBean.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }
/*
        if (moduleCourant != null) {
            titre += (" - " + moduleCourant);
        }
*/
        primaryStage.setTitle(titre);
    }

    public void definirDateEtat(@Null LocalDate dateEtat) {
        assert (dateEtat == null) || (dateEtat.getDayOfWeek() == DayOfWeek.MONDAY);

        if ((dateEtat == null) || !dateEtat.equals(planChargeBean.getDateEtat())) {
            planChargeBean.setDateEtat(dateEtat);
        }
        if ((dateEtat == null) || !dateEtat.equals(getChargesController().getDateEtatPicker().getValue())) {
            getChargesController().getDateEtatPicker().setValue(dateEtat);
        }

        majTitre();
    }

    public void majBarreEtat() {
        getApplicationController().majBarreEtat();
    }
}
