package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ApplicationController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    // Les services métier :
    private PlanChargeService planChargeService;
    public void setPlanChargeService(PlanChargeService planChargeService) {
        this.planChargeService = planChargeService;
    }

    /*
    Menu "Fichier" :
     */

    @FXML
    private void quitter(ActionEvent event) throws Exception {
        LOGGER.debug("Fichier > Quitter");
        getApplicationIhm().stop();
    }

    @FXML
    private void sauver(ActionEvent event) throws Exception {
        LOGGER.debug("Fichier > Sauver");

        PlanCharge planCharge = getApplicationIhm().getPlanCharge();
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
            getApplicationIhm().setPlanCharge(planChargeService.charger(dateEtat));
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
        getApplication().getIhm().afficherModuleDisponibilites();
    }

    @FXML
    private void afficherModuleTache(ActionEvent event) {
        LOGGER.debug("afficherModuleTaches");
        getApplication().getIhm().afficherModuleTaches();
    }

    @FXML
    private void afficherModuleCharge(ActionEvent event) {
        LOGGER.debug("afficherModuleCharge");
        getApplication().getIhm().afficherModuleCharge();
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
