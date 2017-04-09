package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeApplication;
import javafx.fxml.FXML;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleDisponibilitesController {

    private PlanChargeApplication application = PlanChargeApplication.APPLICATION();

    public void setApplication(PlanChargeApplication application) {
        this.application = application;
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleDisponibilitesController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    }
}
