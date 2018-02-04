package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.Executeur;
import fr.gouv.agriculture.dal.ct.ihm.view.TableViews;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.StatutBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBeans;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.TacheService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings({"ClassHasNoToStringMethod", "ClassWithoutConstructor"})
public class BarreEtatTachesController<TB extends TacheBean> extends AbstractController {

    public static final NumberFormat FORMAT_NBR_TACHES = new DecimalFormat("#0");
    public static final NumberFormat FORMAT_CHARGE = new DecimalFormat("0.#");


    // Fields:

    private static final Logger LOGGER = LoggerFactory.getLogger(BarreEtatTachesController.class);

    private ObservableList<Button> actions;

    // Métier

    //    @Autowired
    @NotNull
    private final TacheService tacheService = TacheService.instance();

    // View

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private HBox boutonsHBox;


    @SuppressWarnings("NullableProblems")
    @NotNull
    @FXML
    private Label nbrTachesTotalLabel;

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
    @FXML
    @NotNull
    private HBox calculSommeBox;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private RadioMenuItem calculerSommeRadioMenuItem;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private Label calculSommeLabel;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private HBox calculNombreBox;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private RadioMenuItem calculerNombreRadioMenuItem;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private Label calculNombreLabel;

    @SuppressWarnings({"NullableProblems", "FieldHasSetterButNoGetter"})
    @NotNull
    private ObservableList<TB> tachesBeans;

    @SuppressWarnings({"NullableProblems", "FieldHasSetterButNoGetter"})
    @NotNull
    private TableView<TB> tachesTable;

    @SuppressWarnings("NullableProblems")
    @FXML
    @NotNull
    private Tooltip nbrTachesATraiterTooltip;

    // Autre

/*
    @SuppressWarnings({"NullableProblems", "FieldHasSetterButNoGetter"})
    @NotNull
    private Executeur fonctionAjouterTache;
*/


    // Getters/Setters:

    public void setTachesBeans(@NotNull ObservableList<TB> tachesBeans) {
        this.tachesBeans = tachesBeans;
    }

    public void setTachesTable(@NotNull TableView<TB> tachesTable) {
        this.tachesTable = tachesTable;
    }

/*
    public void setFonctionAjouterTache(@NotNull Executeur fonctionAjouterTache) {
        this.fonctionAjouterTache = fonctionAjouterTache;
    }
*/

    public void setActions(ObservableList<Button> actions) {
        this.actions = actions;
    }

    public ObservableList<Button> getActions() {
        return actions;
    }


    // Methods:

    @Override
    protected void initialize() throws ControllerException {
        nbrTachesATraiterTooltip.setText(nbrTachesATraiterTooltip.getText().replaceFirst("\\Q${CODE_STATUT_REPORTE}", StatutBean.REPORTE.getCode()));
    }

    public void prepare() {

        boutonsHBox.getChildren().setAll(actions);

        tachesBeans.addListener((ListChangeListener<? super TacheBean>) change -> {

            //noinspection unchecked
            List<TacheDTO> tacheDTOs = TacheBeans.<TacheDTO>toDTO((List<TacheBean>) tachesBeans);
            List<TacheDTO> tacheATraiterDTOs = tacheService.tachesATraiter(tacheDTOs);

            long nbrTachesTotal = (long) tacheDTOs.size();
            nbrTachesTotalLabel.setText(FORMAT_NBR_TACHES.format(nbrTachesTotal));

            long nbrTachesAAfficher = (long) tacheATraiterDTOs.size();
            nbrTachesATraiterLabel.setText(FORMAT_NBR_TACHES.format(nbrTachesAAfficher));

            Double totalRAF = tacheATraiterDTOs.parallelStream()
                    .mapToDouble(tacheBean -> Objects.value(tacheBean.getCharge(), 0.0))
                    .sum();
            totalResteAFaireLabel.setText(FORMAT_CHARGE.format(totalRAF));
        });
        tachesTable.getItems().addListener((ListChangeListener<? super TacheBean>) change -> {
            long nbrTachesAffichees = (long) tachesTable.getItems().size();
            nbrTachesAfficheesLabel.setText(FORMAT_NBR_TACHES.format(nbrTachesAffichees));
        });

        if (calculerSommeRadioMenuItem.isSelected()) {
            activerCalculSomme();
        }
        if (calculerNombreRadioMenuItem.isSelected()) {
            activerCalculNombre();
        }
    }


