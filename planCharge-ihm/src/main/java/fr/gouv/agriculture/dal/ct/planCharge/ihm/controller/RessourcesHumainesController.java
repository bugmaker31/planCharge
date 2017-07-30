package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.TextFieldTableCells;
import fr.gouv.agriculture.dal.ct.ihm.view.UpperCaseTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.RessourceHumaineBean;
import javafx.collections.ObservableList;
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

    @NotNull
    private ObservableList<RessourceHumaineBean> ressourceHumainesBeans = planChargeBean.getRessourcesHumainesBeans();


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

        // Paramétrage de l'affichage des valeurs des colonnes (mode "consultation") :
        trigrammeColumn.setCellValueFactory(cellData -> cellData.getValue().trigrammeProperty());
        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomColumn.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        societeColumn.setCellValueFactory(cellData -> cellData.getValue().societeProperty());
        debutMissionColumn.setCellValueFactory(cellData -> cellData.getValue().debutMissionProperty());
        finMissionColumn.setCellValueFactory(cellData -> cellData.getValue().finMissionProperty());

        // Paramétrage de la saisie des valeurs des colonnes (mode "édition") :
        PlanChargeIhm.symboliserChampObligatoire(trigrammeColumn);
        trigrammeColumn.setCellFactory(param -> {
            TextFieldTableCell<RessourceHumaineBean, String> trigrammeCell = new UpperCaseTextFieldTableCell<>();
//            PlanChargeIhm.symboliserChampObligatoire(trigrammeCell);
            PlanChargeIhm.controler(trigrammeCell, "Trigramme incorrect", this::validerTrigramme);
            return trigrammeCell;
        });
        PlanChargeIhm.symboliserChampObligatoire(nomColumn);
        nomColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, String> cell = TextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Nom incorrect", this::validerNom);
            return cell;
        });
        PlanChargeIhm.symboliserChampObligatoire(prenomColumn);
        prenomColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, String> cell = TextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Prénom incorrect", this::validerPrenom);
            return cell;
        });
        PlanChargeIhm.symboliserChampObligatoire(societeColumn);
        societeColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, String> cell = TextFieldTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Société incorrecte", this::validerSociete);
            return cell;
        });
        PlanChargeIhm.symboliserChampObligatoire(debutMissionColumn);
        debutMissionColumn.setCellFactory(param -> {
            TableCell<RessourceHumaineBean, LocalDate> cell = DatePickerTableCells.<RessourceHumaineBean>forRequiredTableColumn().call(param);
            PlanChargeIhm.controler(cell, "Date de début incorrecte", this::validerDebutMission);
            return cell;
        });
        finMissionColumn.setCellFactory(DatePickerTableCells.forTableColumn());

        ressourcesHumainesTable.setItems(ressourceHumainesBeans);

        TableFilter.Builder<RessourceHumaineBean> filter = TableFilter.forTableView(ressourcesHumainesTable);
//        filter.lazy(true); // TODO FDA 2017/07 Confirmer (ne semble rien changer).
        //noinspection unused
        TableFilter<RessourceHumaineBean> ressourcesHumainesTableFilter = filter.apply();
        getIhm().symboliserFiltrable(trigrammeColumn, nomColumn, prenomColumn, societeColumn, debutMissionColumn, finMissionColumn);
/*
        ressourceHumainesBeans.addListener((ListChangeListener<RessourceHumaineBean>) changeListener -> {
//            ressourcesHumainesTable.setPrefHeight(Region.USE_COMPUTED_SIZE);
//            ressourcesHumainesTableFilter.executeFilter();
        });
*/

        LOGGER.debug("Initialisé.");
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
        int idxLigNouvBean = ressourcesHumainesTable.getItems().indexOf(nouvRsrcHumaine);
        assert idxLigNouvBean != -1;
        ressourcesHumainesTable.scrollTo(idxLigNouvBean);
        ressourcesHumainesTable.getSelectionModel().clearAndSelect(idxLigNouvBean);
//        ressourcesHumainesTable.getSelectionModel().focus(idxLigNouvBean);
        // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en mode édition de la cellule.
        ressourcesHumainesTable.edit(idxLigNouvBean, trigrammeColumn);
//        ressourcesHumainesTable.refresh();
    }
}
