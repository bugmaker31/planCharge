package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AjoutTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class TachesController extends AbstractTachesController<TacheBean> implements Module {

    public static final PseudoClass ECHUE = PseudoClass.getPseudoClass("echue");


    private static final Logger LOGGER = LoggerFactory.getLogger(TachesController.class);


    private static TachesController instance;

    public static TachesController instance() {
        return instance;
    }


    /*
     La couche "View" :
      */

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private TableView<TacheBean> tachesTable;

    /*
     La couche métier :
      */
    @SuppressWarnings("NullableProblems")
    @NotNull
    // 'final' pour empêcher de resetter cette variable.
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // TODO FDA 2017/05 Résoudre le warning de compilation (unchecked assignement).
    @SuppressWarnings({"unchecked", "rawtypes"})
    @NotNull
    // 'final' pour empêcher de resetter cette ObsevableList, ce qui enleverait les Listeners.
    private final ObservableList<TacheBean> planificationsBeans = (ObservableList) planChargeBean.getPlanificationsBeans();


    // Constructeurs :

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


    // Getters/Setters :

    @SuppressWarnings("SuspiciousGetterSetter")
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


    // Méthodes :


    // Module

    @Override
    public String getTitre() {
        return "Tâches";
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws ControllerException {
        try {
            Calculateur.executerSansCalculer(() -> {

                super.initialize(); // TODO FDA 2017/05 Très redondant (le + gros est déjà initialisé par le ModuleTacheController) => améliorer le code.

                tachesTable.setOnKeyReleased(event -> {
                    if (event.getCode() == KeyCode.DELETE) {
                        if (!tachesTable.isFocused()) {
                            return;
                        }
                        supprimerTacheSelectionnee();
                        event.consume(); // TODO FDA 2017/08 Confirmer.
                        //noinspection UnnecessaryReturnStatement
                        return;
                    }
                });


            });
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur.", e);
        }
    }

/* Le menu contextuel est défini dans le fichier FXML.
    void definirMenuContextuel() {
        super.definirMenuContextuel();

        if (menuVoirTacheDansPlanCharge == null) {
            menuVoirTacheDansPlanCharge = new MenuItem("Voir la tâche dans le plan de charge");
            menuVoirTacheDansPlanCharge.setOnAction(event -> afficherPlanification());
        }
        if (!tachesTableContextMenu.getItems().contains(menuVoirTacheDansPlanCharge)) {
            tachesTableContextMenu.getItems().add(menuVoirTacheDansPlanCharge);
        }
    }
*/

    @NotNull
    protected TacheBean ajouterTache() throws ControllerException {
        LOGGER.debug("ajouterTache...");
        try {

            TacheBean tacheBean = super.ajouterTache();

            planChargeBean.vientDEtreModifie();
            getSuiviActionsUtilisateur().historiser(new AjoutTache<>(tacheBean, planificationsBeans));

            ihm.getApplicationController().majBarreEtat();
            return tacheBean;
        } catch (SuiviActionsUtilisateurException e) {
            throw new ControllerException("Impossible d'ajouter une tâche.", e);
        }
    }

    @NotNull
    @Override
    TacheBean nouveauBean() throws ControllerException {
        return ihm.getChargesController().nouveauBean();
    }

    @FXML
    private void afficherPlanification(@SuppressWarnings("unused") ActionEvent actionEvent) {
        afficherPlanification();
    }

    private void afficherPlanification() {
        TacheBean tacheBean = TableViews.selectedItem(tachesTable);
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator,MagicNumber
            ihm.afficherDialog(
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
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification pour la tâche",
                    Exceptions.causes(e)
            );
        }
    }

    @Override
    protected boolean estTacheAvecAutreFiltreAVoir(@NotNull TacheBean tache) {
        return false;
    }

}