    @FXML
    private void modifierEcheances(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        LOGGER.debug("modifierEcheances...");
        ihm.getTachesController().modifierEcheances();
    }


    @FXML
    private void inverserCalculSomme(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        if (calculerSommeRadioMenuItem.isSelected()) { // Vient tout juste d'être (dé)coché/activé/sélectionné.
            activerCalculSomme();
        } else {
            desactiverCalculSomme();
        }
    }

    private static DecimalFormat DECIMAL_FORMATER = new DecimalFormat("0.###");

    private static final Function<Double, String> FORMATEUR_SOMME = somme -> {
        if (somme == null) {
            return "N/C";
        }
        return DECIMAL_FORMATER.format(somme);
    };

    @Null
    private ListChangeListener<? super TablePosition> calculSommeChangeListener = c -> {
        if (!calculerSommeRadioMenuItem.isSelected()) {
            return;
        }
        Double somme = TableViews.sumSelected(tachesTable);
        calculSommeLabel.setText(FORMATEUR_SOMME.apply(somme));
    };

    private void activerCalculSomme() {
        calculSommeBox.setVisible(true);
        calculSommeBox.setMaxWidth(Double.MAX_VALUE);
        if (!calculerSommeRadioMenuItem.isSelected()) {
            calculerSommeRadioMenuItem.setSelected(true);
        }
        tachesTable.getSelectionModel().getSelectedCells().addListener(calculSommeChangeListener);
    }

    private void desactiverCalculSomme() {
        calculSommeBox.setVisible(false);
        calculSommeBox.setMaxWidth(0);
        if (calculerSommeRadioMenuItem.isSelected()) {
            calculerSommeRadioMenuItem.setSelected(false);
        }
        //noinspection HardcodedFileSeparator
        calculSommeLabel.setText("N/C");
        tachesTable.getSelectionModel().getSelectedCells().removeListener(calculSommeChangeListener);
    }


    @FXML
    private void inverserCalculNombre(@SuppressWarnings("unused") @NotNull ActionEvent actionEvent) throws ControllerException {
        if (calculerNombreRadioMenuItem.isSelected()) { // Vient tout juste d'être (dé)coché/activé/sélectionné.
            activerCalculNombre();
        } else {
            desactiverCalculNombre();
        }
    }

    private static final Function<Long, String> FORMATEUR_NOMBRE = nombre -> {
        if (nombre == null) {
            return "N/C";
        }
        return nombre + "";
    };

    @Null
    private ListChangeListener<? super TablePosition> calculNombreChangeListener = c -> {
        if (!calculerNombreRadioMenuItem.isSelected()) {
            return;
        }
        Long nombre = TableViews.countSelected(tachesTable);
        calculNombreLabel.setText(FORMATEUR_NOMBRE.apply(nombre));
    };

    private void activerCalculNombre() {
        calculNombreBox.setVisible(true);
        calculNombreBox.setMaxWidth(Double.MAX_VALUE);
        if (!calculerNombreRadioMenuItem.isSelected()) {
            calculerNombreRadioMenuItem.setSelected(true);
        }
        tachesTable.getSelectionModel().getSelectedCells().addListener(calculNombreChangeListener);
    }

    private void desactiverCalculNombre() {
        calculNombreBox.setVisible(false);
        calculNombreBox.setMaxWidth(0);
        if (calculerNombreRadioMenuItem.isSelected()) {
            calculerNombreRadioMenuItem.setSelected(false);
        }
        //noinspection HardcodedFileSeparator
        calculNombreLabel.setText("N/C");
        tachesTable.getSelectionModel().getSelectedCells().removeListener(calculNombreChangeListener);
    }

}
