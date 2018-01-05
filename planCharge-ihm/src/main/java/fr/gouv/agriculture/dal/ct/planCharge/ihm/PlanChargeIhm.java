package fr.gouv.agriculture.dal.ct.planCharge.ihm;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.util.ParametresIhm;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCell;
import fr.gouv.agriculture.dal.ct.ihm.view.Notification;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationsReglesGestionException;
import fr.gouv.agriculture.dal.ct.metier.service.RapportService;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.decoration.Decoration;
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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("ClassHasNoToStringMethod")
public class PlanChargeIhm extends Application {

    @NotNull
    public static final String APP_NAME = "Plan de charge";

    public static final int NBR_SEMAINES_PLANIFIEES = Planifications.NBR_SEMAINES_PLANIFIEES;

    /**
     * Format : {@code <abr. du jour de la semaine ("lun.", "mar.", etc.)> <jour sur 2 chiffres>/<mois sur 2 chiffres>/<année sur 4 chiffres>}.
     * Exemple : "mar. 15/07/2018".
     *
     * @see DateTimeFormatter
     */
    @NotNull
    public static final String PATRON_FORMAT_DATE = "ccc d MMM yyyy";

    @SuppressWarnings("HardcodedFileSeparator")
    @NotNull
    public static final String PROMPT_FORMAT_DATE = "[J]J/[M]M/[AA]AA";


    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeIhm.class);


    public static PlanChargeIhm instance() {
        return instance;
    }

    private static PlanChargeIhm instance;


    public static void main(String[] args) {
        launch(args);
    }


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


    public static final ParametresIhm paramsIhm = ParametresIhm.instance();
    //    private static Contexte contexte = Contexte.instance();
    public static final ParametresMetiers paramsMetier = ParametresMetiers.instance();

    private static boolean estEnDeveloppement = false; // Par défaut.

    public static boolean estEnDeveloppement() {
        return estEnDeveloppement;
    }


    @SuppressWarnings("NullableProblems")
    @NotNull
    private Stage primaryStage;

    @SuppressWarnings("NullableProblems")
    @NotNull
    private BorderPane applicationView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region joursFeriesView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region ressourcesHumainesView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region projetsApplisView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region disponibilitesView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region tachesView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region chargesView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region revueWizardView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region tracageRevisionView;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private Region saisieEcheanceView;

    @SuppressWarnings("NullableProblems")
    @NotNull
    private Pane contentPane;

