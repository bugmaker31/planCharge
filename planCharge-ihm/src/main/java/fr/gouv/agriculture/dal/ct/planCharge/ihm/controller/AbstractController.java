package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateur;
import javafx.fxml.Initializable;
import javafx.scene.layout.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by frederic.danna on 23/04/2017.
 */
public abstract class AbstractController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);


    //    @Autowired
    @NotNull
    protected final PlanChargeIhm ihm = PlanChargeIhm.instance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            LOGGER.debug("Contrôleur '{}' en cours d'initialisation...", getClass().getSimpleName());
            initialize();
            LOGGER.info("Contrôleur '{}' initialisé.", getClass().getSimpleName());
        } catch (ControllerException e) {
            // TODO FDA 2017/06 Trouver mieux, comme gestion d'erreur.
            LOGGER.error("Impossible d'initialiser le contrôleur '" + getClass().getSimpleName() + "'.", e);
            throw new RuntimeException("Impossible d'initialiser le contrôleur '" + getClass().getSimpleName() + "'.", e);
        }
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    protected abstract void initialize() throws ControllerException;


    // Suivi des actions de l'utilisateur :

    //    @Autowired
    @NotNull
    private SuiviActionsUtilisateur suiviActionsUtilisateur = SuiviActionsUtilisateur.instance();

    @NotNull
    public SuiviActionsUtilisateur getSuiviActionsUtilisateur() {
        return suiviActionsUtilisateur;
    }
}
