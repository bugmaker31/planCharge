package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.NotImplementedException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

/**
 * Created by frederic.danna on 26/03/2017.
 * @author frederic.danna
 */
public class DisponibilitesController extends AbstractController  {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisponibilitesController.class);

    private static DisponibilitesController instance;

    public static DisponibilitesController instance() {
        return instance;
    }


    /*
     La couche métier :
      */

    //    @Autowired
    @NotNull
    private PlanChargeService planChargeService = PlanChargeService.instance();

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<PlanificationBean> planificationsBeans = planChargeBean.getPlanificationsBeans();


    /*
     La couche "View" :
      */

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableView disponibilitesTable;

    // Les colonnes spécifiques du calendrier des tâches :
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine1Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine2Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine3Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine4Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine5Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine6Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine7Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine8Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine9Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine10Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine11Column;
    @FXML
    @NotNull
    private TableColumn<PlanificationBean, Double> semaine12Column;


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


    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine1Column() {
        return semaine1Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine2Column() {
        return semaine2Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine3Column() {
        return semaine3Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine4Column() {
        return semaine4Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine5Column() {
        return semaine5Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine6Column() {
        return semaine6Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine7Column() {
        return semaine7Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine8Column() {
        return semaine8Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine9Column() {
        return semaine9Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine10Column() {
        return semaine10Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine11Column() {
        return semaine11Column;
    }

    @NotNull
    public TableColumn<PlanificationBean, Double> getSemaine12Column() {
        return semaine12Column;
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws IhmException {
        // TODO FDA 2017/07 Coder.
    }


    public void afficherPlanification() {

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification",
                    "Date d'état non définie."
            );
            return;
        }
        assert dateEtat != null;

        try {
            afficherPlanification(dateEtat);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher la planification.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification",
                    Exceptions.causes(e)
            );
        }
    }

    private void afficherPlanification(@NotNull LocalDate dateEtat) throws IhmException {
        LOGGER.debug("Affichage de la planification : ");
        PlanificationsDTO planificationsInitiales = planChargeBean.toPlanificationDTOs();
        PlanificationsDTO planifications = planChargeService.replanifier(planificationsInitiales, dateEtat);
        planifications
                .forEach((tache, planifTache) -> {
                    Optional<PlanificationBean> planifBeanOpt = planChargeBean.getPlanificationsBeans().parallelStream()
                            .filter(planificationBean -> planificationBean.getId() == tache.getId())
                            .findAny();
                    assert planifBeanOpt.isPresent();
                    PlanificationBean planifBean = planifBeanOpt.get();
                    Map<LocalDate, DoubleProperty> calendrierTache = planifBean.getCalendrier();
                    calendrierTache.clear();
                    planifTache.forEach((debutPeriode, charge) -> calendrierTache.put(debutPeriode, new SimpleDoubleProperty(charge)));
                });
        disponibilitesTable.refresh();
        LOGGER.debug("Planification affichée.");
    }
}
