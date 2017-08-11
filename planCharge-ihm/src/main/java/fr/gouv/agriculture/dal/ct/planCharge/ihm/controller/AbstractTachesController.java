package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ModificationNoTicketIdal;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ModificationTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.ImportanceCell;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.CategorieTacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.SousCategorieTacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.CategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Strings;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.table.TableFilter.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 01/05/2017.
 *
 * @author frederic.danna
 */
public abstract class AbstractTachesController<TB extends TacheBean> extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTachesController.class);

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    //    @Autowired
    @NotNull
    private ReferentielsService referentielsService = ReferentielsService.instance();

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();


    // Les beans :

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
    private TableColumn<TB, ProjetAppliBean> projetAppliColumn;
    @FXML
    @NotNull
    private TableColumn<TB, StatutBean> statutColumn;
    @FXML
    @NotNull
    private TableColumn<TB, LocalDate> debutColumn;
    @FXML
    @NotNull
    private TableColumn<TB, LocalDate> echeanceColumn;
    @FXML
    @NotNull
    private TableColumn<TB, ImportanceBean> importanceColumn;
    @FXML
    @NotNull
    private TableColumn<TB, Double> chargeColumn;
    @FXML
    @NotNull
    private TableColumn<TB, RessourceBean> ressourceColumn;
    @FXML
    @NotNull
    private TableColumn<TB, ProfilBean> profilColumn;

    // Les filtres :
/* planCharge-52 Filtre global inopérant -> Incompatible avec TableFilter. Désactivé le temps de rendre compatible (TableFilter préféré).
    @FXML
    @NotNull
    protected TextField filtreGlobalField;
*/


    @NotNull
    abstract ObservableList<TB> getTachesBeans();

    @NotNull
    abstract TableView<TB> getTachesTable();

    @NotNull
    TableColumn<TB, Double> getChargeColumn() {
        return chargeColumn;
    }

    @FXML
    @NotNull
    protected ContextMenu tachesTableContextMenu;


    @SuppressWarnings("OverlyLongMethod")
    @Override
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");
//        super.initialize();

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        categorieColumn.setCellValueFactory(cellData -> cellData.getValue().codeCategorieProperty());
        sousCategorieColumn.setCellValueFactory(cellData -> cellData.getValue().codeSousCategorieProperty());
        noTacheColumn.setCellValueFactory(cellData -> cellData.getValue().noTacheProperty());
        noTicketIdalColumn.setCellValueFactory(cellData -> cellData.getValue().noTicketIdalProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        projetAppliColumn.setCellValueFactory(cellData -> cellData.getValue().projetAppliProperty());
        statutColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        debutColumn.setCellValueFactory(cellData -> cellData.getValue().debutProperty());
        echeanceColumn.setCellValueFactory(cellData -> cellData.getValue().echeanceProperty());
        importanceColumn.setCellValueFactory(cellData -> cellData.getValue().importanceProperty());
        chargeColumn.setCellValueFactory(cellData -> cellData.getValue().chargeProperty().asObject());
        ressourceColumn.setCellValueFactory(cellData -> cellData.getValue().ressourceProperty());
        profilColumn.setCellValueFactory(cellData -> cellData.getValue().profilProperty());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        categorieColumn.setCellFactory(ComboBoxTableCell.forTableColumn(CategorieTacheDTO.CODES_CATEGORIES));
        sousCategorieColumn.setCellFactory(ComboBoxTableCell.forTableColumn(SousCategorieTacheDTO.CODES_SOUS_CATEGORIES));
        // Rq : La colonne "N° de tâche" n'est pas éditable (car c'est la "primary key").
        noTicketIdalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        projetAppliColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<ProjetAppliBean>() {
                    @Null
                    @Override
                    public String toString(ProjetAppliBean projetAppliBean) {
                        return projetAppliBean.getCode();
                    }

                    @Override
                    public ProjetAppliBean fromString(String codeProjetAppli) {
                        return planChargeBean.getProjetsApplisBeans().parallelStream()
                                .filter(projetAppliBean -> projetAppliBean.getCode().equals(codeProjetAppli))
                                .collect(Collectors.toList())
                                .get(0);
                    }
                },
                planChargeBean.getProjetsApplisBeans())
        );
        statutColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<StatutBean>() {

                    @Null
                    @Override
                    public String toString(StatutBean statutBean) {
                        return statutBean.getCode();
                    }

                    @Override
                    public StatutBean fromString(String codeStatut) {
                        return planChargeBean.getStatutsBeans().parallelStream()
                                .filter(statutBean -> statutBean.getCode().equals(codeStatut))
                                .collect(Collectors.toList())
                                .get(0);
                    }
                },
                planChargeBean.getStatutsBeans()));
        debutColumn.setCellFactory(DatePickerTableCells.forTableColumn());
        echeanceColumn.setCellFactory(DatePickerTableCells.forRequiredTableColumn());
        importanceColumn.setCellFactory(cell -> new ImportanceCell<>(planChargeBean.getImportancesBeans()));
        chargeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        ressourceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<RessourceBean>() {
                    @Null
                    @Override
                    public String toString(RessourceBean ressourceBean) {
                        return ressourceBean.getCode();
                    }

                    @Override
                    public RessourceBean fromString(String trigramme) {
                        return planChargeBean.getRessourcesBeans().parallelStream()
                                .filter(ressourceBean -> ressourceBean.getCode().equals(trigramme))
                                .collect(Collectors.toList())
                                .get(0);
                    }
                },
                planChargeBean.getRessourcesBeans()));
        profilColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<ProfilBean>() {

                    @Null
                    @Override
                    public String toString(ProfilBean profilBean) {
                        return profilBean.getCode();
                    }

                    @Override
                    public ProfilBean fromString(String codeProfil) {
                        return planChargeBean.getProfilsBeans().parallelStream()
                                .filter(profilBean -> profilBean.getCode().equals(codeProfil))
                                .collect(Collectors.toList())
                                .get(0);
                    }
                },
                planChargeBean.getProfilsBeans()));

        // Paramétrage des ordres de tri :
        categorieColumn.setComparator(CodeCategorieTacheComparator.COMPARATEUR);
        importanceColumn.setComparator(ImportanceComparator.COMPARATEUR);

        // Ajout des filtres "globaux" (à la TableList, pas sur chaque TableColumn) :
        //
        // TODO FDA 2017/08 Comprendre pourquoi il faut trier.
        SortedList<TB> sortedPlanifBeans = new SortedList<>(getTachesBeans());
        sortedPlanifBeans.comparatorProperty().bind(getTachesTable().comparatorProperty());
        getTachesTable().setItems(sortedPlanifBeans);
