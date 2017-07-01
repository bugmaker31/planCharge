package fr.gouv.agriculture.dal.ct.planCharge.ihm;

import fr.gouv.agriculture.dal.ct.ihm.util.ParametresIhm;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.controlsfx.dialog.ProgressDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

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
    private static ParametresMetiers paramsMetier = ParametresMetiers.instance();
    private static ParametresIhm paramsIhm = ParametresIhm.instance();

    @NotNull
    private Stage primaryStage;


    @NotNull
    private BorderPane applicationView;
    /*
        @NotNull
        private BorderPane errorView;
    */
/*
    @NotNull
    private Region workProgressView;
*/
    @NotNull
    private Region joursFeriesView;
    @NotNull
    private Region disponibilitesView;
    @NotNull
    private Region tachesView;
    @NotNull
    private Region chargesView;


    @NotNull
    private ApplicationController applicationController;
    /*
    @NotNull
    private ErrorController errorController;
    */
/*
    @NotNull
    private WorkProgressController workProgressController;
*/
    @NotNull
    private ModuleJoursFeriesController joursFeriesController;
    @NotNull
    private ModuleDisponibilitesController disponibilitesController;
    @NotNull
    private ModuleTachesController tachesController;
    @NotNull
    private ModuleChargesController chargesController;

