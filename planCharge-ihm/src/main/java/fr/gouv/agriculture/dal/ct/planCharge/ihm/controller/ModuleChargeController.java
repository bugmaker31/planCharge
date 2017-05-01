package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.controlsfx.control.CheckComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleChargeController extends AbstractTachesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleChargeController.class);

    // La table de calendrier des tâches :
    @FXML
    private TableView<PlanificationBean> planificationsTable;
    @FXML
    private TableColumn<PlanificationBean, String> noTacheColumn;
    @FXML
    private TableColumn<PlanificationBean, String> noTicketIdalColumn;
    @FXML
    private TableColumn<PlanificationBean, String> descriptionColumn;
    @FXML
    private TableColumn<PlanificationBean, String> projetAppliColumn;
    @FXML
    private TableColumn<PlanificationBean, LocalDate> debutColumn;
    @FXML
    private TableColumn<PlanificationBean, LocalDate> echeanceColumn;
    @FXML
    private TableColumn<PlanificationBean, Importance> importanceColumn;
    @FXML
    private TableColumn<PlanificationBean, Double> chargeColumn;
    @FXML
    private TableColumn<PlanificationBean, String> ressourceColumn;
    @FXML
    private TableColumn<PlanificationBean, String> profilColumn;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine1Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine2Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine3Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine4Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine5Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine6Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine7Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine8Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine9Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine10Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine11Column;
    @FXML
    private TableColumn<PlanificationBean, Double> semaine12Column;
    @FXML
    private TableColumn<PlanificationBean, Double> chargePlanifieeColumn;

    // Les paramètres :
    @FXML
    private DatePicker dateEtatPicker;

    // Les filtres :
    // TODO FDA 2017/05 Ajouter les filtres spécifiques des charges : semaine chargée, reste à planifier <> 0, planifiée dans le mois qui vient, etc.

    @NotNull
    private ObservableList<Importance> importancesTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesProjetsApplisTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesRessourcesTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesProfilsTaches = FXCollections.observableArrayList();

    // Les services métier :
//    @Autowired
    @NotNull
    private PlanChargeService planChargeService = PlanChargeService.instance();

    // L'IHM :
