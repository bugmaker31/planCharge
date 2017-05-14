package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import javafx.fxml.FXML;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleDisponibilitesController extends AbstractController {

    private static ModuleDisponibilitesController instance;

    public static ModuleDisponibilitesController instance() {
        return instance;
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleDisponibilitesController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    void initialize() throws IhmException {
        // Rien... pour l'instant.
    }
}