/*
    //    @Autowired
    @NotNull
    private SuiviActionsUtilisateur suiviActionsUtilisateur = SuiviActionsUtilisateur.instance();
*/


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @NotNull
    public BorderPane getApplicationView() {
        return applicationView;
    }

    @NotNull
    public Region getJoursFeriesView() {
        return joursFeriesView;
    }

    @NotNull
    public Region getDisponibilitesView() {
        return disponibilitesView;
    }

    @NotNull
    public Region getTachesView() {
        return tachesView;
    }

    @NotNull
    public Region getChargesView() {
        return chargesView;
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
            paramsMetier.init();
            paramsIhm.init();
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
/*
        {
            FXMLLoader workProgressLoader = new FXMLLoader();
            workProgressLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/WorkProgressView.fxml"));
            workProgressView = workProgressLoader.load();
            workProgressController = workProgressLoader.getController();
        }
*/
        {
            FXMLLoader appLoader = new FXMLLoader();
            appLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ApplicationView.fxml"));
            applicationView = appLoader.load();
            applicationController = appLoader.getController();
        }
        {
            FXMLLoader joursFeriesLoader = new FXMLLoader();
            joursFeriesLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleJoursFeriesView.fxml"));
            joursFeriesView = joursFeriesLoader.load();
            joursFeriesController = joursFeriesLoader.getController();
        }
        {
            FXMLLoader disponibilitesLoader = new FXMLLoader();
            disponibilitesLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleDisponibilitesView.fxml"));
            disponibilitesView = disponibilitesLoader.load();
            disponibilitesController = disponibilitesLoader.getController();
        }
        {
            FXMLLoader tachesLoader = new FXMLLoader();
            tachesLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleTachesView.fxml"));
            tachesView = tachesLoader.load();
            tachesController = tachesLoader.getController();
        }
        {
            FXMLLoader chargesLoader = new FXMLLoader();
            chargesLoader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ModuleChargesView.fxml"));
            chargesView = chargesLoader.load();
            chargesController = chargesLoader.getController();
        }
    }


    public void showError(Thread thread, Throwable throwable) {
        LOGGER.error("An (uncaught) error occurred in thread " + thread + ".", throwable);
        if (Platform.isFxApplicationThread()) {

/*
            StringWriter errorMsg = new StringWriter();
            throwable.printStackTrace(new PrintWriter(errorMsg));
*/

            afficherPopUp(Alert.AlertType.ERROR, "Erreur interne", Exceptions.causes(throwable));
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
        Alert alert = new Alert(type);

        alert.setTitle(APP_NAME + " - " + titre);
        alert.setHeaderText(titre);
        alert.setContentText(message);
/*
        // Cf. http://stackoverflow.com/questions/29738083/javafx-alert-dialog-html
        WebView webView = new WebView();
        webView.getEngine().loadContent("<html>" + message + "</html>");
        webView.setPrefSize(width, height);
        alert.getDialogPane().setContent(webView);
*/

        ScrollPane scroll = new ScrollPane(alert.getDialogPane().getContent());
        alert.getDialogPane().getChildren().add(scroll);

        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.getButtonTypes().addAll(buttons);
        alert.getButtonTypes()
                .forEach(buttonType -> {
                    Button bouton = (Button) alert.getDialogPane().lookupButton(buttonType);
                    bouton.setDefaultButton(Objects.equals(buttonType, boutonParDefaut));
                });

        alert.getDialogPane().setPrefWidth(width);
        alert.getDialogPane().setPrefHeight(height);

        alert.setResizable(true);

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().addAll(primaryStage.getIcons());

        return alert.showAndWait();
    }


    public void afficherErreurSaisie(@NotNull TextField field, @NotNull String message) throws IhmException {
        field.getStyleClass().add("erreurSaisie");
        afficherPopUpErreurSaisie(field, message);
    }

    public void enleverErreurSaisie(@NotNull TextField field) throws IhmException {
        field.getStyleClass().remove("erreurSaisie");
        masquerPopupErreurSaisie(field);
    }

    private void afficherPopUpErreurSaisie(@NotNull TextField field, @NotNull String message) throws IhmException {

        Label label = new Label(message);
        label.setId(idLabelErreurSaisie(field));
        label.getStyleClass().setAll("messageErreurSaisie");

        /* V1 Avec une Popup :
        Popup popup = new Popup();
        Bounds fieldBounds = field.getBoundsInLocal();
        Point2D popupLocation = field.localToScreen(fieldBounds.getMinX(), fieldBounds.getMinY() + fieldBounds.getHeight());
        Region hbox = new HBox(label);
        popup.getContent().add(hbox);
        popup.show(field, popupLocation.getX(), popupLocation.getY());
        */
        // V2 Avec un VBox ajouté/retiré dynamiquement :
        VBox vbox = fieldVBox(field);
        vbox.getChildren().add(label);
    }

    private void masquerPopupErreurSaisie(@NotNull TextField field) throws IhmException {
        VBox vbox = fieldVBox(field);
        FilteredList<Node> filtered = vbox.getChildren().filtered(node -> node.getId().equals(idLabelErreurSaisie(field)));
        assert (filtered != null);
        if (filtered.isEmpty()) {
            return;
        }
        assert filtered.size() == 1;
        Node node = filtered.get(0);
        assert (node != null);
        vbox.getChildren().remove(node);
    }

    @NotNull
    private VBox fieldVBox(@NotNull TextField field) throws IhmException {
        if (!(field.getParent() instanceof Pane)) {
            throw new IhmException("Le parent du champ '" + field.getId() + "' n'est pas un Pane, donc on ne peut pas dynamiquement ajouter le Label servant à afficher le message d'erreur.");
        }
        VBox vbox;
        Pane parent = (Pane) field.getParent();
        if ((parent instanceof VBox) && (parent.getId().equals(idVBoxErreurSaisie(field)))) {
            vbox = (VBox) parent;
        } else { // La VBox n'a jamais encore été créée, on le fait :

            // Rq : Le simple fait d'ajouter le Filed à la VBox "détache" ce Field du Parent, donc il faut récupérer son index avant.
            int fieldIndex = parent.getChildren().indexOf(field);

            // Création de la VBox, qui ne contient que le Field (pour l'instant, on ajoutera le Label avec le message d'erreur... qu'en cas d'erreur) :
            vbox = new VBox();
            vbox.getStyleClass().setAll("erreurSaisie-vbox");
            //noinspection StringConcatenationMissingWhitespace
            vbox.setId(idVBoxErreurSaisie(field));
            vbox.getChildren().add(field);

            // Ajout de la VBox au Parent :
            parent.getChildren().add(fieldIndex, vbox);

            // Rq : Pas besoin de supprimer le Field du Parent, le simple fait de l'ajouter à la VBox le "détache" du Parent.
            //parent.getChildren().remove(field);
        }
        return vbox;
    }

    private String idLabelErreurSaisie(@NotNull TextField field) {
        //noinspection StringConcatenationMissingWhitespace
        return field.getId() + "ErreurSaisie-label";
    }

    private String idVBoxErreurSaisie(@NotNull TextField field) {
        //noinspection StringConcatenationMissingWhitespace
        return field.getId() + "ErreurSaisie-vbox";
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

    @NotNull
    public <R extends RapportService> R afficherProgression(@NotNull String titre, @NotNull Task<R> task) throws IhmException {
        final ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.setTitle(titre);
        progressDialog.setHeaderText(titre);
//        task.messageProperty().addListener((observable, oldValue, newValue) -> progressDialog.setContentText(newValue)); Redondant, car le ProgressDialog doit déjà le faire (ou un autre compsant JavaFX)
        progressDialog.setResizable(true);
        progressDialog.getDialogPane().setPrefWidth(500);
        progressDialog.getDialogPane().setPrefHeight(200);
        ((Stage) progressDialog.getDialogPane().getScene().getWindow()).getIcons().setAll(primaryStage.getIcons());

        // Le Worker (task) doit être lancée en background pour que l'IHM continue de fonctionner (le resize du progressDialog, l'affiche du PogressBar dans le ProgressDialog, etc.).
        Thread taskThread = new Thread(task, "progressDialog");
        taskThread.start();

        progressDialog.initModality(Modality.APPLICATION_MODAL); // Cf. https://stackoverflow.com/questions/29625170/display-popup-with-progressbar-in-javafx
        progressDialog.showAndWait(); // C'est le Worker (task) qui fermera ce dialog, "on succeeded".

        R resultat;
        try {
            resultat = task.get(); // Waits until task is completed.
            taskThread.join(); // Pas nécessaire.
        } catch (InterruptedException | ExecutionException e) {
            throw new IhmException("Impossible d'exécuter le traitement.", e);
        }

        return resultat;
    }


    @Override
    public void start(@SuppressWarnings("ParameterHidesMemberVariable") @NotNull Stage primaryStage) throws Exception {
        try {
            LOGGER.info("Application en cours de démarrage...");

            this.primaryStage = primaryStage;

            primaryStage.setTitle(APP_NAME);
            //
            primaryStage.setScene(new Scene(applicationView));
            //
            primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/images/planCharge-logo.png")));
            //
/*
            // Cf. https://stackoverflow.com/questions/40320199/how-to-automatically-resize-windows-in-javafx-for-different-resolutions
            final double screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
            final double screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
            primaryStage.setMaxWidth(screenWidth);
            primaryStage.setMaxHeight(screenHeight);
*/
            primaryStage.setResizable(true);
//            primaryStage.setFullScreen(true);
            primaryStage.setMaximized(true);
            //
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
            //
            applicationController.afficherModuleJoursFeries();

            LOGGER.info("Application démarrée.");
        } catch (Throwable e) {
            String erreur = "Impossible de démarrer l'IHM.";
            LOGGER.error(erreur, e);
            throw new Exception(erreur, e);
        }
    }

    @Null
    private LocalDate dateEtatPrecedente() {
        // TODO FDA 2017/04 Récupérer la dernière date d'état dans les préférences de l'utilisateur.
//        return LocalDate.of(2017, 4, 17);
        return null;
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Application en cours d'arrêt...");
        super.stop();
        LOGGER.info("Application arrêtée.");
        Platform.exit();
    }

    public void definirTitre(String titre) {
        primaryStage.setTitle(titre);
    }

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

}