//    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    // Les données métier :
//    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    private ObservableList<PlanificationBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

    @NotNull
    public DatePicker getDateEtatPicker() {
        return dateEtatPicker;
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleChargeController() {
        super();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        dateEtatPicker.setValue(planChargeBean.getDateEtat());

        /*
        // Rendre le "divider" du SplitPane fixe (== "non draggable") :
        // (Cf. http://stackoverflow.com/questions/26762928/javafx-disable-divider)
        splitPane.lookupAll(".split-pane-divider").stream().forEach(div ->  div.setMouseTransparent(true) );
*/

        // Formattage des données des cellules (en rendu "non éditable") :
        class ChargeSemaineCellCallback implements Callback<TableColumn.CellDataFeatures<PlanificationBean, Double>, ObservableValue<Double>> {
            private final int noSemaine;

            private ChargeSemaineCellCallback(int noSemaine) {
                this.noSemaine = noSemaine;
            }

            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<PlanificationBean, Double> cell) {
                PlanificationBean planifBean = cell.getValue();
                Double nouvelleCharge = planifBean.charge(noSemaine).doubleValue();
                return nouvelleCharge.equals(0.0) ? null : new SimpleDoubleProperty(nouvelleCharge).asObject();
            }
        }
        noTacheColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().noTacheProperty());
        noTicketIdalColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().noTicketIdalProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().descriptionProperty());
        projetAppliColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().projetAppliProperty());
        debutColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().debutProperty());
        echeanceColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().echeanceProperty());
        importanceColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().importanceProperty());
        chargeColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().chargeProperty().asObject());
        ressourceColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().ressourceProperty());
        profilColumn.setCellValueFactory(cellData -> cellData.getValue().getTacheBean().profilProperty());
        semaine1Column.setCellValueFactory(new ChargeSemaineCellCallback(1));
        semaine2Column.setCellValueFactory(new ChargeSemaineCellCallback(2));
        semaine3Column.setCellValueFactory(new ChargeSemaineCellCallback(3));
        semaine4Column.setCellValueFactory(new ChargeSemaineCellCallback(4));
        semaine5Column.setCellValueFactory(new ChargeSemaineCellCallback(5));
        semaine6Column.setCellValueFactory(new ChargeSemaineCellCallback(6));
        semaine7Column.setCellValueFactory(new ChargeSemaineCellCallback(7));
        semaine8Column.setCellValueFactory(new ChargeSemaineCellCallback(8));
        semaine9Column.setCellValueFactory(new ChargeSemaineCellCallback(9));
        semaine10Column.setCellValueFactory(new ChargeSemaineCellCallback(10));
        semaine11Column.setCellValueFactory(new ChargeSemaineCellCallback(11));
        semaine12Column.setCellValueFactory(new ChargeSemaineCellCallback(12));
        chargePlanifieeColumn.setCellValueFactory(cellData -> cellData.getValue().chargePlanifieeProperty().asObject());
        //
        // Custom rendering of the table cell:
        // Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/
        chargeColumn.setCellFactory(column -> new TableCell<PlanificationBean, Double>() {
            @Override
            protected void updateItem(Double charge, boolean empty) {
                super.updateItem(charge, empty);

                if (charge == null || empty) {
                    setText(null);
                    setStyle("");
                } else {

                    // Format.
                    setText(charge.toString()); // TODO FDA 2017/04 Mieux formater les charges ?

                    // Style with a different color.
                    //            TODO FDA 2017/04 Terminer.
                    String styleBackgroundColour;
                    double chargePlanifiee = planificationsBeans.get(this.getIndex()).getChargePlanifiee();
                    if (charge < chargePlanifiee) {
                        setTextFill(Color.WHITE);
                        styleBackgroundColour = "maroon";
                    } else {
                        setTextFill(Color.BLACK);
                        styleBackgroundColour = "black";
                    }
                    setStyle("-fx-background-color: " + styleBackgroundColour + "; -fx-alignment: center-right");
                }
            }
        });
        class ChargeSemaineEditHandler implements EventHandler<TableColumn.CellEditEvent<PlanificationBean, Double>> {

            private final int noSemaine;

            public ChargeSemaineEditHandler(int noSemaine) {
                this.noSemaine = noSemaine;
            }

            @Override
            public void handle(TableColumn.CellEditEvent<PlanificationBean, Double> event) {

                PlanificationBean planifBean = event.getRowValue();
                planifBean.charge(noSemaine).setValue(event.getNewValue());

                planifBean.majChargePlanifiee();
            }
        }
        semaine1Column.setOnEditCommit(new ChargeSemaineEditHandler(1));
        semaine2Column.setOnEditCommit(new ChargeSemaineEditHandler(2));
        semaine3Column.setOnEditCommit(new ChargeSemaineEditHandler(3));
        semaine4Column.setOnEditCommit(new ChargeSemaineEditHandler(4));
        semaine5Column.setOnEditCommit(new ChargeSemaineEditHandler(5));
        semaine6Column.setOnEditCommit(new ChargeSemaineEditHandler(6));
        semaine7Column.setOnEditCommit(new ChargeSemaineEditHandler(7));
        semaine8Column.setOnEditCommit(new ChargeSemaineEditHandler(8));
        semaine9Column.setOnEditCommit(new ChargeSemaineEditHandler(9));
        semaine10Column.setOnEditCommit(new ChargeSemaineEditHandler(10));
        semaine11Column.setOnEditCommit(new ChargeSemaineEditHandler(11));
        semaine12Column.setOnEditCommit(new ChargeSemaineEditHandler(12));

        // Paramétrage des cellules éditables :
