package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrJourOuvreBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.DisponibilitesService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

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
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private final ObservableList<PlanificationTacheBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    @NotNull
    private final ObservableList<NbrJourOuvreBean> nbrsJoursOuvresBeans = FXCollections.observableArrayList();


    /*
     La couche "View" :
      */

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableView<NbrJourOuvreBean> nbrsJoursOuvresTable;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableView<Integer> nbrsJoursAbsenceTable;

    // Les colonnes spécifiques du calendrier des tâches :
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine1Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine2Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine3Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine4Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine5Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine6Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine7Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine8Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine9Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine10Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine11Column;
    @FXML
    @NotNull
    private TableColumn<NbrJourOuvreBean, Integer> semaine12Column;


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
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine1Column() {
        return semaine1Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine2Column() {
        return semaine2Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine3Column() {
        return semaine3Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine4Column() {
        return semaine4Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine5Column() {
        return semaine5Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine6Column() {
        return semaine6Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine7Column() {
        return semaine7Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine8Column() {
        return semaine8Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine9Column() {
        return semaine9Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine10Column() {
        return semaine10Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine11Column() {
        return semaine11Column;
    }

    @NotNull
    public TableColumn<NbrJourOuvreBean, Integer> getSemaine12Column() {
        return semaine12Column;
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws IhmException {

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        class NbrJourOuvreCellCallback implements Callback<CellDataFeatures<NbrJourOuvreBean, Integer>, ObservableValue<Integer>> {
            private final int noSemaine;

            public NbrJourOuvreCellCallback(int noSemaine) {
                this.noSemaine = noSemaine;
            }

            @Null
            @Override
            public ObservableValue<Integer> call(@NotNull CellDataFeatures<NbrJourOuvreBean, Integer> cell) {
                NbrJourOuvreBean planifBean = cell.getValue();
                LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                try {
                    int nbrJoursOuvres = disponibilitesService.nbrJoursOuvres(debutPeriode);
                    return new SimpleIntegerProperty(nbrJoursOuvres).asObject();
                } catch (ServiceException e) {
                    LOGGER.error("Impossible de gérer l'édition de la cellule conternant le nombre de jours ouvrés de la semaine n°" + noSemaine + ".", e);
                    return null;
                }
            }
        }
        semaine1Column.setCellValueFactory(new NbrJourOuvreCellCallback(1));
        semaine2Column.setCellValueFactory(new NbrJourOuvreCellCallback(2));
        semaine3Column.setCellValueFactory(new NbrJourOuvreCellCallback(3));
        semaine4Column.setCellValueFactory(new NbrJourOuvreCellCallback(4));
        semaine5Column.setCellValueFactory(new NbrJourOuvreCellCallback(5));
        semaine6Column.setCellValueFactory(new NbrJourOuvreCellCallback(6));
        semaine7Column.setCellValueFactory(new NbrJourOuvreCellCallback(7));
        semaine8Column.setCellValueFactory(new NbrJourOuvreCellCallback(8));
        semaine9Column.setCellValueFactory(new NbrJourOuvreCellCallback(9));
        semaine10Column.setCellValueFactory(new NbrJourOuvreCellCallback(10));
        semaine11Column.setCellValueFactory(new NbrJourOuvreCellCallback(11));
        semaine12Column.setCellValueFactory(new NbrJourOuvreCellCallback(12));

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        //Pas sur cet écran (non modifiable, calculé à partir du référentiel des jours fériés.).

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (1 seule ligne).

        nbrsJoursOuvresBeans.setAll(new NbrJourOuvreBean());

        nbrsJoursOuvresTable.setItems(nbrsJoursOuvresBeans);
    }


    public void afficherCalendrier() {

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le calendrier",
                    "Date d'état non définie."
            );
            return;
        }
        assert dateEtat != null;

        try {
            afficherCalendrier(dateEtat);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le calendrier.", e);
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le calendrier",
                    Exceptions.causes(e)
            );
        }
    }

    private void afficherCalendrier(@NotNull LocalDate dateEtat) throws IhmException {
        LOGGER.debug("Affichage du calendrier : ");
        // TODO FDA 2017/08 Recalculer les nouveaux nombres.
        nbrsJoursAbsenceTable.refresh();
        nbrsJoursAbsenceTable.refresh();
        LOGGER.debug("Calendrier affiché.");
    }
}
