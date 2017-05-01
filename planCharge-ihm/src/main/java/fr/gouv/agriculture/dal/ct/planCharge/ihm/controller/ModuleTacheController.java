package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.PlanChargeService;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
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
public class ModuleTacheController extends AbstractTachesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleTacheController.class);

    // La table de planification des tâches :
    @FXML
    private TableView<TacheBean> tachesTable;
    @FXML
    private TableColumn<TacheBean, String> noTacheColumn;
    @FXML
    private TableColumn<TacheBean, String> noTicketIdalColumn;
    @FXML
    private TableColumn<TacheBean, String> descriptionColumn;
    @FXML
    private TableColumn<TacheBean, String> projetAppliColumn;
    @FXML
    private TableColumn<TacheBean, LocalDate> debutColumn;
    @FXML
    private TableColumn<TacheBean, LocalDate> echeanceColumn;
    @FXML
    private TableColumn<TacheBean, Importance> importanceColumn;
    @FXML
    private TableColumn<TacheBean, Double> chargeColumn;
    @FXML
    private TableColumn<TacheBean, String> ressourceColumn;
    @FXML
    private TableColumn<TacheBean, String> profilColumn;

    // Les filtres :
    // TODO FDA 2017/05 Ajouter les filtres spécifiques des charges : semaine chargée, reste à planifier <> 0, planifiée dans le mois qui vient, etc.

    // Les services métier :
    private PlanChargeService planChargeService;
    public void setPlanChargeService(PlanChargeService planChargeService) {
        this.planChargeService = planChargeService;
    }

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public ModuleTacheController() {
        super();
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        // Formattage des données des cellules (en rendu "non éditable") :
        noTacheColumn.setCellValueFactory(cellData -> cellData.getValue().noTacheProperty());
        noTicketIdalColumn.setCellValueFactory(cellData -> cellData.getValue().noTicketIdalProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        projetAppliColumn.setCellValueFactory(cellData -> cellData.getValue().projetAppliProperty());
        debutColumn.setCellValueFactory(cellData -> cellData.getValue().debutProperty());
        echeanceColumn.setCellValueFactory(cellData -> cellData.getValue().echeanceProperty());
        importanceColumn.setCellValueFactory(cellData -> cellData.getValue().importanceProperty());
        chargeColumn.setCellValueFactory(cellData -> cellData.getValue().chargeProperty().asObject());
        ressourceColumn.setCellValueFactory(cellData -> cellData.getValue().ressourceProperty());
        profilColumn.setCellValueFactory(cellData -> cellData.getValue().profilProperty());

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

        // Paramétrage des ordres de tri :
        importanceColumn.setComparator(Importance::compareTo);

/*
        // Cf. http://stackoverflow.com/questions/23789525/onscroll-listener-does-not-working-in-tableview-in-javafx-2
        tachesTable.addEventFilter(ScrollEvent.ANY, event -> tachesScroll(event));
*/

        // Chargement des lignes du plan de charge :
        populerTaches();
    }

    private void populerTaches() {
        taches.addListener((ListChangeListener<? super TacheBean>) changeListener -> {
            importancesTaches.setAll(changeListener.getList().stream().map(tache -> tache.getImportance()).sorted(Importance::compareTo).collect(Collectors.toSet()));
            codesProjetsApplisTaches.setAll(changeListener.getList().stream().map(tache -> tache.getProjetAppli()).sorted(String::compareTo).collect(Collectors.toSet()));
            codesRessourcesTaches.setAll(changeListener.getList().stream().map(tache -> tache.getRessource()).sorted(String::compareTo).collect(Collectors.toSet()));
            codesProfilsTaches.setAll(changeListener.getList().stream().map(tache -> tache.getProfil()).sorted(String::compareTo).collect(Collectors.toSet()));
        });

        populerFiltreProjetsApplis();
        populerFiltreImportances();
        populerFiltreRessources();
        populerFiltreProfils();

        // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
        // 1. Wrap the ObservableList in a FilteredList
        FilteredList<TacheBean> filteredData = new FilteredList<>(taches);
        // 2. Set the filter Predicate whenever the filter changes.
        filtreGlobalField.textProperty().addListener((observable, oldValue, newValue) ->
                filteredData.setPredicate(tache -> {

                    // If filter text is empty, display all data.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (tache.matcheNoTache(newValue)) {
                        return true; // Filter matches
                    }
                    if (tache.matcheNoTicketIdal(newValue)) {
                        return true; // Filter matches
                    }
                    if (tache.matcheDescription(newValue)) {
                        return true; // Filter matches
                    }
                    if (tache.matcheProjetAppli(newValue)) {
                        return true; // Filter matches
                    }
                    if (tache.matcheImportance(newValue)) {
                        return true; // Filter matches
                    }
                    if (tache.matcheDebut(newValue)) {
                        return true; // Filter matches
                    }
                    if (tache.matcheEcheance(newValue)) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                })
        );
        filtreNoTacheField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (tache.matcheNoTache(lowerCaseFilter)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreNoTicketIdalField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (tache.matcheNoTicketIdal(lowerCaseFilter)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (tache.matcheDescription(lowerCaseFilter)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreDebutField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
            filteredData.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if (newValue == null) {
                    return true;
                }

                // Compare column values with filter text.
                if (tache.matcheDebut(newValue.format(TacheBean.DATE_FORMATTER))) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreEcheanceField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
            filteredData.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if (newValue == null) {
                    return true;
                }

                // Compare column values with filter text.
                if (tache.matcheEcheance(newValue.format(TacheBean.DATE_FORMATTER))) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreProjetsApplisField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(tache -> {
                            for (String codeProjetAppliSelectionne : comboBoxChange.getList()) {
                                if (tache.matcheProjetAppli(codeProjetAppliSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreImportancesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(tache -> {
                            for (String codeImportanceSelectionne : comboBoxChange.getList()) {
                                if (tache.matcheImportance(codeImportanceSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreRessourcesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(tache -> {
                            for (String codeRessourceSelectionne : comboBoxChange.getList()) {
                                if (tache.matcheRessource(codeRessourceSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreProfilsField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(tache -> {
                            for (String codeProfilSelectionne : comboBoxChange.getList()) {
                                if (tache.matcheProfil(codeProfilSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<TacheBean> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList COMPARATOR to the TableView COMPARATOR.
        sortedData.comparatorProperty().bind(tachesTable.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tachesTable.setItems(sortedData);
    }

    private void populerFiltreProjetsApplis() {

        List<String> codesProjetsApplisList = new ArrayList<>();
        codesProjetsApplisList.addAll(taches.stream().map(tache -> tache.getProjetAppli()).distinct().collect(Collectors.toList()));
        codesProjetsApplisList.sort(String::compareTo);

        filtreProjetsApplisField.getItems().clear();
        for (String codeProjetAppli : codesProjetsApplisList) {
            filtreProjetsApplisField.getItems().add(codeProjetAppli);
        }

        filtreProjetsApplisField.getCheckModel().checkAll();
    }

    private void populerFiltreImportances() {

        List<String> codesImportancesList = new ArrayList<>();
        codesImportancesList.addAll(taches.stream().map(tache -> tache.getImportance().getCode()).distinct().collect(Collectors.toList()));
        codesImportancesList.sort(String::compareTo);

        filtreImportancesField.getItems().clear();
        for (String codeImportance : codesImportancesList) {
            filtreImportancesField.getItems().add(codeImportance);
        }

        filtreImportancesField.getCheckModel().checkAll();
    }

    private void populerFiltreRessources() {

        List<String> codesRessourcesList = new ArrayList<>();
        codesRessourcesList.addAll(taches.stream().map(tache -> tache.getRessource()).distinct().collect(Collectors.toList()));
        codesRessourcesList.sort(String::compareTo);

        filtreRessourcesField.getItems().clear();
        for (String codeRessource : codesRessourcesList) {
            filtreRessourcesField.getItems().add(codeRessource);
        }

        filtreRessourcesField.getCheckModel().checkAll();
    }

    private void populerFiltreProfils() {

        List<String> codesProfilsList = new ArrayList<>();
        codesProfilsList.addAll(taches.stream().map(tache -> tache.getProfil()).distinct().collect(Collectors.toList()));
        codesProfilsList.sort(String::compareTo);

        filtreProfilsField.getItems().clear();
        for (String codeProfil : codesProfilsList) {
            filtreProfilsField.getItems().add(codeProfil);
        }

        filtreProfilsField.getCheckModel().checkAll();
    }

}
