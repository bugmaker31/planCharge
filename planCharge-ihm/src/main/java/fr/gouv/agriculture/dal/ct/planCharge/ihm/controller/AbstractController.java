package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import javafx.fxml.Initializable;
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
    private SuiviActionsUtilisateur suiviActionsUtilisateur = SuiviActionsUtilisateur.instance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initialize();
        } catch (IhmException e) {
            // TODO FDA 2017/06 Trouver mieux, comme gestion d'erreur.
            LOGGER.error("Impossible d'initialiser le contrôleur.", e);
            throw new RuntimeException("Impossible d'initialiser le contrôleur.", e);
        }
    }

    abstract void initialize() throws IhmException;


    // Suivi des actions de l'utilisateur :

    SuiviActionsUtilisateur getSuiviActionsUtilisateur() {
        return suiviActionsUtilisateur;
    }
}