/*
    //    @Autowired
    @NotNull
    private SuiviActionsUtilisateur suiviActionsUtilisateur = SuiviActionsUtilisateur.instance();
*/


    @NotNull
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
    public Region getProjetsApplisView() {
        return projetsApplisView;
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

    @NotNull
    public Region getRevueWizardView() {
        return revueWizardView;
    }

    @NotNull
    public Region getTracageRevisionView() {
        return tracageRevisionView;
    }

    @NotNull
    public Region getSaisieEcheanceView() {
        return saisieEcheanceView;
    }


    /*
    La couche "Controller" :
     */

    @SuppressWarnings("NullableProblems")
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
    @SuppressWarnings("NullableProblems")
    @NotNull
    private JoursFeriesController joursFeriesController;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private RessourcesHumainesController ressourcesHumainesController;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private ProjetsApplisController projetsApplisController;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private DisponibilitesController disponibilitesController;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TachesController tachesController;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private ChargesController chargesController;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private RevueWizardController revueWizardController;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TracageRevisionController tracageRevisionController;
    @SuppressWarnings("NullableProblems")
    @NotNull
    private SaisieEcheanceController saisieEcheanceController;


    @NotNull
    public ApplicationController getApplicationController() {
        return applicationController;
    }

    @NotNull
    public DisponibilitesController getDisponibilitesController() {
        return disponibilitesController;
    }

    @NotNull
    public JoursFeriesController getJoursFeriesController() {
        return joursFeriesController;
    }

    @NotNull
    public RessourcesHumainesController getRessourcesHumainesController() {
        return ressourcesHumainesController;
    }

    @NotNull
    public ProjetsApplisController getProjetsApplisController() {
        return projetsApplisController;
    }

    @NotNull
    public TachesController getTachesController() {
        return tachesController;
    }

    @NotNull
    public ChargesController getChargesController() {
        return chargesController;
    }

    @NotNull
    public RevueWizardController getRevueWizardController() {
        return revueWizardController;
    }

    @NotNull
    public TracageRevisionController getTracageRevisionController() {
        return tracageRevisionController;
    }

    @NotNull
    public SaisieEcheanceController getSaisieEcheanceController() {
        return saisieEcheanceController;
    }

/*
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();
*/


    public <S, T> void controler(@NotNull TableCell<S, T> cell, @NotNull String title, @NotNull Function<T, String> validator) /*throws IhmException*/ {
        //noinspection RedundantTypeArguments
        this.<T>controler(cell, cell.itemProperty(), title, validator, (Control field) -> cell.getIndex() != -1);
    }

    public <T> void controler(@NotNull TextInputControl field, @NotNull String title, @NotNull Function<T, String> validator) /*throws IhmException*/ {
        //noinspection RedundantTypeArguments
        this.<T>controler(field, (ObservableValue<? extends T>) field.textProperty(), title, validator, null);
    }

    private <T> void controler(@NotNull Control field, @NotNull ObservableValue<? extends T> obsValue, @NotNull String title, @NotNull Function<T, String> validator, @Null Predicate<Control> filter) {
        obsValue.addListener((ObservableValue<? extends T> observable, T oldValue, T newValue) -> {
//            Platform.runLater(() -> { // Surtout pas, sinon JavaFX est "perdu" et affiche tous les champs d'une TableView en erreur car déclenche avec newValue à null (voir TableCell#isFirstRun).
            if ((filter != null) && !filter.test(field)) {
                return;
            }
            try {
                masquerErreurSaisie(field);
                String error = validator.apply(newValue);
                if (error != null) {
                    LOGGER.debug("Erreur de saisie détectée au niveau du champ '{}' pour la valeur '{}' : << {} >>", id(field), newValue, error);
                    afficherErreurSaisie(field, title, error);
                }
            } catch (IhmException e) {
                // TODO FDA 2017/07 Trouver mieux que thrower une RuntimeException.
                throw new RuntimeException("Impossible de contrôler la saisie du champ '" + field.getId() + "'.", e);
            }
        });
//        });
    }


    /**
     * Constructor
     */
    @SuppressWarnings("RedundantNoArgConstructor")
    public PlanChargeIhm() {
        super();
        instance = this;
    }


    @Override
    public void init() throws Exception {
        LOGGER.info("Application en cours d'initialisation...");

        // Cf. http://stackoverflow.com/questions/26361559/general-exception-handling-in-javafx-8
        Thread.setDefaultUncaughtExceptionHandler(this::showError);

        super.init();

        chargerParametresApplicatifs();

//        injecter();

        initialiserViewsEtControllers();

        // Cf. https://controlsfx.bitbucket.io/org/controlsfx/validation/ValidationSupport.html
        ValueExtractor.addObservableValueExtractor(control -> control instanceof TextFieldTableCell, control -> ((TextFieldTableCell) control).textProperty());
        ValueExtractor.addObservableValueExtractor(control -> control instanceof DatePickerTableCell, control -> ((DatePickerTableCell) control).textProperty());

        LOGGER.info("Application initialisée.");
    }

    public int noEcranParDefaut() {
        return (Screen.getScreens().size() >= 2) ? 1 : 0; // TODO FDA 2017/07 A stocker dans les préférences de l'utilisateur.
    }

    @SuppressWarnings("ProhibitedExceptionDeclared")
    @Override
    public void start(@SuppressWarnings({"ParameterHidesMemberVariable", "NullableProblems"}) @NotNull Stage primaryStage) throws Exception {
        //noinspection OverlyBroadCatchBlock
        try {
            LOGGER.debug("Application en cours de démarrage...");

            this.primaryStage = primaryStage;

            primaryStage.setTitle(APP_NAME);
            //
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/planCharge-logo.png")));
            //
            // Cf. https://stackoverflow.com/questions/40320199/how-to-automatically-resize-windows-in-javafx-for-different-resolutions
/*
            Screen ecranParDefaut = Screen.getScreens().get(noEcranParDefaut());
            double screenWidth = ecranParDefaut.getBounds().getWidth();
            double screenHeight = ecranParDefaut.getBounds().getHeight();
            primaryStage.setWidth(screenWidth);
            primaryStage.setHeight(screenHeight);
*/
            primaryStage.setResizable(true);
//            primaryStage.setFullScreen(true);
            primaryStage.setMaximized(true);
            //
            primaryStage.setScene(new Scene(applicationView));
            //
            theme.setValue(Theme.SOMBRE); // TODO FDA 2018/01 Récupérer le thème dans les préférences de l'utilisateur.
            //
            primaryStage.show();

            // Chargement des données utilisées dernièrement (if any) :
            LocalDate dateEtatPrec = dateEtatPrecedente();
            if (dateEtatPrec != null) {
                ChargeService planChargeService = ChargeService.instance();
                File ficCalc = planChargeService.fichierPersistancePlanCharge(dateEtatPrec);
                if (ficCalc.exists()) {
                    applicationController.charger(dateEtatPrec);
                }
            }
            // TODO FDA 2017/04 Juste pour accélérer les tests du développeur. A supprimer avant de livrer.
            if (estEnDeveloppement) {
                // Référentiels :
//                applicationController.afficherModuleJoursFeries();
//                applicationController.afficherModuleRessourcesHumaines();
//                applicationController.afficherModuleProjetsApplis();
                // Imports :
                //noinspection HardcodedFileSeparator
//                applicationController.majTachesDepuisCalc(new File("./donnees/DAL-CT_14_PIL_Suivi des demandes_T4.56.ods"));
                //noinspection HardcodedFileSeparator
//                applicationController.importerPlanChargeDepuisCalc(new File("./donnees/DAL-CT_11_PIL_Plan de charge_2017s42_t3.44.ods"));
                // Affichage des modules/écrans :
//                applicationController.afficherModuleDisponibilites();
//                applicationController.afficherModuleTaches();
//                applicationController.afficherModuleCharges();
                // Autres :
                applicationController.afficherAssistantRevue();
                applicationController.afficherFenetreTracageRevision();
            }

            LOGGER.info("Application démarrée.");
        } catch (Exception e) {
            String erreur = "Impossible de démarrer l'IHM.";
            LOGGER.error(erreur, e);
            throw new Exception(erreur, e);
        }
    }

    @SuppressWarnings("ProhibitedExceptionDeclared")
    @Override
    public void stop() throws Exception {
        LOGGER.info("Application en cours d'arrêt...");
        super.stop();
        LOGGER.info("Application arrêtée.");
        Platform.exit();
    }


    @SuppressWarnings("ProhibitedExceptionDeclared")
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
        planChargeService = contexte.getBean(ChargeService.class);
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
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ProjetsApplisView.fxml"));
            projetsApplisView = loader.load();
            projetsApplisController = loader.getController();
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
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/RevueWizardView.fxml"));
            revueWizardView = loader.load();
            revueWizardController = loader.getController();
        }
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/TracageRevisionView.fxml"));
            tracageRevisionView = loader.load();
            tracageRevisionController = loader.getController();
        }
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/SaisieEcheanceView.fxml"));
            saisieEcheanceView = loader.load();
            saisieEcheanceController = loader.getController();
        }
    }

    private void showError(@NotNull Thread thread, @NotNull Throwable throwable) {
        LOGGER.error("An (uncaught) error occurred (in thread " + thread.getName() + ").", throwable);
        if (Platform.isFxApplicationThread()) {

            // Cf. http://code.makery.ch/blog/javafx-dialogs-official/
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur interne");
            alert.setHeaderText("Une erreur non gérée est survenue.");

            // TODO FDA 2017/07 Tester (à n'afficher que pour le développement, pas pour la production car peut contenuir des informations sensibles comme les mots de passe, etc.).
            if (estEnDeveloppement) {
                alert.setContentText(throwable.getLocalizedMessage());
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                throwable.printStackTrace(printWriter);
                String exceptionText = stringWriter.toString();

                Label label = new Label("Exception Java : ");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(false);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                // Set expandable Exception into the impl.org.controlsfx.dialog pane.
                alert.getDialogPane().setExpandableContent(expContent);
            }

            alert.showAndWait();
        }
    }


    // Notifiactions des violations des Règles de Gestion ---------------------------------------

    private Set<Notification> notificationsViolationsReglesGestion = new HashSet<>(10);

    public void afficherViolationsReglesGestion(@NotNull String titre, @NotNull String message, @SuppressWarnings("rawtypes") @NotNull List<ViolationRegleGestion> violations) {
//        Platform.runLater(() -> {

        masquerNotificationsViolationsReglesGestion();

        //noinspection rawtypes
        for (ViolationRegleGestion violation : violations) {
            //noinspection HardcodedLineSeparator,unchecked
            Notification notifErreur = new Notification()
                    .title(titre)
                    .text(
                            violation.getRegle().getLibelle() + " (" + violation.getRegle().getCode() + ")"
                                    + "\n" + violation.getRegle().getFormateurMessage().apply(violation.getEntity())
                    )
                    .hideAfter(Duration.INDEFINITE); // Voir #masquerNotificationsViolationsReglesGestion().
            notifErreur.showError();
            notificationsViolationsReglesGestion.add(notifErreur);
        }
//    });
    }

    @SuppressWarnings({"WeakerAccess"})
    public void masquerNotificationsViolationsReglesGestion() {
        for (Notification notification : notificationsViolationsReglesGestion) {
            notification.hide();
        }
    }

    // Saisie ---------------------------------------

    public <S, T> void interdireEdition(@NotNull TableColumn<S, T> column, @NotNull String message, @Null ButtonType... typesBouton) {
        // Vu qu'on veut afficher un message lorsque l'utilisateur demande à édite une cellule de la colonne, il faut rendre la table éditable.
        if (!column.getTableView().isEditable()) {
            // TODO FDA 2017/08 Un peu dangereux comme implémentation, de rendre la table éditable. Trouver mieux.
            column.getTableView().setEditable(true);
            LOGGER.warn("La table '{}' vient d'être rendue éditable.", column.getTableView().getId());
        }
        if (!column.isEditable()) {
            // TODO FDA 2017/08 Un peu dangereux comme implémentation, de rendre la colonne éditable. Trouver mieux.
            column.setEditable(true);
            LOGGER.warn("La colonne '{}' vient d'être rendue éditable.", column.getId());
        }
/* N'empêche pas le passage en mode "édition" pour la cellule.
        column.setOnEditStart(event -> {
            afficherInterdictionEditer(message, typesBouton);
//            event.consume();
        });
*/
        // Cf. https://stackoverflow.com/questions/25910066/javafx-handling-events-on-tableview
        column.addEventHandler(TableColumn.CellEditEvent.ANY, event -> {
            //noinspection unchecked
            TableColumn.CellEditEvent<S, T> cellEditEvent = (TableColumn.CellEditEvent<S, T>) event;
            if ((cellEditEvent.getTableColumn() == null)/* || !cellEditEvent.getTableColumn().equals(column)*/) {
                return;
            }
            afficherInterdictionEditer(message);
            event.consume(); /// FIXME FDA 2017/08 Ne sufit pas : Consuming an event does not prevent other EventHandlers on TableView from being invoked.
        });
    }

    public void afficherInterdictionEditer(@NotNull String message) {
        afficherDialog(AlertType.WARNING, "Données non modifiables", message, 400, 200);
    }


    @SuppressWarnings("WeakerAccess")
    public static void symboliserChampsObligatoires(@NotNull Control... fields) throws IhmException {
        for (Control field : fields) {
            // TODO FDA 2017/07 Comprendre pourquoi il faut les 2 lignes ci-dessous à la fois (la 1ère affiche bien le symbole rouge dans le coin haut gauche des entêtes des TableColumn mais pas dans les EditableAwareTextFieldTableCell, la 2nde fait l'inverse).
            Decorator.addDecoration(field, new GraphicDecoration(new ImageView(REQUIRED_INDICATOR_IMAGE), Pos.TOP_LEFT, REQUIRED_INDICATOR_IMAGE.getWidth() / 2, REQUIRED_INDICATOR_IMAGE.getHeight() / 2));
            validationSupport().registerValidator(field, true, Validator.createEmptyValidator("Requis"));
        }
    }

    public static void symboliserNoeudsFiltrables(@NotNull Node... nodes) throws IhmException {
//        Platform.runLater(() -> { // TODO FDA 2017/07 Supprimer si non nécessaire/utile.
        for (Node node : nodes) {
            Decorator.addDecoration(node, new GraphicDecoration(new ImageView(FILTERABLE_INDICATOR_IMAGE), Pos.BOTTOM_RIGHT, -FILTERABLE_INDICATOR_IMAGE.getWidth() / 2, -FILTERABLE_INDICATOR_IMAGE.getHeight() / 2));
        }
//        });
    }

    // Notifications ---------------------------------------

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

    public void afficherNotificationInfo(@NotNull String titre, @NotNull String message) {
        new Notification()
                .darkStyle() /*TODO FDA 2018/01 Trouver comment utiliser dark-theme.css*/
                .title(titre)
                .text(message)
                .hideAfter(new Duration(5000)) // 5 secondes. TODO FDA 2017/08 Permettre à l'utilisateur de changer ce paramètre (à mémoriser dans ses préférences ?)
                .showInformation();
    }

    public void afficherNotificationWarning(@NotNull String titre, @NotNull String message) {
        new Notification()
                .darkStyle() /*TODO FDA 2018/01 Trouver comment utiliser dark-theme.css*/
                .title(titre)
                .text(message)
                .hideAfter(Duration.INDEFINITE) // TODO FDA 2017/08 Permettre à l'utilisateur de changer ce paramètre (à mémoriser dans ses préférences ?)
                .showWarning();
    }


    // Pop-ups ---------------------------------------

    @NotNull
    private static final Map<Control, PopupControl> popups = new HashMap<>(10);

    @NotNull
    private static final Map<Control, EventHandler<MouseEvent>> popupsMouseEnteredHandlers = new HashMap<>(10);
    @NotNull
    private static final Map<Control, EventHandler<MouseEvent>> popupsMouseExitedHandlers = new HashMap<>(10);

    public static void activerPopup(@NotNull Control field, @NotNull String titre, @NotNull String message) throws IhmException {
        // On "programme" les actions sur ces 2 événements du champ :
//        String idPopup = idPopup(field);
        {
            EventHandler<MouseEvent> mouseEnteredHandler = event -> {
                try {
                    afficherPopup(field, titre, message);
                } catch (IhmException e) {
                    LOGGER.error("Impossible d'afficher la pop-up.", e);
                }
            };
            assert !popupsMouseEnteredHandlers.containsKey(field);
            popupsMouseEnteredHandlers.put(field, mouseEnteredHandler);
            field.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnteredHandler);
        }
        {
            // NB : Planifier le "hide" après avoir appelé "show", car sinon l'utilisateur peut déclencher un "onMouseExited" avant qu'on ait eu le temps de "show"er la PopUp (ce qui provoque des NPE).
            EventHandler<MouseEvent> mouseExitedHandler = event -> {
                try {
                    masquerPopup(field);
                } catch (IhmException e) {
                    LOGGER.error("Impossible de masquer la pop-up.", e);
                }
            };
            assert !popupsMouseExitedHandlers.containsKey(field);
            popupsMouseExitedHandlers.put(field, mouseExitedHandler);
            field.addEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedHandler);
        }
    }

    public static void desactiverPopup(@NotNull Control field) throws IhmException {
        // On "déprogramme" les actions sur ces 2 événements du champ :
//        String idPopup = idPopup(field);
        {
            assert popupsMouseEnteredHandlers.containsKey(field);
            EventHandler<MouseEvent> mouseEnteredEventHandler = popupsMouseEnteredHandlers.get(field);
            assert mouseEnteredEventHandler != null;
            field.removeEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnteredEventHandler);
            popupsMouseEnteredHandlers.remove(field);
        }
        {
            assert popupsMouseExitedHandlers.containsKey(field);
            EventHandler<MouseEvent> mouseExitedEventHandler = popupsMouseExitedHandlers.get(field);
            assert mouseExitedEventHandler != null;
            field.removeEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedEventHandler);
            popupsMouseExitedHandlers.remove(field);
        }
    }

    public static void afficherPopup(@NotNull Control field, @NotNull String titre, @NotNull String message) throws IhmException {
        PopupWindow popup = createPopup(field, titre, message);
        popups.put(field, (PopupControl) popup);
        ((PopOver) popup).show(field);
    }

    public static void masquerPopup(@NotNull Control field) throws IhmException {
//        String idPopup = idPopup(field);
        if (!popups.containsKey(field)) {
            throw new IhmException("Impossible de retrouver la popup associée au champ " + id(field) + " (la popup qui affiche le message d'erreur de saisie).");
        }
        PopupWindow popup = popups.get(field);
        popup.hide();
        popups.remove(field);
    }

    @NotNull
    private static PopupControl createPopup(@NotNull Control field, @NotNull String titre, @NotNull String message) throws IhmException {
//        String idPopup = idPopup(field);
        if (popups.containsKey(field)) {
            return popups.get(field);
        }

        PopOver popup = new PopOver();
        popup.setId("popup"); // Juste par soucis de clarté (pour faciliter le débogage, aussi).

        popup.setHeaderAlwaysVisible(true);

        Label label = new Label(message);
//        label.setId(idLabelPopup(field));

        popup.setTitle(titre);
        popup.setContentNode(label);

        popup.getStyleClass().add("popup");
        return popup;
    }