/*
        La colonne "N° de tâche" n'est pas éditable (car c'est la "primaty key").
        noTacheColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
*/
        noTicketIdalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projetAppliColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesProjetsApplisTaches));
        debutColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter())); // TODO FDA 2017/04 Ne permettre de saisir qu'une date (DatePicker), plutôt qu'un string.
        echeanceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter())); // TODO FDA 2017/04 Ne permettre de saisir qu'une date (DatePicker), plutôt qu'un string.
        importanceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(importancesTaches));
        chargeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        ressourceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesRessourcesTaches));
        profilColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesProfilsTaches));
        semaine1Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine2Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine3Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine4Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine5Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine6Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine7Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine8Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine9Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine10Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine11Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        semaine12Column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        definirNomsColonnesSemaine();

        // Paramétrage des ordres de tri :
        importanceColumn.setComparator(Importance::compareTo);

/*
        // Cf. http://stackoverflow.com/questions/23789525/onscroll-listener-does-not-working-in-tableview-in-javafx-2
        tachesTable.addEventFilter(ScrollEvent.ANY, event -> tachesScroll(event));
*/

/*
        importancesTaches.addListener((ListChangeListener<? super String>) changeListener -> changeListener.getList().sort(String::compareTo));
        codesProjetsApplisTaches.addListener((ListChangeListener<? super String>) changeListener -> changeListener.getList().sort(String::compareTo));
        codesRessourcesTaches.addListener((ListChangeListener<? super String>) changeListener -> changeListener.getList().sort(String::compareTo));
        codesProfilsTaches.addListener((ListChangeListener<? super String>) changeListener -> changeListener.getList().sort(String::compareTo));
*/

