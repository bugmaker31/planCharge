package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.css.PseudoClass;
import javafx.scene.control.TableRow;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by frederic.danna on 11/05/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class PlanificationChargeCell extends EditableAwareTextFieldTableCell<PlanificationTacheBean, Double> {

    public static final PseudoClass AVANT_PERIODE_DEMANDEE = PseudoClass.getPseudoClass("avantPeriodeDemandee");
    public static final PseudoClass PENDANT_PERIODE_DEMANDEE = PseudoClass.getPseudoClass("pendantPeriodeDemandee");
    public static final PseudoClass APRES_PERIODE_DEMANDEE = PseudoClass.getPseudoClass("apresPeriodeDemandee");
    //
    public static final List<PseudoClass> PSEUDO_CLASSES = Arrays.asList(AVANT_PERIODE_DEMANDEE, PENDANT_PERIODE_DEMANDEE, APRES_PERIODE_DEMANDEE);

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(PlanificationChargeCell.class);

    private PlanChargeBean planChargeBean;
    private final int noSemaine;


    public PlanificationChargeCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        super(new DoubleStringConverter(), null); // TODO FDA 2017/04 Mieux formater les charges ?
        this.planChargeBean = planChargeBean;
        this.noSemaine = noSemaine;
    }

    @Override
    public void updateItem(@Null Double item, boolean empty) {
        super.updateItem(item, empty);
        formater(item, empty);
        styler(item);
    }

    private void formater(@Null Double item, boolean empty) {
        if ((item == null) || empty) {
            setText(null);
            return;
        }
        //noinspection UnnecessaryLocalVariable
        Double charge = item;
        setText((charge == 0.0) ? "" : ChargesController.FORMAT_CHARGE.format(charge));
    }

    private void styler(@SuppressWarnings("unused") @Null Double item) {

        // Réinit du texte et du style de la cellule :
        PSEUDO_CLASSES.parallelStream()
                .forEach(pseudoClass -> pseudoClassStateChanged(pseudoClass, false));

/* Non, surtout pas, sinon les cellules vides (donc avant et après la période planifiée), ne seront pas décorées.
        // Stop, si cellule vide :
        if (empty || (item == null)) {
            return;
        }
*/

        // Récupération des infos sur la cellule :
        //noinspection unchecked
        TableRow<PlanificationTacheBean> tableRow = getTableRow();
        if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
            return;
        }
        PlanificationTacheBean planifBean = tableRow.getItem();
        if (planifBean == null) {
            return;
        }

        if (planChargeBean.getDateEtat() == null) {
            return;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((long) ((noSemaine - 1) * 7)); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7L);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]

        // Formatage du style (CSS) de la cellule :
        if (planifBean.getDebut() != null) {
            if (debutPeriode.isBefore(planifBean.getDebut().minusDays(7L-1L))) { // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                pseudoClassStateChanged(AVANT_PERIODE_DEMANDEE, true);
                return;
            }
        }
        if (planifBean.getEcheance() != null) {
            if (finPeriode.isAfter(planifBean.getEcheance().plusDays(7L))) { // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                pseudoClassStateChanged(APRES_PERIODE_DEMANDEE, true);
                return;
            }
        }
        pseudoClassStateChanged(PENDANT_PERIODE_DEMANDEE, true);
    }

    @Override
    public void commitEdit(Double newValue) {
        super.commitEdit(newValue);

        Double charge = Objects.value(newValue, 0.0);

        //noinspection unchecked
        TableRow<PlanificationTacheBean> tableRow = getTableRow();
        if (tableRow == null) { // Un clic sur le TableHeaderRow fait retourner null à getTableRow().
            return;
        }
        PlanificationTacheBean planifBean = tableRow.getItem();
        if (planifBean == null) {
            return;
        }
        if (planChargeBean.getDateEtat() == null) {
            return;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((long) ((noSemaine - 1) * 7)); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7L);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]

        // TODO FDA 2017/08 Comprendre pourquoi il est nécessaire de setter la valeur alors qu'on a appelé "super.commitEdit(newValue)", mais juste pour les cellules qui étaient à zéro jusqu'alors.
        planifBean.setChargePlanifiee(debutPeriode, finPeriode, charge);

        planifBean.majChargePlanifieeTotale();
    }
}