/*
    private static String idPopup(@NotNull Control field) {
        //noinspection StringConcatenationMissingWhitespace
        return id(field) + FIELD_ID_SEPARATOR + "popup";
    }
*/


    // DOM ---------------------------------------

    private static final String FIELD_ID_SEPARATOR = ".";

    @NotNull
    private static String id(@NotNull Node noeud) {
        String idNoeud = idNoeud(noeud);
        if (noeud.getParent() == null) {
            return idNoeud;
        }
        String idParent = id(noeud.getParent());
        assert !idParent.isEmpty();
        return idParent + FIELD_ID_SEPARATOR + idNoeud; // TODO FDA 2017/09 S'assurer qu'on génère bien ainsi un ID *unique*.
    }

    private static String idNoeud(Node noeud) {
        if ((noeud instanceof IndexedCell) && (noeud.getId() == null)) {
            return "<" + noeud.getClass().getSimpleName() + ">" + "#" + ((IndexedCell) noeud).getIndex();
        }
        if ((noeud.getId() == null) || noeud.getId().isEmpty()) {
            return "<" + noeud.getClass().getSimpleName() + ">";
        }
        return noeud.getId();
    }


    // Erreurs de saisie ---------------------------------------

    private static final Map<Node, Set<Decoration>> nodesDecorations = new HashMap<>(10);

    @SuppressWarnings("WeakerAccess")
    public static void afficherErreurSaisie(@NotNull Control field, @NotNull String titre, @NotNull String message) throws IhmException {
        Platform.runLater(() -> { // Sinon les Décorations ne sont pas affichées. TODO FDA 2017/09 Comprendre pourquoi.
            GraphicDecoration graphicDecoration = new GraphicDecoration(new ImageView(ERROR_INDICATOR_IMAGE), Pos.BOTTOM_RIGHT, -ERROR_INDICATOR_IMAGE.getWidth(), -ERROR_INDICATOR_IMAGE.getHeight());
            StyleClassDecoration styleDecoration = new StyleClassDecoration("erreurSaisie");
            Decorator.addDecoration(field, graphicDecoration);
            Decorator.addDecoration(field, styleDecoration);
            assert !nodesDecorations.containsKey(field);
            nodesDecorations.put(field, new HashSet<>(2));
            nodesDecorations.get(field).add(graphicDecoration);
            nodesDecorations.get(field).add(styleDecoration);
            try {
                activerPopup(field, titre, message);
            } catch (IhmException e) {
                // TODO FDA 2017/07 Trouver mieux que thrower une RuntimeException.
                throw new RuntimeException("Impossible d'activer l'affichage de l'erreur de saisie à l'IHM.", e);
            }
        });
    }

    @SuppressWarnings("WeakerAccess")
    public static void masquerErreurSaisie(@NotNull Control field) throws IhmException {
        if (!popups.containsKey(field)) {
//            LOGGER.debug("Pas d'erreur de saisie affichée actuellement pour le champ {}, rien à masquer donc.", field.getId());
            return;
        }
        if (!nodesDecorations.containsKey(field) || (nodesDecorations.get(field) == null)) {
            // Ne devrait pas arriver, mais arrive parfois qd même (traitements en // de JavaFX ?).
            // Aucun impact car la décoration a déjà été enlevée (par qui ?), donc juste un warning (pas une erreur).
            LOGGER.warn("Le noeud '{}' a bien une pop-up pour afficher l'erreur de saisie, mais n'a pas de décoration pour symboliser l'erreur de saisie.", field.getId());
            return;
        }
        for (Decoration decoration : nodesDecorations.get(field)) {
            Decorator.removeDecoration(field, decoration);
        }
        nodesDecorations.remove(field);
        try {
            desactiverPopup(field);
        } catch (IhmException e) {
            // TODO FDA 2017/07 Trouver mieux que thrower une RuntimeException.
            throw new RuntimeException("Impossible de masquer l'erreur de saisie à l'IHM.", e);
        }
    }


    // Dialogs ---------------------------------------

    @NotNull
    public Optional<ButtonType> afficherDialog(@NotNull Alert.AlertType type, @NotNull String titre, @NotNull String message) {
        //noinspection ConstantConditions
        if (primaryStage == null) {
            LOGGER.warn("Impossbile d'afficher le message, application non entièrement initialisée (en cours de démarrage ?).");
            return Optional.empty();
        }
        return afficherDialog(type,
                titre, message,
                Math.max(100, primaryStage.getWidth() - 100), Math.max(primaryStage.getHeight() - 100, 100),
                null
        );
    }

    @NotNull
    public Optional<ButtonType> afficherDialog(@NotNull Alert.AlertType type, @NotNull String titre, @NotNull String message, @NotNull ButtonType boutonParDefaut, @NotNull ButtonType... bouttons) {
        return afficherDialog(type,
                titre, message,
                Math.max(100, primaryStage.getWidth() - 100), Math.max(primaryStage.getHeight() - 100, 100),
                boutonParDefaut, bouttons
        );
    }

    @NotNull
    public Optional<ButtonType> afficherDialog(@NotNull Alert.AlertType type, @NotNull String titre, @NotNull String message, double width, double height) {
        return afficherDialog(type,
                titre, message,
                width, height,
                null
        );
    }


    @NotNull
    public Optional<ButtonType> afficherDialog(@NotNull AlertType type, @NotNull String titre, @NotNull String message, double width, double height, @Null ButtonType typeBoutonParDefaut, @Null ButtonType... typesBouton) {
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
        if (typesBouton != null) {
            alert.getButtonTypes().addAll(typesBouton);
        }
        if (typeBoutonParDefaut != null) {
            alert.getButtonTypes()
                    .forEach(buttonType -> {
                        Button unBouton = (Button) alert.getDialogPane().lookupButton(buttonType);
                        unBouton.setDefaultButton(Objects.equals(buttonType, typeBoutonParDefaut));
                    });
        }

        alert.getDialogPane().setPrefWidth(width);
        alert.getDialogPane().setPrefHeight(height);

        alert.setResizable(true);

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        //noinspection ConstantConditions
        if (primaryStage == null) {
            LOGGER.warn("Impossible de reprendre les icônes de l'application, application non entièrement initialisée (en cours de démarrage ?).");
        } else {
            alertStage.getIcons().addAll(primaryStage.getIcons());
        }

        // Set CSS:
        // Cf. https://stackoverflow.com/questions/28417140/styling-default-javafx-dialogs
        {
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().addAll(styleSheets());
        }

        return alert.showAndWait();
    }

