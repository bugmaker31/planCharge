package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.ihm.view.TextFieldTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.UpperCaseTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import org.controlsfx.control.table.TableFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 13/07/2017.
 *
 * @author frederic.danna
 */
public class RessourcesHumainesController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RessourcesHumainesController.class);

    private static RessourcesHumainesController instance;

    public static RessourcesHumainesController instance() {
        return instance;
    }


    // Couche "métier" :

    //    @Autowired
    @NotNull
    private PlanChargeBean planChargeBean = PlanChargeBean.instance();

    // 'final' car personne ne doit (re)set'er cette ObservableList, sinon on perdra les Listeners qu'on a enregistré dessus.
    @NotNull
    private final ObservableList<RessourceHumaineBean> ressourceHumainesBeans = FXCollections.observableArrayList();

/*
    //    @Autowired
    @NotNull
    private final ReferentielsService referentielsService = ReferentielsService.instance();
*/


    // Couche "vue" :

    //    @Autowired
    @NotNull
    private PlanChargeIhm ihm = PlanChargeIhm.instance();
    @FXML
    @NotNull
    private TableView<RessourceHumaineBean> ressourcesHumainesTable;
    @FXML
    @NotNull
    private TableColumn<RessourceHumaineBean, String> trigrammeColumn;
    @FXML
    @NotNull
    private TableColumn<RessourceHumaineBean, String> nomColumn;
    @FXML
    @NotNull
    private TableColumn<RessourceHumaineBean, String> prenomColumn;
    @FXML
    @NotNull
    private TableColumn<RessourceHumaineBean, String> societeColumn;
    @FXML
    @NotNull
    private TableColumn<RessourceHumaineBean, LocalDate> debutMissionColumn;
    @FXML
    @NotNull
    private TableColumn<RessourceHumaineBean, LocalDate> finMissionColumn;


    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RessourcesHumainesController() throws IhmException {
        super();
        if (instance != null) {
            throw new IhmException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws IhmException {
        LOGGER.debug("Initialisation...");

/*
        FilteredList<RessourceHumaineBean> rsrcHumainesBeans = (FilteredList<RessourceHumaineBean>) planChargeBean.getRessourcesBeans().filtered(rsrcBean -> rsrcBean instanceof RessourceHumaineBean);
        Bindings.bindContentBidirectional(ressourceHumainesBeans, rsrcHumainesBeans);
*/
        planChargeBean.getRessourcesBeans().addListener((ListChangeListener<? super RessourceBean>) change -> {
            while (change.next()) {
                //If items are removed
                RessourceBean[] removedRessourceBeans = change.getRemoved().toArray(new RessourceBean[0]); // On passe par un Array pour éviter le ConcurrentmodificationException.
                for (RessourceBean removedRessourceBean : removedRessourceBeans) {
                    if (removedRessourceBean instanceof RessourceHumaineBean) {
                        if (ressourceHumainesBeans.contains(removedRessourceBean)) {
                            ressourceHumainesBeans.remove(removedRessourceBean);
                        }
                    }
                }
                //If items are added
                RessourceBean[] addedRessourceBeans = change.getAddedSubList().toArray(new RessourceBean[0]); // On passe par un Array pour éviter le ConcurrentmodificationException.
                for (RessourceBean addedRessourceBean : addedRessourceBeans) {
                    if (addedRessourceBean instanceof RessourceHumaineBean) {
                        if (!ressourceHumainesBeans.contains(addedRessourceBean)) {
                            ressourceHumainesBeans.add((RessourceHumaineBean) addedRessourceBean);
                        }
                    }
                }
            }
        });
        ressourceHumainesBeans.addListener((ListChangeListener<? super RessourceHumaineBean>) change -> {
            while (change.next()) {
                //If items are removed
                RessourceHumaineBean[] removedRessourceBeans = change.getRemoved().toArray(new RessourceHumaineBean[0]); // On passe par un Array pour éviter le ConcurrentmodificationException.
                for (RessourceHumaineBean removedRessourceBean : removedRessourceBeans) {
                    if (planChargeBean.getRessourcesBeans().contains(removedRessourceBean)) {
                        planChargeBean.getRessourcesBeans().remove(removedRessourceBean);
                    }
                }
                //If items are added
                RessourceHumaineBean[] addedRessourceBeans = change.getAddedSubList().toArray(new RessourceHumaineBean[0]); // On passe par un Array pour éviter le ConcurrentmodificationException.
                for (RessourceHumaineBean addedRessourceBean : addedRessourceBeans) {
                    if (!planChargeBean.getRessourcesBeans().contains(addedRessourceBean)) {
                        planChargeBean.getRessourcesBeans().add(addedRessourceBean);
                    }
                }
            }
        });

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        trigrammeColumn.setCellValueFactory(cellData -> cellData.getValue().trigrammeProperty());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        societeColumn.setCellValueFactory(cellData -> cellData.getValue().societeProperty());
        debutMissionColumn.setCellValueFactory(cellData -> cellData.getValue().debutMissionProperty());
        finMissionColumn.setCellValueFactory(cellData -> cellData.getValue().finMissionProperty());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        PlanChargeIhm.symboliserColonnesObligatoires(trigrammeColumn);
        trigrammeColumn.setCellFactory(param -> {
            TextFieldTableCell<RessourceHumaineBean, String> trigrammeCell = new UpperCaseTextFieldTableCell<>();
//            PlanChargeIhm.symboliserColonnesObligatoires(trigrammeCell);
            PlanChargeIhm.controler(trigrammeCell, "Trigramme incorrect", this::validerTrigramme);
            return trigrammeCell;
        });
        PlanChargeIhm.symboliserColonnesObligatoires(nomColumn);
        nomColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, String> cell = TextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Nom incorrect", this::validerNom);
            return cell;
        });
        PlanChargeIhm.symboliserColonnesObligatoires(prenomColumn);
        prenomColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, String> cell = TextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Prénom incorrect", this::validerPrenom);
            return cell;
        });
        PlanChargeIhm.symboliserColonnesObligatoires(societeColumn);
        societeColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, String> cell = TextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Société incorrecte", this::validerSociete);
            return cell;
        });
