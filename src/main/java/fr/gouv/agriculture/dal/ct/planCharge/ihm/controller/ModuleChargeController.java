package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeApplication;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.PlanCharge;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ModuleChargeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleChargeController.class);

    private PlanChargeApplication application;

    @FXML
    private TableView<PlanificationBean> planificationTable;

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

/*
    @FXML
    private SplitPane splitPane;
*/

    private PlanChargeService planChargeService = new PlanChargeService();

    private ObservableList<PlanificationBean> lignesPlan;

    private ObservableList<String> codesImportancesLignesPlan;
    private ObservableList<String> codesProjetsApplisLignesPlan;
    private ObservableList<String> codesRessourcesLignesPlan;
    private ObservableList<String> codesProfilsLignesPlan;

    public void setApplication(PlanChargeApplication application) {
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
        // Custom rendering of the table cell:
        // Cf. http://code.makery.ch/blog/javafx-8-tableview-cell-renderer/
        chargeColumn.setCellFactory(column -> new TableCell<PlanificationBean, Double>() {
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

        // Chargement des lignes du plan de charge :

        // Paramétrage des cellules éditables :
/*
        La colonne "N° de tâche" n'est pas éditable (car c'est la "primaty key").
        noTacheColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
*/
        noTicketIdalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projetAppliColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesProjetsApplisLignesPlan));
        debutColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter())); // TODO FDA 2017/04 Ne permettre de saisir qu'une date (DatePicker), plutôt qu'un string.
        echeanceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter())); // TODO FDA 2017/04 Ne permettre de saisir qu'une date (DatePicker), plutôt qu'un string.
        importanceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesImportancesLignesPlan));
        chargeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        ressourceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesRessourcesLignesPlan));
        profilColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesProfilsLignesPlan));

        populePlan();
    }

    private void populePlan() {

        lignesPlan = FXCollections.observableArrayList();

        codesImportancesLignesPlan = FXCollections.observableArrayList();
        codesProjetsApplisLignesPlan = FXCollections.observableArrayList();
        codesRessourcesLignesPlan = FXCollections.observableArrayList();
        codesProfilsLignesPlan = FXCollections.observableArrayList();

        lignesPlan.addListener((ListChangeListener<? super PlanificationBean>) changeListener -> {
            codesImportancesLignesPlan.clear();
            codesImportancesLignesPlan.addAll(changeListener.getList().stream().map(planifBean -> planifBean.getTache().getImportance()).collect(Collectors.toSet()));

            codesProjetsApplisLignesPlan.clear();
            codesProjetsApplisLignesPlan.addAll(changeListener.getList().stream().map(planifBean -> planifBean.getTache().getProjetAppli()).collect(Collectors.toSet()));

            codesRessourcesLignesPlan.clear();
            codesRessourcesLignesPlan.addAll(changeListener.getList().stream().map(planifBean -> planifBean.getTache().getRessource()).collect(Collectors.toSet()));

            codesProfilsLignesPlan.clear();
            codesProfilsLignesPlan.addAll(changeListener.getList().stream().map(planifBean -> planifBean.getTache().getProfil()).collect(Collectors.toSet()));
        });

        PlanCharge planCharge = planChargeService.load(LocalDate.of(2016, 11, 28));
        lignesPlan.addAll(planCharge.getPlanification().taches().stream().map(tache -> new PlanificationBean(new TacheBean(tache))).collect(Collectors.toList()));

        populeFiltreProjetsApplis();
        populeFiltreImportances();
        populeFiltreRessources();
        populeFiltreProfils();

        // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
        // 1. Wrap the ObservableList in a FilteredList
        FilteredList<PlanificationBean> filteredData = new FilteredList<>(lignesPlan);
        // 2. Set the filter Predicate whenever the filter changes.
        filtreGlobalField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(lignePlanif -> {

                    // If filter text is empty, display all data.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (lignePlanif.matcheNoTache(newValue)) {
                        return true; // Filter matches
                    }
                    if (lignePlanif.matcheNoTicketIdal(newValue)) {
                        return true; // Filter matches
                    }
                    if (lignePlanif.matcheDescription(newValue)) {
                        return true; // Filter matches
                    }
                    if (lignePlanif.matcheProjetAppli(newValue)) {
                        return true; // Filter matches
                    }
                    if (lignePlanif.matcheImportance(newValue)) {
                        return true; // Filter matches
                    }
                    if (lignePlanif.matcheDebut(newValue)) {
                        return true; // Filter matches
                    }
                    if (lignePlanif.matcheEcheance(newValue)) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                })
        );
        filtreNoTacheField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lignePlanif -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (lignePlanif.matcheNoTache(lowerCaseFilter)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreNoTicketIdalField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lignePlanif -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (lignePlanif.matcheNoTicketIdal(lowerCaseFilter)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lignePlanif -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (lignePlanif.matcheDescription(lowerCaseFilter)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreDebutField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
            filteredData.setPredicate(lignePlanif -> {

                // If filter text is empty, display all data.
                if (newValue == null) {
                    return true;
                }

                // Compare column values with filter text.
                if (lignePlanif.matcheDebut(newValue.format(PlanificationBean.DATE_FORMATTER))) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
//        filtreEcheanceField TODO FDA 2017/04
        filtreProjetsApplisField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(lignePlanif -> {
                            for (String codeProjetAppliSelectionne : comboBoxChange.getList()) {
                                if (lignePlanif.matcheProjetAppli(codeProjetAppliSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreImportancesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(lignePlanif -> {
                            for (String codeImportanceSelectionne : comboBoxChange.getList()) {
                                if (lignePlanif.matcheImportance(codeImportanceSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreRessourcesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(lignePlanif -> {
                            for (String codeRessourceSelectionne : comboBoxChange.getList()) {
                                if (lignePlanif.matcheRessource(codeRessourceSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreProfilsField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(lignePlanif -> {
                            for (String codeProfilSelectionne : comboBoxChange.getList()) {
                                if (lignePlanif.matcheProfil(codeProfilSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<PlanificationBean> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(planificationTable.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        planificationTable.setItems(sortedData);
    }

    private void populeFiltreProjetsApplis() {

        Set<String> codesProjetsApplisSet = new HashSet<>();
        for (PlanificationBean planificationBean : lignesPlan) {
            codesProjetsApplisSet.add(planificationBean.getTache().getProjetAppli());
        }

        List<String> codesProjetsApplisList = Arrays.asList(codesProjetsApplisSet.toArray(new String[0]));
        Collections.sort(codesProjetsApplisList);

        filtreProjetsApplisField.getItems().clear();
        for (String codeProjetAppli : codesProjetsApplisList) {
            filtreProjetsApplisField.getItems().add(codeProjetAppli);
        }

        filtreProjetsApplisField.getCheckModel().checkAll();
    }

    private void populeFiltreImportances() {

        Set<String> codesImportancesSet = new HashSet<>();
        for (PlanificationBean planificationBean : lignesPlan) {
            codesImportancesSet.add(planificationBean.getTache().getImportance());
        }

        List<String> codesImportancesList = Arrays.asList(codesImportancesSet.toArray(new String[0]));
        Collections.sort(codesImportancesList);

        filtreImportancesField.getItems().clear();
        for (String codeImportance : codesImportancesList) {
            filtreImportancesField.getItems().add(codeImportance);
        }

        filtreImportancesField.getCheckModel().checkAll();
    }

    private void populeFiltreRessources() {

        Set<String> codesRessourcesSet = new HashSet<>();
        for (PlanificationBean planificationBean : lignesPlan) {
            codesRessourcesSet.add(planificationBean.getTache().getRessource());
        }

        List<String> codesRessourcesList = Arrays.asList(codesRessourcesSet.toArray(new String[0]));
        Collections.sort(codesRessourcesList);

        filtreRessourcesField.getItems().clear();
        for (String codeRessource : codesRessourcesList) {
            filtreRessourcesField.getItems().add(codeRessource);
        }

        filtreRessourcesField.getCheckModel().checkAll();
    }


    private void populeFiltreProfils() {

        Set<String> codesProfilsSet = new HashSet<>();
        for (PlanificationBean planificationBean : lignesPlan) {
            codesProfilsSet.add(planificationBean.getTache().getProfil());
        }

        List<String> codesProfilsList = Arrays.asList(codesProfilsSet.toArray(new String[0]));
        Collections.sort(codesProfilsList);

        filtreProfilsField.getItems().clear();
        for (String codeProfil : codesProfilsList) {
            filtreProfilsField.getItems().add(codeProfil);
        }

        filtreProfilsField.getCheckModel().checkAll();
    }

    @FXML
    private void razFiltres(ActionEvent event) {
        LOGGER.debug("RAZ des filtres...");
        filtreNoTacheField.clear();
        filtreNoTicketIdalField.clear();
        filtreDescriptionField.clear();
        filtreDebutField.setValue(null);
        filtreEcheanceField.setValue(null);
        filtreProjetsApplisField.getCheckModel().checkAll();
        filtreImportancesField.getCheckModel().checkAll();
        filtreProfilsField.getCheckModel().checkAll();
    }

}
