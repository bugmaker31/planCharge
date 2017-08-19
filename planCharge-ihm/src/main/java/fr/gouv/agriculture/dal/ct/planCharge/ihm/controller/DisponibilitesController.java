package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.view.PercentageStringConverter;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.TableViewAvecCalendrier;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.DisponibilitesService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.*;

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
    private final NbrsJoursOuvresBean nbrsJoursOuvresBean = new NbrsJoursOuvresBean();

    @NotNull
    private final ObservableList<NbrsJoursOuvresBean> nbrsJoursOuvresBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeans = planChargeBean.getAbsencesBeans();

    @NotNull
    private final ObservableList<NbrsJoursDispoMinAgriBean> nbrsJoursDispoMinAgriBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<PctagesDispoCTBean> pctagesDispoCTBeans = FXCollections.observableArrayList();

    @NotNull
    private final ObservableList<NbrsJoursDispoCTBean> nbrsJoursDispoCTBeans = FXCollections.observableArrayList();

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
    private TableViewAvecCalendrier<NbrsJoursAbsenceBean, Double> nbrsJoursDAbsenceTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, String> premiereColonneAbsencesColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine1NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine2NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine3NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine4NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine5NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine6NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine7NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine8NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine9NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine10NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine11NbrsJoursAbsenceColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursAbsenceBean, Double> semaine12NbrsJoursAbsenceColumn;

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
    private TableColumn<PctagesDispoCTBean, String> premiereColonnePctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine1PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine2PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine3PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine4PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine5PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine6PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine7PctagesDispoMinCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine8PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine9PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine10PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine11PctagesDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<PctagesDispoCTBean, Percentage> semaine12PctagesDispoCTColumn;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private TableViewAvecCalendrier<NbrsJoursDispoCTBean, Double> nbrsJoursDispoCTTable;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, String> premiereColonneNbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine1NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine2NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine3NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine4NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine5NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine6NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine7NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine8NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine9NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine10NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine11NbrsJoursDispoCTColumn;
    @FXML
    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableColumn<NbrsJoursDispoCTBean, Double> semaine12NbrsJoursDispoCTColumn;

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
    public TableViewAvecCalendrier<NbrsJoursAbsenceBean, Double> getNbrsJoursDAbsenceTable() {
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
        initBeanNbrsJoursOuvres();
        initBeanNbrsJoursAbsence();
        initBeanNbrsJoursDispoMinAgri();
        initBeanPctagesDispoCT();
        initBeanNbrsJoursDispoCT();
        planChargeBean.getJoursFeriesBeans().addListener((ListChangeListener<? super JourFerieBean>) change -> {
            definirValeursCalendrier();
        });
    }

    private void initBeanNbrsJoursOuvres() {
        nbrsJoursOuvresBeans.setAll(nbrsJoursOuvresBean);
    }

    private void initBeanNbrsJoursAbsence() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (nbrsJoursAbsenceBeans.parallelStream().anyMatch(nbrsJoursAbsenceBean -> nbrsJoursAbsenceBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new TreeMap<>();
                        nbrsJoursAbsenceBeansAAjouter.add(new NbrsJoursAbsenceBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursAbsenceBeans.addAll(nbrsJoursAbsenceBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursAbsenceBean> nbrsJoursAbsenceBeansASupprimer = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getRemoved()) {
                        if (nbrsJoursAbsenceBeans.parallelStream().noneMatch(nbrsJoursAbsenceBean -> nbrsJoursAbsenceBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new HashMap<>();
                        nbrsJoursAbsenceBeansASupprimer.add(new NbrsJoursAbsenceBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursAbsenceBeans.removeAll(nbrsJoursAbsenceBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                }
                // TODO FDA 2017/08 Coder les autres modifs (permutations, etc.)... si besoin ?
            }
        });
    }

    private void initBeanNbrsJoursDispoMinAgri() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoMinAgriBean> nbrsJoursDispoMinAgriBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
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
                        if (nbrsJoursDispoMinAgriBeans.parallelStream().noneMatch(nbrsJoursDispoMinAgriBean -> nbrsJoursDispoMinAgriBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new HashMap<>();
                        nbrsJoursDispoMinAgriBeansASupprimer.add(new NbrsJoursDispoMinAgriBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDispoMinAgriBeans.removeAll(nbrsJoursDispoMinAgriBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                }
                // TODO FDA 2017/08 Coder les autres modifs (permutations, etc.)... si besoin ?
            }
        });
    }

    private void initBeanPctagesDispoCT() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<PctagesDispoCTBean> pctagesDispoCTBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (pctagesDispoCTBeans.parallelStream().anyMatch(pctagesDispoCTBean -> pctagesDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, PercentageProperty> calendrier = new TreeMap<>();
                        pctagesDispoCTBeansAAjouter.add(new PctagesDispoCTBean(ressourceHumaineBean, calendrier));
                    }
                    pctagesDispoCTBeans.addAll(pctagesDispoCTBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<PctagesDispoCTBean> pctagesDispoCTBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (pctagesDispoCTBeans.parallelStream().noneMatch(pctagesDispoCTBean -> pctagesDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, PercentageProperty> calendrier = new TreeMap<>();
                        pctagesDispoCTBeansASupprimer.add(new PctagesDispoCTBean(ressourceHumaineBean, calendrier));
                    }
                    pctagesDispoCTBeans.removeAll(pctagesDispoCTBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                }
                // TODO FDA 2017/08 Coder les suppressions (et permutations ?).
            }
        });
    }

    private void initBeanNbrsJoursDispoCT() {
        planChargeBean.getRessourcesHumainesBeans().addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<NbrsJoursDispoCTBean> nbrsJoursDispoCTBeansAAjouter = new ArrayList<>();
                    for (RessourceHumaineBean ressourceHumaineBean : change.getAddedSubList()) {
                        if (nbrsJoursDispoCTBeans.parallelStream().anyMatch(nbrsJoursDispoCTBean -> nbrsJoursDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new TreeMap<>();
                        nbrsJoursDispoCTBeansAAjouter.add(new NbrsJoursDispoCTBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDispoCTBeans.addAll(nbrsJoursDispoCTBeansAAjouter);
                }
                if (change.wasRemoved()) {
                    List<NbrsJoursDispoCTBean> nbrsJoursDispoCTBeansASupprimer = new ArrayList<>();
                    for (RessourceBean ressourceBean : change.getRemoved()) {
                        if (!(ressourceBean instanceof RessourceHumaineBean)) {
                            continue;
                        }
                        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;
                        if (nbrsJoursDispoCTBeans.parallelStream().noneMatch(nbrsJoursDispoCTBean -> nbrsJoursDispoCTBean.getRessourceHumaineBean().equals(ressourceHumaineBean))) {
                            continue;
                        }
                        Map<LocalDate, DoubleProperty> calendrier = new HashMap<>();
                        nbrsJoursDispoCTBeansASupprimer.add(new NbrsJoursDispoCTBean(ressourceHumaineBean, calendrier));
                    }
                    nbrsJoursDispoCTBeans.removeAll(nbrsJoursDispoCTBeansASupprimer); // FIXME FDA 2017/08 La liste contient toujours les éléments à supprimer, bien qu'on ait implémneté les méthode equals/hashCode.
                }
                // TODO FDA 2017/08 Coder les autres modifs (permutations, etc.)... si besoin ?
            }
        });
    }

    
    private void initTables() throws IhmException {
        initTableJoursOuvres();
        initTableNbrsJoursAbsence();
        initTableNbrsJoursDispoMinAgri();
        initTablePctagesDispoMinAgri();
        initTableNbrsJoursDispoCT();
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
                NbrsJoursOuvresBean nbrsJoursOuvresRsrcHumPeriodeBean = cell.getValue();
                LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                IntegerProperty nbrJoursOuvresPeriode = nbrsJoursOuvresRsrcHumPeriodeBean.get(debutPeriode);
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

    private void initTableNbrsJoursAbsence() throws IhmException {

        nbrsJoursDAbsenceTable.setCalendrierColumns(
                semaine1NbrsJoursAbsenceColumn,
                semaine2NbrsJoursAbsenceColumn,
                semaine3NbrsJoursAbsenceColumn,
                semaine4NbrsJoursAbsenceColumn,
                semaine5NbrsJoursAbsenceColumn,
                semaine6NbrsJoursAbsenceColumn,
                semaine7NbrsJoursAbsenceColumn,
                semaine8NbrsJoursAbsenceColumn,
                semaine9NbrsJoursAbsenceColumn,
                semaine10NbrsJoursAbsenceColumn,
                semaine11NbrsJoursAbsenceColumn,
                semaine12NbrsJoursAbsenceColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonneAbsencesColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrJoursDAbsenceCellCallback implements Callback<CellDataFeatures<NbrsJoursAbsenceBean, Double>, ObservableValue<Double>> {
                private final int noSemaine;

                private NbrJoursDAbsenceCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Double> call(CellDataFeatures<NbrsJoursAbsenceBean, Double> cell) {
                    if (cell == null) {
                        return null;
                    }
                    NbrsJoursAbsenceBean nbrsJoursAbsenceBean = cell.getValue();
                    RessourceHumaineBean ressourceHumaineBean = nbrsJoursAbsenceBean.getRessourceHumaineBean();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    DoubleProperty nbrJoursDAbsencePeriode = nbrsJoursAbsenceBean.get(debutPeriode);
                    if (nbrJoursDAbsencePeriode == null) { // Pas d'absence prévue pour cette ressource humaine sur cette période.
                        return null;
                    }
                    return nbrJoursDAbsencePeriode.asObject();
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursAbsenceBean, Double> nbrsJoursDAbsenceColumn : nbrsJoursDAbsenceTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDAbsenceColumn.setCellValueFactory(new NbrJoursDAbsenceCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        // Rq : 1ère colonne (ressource) non éditable.
        ihm.interdireEdition(premiereColonneAbsencesColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrsJoursAbsenceCell extends TextFieldTableCell<NbrsJoursAbsenceBean, Double> {

                private final int noSemaine;

                private NbrsJoursAbsenceCell(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;

                    setConverter(new DoubleStringConverter());
                }

                @Override
                public void commitEdit(Double newValue) {
                    super.commitEdit(newValue);

                    //noinspection unchecked
                    TableRow<NbrsJoursAbsenceBean> tableRow = getTableRow();
                    NbrsJoursAbsenceBean nbrJoursAbsenceBean = tableRow.getItem();
                    if (nbrJoursAbsenceBean == null) {
                        return;
                    }
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    if (!nbrJoursAbsenceBean.containsKey(debutPeriode)) {
                        nbrJoursAbsenceBean.put(debutPeriode, new SimpleDoubleProperty());
                    }
                    DoubleProperty nbrJoursDAbsencePeriode = nbrJoursAbsenceBean.get(debutPeriode);
                    nbrJoursDAbsencePeriode.set(newValue);

                    try {
                        majDisponibilites(nbrJoursAbsenceBean.getRessourceHumaineBean(), noSemaine);
                    } catch (IhmException e) {
                        // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        LOGGER.error("Impossible de màj les disponibilités.", e);
                    }
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursAbsenceBean, Double> nbrsJoursDAbsenceColumn : nbrsJoursDAbsenceTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                nbrsJoursDAbsenceColumn.setCellFactory(cell -> new NbrsJoursAbsenceCell(finalCptColonne));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableColumnReorderable(nbrsJoursDAbsenceTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDAbsenceTable);

        SortedList<NbrsJoursAbsenceBean> sortedBeans = new SortedList<>(nbrsJoursAbsenceBeans);
        sortedBeans.comparatorProperty().bind(nbrsJoursDAbsenceTable.comparatorProperty());

        nbrsJoursDAbsenceTable.setItems(nbrsJoursAbsenceBeans);
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
                    if (cell == null) {
                        return null;
                    }
                    NbrsJoursDispoMinAgriBean nbrsJoursDispoMinAgriBean = cell.getValue();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    DoubleProperty nbrsJoursDispoMinAgriProperty = nbrsJoursDispoMinAgriBean.get(debutPeriode);
                    return nbrsJoursDispoMinAgriProperty.asObject();
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

        // Tri de la liste :
        nbrsJoursDispoMinAgriTable.getItems().addListener((ListChangeListener<? super NbrsJoursDispoMinAgriBean>) change -> {
            SortedList<NbrsJoursDispoMinAgriBean> sortedBeans = new SortedList<>(nbrsJoursDispoMinAgriBeans);
            sortedBeans.comparatorProperty().bind(nbrsJoursDispoMinAgriTable.comparatorProperty());
        });

        nbrsJoursDispoMinAgriTable.setItems(nbrsJoursDispoMinAgriBeans);
    }

    private void initTablePctagesDispoMinAgri() {

        pctagesDispoCTTable.setCalendrierColumns(
                semaine1PctagesDispoCTColumn,
                semaine2PctagesDispoCTColumn,
                semaine3PctagesDispoCTColumn,
                semaine4PctagesDispoCTColumn,
                semaine5PctagesDispoCTColumn,
                semaine6PctagesDispoCTColumn,
                semaine7PctagesDispoMinCTColumn,
                semaine8PctagesDispoCTColumn,
                semaine9PctagesDispoCTColumn,
                semaine10PctagesDispoCTColumn,
                semaine11PctagesDispoCTColumn,
                semaine12PctagesDispoCTColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonnePctagesDispoCTColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
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
                    PctagesDispoCTBean pctagesDispoCTBean = cell.getValue();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    PercentageProperty percentageProperty = pctagesDispoCTBean.get(debutPeriode);
                    return percentageProperty;
                }
            }
            int cptColonne = 0;
            for (TableColumn<PctagesDispoCTBean, Percentage> pctagesDispoMinAgriColumn : pctagesDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                pctagesDispoMinAgriColumn.setCellValueFactory(new PctagesDispoMinAgriCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        // Rq : 1ère colonne (ressource) non éditable.
        ihm.interdireEdition(premiereColonnePctagesDispoCTColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class PctagesDispoCTCell extends TextFieldTableCell<PctagesDispoCTBean, Percentage> {

                private final int noSemaine;

                private PctagesDispoCTCell(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;

                    setConverter(new PercentageStringConverter());
                }

                @Override
                public void commitEdit(Percentage newValue) {
                    super.commitEdit(newValue);

                    //noinspection unchecked
                    TableRow<PctagesDispoCTBean> tableRow = getTableRow();
                    PctagesDispoCTBean pctagesDispoCTBean = tableRow.getItem();
                    if (pctagesDispoCTBean == null) {
                        return;
                    }
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    if (!pctagesDispoCTBean.containsKey(debutPeriode)) {
                        pctagesDispoCTBean.put(debutPeriode, new PercentageProperty(disponibilitesService.PCTAGE_DISPO_CT_MIN.floatValue()));
                    }
                    PercentageProperty pctageDispoCTPeriodeProperty = pctagesDispoCTBean.get(debutPeriode);
                    pctageDispoCTPeriodeProperty.setValue(newValue);

                    try {
                        majDisponibilites(pctagesDispoCTBean.getRessourceHumaineBean(), noSemaine);
                    } catch (IhmException e) {
                        // TODO FDA 2017/08 Trouver mieux que juste loguer une erreur.
                        LOGGER.error("Impossible de màj les disponibilités.", e);
                    }
                }
            }
            int cptColonne = 0;
            for (TableColumn<PctagesDispoCTBean, Percentage> pctagesDispoCTColumn : pctagesDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                int finalCptColonne = cptColonne;
                pctagesDispoCTColumn.setCellFactory(cell -> new PctagesDispoCTCell(finalCptColonne));
            }
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableColumnReorderable(pctagesDispoCTTable);
        TableViews.adjustHeightToRowCount(pctagesDispoCTTable);

        // Tri de la liste :
        pctagesDispoCTTable.getItems().addListener((ListChangeListener<? super PctagesDispoCTBean>) change -> {
            SortedList<PctagesDispoCTBean> sortedBeans = new SortedList<>(pctagesDispoCTBeans);
            sortedBeans.comparatorProperty().bind(pctagesDispoCTTable.comparatorProperty());
        });

        pctagesDispoCTTable.setItems(pctagesDispoCTBeans);
    }

    private void initTableNbrsJoursDispoCT() {

        nbrsJoursDispoCTTable.setCalendrierColumns(
                semaine1NbrsJoursDispoCTColumn,
                semaine2NbrsJoursDispoCTColumn,
                semaine3NbrsJoursDispoCTColumn,
                semaine4NbrsJoursDispoCTColumn,
                semaine5NbrsJoursDispoCTColumn,
                semaine6NbrsJoursDispoCTColumn,
                semaine7NbrsJoursDispoCTColumn,
                semaine8NbrsJoursDispoCTColumn,
                semaine9NbrsJoursDispoCTColumn,
                semaine10NbrsJoursDispoCTColumn,
                semaine11NbrsJoursDispoCTColumn,
                semaine12NbrsJoursDispoCTColumn
        );

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        premiereColonneNbrsJoursDispoCTColumn.setCellValueFactory(cell -> cell.getValue().getRessourceHumaineBean().trigrammeProperty());
        {
            //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
            final class NbrJoursDispoCTCellCallback implements Callback<CellDataFeatures<NbrsJoursDispoCTBean, Double>, ObservableValue<Double>> {
                private final int noSemaine;

                private NbrJoursDispoCTCellCallback(int noSemaine) {
                    super();
                    this.noSemaine = noSemaine;
                }

                @Null
                @Override
                public ObservableValue<Double> call(CellDataFeatures<NbrsJoursDispoCTBean, Double> cell) {
                    if (cell == null) {
                        return null;
                    }
                    NbrsJoursDispoCTBean nbrsJoursDispoCTBean = cell.getValue();
                    if (planChargeBean.getDateEtat() == null) {
                        LOGGER.warn("Date d'état non définie !?");
                        return null;
                    }
                    LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // FIXME FDA 2017/06 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                    DoubleProperty nbrsJoursDispoCTProperty = nbrsJoursDispoCTBean.get(debutPeriode);
                    return nbrsJoursDispoCTProperty.asObject();
                }
            }
            int cptColonne = 0;
            for (TableColumn<NbrsJoursDispoCTBean, Double> nbrsJoursDispoCTColumn : nbrsJoursDispoCTTable.getCalendrierColumns()) {
                cptColonne++;
                nbrsJoursDispoCTColumn.setCellValueFactory(new NbrJoursDispoCTCellCallback(cptColonne));
            }
        }

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        ihm.interdireEdition(premiereColonneNbrsJoursDispoCTColumn, "Cette colonne reprend les ressources humaines (ajouter une ressource humaine pour ajouter une ligne dans cette table).");
        for (TableColumn<NbrsJoursDispoCTBean, Double> nbrsJoursDispoCTColumn : nbrsJoursDispoCTTable.getCalendrierColumns()) {
            ihm.interdireEdition(nbrsJoursDispoCTColumn, "Le nombre de jours de disponibilité au Ministère est calculé à partir des jours ouvrés et d'absence.");
        }

        // Paramétrage des ordres de tri :
        //Pas sur cet écran (pas d'ordre de tri spécial, les tris standards de JavaFX font l'affaire).

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //Pas sur cet écran (pas nécessaire, ni même utile).

        TableViews.disableColumnReorderable(nbrsJoursDispoCTTable);
        TableViews.adjustHeightToRowCount(nbrsJoursDispoCTTable);

        // Tri de la liste :
        nbrsJoursDispoCTTable.getItems().addListener((ListChangeListener<? super NbrsJoursDispoCTBean>) change -> {
            SortedList<NbrsJoursDispoCTBean> sortedBeans = new SortedList<>(nbrsJoursDispoCTBeans);
            sortedBeans.comparatorProperty().bind(nbrsJoursDispoCTTable.comparatorProperty());
        });

        nbrsJoursDispoCTTable.setItems(nbrsJoursDispoCTBeans);
    }
    
    
    private void synchroniserLargeurPremieresColonnes() {
        TableViews.synchronizeColumnsWidth(nbrsJoursOuvresTable, nbrsJoursDAbsenceTable, nbrsJoursDispoMinAgriTable, pctagesDispoCTTable);
    }


    private void majDisponibilites(@NotNull RessourceHumaineBean rsrcHumBean, int noSemaine) throws IhmException {

        LocalDate debutPeriode = planChargeBean.dateEtat().plusDays(7 * (noSemaine - 1)); // FIXME FDA 2017/08 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
        LocalDate finPeriode = debutPeriode.plusDays(7); // FIXME FDA 2017/08 Ne marche que quand les périodes sont des semaines, pas pour les trimestres.

        // Nbr de jours ouvrés :
        int nbrJoursOuvresPeriode;
        try {
            nbrJoursOuvresPeriode = disponibilitesService.nbrJoursOuvres(debutPeriode, finPeriode);
        } catch (ServiceException e) {
            throw new IhmException("Impossible de calculer le nombre de jours ouvrés.", e);
        }
        if (!nbrsJoursOuvresBean.containsKey(debutPeriode)) {
            nbrsJoursOuvresBean.put(debutPeriode, new SimpleIntegerProperty());
        }
        IntegerProperty nbrJoursOuvresPeriodeProperty = nbrsJoursOuvresBean.get(debutPeriode);
        nbrJoursOuvresPeriodeProperty.set(nbrJoursOuvresPeriode);

        // Nbr de jours d'absence :
        NbrsJoursAbsenceBean nbrsJoursAbsenceBean = Collections.getFirst(nbrsJoursAbsenceBeans, bean -> bean.getRessourceHumaineBean().equals(rsrcHumBean)/*, new IhmException("Impossible de retrouver la ressource humaine '" + rsrcHumBean.getTrigramme() + "'.")*/);
        if (nbrsJoursAbsenceBean == null) {
            nbrsJoursAbsenceBean = new NbrsJoursAbsenceBean(rsrcHumBean);
            nbrsJoursAbsenceBeans.add(nbrsJoursAbsenceBean);
        }
        DoubleProperty nbrsJoursAbsencePeriodeProperty = nbrsJoursAbsenceBean.get(debutPeriode);
        double nbrsJoursAbsencePeriode = Objects.<DoubleProperty, Double>value(nbrsJoursAbsencePeriodeProperty, DoubleProperty::get, 0.0);

        // Nbr de jours de dispo pour le Ministère :
        NbrsJoursDispoMinAgriBean nbrsJoursDispoMinAgriBean = Collections.fetchFirst(nbrsJoursDispoMinAgriBeans, bean -> bean.getRessourceHumaineBean().equals(rsrcHumBean), new IhmException("Impossible de retrouver la ressource humaine '" + rsrcHumBean.getTrigramme() + "'."));
        if (!nbrsJoursDispoMinAgriBean.containsKey(debutPeriode)) {
            nbrsJoursDispoMinAgriBean.put(debutPeriode, new SimpleDoubleProperty());
        }
        DoubleProperty nbrJoursDispoMinAgriPeriodeProperty = nbrsJoursDispoMinAgriBean.get(debutPeriode);
        double nbrJoursDispoMinAgriPeriode = Math.max(nbrJoursOuvresPeriode - nbrsJoursAbsencePeriode, 0);
        nbrJoursDispoMinAgriPeriodeProperty.set(nbrJoursDispoMinAgriPeriode);

        // % dispo CT  :
        PctagesDispoCTBean pctagesDispoCTBean = Collections.fetchFirst(pctagesDispoCTBeans, bean -> bean.getRessourceHumaineBean().equals(rsrcHumBean), new IhmException("Impossible de retrouver la ressource humaine '" + rsrcHumBean.getTrigramme() + "'."));
        PercentageProperty percentageDispoCTPeriodeProperty = pctagesDispoCTBean.get(debutPeriode);
        Percentage percentageDispoCTPeriode = Objects.value(percentageDispoCTPeriodeProperty, PercentageProperty::getValue, disponibilitesService.PCTAGE_DISPO_CT_MIN);

        // Nbr de jours de dispo pour la CT :
        NbrsJoursDispoCTBean nbrsJoursDispoCTBean = Collections.fetchFirst(nbrsJoursDispoCTBeans, bean -> bean.getRessourceHumaineBean().equals(rsrcHumBean), new IhmException("Impossible de retrouver la ressource humaine '" + rsrcHumBean.getTrigramme() + "'."));
        if (!nbrsJoursDispoCTBean.containsKey(debutPeriode)) {
            nbrsJoursDispoCTBean.put(debutPeriode, new SimpleDoubleProperty());
        }
        DoubleProperty nbrJoursDispoCTPeriodeProperty = nbrsJoursDispoCTBean.get(debutPeriode);
        nbrJoursDispoCTPeriodeProperty.set((nbrJoursDispoMinAgriPeriode * percentageDispoCTPeriode.floatValue()) / 100);

        // FIXME FDA 2017/08 Coder les autres tables (en cascade).
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
        for (int noSemaine = 1; noSemaine <= PlanificationsDTO.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            for (RessourceHumaineBean ressourceHumaineBean : planChargeBean.getRessourcesHumainesBeans()) {
                majDisponibilites(ressourceHumaineBean, noSemaine);
            }
        }
        LOGGER.debug("Valeurs du calendrier définies.");
    }
}
