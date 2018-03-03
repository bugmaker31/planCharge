package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge.ModificationPlanification;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurTache.ModificationTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component.BarreEtatTachesComponent;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component.FiltreGlobalTachesComponent;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.TypeChangement;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.CategorieTacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.SousCategorieTacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.function.TriFunction;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.table.TableFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;

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
    private TitledPane parametresAffichagePane;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private Accordion filtresAccordion;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TitledPane filtresPane;

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
    private ToggleButton filtrePeriodeAVenirDemandeeDansSemestreToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtrePeriodeEchueToggleButton;


    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreChangementInchangeToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreChangementAjoutToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreChangementModificationToggleButton;
    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private ToggleButton filtreChangementSuppressionToggleButton;

    @Null
    private List<TableColumn<TB, ?>> ordreTriParDefautTachesTable;


    // Les menus :

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

    @NotNull
    public ToggleButton getFiltrePeriodeEchueToggleButton() {
        return filtrePeriodeEchueToggleButton;
    }


    // Méthodes :

    @SuppressWarnings("OverlyLongMethod")
    @Override
    protected void initialize() throws ControllerException {
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
        statutColumn.setCellFactory(ComboBoxTableCell.forTableColumn(Converters.STATUT_STRING_CONVERTER, planChargeBean.getStatutsBeans()));
        debutColumn.setCellFactory(DatePickerTableCells.forTableColumn());
        echeanceColumn.setCellFactory(param -> new EcheanceCell<>(PlanChargeIhm.PATRON_FORMAT_DATE, PlanChargeIhm.PROMPT_FORMAT_DATE, getTachesTable()));
        importanceColumn.setCellFactory(cell -> new ImportanceCell<>(planChargeBean.getImportancesBeans()));
        chargeColumn.setCellFactory(TextFieldTableCell.forTableColumn(Converters.CHARGE_STRING_CONVERTER)); // TODO FDA 2017/08 Mieux formater.
        ressourceColumn.setCellFactory(param -> new RessourceCell<>(planChargeBean.getRessourcesBeans(), this::afficherRessourceHumaine, this::filtrerSurRessource));
        profilColumn.setCellFactory(param -> new ProfilCell<>(planChargeBean.getProfilsBeans(), this::afficherProfil, this::filtrerSurProfil));

        // Paramétrage des ordres de tri :
        categorieColumn.setComparator(CodeCategorieTacheComparator.COMPARATEUR);
        importanceColumn.setComparator(ImportanceComparator.COMPARATEUR);
        //
        TableViews.ensureSorting(getTachesTable(), filteredTachesBeans);
        //
        memoriserOrdreTriParDefautTachesTable();

        // Ajout des filtres "globaux" (à la TableView, pas sur chaque TableColumn) :
        //
        filtreGlobalComponent.getFiltreGlobalField().textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour le filtre 'filtreGlobal' : {}...", newValue);
            filtrer();
        });
        filtreGlobalComponent.getSensibleCasseCheckBox().selectedProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour l'option de filtre 'filtreGlobal' : {}...", newValue);
            filtrer();
        });

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //
        tableFilter = TableViews.enableFilteringOnColumns(getTachesTable(), Arrays.asList(categorieColumn, sousCategorieColumn, noTacheColumn, noTicketIdalColumn, descriptionColumn, projetAppliColumn, statutColumn, debutColumn, echeanceColumn, importanceColumn, ressourceColumn, chargeColumn, profilColumn));

        // Gestion des undo/redo :
        //
        noTicketIdalColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, String>(
                TacheBean::setNoTicketIdal,
                (TB tb, String valeurAvant, String valeurApres) -> new ModificationTache<TB, String>(tb, TB::setNoTicketIdal, valeurAvant, valeurApres,"n° de ticket IDAL")
        ));
        descriptionColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, String>(
                TB::setDescription,
                (TB tb, String valeurAvant, String valeurApres) -> new ModificationTache<TB, String>(tb, TB::setDescription, valeurAvant, valeurApres,"de la description")
        ));
        projetAppliColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, ProjetAppliBean>(
                TB::setProjetAppli,
                (TB tb, ProjetAppliBean valeurAvant, ProjetAppliBean valeurApres) -> new ModificationTache<TB, ProjetAppliBean>(tb, TB::setProjetAppli, valeurAvant, valeurApres,"du projet/appli", ProjetAppliBean::getCode)
        ));
        statutColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, StatutBean>(
                TB::setStatut,
                (TB tb, StatutBean valeurAvant, StatutBean valeurApres) -> new ModificationTache<TB, StatutBean>(tb, TB::setStatut, valeurAvant, valeurApres,"du statut", StatutBean::getCode)
        ));
        debutColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, LocalDate>(
                TB::setDebut,
                (TB tb, LocalDate valeurAvant, LocalDate valeurApres) -> new ModificationTache<TB, LocalDate>(tb, TB::setDebut, valeurAvant, valeurApres,"de la date de début", date -> DateTimeFormatter.ofPattern(PlanChargeIhm.PATRON_FORMAT_DATE).format(date))
        ));
        echeanceColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, LocalDate>(
                TB::setEcheance,
                (TB tb, LocalDate valeurAvant, LocalDate valeurApres) -> new ModificationTache<TB, LocalDate>(tb, TB::setEcheance, valeurAvant, valeurApres,"de la date d'échéance", date -> DateTimeFormatter.ofPattern(PlanChargeIhm.PATRON_FORMAT_DATE).format(date))
        ));
        importanceColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, ImportanceBean>(
                TB::setImportance,
                (TB tb, ImportanceBean valeurAvant, ImportanceBean valeurApres) -> new ModificationTache<TB, ImportanceBean>(tb, TB::setImportance, valeurAvant, valeurApres,"de l'importance", ImportanceBean::getCode)
        ));
        chargeColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, Double>(
                TB::setCharge,
                (TB tb, Double valeurAvant, Double valeurApres) -> new ModificationTache<TB, Double>(tb, TB::setCharge, valeurAvant, valeurApres,"de la charge", Converters.CHARGE_STRING_CONVERTER::toString)
        ));
        ressourceColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, RessourceBean<?, ?>>(
                TB::setRessource,
                (TB tb, RessourceBean<?, ?> valeurAvant, RessourceBean<?, ?> valeurApres) -> new ModificationTache<TB, RessourceBean<?, ?>>(tb, TB::setRessource, valeurAvant, valeurApres,"de la ressource", RessourceBean::getCode)
        ));
        profilColumn.setOnEditCommit(new SuiviActionUtilisateurTableCommitHandler<TB, ProfilBean>(
                TB::setProfil,
                (TB tb, ProfilBean valeurAvant, ProfilBean valeurApres) -> new ModificationTache<TB, ProfilBean>(tb, TB::setProfil, valeurAvant, valeurApres,"du profil", ProfilBean::getCode)
        ));

        definirRaccourcisClavier();

        // Barre d'état :
        barreEtatComponent.initialize(getTachesBeans(), getTachesTable());

        // Gestion de la sélection des cellules de la table des tâches :
        getTachesTable().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // Cf. https://stackoverflow.com/questions/27667965/set-selectionmodel-for-tableview-in-fxml
        getTachesTable().getSelectionModel().setCellSelectionEnabled(true);

        /*
         La tâche sélectionnée doit être affichée :
         - dans la fenêtre qui sert à tracer les révisions
         - dans les autres fenêtres qui gèrent des tâches (liste des tâches, plan de charge, liste des révisions).
         */
        //noinspection OverlyLongLambda
        getTachesTable().getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            if (!Objects.equals(this, ihm.getTracerRevisionController()) && ihm.getTracerRevisionController().isShowing()) {
                ihm.getTracerRevisionController().afficher(newValue);
            }

            if (!Objects.equals(this, ihm.getTachesController()) && ihm.getTachesController().isShowing()) {
                ihm.getTachesController().afficherTache(newValue);
            }
            if (!Objects.equals(this, ihm.getChargesController()) && ihm.getChargesController().isShowing()) {
                ihm.getChargesController().afficherTache((PlanificationTacheBean) newValue);
            }
            if (!Objects.equals(this, ihm.getRevisionsController()) && ihm.getRevisionsController().isShowing()) {
                ihm.getRevisionsController().afficherTache(newValue);
            }
        });
    }

    @SuppressWarnings({"ClassHasNoToStringMethod", "PackageVisibleInnerClass", "NonStaticInnerClassInSecureContext", "TypeParameterHidesVisibleType"})
    class SuiviActionUtilisateurTableCommitHandler<TB extends TacheBean, T> implements EventHandler<TableColumn.CellEditEvent<TB, T>> {

        @NotNull
        private BiConsumer<TB, T> fctModifieur;
        @NotNull
        private TriFunction<TB, T, T, ModificationPlanification> fctActionModification;

        SuiviActionUtilisateurTableCommitHandler(
                @NotNull BiConsumer<TB, T> fctModifieur,
                @NotNull TriFunction<TB, T, T, ModificationPlanification> fctActionModification
        ) {
            super();
            this.fctModifieur = fctModifieur;
            this.fctActionModification = fctActionModification;
        }

        @Override
        public void handle(TableColumn.CellEditEvent<TB, T> event) {
            assert event != null;
            if (Objects.equals(event.getOldValue(), event.getNewValue())) {
                return;
            }

            T valeurAvant = event.getOldValue();
            TB tacheBean = event.getRowValue();
            T valeurApres = event.getNewValue();

            // Modification de la valeur :
            fctModifieur.accept(tacheBean, valeurApres);

            // Historisation de la modification de la valeur (par l'utiisateur) :
            ModificationPlanification actionModif = fctActionModification.apply(tacheBean, valeurAvant, valeurApres);
            try {
                getSuiviActionsUtilisateur().historiser(actionModif);
            } catch (SuiviActionsUtilisateurException e) {
                LOGGER.error("Impossible d'historiser la modification de la tâche : " + actionModif.getTexte() + ".", e);
            }
        }
    }


    public boolean isShowing() {
        return Objects.equals(ihm.getApplicationController().getModuleCourant(), this);
    }

    private void memoriserOrdreTriParDefautTachesTable() {
        ordreTriParDefautTachesTable = new ArrayList<>(getTachesTable().getSortOrder());
    }

    @FXML
    private void reinitOrdreTriTachesTable(@NotNull ActionEvent actionEvent) {
        LOGGER.debug("reinitOrdreTriTachesTable");
        getTachesTable().getSortOrder().setAll(ordreTriParDefautTachesTable);
    }


    MenuButton menuActions(@NotNull TableColumn.CellDataFeatures<TB, MenuButton> cellData) {
        MenuButton menuActions = new MenuButton("_Actions");
        TB tacheBean = cellData.getValue();
        {
            MenuItem menuItemSupprimer = new MenuItem("Supprimer la tâche " + tacheBean.noTache());
            menuItemSupprimer.setOnAction(event -> supprimerTache(tacheBean));
            menuActions.getItems().add(menuItemSupprimer);
        }
        {
            MenuItem menuItemVoirTache = new MenuItem("Voir la tâche " + tacheBean.noTache() + " dans outil de ticketing");
            menuItemVoirTache.setOnAction(event -> {
                try {
                    afficherTacheDansOutilTicketing(tacheBean);
                } catch (ControllerException e) {
                    LOGGER.error("Impossible d'afficher la tâche " + tacheBean.noTache() + " dans l'outil de ticketing.", e);
                }
            });
            menuActions.getItems().add(menuItemVoirTache);
        }
        {
            MenuItem menuItemReviser = new MenuItem("Réviser la tâche " + tacheBean.noTache());
            menuItemReviser.setOnAction(event -> {
                try {
                    ihm.getApplicationController().afficherFenetreTracerRevision();
                } catch (ControllerException e) {
                    LOGGER.error("Impossible d'afficher la fenêtre de révision pour la tâche " + tacheBean.noTache(), e);
                }
            });
            menuActions.getItems().add(menuItemReviser);
        }
        return menuActions;
    }

    private void definirRaccourcisClavier() {
        // FIXME FDA 2017/08 Ne fonctionne que si l'utilisateur a focusé sur la table avant de taper Ctrl+F. Il faudrait ajouter le handler sur la Scene du "primary" Stage, mais ce dernier n'est pas encore initialisé (NPE).
        getTachesTable().setOnKeyReleased(event -> {
            if (event.isControlDown() && (event.getCode() == KeyCode.F)) {

                parametresAffichagePane.setExpanded(true);
                filtresAccordion.setExpandedPane(filtresPane);

                // FIXME FDA 2018/02 Sans effet : le focus n'est pas positionné sur le textfield du filtre global.
//                Platform.runLater(() -> {
                filtreGlobalComponent.getFiltreGlobalField().requestFocus();
//                });

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


    @FXML
    private void ajouterTache(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        ajouterTache();
    }

    @NotNull
    protected TB ajouterTache() throws ControllerException {

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
    private void filtrerRien(@SuppressWarnings("unused") ActionEvent event) {
        filtrerRien();
    }

    void filtrerRien() {
        LOGGER.debug("RàZ des filtres pour {} : ", getClass().getSimpleName());
        selectionnerTousFiltres();
        filtrer();
        LOGGER.debug("Filtres RàZ pour {}.", getClass().getSimpleName());
    }

    private void selectionnerTousFiltres() {

        filtreGlobalComponent.getFiltreGlobalField().setText("");

        for (ToggleButton filtreButton : filtresButtons()) {
            if (filtreButton.getToggleGroup() != null) {
                boolean estBoutonTout = estBoutonFiltreTout(filtreButton);
                filtreButton.setSelected(estBoutonTout);
            } else {
                filtreButton.setSelected(true);
            }
        }

        // TODO FDA 2017/08 RAZ les filtres / colonne aussi (TableFilter).
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
                filtrePeriodeAVenirDemandeeDansSemestreToggleButton,
                filtrePeriodeContemporaineToggleButton,
                filtrePeriodeEchueToggleButton,
                //
                filtreChangementInchangeToggleButton,
                filtreChangementAjoutToggleButton,
                filtreChangementModificationToggleButton,
                filtreChangementSuppressionToggleButton
                //
                // Ajouter les futurs filtres ici.
        );
    }

    @FXML
    private void filtrer(@SuppressWarnings("unused") ActionEvent event) {
        filtrer();
    }

    void filtrer() {
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

    @SuppressWarnings("MethodWithMultipleReturnPoints")
    private boolean estTacheAVoir(@NotNull TB tache) throws ControllerException {

        if (!filtreGlobalComponent.getFiltreGlobalField().getText().isEmpty()) {
            // Si un filtre global est positionné, c'est un filtre exhaustif, il ne s'additionne pas avec les autres filtres.
/*
            if (tache.matcheGlobal(filtreGlobalComponent.getFiltreGlobalField().getText())) {
                return true;
            }
*/
            return tache.matcheGlobal(filtreGlobalComponent.getFiltreGlobalField().getText(), filtreGlobalComponent.getSensibleCasseCheckBox().isSelected());
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
        if (estTacheAvecTypeChangementAVoir(tache)) {
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
        //noinspection ConstantConditions
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
        if (filtrePeriodeAVenirDemandeeDansSemestreToggleButton.isSelected()) {
            if ((planChargeBean.getDateEtat() == null) || (tache.getDebut() == null)) {
                return true;
            }
            if (tache.getDebut().isBefore(planChargeBean.getDateEtat().plusMonths(6L))) {
                return true;
            }
        }
        if ((tache.getEcheance() != null) && (planChargeBean.getDateEtat() != null)) {
            if (filtrePeriodeEchueToggleButton.isSelected() && tache.getEcheance().isBefore(planChargeBean.getDateEtat())) { // TODO FDA 2017/09 Coder cette RG dans un DTO/Entity/Service.
                return true;
            }
        }
        return false;
    }

    private boolean estTacheAvecTypeChangementAVoir(@NotNull TB tache) {
        if (filtreChangementInchangeToggleButton.isSelected()) {
            if (tache.getTypeChangement() == null) {
                return true;
            }
        }
        if (filtreChangementAjoutToggleButton.isSelected()) {
            if (tache.getTypeChangement() == TypeChangement.AJOUT) {
                return true;
            }
        }
        if (filtreChangementModificationToggleButton.isSelected()) {
            if (tache.getTypeChangement() == TypeChangement.MODIFICATION) {
                return true;
            }
        }
        if (filtreChangementSuppressionToggleButton.isSelected()) {
            if (tache.getTypeChangement() == TypeChangement.SUPPRESSION) {
                return true;
            }
        }
        return false;
    }


    @FXML
    private void filtrerTout(@SuppressWarnings("unused") ActionEvent event) {
        filtrerTout();
    }

    void filtrerTout() {
        LOGGER.debug("filtrerTout...");
        deselectionnerTousFiltres();
        filtrer();
    }

    void deselectionnerTousFiltres() {
        filtreGlobalComponent.getFiltreGlobalField().setText("");

        for (ToggleButton filtreButton : filtresButtons()) {
            filtreButton.setSelected(false);
        }
    }

    private boolean estBoutonFiltreTout(@NotNull ToggleButton filtreButton) {
        Object buttonUserData = filtreButton.getUserData();
        return (buttonUserData instanceof String) && buttonUserData.equals("TOUT");
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

        RessourceBean<?, ?> ressourceBean = tacheBean.getRessource();
        assert ressourceBean != null;

        filtrerSurRessource(ressourceBean);
    }

    void filtrerSurRessource(@NotNull RessourceBean<?, ?> ressourceBean) {
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


    void filtrerSurTachesAjoutees() {
        deselectionnerTousFiltres();
        filtreChangementAjoutToggleButton.setSelected(true);
        filtrer();
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

        RessourceBean<?, ?> ressourceBean = tacheBean.getRessource();
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
    private void afficherTacheDansOutilTicketing(@SuppressWarnings("unused") ActionEvent mouseEvent) throws ControllerException {
        LOGGER.debug("afficherTacheDansOutilTicketing...");
        afficherTacheDansOutilTicketing();
    }

    void afficherTacheDansOutilTicketing() throws ControllerException {

        TB tacheBean = TableViews.selectedItem(getTachesTable());
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la tâche dans l'outil de ticketing",
                    "Aucune tâche n'est actuellement sélectionnée."
                            + "\nSélectionnez d'abord une ligne, puis re-cliquez.",
                    400, 200
            );
            return;
        }

        afficherTacheDansOutilTicketing(tacheBean);
    }

    void afficherTacheDansOutilTicketing(@NotNull TB tacheBean) throws ControllerException {

        String urlOutilTicketing;
        try {
            urlOutilTicketing = ihm.paramsIhm.getParametrage("outilTicketing.url");
        } catch (KernelException e) {
            throw new ControllerException("Impossible de retrouver l'URL de l'outil de ticketing.", e);
        }

        // Cf. http://www.java2s.com/Tutorials/Java/JavaFX/1510__JavaFX_WebView.htm
        // Cf. https://docs.oracle.com/javase/8/javafx/embedded-browser-tutorial/overview.htm
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        webEngine.load(urlOutilTicketing);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);

        Stage outilTicketingStage = new Stage();
        outilTicketingStage.setTitle(PlanChargeIhm.APP_NAME + " - " + "Outil de ticketing");
        outilTicketingStage.setScene(new Scene(scrollPane));
        outilTicketingStage.show();

        // TODO FDA 2017/07 Terminer de coder.
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
/*
        getTachesBeans().remove(tacheBean);
        TableViews.clearSelection(getTachesTable()); // Contournement pour [issue#84:1 appui sur DELETE supprime 2 lignes]. Sinon, 2 lignes sont supprimées. Va savoir pourquoi...
*/
        tacheBean.setTypeChangement(TypeChangement.SUPPRESSION);
    }

    @FXML
    private void modifierEcheances(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        LOGGER.debug("modifierEcheances...");
        modifierEcheances();
    }

    void modifierEcheances() throws ControllerException {

        LocalDate nouvelleEcheance = saisirNouvelleEcheance();
        if (nouvelleEcheance == null) {
            ihm.afficherDialog(Alert.AlertType.INFORMATION,
                    "Modification annulée",
                    "La modification des échéances des tâches a été annulée par l'utilisateur.",
                    400, 100
            );
            return;
        }

        for (TB tacheBean : getTachesTable().getItems()) {
            reporterTache(tacheBean, nouvelleEcheance);
        }
        getTachesTable().refresh(); // Pour recalculer les styles CSS.
        ihm.afficherNotificationInfo("Echéances modifiées", getTachesTable().getItems().size() + " tâches ont maintenant une échéance au " + nouvelleEcheance.format(DateTimeFormatter.ISO_LOCAL_DATE) + ".");
    }

    private Stage saisieEcheanceStage = null;

    @Null
    private LocalDate saisirNouvelleEcheance() {
        LocalDate nouvelleEcheance;

        if (saisieEcheanceStage == null) {
            saisieEcheanceStage = new Stage();
            saisieEcheanceStage.setTitle(ihm.APP_NAME + " - " + "Nouvelle échéance ?");
            saisieEcheanceStage.getIcons().setAll(ihm.getPrimaryStage().getIcons());
            saisieEcheanceStage.setScene(new Scene(ihm.getSaisieEcheanceView()));
            saisieEcheanceStage.initModality(Modality.APPLICATION_MODAL);
            ihm.getSaisieEcheanceController().getAnnulerButton().setOnAction(event -> {
                ihm.getSaisieEcheanceController().getEcheanceDatePicker().setValue(null);
                saisieEcheanceStage.close();
            });
            ihm.getSaisieEcheanceController().getValiderButton().setOnAction(event -> {
                saisieEcheanceStage.close();
            });
        }
        saisieEcheanceStage.showAndWait();

        nouvelleEcheance = ihm.getSaisieEcheanceController().getEcheanceDatePicker().getValue();

        return nouvelleEcheance;
    }

    private void reporterTache(@NotNull TB tacheBean, @NotNull LocalDate nouvelleEcheance) throws ControllerException {
        tacheBean.setEcheance(nouvelleEcheance);
    }


    void afficherTache(@NotNull TB tacheBean) {
        TB tacheCourante = TableViews.selectedItem(getTachesTable());
        if ((tacheCourante != null) && tacheCourante.equals(tacheBean)) {
            return;
        }

/*
        try {
            ihm.getApplicationController().afficherModuleTaches();
*/
        TableViews.focusOnItem(getTachesTable(), tacheBean, getNoTacheColumn());
/*
        } catch (ControllerException e) {
            LOGGER.error("Impossible d'afficher la tâche " + tacheBean.noTache() + ".", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la tâche" + tacheBean.noTache(),
                    Exceptions.causes(e),
                    400, 200
            );
        }
*/
    }

    void afficherPlanification(@NotNull TacheBean tacheBean) {
        try {
            ihm.getApplicationController().afficherModuleCharges();
            TableViews.focusOnItem(ihm.getChargesController().getTachesTable(), (PlanificationTacheBean) tacheBean, ihm.getChargesController().getNoTacheColumn());
        } catch (IhmException e) {
            LOGGER.error("Impossible d'afficher la planification pour la tâche " + tacheBean.getId() + ".", e);
            ihm.afficherDialog(
                    Alert.AlertType.ERROR,
                    "Impossible d'afficher la planification pour la tâche" + tacheBean.getId() + ".",
                    Exceptions.causes(e)
            );
        }
    }


}
