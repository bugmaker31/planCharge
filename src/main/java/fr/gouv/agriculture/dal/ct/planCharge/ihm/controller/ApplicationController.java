package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

    private PlanChargeApplication application;

    private BorderPane getApplicationView() {
        return application.getApplicationView();
    }

    private Region getDisponibiliteView() {
        return application.getDisponibilitesView();
    }

    private Region getChargeView() {
        return application.getChargeView();
    }

    public void setApplication(PlanChargeApplication application) {
        this.application = application;
    }

    /*
    Menu "Fichier" :
     */

    @FXML
    private void quitter(ActionEvent event) throws Exception {
        LOGGER.debug("Fichier > Quitter");
        application.stop();
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
        LOGGER.debug("afficherModuleDispo");
        getApplicationView().setCenter(getDisponibiliteView());
    }

    @FXML
    private void afficherModuleCharge(ActionEvent event) {
        LOGGER.debug("afficherModuleCharge");
        getApplicationView().setCenter(getChargeView());
    }

    /*
    Menu "Aide" :
     */

    @FXML
    private void aPropos(ActionEvent event) {
        LOGGER.debug("Aide > A propos");

        Alert aProposInfo = new Alert(Alert.AlertType.INFORMATION);
        aProposInfo.setTitle(PlanChargeApplication.APP_NAME);
        aProposInfo.setHeaderText("A propos");
        aProposInfo.setContentText("Auteur : Frédéric Danna\n2017/03");

        aProposInfo.showAndWait();
    }
}
