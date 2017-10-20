package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ModificationNoTicketIdal;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ModificationTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component.BarreEtatTachesComponent;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component.FiltreGlobalTachesComponent;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.CategorieTacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.SousCategorieTacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.Strings;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;
import org.controlsfx.control.table.TableFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Created by frederic.danna on 01/05/2017.
 *
 * @author frederic.danna
 */
public abstract class AbstractTachesController<TB extends TacheBean> extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTachesController.class);


    // La couche métier :

    //    @Autowired
    @NotNull
    // 'final' pour empêcher de resetter cette variable.
    private final ReferentielsService referentielsService = ReferentielsService.instance();

    // Les beans :

    //    @Autowired
    @NotNull
    // 'final' pour empêcher de resetter cette variable.
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // TODO FDA 2017/05 Résoudre le warning de compilation (unchecked assignement).
    @NotNull
    @SuppressWarnings("unchecked")
    // 'final' pour empêcher de resetter cette ObsevableList, ce qui enleverait les Listeners.
    private final FilteredList<TB> filteredTachesBeans = new FilteredList<>((ObservableList<TB>) planChargeBean.getPlanificationsBeans());

    @NotNull
    FilteredList<TB> getFilteredTachesBeans() {
        return filteredTachesBeans;
    }

    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableFilter<TB> tableFilter;

    @NotNull
    public TableFilter<TB> getTableFilter() {
        return tableFilter;
    }

    // Les tables :

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, String> categorieColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, String> sousCategorieColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, String> noTacheColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, String> noTicketIdalColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, String> descriptionColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, ProjetAppliBean> projetAppliColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, StatutBean> statutColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, LocalDate> debutColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, LocalDate> echeanceColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, ImportanceBean> importanceColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, Double> chargeColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, RessourceBean<?, ?>> ressourceColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, ProfilBean> profilColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, MenuButton> menuActionsColumn;


    // Les filtres :

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private FiltreGlobalTachesComponent filtreGlobalComponent;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreCategorieProjetToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreCategorieServiceToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreCategorieOrganisationToggleButton;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreStatutNouveauToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreStatutEnCoursToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreStatutEnAttenteToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreStatutRecurrentToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreStatutReporteToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreStatutClosToggleButton;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtrePeriodeAVenirToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtrePeriodeContemporaineToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtrePeriodeEchueToggleButton;

    // Les menus :

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ContextMenu tachesTableContextMenu;

    // Les items de la barre d'état :

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private BarreEtatTachesComponent<TB> barreEtatComponent;


    // Getters/Setters :

    @NotNull
    abstract TableView<TB> getTachesTable();

    @NotNull
    abstract ObservableList<TB> getTachesBeans();

    @NotNull
    public TableColumn<TB, String> getCategorieColumn() {
        return categorieColumn;
    }

    @NotNull
    public TableColumn<TB, String> getSousCategorieColumn() {
        return sousCategorieColumn;
    }

    @NotNull
    public TableColumn<TB, String> getNoTacheColumn() {
        return noTacheColumn;
    }

    @NotNull
    public TableColumn<TB, String> getNoTicketIdalColumn() {
        return noTicketIdalColumn;
    }

    @NotNull
    public TableColumn<TB, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    @NotNull
    public TableColumn<TB, ProjetAppliBean> getProjetAppliColumn() {
        return projetAppliColumn;
    }

    @NotNull
    public TableColumn<TB, StatutBean> getStatutColumn() {
        return statutColumn;
    }

    @NotNull
    public TableColumn<TB, LocalDate> getDebutColumn() {
        return debutColumn;
    }

    @NotNull
    public TableColumn<TB, LocalDate> getEcheanceColumn() {
        return echeanceColumn;
    }

    @NotNull
    public TableColumn<TB, ImportanceBean> getImportanceColumn() {
        return importanceColumn;
    }

    @NotNull
    public TableColumn<TB, Double> getChargeColumn() {
        return chargeColumn;
    }

    @NotNull
    public TableColumn<TB, RessourceBean<?, ?>> getRessourceColumn() {
        return ressourceColumn;
    }

    @NotNull
    public TableColumn<TB, ProfilBean> getProfilColumn() {
        return profilColumn;
    }

    /*
    @NotNull
    public FilteredList<TB> getFilteredTachesBeans() {
        return filteredTachesBeans;
    }
*/


    // Méthodes :

    @SuppressWarnings("OverlyLongMethod")
    @Override
    protected void initialize() throws ControllerException {
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
        menuActionsColumn.setCellValueFactory((TableColumn.CellDataFeatures<TB, MenuButton> cellData) -> {
            MenuButton actionsMenu = menuActions(cellData);
            return new SimpleObjectProperty<>(actionsMenu);
        });

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        categorieColumn.setCellFactory(ComboBoxTableCell.forTableColumn(CategorieTacheDTO.CODES_CATEGORIES));
        sousCategorieColumn.setCellFactory(ComboBoxTableCell.forTableColumn(SousCategorieTacheDTO.CODES_SOUS_CATEGORIES));
        // Rq : La colonne "N° de tâche" n'est pas éditable (car c'est la "primary key").
        noTicketIdalColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        //noinspection OverlyComplexAnonymousInnerClass
        projetAppliColumn.setCellFactory(param -> new ProjetAppliCell<>(planChargeBean.getProjetsApplisBeans(), this::afficherProjetAppli, this::filtrerSurProjetAppli));
        //noinspection OverlyComplexAnonymousInnerClass
        statutColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<StatutBean>() {

                    @Null
                    @Override
                    public String toString(@Null StatutBean statutBean) {
                        if (statutBean == null) {
                            return null;
                        }
                        return statutBean.getCode();
                    }

                    @Null
                    @Override
                    public StatutBean fromString(@Null String codeStatut) {
                        if (codeStatut == null) {
                            return null;
                        }
                        return Collections.any(
                                planChargeBean.getStatutsBeans(),
                                statutBean -> (statutBean.getCode() != null) && statutBean.getCode().equals(codeStatut)
                        );
                    }
                },
                planChargeBean.getStatutsBeans()));
        debutColumn.setCellFactory(DatePickerTableCells.forTableColumn());
        echeanceColumn.setCellFactory(param -> new EcheanceCell<>(PlanChargeIhm.PATRON_FORMAT_DATE, PlanChargeIhm.PROMPT_FORMAT_DATE, getTachesTable()));
        importanceColumn.setCellFactory(cell -> new ImportanceCell<>(planChargeBean.getImportancesBeans()));
        chargeColumn.setCellFactory(TextFieldTableCell.forTableColumn(Converters.CHARGE_STRING_CONVERTER)); // TODO FDA 2017/08 Mieux formater.
        ressourceColumn.setCellFactory(param -> new RessourceCell<>(planChargeBean.getRessourcesBeans(), this::afficherRessourceHumaine, this::filtrerSurRessource));
        profilColumn.setCellFactory(param -> new ProfilCell<>(planChargeBean.getProfilsBeans(), this::afficherProfil, this::filtrerSurProfil));
