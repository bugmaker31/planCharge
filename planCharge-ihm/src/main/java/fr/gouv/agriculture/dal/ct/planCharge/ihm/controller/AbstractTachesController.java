package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.javafx.DatePickerCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ModificationTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.CodeCategorieTacheComparator;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.CodeImportanceComparator;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.ImportanceCell;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import org.controlsfx.control.CheckComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 01/05/2017.
 *
 * @author frederic.danna
 */
public abstract class AbstractTachesController<TB extends TacheBean> extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTachesController.class);

/*
    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();
*/

    //    @Autowired
    @NotNull
    private ReferentielsService referentielsService = ReferentielsService.instance();

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();

    // Les beans :
    @NotNull
    private ObservableList<String> codesCategories = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesSousCategories = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesImportances = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesProjetsApplis = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesRessources = FXCollections.observableArrayList();
    @NotNull
    private ObservableList<String> codesProfils = FXCollections.observableArrayList();

    @FXML
    @NotNull
    private TableColumn<TB, String> categorieColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> sousCategorieColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> noTacheColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> noTicketIdalColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> descriptionColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> projetAppliColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> debutColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> echeanceColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> importanceColumn;
    @FXML
    @NotNull
    private TableColumn<TB, Double> chargeColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> ressourceColumn;
    @FXML
    @NotNull
    private TableColumn<TB, String> profilColumn;

    // Les filtres :
    @FXML
    @NotNull
    protected TextField filtreGlobalField;
    @FXML
    @NotNull
    protected CheckComboBox<String> filtreCategoriesField;
    @FXML
    @NotNull
    protected CheckComboBox<String> filtreSousCategoriesField;
    @FXML
    @NotNull
    protected TextField filtreNoTacheField;
    @FXML
    @NotNull
    protected TextField filtreNoTicketIdalField;
    @FXML
    @NotNull
    protected TextField filtreDescriptionField;
    @FXML
    @NotNull
    protected CheckComboBox<String> filtreProjetsApplisField;
    @FXML
    @NotNull
    protected DatePicker filtreDebutField;
    @FXML
    @NotNull
    protected DatePicker filtreEcheanceField;
    @FXML
    @NotNull
    protected CheckComboBox<String> filtreImportancesField;
    @FXML
    @NotNull
    protected TextField filtreChargeField;
    @FXML
    @NotNull
    protected CheckComboBox<String> filtreRessourcesField;
    @FXML
    @NotNull
    protected CheckComboBox<String> filtreProfilsField;

    @NotNull
    abstract ObservableList<TB> getTachesBeans();

    @NotNull
    abstract TableView<TB> getTachesTable();

    @NotNull
    TableColumn<TB, Double> getChargeColumn() {
        return chargeColumn;
    }

    @NotNull
    protected TB ajouterTache(ActionEvent event) throws Exception {
        LOGGER.debug("ajouterTache...");
        try {
            TB nouvTache = nouveauBean();
            getTachesBeans().add(nouvTache);

            // Positionnement sur la tâche qu'on vient d'ajouter :
            int noLigNouvTache = getTachesTable().getItems().size();
            getTachesTable().scrollTo(noLigNouvTache - 1);
            getTachesTable().scrollToColumn(descriptionColumn);
            getTachesTable().getSelectionModel().select(noLigNouvTache - 1);
            // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en modé édition de la cellule.
//            getTachesTable().getSelectionModel().select(noLigNouvTache - 1, descriptionColumn);
//            getTachesTable().edit(getTachesTable().getSelectionModel().getFocusedIndex(), descriptionColumn);
            getTachesTable().edit(noLigNouvTache - 1, descriptionColumn);

            return nouvTache;
        } catch (IhmException e) {
            throw new Exception("Impossible d'ajouter une tâche.", e);
        }
    }

    abstract TB nouveauBean() throws IhmException;

    int idTacheSuivant() {
        OptionalInt max = getTachesBeans().stream().mapToInt(TacheBean::getId).max();
        return max.isPresent() ? (max.getAsInt() + 1) : 1;
    }

    @FXML
    protected void razFiltres(@SuppressWarnings("unused") ActionEvent event) {
        razFiltres();
    }

    private void razFiltres() {
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

    @SuppressWarnings("OverlyLongMethod")
    @Override
    void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");
//        super.initialize();

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        categorieColumn.setCellValueFactory(cellData -> cellData.getValue().codeCategorieProperty());
        sousCategorieColumn.setCellValueFactory(cellData -> cellData.getValue().codeSousCategorieProperty());
        noTacheColumn.setCellValueFactory(cellData -> cellData.getValue().noTacheProperty());
        noTicketIdalColumn.setCellValueFactory(cellData -> cellData.getValue().noTicketIdalProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        projetAppliColumn.setCellValueFactory(cellData -> cellData.getValue().codeProjetAppliProperty());
        debutColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().debutProperty().isNull().get()) {
                //noinspection ReturnOfNull
                return null;
            }
            LocalDate debut = cellData.getValue().debutProperty().get();
            return new SimpleStringProperty((debut == null) ? "" : debut.format(DateTimeFormatter.ofPattern(PlanChargeIhm.FORMAT_DATE)));
        });
        echeanceColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().echeanceProperty().isNull().get()) {
                //noinspection ReturnOfNull
                return null;
            }
            LocalDate echeance = cellData.getValue().echeanceProperty().get();
            return new SimpleStringProperty(echeance.format(DateTimeFormatter.ofPattern(PlanChargeIhm.FORMAT_DATE)));
        });
        importanceColumn.setCellValueFactory(cellData -> cellData.getValue().codeImportanceProperty());
        chargeColumn.setCellValueFactory(cellData -> cellData.getValue().chargeProperty().asObject());
        ressourceColumn.setCellValueFactory(cellData -> cellData.getValue().codeRessourceProperty());
        profilColumn.setCellValueFactory(cellData -> cellData.getValue().codeProfilProperty());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        categorieColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesCategories));
        sousCategorieColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesSousCategories));
        // Rq : La colonne "N° de tâche" n'est pas éditable (car c'est la "primaty key").
        noTicketIdalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projetAppliColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesProjetsApplis));
        debutColumn.setCellFactory(cell -> new DatePickerCell<>(PlanChargeIhm.FORMAT_DATE));
        echeanceColumn.setCellFactory(cell -> new DatePickerCell<>(PlanChargeIhm.FORMAT_DATE));
        importanceColumn.setCellFactory(cell -> new ImportanceCell<>(codesImportances));
        chargeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        ressourceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesRessources));
        profilColumn.setCellFactory(ComboBoxTableCell.forTableColumn(codesProfils));

        // Paramétrage des ordres de tri :
        categorieColumn.setComparator(CodeCategorieTacheComparator.COMPARATEUR);
        importanceColumn.setComparator(CodeImportanceComparator.COMPARATEUR);

