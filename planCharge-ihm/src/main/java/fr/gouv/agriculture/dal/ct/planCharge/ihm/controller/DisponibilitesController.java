package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDAbsenceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursOuvresBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.DisponibilitesService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
    private TableColumn<NbrsJoursOuvresBean, String> premiereColonneAbsencesColumn;
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

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws IhmException {
        initTableJoursOuvres();
        initTableAbsences();
        synchroniserLargeurPremieresColonnes();
    }

    private void initTableJoursOuvres() {

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
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

        NbrsJoursOuvresBean nbrsJoursOuvresBean = new NbrsJoursOuvresBean();
        nbrsJoursOuvresBeans.setAll(nbrsJoursOuvresBean);
        nbrsJoursOuvresTable.setItems(nbrsJoursOuvresBeans);
    }

    private void initTableAbsences() throws IhmException {
        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        semaine1NbrsJoursDAbsenceColumn.setCellValueFactory(param -> {
            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((1 - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
            return param.getValue().get(debutPeriode).asObject();
        });
        semaine2NbrsJoursDAbsenceColumn.setCellValueFactory(param -> {
            LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((2 - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
            return param.getValue().get(debutPeriode).asObject();
        });

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        // TODO FDA 2017/08 Coder.

        // Paramétrage des ordres de tri :
        // TODO FDA 2017/08 Coder.

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

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
            nbrsJoursAbsenceBeans.setAll();
        });

        nbrsJoursDAbsenceTable.setItems(nbrsJoursAbsenceBeans);
    }

    private void synchroniserLargeurPremieresColonnes() {
        TableViews.synchronizeColumnsWidth(nbrsJoursOuvresTable, nbrsJoursDAbsenceTable);
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