/*
        actionsColumn.setCellFactory(param -> {
            ComboBoxTableCell<TB, ComboBox<?>> actionsComboBox = new ComboBoxTableCell<>();
            actionsComboBox.getItems().setAll(
                    new ComboBox<>(FXCollections.observableArrayList("action 1", "action 2"))
            );
            return actionsComboBox;
        });
*/

        // Paramétrage des ordres de tri :
        categorieColumn.setComparator(CodeCategorieTacheComparator.COMPARATEUR);
        importanceColumn.setComparator(ImportanceComparator.COMPARATEUR);
        //
        TableViews.ensureSorting(getTachesTable(), filteredTachesBeans);

        // Ajout des filtres "globaux" (à la TableView, pas sur chaque TableColumn) :
        //
        filtreGlobalComponent.getFiltreGlobalField().textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour le filtre 'filtreGlobal' : {}...", newValue);
            filtrer();
        });

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //
        tableFilter = TableViews.enableFilteringOnColumns(getTachesTable(), Arrays.asList(categorieColumn, sousCategorieColumn, noTacheColumn, noTicketIdalColumn, descriptionColumn, projetAppliColumn, statutColumn, debutColumn, echeanceColumn, importanceColumn, ressourceColumn, chargeColumn, profilColumn));

        // Gestion des undo/redo :
        //
        //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass,ClassWithOnlyPrivateConstructors,ClassWithoutLogger
        class TacheTableCommitHandler<T> implements EventHandler<TableColumn.CellEditEvent<TB, T>> {

            @NotNull
            private BiConsumer<TB, T> modifieurValeur;
            @NotNull
            private BiFunction<TB, TB, ModificationTache<TB>> actionModificationFct;

            private TacheTableCommitHandler(@NotNull BiConsumer<TB, T> modifieurValeur, @NotNull BiFunction<TB, TB, ModificationTache<TB>> actionModificationFct) {
                super();
                this.modifieurValeur = modifieurValeur;
                this.actionModificationFct = actionModificationFct;
            }

            @Override
            public void handle(TableColumn.CellEditEvent<TB, T> event) {
                if ((event.getOldValue() != null) && event.getOldValue().equals(event.getNewValue())) {
                    return;
                }

                TB tacheBean = event.getRowValue();

                TB tacheBeanAvant = null;
                try {
                    //noinspection unchecked
                    tacheBeanAvant = (TB) tacheBean.copier();
                } catch (CopieException e) {
                    LOGGER.error("Impossible d'historiser la modification de la tâche.", e);
                }

                modifieurValeur.accept(tacheBean, event.getNewValue());

                if (tacheBeanAvant != null) {
                    try {
//                        ModificationTache actionModif = actionModification(tacheBeanAvant, tacheBean);
                        ModificationTache<TB> actionModif = actionModificationFct.apply(tacheBeanAvant, tacheBean);
                        getSuiviActionsUtilisateur().historiser(actionModif);
                    } catch (SuiviActionsUtilisateurException e) {
                        LOGGER.error("Impossible d'historiser la modification de la tâche.", e);
                    }
                }
            }
        }
        noTicketIdalColumn.setOnEditCommit(new TacheTableCommitHandler<>(
                (tache, nouvelleValeur) -> tache.noTicketIdalProperty().set(nouvelleValeur),
                ModificationNoTicketIdal::new
        ));
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

        definirRaccourcisClavier();

        // Barre d'état :
        barreEtatComponent.initialize(getTachesBeans(), getTachesTable(), this::ajouterTache);

        // Gestion de la sélection :
        getTachesTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Cf. https://stackoverflow.com/questions/27667965/set-selectionmodel-for-tableview-in-fxml
        getTachesTable().getSelectionModel().setCellSelectionEnabled(true);

        LOGGER.info("Initialisé.");
    }

    MenuButton menuActions(@NotNull TableColumn.CellDataFeatures<TB, MenuButton> cellData) {
        MenuButton menuActions = new MenuButton("_Actions");
        {
            MenuItem menuItemSupprimer = new MenuItem("_Supprimer");
            menuItemSupprimer.setOnAction(event -> {
                TB tacheBean = cellData.getValue();
                supprimerTache(tacheBean);
            });
            menuActions.getItems().add(menuItemSupprimer);
        }
        return menuActions;
    }

    private void definirRaccourcisClavier() {
        // FIXME FDA 2017/08 Ne fonctionne que si l'utilisateur a focusé sur la table avant de taper Ctrl+F. Il faudrait ajouter le handler sur la Scene du "primary" Stage, mais ce dernier n'est pas encore initialisé (NPE).
        getTachesTable().setOnKeyReleased(event -> {
            if (event.isControlDown() && (event.getCode() == KeyCode.F)) {
                filtreGlobalComponent.show();
                filtreGlobalComponent.requestFocus();
                event.consume(); // TODO FDA 2017/08 Confirmer.
                return;
            }
/* Trop dangereux pour le module des Charges (tant qu'on n'a pas programmé le "undo"/Ctrl+Z). Seul le module des Tâches aura ce raccourci-clavier.
            if (event.getCode() == KeyCode.DELETE) {
                if (!getTachesTable().isFocused()) {
                    return;
                }
                supprimerTacheSelectionnee();
                event.consume(); // TODO FDA 2017/08 Confirmer.
                //noinspection UnnecessaryReturnStatement
                return;
            }
*/
        });
    }

    @NotNull
    protected TB ajouterTache() throws ControllerException {
        LOGGER.debug("ajouterTache...");
        TB nouvTache = nouveauBean();
        getTachesBeans().add(nouvTache);

        // Positionnement sur la tâche qu'on vient d'ajouter (si elle n'est pas masquée à cause des filtres éventuellement posés par l'utilisateur) :
        if (TableViews.itemIndex(getTachesTable(), nouvTache) < 0) {
            // La tâche n'apparaît pas dans la liste, car filtrée.
            ihm.afficherNotificationWarning("Nouvelle tâche filtrée", "Tâche " + nouvTache.noTache() + " ajoutée, mais non visible à cause des filtres posés.");
        } else {
            TableViews.editCell(getTachesTable(), nouvTache, descriptionColumn);
        }

//        getTachesTable().refresh(); // Notamment pour recalculer les styles CSS.

        return nouvTache;
    }

    @NotNull
    abstract TB nouveauBean() throws ControllerException;

    int idTacheSuivant() {
        OptionalInt max = getTachesBeans().stream().mapToInt(TacheBean::getId).max();
        return max.isPresent() ? (max.getAsInt() + 1) : 1;
    }


    @FXML
    private void razFiltres(@SuppressWarnings("unused") ActionEvent event) {
        razFiltres();
    }

    void razFiltres() {
        LOGGER.debug("razFiltres...");

        filtreGlobalComponent.getFiltreGlobalField().setText("");
        // TODO FDA 2017/08 Décocher tous les autres filtres.

        for (ToggleButton filtreButton : filtresButtons()) {
            filtreButton.setSelected(true);
        }

        // TODO FDA 2017/08 RAZ les filtres / colonne aussi.

        filtrer();
    }

    List<ToggleButton> filtresButtons() {
        return Arrays.asList(
                filtreCategorieProjetToggleButton,
                filtreCategorieServiceToggleButton,
                filtreCategorieOrganisationToggleButton,
                //
                filtreStatutNouveauToggleButton,
                filtreStatutEnCoursToggleButton,
                filtreStatutEnAttenteToggleButton,
                filtreStatutRecurrentToggleButton,
                filtreStatutReporteToggleButton,
                filtreStatutClosToggleButton,
                //
                filtrePeriodeAVenirToggleButton,
                filtrePeriodeContemporaineToggleButton,
                filtrePeriodeEchueToggleButton
                // TODO FDA 2017/08 Cocher tous les autres filtres.
        );
    }

    @FXML
    private void filtrer(@SuppressWarnings("unused") ActionEvent event) {
        filtrer();
    }

    private void filtrer() {
        filteredTachesBeans.setPredicate(tache -> {
            try {
                return estTacheAVoir(tache);
            } catch (ControllerException e) {
                LOGGER.error("Impossible de filtrer les tâches.", e);
                return true;
            }
        });
//        getTachesTable().refresh(); // Notamment pour réappliquer les styles CSS.
    }

    private boolean estTacheAVoir(@NotNull TB tache) throws ControllerException {

        if (Strings.epure(filtreGlobalComponent.getFiltreGlobalField().getText()) != null) {
            // Si un filtre global est positionné, c'est un filtre exhaustif, il ne s'additionne pas avec les autres filtres.
/*
            if (tache.matcheGlobal(filtreGlobalComponent.getFiltreGlobalField().getText())) {
                return true;
            }
*/
            return tache.matcheGlobal(filtreGlobalComponent.getFiltreGlobalField().getText());
        }

        // Filtres cumulatifs :
        if (estTacheAvecCategorieAVoir(tache)) {
            return true;
        }
        if (estTacheAvecStatutAVoir(tache)) {
            return true;
        }
        if (estTacheAvecPeriodeAVoir(tache)) {
            return true;
        }
        if (estTacheAvecAutreFiltreAVoir(tache)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("WeakerAccess")
    protected abstract boolean estTacheAvecAutreFiltreAVoir(@NotNull TB tache) throws ControllerException;

    private boolean estTacheAvecCategorieAVoir(@NotNull TB tache) {

        if (filtreCategorieProjetToggleButton.isSelected() && tache.matcheCategorie(CategorieTacheDTO.PROJET.getCode())) {
            return true;
        }

        if (filtreCategorieServiceToggleButton.isSelected() && tache.matcheCategorie(CategorieTacheDTO.SERVICE.getCode())) {
            return true;
        }
        if (filtreCategorieOrganisationToggleButton.isSelected() && tache.matcheCategorie(CategorieTacheDTO.ORGANISATION_INTERNE.getCode())) {
            return true;
        }
        return false;
    }

    private boolean estTacheAvecStatutAVoir(@NotNull TB tache) {

        //noinspection ConstantConditions
        if (filtreStatutNouveauToggleButton.isSelected() && tache.matcheStatut(StatutDTO.NOUVEAU.getCode())) {
            return true;
        }
        //noinspection ConstantConditions
        if (filtreStatutEnCoursToggleButton.isSelected() && tache.matcheStatut(StatutDTO.EN_COURS.getCode())) {
            return true;
        }
        //noinspection ConstantConditions
        if (filtreStatutEnAttenteToggleButton.isSelected() && tache.matcheStatut(StatutDTO.EN_ATTENTE.getCode())) {
            return true;
        }
        //noinspection ConstantConditions
        if (filtreStatutRecurrentToggleButton.isSelected() && tache.matcheStatut(StatutDTO.RECURRENT.getCode())) {
            return true;
        }
        //noinspection ConstantConditions
        if (filtreStatutReporteToggleButton.isSelected() && tache.matcheStatut(StatutDTO.REPORTE.getCode())) {
            return true;
        }
        //noinspection ConstantConditions,OverlyComplexBooleanExpression
        if (filtreStatutClosToggleButton.isSelected() && (
                tache.matcheStatut(StatutDTO.ANNULE.getCode())
                        || tache.matcheStatut(StatutDTO.DOUBLON.getCode())
                        || tache.matcheStatut(StatutDTO.TERMINE.getCode())
        )) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("MethodWithMoreThanThreeNegations")
    private boolean estTacheAvecPeriodeAVoir(@NotNull TB tache) {
        //noinspection OverlyComplexBooleanExpression
        if ((tache.getEcheance() != null) && (planChargeBean.getDateEtat() != null)) {
            if (filtrePeriodeEchueToggleButton.isSelected() && tache.getEcheance().isBefore(planChargeBean.getDateEtat())) { // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
                return true;
            }
        }
        return false;
    }


    @FXML
    private void activerTousFiltres(@SuppressWarnings("unused") ActionEvent event) {
        activerTousFiltres();
    }

    void activerTousFiltres() {
        LOGGER.debug("activerTousFiltres...");

        filtreGlobalComponent.getFiltreGlobalField().setText("");
        // TODO FDA 2017/08 RAZ les filtres / colonne aussi.

        for (ToggleButton filtreButton : filtresButtons()) {
            filtreButton.setSelected(false);
        }

        filtrer();
    }

    @FXML
    private void filtrerSurProjetAppli(@SuppressWarnings("unused") ActionEvent event) {
        filtrerSurProjetAppli();
    }

    private void filtrerSurProjetAppli() {
        TB tacheBean = TableViews.selectedItem(getTachesTable());
        if (tacheBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucun projet/appli sélectionné",
                    "Sélectionnez d'abord une tâche, puis recliquez.",
                    400, 100
            );
            return;
        }

        ProjetAppliBean projetAppliBean = tacheBean.getProjetAppli();
        assert projetAppliBean != null;

        filtrerSurProjetAppli(projetAppliBean);
    }

    private void filtrerSurProjetAppli(@NotNull ProjetAppliBean projetAppliBean) {
        TableViews.applyFilter(projetAppliColumn, getTableFilter(), projetAppliBean);
    }


    @FXML
    private void filtrerSurRessource(@SuppressWarnings("unused") ActionEvent event) {
        filtrerSurRessource();
    }

    void filtrerSurRessource() {
        TB tacheBean = TableViews.selectedItem(getTachesTable());
        if (tacheBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucune ressource sélectionnée",
                    "Sélectionnez d'abord une tâche, puis recliquez.",
                    400, 100
            );
            return;
        }

        RessourceBean ressourceBean = tacheBean.getRessource();
        assert ressourceBean != null;

        filtrerSurRessource(ressourceBean);
    }

    void filtrerSurRessource(@NotNull RessourceBean ressourceBean) {
        TableViews.applyFilter(ressourceColumn, getTableFilter(), ressourceBean);
    }

    @FXML
    private void filtrerSurProfil(@SuppressWarnings("unused") ActionEvent event) {
        filtrerSurProfil();
    }

    void filtrerSurProfil() {
        TB tacheBean = TableViews.selectedItem(getTachesTable());
        if (tacheBean == null) {
            ihm.afficherDialog(
                    Alert.AlertType.WARNING,
                    "Aucune ressource sélectionnée",
                    "Sélectionnez d'abord une tâche, puis recliquez.",
                    400, 100
            );
            return;
        }

        ProfilBean profil = tacheBean.getProfil();
        assert profil != null;

        filtrerSurProfil(profil);
    }

    void filtrerSurProfil(@NotNull ProfilBean profilBean) {
        TableViews.applyFilter(profilColumn, getTableFilter(), profilBean);
    }


    @SuppressWarnings("unused")
    @FXML
    private void afficherRessourceHumaine(ActionEvent actionEvent) {
        afficherRessourceHumaine();
    }

    void afficherRessourceHumaine() {
        TB tacheBean = TableViews.selectedItem(getTachesTable());
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la tâche",
                    "Aucune tâche n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        RessourceBean ressourceBean = tacheBean.getRessource();
        if (ressourceBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la ressource",
                    "Aucune ressource n'est (encore) affectée à la tâche sélectionnée (" + tacheBean.noTache() + ")."
                            + "\nAffectez d'abord une ressource, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        if (!(ressourceBean instanceof RessourceHumaineBean)) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la ressource",
                    "La ressource (" + ressourceBean.getCode() + ") affectée à la tâche sélectionnée (" + tacheBean.noTache() + ") n'est pas une ressource humaine.",
                    400, 200
            );
            return;
        }
        RessourceHumaineBean ressourceHumaineBean = (RessourceHumaineBean) ressourceBean;

        try {
            ihm.getApplicationController().afficherModuleRessourcesHumaines();
            TableViews.focusOnItem(ihm.getRessourcesHumainesController().getRessourcesHumainesTable(), ressourceHumaineBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher la ressource humaine " + ressourceHumaineBean.getTrigramme() + ".", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la ressource humaine '" + ressourceHumaineBean.getTrigramme() + "'",
                    Exceptions.causes(e),
                    400, 200
            );
        }
    }

    @FXML
    private void afficherTacheDansOutilTicketing(@SuppressWarnings("unused") ActionEvent mouseEvent) {
        afficherTacheDansOutilTicketing();
    }

    void afficherTacheDansOutilTicketing() {
        LOGGER.debug("afficherTacheDansOutilTicketing...");

        // Cf. http://www.java2s.com/Tutorials/Java/JavaFX/1510__JavaFX_WebView.htm
        // Cf. https://docs.oracle.com/javase/8/javafx/embedded-browser-tutorial/overview.htm
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
        webEngine.load("http://alim-prod-iws-1.zsg.agri/isilogwebsystem/homepage.aspx");

        // TODO FDA 2017/07 Terminer de coder.

        ihm.getPrimaryStage().setScene(new Scene(scrollPane));
    }

    @SuppressWarnings("unused")
    @FXML
    private void afficherProjetAppli(ActionEvent actionEvent) {
        afficherProjetAppli();
    }

    void afficherProjetAppli() {
        TB tacheBean = TableViews.selectedItem(getTachesTable());
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le projet/appli",
                    "Aucune tâche n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        ProjetAppliBean projetAppliBean = tacheBean.getProjetAppli();
        if (projetAppliBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le projet/appli",
                    "Aucun projet/appli n'est (encore) indiqué pour la tâche sélectionnée (" + tacheBean.noTache() + ")."
                            + "\nIndiquez d'abord un projet/appli, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        try {
            ihm.getApplicationController().afficherModuleProjetsApplis();
            TableViews.focusOnItem(ihm.getProjetsApplisController().getProjetsApplisTable(), projetAppliBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le projet/appli " + projetAppliBean.getCode() + ".", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le projet/appli '" + projetAppliBean.getCode() + "'",
                    Exceptions.causes(e),
                    400, 200
            );
        }
    }

    @SuppressWarnings("unused")
    @FXML
    private void afficherProfil(ActionEvent actionEvent) {
        afficherProfil();
    }

    void afficherProfil() {
        TB tacheBean = TableViews.selectedItem(getTachesTable());
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le profil",
                    "Aucune tâche n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        ProfilBean profilBean = tacheBean.getProfil();
        if (profilBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le profil",
                    "Aucun profil n'est (encore) indiqué pour la tâche sélectionnée (" + tacheBean.noTache() + ")."
                            + "\nIndiquez d'abord un profil, puis re-cliquez.",
                    400, 200
            );
            return;
        }