/* TODO FDA 2017/04 Alimenter ces listes avec les référentiels, plutôt.
            importancesTaches.setAll(changeListener.getList().stream().map(planification -> planification.getTacheBean().getImportance()).collect(Collectors.toSet()));
            codesProjetsApplisTaches.setAll(changeListener.getList().stream().map(planification -> planification.getTacheBean().getProjetAppli()).collect(Collectors.toSet()));
            codesRessourcesTaches.setAll(changeListener.getList().stream().map(planification -> planification.getTacheBean().getIdRessource()).collect(Collectors.toSet()));
            codesProfilsTaches.setAll(changeListener.getList().stream().map(planification -> planification.getTacheBean().getProfil()).collect(Collectors.toSet()));
*/
        populerFiltreProjetsApplis();
        populerFiltreImportances();
        populerFiltreRessources();
        populerFiltreProfils();

        planificationsBeans.addListener((ListChangeListener<? super PlanificationBean>) changeListener -> {

            // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
            // 1. Wrap the ObservableList in a FilteredList
            FilteredList<PlanificationBean> filteredPlanifBeans = new FilteredList<>(planificationsBeans);
            // 2. Set the filter Predicate whenever the filter changes.
            filtreGlobalField.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredPlanifBeans.setPredicate(planification -> {

                        // If filter text is empty, display all data.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare column values with filter text.
                        if (planification.getTacheBean().matcheNoTache(newValue)) {
                            return true; // Filter matches
                        }
                        if (!planification.getTacheBean().noTicketIdalProperty().isEmpty().get() && planification.getTacheBean().matcheNoTicketIdal(newValue)) {
                            return true; // Filter matches
                        }
                        if (!planification.getTacheBean().descriptionProperty().isEmpty().get() && planification.getTacheBean().matcheDescription(newValue)) {
                            return true; // Filter matches
                        }
                        if (!planification.getTacheBean().debutProperty().isNull().get() && planification.getTacheBean().matcheDebut(newValue)) {
                            return true; // Filter matches
                        }
                        if (!planification.getTacheBean().echeanceProperty().isNull().get() && planification.getTacheBean().matcheEcheance(newValue)) {
                            return true; // Filter matches
                        }
                        if (!planification.getTacheBean().projetAppliProperty().isEmpty().get() && planification.getTacheBean().matcheProjetAppli(newValue)) {
                            return true; // Filter matches
                        }
                        if (!planification.getTacheBean().importanceProperty().isNull().get() && planification.getTacheBean().matcheImportance(newValue)) {
                            return true; // Filter matches
                        }
                        if (!planification.getTacheBean().ressourceProperty().isEmpty().get() && planification.getTacheBean().matcheRessource(newValue)) {
                            return true; // Filter matches
                        }
                        if (!planification.getTacheBean().profilProperty().isEmpty().get() && planification.getTacheBean().matcheProfil(newValue)) {
                            return true; // Filter matches
                        }
                        return false; // Does not match.
                    })
            );
            filtreNoTacheField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredPlanifBeans.setPredicate(planification -> {

                    // If filter text is empty, display all data.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare column values with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (planification.getTacheBean().matcheNoTache(lowerCaseFilter)) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                });
            });
            filtreNoTicketIdalField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredPlanifBeans.setPredicate(planification -> {

                    // If filter text is empty, display all data.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare column values with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (!planification.getTacheBean().noTicketIdalProperty().isEmpty().get() && planification.getTacheBean().matcheNoTicketIdal(lowerCaseFilter)) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                });
            });
            filtreDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredPlanifBeans.setPredicate(planification -> {

                    // If filter text is empty, display all data.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare column values with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (!planification.getTacheBean().descriptionProperty().isEmpty().get() && planification.getTacheBean().matcheDescription(lowerCaseFilter)) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                });
            });
            filtreDebutField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
                filteredPlanifBeans.setPredicate(planification -> {

                    // If filter text is empty, display all data.
                    if (newValue == null) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (!planification.getTacheBean().debutProperty().isNull().get() && planification.getTacheBean().matcheDebut(newValue.format(TacheBean.DATE_FORMATTER))) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                });
            });
            filtreEcheanceField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
                filteredPlanifBeans.setPredicate(planification -> {

                    // If filter text is empty, display all data.
                    if (newValue == null) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (!planification.getTacheBean().echeanceProperty().isNull().get() && planification.getTacheBean().matcheEcheance(newValue.format(TacheBean.DATE_FORMATTER))) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                });
            });
            filtreProjetsApplisField.getCheckModel().getCheckedItems().addListener(
                    (ListChangeListener<String>) change -> {
//                    change.reset();
                        filteredPlanifBeans.setPredicate(planification -> {
                            for (String codeProjetAppliSelectionne : change.getList()) {
                                if (!planification.getTacheBean().projetAppliProperty().isEmpty().get() && planification.getTacheBean().matcheProjetAppli(codeProjetAppliSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
            );
            filtreImportancesField.getCheckModel().getCheckedItems().addListener(
                    (ListChangeListener<String>) change -> {
//                    change.reset();
                        filteredPlanifBeans.setPredicate(planification -> {
                            for (String codeImportanceSelectionne : change.getList()) {
                                if (!planification.getTacheBean().importanceProperty().isNull().get() && planification.getTacheBean().matcheImportance(codeImportanceSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
            );
            filtreRessourcesField.getCheckModel().getCheckedItems().addListener(
                    (ListChangeListener<String>) change -> {
//                    change.reset();
                        filteredPlanifBeans.setPredicate(planification -> {
                            for (String codeRessourceSelectionne : change.getList()) {
                                if (!planification.getTacheBean().ressourceProperty().isEmpty().get() && planification.getTacheBean().matcheRessource(codeRessourceSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
            );
            filtreProfilsField.getCheckModel().getCheckedItems().addListener(
                    (ListChangeListener<String>) change -> {
//                    change.reset();
                        filteredPlanifBeans.setPredicate(planification -> {
                            for (String codeProfilSelectionne : change.getList()) {
                                if (!planification.getTacheBean().profilProperty().isEmpty().get() && planification.getTacheBean().matcheProfil(codeProfilSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
            );
            // 3. Wrap the FilteredList in a SortedList.
            SortedList<PlanificationBean> sortedPlanifBeans = new SortedList<>(filteredPlanifBeans);
            // 4. Bind the SortedList COMPARATOR to the TableView COMPARATOR.
            sortedPlanifBeans.comparatorProperty().bind(planificationsTable.comparatorProperty());
            // 5. Add sorted (and filtered) data to the table.
            planificationsTable.setItems(sortedPlanifBeans);
        });
    }

    private void definirNomsColonnesSemaine() {
        // TODO FDA 2017/04 Coder.
    }

    private void populerFiltreProjetsApplis() {
        filtreProjetsApplisField.getItems().setAll(
                planificationsBeans.stream()
                        .filter(planification -> (planification.getTacheBean().getProjetAppli() != null))
                        .map(planification -> planification.getTacheBean().getProjetAppli())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreProjetsApplisField.getCheckModel().checkAll();
    }

    private void populerFiltreImportances() {
        filtreImportancesField.getItems().setAll(
                planificationsBeans.stream()
                        .filter(planification -> (planification.getTacheBean().getImportance() != null))
                        .map(planification -> planification.getTacheBean().getImportance().getCode())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreImportancesField.getCheckModel().checkAll();
    }

    private void populerFiltreRessources() {
        filtreRessourcesField.getItems().setAll(
                planificationsBeans.stream()
                        .filter(planification -> (planification.getTacheBean().getRessource() != null))
                        .map(planification -> planification.getTacheBean().getRessource())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreRessourcesField.getCheckModel().checkAll();
    }

    private void populerFiltreProfils() {
        filtreProfilsField.getItems().setAll(
                planificationsBeans.stream()
                        .filter(planification -> (planification.getTacheBean().getProfil() != null))
                        .map(planification -> planification.getTacheBean().getProfil())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreProfilsField.getCheckModel().checkAll();
    }

/*
    @FXML
    private void tachesScroll(Event event) {
        LOGGER.debug("Scroll sur la table des tâches...");
    }

    @FXML
    private void tachesScroll(ScrollToEvent<Integer> event) {
        LOGGER.debug("Scroll sur la table des tâches...");

    }

    private void tachesScroll(ScrollEvent event) {
        LOGGER.debug("Scroll sur la table des tâches...");
    }
*/

    @FXML
    protected void ajouterTache(ActionEvent event) {
        LOGGER.debug("ajouterTache...");

        if (planChargeBean.getDateEtat() == null) {
            LOGGER.warn("Impossible d'ajouter une tâche car la date d'état n'est pas définie.");
            ihm.afficherPopUp(
                    Alert.AlertType.WARNING,
                    "Impossible d'ajouter une tâche",
                    "Impossible d'ajouter une tâche car la date d'état n'est pas définie. Précisez une date auparavant.",
                    500, 200
            );
            return;
        }

        super.ajouterTache(event);

        TacheBean tache = taches.get(taches.size() - 1);

        List<Pair<LocalDate, DoubleProperty>> calendrier = new ArrayList<>(Planifications.NBR_SEMAINES_PLANIFIEES);
        LocalDate dateSemaine = planChargeBean.getDateEtat();
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calendrier.add(new Pair(dateSemaine, new SimpleDoubleProperty(0.0)));
            dateSemaine = dateSemaine.plusDays(7);
        }

        PlanificationBean planifBean = new PlanificationBean(tache, calendrier);
        planificationsBeans.add(planifBean);
    }

    @FXML
    private void definirDateEtat(ActionEvent event) {
        LOGGER.debug("definirDateEtat...");
        LocalDate dateEtat = dateEtatPicker.getValue();
        ihm.definirDateEtat(dateEtat);
    }

    @FXML
    private void positionnerDateEtatAuProchainLundi(ActionEvent event) {
        LOGGER.debug("positionnerDateEtatAuProchainLundi...");

        LocalDate dateEtat = LocalDate.now();
        if (dateEtat.getDayOfWeek() != DayOfWeek.MONDAY) {
            dateEtat = dateEtat.plusDays(7 - dateEtat.getDayOfWeek().getValue() + 1);
        }

        ihm.definirDateEtat(dateEtat);
    }

}
