package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.Executeur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBeans;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.TacheService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

@SuppressWarnings("ClassHasNoToStringMethod")
public class BarreEtatTachesController<TB extends TacheBean> extends AbstractController {

    // Fields:

    // Métier

    //    @Autowired
    @NotNull
    private TacheService tacheService = TacheService.instance();

    // View

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private Label nbrTachesATraiterLabel;

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private Label totalResteAFaireLabel;

    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private Label nbrTachesAfficheesLabel;

    @SuppressWarnings("NullableProblems")
    @NotNull
    private ObservableList<TB> tachesBeans;

    @SuppressWarnings("NullableProblems")
    @NotNull
    private TableView<TB> tachesTable;

    // Autre

    @SuppressWarnings("NullableProblems")
    @NotNull
    private Executeur fonctionAjouterTache;


    // Getters/Setters:

    public void setTachesBeans(@NotNull ObservableList<TB> tachesBeans) {
        this.tachesBeans = tachesBeans;
    }

    public void setTachesTable(@NotNull TableView<TB> tachesTable) {
        this.tachesTable = tachesTable;
    }

    public void setFonctionAjouterTache(@NotNull Executeur fonctionAjouterTache) {
        this.fonctionAjouterTache = fonctionAjouterTache;
    }


    // Methods:

    @Override
    protected void initialize() throws ControllerException {
        // Rien... pour l'instant.
    }

    public void prepare() {
        NumberFormat formatNbrTaches = new DecimalFormat("#,##0");
        NumberFormat formatCharge = new DecimalFormat("#,##0.###");
        tachesBeans.addListener((ListChangeListener<? super TacheBean>) change -> {

            //noinspection unchecked
            List<TacheDTO> tacheDTOs = TacheBeans.<TacheDTO>toDTO((List<TacheBean>) tachesBeans);
            List<TacheDTO> tacheATraiterDTOs = tacheService.tachesATraiter(tacheDTOs);

            long nbrTachesAAfficher = tacheATraiterDTOs.size();
            nbrTachesATraiterLabel.setText(formatNbrTaches.format(nbrTachesAAfficher));

            Double totalRAF = tacheATraiterDTOs.parallelStream()
                    .mapToDouble(tacheBean -> Objects.value(tacheBean.getCharge(), 0.0))
                    .sum();
            totalResteAFaireLabel.setText(formatCharge.format(totalRAF));
        });
        tachesTable.getItems().addListener((ListChangeListener<? super TacheBean>) change -> {
            long nbrTachesAffichees = tachesTable.getItems().size();
            nbrTachesAfficheesLabel.setText(formatNbrTaches.format(nbrTachesAffichees));
        });
    }

    @FXML
    private void ajouterTache(@NotNull ActionEvent actionEvent) throws ControllerException {
        try {
            fonctionAjouterTache.executer();
        } catch (IhmException e) {
            throw new ControllerException("Impossible d'exécuter la fonction d'ajout d'une tâche.", e);
        }
    }
}
