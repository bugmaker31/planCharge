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
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.TableViewAvecCalendrier;
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
import javafx.collections.transformation.SortedList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by frederic.danna on 26/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithTooManyFields"})
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
    private TableViewAvecCalendrier<NbrsJoursOuvresBean, Integer> nbrsJoursOuvresTable;
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
    private TableViewAvecCalendrier<NbrsJoursDAbsenceBean, Integer> nbrsJoursDAbsenceTable;
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
    private TableViewAvecCalendrier<NbrsJoursDispoMinAgriBean, Integer> nbrsJoursDispoMinAgriTable;
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
    private TableViewAvecCalendrier<PctagesDispoMinAgriBean, Percentage> pctagesDispoMinAgriTable;
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
    public TableViewAvecCalendrier<NbrsJoursOuvresBean, Integer> getNbrsJoursOuvresTable() {
        return nbrsJoursOuvresTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursDAbsenceBean, Integer> getNbrsJoursDAbsenceTable() {
        return nbrsJoursDAbsenceTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursDispoMinAgriBean, Integer> getNbrsJoursDispoMinAgriTable() {
        return nbrsJoursDispoMinAgriTable;
    }

    @NotNull
    public TableViewAvecCalendrier<PctagesDispoMinAgriBean, Percentage> getPctagesDispoMinAgriTable() {
        return pctagesDispoMinAgriTable;
    }


    // Méthodes :

    @FXML
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");
        initBeans();
        initTables();
        LOGGER.info("Initialisé.");
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

        planChargeBean.getJoursFeriesBeans().addListener((ListChangeListener<? super JourFerieBean>) change -> {
            definirValeursCalendrier();
        });
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
                    List<NbrsJoursDAbsenceBean> nbrsJoursAbsenceBeansAAjouter = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        Map<LocalDate, IntegerProperty> calendrier = new TreeMap<>(); // TODO FDA 2017/08 Coder.
                        nbrsJoursAbsenceBeansAAjouter.add(new NbrsJoursDAbsenceBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursAbsenceBeans.addAll(nbrsJoursAbsenceBeansAAjouter);
                }
                // TODO FDA 2017/08 Coder les suppressions (et permutations ?).
            }
        });
    }

    private void initBeansNbrsJoursDispoMinAgri() {
        nbrsJoursAbsenceBeans.addListener((ListChangeListener<? super NbrsJoursDAbsenceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoMinAgriBean> nbrsJoursDispoMinAgriBeansAAjouter = new ArrayList<>();
                    for (NbrsJoursDAbsenceBean nbrsJoursDAbsenceBean : change.getAddedSubList()) {
                        RessourceHumaineBean ressourceHumaineBean = nbrsJoursDAbsenceBean.getRessourceHumaineBean();
                        Map<LocalDate, IntegerProperty> calendrier = new TreeMap<>(); // TODO FDA 2017/08 Coder.
                        nbrsJoursDispoMinAgriBeansAAjouter.add(new NbrsJoursDispoMinAgriBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDispoMinAgriBeans.addAll(nbrsJoursDispoMinAgriBeansAAjouter);
                }
                // TODO FDA 2017/08 Coder les suppressions (et permutations ?).
            }
        });
    }

    private void initBeansPctagesDispoMinAgri() {
        nbrsJoursDispoMinAgriBeans.addListener((ListChangeListener<? super NbrsJoursDispoMinAgriBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<PctagesDispoMinAgriBean> pctagesDispoMinAgriBeansAAjouter = new ArrayList<>();
                    for (NbrsJoursDispoMinAgriBean nbrsJoursDispoMinAgriBean : change.getAddedSubList()) {
                        RessourceHumaineBean ressourceHumaineBean = nbrsJoursDispoMinAgriBean.getRessourceHumaineBean();
                        Map<LocalDate, PercentageProperty> calendrier = new TreeMap<>(); // TODO FDA 2017/08 Coder.
                        pctagesDispoMinAgriBeansAAjouter.add(new PctagesDispoMinAgriBean(ressourceHumaineBean, calendrier));
                    }
                    pctagesDispoMinAgriBeans.addAll(pctagesDispoMinAgriBeansAAjouter);
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

        nbrsJoursOuvresTable.setCalendrierColumns(
                semaine1NbrsJoursOuvresColumn,
                semaine2NbrsJoursOuvresColumn,
                semaine3NbrsJoursOuvresColumn,
                semaine4NbrsJoursOuvresColumn,
                semaine5NbrsJoursOuvresColumn,
                semaine6NbrsJoursOuvresColumn,
                semaine7NbrsJoursOuvresColumn,
                semaine8NbrsJoursOuvresColumn,
                semaine9NbrsJoursOuvresColumn,
                semaine10NbrsJoursOuvresColumn,
                semaine11NbrsJoursOuvresColumn,
                semaine12NbrsJoursOuvresColumn
        );

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
        int cptColonne = 0;
        for (TableColumn<NbrsJoursOuvresBean, Integer> nbrsJoursOuvresColumn : nbrsJoursOuvresTable.getCalendrierColumns()) {
            cptColonne++;
            nbrsJoursOuvresColumn.setCellValueFactory(new NbrJoursOuvresCellCallback(cptColonne));
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        //Pas sur cet écran (non modifiable, calculé à partir du référentiel des jours fériés.).

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (1 seule ligne).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (1 seule ligne).

        TableViews.disableColumnReorderable(nbrsJoursOuvresTable);
        TableViews.adjustHeightToRowCount(nbrsJoursOuvresTable);

        SortedList<NbrsJoursOuvresBean> sortedBeans = new SortedList<>(nbrsJoursOuvresBeans);
        sortedBeans.comparatorProperty().bind(nbrsJoursOuvresTable.comparatorProperty());

        nbrsJoursOuvresTable.setItems(sortedBeans);
    }

    private void initTableAbsences() throws IhmException {

        nbrsJoursDAbsenceTable.setCalendrierColumns(
                semaine1NbrsJoursDAbsenceColumn,
                semaine2NbrsJoursDAbsenceColumn,
                semaine3NbrsJoursDAbsenceColumn,
                semaine4NbrsJoursDAbsenceColumn,
                semaine5NbrsJoursDAbsenceColumn,
                semaine6NbrsJoursDAbsenceColumn,
                semaine7NbrsJoursDAbsenceColumn,
                semaine8NbrsJoursDAbsenceColumn,
                semaine9NbrsJoursDAbsenceColumn,
                semaine10NbrsJoursDAbsenceColumn,
                semaine11NbrsJoursDAbsenceColumn,
                semaine12NbrsJoursDAbsenceColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonneAbsencesColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        {
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
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDAbsenceBean, Integer> nbrsJoursDAbsenceColumn : nbrsJoursDAbsenceTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        for (TableColumn<NbrsJoursDAbsenceBean, Integer> nbrsJoursDAbsenceColumn : nbrsJoursDAbsenceTable.getCalendrierColumns()) {
            nbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableColumnReorderable(nbrsJoursDAbsenceTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDAbsenceTable);

        SortedList<NbrsJoursDAbsenceBean> sortedBeans = new SortedList<>(nbrsJoursAbsenceBeans);
        sortedBeans.comparatorProperty().bind(nbrsJoursDAbsenceTable.comparatorProperty());

//        nbrsJoursDAbsenceTable.setItems(nbrsJoursAbsenceBeans);
        nbrsJoursAbsenceBeans.addListener((ListChangeListener<? super NbrsJoursDAbsenceBean>) change -> {
            nbrsJoursDAbsenceTable.setItems(sortedBeans);
        });
    }

    private void initTableNbrsJoursDispoMinAgri() {

        nbrsJoursDispoMinAgriTable.setCalendrierColumns(
                semaine1NbrsJoursDispoMinAgriColumn,
                semaine2NbrsJoursDispoMinAgriColumn,
                semaine3NbrsJoursDispoMinAgriColumn,
                semaine4NbrsJoursDispoMinAgriColumn,
                semaine5NbrsJoursDispoMinAgriColumn,
                semaine6NbrsJoursDispoMinAgriColumn,
                semaine7NbrsJoursDispoMinAgriColumn,
                semaine8NbrsJoursDispoMinAgriColumn,
                semaine9NbrsJoursDispoMinAgriColumn,
                semaine10NbrsJoursDispoMinAgriColumn,
                semaine11NbrsJoursDispoMinAgriColumn,
                semaine12NbrsJoursDispoMinAgriColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonneNbrsJoursDispoMinAgriColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        {
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
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoMinAgriBean, Integer> nbrsJoursDispoMinAgriColumn : nbrsJoursDispoMinAgriTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        for (TableColumn<NbrsJoursDispoMinAgriBean, Integer> nbrsJoursDispoMinAgriColumn : nbrsJoursDispoMinAgriTable.getCalendrierColumns()) {
            nbrsJoursDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableColumnReorderable(nbrsJoursDispoMinAgriTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDispoMinAgriTable);

        SortedList<NbrsJoursDispoMinAgriBean> sortedBeans = new SortedList<>(nbrsJoursDispoMinAgriBeans);
        sortedBeans.comparatorProperty().bind(nbrsJoursDispoMinAgriTable.comparatorProperty());

        nbrsJoursDispoMinAgriTable.setItems(sortedBeans);
    }

    private void initTablePctagesDispoMinAgri() {

        pctagesDispoMinAgriTable.setCalendrierColumns(
                semaine1PctagesDispoMinAgriColumn,
                semaine2PctagesDispoMinAgriColumn,
                semaine3PctagesDispoMinAgriColumn,
                semaine4PctagesDispoMinAgriColumn,
                semaine5PctagesDispoMinAgriColumn,
                semaine6PctagesDispoMinAgriColumn,
                semaine7PctagesDispoMinAgriColumn,
                semaine8PctagesDispoMinAgriColumn,
                semaine9PctagesDispoMinAgriColumn,
                semaine10PctagesDispoMinAgriColumn,
                semaine11PctagesDispoMinAgriColumn,
                semaine12PctagesDispoMinAgriColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonnePctagesDispoMinAgriColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        {
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
                    return new PercentageProperty(new Double(noSemaine / 12.0).floatValue());
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
            int cptColonne = 0;
            for (TableColumn<PctagesDispoMinAgriBean, Percentage> pctagesDispoMinAgriColumn : pctagesDispoMinAgriTable.getCalendrierColumns()) {
                cptColonne++;
                pctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        for (TableColumn<PctagesDispoMinAgriBean, Percentage> pctagesDispoMinAgriColumn : pctagesDispoMinAgriTable.getCalendrierColumns()) {
            pctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableColumnReorderable(pctagesDispoMinAgriTable);
        TableViews.adjustHeightToRowCount(pctagesDispoMinAgriTable);

        SortedList<PctagesDispoMinAgriBean> sortedBeans = new SortedList<>(pctagesDispoMinAgriBeans);
        sortedBeans.comparatorProperty().bind(pctagesDispoMinAgriTable.comparatorProperty());

        pctagesDispoMinAgriTable.setItems(sortedBeans);
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
