package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
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
public class TachesController extends AbstractTachesController<TacheBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TachesController.class);

    private static TachesController instance;

    public static TachesController instance() {
        return instance;
    }

    /*
     La couche "View" :
      */

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
    public TachesController() throws IhmException {
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
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");

        super.initialize();

        // Rien de plus... pour l'instant.

        LOGGER.info("Initialisé.");
    }

    @Override
    public void definirMenuContextuel() {
        MenuItem menuVoirTacheDansPlanCharge = new MenuItem("Voir dans le plan de charge");
        menuVoirTacheDansPlanCharge.setOnAction(event -> afficherPlanification());

        MenuItem menuVoirOutilTicketing = new MenuItem("Voir dans l'outil de ticketing");
        menuVoirOutilTicketing.setOnAction(event -> afficherTacheDansOutilTicketing());

        tachesTableContextMenu.getItems().setAll(menuVoirTacheDansPlanCharge, menuVoirOutilTicketing);
    }

    // Surchargée juste pour pouvoir ajouter le @FXML.
    @FXML
    @NotNull
    protected TacheBean ajouterTache(@SuppressWarnings("unused") ActionEvent event) throws Exception {
        LOGGER.debug("ajouterTache...");
        try {
            TacheBean tacheBean = super.ajouterTache();

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
    private void afficherPlanification(@SuppressWarnings("unused") ActionEvent actionEvent) {
        afficherPlanification();
    }

    private void afficherPlanification() {
        TacheBean tacheBean = TableViews.selectedItem(tachesTable);
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
            TableViews.focusOnItem(ihm.getChargesController().getTachesTable(), tacheBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher la planification pour la tâche " + tacheBean.getId() + ".", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification pour la tâche",
                    Exceptions.causes(e)
            );
        }
    }

}
