package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ModificationNoTicketIdal;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ModificationTache;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.ImportanceCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.component.FiltreGlobalTachesComponent;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.CategorieTacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.SousCategorieTacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import fr.gouv.agriculture.dal.ct.planCharge.util.Exceptions;
import fr.gouv.agriculture.dal.ct.planCharge.util.Strings;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
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
import javafx.scene.input.KeyCode;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
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


    //    @Autowired
    @NotNull
    final // 'final' pour empêcher de resetter cette variable.
    private ReferentielsService referentielsService = ReferentielsService.instance();

    // Les beans :

    //    @Autowired
    @NotNull
    final // 'final' pour empêcher de resetter cette variable.
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    @NotNull
    abstract ObservableList<TB> getTachesBeans();

    // TODO FDA 2017/05 Résoudre le warning de compilation (unchecked assignement).
    @SuppressWarnings("unchecked")
    @NotNull
    final // 'final' pour empêcher de resetter cette ObsevableList, ce qui enleverait les Listeners.
    private FilteredList<TB> filteredTachesBeans = new FilteredList<TB>((ObservableList<TB>) planChargeBean.getPlanificationsBeans());

    @NotNull
    public FilteredList<TB> getFilteredTachesBeans() {
        return filteredTachesBeans;
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
    private TableColumn<TB, RessourceBean> ressourceColumn;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private TableColumn<TB, ProfilBean> profilColumn;

    // Les filtres :

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private FiltreGlobalTachesComponent filtreGlobalComponent;

    /*
        @FXML
        @NotNull
        @SuppressWarnings("NullableProblems")
        private SegmentedButton filtreCategorieSegmentedButton;
    */
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreCategorieToutToggleButton;
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

    /*
        @FXML
        @NotNull
        @SuppressWarnings("NullableProblems")
        private SegmentedButton filtreStatutSegmentedButton;
    */
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreStatutToutToggleButton;
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
/*
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtreStatutEchuToggleButton;
*/

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtrePeriodeToutToggleButton;
    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    private ToggleButton filtrePeriodeEchueToggleButton;

    @FXML
    @NotNull
    @SuppressWarnings("NullableProblems")
    protected ContextMenu tachesTableContextMenu;


    @NotNull
    abstract TableView<TB> getTachesTable();

    @NotNull
    TableColumn<TB, Double> getChargeColumn() {
        return chargeColumn;
    }


    @SuppressWarnings("OverlyLongMethod")
    @Override
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");
//        super.initialize();

        // Définition dees items de la table :
        // TODO FDA 2017/08 Comprendre pourquoi il faut trier.
        SortedList<TB> sortedFilteredPlanifBeans = new SortedList<>(filteredTachesBeans);
        sortedFilteredPlanifBeans.comparatorProperty().bind(getTachesTable().comparatorProperty());
        getTachesTable().setItems(sortedFilteredPlanifBeans);

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
        //noinspection OverlyComplexAnonymousInnerClass
        projetAppliColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<ProjetAppliBean>() {

                    @Null
                    @Override
                    public String toString(@Null ProjetAppliBean projetAppliBean) {
                        if (projetAppliBean == null) {
                            return null;
                        }
                        return projetAppliBean.getCode();
                    }

                    @Null
                    @Override
                    public ProjetAppliBean fromString(@Null String codeProjetAppli) {
                        if (codeProjetAppli == null) {
                            return null;
                        }
                        return Collections.any(
                                planChargeBean.getProjetsApplisBeans(),
                                projetAppliBean -> (projetAppliBean.getCode() != null) && projetAppliBean.getCode().equals(codeProjetAppli)
                        );
                    }
                },
                planChargeBean.getProjetsApplisBeans())
        );
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
        echeanceColumn.setCellFactory(DatePickerTableCells.forRequiredTableColumn());
        importanceColumn.setCellFactory(cell -> new ImportanceCell<>(planChargeBean.getImportancesBeans()));
        chargeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        //noinspection OverlyComplexAnonymousInnerClass
        ressourceColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<RessourceBean>() {
                    @Null
                    @Override
                    public String toString(@Null RessourceBean ressourceBean) {
                        if (ressourceBean == null) {
                            return null;
                        }
                        return ressourceBean.getCode();
                    }

                    @Override
                    @Null
                    public RessourceBean fromString(@Null String trigramme) {
                        if (trigramme == null) {
                            return null;
                        }
                        return Collections.any(
                                planChargeBean.getRessourcesBeans(),
                                ressourceBean -> (ressourceBean.getCode() != null) && ressourceBean.getCode().equals(trigramme)
                        );
                    }
                },
                planChargeBean.getRessourcesBeans()));
        //noinspection OverlyComplexAnonymousInnerClass
        profilColumn.setCellFactory(ComboBoxTableCell.forTableColumn(
                new StringConverter<ProfilBean>() {

                    @Null
                    @Override
                    public String toString(@Null ProfilBean profilBean) {
                        if (profilBean == null) {
                            return null;
                        }
                        return profilBean.getCode();
                    }

                    @Override
                    @Null
                    public ProfilBean fromString(@Null String codeProfil) {
                        if (codeProfil == null) {
                            return null;
                        }
                        return Collections.any(
                                planChargeBean.getProfilsBeans(),
                                profilBean -> (profilBean.getCode() != null) && profilBean.getCode().equals(codeProfil)
                        );
                    }
                },
                planChargeBean.getProfilsBeans()));

        // Paramétrage des ordres de tri :
        categorieColumn.setComparator(CodeCategorieTacheComparator.COMPARATEUR);
        importanceColumn.setComparator(ImportanceComparator.COMPARATEUR);

        // Ajout des filtres "globaux" (à la TableView, pas sur chaque TableColumn) :
        //
        filtreGlobalComponent.getFiltreGlobalField().textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.debug("Changement pour le filtre 'filtreGlobal' : {}...", newValue);
            filtrer();
        });

        // Ajout des filtres "par colonne" (sur des TableColumn, pas sur la TableView) :
        //
        TableViews.enableFilteringOnColumns(getTachesTable(), categorieColumn, sousCategorieColumn, noTacheColumn, noTicketIdalColumn, descriptionColumn, projetAppliColumn, statutColumn, debutColumn, echeanceColumn, importanceColumn, ressourceColumn, chargeColumn, profilColumn);

        // Gestion des undo/redo :
        //
        //noinspection ClassHasNoToStringMethod,LimitedScopeInnerClass
        final class TacheTableCommitHandler<T> implements EventHandler<TableColumn.CellEditEvent<TB, T>> {

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

        // FIXME FDA 2017/08 Ne fonctionne que si l'utilisateur a focusé sur la table avant de taper Ctrl+F. Il faudrait ajouter le handler sur la Scene du "primary" Stage, mais ce dernier n'est pas encore initialisé (NPE).
        getTachesTable().setOnKeyReleased(event -> {
            if (event.isControlDown() && (event.getCode() == KeyCode.F)) {
                filtreGlobalComponent.show();
                filtreGlobalComponent.requestFocus();
                event.consume(); // TODO FDA 2017/08 Confirmer.
                return;
            }
            if (event.getCode() == KeyCode.DELETE) {
                if (!getTachesTable().isFocused()) {
                    return;
                }
                supprimerTacheSelectionnee();
                event.consume(); // TODO FDA 2017/08 Confirmer.
                //noinspection UnnecessaryReturnStatement
                return;
            }
        });

        LOGGER.info("Initialisé.");
    }