/*
        getTachesBeans().addListener((ListChangeListener<TB>) changeListener -> {
            LOGGER.debug("Changement pour la liste des tâches => màj des combobox des filtres...");
            populerFiltres();
        });
*/

        getTachesBeans().addListener((ListChangeListener<TB>) changeListener -> {
            LOGGER.debug("Changement pour la liste des tâches => filtrage des lignes de la table...");

            // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
            // 1. Wrap the ObservableList in a FilteredList
            FilteredList<TB> filteredTaches = new FilteredList<>(getTachesBeans());
            // 2. Set the filter Predicate whenever the filter changes.
            enregistrerListenersSurFiltres(filteredTaches);
            // 3. Wrap the FilteredList in a SortedList.
            SortedList<TB> sortedPlanifBeans = new SortedList<>(filteredTaches);
            // 4. Bind the SortedList COMPARATOR_DEFAUT to the TableView COMPARATOR_DEFAUT.
            sortedPlanifBeans.comparatorProperty().bind(getTachesTable().comparatorProperty());
            // 5. Add sorted (and filtered) data to the table.
            getTachesTable().setItems(sortedPlanifBeans);
        });

        abstract class TacheTableCommitHandler<S extends TB, T> implements EventHandler<TableColumn.CellEditEvent<S, T>> {
            @Override
            public void handle(TableColumn.CellEditEvent<S, T> event) {
                if (event.getOldValue().equals(event.getNewValue())) {
                    return;
                }

                S tacheBean = event.getRowValue();

                S tacheBeanAvant = null;
                try {
                    tacheBeanAvant = (S) tacheBean.copier();
                } catch (CopieException e) {
                    LOGGER.error("Impossible d'historiser la modification de la tâche.", e);
                }

                modifierValeur(tacheBean, event.getNewValue());

                if (tacheBeanAvant != null) {
                    try {
                        getSuiviActionsUtilisateur().historiser(new ModificationTache<>(tacheBeanAvant, tacheBean));
                    } catch (SuiviActionsUtilisateurException e) {
                        LOGGER.error("Impossible d'historiser la modification de la tâche.", e);
                    }
                }
            }

            abstract void modifierValeur(S tacheBean, T nouvelleValeur);
        }
        descriptionColumn.setOnEditCommit(new TacheTableCommitHandler<TB, String>() {
            @Override
            void modifierValeur(TB tacheBean, String nouvelleValeur) {
                tacheBean.descriptionProperty().set(nouvelleValeur);
            }
        });

        LOGGER.debug("Initialisé.");
    }

    @SuppressWarnings({"MethodWithMoreThanThreeNegations", "MethodWithMultipleLoops", "OverlyComplexMethod", "OverlyLongMethod"})
    private void enregistrerListenersSurFiltres(FilteredList<TB> filteredTaches) {
        LOGGER.debug("enregistrerListenersSurFiltres...");

        // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/

        //noinspection OverlyLongLambda
        filtreGlobalField.textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour le filtre 'filtreGlobal'...");
            //noinspection OverlyLongLambda
            filteredTaches.setPredicate(tache -> {
                // If filter text is empty, display all data.
                if ((newValue == null) || newValue.isEmpty()) {
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
                try {
                    if (!tache.descriptionProperty().isEmpty().get() && tache.matcheDescription(newValue)) {
                        return true; // Filter matches
                    }
                } catch (IhmException e) {
                    LOGGER.error("Impossible de filtrer sur la description, pour la tâche n° {}.", tache.getId(), e);
                }
                if (!tache.debutProperty().isNull().get() && tache.matcheDebut(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.echeanceProperty().isNull().get() && tache.matcheEcheance(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.codeProjetAppliProperty().isEmpty().get() && tache.matcheProjetAppli(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.codeImportanceProperty().isNull().get() && tache.matcheImportance(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.codeRessourceProperty().isEmpty().get() && tache.matcheRessource(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.codeProfilProperty().isEmpty().get() && tache.matcheProfil(newValue)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreCategoriesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) change -> {
                    LOGGER.debug("Changement pour le filtre des catégories...");
                    filteredTaches.setPredicate(tache -> {
                        for (String categorieSelectionnee : change.getList()) {
                            if (!tache.codeCategorieProperty().isEmpty().get() && tache.matcheCategorie(categorieSelectionnee)) {
                                return true;
                            }
                        }
                        return false;
                    });
                }
        );
        filtreSousCategoriesField.getCheckModel().getCheckedItems().addListener(
                (ListChangeListener<String>) change -> {
                    LOGGER.debug("Changement pour le filtre des sous-catégories...");
                    filteredTaches.setPredicate(tache -> {
                        for (String sousCategorieSelectionnee : change.getList()) {
                            if (!tache.codeSousCategorieProperty().isEmpty().get() && tache.matcheSousCategorie(sousCategorieSelectionnee)) {
                                return true;
                            }
                        }
                        return false;
                    });
                }
        );
        filtreNoTacheField.textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour le filtre des n°s de tâche...");
            filteredTaches.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if ((newValue == null) || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                if (!tache.noTacheProperty().isEmpty().get() && tache.matcheNoTache(newValue)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreNoTicketIdalField.textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour le filtre des n°s de ticket IDAL...");
            filteredTaches.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if ((newValue == null) || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                if (!tache.noTicketIdalProperty().isEmpty().get() && tache.matcheNoTicketIdal(newValue)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        filtreDescriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour le filtre des descriptions...");

            try {
                ihm.enleverErreurSaisie(filtreDescriptionField);
                if ((newValue != null) && !newValue.isEmpty()) {
                    //noinspection UnusedCatchParameter,NestedTryStatement
                    try {
                        //noinspection ResultOfMethodCallIgnored
                        Pattern.compile(newValue);
                    } catch (PatternSyntaxException e) {
                        ihm.afficherErreurSaisie(filtreDescriptionField, "Expression régulière incorrecte");
                        return;
                    }
                }
            } catch (IhmException e) {
                LOGGER.error("Impossible de gérer la modification du filtre des descriptions.", e);
            }

            filteredTaches.setPredicate(tache -> {

                // If filter text is empty, display all data.
                if ((newValue == null) || newValue.isEmpty()) {
                    return true;
                }

                // Compare column values with filter text.
                try {
                    if (!tache.descriptionProperty().isEmpty().get() && tache.matcheDescription(newValue)) {
                        return true; // Filter matches
                    }
                } catch (IhmException e) {
                    LOGGER.error("Impossible de filtrer sur la description.", e);
                }
                return false; // Does not match.
            });
        });
        filtreDebutField.valueProperty().addListener((observable, oldValue, newValue) -> { // Cf. http://stackoverflow.com/questions/17039101/jfxtras-how-to-add-change-listener-to-calendartextfield
            LOGGER.debug("Changement pour le filtre des débuts...");
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
            LOGGER.debug("Changement pour le filtre des échéances...");
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
        filtreProjetsApplisField.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            LOGGER.debug("Changement pour le filtre des projets/applis...");
            filteredTaches.setPredicate(tache -> {
                for (String codeProjetAppliSelectionne : change.getList()) {
                    if (!tache.codeProjetAppliProperty().isEmpty().get() && tache.matcheProjetAppli(codeProjetAppliSelectionne)) {
                        return true;
                    }
                }
                return false;
            });
        });
        filtreImportancesField.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            LOGGER.debug("Changement pour le filtre des importances...");
            filteredTaches.setPredicate(tache -> {
                for (String codeImportanceSelectionne : change.getList()) {
                    if (!tache.codeImportanceProperty().isNull().get() && tache.matcheImportance(codeImportanceSelectionne)) {
                        return true;
                    }
                }
                return false;
            });
        });
        filtreRessourcesField.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            LOGGER.debug("Changement pour le filtre des ressources...");
            filteredTaches.setPredicate(tache -> {
                for (String codeRessourceSelectionne : change.getList()) {
                    if (!tache.codeRessourceProperty().isEmpty().get() && tache.matcheRessource(codeRessourceSelectionne)) {
                        return true;
                    }
                }
                return false;
            });
        });
        filtreProfilsField.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            LOGGER.debug("Changement pour le filtre des profils...");
            filteredTaches.setPredicate(tache -> {
                for (String codeProfilSelectionne : change.getList()) {
                    if (!tache.codeProfilProperty().isEmpty().get() && tache.matcheProfil(codeProfilSelectionne)) {
                        return true;
                    }
                }
                return false;
            });
        });
    }

    void populerReferentiels() throws IhmException {
        LOGGER.debug("populerReferentiels...");
        try {
            List<CategorieTache> categories = referentielsService.categoriesTache();
            codesCategories.setAll(categories.parallelStream()
                    .sorted(CategorieTache::compareTo)
                    .map(CategorieTache::getCode)
                    .collect(Collectors.toList())
            );
        } catch (ServiceException e) {
            throw new IhmException("Impossible de populer la liste des catégories de tâche.", e);
        }
        try {
            List<SousCategorieTache> sousCategories = referentielsService.sousCategoriesTache();
            codesSousCategories.setAll(sousCategories.parallelStream()
                    .sorted(SousCategorieTache::compareTo)
                    .map(SousCategorieTache::getCode)
                    .collect(Collectors.toList())
            );
        } catch (ServiceException e) {
            throw new IhmException("Impossible de populer la liste des sous-catégories de tâche.", e);
        }

        try {
            List<Importance> importances = referentielsService.importances();
            codesImportances.setAll(importances.parallelStream()
                    .sorted(Importance::compareTo)
                    .map(Importance::getCode)
                    .collect(Collectors.toList())
            );
        } catch (ServiceException e) {
            throw new IhmException("Impossible de populer la liste des importances.", e);
        }
        try {
            List<ProjetAppli> projetApplis = referentielsService.projetsApplis();
            codesProjetsApplis.setAll(projetApplis.parallelStream()
                    .sorted(ProjetAppli::compareTo)
                    .map(ProjetAppli::getCode)
                    .collect(Collectors.toList())
            );
        } catch (ServiceException e) {
            //noinspection HardcodedFileSeparator
            throw new IhmException("Impossible de populer la liste des projets/applis.", e);
        }
        try {
            List<Ressource> ressources = referentielsService.ressources();
            codesRessources.setAll(ressources.parallelStream()
                    .sorted(Ressource::compareTo)
                    .map(Ressource::getTrigramme)
                    .collect(Collectors.toList())
            );
        } catch (ServiceException e) {
            throw new IhmException("Impossible de populer la liste des ressources.", e);
        }
        try {
            List<Profil> profils = referentielsService.profils();
            codesProfils.setAll(profils.parallelStream()
                    .sorted(Profil::compareTo)
                    .map(Profil::getCode)
                    .collect(Collectors.toList())
            );
        } catch (ServiceException e) {
            throw new IhmException("Impossible de populer la liste des profils.", e);
        }
    }

    void populerFiltres() {
        LOGGER.debug("populerFiltres...");
        populerFiltreCategories();
        populerFiltreSousCategories();
        populerFiltreProjetsApplis();
        populerFiltreImportances();
        populerFiltreRessources();
        populerFiltreProfils();
    }

    private void populerFiltreCategories() {
        filtreCategoriesField.getItems().setAll(
                getTachesBeans().parallelStream()
                        .filter(tacheBean -> (tacheBean.getCodeCategorie() != null))
                        .map(TacheBean::getCodeCategorie)
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        // TODO FDA 2017/05 Optimiser le temps d'exécution en ne simulant la coche 1 par 1 comme le fait org.controlsfx.control.CheckBitSetModelBase#checkAll, ce qui déclenche N fois le Listener du CheckComboBox#getCheckModel#getCheckedItems.
        filtreCategoriesField.getCheckModel().checkAll();
    }

    private void populerFiltreSousCategories() {
        filtreSousCategoriesField.getItems().setAll(
                getTachesBeans().parallelStream()
                        .filter(tacheBean -> (tacheBean.getCodeSousCategorie() != null))
                        .map(TacheBean::getCodeSousCategorie)
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        // TODO FDA 2017/05 Optimiser le temps d'exécution en ne simulant la coche 1 par 1 comme le fait org.controlsfx.control.CheckBitSetModelBase#checkAll, ce qui déclenche N fois le Listener du CheckComboBox#getCheckModel#getCheckedItems.
        filtreSousCategoriesField.getCheckModel().checkAll();
    }

    private void populerFiltreProjetsApplis() {
        filtreProjetsApplisField.getItems().setAll(
                getTachesBeans().parallelStream()
                        .filter(tacheBean -> (tacheBean.getCodeProjetAppli() != null))
                        .map(TacheBean::getCodeProjetAppli)
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        // TODO FDA 2017/05 Optimiser le temps d'exécution en ne simulant la coche 1 par 1 comme le fait org.controlsfx.control.CheckBitSetModelBase#checkAll, ce qui déclenche N fois le Listener du CheckComboBox#getCheckModel#getCheckedItems.
        filtreProjetsApplisField.getCheckModel().checkAll();
    }

    private void populerFiltreImportances() {
        filtreImportancesField.getItems().setAll(
                getTachesBeans().parallelStream()
                        .filter(tacheBean -> (tacheBean.getCodeImportance() != null))
                        .map(TacheBean::getCodeImportance)
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        // TODO FDA 2017/05 Optimiser le temps d'exécution en ne simulant la coche 1 par 1 comme le fait org.controlsfx.control.CheckBitSetModelBase#checkAll, ce qui déclenche N fois le Listener du CheckComboBox#getCheckModel#getCheckedItems.
        filtreImportancesField.getCheckModel().checkAll();
    }

    private void populerFiltreRessources() {
        filtreRessourcesField.getItems().setAll(
                getTachesBeans().parallelStream()
                        .filter(tacheBean -> (tacheBean.getCodeRessource() != null))
                        .map(TacheBean::getCodeRessource)
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        // TODO FDA 2017/05 Optimiser le temps d'exécution en ne simulant la coche 1 par 1 comme le fait org.controlsfx.control.CheckBitSetModelBase#checkAll, ce qui déclenche N fois le Listener du CheckComboBox#getCheckModel#getCheckedItems.
        filtreRessourcesField.getCheckModel().checkAll();
    }

    private void populerFiltreProfils() {
        filtreProfilsField.getItems().setAll(
                getTachesBeans().parallelStream()
                        .filter(tacheBean -> (tacheBean.getCodeProfil() != null))
                        .map(TacheBean::getCodeProfil)
                        .distinct()
                        .sorted(String::compareTo)
                        .collect(Collectors.toList())
        );
        // TODO FDA 2017/05 Optimiser le temps d'exécution en ne simulant la coche 1 par 1 comme le fait org.controlsfx.control.CheckBitSetModelBase#checkAll, ce qui déclenche N fois le Listener du CheckComboBox#getCheckModel#getCheckedItems.
        filtreProfilsField.getCheckModel().checkAll();
    }

}