/*
        PlanChargeIhm.symboliserColonnesObligatoires(debutMissionColumn);
        debutMissionColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, LocalDate> cell = DatePickerTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Date de début incorrecte", this::validerDebutMission);
            return cell;
        });
*/
        debutMissionColumn.setCellFactory(DatePickerTableCells.forTableColumn());
        finMissionColumn.setCellFactory(DatePickerTableCells.forTableColumn());

        SortedList<RessourceHumaineBean> sortedBeans = new SortedList<>(ressourceHumainesBeans);
        sortedBeans.comparatorProperty().bind(ressourcesHumainesTable.comparatorProperty());

        ressourcesHumainesTable.setItems(sortedBeans);

        TableFilter.Builder<RessourceHumaineBean> filterBuilder = TableFilter.forTableView(ressourcesHumainesTable);
//        filter.lazy(true); // TODO FDA 2017/07 Confirmer (ne semble rien changer).
        filterBuilder.apply();
        ihm.symboliserColonnesFiltrables(trigrammeColumn, nomColumn, prenomColumn, societeColumn, debutMissionColumn, finMissionColumn);

/*
        ressourceHumainesBeans.addListener((ListChangeListener<RessourceHumaineBean>) changeListener -> {
//            ressourcesHumainesTable.setPrefHeight(Region.USE_COMPUTED_SIZE);
//            ressourcesHumainesTableFilter.executeFilter();
        });
*/

        definirMenuContextuel();

        definirTouches();

        LOGGER.info("Initialisé.");
    }

    private void definirMenuContextuel() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuVoirTache = new MenuItem("Supprimer");
        menuVoirTache.setOnAction(this::supprimerRessourceHumaine);

        contextMenu.getItems().setAll(menuVoirTache);

        ressourcesHumainesTable.setContextMenu(contextMenu);
    }

    private void definirTouches() {
        // Cf. https://stackoverflow.com/questions/27314495/delete-javafx-table-row-with-delete-key
        ressourcesHumainesTable.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE:
                    supprimerRessourceHumaine(ressourcesHumainesTable.getSelectionModel().getSelectedItem());
                    break;
                default:
                    LOGGER.debug("Touche ignorée : '" + event.getCode() + "'.");
            }
        });
    }


    @Null
    private String validerTrigramme(@Null String trigramme) {
        if ((trigramme == null) || trigramme.isEmpty()) {
            return "Un trigramme est obligatoire.";
        }
        long nbrOccurrencesTrigramme = ressourceHumainesBeans.parallelStream()
                .map(RessourceHumaineBean::getTrigramme)
                .filter(s -> (s != null) && s.equalsIgnoreCase(trigramme))
                .count();
        if (nbrOccurrencesTrigramme >= 2) {
            return "Deux ressources ne peuvent avoir le même trigramme.";
        }
        return null;
    }

    @Null
    private String validerNom(@Null String nom) {
        if ((nom == null) || nom.trim().isEmpty()) {
            return "Un nom est obligatoire.";
        }
        return null;
    }

    @Null
    private String validerPrenom(@Null String prenom) {
        if ((prenom == null) || prenom.trim().isEmpty()) {
            return "Un prénom est obligatoire.";
        }
        return null;
    }

    @Null
    private String validerSociete(@Null String societe) {
        if ((societe == null) || societe.trim().isEmpty()) {
            return "La société est obligatoire.";
        }
        return null;
    }

    @Null
    private String validerDebutMission(@Null LocalDate debutMission) {
        if (debutMission == null) {
            return "Le début de mission est obligatoire.";
        }
        return null;
    }


    @FXML
    private void ajouterRessourceHumaine(@SuppressWarnings("unused") ActionEvent actionEvent) {
        LOGGER.debug("ajouterRessourceHumaine...");

        RessourceHumaineBean nouvRsrcHumaine = new RessourceHumaineBean();
        ressourceHumainesBeans.add(nouvRsrcHumaine);

        // Positionnement sur la ressource qu'on vient d'ajouter :
        TableViews.editCell(ressourcesHumainesTable, nouvRsrcHumaine, trigrammeColumn);
    }


    @FXML
    private void supprimerRessourceHumaine(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) {
        LOGGER.debug("supprimerJourFerie...");

        RessourceHumaineBean focusedItem = TableViews.selectedItem(ressourcesHumainesTable);
        if (focusedItem == null) {
            LOGGER.debug("Aucun item sélectionné, donc on en sait pas que supprimer, on ne fait rien.");
            return;
        }
        supprimerRessourceHumaine(focusedItem);
    }

    private void supprimerRessourceHumaine(@NotNull RessourceHumaineBean focusedItem) {
        LOGGER.debug("supprimerRessourceHumaine...");
        ressourceHumainesBeans.remove(focusedItem);
    }

}