/*
    private void showErrorDialog(@NotNull String titre,  String errorMsg) {
        Stage impl.org.controlsfx.dialog = new Stage();
        impl.org.controlsfx.dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(PlanChargeIhm.class.getResource("/fr/gouv/agriculture/dal/ct/planCharge/ihm/view/ErrorView.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController) loader.getController()).setErrorText(errorMsg);
            impl.org.controlsfx.dialog.setScene(new Scene(root, 800, 400));
            impl.org.controlsfx.dialog.show();
        } catch (IOException e) {
            LOGGER.error("Impossible d'afficher la boîte de dialogue avec l'erreur.", e);
        }
    }
*/

    // Progression Dialog -------------------------------------

    @NotNull
    public <R extends RapportService> R afficherProgression(@NotNull String titre, @NotNull Task<R> task) throws IhmException, ViolationsReglesGestionException {
        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.setTitle(titre);
        progressDialog.setHeaderText(titre);
//        task.messageProperty().addListener((observable, oldValue, newValue) -> progressDialog.setContentText(newValue)); Redondant, car le ProgressDialog doit déjà le faire (ou un autre compsant JavaFX)
        progressDialog.setResizable(true);
        progressDialog.getDialogPane().setPrefWidth(1000);
        progressDialog.getDialogPane().setPrefHeight(200);
        ((Stage) progressDialog.getDialogPane().getScene().getWindow()).getIcons().setAll(primaryStage.getIcons());
        // Set CSS:
        // Cf. https://stackoverflow.com/questions/28417140/styling-default-javafx-dialogs
        {
            DialogPane dialogPane = progressDialog.getDialogPane();
            dialogPane.getStylesheets().addAll(styleSheets());
        }
        progressDialog.initModality(Modality.APPLICATION_MODAL); // Cf. https://stackoverflow.com/questions/29625170/display-popup-with-progressbar-in-javafx

        // Le Worker (task) doit être lancé en background pour que l'IHM continue de fonctionner (le resize du progressionDialog, l'affichage du PogressBar dans le ProgressDialog, etc.).
        Thread taskThread = new Thread(task, "progressionDialog");
        taskThread.start();

        progressDialog.showAndWait(); // C'est le Worker (task) qui fermera ce impl.org.controlsfx.dialog, "on succeeded".

        R resultat;
        try {
            resultat = task.get(); // Waits until task is completed.
//            taskThread.join(); // Pas nécessaire.
        } catch (CancellationException | InterruptedException | ExecutionException e) {
            if (e.getCause() instanceof ViolationsReglesGestionException) {
                //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
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


    @SuppressWarnings("ConstantConditions")
    @Null
    private LocalDate dateEtatPrecedente() {

        if (estEnDeveloppement) {
//            return LocalDate.of(2017, 10, 23);
            return LocalDate.of(2017, 11, 13);
        }

        // TODO FDA 2017/04 Récupérer la dernière date d'état dans les préférences de l'utilisateur.
        return null;
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


    // StyleSheets (CSS):

    public static final URL APP_CSS_URL = ApplicationController.class.getResource("/css/planCharge.css");

    static {
        if (APP_CSS_URL == null) {
            //noinspection HardcodedFileSeparator
            throw new NullPointerException("CSS not found for application.");
        }
    }

    public static final URL THEME_STANDARD_CSS_URL = ApplicationController.class.getResource("/com/sun/javafx/scene/control/skin/modena/modena.css");

    static {
        if (THEME_STANDARD_CSS_URL == null) {
            //noinspection HardcodedFileSeparator
            throw new NullPointerException("CSS not found for standard theme.");
        }
    }

    public static final URL THEME_SOMBRE_CSS_URL = ApplicationController.class.getResource("/css/dark_theme.css");

    static {
        if (THEME_SOMBRE_CSS_URL == null) {
            //noinspection HardcodedFileSeparator
            throw new NullPointerException("CSS not found for dark theme.");
        }
    }

    @NotNull
    public List<String> styleSheets() {
        List<String> styleSheetUrls = new ArrayList<>(2);

        if (getTheme() == Theme.SOMBRE) {
            styleSheetUrls.add(Theme.SOMBRE.getUrl().toExternalForm());
        }

        styleSheetUrls.add(APP_CSS_URL.toExternalForm());

        return styleSheetUrls;
    }


    // Themes :

    @SuppressWarnings("PublicInnerClass")
    public enum Theme {

        STANDARD(THEME_STANDARD_CSS_URL),
        SOMBRE(THEME_SOMBRE_CSS_URL);

        @NotNull
        private final URL url;

        Theme(@NotNull URL styleSheetUrl) {
            url = styleSheetUrl;
            if (url == null) {
                throw new NullPointerException("CSS not found: '" + styleSheetUrl.toString() + "'.");
            }
        }

        public String getName() {
            return name();
        }

        @NotNull
        public URL getUrl() {
            return url;
        }
    }

    @NotNull
    private Property<Theme> theme = new SimpleObjectProperty<>();

    {
        theme.addListener((observable, oldValue, newValue) -> {
            ObservableList<String> appStyleSheets = getApplicationView().getStylesheets();
            for (Theme theme : Theme.values()) {
                appStyleSheets.remove(theme.getUrl().toExternalForm());
            }
            if (newValue == null) {
                return;
            }
            appStyleSheets.add(newValue.getUrl().toExternalForm());
            LOGGER.debug("Thème = {}", newValue.getName());
        });
    }

    @Null
    public Theme getTheme() {
        return theme.getValue();
    }

    @NotNull
    public Property<Theme> themeProperty() {
        return theme;
    }
}
