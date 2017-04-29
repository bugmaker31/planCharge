package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ServiceException;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    // L'IHM :
    @NotNull
    @Autowired
    private PlanChargeIhm ihm = PlanChargeIhm.getContext().getBean(PlanChargeIhm.class);

    // Les services métier :
    @NotNull
    @Autowired
    private PlanChargeService planChargeService = PlanChargeIhm.getContext().getBean(PlanChargeService.class);

    // Les données métier :
    @NotNull
    @Autowired
    private PlanChargeBean planChargeBean = PlanChargeIhm.getContext().getBean(PlanChargeBean.class);

/*
    Menu "Fichier" :
     */

    @FXML
    private void quitter(ActionEvent event) throws Exception {
        LOGGER.debug("Fichier > Quitter");
        ihm.stop();
    }

    @FXML
    private void sauver(ActionEvent event) throws Exception {
        LOGGER.debug("Fichier > Sauver");

        if (planChargeBean.getDateEtat() == null || planChargeBean.getPlanificationsBeans() == null) {
            LOGGER.warn("Impossible de sauver un plan de charge non défini.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Impossible de sauver le plan de charge");
            alert.setContentText("Impossible de sauver le plan de charge car non défini.");
            alert.showAndWait();
            return;
        }

        Planifications planifications = new Planifications();
        planChargeBean.getPlanificationsBeans().stream().forEach(
                planificationBean -> {
                    List<Pair<LocalDate, DoubleProperty>> ligne = planificationBean.getCalendrier();
                    Map<LocalDate, Double> calendrier = new HashMap<>();
                    ligne.forEach(semaine -> calendrier.put(semaine.getKey(), semaine.getValue().doubleValue()));
                    planifications.ajouter(planificationBean.getTacheBean().getTache(), calendrier);
                }
        );
        PlanCharge planCharge = new PlanCharge(
                planChargeBean.getDateEtat(),
                planifications
        );

        try {

            planChargeService.sauver(planCharge);

        } catch (ServiceException e) {
            LOGGER.error("Impossible de sauver les données.", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de sauver le plan de charge");
            alert.setContentText("Impossible de sauver le plan de charge en date du " + planCharge.getDateEtat().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".");
            alert.showAndWait();
        }
    }

    @FXML
    private void charger(ActionEvent event) throws Exception {
        LOGGER.debug("Fichier > Charger");
        LocalDate dateEtat = LocalDate.of(2017, 1, 1); // TODO FDA 2017/04 Débouchonner : lister les sauvegardes/fichiers/dates d'état existantes et permettre à l'utilisateur d'en choisir 1.
        try {

            PlanCharge planCharge = planChargeService.charger(dateEtat);

            planChargeBean.setDateEtat(planCharge.getDateEtat());
            planChargeBean.getPlanificationsBeans().clear();
            planCharge.getPlanifications().entrySet().stream().forEach(
                    planif -> planChargeBean.getPlanificationsBeans().add(new PlanificationBean(planif.getKey(), planif.getValue()))
            );
        } catch (ServiceException e) {
            LOGGER.error("Impossible de charger les données datées du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de charger le plan de charge");
            alert.setContentText("Impossible de charger les données datées du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".");
            alert.showAndWait();
        }
    }

    /*
    Menu "Editer" :
     */

    @FXML
    private void supprimer(ActionEvent event) {
        // TODO FDA 2017/03 Coder.
        LOGGER.debug("Editer > Supprimer");
        throw new NotImplementedException();
    }

    /*
    Menu "Gérer" :
     */

    @FXML
    private void afficherModuleDispo(ActionEvent event) {
        LOGGER.debug("afficherModuleDisponibilites");
        ihm.afficherModuleDisponibilites();
    }

    @FXML
    private void afficherModuleTache(ActionEvent event) {
        LOGGER.debug("afficherModuleTaches");
        ihm.afficherModuleTaches();
    }

    @FXML
    private void afficherModuleCharge(ActionEvent event) {
        LOGGER.debug("afficherModuleCharge");
        ihm.afficherModuleCharge();
    }

    @FXML
    private void importerDepuisCalc(ActionEvent event) {
        LOGGER.debug("Charges > Importer depuis Calc");
        // TODO FDA 2017/04 Coder.
    }


    /*
    Menu "Aide" :
     */

    @FXML
    private void aPropos(ActionEvent event) {
        LOGGER.debug("Aide > A propos");

        Alert aProposInfo = new Alert(Alert.AlertType.INFORMATION);
        aProposInfo.setTitle(PlanChargeIhm.APP_NAME);
        aProposInfo.setHeaderText("A propos");
        aProposInfo.setContentText("Auteur : Frédéric Danna\n2017/03");

        aProposInfo.showAndWait();
    }
}
