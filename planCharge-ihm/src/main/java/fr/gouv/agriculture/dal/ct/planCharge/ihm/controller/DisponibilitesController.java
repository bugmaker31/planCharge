package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.view.PercentageStringConverter;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDAbsenceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDispoMinAgriBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursOuvresBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.PctagesDispoMinAgriBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.DisponibilitesService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class DisponibilitesController extends AbstractController {

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
    private final DisponibilitesService disponibilitesService = DisponibilitesService.instance();

    //    @Autowired
    @NotNull
    private final ReferentielsService referentielsService = ReferentielsService.instance();

    //    @Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

/*
    @NotNull
    private final ObservableList<PlanificationTacheBean> planificationsBeans = planChargeBean.getPlanificationsBeans();
*/

    @NotNull
    private final ObservableList<NbrsJoursOuvresBean> nbrsJoursOuvresBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<NbrsJoursDAbsenceBean> nbrsJoursAbsenceBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<NbrsJoursDispoMinAgriBean> nbrsJoursDispoMinAgriBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<PctagesDispoMinAgriBean> pctagesDispoMinAgriBeans = FXCollections.observableArrayList();

    /*
     La couche "View" :
      */

    // Les tables/colonnes :
    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableView<NbrsJoursOuvresBean> nbrsJoursOuvresTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, String> premiereColonneJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine1NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine2NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine3NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine4NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine5NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine6NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine7NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine8NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine9NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine10NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine11NbrsJoursOuvresColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursOuvresBean, Integer> semaine12NbrsJoursOuvresColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableView<NbrsJoursDAbsenceBean> nbrsJoursDAbsenceTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, String> premiereColonneAbsencesColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine1NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine2NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine3NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine4NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine5NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine6NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine7NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine8NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine9NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine10NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine11NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Integer> semaine12NbrsJoursDAbsenceColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableView<NbrsJoursDispoMinAgriBean> nbrsJoursDispoMinAgriTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, String> premiereColonneNbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine1NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine2NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine3NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine4NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine5NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine6NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine7NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine8NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine9NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine10NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine11NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Integer> semaine12NbrsJoursDispoMinAgriColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableView<PctagesDispoMinAgriBean> pctagesDispoMinAgriTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, String> premiereColonnePctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine1PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine2PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine3PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine4PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine5PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine6PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine7PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine8PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine9PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine10PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine11PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoMinAgriBean, Percentage> semaine12PctagesDispoMinAgriColumn;


    // Constructeurs :

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


    // Getters/Setters:

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine1NbrsJoursOuvresColumn() {
        return semaine1NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine2NbrsJoursOuvresColumn() {
        return semaine2NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine3NbrsJoursOuvresColumn() {
        return semaine3NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine4NbrsJoursOuvresColumn() {
        return semaine4NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine5NbrsJoursOuvresColumn() {
        return semaine5NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine6NbrsJoursOuvresColumn() {
        return semaine6NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine7NbrsJoursOuvresColumn() {
        return semaine7NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine8NbrsJoursOuvresColumn() {
        return semaine8NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine9NbrsJoursOuvresColumn() {
        return semaine9NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine10NbrsJoursOuvresColumn() {
        return semaine10NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine11NbrsJoursOuvresColumn() {
        return semaine11NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursOuvresBean, Integer> getSemaine12NbrsJoursOuvresColumn() {
        return semaine12NbrsJoursOuvresColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine1NbrsJoursDAbsenceColumn() {
        return semaine1NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine2NbrsJoursDAbsenceColumn() {
        return semaine2NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine3NbrsJoursDAbsenceColumn() {
        return semaine3NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine4NbrsJoursDAbsenceColumn() {
        return semaine4NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine5NbrsJoursDAbsenceColumn() {
        return semaine5NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine6NbrsJoursDAbsenceColumn() {
        return semaine6NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine7NbrsJoursDAbsenceColumn() {
        return semaine7NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine8NbrsJoursDAbsenceColumn() {
        return semaine8NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine9NbrsJoursDAbsenceColumn() {
        return semaine9NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine10NbrsJoursDAbsenceColumn() {
        return semaine10NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine11NbrsJoursDAbsenceColumn() {
        return semaine11NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDAbsenceBean, Integer> getSemaine12NbrsJoursDAbsenceColumn() {
        return semaine12NbrsJoursDAbsenceColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine1NbrsJoursDispoMinAgriColumn() {
        return semaine1NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine2NbrsJoursDispoMinAgriColumn() {
        return semaine2NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine3NbrsJoursDispoMinAgriColumn() {
        return semaine3NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine4NbrsJoursDispoMinAgriColumn() {
        return semaine4NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine5NbrsJoursDispoMinAgriColumn() {
        return semaine5NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine6NbrsJoursDispoMinAgriColumn() {
        return semaine6NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine7NbrsJoursDispoMinAgriColumn() {
        return semaine7NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine8NbrsJoursDispoMinAgriColumn() {
        return semaine8NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine9NbrsJoursDispoMinAgriColumn() {
        return semaine9NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine10NbrsJoursDispoMinAgriColumn() {
        return semaine10NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine11NbrsJoursDispoMinAgriColumn() {
        return semaine11NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<NbrsJoursDispoMinAgriBean, Integer> getSemaine12NbrsJoursDispoMinAgriColumn() {
        return semaine12NbrsJoursDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine1PctagesDispoMinAgriColumn() {
        return semaine1PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine2PctagesDispoMinAgriColumn() {
        return semaine2PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine3PctagesDispoMinAgriColumn() {
        return semaine3PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine4PctagesDispoMinAgriColumn() {
        return semaine4PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine5PctagesDispoMinAgriColumn() {
        return semaine5PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine6PctagesDispoMinAgriColumn() {
        return semaine6PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine7PctagesDispoMinAgriColumn() {
        return semaine7PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine8PctagesDispoMinAgriColumn() {
        return semaine8PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine9PctagesDispoMinAgriColumn() {
        return semaine9PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine10PctagesDispoMinAgriColumn() {
        return semaine10PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine11PctagesDispoMinAgriColumn() {
        return semaine11PctagesDispoMinAgriColumn;
    }

    @NotNull
    public TableColumn<PctagesDispoMinAgriBean, Percentage> getSemaine12PctagesDispoMinAgriColumn() {
        return semaine12PctagesDispoMinAgriColumn;
    }


    // Méthodes :

    @FXML
    protected void initialize() throws IhmException {
        initBeans();
        initTables();
    }

    private void initBeans() {
        initBeansJoursOuvres();
        initBeansAbsences();
        initBeansNbrsJoursDispoMinAgri();
        initBeansPctagesDispoMinAgri();
    }

    private void initBeansJoursOuvres() {
        NbrsJoursOuvresBean nbrsJoursOuvresBean = new NbrsJoursOuvresBean();
        nbrsJoursOuvresBeans.setAll(nbrsJoursOuvresBean);
        // TODO FDA 2017/08 Synchroniser avec le référentiel des jours fériés (planChargeBean.getJoursFeriesBeans())
    }

    private void initBeansAbsences() {
/*
        try {
            List<RessourceHumaineDTO> ressourceHumaineDTOS = referentielsService.ressourcesHumaines();
            nbrsJoursAbsenceBeans.setAll(ressourceHumaineDTOS.stream()
                    .map(ressourceHumaineDTO -> {
                        RessourceHumaineBean ressourceHumaineBean = RessourceHumaineBean.from(ressourceHumaineDTO);
                        NbrsJoursDAbsenceBean nbrsJoursAbsenceBean = new NbrsJoursDAbsenceBean(ressourceHumaineBean, null);
                        return nbrsJoursAbsenceBean;
                    })
                    .collect(Collectors.toList())
            );
        } catch (ServiceException e) {
            throw new IhmException("Impossible de déterminer la liste des ressources humaines.", e);
        }
*/
        planChargeBean.getRessourcesBeans().addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        Map<LocalDate, IntegerProperty> calendrier = new TreeMap<>(); // TODO FDA 2017/08 Coder.
                        nbrsJoursAbsenceBeans.add(new NbrsJoursDAbsenceBean(ressourceHumaineBean, calendrier));
                    }
                }
                // TODO FDA 2017/08 Coder les suppressions (et permutations ?).
            }
        });
    }

    private void initBeansNbrsJoursDispoMinAgri() {
        nbrsJoursAbsenceBeans.addListener((ListChangeListener<? super NbrsJoursDAbsenceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (NbrsJoursDAbsenceBean nbrsJoursDAbsenceBean : change.getAddedSubList()) {
                        RessourceHumaineBean ressourceHumaineBean = nbrsJoursDAbsenceBean.getRessourceHumaineBean();
                        Map<LocalDate, IntegerProperty> calendrier = new TreeMap<>(); // TODO FDA 2017/08 Coder.
                        nbrsJoursDispoMinAgriBeans.add(new NbrsJoursDispoMinAgriBean(ressourceHumaineBean, calendrier));
                    }
                }
                // TODO FDA 2017/08 Coder les suppressions (et permutations ?).
            }
        });
    }

    private void initBeansPctagesDispoMinAgri() {
        nbrsJoursDispoMinAgriBeans.addListener((ListChangeListener<? super NbrsJoursDispoMinAgriBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (NbrsJoursDispoMinAgriBean nbrsJoursDispoMinAgriBean : change.getAddedSubList()) {
                        RessourceHumaineBean ressourceHumaineBean = nbrsJoursDispoMinAgriBean.getRessourceHumaineBean();
                        Map<LocalDate, PercentageProperty> calendrier = new TreeMap<>(); // TODO FDA 2017/08 Coder.
                        pctagesDispoMinAgriBeans.add(new PctagesDispoMinAgriBean(ressourceHumaineBean, calendrier));
                    }
                }
                // TODO FDA 2017/08 Coder les suppressions (et permutations ?).
            }
        });
    }

    private void initTables() throws IhmException {
        initTableJoursOuvres();
        initTableAbsences();
        initTableNbrsJoursDispoMinAgri();
        initTablePctagesDispoMinAgri();
        synchroniserLargeurPremieresColonnes();
    }

    private void initTableJoursOuvres() {

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonneJoursOuvresColumn.setCellValueFactory((CellDataFeatures<NbrsJoursOuvresBean, String> cell) -> new SimpleStringProperty("N/A"));
        //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
        final class NbrJoursOuvresCellCallback implements Callback<CellDataFeatures<NbrsJoursOuvresBean, Integer>, ObservableValue<Integer>> {
            private final int noSemaine;

            private NbrJoursOuvresCellCallback(int noSemaine) {
                super();
                this.noSemaine = noSemaine;
            }

            @Null
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<NbrsJoursOuvresBean, Integer> cell) {
                if (cell == null) {
                    return null;
                }
                NbrsJoursOuvresBean nbrsJoursOuvresBean = cell.getValue();
                LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                IntegerProperty nbrJoursOuvresPeriode = nbrsJoursOuvresBean.get(debutPeriode);
                return nbrJoursOuvresPeriode.asObject();
            }
        }
        semaine1NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(1));
        semaine2NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(2));
        semaine3NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(3));
        semaine4NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(4));
        semaine5NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(5));
        semaine6NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(6));
        semaine7NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(7));
        semaine8NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(8));
        semaine9NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(9));
        semaine10NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(10));
        semaine11NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(11));
        semaine12NbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(12));

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        //Pas sur cet écran (non modifiable, calculé à partir du référentiel des jours fériés.).

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (1 seule ligne).

        nbrsJoursOuvresTable.setItems(nbrsJoursOuvresBeans);

        TableViews.disableColumnReorderable(nbrsJoursOuvresTable);
        TableViews.adjustHeightToRowCount(nbrsJoursOuvresTable);
    }

    private void initTableAbsences() throws IhmException {

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonneAbsencesColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
        final class NbrJoursDAbsenceCellCallback implements Callback<CellDataFeatures<NbrsJoursDAbsenceBean, Integer>, ObservableValue<Integer>> {
            private final int noSemaine;

            private NbrJoursDAbsenceCellCallback(int noSemaine) {
                super();
                this.noSemaine = noSemaine;
            }

            @Null
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<NbrsJoursDAbsenceBean, Integer> cell) {
                return new SimpleIntegerProperty(noSemaine).asObject();
//                TODO FDA 2017/08 Coder.
/*
                if (cell == null) {
                    return null;
                }
                NbrsJoursDAbsenceBean nbrsJoursDAbsenceBean = cell.getValue();
                LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                IntegerProperty nbrJoursDAbsencePeriode = nbrsJoursDAbsenceBean.get(debutPeriode);
                return nbrJoursDAbsencePeriode.asObject();
*/
            }
        }
        semaine1NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(1));
        semaine2NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(2));
        semaine3NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(3));
        semaine4NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(4));
        semaine5NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(5));
        semaine6NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(6));
        semaine7NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(7));
        semaine8NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(8));
        semaine9NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(9));
        semaine10NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(10));
        semaine11NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(11));
        semaine12NbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(12));

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        semaine1NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine2NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine3NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine4NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine5NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine6NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine7NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine8NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine9NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine10NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine11NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine12NbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        nbrsJoursDAbsenceTable.setItems(nbrsJoursAbsenceBeans);

        TableViews.disableColumnReorderable(nbrsJoursDAbsenceTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDAbsenceTable);
    }

    private void initTableNbrsJoursDispoMinAgri() {

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonneNbrsJoursDispoMinAgriColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
        final class NbrJoursDispoMinAgriCellCallback implements Callback<CellDataFeatures<NbrsJoursDispoMinAgriBean, Integer>, ObservableValue<Integer>> {
            private final int noSemaine;

            private NbrJoursDispoMinAgriCellCallback(int noSemaine) {
                super();
                this.noSemaine = noSemaine;
            }

            @Null
            @Override
            public ObservableValue<Integer> call(CellDataFeatures<NbrsJoursDispoMinAgriBean, Integer> cell) {
                return new SimpleIntegerProperty(noSemaine).asObject();
//                TODO FDA 2017/08 Coder.
/*
                if (cell == null) {
                    return null;
                }
                NbrsJoursDAbsenceBean nbrsJoursDAbsenceBean = cell.getValue();
                LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                IntegerProperty nbrJoursDAbsencePeriode = nbrsJoursDAbsenceBean.get(debutPeriode);
                return nbrJoursDAbsencePeriode.asObject();
*/
            }
        }
        semaine1NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(1));
        semaine2NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(2));
        semaine3NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(3));
        semaine4NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(4));
        semaine5NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(5));
        semaine6NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(6));
        semaine7NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(7));
        semaine8NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(8));
        semaine9NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(9));
        semaine10NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(10));
        semaine11NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(11));
        semaine12NbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(12));

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        semaine1NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine2NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine3NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine4NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine5NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine6NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine7NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine8NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine9NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine10NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine11NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        semaine12NbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        nbrsJoursDispoMinAgriTable.setItems(nbrsJoursDispoMinAgriBeans);

        TableViews.disableColumnReorderable(nbrsJoursDispoMinAgriTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDispoMinAgriTable);
    }

    private void initTablePctagesDispoMinAgri() {

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonnePctagesDispoMinAgriColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
        final class PctagesDispoMinAgriCellCallback implements Callback<CellDataFeatures<PctagesDispoMinAgriBean, Percentage>, ObservableValue<Percentage>> {
            private final int noSemaine;

            private PctagesDispoMinAgriCellCallback(int noSemaine) {
                super();
                this.noSemaine = noSemaine;
            }

            @Null
            @Override
            public ObservableValue<Percentage> call(CellDataFeatures<PctagesDispoMinAgriBean, Percentage> cell) {
                return new PercentageProperty(new Double(noSemaine/12.0).floatValue());
//                TODO FDA 2017/08 Coder.
/*
                if (cell == null) {
                    return null;
                }
                NbrsJoursDAbsenceBean nbrsJoursDAbsenceBean = cell.getValue();
                LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                IntegerProperty nbrJoursDAbsencePeriode = nbrsJoursDAbsenceBean.get(debutPeriode);
                return nbrJoursDAbsencePeriode.asObject();
*/
            }
        }
        semaine1PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(1));
        semaine2PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(2));
        semaine3PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(3));
        semaine4PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(4));
        semaine5PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(5));
        semaine6PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(6));
        semaine7PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(7));
        semaine8PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(8));
        semaine9PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(9));
        semaine10PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(10));
        semaine11PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(11));
        semaine12PctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(12));

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        semaine1PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine2PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine3PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine4PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine5PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine6PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine7PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine8PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine9PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine10PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine11PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        semaine12PctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        pctagesDispoMinAgriTable.setItems(pctagesDispoMinAgriBeans);

        TableViews.disableColumnReorderable(pctagesDispoMinAgriTable);
        TableViews.adjustHeightToRowCount(pctagesDispoMinAgriTable);
    }

    private void synchroniserLargeurPremieresColonnes() {
        TableViews.synchronizeColumnsWidth(nbrsJoursOuvresTable, nbrsJoursDAbsenceTable, nbrsJoursDispoMinAgriTable, pctagesDispoMinAgriTable);
    }


    public void definirValeursCalendrier() {

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de définir les valeurs du calendrier",
                    "Date d'état non définie."
            );
            return;
        }

        try {
            definirValeursCalendrier(dateEtat);
        } catch (IhmException e) {
            LOGGER.error("Impossible de définir les valeurs du calendrier.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de définir les valeurs du calendrier",
                    Exceptions.causes(e)
            );
        }
    }

    private void definirValeursCalendrier(@NotNull LocalDate dateEtat) throws IhmException {
        LOGGER.debug("Définition des valeurs du calendrier : ");
        try {
            NbrsJoursOuvresBean nbrsJoursOuvresBean = nbrsJoursOuvresBeans.get(0);
            assert nbrsJoursOuvresBean != null;
            nbrsJoursOuvresBean.clear();
            for (int noSemaine = 1; noSemaine <= PlanificationsDTO.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
                LocalDate debutPeriode = dateEtat.plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/08 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                LocalDate finPeriode = debutPeriode.plusDays(7); // FIXME FDA 2017/08 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                int nbrJoursOuvresPeriode = disponibilitesService.nbrJoursOuvres(debutPeriode, finPeriode);
                nbrsJoursOuvresBean.put(debutPeriode, new SimpleIntegerProperty(nbrJoursOuvresPeriode));
            }
            nbrsJoursOuvresTable.refresh();
            nbrsJoursDAbsenceTable.refresh();
            LOGGER.debug("Valeurs du calendrier définies.");
        } catch (ServiceException e) {
            throw new IhmException("Impossible de définir les valeurs du calendrier.", e);
        }
    }
}
