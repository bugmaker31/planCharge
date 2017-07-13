package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.javafx.DatePickerCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.JourFerieBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.RessourceHumaineBean;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

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
        class TrigrammeFieldTableCell extends TextFieldTableCell<RessourceHumaineBean,String> {

            public TrigrammeFieldTableCell() {
                super(new DefaultStringConverter());
            }

            @Override
            public void commitEdit(String newValue) {
                ihm.enleverErreurSaisie(this);
                String erreur = validerTrigramme(newValue);
                if (erreur != null) {
                    ihm.afficherErreurSaisie(this, erreur);
                    return;
                }
                super.commitEdit(newValue);
            }
        }
        trigrammeColumn.setCellFactory(param -> new TrigrammeFieldTableCell());
        nomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        prenomColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        societeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        debutMissionColumn.setCellFactory(p -> new DatePickerCell<>(PlanChargeIhm.FORMAT_DATE, ressourceHumainesBeans, RessourceHumaineBean::setDebutMission));
        finMissionColumn.setCellFactory(p -> new DatePickerCell<>(PlanChargeIhm.FORMAT_DATE, ressourceHumainesBeans, RessourceHumaineBean::setFinMission));

/*
        trigrammeColumn.setOnEditCommit((TableColumn.CellEditEvent<RessourceHumaineBean, String> change) -> {
            String erreur = validerTrigramme(change.getNewValue());
            if (erreur != null) {
                TextFieldTableCell<RessourceHumaineBean, String> cell = change.getTablePosition();
                ihm.afficherErreurSaisie(cell, erreur);
                RessourceHumaineBean rhb = change.getRowValue();
                rhb.setTrigramme(change.getOldValue());
            }
            change.consume(); // TODO FDA 2017/07 Confirmer qu'il faut indiquer que le Event est consommé.
        });
*/

        // Association (binding) entre la liste et la table des jours fériés :
        ressourceHumainesBeans.addListener((ListChangeListener<RessourceHumaineBean>) changeListener -> {
            // Wrap the FilteredList in a SortedList.
            final SortedList<RessourceHumaineBean> sortedBeans = new SortedList<>(ressourceHumainesBeans);
            // Bind the SortedList COMPARATOR_DEFAUT to the TableView COMPARATOR_DEFAUT.
            sortedBeans.comparatorProperty().bind(ressourcesHumainesTable.comparatorProperty());
            // Add sorted data to the table.
            ressourcesHumainesTable.setItems(sortedBeans);
        });

        LOGGER.debug("Initialisé.");
    }

    @Null
    private String validerTrigramme(@Null String trigramme) {
        if (trigramme == null) {
            return "Un trigramme est obligatoire.";
        }
        Set<String> trigrammes = ressourceHumainesBeans.parallelStream()
                .map(RessourceHumaineBean::getTrigramme)
                .collect(Collectors.toSet());
        if (trigrammes.contains(trigramme)) {
            return "Deux ressources ne peuvent avoir le même trigramme.";
        }
        return null;
    }

    @FXML
    private void ajouterRessourceHumaine(@SuppressWarnings("unused") ActionEvent actionEvent) {
        LOGGER.debug("ajouterRessourceHumaine...");

        RessourceHumaineBean nouvRsrcHumaine = new RessourceHumaineBean();
        ressourceHumainesBeans.add(nouvRsrcHumaine);

        // Positionnement sur la ressource qu'on vient d'ajouter :
        int idxLigNouvBean = ressourceHumainesBeans.indexOf(nouvRsrcHumaine);
        assert idxLigNouvBean != -1;
        ressourcesHumainesTable.scrollTo(idxLigNouvBean);
        ressourcesHumainesTable.getSelectionModel().clearAndSelect(idxLigNouvBean);
//        ressourcesHumainesTable.getSelectionModel().focus(idxLigNouvBean);
        // FIXME FDA 2017/05 Ne fonctionne pas, on ne passe pas automatiquement en mode édition de la cellule.
        ressourcesHumainesTable.edit(idxLigNouvBean, trigrammeColumn);
//        ressourcesHumainesTable.refresh();
    }
}
