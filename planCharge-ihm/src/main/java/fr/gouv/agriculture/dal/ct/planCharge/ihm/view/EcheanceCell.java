package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.DatePickerTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.TachesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import javafx.css.PseudoClass;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@SuppressWarnings("ClassHasNoToStringMethod")
public class EcheanceCell<TB extends TacheBean> extends DatePickerTableCell<TB> {

    public static final PseudoClass ECHUE = TachesController.ECHUE;

    //    @Autowired
    @NotNull
    // 'final' pour éviter que quiconque resette cette variable.
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();


    @NotNull
    private final TableView table;


    public EcheanceCell(@NotNull String format, @NotNull String promptText, @NotNull TableView table) {
        super(format, promptText);
        this.table = table;
    }


    @Override
    public void updateItem(@Null LocalDate item, boolean empty) {
        super.updateItem(item, empty);
//        formater();
        styler();
    }

    private void formater() {
        if (isEmpty() || (getItem() == null)) {
            setText(null);
            setGraphic(null);
            return;
        }
        setText(getConverter().toString(getItem()));
    }

    protected void styler() {

        // Réinit du style de la cellule :
        pseudoClassStateChanged(ECHUE, false);

        // Stop, si cellule vide :
        if (isEmpty() || (getItem() == null)) {
            return;
        }

        TB tacheBean = tacheBean();
        if (tacheBean == null) {
            return;
        }

        //noinspection UnnecessaryLocalVariable
        LocalDate echeance = getItem();

        if (planChargeBean.getDateEtat() == null) {
            return;
        }
        LocalDate dateEtat = planChargeBean.getDateEtat();

        // Formatage du style (CSS) de la cellule :
        if (dateEtat.isAfter(echeance)) {
            pseudoClassStateChanged(ECHUE, true);
        }
    }

    /**
     * Récupération des infos sur la cellule.
     */
    @Null
    protected TB tacheBean() {

        //noinspection unchecked
        TableRow<TB> tableRow = getTableRow();
        if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
            // Rq : On tombe dans ce cas-là dans le cas de ce DatePickerTableCell (pas pour PlanificationChargeCell).
            return null;
        }

        TB tacheBean = tableRow.getItem();
        if (tacheBean == null) {
            return null;
        }

        return tacheBean;
    }
}
