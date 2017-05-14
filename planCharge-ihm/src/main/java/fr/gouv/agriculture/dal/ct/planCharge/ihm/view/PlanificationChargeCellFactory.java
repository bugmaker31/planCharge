package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Pair;
import javafx.util.converter.DoubleStringConverter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 11/05/2017.
 *
 * @author frederic.danna
 */
public class PlanificationChargeCellFactory extends TextFieldTableCell<PlanificationBean, Double> {

    private final int noPeriode;

    public PlanificationChargeCellFactory(@NotNull int noPeriode) {
        super();
        this.noPeriode = noPeriode;
        setConverter(new DoubleStringConverter()); // TODO FDA 2017/04 Mieux formater les charges ?
    }

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            return; // TODO FDA 2017/05 Confirmer.
        }

        TableRow<PlanificationBean> tableRow = this.getTableRow();
        PlanificationBean planifBean = tableRow.getItem();
        if (planifBean == null) {
            return;
        }

        int indexCol = noPeriode - 1;
        Pair<LocalDate, DoubleProperty> periodePlanif = planifBean.getCalendrier().get(indexCol);
        LocalDate debutPeriode = periodePlanif.getKey();
        Pair<LocalDate, DoubleProperty> periodePlanifSuivante;
        LocalDate finPeriode;
        if (planifBean.getCalendrier().size() <= (indexCol + 1)) {
            finPeriode = null;
        } else {
            periodePlanifSuivante = planifBean.getCalendrier().get(indexCol + 1);
            finPeriode = periodePlanifSuivante.getKey();
        }

        if (planifBean.getDebut() != null) {
            if (debutPeriode.isBefore(planifBean.getDebut())) {
                getStyleClass().add("tropTot");
            }
        }
        if ((finPeriode != null) && (finPeriode.isAfter(planifBean.getEcheance()))) {
            if (planifBean.getEcheance().isAfter(finPeriode)) {
                getStyleClass().add("tropTard");
            }
        }
    }
}