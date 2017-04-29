package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.ImportanceComparator;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
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
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleChargeController extends AbstractController {

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
    private TableColumn<PlanificationBean, String> importanceColumn;
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
    @FXML
    private TextField filtreGlobalField;
    @FXML
    private TextField filtreNoTacheField;
    @FXML
    private TextField filtreNoTicketIdalField;
    @FXML
    private TextField filtreDescriptionField;
    @FXML
    private CheckComboBox<String> filtreProjetsApplisField;
    @FXML
    private DatePicker filtreDebutField; // TODO FDA 2017/04 Utiliser.
    @FXML
    private DatePicker filtreEcheanceField;  // TODO FDA 2017/04 Utiliser.
    @FXML
    private CheckComboBox<String> filtreImportancesField;
    @FXML
    private CheckComboBox<String> filtreRessourcesField;
    @FXML
    private CheckComboBox<String> filtreProfilsField;

    @NotNull
    private ObservableList<String> codesImportancesTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesProjetsApplisTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesRessourcesTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesProfilsTaches = FXCollections.observableArrayList();

    // L'IHM :
    @NotNull
    @Autowired
    private PlanChargeIhm ihm = PlanChargeIhm.getContext().getBean(PlanChargeIhm.class);

    // Les services métier :
    @NotNull
    @Autowired
    private PlanChargeService planChargeService = PlanChargeIhm.getContext().getBean(PlanChargeService.class);

    // Les données métier :
    @NotNull
    @Autowired
    private PlanChargeBean planChargeBean = PlanChargeIhm.getContext().getBean(PlanChargeBean.class);

    @NotNull
    private ObservableList<PlanificationBean> planificationsBeans = planChargeBean.getPlanificationsBeans();

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

        planificationsBeans = planChargeBean.getPlanificationsBeans();

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
        importanceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesImportancesTaches));
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

        // Paramétrage des ordres de tri :
        importanceColumn.setComparator(new ImportanceComparator());

