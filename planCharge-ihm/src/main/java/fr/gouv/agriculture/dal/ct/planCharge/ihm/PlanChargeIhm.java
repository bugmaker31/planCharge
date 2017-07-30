package fr.gouv.agriculture.dal.ct.planCharge.ihm;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.util.ParametresIhm;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCell;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.*;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationsReglesGestionException;
import fr.gouv.agriculture.dal.ct.metier.service.RapportService;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.control.decoration.StyleClassDecoration;
import org.controlsfx.dialog.ProgressDialog;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.controlsfx.validation.decoration.CompoundValidationDecoration;
import org.controlsfx.validation.decoration.GraphicValidationDecoration;
import org.controlsfx.validation.decoration.StyleClassValidationDecoration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class PlanChargeIhm extends Application {

    @NotNull
    public static final String APP_NAME = "Plan de charge";

    @NotNull
    public static final String PATRON_FORMAT_DATE = "dd/MM/yy";

    @NotNull
    public static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern(PATRON_FORMAT_DATE);


    @NotNull
    public static ValidationSupport validationSupport() {
        // Cf. https://stackoverflow.com/questions/29607080/textfield-component-validation-with-controls-fx
        ValidationSupport validationSupport = new ValidationSupport();
        // TODO FDA 2017/07 Personnaliser la décoration (... ou pas ?).
        validationSupport.setValidationDecorator(new CompoundValidationDecoration(
                new StyleClassValidationDecoration("erreurSaisie", "warningSaisie"),
                new GraphicValidationDecoration()
        ));

        return validationSupport;
    }


    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeIhm.class);

    private static PlanChargeIhm instance;

    @NotNull
    private static Map<String, PopupWindow> popups = new HashMap<>();

    public static PlanChargeIhm instance() {
        return instance;
    }

    //    private static Contexte contexte = Contexte.instance();
    private static ParametresMetiers paramsMetier = ParametresMetiers.instance();
    private static ParametresIhm paramsIhm = ParametresIhm.instance();

    private static boolean estEnDeveloppement = false; // Par défaut.

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
    private Region ressourcesHumainesView;
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
    private JoursFeriesController joursFeriesController;
    @NotNull
    private RessourcesHumainesController ressourcesHumainesController;
    @NotNull
    private DisponibilitesController disponibilitesController;
    @NotNull
    private TachesController tachesController;
    @NotNull
    private ChargesController chargesController;

    @NotNull
    private Pane contentPane;

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
    public Region getRessourcesHumainesView() {
        return ressourcesHumainesView;
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

    public static <S, T> void controler(@NotNull TableCell<S, T> cell, @NotNull String title, @NotNull Function<T, String> validator) /*throws IhmException*/ {
        cell.itemProperty().addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
//                    Platform.runLater(() -> {
                                masquerErreurSaisie(cell);
                                String error = validator.apply(newValue);
                                if (error != null) {
                                    afficherErreurSaisie(cell, title, error);
                                }
//                            }
//                    );
                }
        );
    }

    public static void controler(@NotNull TextField field, @NotNull String title, @NotNull Function<String, String> validator) /*throws IhmException*/ {
        field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//                    Platform.runLater(() -> {
                                masquerErreurSaisie(field);
                                String error = validator.apply(newValue);
                                if (error != null) {
                                    afficherErreurSaisie(field, title, error);
                                }
//                            }
//                    );
                }
        );
    }

    public DisponibilitesController getDisponibilitesController() {
        return disponibilitesController;
    }

    public TachesController getTachesController() {
        return tachesController;
    }

    public ChargesController getChargesController() {
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

        // Cf. https://controlsfx.bitbucket.io/org/controlsfx/validation/ValidationSupport.html
        ValueExtractor.addObservableValueExtractor(control -> control instanceof TextFieldTableCell, control -> ((TextFieldTableCell) control).textProperty());
        ValueExtractor.addObservableValueExtractor(control -> control instanceof DatePickerTableCell, control -> ((DatePickerTableCell) control).textProperty());

        LOGGER.info("Application initialisée.");
    }

    private void chargerParametresApplicatifs() throws Exception {
        try {
            paramsMetier.init();
            paramsIhm.init();

            estEnDeveloppement = paramsIhm.getParametrage("execution.mode").equalsIgnoreCase("developpement");
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
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ApplicationView.fxml"));
            applicationView = loader.load();
            applicationController = loader.getController();
            contentPane = applicationController.getContentPane();
        }
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/JoursFeriesView.fxml"));
            joursFeriesView = loader.load();
            joursFeriesController = loader.getController();
        }
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/RessourcesHumainesView.fxml"));
            ressourcesHumainesView = loader.load();
            ressourcesHumainesController = loader.getController();
        }
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/DisponibilitesView.fxml"));
            disponibilitesView = loader.load();
            disponibilitesController = loader.getController();
        }
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/TachesView.fxml"));
            tachesView = loader.load();
            tachesController = loader.getController();
        }
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ChargesView.fxml"));
            chargesView = loader.load();
            chargesController = loader.getController();
        }
    }

    public void showError(@NotNull Thread thread, @NotNull Throwable throwable) {
        LOGGER.error("An (uncaught) error occurred (in thread " + thread.getName() + ").", throwable);
        if (Platform.isFxApplicationThread()) {

            // Cf. http://code.makery.ch/blog/javafx-dialogs-official/
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur interne");
            alert.setHeaderText("Une erreur non gérée est survenue.");

            // TODO FDA 2017/07 Tester (à n'afficher que pour le développement, pas pour la production car peut contenuir des informations sensibles comme les mots de passe, etc.).
            if (estEnDeveloppement) {
                alert.setContentText(throwable.getLocalizedMessage());
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                String exceptionText = sw.toString();

                Label label = new Label("Exception Java : ");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the dialog pane.
                alert.getDialogPane().setExpandableContent(expContent);
            }

            alert.showAndWait();
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


    @SuppressWarnings("HardcodedFileSeparator")
    private static final Image REQUIRED_INDICATOR_IMAGE = new Image("/images/required-indicator.png");
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Image ERROR_INDICATOR_IMAGE = new Image("/images/decoration-error.png");
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Image WARNING_INDICATOR_IMAGE = new Image("/images/decoration-warning.png");
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Image SECURED_INDICATOR_IMAGE = new Image("/images/decoration-shield.png");
    @SuppressWarnings("HardcodedFileSeparator")
    private static final Image FILTERABLE_INDICATOR_IMAGE = new Image("/images/decoration-filterable .png");

    private static void masquerErreurSaisie(@NotNull Control field) /*throws IhmException*/ {
        if (!popups.containsKey(idVBoxErreurSaisie(field))) {
            LOGGER.debug("Pas d'erreur de saisie affichée actuellement pour le champ " + field.getId() + ", rien à masquer donc.");
            return;
        }
        Decorator.removeAllDecorations(field);
        try {
            masquerPopupErreurSaisie(field);
        } catch (IhmException e) {
            // TODO FDA 2017/07 Trouver mieux que thrower un RuntimeException.
            throw new RuntimeException("Impossible de masquer l'erreur de saisie à l'IHM.", e);
        }
    }

    public void symboliserFiltrable(@NotNull Node... nodes) {
//        Platform.runLater(() -> {
        for (Node node : nodes) {
            Decorator.addDecoration(node, new GraphicDecoration(new ImageView(FILTERABLE_INDICATOR_IMAGE), Pos.BOTTOM_RIGHT, -FILTERABLE_INDICATOR_IMAGE.getWidth() / 2, -FILTERABLE_INDICATOR_IMAGE.getHeight() / 2));
        }
//        });
    }

    @SuppressWarnings("WeakerAccess")
    public static void symboliserChampObligatoire(@NotNull Control... fields) /*throws IhmException*/ {
        for (Control field : fields) {
            // TODO FDA 2017/07 Comprendre pourquoi il faut les 2 lignes ci-dessous à la fois (la 1ère affiche bien le symbole rouge dans le coin haut gauche des entêtes des TableColumn mais pas dans les TextFieldTableCell, la 2nde fait l'inverse).
            Decorator.addDecoration(field, new GraphicDecoration(new ImageView(REQUIRED_INDICATOR_IMAGE), Pos.TOP_LEFT, REQUIRED_INDICATOR_IMAGE.getWidth() / 2, REQUIRED_INDICATOR_IMAGE.getHeight() / 2));
            validationSupport().registerValidator(field, true, Validator.createEmptyValidator("Requis"));
        }
    }

    public static void symboliserChampObligatoire(@NotNull TableColumn... columns) /*throws IhmException*/ {
        for (TableColumn column : columns) {
            // TODO FDA 2017/07 Confirmer qu'utiliser un Label vide est une bonne façon de faire.
            Label label = new Label();
            symboliserChampObligatoire(label);
            column.setGraphic(label);
        }
    }

    private static void masquerPopupErreurSaisie(@NotNull Control field) throws IhmException {
        if (!popups.containsKey(idVBoxErreurSaisie(field))) {
            // TODO FDA 2017/07 Trouver mieux que thrower un RuntimeException.
            throw new RuntimeException("Impossible de retrouver la popup associée au control " + field.getId() + " (pour afficher l'erreur de saisie).");
        }
        PopupWindow popup = popups.get(idVBoxErreurSaisie(field));
        field.setOnMouseEntered(event -> {
        });
        field.setOnMouseExited(event -> {
        });
        popup.hide();
    }

    @NotNull
    public JoursFeriesController getJoursFeriesController() {
        return joursFeriesController;
    }

    public static void afficherErreurSaisie(@NotNull Control field, @NotNull String titre, @NotNull String message) /*throws IhmException*/ {
        Decorator.addDecoration(field, new GraphicDecoration(new ImageView(ERROR_INDICATOR_IMAGE), Pos.BOTTOM_RIGHT, -ERROR_INDICATOR_IMAGE.getWidth(), -ERROR_INDICATOR_IMAGE.getHeight()));
        Decorator.addDecoration(field, new StyleClassDecoration("erreurSaisie"));
        try {
            afficherPopUpErreurSaisie(field, titre, message);
        } catch (IhmException e) {
            // TODO FDA 2017/07 Trouver mieux que thrower un RuntimeException.
            throw new RuntimeException("Impossible d'afficher l'erreur de saisie à l'IHM.", e);
        }
    }

    @NotNull
    public RessourcesHumainesController getRessourcesHumainesController() {
        return ressourcesHumainesController;
    }

    private static void afficherPopUpErreurSaisie(@NotNull Control field, @NotNull String titre, @NotNull String message) throws IhmException {
        PopupWindow popup = createFieldPopup(field, titre, message);
        field.setOnMouseEntered(event -> ((PopOver) popup).show(field));
        field.setOnMouseExited(event -> popup.hide());
        ((PopOver) popup).show(field);
    }

    public void symboliserFiltrable(@NotNull TableColumn<?, ?>... columns) {
        Platform.runLater(() -> { // TODO FDA 2017/07 Supprimer si non nécessaire/utile.
            for (TableColumn<?, ?> column : columns) {
                if (column.getGraphic() == null) {
                    Label label = new Label();
                    column.setGraphic(label);
                }
                symboliserFiltrable(column.getGraphic());
            }
        });
    }

    @NotNull
    private static PopupWindow createFieldPopup(@NotNull Control field, @NotNull String titre, @NotNull String message) throws IhmException {
        PopOver popup = new PopOver();
        popup.setHeaderAlwaysVisible(true);

        Label label = new Label(message);
        label.setId(idLabelErreurSaisie(field));

        popup.setTitle(titre);
        popup.setContentNode(label);

        popup.getStyleClass().add("messageErreurSaisie");

        popups.put(idVBoxErreurSaisie(field), popup);
        return popup;
    }

    private static String idLabelErreurSaisie(@NotNull Control field) {
        //noinspection StringConcatenationMissingWhitespace
        return field.getId() + "ErreurSaisie-label";
    }

    private static String idVBoxErreurSaisie(@NotNull Control field) {
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
    public <R extends RapportService> R afficherProgression(@NotNull String titre, @NotNull Task<R> task) throws IhmException, ViolationsReglesGestionException {
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
//            taskThread.join(); // Pas nécessaire.
        } catch (CancellationException | InterruptedException | ExecutionException e) {
            if (e.getCause() instanceof ViolationsReglesGestionException) {
                throw (ViolationsReglesGestionException) e.getCause();
            }
            throw new IhmException("Impossible d'exécuter le traitement.", e);
        }

        /// Utile ? Impossible d'arriver à passer dans ce cas...
        if ((task.getState() != Worker.State.SUCCEEDED) || (task.getException() != null)) {
            throw new IhmException("Impossible d'exécuter le traitement.", task.getException());
        }

        return resultat;
    }


    public static void afficherViolationsReglesGestion(@NotNull String titre, @NotNull String message, @NotNull List<ViolationRegleGestion> violations) {
        for (ViolationRegleGestion violation : violations) {
            // TODO FDA 2017/07 Améliorer l'affichage.
            Notifications.create()
                    .title(titre)
                    .text((String) violation.getRegle().getFormateurMessage().apply(violation.getEntity()))
                    .hideAfter(Duration.INDEFINITE)
                    .showError();
        }
    }


    public static void afficherNotification(@NotNull String titre, @NotNull String message) {
        Notifications.create()
                .title(titre)
                .text(message)
                .showInformation();
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
            // Cf. https://stackoverflow.com/questions/40320199/how-to-automatically-resize-windows-in-javafx-for-different-resolutions
            Screen ecranParDefaut = Screen.getScreens().get((Screen.getScreens().size() >= 2) ? 1 : 0); // TODO FDA 2017/07 A stocker dans les préférences de l'utilisateur.
            final double screenWidth = (int) ecranParDefaut.getBounds().getWidth();
            final double screenHeight = (int) ecranParDefaut.getBounds().getHeight();
            primaryStage.setWidth(screenWidth);
            primaryStage.setHeight(screenHeight);
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
            // TODO FDA 2017/04 Juste pour accélérer les tests du développeur. A supprimer avant de livrer.
//            applicationController.afficherModuleJoursFeries();
            applicationController.afficherModuleRessourcesHumaines();
//            applicationController.importerPlanChargeDepuisCalc(new File("./donnees/DAL-CT_11_PIL_Plan de charge_2017s16_t3.18.ods"));
//        applicationController.afficherModuleDisponibilites();
//        applicationController.afficherModuleTaches();
//        applicationController.afficherModuleCharges();

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
        return LocalDate.of(2017, 4, 17);
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
