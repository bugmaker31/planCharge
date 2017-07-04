package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.javafx.DatePickerCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
public class ModuleJoursFeriesController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleJoursFeriesController.class);

    private static ModuleJoursFeriesController instance;

    public static ModuleJoursFeriesController instance() {
        return instance;
    }


    // Couche "métier" :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<JourFerieBean> joursFeriesBeans = planChargeBean.getJoursFeriesBeans();


    // Couche "vue" :

    @FXML
    private TableView<JourFerieBean> joursFeriesTable;

    @FXML
    private TableColumn<JourFerieBean, LocalDate> dateColumn;

    @FXML
    private TableColumn<JourFerieBean, String> descriptionColumn;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleJoursFeriesController() throws IhmException {
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
        LOGGER.debug("Initialisation...");

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        dateColumn.setCellFactory(p -> new DatePickerCell<>(PlanChargeIhm.FORMAT_DATE, joursFeriesBeans, JourFerieBean::setDate));
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Association (binding) entre la liste et la table des jours fériés :
//        joursFeriesBeans.addListener((ListChangeListener<JourFerieBean>) changeListener -> {
            // Wrap the FilteredList in a SortedList.
            final SortedList<JourFerieBean> sortedBeans = new SortedList<>(joursFeriesBeans);
            // Bind the SortedList COMPARATOR_DEFAUT to the TableView COMPARATOR_DEFAUT.
            sortedBeans.comparatorProperty().bind(joursFeriesTable.comparatorProperty());
            // Add sorted data to the table.
            joursFeriesTable.setItems(sortedBeans);
//        });

        LOGGER.debug("Initialisé.");
    }

    @FXML
    private void ajouterJourFerie(@SuppressWarnings("unused") ActionEvent actionEvent) {
        LOGGER.debug("ajouterJourFerie...");

        JourFerieBean nouvJourFerieBean = new JourFerieBean(LocalDate.now(), "A RENSEIGNER");
        joursFeriesBeans.add(nouvJourFerieBean);

        // Positionnement sur la tâche qu'on vient d'ajouter :
        int noLigNouvBean = joursFeriesBeans.indexOf(nouvJourFerieBean) + 1;
        joursFeriesTable.scrollTo(noLigNouvBean - 1);
        joursFeriesTable.getSelectionModel().select(noLigNouvBean - 1);
        // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en modé édition de la cellule.
        joursFeriesTable.edit(noLigNouvBean - 1, dateColumn);
    }
}