/*
        // Cf. http://stackoverflow.com/questions/23789525/onscroll-listener-does-not-working-in-tableview-in-javafx-2
        tachesTable.addEventFilter(ScrollEvent.ANY, event -> tachesScroll(event));
*/

        planificationsBeans.addListener((ListChangeListener<? super PlanificationBean>) changeListener -> {
            codesImportancesTaches.clear();
            codesImportancesTaches.addAll(changeListener.getList().stream().map(planification -> planification.getTacheBean().getImportance()).collect(Collectors.toSet()));
            codesImportancesTaches.sort(String::compareTo);

            codesProjetsApplisTaches.clear();
            codesProjetsApplisTaches.addAll(changeListener.getList().stream().map(planification -> planification.getTacheBean().getProjetAppli()).collect(Collectors.toSet()));
            codesProjetsApplisTaches.sort(String::compareTo);

            codesRessourcesTaches.clear();
            codesRessourcesTaches.addAll(changeListener.getList().stream().map(planification -> planification.getTacheBean().getRessource()).collect(Collectors.toSet()));
            codesRessourcesTaches.sort(String::compareTo);

            codesProfilsTaches.clear();
            codesProfilsTaches.addAll(changeListener.getList().stream().map(planification -> planification.getTacheBean().getProfil()).collect(Collectors.toSet()));
            codesProfilsTaches.sort(String::compareTo);
        });

        populerFiltreProjetsApplis();
        populerFiltreImportances();
        populerFiltreRessources();
        populerFiltreProfils();
        definirNomsColonnesSemaine();

        // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
        // 1. Wrap the ObservableList in a FilteredList
        FilteredList<PlanificationBean> filteredData = new FilteredList<>(planificationsBeans);
        // 2. Set the filter Predicate whenever the filter changes.
        filtreGlobalField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(planification -> {

                    // If filter text is empty, display all data.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (planification.getTacheBean().matcheNoTache(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTacheBean().matcheNoTicketIdal(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTacheBean().matcheDescription(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTacheBean().matcheProjetAppli(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTacheBean().matcheImportance(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTacheBean().matcheDebut(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTacheBean().matcheEcheance(newValue)) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                })
        );
        filtreNoTacheField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(planification -> {

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
            filteredData.setPredicate(planification -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (planification.getTacheBean().matcheNoTicketIdal(lowerCaseFilter)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(planification -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (planification.getTacheBean().matcheDescription(lowerCaseFilter)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreDebutField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
            filteredData.setPredicate(planification -> {

                // If filter text is empty, display all data.
                if (newValue == null) {
                    return true;
                }

                // Compare column values with filter text.
                if (planification.getTacheBean().matcheDebut(newValue.format(TacheBean.DATE_FORMATTER))) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreEcheanceField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
            filteredData.setPredicate(planification -> {

                // If filter text is empty, display all data.
                if (newValue == null) {
                    return true;
                }

                // Compare column values with filter text.
                if (planification.getTacheBean().matcheEcheance(newValue.format(TacheBean.DATE_FORMATTER))) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreProjetsApplisField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(planification -> {
                            for (String codeProjetAppliSelectionne : comboBoxChange.getList()) {
                                if (planification.getTacheBean().matcheProjetAppli(codeProjetAppliSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreImportancesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(planification -> {
                            for (String codeImportanceSelectionne : comboBoxChange.getList()) {
                                if (planification.getTacheBean().matcheImportance(codeImportanceSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreRessourcesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(planification -> {
                            for (String codeRessourceSelectionne : comboBoxChange.getList()) {
                                if (planification.getTacheBean().matcheRessource(codeRessourceSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreProfilsField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(planification -> {
                            for (String codeProfilSelectionne : comboBoxChange.getList()) {
                                if (planification.getTacheBean().matcheProfil(codeProfilSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<PlanificationBean> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList COMPARATOR to the TableView COMPARATOR.
        sortedData.comparatorProperty().bind(planificationsTable.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        planificationsTable.setItems(sortedData);
    }

    private void definirNomsColonnesSemaine() {
        // TODO FDA 2017/04 Coder.
    }

    private void populerFiltreProjetsApplis() {

        List<String> codesProjetsApplisList = new ArrayList<>();
        codesProjetsApplisList.addAll(planificationsBeans.stream().map(planification -> planification.getTacheBean().getProjetAppli()).distinct().collect(Collectors.toList()));
        codesProjetsApplisList.sort(String::compareTo);

        filtreProjetsApplisField.getItems().clear();
        for (String codeProjetAppli : codesProjetsApplisList) {
            filtreProjetsApplisField.getItems().add(codeProjetAppli);
        }

        filtreProjetsApplisField.getCheckModel().checkAll();
    }

    private void populerFiltreImportances() {

        List<String> codesImportancesList = new ArrayList<>();
        codesImportancesList.addAll(planificationsBeans.stream().map(planification -> planification.getTacheBean().getImportance()).distinct().collect(Collectors.toList()));
        codesImportancesList.sort(String::compareTo);

        filtreImportancesField.getItems().clear();
        for (String codeImportance : codesImportancesList) {
            filtreImportancesField.getItems().add(codeImportance);
        }

        filtreImportancesField.getCheckModel().checkAll();
    }

    private void populerFiltreRessources() {

        List<String> codesRessourcesList = new ArrayList<>();
        codesRessourcesList.addAll(planificationsBeans.stream().map(planification -> planification.getTacheBean().getRessource()).distinct().collect(Collectors.toList()));
        codesRessourcesList.sort(String::compareTo);

        filtreRessourcesField.getItems().clear();
        for (String codeRessource : codesRessourcesList) {
            filtreRessourcesField.getItems().add(codeRessource);
        }

        filtreRessourcesField.getCheckModel().checkAll();
    }

    private void populerFiltreProfils() {

        List<String> codesProfilsList = new ArrayList<>();
        codesProfilsList.addAll(planificationsBeans.stream().map(planification -> planification.getTacheBean().getProfil()).distinct().collect(Collectors.toList()));
        codesProfilsList.sort(String::compareTo);

        filtreProfilsField.getItems().clear();
        for (String codeProfil : codesProfilsList) {
            filtreProfilsField.getItems().add(codeProfil);
        }

        filtreProfilsField.getCheckModel().checkAll();
    }

    @FXML
    private void razFiltres(ActionEvent event) {
        LOGGER.debug("RAZ des filtres...");
        filtreGlobalField.clear();
        filtreNoTacheField.clear();
        filtreNoTicketIdalField.clear();
        filtreDescriptionField.clear();
        filtreDebutField.setValue(null);
        filtreEcheanceField.setValue(null);
        filtreProjetsApplisField.getCheckModel().checkAll();
        filtreImportancesField.getCheckModel().checkAll();
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
    private void ajouterTache(ActionEvent event) {
        LOGGER.debug("ajouterTache...");

        if (planChargeBean.getDateEtat() == null) {
            LOGGER.warn("Impossible d'ajouter une tâche car la date d'état n'est pas définie.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Impossible d'ajouter une tâche");
            alert.setContentText("Impossible d'ajouter une tâche car la date d'état n'est pas définie. Précisez une date auparavant.");
            alert.showAndWait();
            return;
        }

        TacheBean t = new TacheBean(
                idTacheSuivant(),
                "(pas de ticket IDAL)",
                null,
                null,
                null,
                null,
                null,
                0.0,
                null,
                null
        );

        List<Pair<LocalDate, DoubleProperty>> calendrier = new ArrayList<>(Planifications.NBR_SEMAINES_PLANIFIEES);
        LocalDate dateSemaine = planChargeBean.getDateEtat();
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calendrier.add(new Pair(dateSemaine, new SimpleDoubleProperty(0.0)));
            dateSemaine = dateSemaine.plusDays(7);
        }

        PlanificationBean planifBean = new PlanificationBean(t, calendrier);
        planificationsBeans.add(planifBean);
    }

    private int idTacheSuivant() {
        OptionalInt max = planificationsBeans.stream().mapToInt(planifBean -> planifBean.getTacheBean().getId()).max();
        return (!max.isPresent()) ? 1 : (max.getAsInt() + 1);
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

        dateEtatPicker.setValue(dateEtat);

        ihm.definirDateEtat(dateEtat);
    }

}