/* planCharge-52 Filtre global inopérant -> Incompatible avec TableFilter. Désactivé le temps de rendre compatible (TableFilter préféré).
        FilteredList<TB> filteredTachesBeans = enregistrerListenersSurFiltres(getTachesBeans());
*/
/*
        getTachesBeans().addListener((ListChangeListener<TB>) changeListener -> {
            LOGGER.debug("Changement pour la liste des tâches => filtrage des lignes de la table...");

            // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/
            // 1. Wrap the ObservableList in a FilteredList, and 2. Set the filter Predicate whenever the filter changes.
            FilteredList<TB> filteredTaches = enregistrerListenersSurFiltres(getTachesBeans());
            // 3. Wrap the FilteredList in a SortedList.
            SortedList<TB> sortedPlanifBeans = new SortedList<>(filteredTaches);
            // 4. Bind the SortedList COMPARATOR_DEFAUT to the TableView COMPARATOR_DEFAUT.
            sortedPlanifBeans.comparatorProperty().bind(getTachesTable().comparatorProperty());
            // 5. Add sorted (and filtered) data to the table.
            getTachesTable().setItems(sortedPlanifBeans);
        });
*/

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //
        Builder<TB> filter = TableFilter.forTableView(getTachesTable());
//        filter.lazy(true); // TODO FDA 2017/07 Confirmer (ne semble rien changer).
        filter.apply();
        getIhm().symboliserColonnesFiltrables(categorieColumn, sousCategorieColumn, noTacheColumn, noTicketIdalColumn, descriptionColumn, projetAppliColumn, statutColumn, debutColumn, echeanceColumn, importanceColumn, ressourceColumn, chargeColumn, profilColumn);

        // Gestion des undo/redo :
        //
        abstract class TacheTableCommitHandler<T> implements EventHandler<TableColumn.CellEditEvent<TB, T>> {
            @Override
            public void handle(TableColumn.CellEditEvent<TB, T> event) {
                if ((event.getOldValue() != null) && event.getOldValue().equals(event.getNewValue())) {
                    return;
                }

                TB tacheBean = event.getRowValue();

                TB tacheBeanAvant = null;
                try {
                    tacheBeanAvant = (TB) tacheBean.copier();
                } catch (CopieException e) {
                    LOGGER.error("Impossible d'historiser la modification de la tâche.", e);
                }

                try {
                    modifierValeur(tacheBean, event.getNewValue());
                } catch (IhmException e) {
                    LOGGER.error("Impossible de modifier la tâche.", e);
                }

                if (tacheBeanAvant != null) {
                    try {
                        ModificationTache actionModif = actionModification(tacheBeanAvant, tacheBean);
                        getSuiviActionsUtilisateur().historiser(actionModif);
                    } catch (SuiviActionsUtilisateurException e) {
                        LOGGER.error("Impossible d'historiser la modification de la tâche.", e);
                    }
                }
            }

            abstract void modifierValeur(@NotNull TB tacheBean, @NotNull T nouvelleValeur) throws IhmException;

            protected abstract ModificationTache actionModification(@NotNull TB tacheBeanAvant, @NotNull TB tacheBean) throws SuiviActionsUtilisateurException;
        }
        noTicketIdalColumn.setOnEditCommit(new TacheTableCommitHandler<String>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull String nouvelleValeur) {
                tacheBean.noTicketIdalProperty().set(nouvelleValeur);
            }

            @Override
            protected ModificationTache actionModification(@NotNull TB tacheBeanAvant, @NotNull TB tacheBean) throws SuiviActionsUtilisateurException {
                return new ModificationNoTicketIdal<>(tacheBeanAvant, tacheBean);
            }
        });
        /*
        TODO FDA 2017/07 D'abord bien tester le undo/redo/repeat de la modif du n° de ticket IDAL (ci-dessus), puis faire pareil pour les autres attributs (adapter le code commenté ci-dessous).
        descriptionColumn.setOnEditCommit(new TacheTableCommitHandler<String>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull String nouvelleValeur, @Null TB tacheBeanAvant) {
                tacheBean.descriptionProperty().set(nouvelleValeur);
            }
        });
        projetAppliColumn.setOnEditCommit(new TacheTableCommitHandler<String>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull String nouvelleValeur, @Null TB tacheBeanAvant) {
                tacheBean.projetAppliProperty().set(nouvelleValeur);
            }
        });
        debutColumn.setOnEditCommit(new TacheTableCommitHandler<String>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull String nouvelleValeur, @Null TB tacheBeanAvant) throws IhmException {
                try {
                    tacheBean.debutProperty().set(LocalDate.parse(nouvelleValeur, TacheBean.DATE_FORMATTER));
                } catch (DateTimeParseException e) {
                    throw new IhmException("Impossible de parser la date de début de la tâche.", e);
                }
            }
        });
        echeanceColumn.setOnEditCommit(new TacheTableCommitHandler<String>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull String nouvelleValeur, @Null TB tacheBeanAvant) throws IhmException {
                try {
                    tacheBean.echeanceProperty().set(LocalDate.parse(nouvelleValeur));
                } catch (DateTimeParseException e) {
                    throw new IhmException("Impossible de parser la date d'échéance de la tâche.", e);
                }
            }
        });
        importanceColumn.setOnEditCommit(new TacheTableCommitHandler<String>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull String nouvelleValeur, @Null TB tacheBeanAvant) {
                tacheBean.importanceProperty().set(nouvelleValeur);
            }
        });
        chargeColumn.setOnEditCommit(new TacheTableCommitHandler<Double>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull Double nouvelleValeur, @Null TB tacheBeanAvant) {
                tacheBean.chargeProperty().set(nouvelleValeur);
            }
        });
        ressourceColumn.setOnEditCommit(new TacheTableCommitHandler<String>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull String nouvelleValeur, @Null TB tacheBeanAvant) {
                tacheBean.ressourceProperty().set(nouvelleValeur);
            }
        });
        profilColumn.setOnEditCommit(new TacheTableCommitHandler<String>() {
            @Override
            void modifierValeur(@NotNull TB tacheBean, @NotNull String nouvelleValeur, @Null TB tacheBeanAvant) {
                tacheBean.profilProperty().set(nouvelleValeur);
            }
        });
        */

        // Définition du menu contextuel :
        //
        // Cf. http://o7planning.org/en/11115/javafx-contextmenu-tutorial
        tachesTableContextMenu = new ContextMenu();
        getTachesTable().setContextMenu(tachesTableContextMenu);

        LOGGER.info("Initialisé.");
    }

    public abstract void definirMenuContextuel();

    @NotNull
    protected TB ajouterTache() throws Exception {
        LOGGER.debug("ajouterTache...");
        try {
            TB nouvTache = nouveauBean();
            getTachesBeans().add(nouvTache);

            // Positionnement sur la tâche qu'on vient d'ajouter :
            int idxLigNouvTache = getTachesTable().getItems().indexOf(nouvTache);
            getTachesTable().scrollTo(idxLigNouvTache);
            getTachesTable().scrollToColumn(descriptionColumn);
            getTachesTable().getSelectionModel().select(idxLigNouvTache);
            // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en mode édition de la cellule.
//            getTachesTable().getSelectionModel().select(idxLigNouvTache, descriptionColumn);
//            getTachesTable().edit(getTachesTable().getSelectionModel().getFocusedIndex(), descriptionColumn);
            getTachesTable().edit(idxLigNouvTache, descriptionColumn);

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
    private void razFiltres(@SuppressWarnings("unused") ActionEvent event) {
        razFiltres();
    }

    void razFiltres() {
        LOGGER.debug("RAZ des filtres...");
/* planCharge-52 Filtre global inopérant -> Incompatible avec TableFilter. Désactivé le temps de rendre compatible (TableFilter préféré).
        filtreGlobalField.clear();
*/
    }

/* planCharge-52 Filtre global inopérant -> Incompatible avec TableFilter. Désactivé le temps de rendre compatible (TableFilter préféré).
    @NotNull
    @SuppressWarnings({"MethodWithMoreThanThreeNegations", "MethodWithMultipleLoops", "OverlyComplexMethod", "OverlyLongMethod"})
    private FilteredList<TB> enregistrerListenersSurFiltres(@NotNull ObservableList<TB> tachesBeans) {
        LOGGER.debug("enregistrerListenersSurFiltres...");

        // Cf. http://code.makery.ch/blog/javafx-8-tableview-sorting-filtering/

        FilteredList<TB> filteredTachesBeans = new FilteredList<>(getTachesBeans());

        //noinspection OverlyLongLambda
        filtreGlobalField.textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour le filtre 'filtreGlobal' : {}...", newValue);
            //noinspection OverlyLongLambda
            filteredTachesBeans.setPredicate(tache -> {
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
                    if (!tache.descriptionProperty().isEmpty().get() && Strings.estExpressionReguliere(newValue) && tache.matcheDescription(newValue)) {
                        return true; // Filter matches
                    }
                } catch (IhmException e) {
                    LOGGER.error("Impossible de filtrer sur la description '" + newValue + "' pour la tâche n° " + tache.getId() + ".", e);
                }
                if (!tache.debutProperty().isNull().get() && tache.matcheDebut(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.echeanceProperty().isNull().get() && tache.matcheEcheance(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.projetAppliProperty().isNull().get() && tache.matcheProjetAppli(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.importanceProperty().isNull().get() && tache.matcheImportance(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.ressourceProperty().isNull().get() && tache.matcheRessource(newValue)) {
                    return true; // Filter matches
                }
                if (!tache.profilProperty().isNull().get() && tache.matcheProfil(newValue)) {
                    return true; // Filter matches
                }
                return false; // Does not match.
            });
        });
        return filteredTachesBeans;
    }
*/

    @Null
    TB tacheSelectionnee() {
        return getTachesTable().getSelectionModel().getSelectedItem();
    }

    void mettreFocusSurTache(@NotNull TB tacheBean) {
        int idxTacheBean = getTachesTable().getItems().indexOf(tacheBean);
        assert idxTacheBean != -1;
        // FIXME FDA 2017/07 Ok qd on bascule du plan charge vers la liste des tâches, mais sans effet dans l'autre sens.
        getTachesTable().getSelectionModel().clearAndSelect(idxTacheBean);
//        getTachesTable().getSelectionModel().focus(idxTacheBean); Ne change rien.
    }


    @FXML
    private void afficherTacheDansOutilTicketing(@SuppressWarnings("unused") ActionEvent mouseEvent) {
        afficherTacheDansOutilTicketing();
    }

    void afficherTacheDansOutilTicketing() {
        LOGGER.debug("afficherTacheDansOutilTicketing...");

        // Cf. http://www.java2s.com/Tutorials/Java/JavaFX/1510__JavaFX_WebView.htm
        // Cf. https://docs.oracle.com/javase/8/javafx/embedded-browser-tutorial/overview.htm
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();

        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
        webEngine.load("http://alim-prod-iws-1.zsg.agri/isilogwebsystem/homepage.aspx");

        // TODO FDA 2017/07 Terminer de coder.

        ihm.getPrimaryStage().setScene(new Scene(scrollPane));
    }
}