/*
    */

    /**
     * Gagne à être surchargé, en veillant à appeler <code>super.definirMenuContextuel()</code>.
     *//*

    void definirMenuContextuel() {

        MenuItem menuVoirRessourceHumaine = new MenuItem("Voir la ressource humaine");
        menuVoirRessourceHumaine.setOnAction(event -> afficherRessourceHumaine());

        MenuItem menuVoirOutilTicketing = new MenuItem("Voir la tâche dans l'outil de ticketing");
        menuVoirOutilTicketing.setOnAction(event -> afficherTacheDansOutilTicketing());

        tachesTableContextMenu.getItems().setAll(menuVoirRessourceHumaine, menuVoirOutilTicketing);
    }
*/
    @NotNull
    protected TB ajouterTache() throws Exception {
        LOGGER.debug("ajouterTache...");
        try {
            TB nouvTache = nouveauBean();
            getTachesBeans().add(nouvTache);

            // Positionnement sur la tâche qu'on vient d'ajouter :
            TableViews.editCell(getTachesTable(), nouvTache, descriptionColumn);

            getTachesTable().refresh(); // Notamment pour recalculer les styles CSS.

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

        filtreGlobalComponent.getFiltreGlobalField().setText("");
        // TODO FDA 2017/08 Décocher tous les autres filtres.
        filtreCategorieToutToggleButton.setSelected(true); // TODO FDA 2017/08 Tester.
        filtreStatutToutToggleButton.setSelected(true); // TODO FDA 2017/08 Tester.
        filtrePeriodeToutToggleButton.setSelected(true); // TODO FDA 2017/08 Tester.

        filtrer();
    }

    @FXML
    private void filtrer(@SuppressWarnings("unused") ActionEvent event) {
        filtrer();
    }

    private void filtrer() {
        filteredTachesBeans.setPredicate(this::estTacheAVoir);
    }

    private boolean estTacheAVoir(@NotNull TB tache) {

        if (Strings.epure(filtreGlobalComponent.getFiltreGlobalField().getText()) != null) {
            if (!tache.matcheGlobal(filtreGlobalComponent.getFiltreGlobalField().getText())) {
                return false;
            }
        }
        if (!estTacheAvecCategorieAVoir(tache)) return false;
        if (!estTacheAvecStatutAVoir(tache)) return false;
        if (!estTacheAvecPeriodeAVoir(tache)) return false;

        // Par défaut, on affiche les tâches :
        return true;
    }

    @SuppressWarnings("MethodWithMoreThanThreeNegations")
    private boolean estTacheAvecStatutAVoir(@NotNull TB tache) {
        if (filtreStatutNouveauToggleButton.isSelected()) {
            //noinspection ConstantConditions
            return tache.matcheStatut(StatutDTO.NOUVEAU.getCode());
        }
        if (filtreStatutEnCoursToggleButton.isSelected()) {
            //noinspection ConstantConditions
            return tache.matcheStatut(StatutDTO.EN_COURS.getCode());
        }
        if (filtreStatutEnAttenteToggleButton.isSelected()) {
            //noinspection ConstantConditions
            return tache.matcheStatut(StatutDTO.EN_ATTENTE.getCode());
        }
        if (filtreStatutRecurrentToggleButton.isSelected()) {
            //noinspection ConstantConditions
            if (!tache.matcheStatut(StatutDTO.RECURRENT.getCode())) {
                return false;
            }
        }
        if (filtreStatutReporteToggleButton.isSelected()) {
            //noinspection ConstantConditions
            return tache.matcheStatut(StatutDTO.REPORTE.getCode());
        }
        if (filtreStatutClosToggleButton.isSelected()) {
            //noinspection ConstantConditions
            return tache.matcheStatut(StatutDTO.ANNULE.getCode())
                    || tache.matcheStatut(StatutDTO.DOUBLON.getCode())
                    || tache.matcheStatut(StatutDTO.TERMINE.getCode());
        }
        return true;
    }

    private boolean estTacheAvecCategorieAVoir(@NotNull TB tache) {
        if (filtreCategorieProjetToggleButton.isSelected()) {
            return tache.matcheCategorie(CategorieTacheDTO.PROJET.getCode());
        }
        if (filtreCategorieServiceToggleButton.isSelected()) {
            return tache.matcheCategorie(CategorieTacheDTO.SERVICE.getCode());
        }
        if (filtreCategorieOrganisationToggleButton.isSelected()) {
            return tache.matcheCategorie(CategorieTacheDTO.ORGANISATION_INTERNE.getCode());
        }
        return true;
    }

    private boolean estTacheAvecPeriodeAVoir(@NotNull TB tache) {
        if (filtrePeriodeEchueToggleButton.isSelected()) {
            if ((tache.getEcheance() != null) && (planChargeBean.getDateEtat() != null)) {
                return !tache.getEcheance().isAfter(planChargeBean.getDateEtat());
            }
        }
        return true;
    }


    @SuppressWarnings("unused")
    @FXML
    private void afficherRessourceHumaine(ActionEvent actionEvent) {
        afficherRessourceHumaine();
    }

    private void afficherRessourceHumaine() {
        TB tacheBean = TableViews.selectedItem(getTachesTable());
        if (tacheBean == null) {
            //noinspection HardcodedLineSeparator
            ihm.afficherPopUp(
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
            ihm.afficherPopUp(
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
            ihm.afficherPopUp(
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
            ihm.afficherPopUp(
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


    @SuppressWarnings("FinalPrivateMethod")
    @FXML
    private final void supprimer(@NotNull ActionEvent actionEvent) {
        LOGGER.debug("supprimer...");
        supprimerTacheSelectionnee();
    }

    @SuppressWarnings("FinalPrivateMethod")
    private final void supprimerTacheSelectionnee() {
        TB focusedItem = TableViews.selectedItem(getTachesTable());
        if (focusedItem == null) {
            LOGGER.debug("Aucune tâche sélectionnée, donc on en sait pas que supprimer, on ne fait rien.");
            return;
        }
        supprimer(focusedItem);
    }

    @SuppressWarnings("FinalPrivateMethod")
    private final void supprimer(@NotNull TB tacheBean) {
        getTachesBeans().remove(tacheBean);
        TableViews.clearSelection(getTachesTable()); // Contournement pour [issue#84:1 appui sur DELETE supprime 2 lignes]. Sinon, 2 lignes sont supprimées. Va savoir pourquoi...
    }

}
