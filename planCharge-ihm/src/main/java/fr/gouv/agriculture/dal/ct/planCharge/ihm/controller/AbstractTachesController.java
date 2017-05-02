package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.CodeImportanceComparator;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
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

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.OptionalInt;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 01/05/2017.
 */
public abstract class AbstractTachesController<TB extends TacheBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTachesController.class);

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    // Les beans :
    @NotNull
    private ObservableList<TB> tachesBeans;
    @NotNull
    private ObservableList<String> codesCategoriesTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesSousCategoriesTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> importancesTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesProjetsApplisTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesRessourcesTaches = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesProfilsTaches = FXCollections.observableArrayList();

    @FXML
    private TableView<TB> tachesTable;
    @FXML
    private TableColumn<TB, String> categorieColumn;
    @FXML
    private TableColumn<TB, String> sousCategorieColumn;
    @FXML
    private TableColumn<TB, String> noTacheColumn;
    @FXML
    private TableColumn<TB, String> noTicketIdalColumn;
    @FXML
    private TableColumn<TB, String> descriptionColumn;
    @FXML
    private TableColumn<TB, String> projetAppliColumn;
    @FXML
    private TableColumn<TB, LocalDate> debutColumn;
    @FXML
    private TableColumn<TB, LocalDate> echeanceColumn;
    @FXML
    private TableColumn<TB, String> importanceColumn;
    @FXML
    private TableColumn<TB, Double> chargeColumn;
    @FXML
    private TableColumn<TB, String> ressourceColumn;
    @FXML
    private TableColumn<TB, String> profilColumn;

    // Les filtres :
    @FXML
    protected TextField filtreGlobalField;
    @FXML
    protected CheckComboBox<String> filtreCategoriesField;
    @FXML
    protected CheckComboBox<String> filtreSousCategoriesField;
    @FXML
    protected TextField filtreNoTacheField;
    @FXML
    protected TextField filtreNoTicketIdalField;
    @FXML
    protected TextField filtreDescriptionField;
    @FXML
    protected CheckComboBox<String> filtreProjetsApplisField;
    @FXML
    protected DatePicker filtreDebutField;
    @FXML
    protected DatePicker filtreEcheanceField;
    @FXML
    protected CheckComboBox<String> filtreImportancesField;
    @FXML
    protected TextField filtreChargeField;
    @FXML
    protected CheckComboBox<String> filtreRessourcesField;
    @FXML
    protected CheckComboBox<String> filtreProfilsField;

    protected ObservableList<TB> getTachesBeans() {
        return tachesBeans;
    }

    protected void setTachesBeans(ObservableList<TB> tachesBeans) {
        this.tachesBeans = tachesBeans;
    }

    protected TableView<TB> getTachesTable() {
        return tachesTable;
    }

    TableColumn<TB, Double> getChargeColumn() {
        return chargeColumn;
    }

    @FXML
    protected void ajouterTache(ActionEvent event) {
        TB nouvTache;
        LOGGER.debug("ajouterTache...");
        nouvTache = nouveauBean();
        tachesBeans.add(nouvTache);
//        return nouvTache;
    }

    abstract TB nouveauBean();

    int idTacheSuivant() {
        OptionalInt max = tachesBeans.stream().mapToInt(tacheBean -> tacheBean.getId()).max();
        return (!max.isPresent()) ? 1 : (max.getAsInt() + 1);
    }

    @FXML
    protected void razFiltres(ActionEvent event) {
        LOGGER.debug("RAZ des filtres...");
        filtreGlobalField.clear();
        filtreCategoriesField.getCheckModel().checkAll();
        filtreSousCategoriesField.getCheckModel().checkAll();
        filtreNoTacheField.clear();
        filtreNoTicketIdalField.clear();
        filtreDescriptionField.clear();
        filtreDebutField.setValue(null);
        filtreEcheanceField.setValue(null);
        filtreProjetsApplisField.getCheckModel().checkAll();
        filtreImportancesField.getCheckModel().checkAll();
        filtreProfilsField.getCheckModel().checkAll();
    }

    void initialize() {

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        categorieColumn.setCellValueFactory(cellData -> cellData.getValue().categorieProperty());
        sousCategorieColumn.setCellValueFactory(cellData -> cellData.getValue().sousCategorieProperty());
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

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        categorieColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesCategoriesTaches));
        sousCategorieColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesSousCategoriesTaches));
        // Rq : La colonne "N° de tâche" n'est pas éditable (car c'est la "primaty key").
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
        importanceColumn.setComparator(CodeImportanceComparator.COMPARATEUR);

        populerFiltreCategories();
        populerFiltreSousCategories();
        populerFiltreProjetsApplis();
        populerFiltreImportances();
        populerFiltreRessources();
        populerFiltreProfils();

        tachesBeans.addListener((ListChangeListener<TB>) changeListener -> {

            // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
            // 1. Wrap the ObservableList in a FilteredList
            FilteredList<TB> filteredTaches = new FilteredList<>(tachesBeans);
            // 2. Set the filter Predicate whenever the filter changes.
            filtreGlobalField.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredTaches.setPredicate(tache -> {

                        // If filter text is empty, display all data.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare column values with filter text.
                        if (tache.matcheCategorie(newValue)) {
                            return true; // Filter matches
                        }
                        if (tache.matcheSousCategorie(newValue)) {
                            return true; // Filter matches
                        }
                        if (tache.matcheNoTache(newValue)) {
                            return true; // Filter matches
                        }
                        if (!tache.noTicketIdalProperty().isEmpty().get() && tache.matcheNoTicketIdal(newValue)) {
                            return true; // Filter matches
                        }
                        if (!tache.descriptionProperty().isEmpty().get() && tache.matcheDescription(newValue)) {
                            return true; // Filter matches
                        }
                        if (!tache.debutProperty().isNull().get() && tache.matcheDebut(newValue)) {
                            return true; // Filter matches
                        }
                        if (!tache.echeanceProperty().isNull().get() && tache.matcheEcheance(newValue)) {
                            return true; // Filter matches
                        }
                        if (!tache.projetAppliProperty().isEmpty().get() && tache.matcheProjetAppli(newValue)) {
                            return true; // Filter matches
                        }
                        if (!tache.importanceProperty().isNull().get() && tache.matcheImportance(newValue)) {
                            return true; // Filter matches
                        }
                        if (!tache.ressourceProperty().isEmpty().get() && tache.matcheRessource(newValue)) {
                            return true; // Filter matches
                        }
                        if (!tache.profilProperty().isEmpty().get() && tache.matcheProfil(newValue)) {
                            return true; // Filter matches
                        }
                        return false; // Does not match.
                    })
            );
            filtreCategoriesField.getCheckModel().getCheckedItems().addListener(
                    (ListChangeListener<String>) change -> {
//                    change.reset();
                        filteredTaches.setPredicate(tache -> {
                            for (String categorieSelectionnee : change.getList()) {
                                if (!tache.categorieProperty().isEmpty().get() && tache.matcheCategorie(categorieSelectionnee)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
            );
            filtreSousCategoriesField.getCheckModel().getCheckedItems().addListener(
                    (ListChangeListener<String>) change -> {
//                    change.reset();
                        filteredTaches.setPredicate(tache -> {
                            for (String sousCategorieSelectionnee : change.getList()) {
                                if (!tache.sousCategorieProperty().isEmpty().get() && tache.matcheSousCategorie(sousCategorieSelectionnee)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
            );
            filtreNoTicketIdalField.textProperty().addListener((observable, oldValue, newValue) ->
                    filteredTaches.setPredicate(tache -> {

                        // If filter text is empty, display all data.
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }

                        // Compare column values with filter text.
                        if (!tache.noTicketIdalProperty().isEmpty().get() && tache.matcheNoTicketIdal(newValue)) {
                            return true; // Filter matches
                        }
                        return false; // Does not match.
                    })
            );
            filtreDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue != null && !newValue.isEmpty()) {
                    try {
                        Pattern.compile(newValue);
                    } catch (PatternSyntaxException e) {
/*
                        ihm.afficherPopUp(
                                Alert.AlertType.ERROR,
                                "Valeur incorrecte",
                                "Expression régulière incorrecte : '" + newValue + "'.",
                                400, 200
                        );
                        filtreDescriptionField.clear();
*/
                        return;
                    }
                }

                filteredTaches.setPredicate(tache -> {

                    // If filter text is empty, display all data.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (!tache.descriptionProperty().isEmpty().get() && tache.matcheDescription(newValue)) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                });
            });
            filtreDebutField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
                filteredTaches.setPredicate(tache -> {

                    // If filter text is empty, display all data.
                    if (newValue == null) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (!tache.debutProperty().isNull().get() && tache.matcheDebut(newValue.format(TacheBean.DATE_FORMATTER))) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                });
            });
            filtreEcheanceField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
                filteredTaches.setPredicate(tache -> {

                    // If filter text is empty, display all data.
                    if (newValue == null) {
                        return true;
                    }

                    // Compare column values with filter text.
                    if (!tache.echeanceProperty().isNull().get() && tache.matcheEcheance(newValue.format(TacheBean.DATE_FORMATTER))) {
                        return true; // Filter matches
                    }
                    return false; // Does not match.
                });
            });
            filtreProjetsApplisField.getCheckModel().getCheckedItems().addListener(
                    (ListChangeListener<String>) change -> {
//                    change.reset();
                        filteredTaches.setPredicate(tache -> {
                            for (String codeProjetAppliSelectionne : change.getList()) {
                                if (!tache.projetAppliProperty().isEmpty().get() && tache.matcheProjetAppli(codeProjetAppliSelectionne)) {
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
                        filteredTaches.setPredicate(tache -> {
                            for (String codeImportanceSelectionne : change.getList()) {
                                if (!tache.importanceProperty().isNull().get() && tache.matcheImportance(codeImportanceSelectionne)) {
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
                        filteredTaches.setPredicate(tache -> {
                            for (String codeRessourceSelectionne : change.getList()) {
                                if (!tache.ressourceProperty().isEmpty().get() && tache.matcheRessource(codeRessourceSelectionne)) {
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
                        filteredTaches.setPredicate(tache -> {
                            for (String codeProfilSelectionne : change.getList()) {
                                if (!tache.profilProperty().isEmpty().get() && tache.matcheProfil(codeProfilSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
            );
            // 3. Wrap the FilteredList in a SortedList.
            SortedList<TB> sortedPlanifBeans = new SortedList<>(filteredTaches);
            // 4. Bind the SortedList COMPARATOR_DEFAUT to the TableView COMPARATOR_DEFAUT.
            sortedPlanifBeans.comparatorProperty().bind(tachesTable.comparatorProperty());
            // 5. Add sorted (and filtered) data to the table.
            getTachesTable().setItems(sortedPlanifBeans);
        });

        tachesBeans.addListener((ListChangeListener<TB>) changeListener -> {
            // TODO FDA 2017/04 Alimenter ces listes avec les référentiels, plutôt.
            codesCategoriesTaches.setAll(changeListener.getList().stream().map(TacheBean::getCategorie).distinct().sorted(String::compareTo).collect(Collectors.toSet()));
            codesSousCategoriesTaches.setAll(changeListener.getList().stream().filter(o -> !o.sousCategorieProperty().isEmpty().get()).map(TacheBean::getSousCategorie).distinct().sorted(String::compareTo).collect(Collectors.toSet()));
            importancesTaches.setAll(changeListener.getList().stream().map(TacheBean::getImportance).distinct().sorted(CodeImportanceComparator.COMPARATEUR).collect(Collectors.toSet()));
            codesProjetsApplisTaches.setAll(changeListener.getList().stream().map(TacheBean::getProjetAppli).distinct().sorted(String::compareTo).collect(Collectors.toSet()));
            codesRessourcesTaches.setAll(changeListener.getList().stream().map(TacheBean::getRessource).distinct().sorted(String::compareTo).collect(Collectors.toSet()));
            codesProfilsTaches.setAll(changeListener.getList().stream().map(TacheBean::getProfil).distinct().sorted(String::compareTo).collect(Collectors.toSet()));
        });

        // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
        // 1. Wrap the ObservableList in a FilteredList
        FilteredList<TB> filteredData = new FilteredList<>(tachesBeans);
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
        filtreCategoriesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(tache -> {
                            for (String codeCategorieSelectionne : comboBoxChange.getList()) {
                                if (tache.matcheCategorie(codeCategorieSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreSousCategoriesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) comboBoxChange ->
                        filteredData.setPredicate(tache -> {
                            for (String codeSousCategorieSelectionne : comboBoxChange.getList()) {
                                if (tache.matcheSousCategorie(codeSousCategorieSelectionne)) {
                                    return true;
                                }
                            }
                            return false;
                        })
        );
        filtreNoTacheField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                if (tache.matcheNoTache(newValue)) {
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
                if (tache.matcheNoTicketIdal(newValue)) {
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
                if (tache.matcheDescription(newValue)) {
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
        SortedList<TB> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList COMPARATOR_DEFAUT to the TableView COMPARATOR_DEFAUT.
        sortedData.comparatorProperty().bind(tachesTable.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tachesTable.setItems(sortedData);
    }

    private void populerFiltreCategories() {
        filtreCategoriesField.getItems().setAll(
                tachesBeans.stream()
                        .filter(tacheBean -> (tacheBean.getCategorie() != null))
                        .map(tacheBean -> tacheBean.getCategorie())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreCategoriesField.getCheckModel().checkAll();
    }

    private void populerFiltreSousCategories() {
        filtreSousCategoriesField.getItems().setAll(
                tachesBeans.stream()
                        .filter(tacheBean -> (tacheBean.getSousCategorie() != null))
                        .map(tacheBean -> tacheBean.getSousCategorie())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreSousCategoriesField.getCheckModel().checkAll();
    }

    private void populerFiltreProjetsApplis() {
        filtreProjetsApplisField.getItems().setAll(
                tachesBeans.stream()
                        .filter(tacheBean -> (tacheBean.getProjetAppli() != null))
                        .map(tacheBean -> tacheBean.getProjetAppli())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreProjetsApplisField.getCheckModel().checkAll();
    }

    private void populerFiltreImportances() {
        filtreImportancesField.getItems().setAll(
                tachesBeans.stream()
                        .filter(tacheBean -> (tacheBean.getImportance() != null))
                        .map(tacheBean -> tacheBean.getImportance())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreImportancesField.getCheckModel().checkAll();
    }

    private void populerFiltreRessources() {
        filtreRessourcesField.getItems().setAll(
                tachesBeans.stream()
                        .filter(tacheBean -> (tacheBean.getRessource() != null))
                        .map(tacheBean -> tacheBean.getRessource())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreRessourcesField.getCheckModel().checkAll();
    }

    private void populerFiltreProfils() {
        filtreProfilsField.getItems().setAll(
                tachesBeans.stream()
                        .filter(tacheBean -> (tacheBean.getProfil() != null))
                        .map(tacheBean -> tacheBean.getProfil())
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        filtreProfilsField.getCheckModel().checkAll();
    }

}
