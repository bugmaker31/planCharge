package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresApplicatifs;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ServiceException;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    private static ParametresApplicatifs params = ParametresApplicatifs.instance();

    // Les menus :

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

    // L'IHM :
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

    public void init() {
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
    }


/*
    Menu "Fichier" :
     */

    @FXML
    private void charger(ActionEvent event) {
        LOGGER.debug("> Fichier > Charger");
        try {
            charger();
        } catch (IhmException e) {
            LOGGER.error("Impossible de charger le plan de charge.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de charger le plan de charge",
                    e.getLocalizedMessage(),
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
            fileChooser.setInitialDirectory(new File(params.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE)));
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
                    e.getLocalizedMessage(),
                    400, 200
            );
        }
    }

    private void charger(@NotNull File ficPlanCharge) throws IhmException {
        if (ficPlanCharge == null) {
            throw new IhmException("Impossible de charger le plan de charge, pas de fichier XML indiqué.");
        }
        try {

            PlanCharge planCharge = planChargeService.charger(ficPlanCharge);

            planChargeBean.init(planCharge);

            ihm.definirDateEtat(planCharge.getDateEtat());
            ihm.afficherPopUp(
                    Alert.AlertType.INFORMATION,
                    "Chargement terminé",
                    "Le chargement est terminé (" + planChargeBean.getPlanificationsBeans().size() + " tâches).",
                    400, 200
            );
            afficherModuleCharges();

        } catch (ServiceException e) {
            throw new IhmException("Impossible de charger le plan de charge depuis le fichier '" + ficPlanCharge.getAbsolutePath() + "'.", e);
        }
    }

    @FXML
    private void sauver(ActionEvent event) {
        LOGGER.debug("> Fichier > Sauver");

        if (planChargeBean.getDateEtat() == null || planChargeBean.getPlanificationsBeans() == null) {
            LOGGER.warn("Impossible de sauver un plan de charge non défini.");
            ihm.afficherPopUp(
                    Alert.AlertType.WARNING,
                    "Impossible de sauver le plan de charge",
                    "Impossible de sauver un plan de charge sans date d'état et/ou planification.",
                    500, 200
            );
            return;
        }

        try {
            PlanCharge planCharge = planChargeBean.extract();

            planChargeService.sauver(planCharge);

            File ficPlanCharge = planChargeService.fichierPersistancePlanCharge(planChargeBean.getDateEtat());
            ihm.afficherPopUp(
                    Alert.AlertType.INFORMATION,
                    "Sauvegarder terminée",
                    "Les " + planCharge.getPlanifications().size() + " lignes du plan de charge"
                            + " en date du " + planCharge.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                            + " ont été sauvées (dans le fichier '" + ficPlanCharge.getAbsolutePath() + "').",
                    500, 300
            );

        } catch (IhmException | ServiceException e) {
            LOGGER.error("Impossible de sauver le plan de charge.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de sauver le plan de charge",
                    "Impossible de sauver le plan de charge en date du " + planChargeBean.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".",
                    500, 200
            );
        }
    }

    @FXML
    private void majTachesDepuisCalc(ActionEvent event) {
        LOGGER.debug("> Fichier > Importer > Taches depuis Calc");
        try {
            majTachesDepuisCalc();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'importer les tâches.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'importer les tâches",
                    e.getLocalizedMessage(),
                    400, 200
            );
        }
    }

    private void majTachesDepuisCalc() throws IhmException {
        File ficCalc;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Indiquez le fichier Calc (LibreOffice) qui contient les tâches ('suivi des demandes'') : ");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("LibreOffice Calc", "*.ods")
        );
        String nomRepFicCalc;
        try {
            // TODO FDA 2017/05 C'est le répertoire des XML, pas forcément des ODS. Plutôt regarder dans les préférences de l'utilisateur ?
            nomRepFicCalc = params.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE);
        } catch (KernelException e) {
            throw new IhmException("Impossible de déterminer le répertoire de persistance des tâches.", e);
        }
        fileChooser.setInitialDirectory(new File(nomRepFicCalc));
        ficCalc = fileChooser.showOpenDialog(ihm.getPrimaryStage());
        if (ficCalc == null) {
            ihm.afficherPopUp(
                    Alert.AlertType.INFORMATION,
                    "MàJ annulé",
                    "La mise à jour des tâches depuis un fichier Calc a été annulé par l'utilisateur.",
                    400, 200
            );
            return;
        }

        majTachesDepuisCalc(ficCalc);
    }

    // TODO FDA 23017/02 Afficher une "progress bar".
    private void majTachesDepuisCalc(@NotNull File ficCalc) throws ControllerException {

        try {
            PlanCharge planCharge = planChargeBean.extract();
            planChargeService.majTachesDepuisCalc(planCharge, ficCalc);
        } catch (IhmException | ServiceException e) {
            throw new ControllerException("Impossible de mettre à jour les tâches depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        }

        ihm.afficherPopUp(
                Alert.AlertType.INFORMATION,
                "Tâches mises à jour importées",
                "Les tâches ont été mises à jour : "
                        + "\n- depuis le fichier : " + ficCalc.getAbsolutePath()
                        + "\n- nombre de tâches au final :" + planChargeBean.getPlanificationsBeans().size(),
                700, 300
        );

        afficherModuleCharges();
    }


    @FXML
    private void importerPlanChargeDepuisCalc(ActionEvent event) {
        LOGGER.debug("> Fichier > Importer > Plan charge depuis Calc");
        try {
            importerPlanChargeDepuisCalc();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'importer le plan de charge.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'importer le plan de charge",
                    e.getLocalizedMessage(),
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
            nomRepFicCalc = params.getParametrage(PlanChargeDao.CLEF_PARAM_REP_PERSISTANCE);
        } catch (KernelException e) {
            throw new IhmException("Impossible de déterminer le répertoire de persistance du plan de charge.", e);
        }
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
        PlanCharge planCharge;
        try {
            planCharge = planChargeService.importerDepuisCalc(ficCalc);
        } catch (ServiceException e) {
            throw new ControllerException("Impossible d'importer le plan de charge depuis le fichier '" + ficCalc.getAbsolutePath() + "'.", e);
        }

        ihm.definirDateEtat(planCharge.getDateEtat());

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
    }

    @FXML
    private void quitter(ActionEvent event) {
        LOGGER.debug("> Fichier > Quitter");
        try {
            ihm.stop();
        } catch (Exception e) {
            LOGGER.error("Impossible de stopper l'application.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de stopper l'application",
                    "Erreur interne : " + e.getLocalizedMessage()
            );
        }
    }

    @FXML
    private void afficherPreferences(ActionEvent event) {
        LOGGER.debug("> Préférences");
        // TODO FDA 2017/04 Coder.
    }


    /*
    Menu "Editer" :
     */

    /**
     * Undo
     *
     * @param event
     */
    @FXML
    private void annuler(ActionEvent event) {
        // TODO FDA 2017/03 Coder.
        LOGGER.debug("> Editer > annuler");
        throw new NotImplementedException();
    }

    /**
     * Redo
     *
     * @param event
     */
    @FXML
    private void refaire(ActionEvent event) {
        // TODO FDA 2017/03 Coder.
        LOGGER.debug("> Editer > refaire");
        throw new NotImplementedException();
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
    }

    @FXML
    private void afficherModuleTaches(ActionEvent event) {
        LOGGER.debug("> Tâches");
        afficherModuleTaches();
    }

    @FXML
    private void afficherModuleCharges(ActionEvent event) {
        afficherModuleCharges();
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
    private void aPropos(ActionEvent event) {
        LOGGER.debug("> Aide > A propos");

        ihm.afficherPopUp(
                Alert.AlertType.INFORMATION,
                "A propos de l'application \"" + PlanChargeIhm.APP_NAME + "\"",
                "Gestion du plan de charge d'une équipe d'un centre de service."
                        + "\n"
                        + "\n(C) Frédéric Danna - 2017",
                400, 200
        );
    }

//    @FXML
    private void afficherModuleDisponibilites() {
//        applicationView.setCenter(disponibilitesView);
        // Cf. http://stackoverflow.com/questions/6902377/javafx-tabpane-how-to-set-the-selected-tab
        gestionTabPane.getSelectionModel().select(disponibilitesTab);
//        moduleCourant = "Disponibilités";
//        ihm.majTitre();
    }

//    @FXML
    private void afficherModuleTaches() {
//        applicationView.setCenter(tachesView);
        // Cf. http://stackoverflow.com/questions/6902377/javafx-tabpane-how-to-set-the-selected-tab
        gestionTabPane.getSelectionModel().select(tachesTab);
//        moduleCourant = "Tâches";
//        ihm.majTitre();
    }

//    @FXML
    private void afficherModuleCharges() {
//        applicationView.setCenter(chargesView);
        // Cf. http://stackoverflow.com/questions/6902377/javafx-tabpane-how-to-set-the-selected-tab
        gestionTabPane.getSelectionModel().select(chargesTab);
//        moduleCourant = "Charges";
//        ihm.majTitre();
    }


}
