package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.ihm.view.UpperCaseTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ActionUtilisateur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.AffichageModuleRessourcesHumaines;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created by frederic.danna on 13/07/2017.
 *
 * @author frederic.danna
 */
public class RessourcesHumainesController extends AbstractController implements Module {

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
    private final ObservableList<RessourceHumaineBean> ressourceHumainesBeans = planChargeBean.getRessourcesHumainesBeans();

/*
    //    @Autowired
    @NotNull
    private final ReferentielsService referentielsService = ReferentielsService.instance();
*/


    // Couche "vue" :


    @Override
    public Node getView() {
        return ihm.getRessourcesHumainesView();
    }

    @NotNull
    @Override
    public ActionUtilisateur actionUtilisateurAffichageModule(@Null Module modulePrecedent) {
        return new AffichageModuleRessourcesHumaines(modulePrecedent);
    }

    /*
            //    @Autowired
            @NotNull
            private PlanChargeIhm ihm = PlanChargeIhm.instance();
        */
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


    // Constructeurs :

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RessourcesHumainesController() throws ControllerException {
        super();
        if (instance != null) {
            throw new ControllerException("Instanciation à plus d'1 exemplaire.");
        }
        instance = this;
    }


    // Getters/Setters

    @NotNull
    public TableView<RessourceHumaineBean> getRessourcesHumainesTable() {
        return ressourcesHumainesTable;
    }


    // Méthodes :

    // Module

    @Override
    public String getTitre() {
        return "Ressources humaines";
    }


    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    protected void initialize() throws ControllerException {
        try {

            // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
            trigrammeColumn.setCellValueFactory(cellData -> cellData.getValue().trigrammeProperty());
            nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
            prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
            societeColumn.setCellValueFactory(cellData -> cellData.getValue().societeProperty());
            debutMissionColumn.setCellValueFactory(cellData -> cellData.getValue().debutMissionProperty());
            finMissionColumn.setCellValueFactory(cellData -> cellData.getValue().finMissionProperty());

            // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
            TableViews.decorateMandatoryColumns(trigrammeColumn);
            trigrammeColumn.setCellFactory(param -> {
                TextFieldTableCell<RessourceHumaineBean, String> trigrammeCell = new UpperCaseTextFieldTableCell<>();
//            PlanChargeIhm.decorateMandatoryColumns(trigrammeCell);
                ihm.controler(trigrammeCell, "Trigramme incorrect", this::validerTrigramme);
                return trigrammeCell;
            });
            TableViews.decorateMandatoryColumns(nomColumn);
            nomColumn.setCellFactory(param -> {
                TableCell<RessourceHumaineBean, String> cell = EditableAwareTextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
                ihm.controler(cell, "Nom incorrect", this::validerNom);
                return cell;
            });
            TableViews.decorateMandatoryColumns(prenomColumn);
            prenomColumn.setCellFactory(param -> {
                TableCell<RessourceHumaineBean, String> cell = EditableAwareTextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
                ihm.controler(cell, "Prénom incorrect", this::validerPrenom);
                return cell;
            });
            TableViews.decorateMandatoryColumns(societeColumn);
            societeColumn.setCellFactory(param -> {
                TableCell<RessourceHumaineBean, String> cell = EditableAwareTextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
                ihm.controler(cell, "Société incorrecte", this::validerSociete);
                return cell;
            });
/*
        PlanChargeIhm.decorateMandatoryColumns(debutMissionColumn);
        debutMissionColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, LocalDate> cell = DatePickerTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, cell.textProperty(), "Date de début incorrecte", this::validerDebutMission);
            return cell;
        });
*/
            debutMissionColumn.setCellFactory(DatePickerTableCells.forTableColumn());
            finMissionColumn.setCellFactory(DatePickerTableCells.forTableColumn());

            TableViews.ensureSorting(ressourcesHumainesTable, ressourceHumainesBeans);

            TableViews.enableFilteringOnColumns(ressourcesHumainesTable, Arrays.asList(trigrammeColumn, nomColumn, prenomColumn, societeColumn, debutMissionColumn, finMissionColumn));

            definirMenuContextuel();

            definirRaccourcisClavier();

        } catch (IhmException e) {
            throw new ControllerException("Impossible d'initialiser le contrôleur du module 'Ressources humaines'.", e);
        }
    }

    private void definirMenuContextuel() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuSupprimer = new MenuItem("Supprimer");
        menuSupprimer.setOnAction(this::supprimerRessourceHumaine);

        contextMenu.getItems().setAll(menuSupprimer);

        ressourcesHumainesTable.setContextMenu(contextMenu);
    }

    private void definirRaccourcisClavier() {
        // Cf. https://stackoverflow.com/questions/27314495/delete-javafx-table-row-with-delete-key
        ressourcesHumainesTable.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE:
                    supprimerRessourceHumaine(ressourcesHumainesTable.getSelectionModel().getSelectedItem());
                    break;
                default:
                    LOGGER.debug("Touche ignorée : '{}'.", event.getCode());
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
        if (nbrOccurrencesTrigramme >= 2L) {
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
