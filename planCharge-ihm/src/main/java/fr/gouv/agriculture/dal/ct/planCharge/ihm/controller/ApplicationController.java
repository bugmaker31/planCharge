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
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ServiceException;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    private static ParametresApplicatifs params = ParametresApplicatifs.instance();

    @FXML
    private Menu menuDisponibilites;
    @FXML
    private Menu menuTaches;
    @FXML
    private Menu menuCharges;

    // L'IHM :
    @NotNull
//    @Autowired
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    // Les services métier :
    @NotNull
//    @Autowired
    private PlanChargeService planChargeService = PlanChargeService.instance();

    // Les données métier :
    @NotNull
//    @Autowired
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<PlanificationBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    public void init() {
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

    public void charger(@NotNull File ficPlanCharge) throws IhmException {
        if (ficPlanCharge == null) {
            throw new IhmException("Impossible de charger le plan de charge, pas de fichier XML indiqué.");
        }
        try {

            PlanCharge planCharge = planChargeService.charger(ficPlanCharge);

            planChargeBean.setDateEtat(planCharge.getDateEtat());
            planChargeBean.getPlanificationsBeans().clear();
            planCharge.getPlanifications().entrySet().stream().forEach(
                    planif -> planChargeBean.getPlanificationsBeans().add(new PlanificationBean(planif.getKey(), planif.getValue()))
            );

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
        try {
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

            Planifications planifications = new Planifications();
            planChargeBean.getPlanificationsBeans().stream().forEach(
                    planificationBean -> {

                        Tache tache = planificationBean.getTacheBean().extract();

                        Map<LocalDate, Double> calendrier = new HashMap<>();
                        List<Pair<LocalDate, DoubleProperty>> ligne = planificationBean.getCalendrier();
                        ligne.forEach(semaine -> calendrier.put(semaine.getKey(), semaine.getValue().doubleValue()));

                        planifications.ajouter(tache, calendrier);
                    }
            );
            PlanCharge planCharge = new PlanCharge(
                    planChargeBean.getDateEtat(),
                    planifications
            );

            try {

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

            } catch (ServiceException e) {
                LOGGER.error("Impossible de sauver le plan de charge.", e);
                ihm.afficherPopUp(
                        Alert.AlertType.ERROR,
                        "Impossible de sauver le plan de charge",
                        "Impossible de sauver le plan de charge en date du " + planCharge.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".",
                        500, 200
                );
            }
        } catch (IhmException e) {

        }
    }

    @FXML
    private void importerPlanChargeDepuisCalc(ActionEvent event) {
        LOGGER.debug("> Fichier > Importer depuis Calc");
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

    private void importerPlanChargeDepuisCalc() {
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
        planificationsBeans.setAll(planCharge.getPlanifications().taches().stream()
                .map(tache -> {
                    try {
                        Map<LocalDate, Double> calendrier = planCharge.getPlanifications().calendrier(tache);
                        return new PlanificationBean(tache, calendrier);
                    } catch (TacheSansPlanificationException e) {
                        throw new ControllerException("Impossible de définir la planification de la tâche " + tache.noTache() + ".", e);
                    }
                })
                .collect(Collectors.toList()));


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
    Menu "Gérer" :
     */

    @FXML
    private void afficherModuleDisponibilites(ActionEvent event) {
        LOGGER.debug("> Disponibilites");
        ihm.afficherModuleDisponibilites();
    }

    @FXML
    private void afficherModuleTaches(ActionEvent event) {
        LOGGER.debug("> Tâches");
        ihm.afficherModuleTaches();
    }

    @FXML
    private void afficherModuleCharges(ActionEvent event) {
        afficherModuleCharges();
    }

    private void afficherModuleCharges() {
        LOGGER.debug("> Charges");
        ihm.afficherModuleCharges();
    }

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

}