/* TODO FDA 2017/10 Décommenter, une fois le module de gestion des profils codé.
        try {
            ihm.getApplicationController().afficherModuleProfils();
            TableViews.focusOnItem(ihm.getProfilsController().getProfilsTable(), profilBean);
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher le profil " + profilBean.getCode() + ".", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher le profil '" + profilBean.getCode() + "'",
                    Exceptions.causes(e),
                    400, 200
            );
        }
*/
    }


    @SuppressWarnings("FinalPrivateMethod")
    @FXML
    private final void supprimerTache(@NotNull ActionEvent actionEvent) {
        LOGGER.debug("supprimerTache...");
        supprimerTacheSelectionnee();
    }

    @SuppressWarnings("FinalPrivateMethod")
    final void supprimerTacheSelectionnee() {
        TB focusedItem = TableViews.selectedItem(getTachesTable());
        if (focusedItem == null) {
            LOGGER.debug("Aucune tâche sélectionnée, donc on en sait pas que supprimer, on ne fait rien.");
            return;
        }
        supprimerTache(focusedItem);
    }

    @SuppressWarnings("FinalPrivateMethod")
    private final void supprimerTache(@NotNull TB tacheBean) {
        getTachesBeans().remove(tacheBean);
        TableViews.clearSelection(getTachesTable()); // Contournement pour [issue#84:1 appui sur DELETE supprime 2 lignes]. Sinon, 2 lignes sont supprimées. Va savoir pourquoi...
    }

}
