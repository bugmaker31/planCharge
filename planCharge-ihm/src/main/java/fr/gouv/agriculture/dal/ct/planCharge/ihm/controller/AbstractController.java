package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by frederic.danna on 23/04/2017.
 */
public abstract class AbstractController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initialize();
        } catch (IhmException e) {
            LOGGER.error("Impossible d'initialiser le contrôleur.", e);
            throw new RuntimeException("Impossible d'initialiser le contrôleur.", e);
        }
    }

    abstract void initialize() throws IhmException;
}
