package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class ModuleTachesController extends AbstractTachesController<TacheBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleTachesController.class);

    private static ModuleTachesController instance;

    public static ModuleTachesController instance() {
        return instance;
    }

    /*
     La couche "View" :
      */

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    @NotNull
    @FXML
    private TableView<TacheBean> tachesTable;

    /*
     La couche métier :
      */
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // TODO FDA 2017/05 Résoudre le warning de compilation (unchecked assignement).
    @SuppressWarnings("unchecked")
    @NotNull
    private ObservableList<TacheBean> planificationsBeans = (ObservableList) planChargeBean.getPlanificationsBeans();


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleTachesController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    @NotNull
    @Override
    ObservableList<TacheBean> getTachesBeans() {
        return planificationsBeans;
    }

    @NotNull
    @Override
    TableView<TacheBean> getTachesTable() {
        return tachesTable;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");

        super.initialize();

        // Rien de plus... pour l'instant.

        LOGGER.debug("Initialisé.");
    }

    // Surchargée juste pour pouvoir ajouter le @FXML.
    @FXML
    @Override
    @NotNull
    protected TacheBean ajouterTache(ActionEvent event) throws Exception {
        LOGGER.debug("ajouterTache...");
        try {
            TacheBean tacheBean = super.ajouterTache(event);

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new AjoutTache<>(tacheBean, getTachesBeans()));

            ihm.getApplicationController().majBarreEtat();
            return tacheBean;
        } catch (IhmException e) {
            throw new Exception("Impossible d'ajouter une tâche.", e);
        }
    }

    @Override
    TacheBean nouveauBean() throws IhmException {
        return ihm.getChargesController().nouveauBean();
    }

    @FXML
    public void afficherPlanification(@SuppressWarnings("unused") ActionEvent actionEvent) {
        TacheBean tacheBean = tacheSelectionnee();
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification pour la tâche",
                    "Aucune tâche n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une tâche (ligne), puis re-cliquez.",
                    400, 200
            );
            return;
        }
        try {
            ihm.getApplicationController().afficherModuleCharges();
            ihm.getTachesController().mettreFocusSurTache(tacheBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher la planification pour la tâche", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification pour la tâche",
                    Exceptions.causes(e)
            );
        }

    }
}
