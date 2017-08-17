package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.view.PercentageStringConverter;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDAbsenceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDispoMinAgriBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursOuvresBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.PctagesDispoCTBean;
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
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final ObservableList<NbrsJoursDAbsenceBean> nbrsJoursDAbsenceBeans = planChargeBean.getAbsencesBeans();

    @NotNull
    private final ObservableList<NbrsJoursDispoMinAgriBean> nbrsJoursDispoMinAgriBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<PctagesDispoCTBean> pctagesDispoMinAgriBeans = FXCollections.observableArrayList();

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
    private TableViewAvecCalendrier<NbrsJoursDAbsenceBean, Double> nbrsJoursDAbsenceTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, String> premiereColonneAbsencesColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine1NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine2NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine3NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine4NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine5NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine6NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine7NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine8NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine9NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine10NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine11NbrsJoursDAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDAbsenceBean, Double> semaine12NbrsJoursDAbsenceColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursDispoMinAgriBean, Double> nbrsJoursDispoMinAgriTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, String> premiereColonneNbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine1NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine2NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine3NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine4NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine5NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine6NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine7NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine8NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine9NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine10NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine11NbrsJoursDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoMinAgriBean, Double> semaine12NbrsJoursDispoMinAgriColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<PctagesDispoCTBean, Percentage> pctagesDispoCTTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, String> premiereColonnePctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine1PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine2PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine3PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine4PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine5PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine6PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine7PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine8PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine9PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine10PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine11PctagesDispoMinAgriColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine12PctagesDispoMinAgriColumn;


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
    public TableViewAvecCalendrier<NbrsJoursDAbsenceBean, Double> getNbrsJoursDAbsenceTable() {
        return nbrsJoursDAbsenceTable;
    }

    @NotNull
    public TableViewAvecCalendrier<NbrsJoursDispoMinAgriBean, Double> getNbrsJoursDispoMinAgriTable() {
        return nbrsJoursDispoMinAgriTable;
    }

    @NotNull
    public TableViewAvecCalendrier<PctagesDispoCTBean, Percentage> getPctagesDispoCTTable() {
        return pctagesDispoCTTable;
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
        initBeansNbrsJoursOuvres();
        initBeansNbrsJoursDAbsences();
        initBeansNbrsJoursDispoMinAgri();
        initBeansPctagesDispoCT();
    }

    private void initBeansNbrsJoursOuvres() {

        NbrsJoursOuvresBean nbrsJoursOuvresBean = new NbrsJoursOuvresBean();
        nbrsJoursOuvresBeans.setAll(nbrsJoursOuvresBean);

        planChargeBean.getJoursFeriesBeans().addListener((ListChangeListener<? super JourFerieBean>) change -> {
            definirValeursCalendrier();
        });
    }

    private void initBeansNbrsJoursDAbsences() {
        planChargeBean.getRessourcesBeans().addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDAbsenceBean> nbrsJoursAbsenceBeansAAjouter = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDAbsenceBeans.parallelStream().anyMatch(nbrsJoursDAbsenceBean -> nbrsJoursDAbsenceBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new TreeMap<>();
                        nbrsJoursAbsenceBeansAAjouter.add(new NbrsJoursDAbsenceBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDAbsenceBeans.addAll(nbrsJoursAbsenceBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursDAbsenceBean> nbrsJoursAbsenceBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDAbsenceBeans.parallelStream().anyMatch(nbrsJoursDAbsenceBean -> nbrsJoursDAbsenceBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new HashMap<>();
                        nbrsJoursAbsenceBeansASupprimer.add(new NbrsJoursDAbsenceBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDAbsenceBeans.removeAll(nbrsJoursAbsenceBeansASupprimer);
                }
                // TODO FDA 2017/08 Coder les autres modifs (permutations, etc.)... si besoin ?
            }
        });
    }

    private void initBeansNbrsJoursDispoMinAgri() {
        planChargeBean.getRessourcesBeans().addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoMinAgriBean> nbrsJoursDispoMinAgriBeansAAjouter = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDispoMinAgriBeans.parallelStream().anyMatch(nbrsJoursDispoMinAgriBean -> nbrsJoursDispoMinAgriBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new TreeMap<>();
                        nbrsJoursDispoMinAgriBeansAAjouter.add(new NbrsJoursDispoMinAgriBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDispoMinAgriBeans.addAll(nbrsJoursDispoMinAgriBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursDispoMinAgriBean> nbrsJoursDispoMinAgriBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDispoMinAgriBeans.parallelStream().anyMatch(nbrsJoursDispoMinAgriBean -> nbrsJoursDispoMinAgriBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new HashMap<>();
                        nbrsJoursDispoMinAgriBeansASupprimer.add(new NbrsJoursDispoMinAgriBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDispoMinAgriBeans.removeAll(nbrsJoursDispoMinAgriBeansASupprimer);
                }
                // TODO FDA 2017/08 Coder les autres modifs (permutations, etc.)... si besoin ?
            }
        });
    }

    private void initBeansPctagesDispoCT() {
        planChargeBean.getRessourcesBeans().addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<PctagesDispoCTBean> pctagesDispoCTBeansAAjouter = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getAddedSubList()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (pctagesDispoMinAgriBeans.parallelStream().anyMatch(pctagesDispoCTBean -> pctagesDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, PercentageProperty> calendrier = new TreeMap<>();
                        pctagesDispoCTBeansAAjouter.add(new PctagesDispoCTBean(ressourceHumaineBean, calendrier));
                    }
                    pctagesDispoMinAgriBeans.addAll(pctagesDispoCTBeansAAjouter);
                }
                // TODO FDA 2017/08 Coder les suppressions (et permutations ?).
            }
        });
    }

    private void initTables() throws IhmException {
        initTableJoursOuvres();
        initTableNbrsJoursAbsences();
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
        //noinspection HardcodedFileSeparator
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
                if (planChargeBean.getDateEtat() == null) {
                    LOGGER.warn("Date d'état non définie !?");
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
        // Cette table n'est pas éditable (données calculées à partir des référentiels).
        ihm.interdireEdition(premiereColonneJoursOuvresColumn, "Cette colonne ne contient pas de données (juste pour aligner avec les lignes suivantes).");
        for (TableColumn<NbrsJoursOuvresBean, ?> nbrsJoursOuvresColumn : nbrsJoursOuvresTable.getCalendrierColumns()) {
            ihm.interdireEdition(nbrsJoursOuvresColumn, "Le nombre de jours ouvrés est calculé à partir du référentiel des jours fériés.");
        }

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

    private void initTableNbrsJoursAbsences() throws IhmException {

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
            final class NbrJoursDAbsenceCellCallback implements Callback<CellDataFeatures<NbrsJoursDAbsenceBean, Double>, ObservableValue<Double>> {
                private final int noSemaine;

                private NbrJoursDAbsenceCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Double> call(CellDataFeatures<NbrsJoursDAbsenceBean, Double> cell) {
                    if (cell == null) {
                        return null;
                    }
                    NbrsJoursDAbsenceBean nbrsJoursDAbsenceBean = cell.getValue();
                    RessourceHumaineBean ressourceHumaineBean = nbrsJoursDAbsenceBean.getRessourceHumaineBean();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    DoubleProperty nbrJoursDAbsencePeriode = nbrsJoursDAbsenceBean.get(debutPeriode);
                    if (nbrJoursDAbsencePeriode == null) { // Pas d'absence prévue pour cette ressource humaine sur cette période.
                        return null;
                    }
                    return nbrJoursDAbsencePeriode.asObject();
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDAbsenceBean, Double> nbrsJoursDAbsenceColumn : nbrsJoursDAbsenceTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        // Rq : 1ère colonne (ressource) non éditable.
        ihm.interdireEdition(premiereColonneAbsencesColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        for (TableColumn<NbrsJoursDAbsenceBean, Double> nbrsJoursDAbsenceColumn : nbrsJoursDAbsenceTable.getCalendrierColumns()) {
            nbrsJoursDAbsenceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableColumnReorderable(nbrsJoursDAbsenceTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDAbsenceTable);

        SortedList<NbrsJoursDAbsenceBean> sortedBeans = new SortedList<>(nbrsJoursDAbsenceBeans);
        sortedBeans.comparatorProperty().bind(nbrsJoursDAbsenceTable.comparatorProperty());

        nbrsJoursDAbsenceTable.setItems(nbrsJoursDAbsenceBeans);
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
            final class NbrJoursDispoMinAgriCellCallback implements Callback<CellDataFeatures<NbrsJoursDispoMinAgriBean, Double>, ObservableValue<Double>> {
                private final int noSemaine;

                private NbrJoursDispoMinAgriCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Double> call(CellDataFeatures<NbrsJoursDispoMinAgriBean, Double> cell) {
                    return new SimpleDoubleProperty(noSemaine).asObject();
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
            for (TableColumn<NbrsJoursDispoMinAgriBean, Double> nbrsJoursDispoMinAgriColumn : nbrsJoursDispoMinAgriTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoMinAgriColumn.setCellValueFactory(new NbrJoursDispoMinAgriCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(premiereColonneNbrsJoursDispoMinAgriColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        for (TableColumn<NbrsJoursDispoMinAgriBean, Double> nbrsJoursDispoMinAgriColumn : nbrsJoursDispoMinAgriTable.getCalendrierColumns()) {
            ihm.interdireEdition(nbrsJoursDispoMinAgriColumn, "Le nombre de jours de disponibilité au Ministère est calculé à partir des jours ouvrés et d'absence.");
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

        pctagesDispoCTTable.setCalendrierColumns(
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
            final class PctagesDispoMinAgriCellCallback implements Callback<CellDataFeatures<PctagesDispoCTBean, Percentage>, ObservableValue<Percentage>> {
                private final int noSemaine;

                private PctagesDispoMinAgriCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Percentage> call(CellDataFeatures<PctagesDispoCTBean, Percentage> cell) {
                    if (cell == null) {
                        return null;
                    }
//                TODO FDA 2017/08 Coder.
                    return new PercentageProperty(new Double(noSemaine / 12.0).floatValue());
/*
                NbrsJoursDAbsenceBean nbrsJoursDAbsenceBean = cell.getValue();
                LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                IntegerProperty nbrJoursDAbsencePeriode = nbrsJoursDAbsenceBean.get(debutPeriode);
                return nbrJoursDAbsencePeriode.asObject();
*/
                }
            }
            int cptColonne = 0;
            for (TableColumn<PctagesDispoCTBean, Percentage> pctagesDispoMinAgriColumn : pctagesDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                pctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(premiereColonnePctagesDispoMinAgriColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        for (TableColumn<PctagesDispoCTBean, Percentage> pctagesDispoMinAgriColumn : pctagesDispoCTTable.getCalendrierColumns()) {
            pctagesDispoMinAgriColumn.setCellFactory(TextFieldTableCell.forTableColumn(new PercentageStringConverter()));
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableColumnReorderable(pctagesDispoCTTable);
        TableViews.adjustHeightToRowCount(pctagesDispoCTTable);

        SortedList<PctagesDispoCTBean> sortedBeans = new SortedList<>(pctagesDispoMinAgriBeans);
        sortedBeans.comparatorProperty().bind(pctagesDispoCTTable.comparatorProperty());

        pctagesDispoCTTable.setItems(sortedBeans);
    }

    private void synchroniserLargeurPremieresColonnes() {
        TableViews.synchronizeColumnsWidth(nbrsJoursOuvresTable, nbrsJoursDAbsenceTable, nbrsJoursDispoMinAgriTable, pctagesDispoCTTable);
    }


    public void definirValeursCalendrier() {

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
/*
            ihm.afficherPopUp(
                    Alert.AlertType.ERROR,
                    "Impossible de définir les valeurs du calendrier",
                    "Date d'état non définie."
            );
*/
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
            nbrsJoursDispoMinAgriTable.refresh();
            pctagesDispoCTTable.refresh();
            LOGGER.debug("Valeurs du calendrier définies.");
        } catch (ServiceException e) {
            throw new IhmException("Impossible de définir les valeurs du calendrier.", e);
        }
    }
}
