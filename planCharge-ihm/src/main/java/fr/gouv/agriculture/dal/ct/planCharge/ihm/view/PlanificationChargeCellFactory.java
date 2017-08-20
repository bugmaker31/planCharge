package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 11/05/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class PlanificationChargeCellFactory extends TextFieldTableCell<PlanificationTacheBean, Double> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanificationChargeCellFactory.class);


    private PlanChargeBean planChargeBean;
    private final int noSemaine;


    public PlanificationChargeCellFactory(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        super();
        this.planChargeBean = planChargeBean;
        this.noSemaine = noSemaine;
        setConverter(new DoubleStringConverter()); // TODO FDA 2017/04 Mieux formater les charges ?
    }


    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        // Réinit du texte et du style de la cellule :
        setText("");
        getStyleClass().remove("avantPeriodeDemandee");
        getStyleClass().remove("pendantPeriodeDemandee");
        getStyleClass().remove("apresPeriodeDemandee");

/* Non, surtout pas, sinon les cellules vides (donc avant et après la période planifiée), ne seront pas décorées.
        // Stop, si cellule vide :
        if (empty || (item == null)) {
            return;
        }
*/

        // Récupération des infos sur la cellule :
        //noinspection unchecked
        TableRow<PlanificationTacheBean> tableRow = this.getTableRow();
        PlanificationTacheBean planifBean = tableRow.getItem();
        if (planifBean == null) {
            return;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]

        // Formatage du texte de la cellule :
        //noinspection UnnecessaryLocalVariable
        Double charge = item;
        setText(((charge == null) || (charge == 0.0)) ? "" : ChargesController.FORMAT_CHARGE.format(charge));

        // Formatage du style (CSS) de la cellule :
        if (planifBean.getDebut() != null) {
            if (debutPeriode.isBefore(planifBean.getDebut())) {
                getStyleClass().add("avantPeriodeDemandee");
                return;
            }
        }
        if (planifBean.getEcheance() != null) {
            if (finPeriode.isAfter(planifBean.getEcheance().plusDays(7))) {
                getStyleClass().add("apresPeriodeDemandee");
                return;
            }
        }
        getStyleClass().add("pendantPeriodeDemandee");
    }
}