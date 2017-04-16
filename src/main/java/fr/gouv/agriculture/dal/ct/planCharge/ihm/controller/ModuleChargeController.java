package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LocalDateStringConverter;
import org.controlsfx.control.CheckComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleChargeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleChargeController.class);

    private PlanChargeIhm application;

    // Les tables :

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

    // Les services métier :

    private PlanChargeService planChargeService = new PlanChargeService();

    // Les collections de données :

    private ObservableList<PlanificationBean> planifications = FXCollections.observableArrayList();

    private ObservableList<String> codesImportancesTaches = FXCollections.observableArrayList();
    private ObservableList<String> codesProjetsApplisTaches = FXCollections.observableArrayList();
    private ObservableList<String> codesRessourcesTaches = FXCollections.observableArrayList();
    private ObservableList<String> codesProfilsTaches = FXCollections.observableArrayList();

    public void setApplication(PlanChargeIhm application) {
        this.application = application;
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

/*
        // Rendre le "divider" du SplitPane fixe (== "non draggable") :
        // (Cf. http://stackoverflow.com/questions/26762928/javafx-disable-divider)
        splitPane.lookupAll(".split-pane-divider").stream().forEach(div ->  div.setMouseTransparent(true) );
*/

        // Formattage des données des cellules (en rendu "non éditable") :
        noTacheColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().noTacheProperty());
        noTicketIdalColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().noTicketIdalProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().descriptionProperty());
        projetAppliColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().projetAppliProperty());
        debutColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().debutProperty());
        echeanceColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().echeanceProperty());
        importanceColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().importanceProperty());
        chargeColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().chargeProperty().asObject());
        ressourceColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().ressourceProperty());
        profilColumn.setCellValueFactory(cellData -> cellData.getValue().getTache().profilProperty());
        semaine1Column.setCellValueFactory(cellData -> (cellData.getValue().charge(1).asObject()));
        semaine2Column.setCellValueFactory(cellData -> (cellData.getValue().charge(2).asObject()));
        semaine3Column.setCellValueFactory(cellData -> (cellData.getValue().charge(3).asObject()));
        semaine4Column.setCellValueFactory(cellData -> (cellData.getValue().charge(4).asObject()));
        semaine5Column.setCellValueFactory(cellData -> (cellData.getValue().charge(5).asObject()));
        semaine6Column.setCellValueFactory(cellData -> (cellData.getValue().charge(6).asObject()));
        semaine7Column.setCellValueFactory(cellData -> (cellData.getValue().charge(7).asObject()));
        semaine8Column.setCellValueFactory(cellData -> (cellData.getValue().charge(8).asObject()));
        semaine9Column.setCellValueFactory(cellData -> (cellData.getValue().charge(9).asObject()));
        semaine10Column.setCellValueFactory(cellData -> (cellData.getValue().charge(10).asObject()));
        semaine11Column.setCellValueFactory(cellData -> (cellData.getValue().charge(11).asObject()));
        semaine12Column.setCellValueFactory(cellData -> (cellData.getValue().charge(12).asObject()));
        //
        // Custom rendering of the table cell:
        // Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/
        chargeColumn.setCellFactory(column -> new TableCell<PlanificationBean, Double>() {
            //            TODO FDA 2017/04 Terminer.
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    // Format.
                    setText(item.toString());

                    // Style with a different color.
                    String styleBackgroundColour;
                    if (item < 10.0) {
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

/*
        // Cf. http://stackoverflow.com/questions/23789525/onscroll-listener-does-not-working-in-tableview-in-javafx-2
        tachesTable.addEventFilter(ScrollEvent.ANY, event -> tachesScroll(event));
*/

        // Chargement des lignes du plan de charge :
        populePlan();
    }

    private void populePlan()  {
        planifications.addListener((ListChangeListener<? super PlanificationBean>) changeListener -> {
            codesImportancesTaches.clear();
            codesImportancesTaches.addAll(changeListener.getList().stream().map(planification -> planification.getTache().getImportance()).collect(Collectors.toSet()));
            codesImportancesTaches.sort(String::compareTo);

            codesProjetsApplisTaches.clear();
            codesProjetsApplisTaches.addAll(changeListener.getList().stream().map(planification -> planification.getTache().getProjetAppli()).collect(Collectors.toSet()));
            codesProjetsApplisTaches.sort(String::compareTo);

            codesRessourcesTaches.clear();
            codesRessourcesTaches.addAll(changeListener.getList().stream().map(planification -> planification.getTache().getRessource()).collect(Collectors.toSet()));
            codesRessourcesTaches.sort(String::compareTo);

            codesProfilsTaches.clear();
            codesProfilsTaches.addAll(changeListener.getList().stream().map(planification -> planification.getTache().getProfil()).collect(Collectors.toSet()));
            codesProfilsTaches.sort(String::compareTo);
        });

        PlanCharge planCharge = planChargeService.load(LocalDate.of(2016, 11, 28));
        planifications.addAll(
                planCharge.getPlanification().taches()
                        .stream()
                        .map(tache -> {
                            try {
                                return new PlanificationBean(tache, planCharge.getPlanification().planification(tache));
                            } catch (TacheSansPlanificationException e) {
                                throw new ControllerException("Impossible de définir le plan de charge.", e);
                            }
                        })
                        .collect(Collectors.toList())
        );

        populeFiltreProjetsApplis();
        populeFiltreImportances();
        populeFiltreRessources();
        populeFiltreProfils();

        // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
        // 1. Wrap the ObservableList in a FilteredList
        FilteredList<PlanificationBean> filteredData = new FilteredList<>(planifications);
        // 2. Set the filter Predicate whenever the filter changes.
        filtreGlobalField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(planification -> {

                    // If filter text is empty, display all data.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (planification.getTache().matcheNoTache(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTache().matcheNoTicketIdal(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTache().matcheDescription(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTache().matcheProjetAppli(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTache().matcheImportance(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTache().matcheDebut(newValue)) {
                        return true; // Filter matches
                    }
                    if (planification.getTache().matcheEcheance(newValue)) {
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
                if (planification.getTache().matcheNoTache(lowerCaseFilter)) {
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
                if (planification.getTache().matcheNoTicketIdal(lowerCaseFilter)) {
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
                if (planification.getTache().matcheDescription(lowerCaseFilter)) {
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
                if (planification.getTache().matcheDebut(newValue.format(TacheBean.DATE_FORMATTER))) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
//        filtreEcheanceField TODO FDA 2017/04
        filtreProjetsApplisField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(planification -> {
                            for (String codeProjetAppliSelectionne : comboBoxChange.getList()) {
                                if (planification.getTache().matcheProjetAppli(codeProjetAppliSelectionne)) {
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
                                if (planification.getTache().matcheImportance(codeImportanceSelectionne)) {
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
                                if (planification.getTache().matcheRessource(codeRessourceSelectionne)) {
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
                                if (planification.getTache().matcheProfil(codeProfilSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<PlanificationBean> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(planificationsTable.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        planificationsTable.setItems(sortedData);
    }

    private void populeFiltreProjetsApplis() {

        List<String> codesProjetsApplisList = new ArrayList<>();
        codesProjetsApplisList.addAll(planifications.stream().map(planification -> planification.getTache().getProjetAppli()).distinct().collect(Collectors.toList()));
        codesProjetsApplisList.sort(String::compareTo);

        filtreProjetsApplisField.getItems().clear();
        for (String codeProjetAppli : codesProjetsApplisList) {
            filtreProjetsApplisField.getItems().add(codeProjetAppli);
        }

        filtreProjetsApplisField.getCheckModel().checkAll();
    }

    private void populeFiltreImportances() {

        List<String> codesImportancesList = new ArrayList<>();
        codesImportancesList.addAll(planifications.stream().map(planification -> planification.getTache().getImportance()).distinct().collect(Collectors.toList()));
        codesImportancesList.sort(String::compareTo);

        filtreImportancesField.getItems().clear();
        for (String codeImportance : codesImportancesList) {
            filtreImportancesField.getItems().add(codeImportance);
        }

        filtreImportancesField.getCheckModel().checkAll();
    }

    private void populeFiltreRessources() {

        List<String> codesRessourcesList = new ArrayList<>();
        codesRessourcesList.addAll(planifications.stream().map(planification -> planification.getTache().getRessource()).distinct().collect(Collectors.toList()));
        codesRessourcesList.sort(String::compareTo);

        filtreRessourcesField.getItems().clear();
        for (String codeRessource : codesRessourcesList) {
            filtreRessourcesField.getItems().add(codeRessource);
        }

        filtreRessourcesField.getCheckModel().checkAll();
    }

    private void populeFiltreProfils() {

        List<String> codesProfilsList = new ArrayList<>();
        codesProfilsList.addAll(planifications.stream().map(planification -> planification.getTache().getProfil()).distinct().collect(Collectors.toList()));
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
}
