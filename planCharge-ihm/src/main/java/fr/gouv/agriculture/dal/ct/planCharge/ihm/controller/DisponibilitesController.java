package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ModuleController;
import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.util.NotImplementedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Created by frederic.danna on 26/03/2017.
 * @author frederic.danna
 */
public class DisponibilitesController extends AbstractController implements ModuleController  {

    private static DisponibilitesController instance;

    public static DisponibilitesController instance() {
        return instance;
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public DisponibilitesController() throws IhmException {
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
    protected void initialize() throws IhmException {
        // TODO FDA 2017/07 Coder.
    }

    @Override
    public void fireActivation() throws IhmException {
        // TODO FDA 2017/07 Coder.
//        LOGGER.info("Données métier chargées.");
    }


    @FXML
    private void ajouterRessource(@SuppressWarnings("unused") ActionEvent actionEvent) {
        // TODO FDA 2017/07 Coder.
        throw new NotImplementedException();
    }
}
