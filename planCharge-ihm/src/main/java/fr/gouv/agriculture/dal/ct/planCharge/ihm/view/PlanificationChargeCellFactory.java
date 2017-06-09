package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ModuleChargesController;
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

        // Réinit du texte et du style de la cellule :
        setText("");
        getStyleClass().remove("tropTot");
        getStyleClass().remove("tropTard");

        // Stop, si cellule vide :
        if (empty) {
            return; // TODO FDA 2017/05 Confirmer.
        }

        // Récupération des infos sur la cellule :
        //noinspection unchecked
        TableRow<PlanificationBean> tableRow = this.getTableRow();
        PlanificationBean planifBean = tableRow.getItem();
        if (planifBean == null) {
            return;
        }
        int indexCol = noPeriode - 1;
        Pair<LocalDate, DoubleProperty> periodePlanif = planifBean.getCalendrier().get(indexCol);

        // Formatage du texte de la cellule :
        double charge = periodePlanif.getValue().get();
        setText((charge == 0.0) ? "" : ModuleChargesController.FORMAT_CHARGE.format(charge));

        // Formatage du style (CSS) de la cellule :
        LocalDate debutPeriode = periodePlanif.getKey();
        LocalDate finPeriode;
        if (planifBean.getCalendrier().size() <= (indexCol + 1)) {
            finPeriode = null;
        } else {
            Pair<LocalDate, DoubleProperty> periodePlanifSuivante = planifBean.getCalendrier().get(indexCol + 1);
            finPeriode = periodePlanifSuivante.getKey();
        }
        if (planifBean.getDebut() != null) {
            if (debutPeriode.isBefore(planifBean.getDebut())) {
                getStyleClass().add("tropTot");
            }
        }
        if ((finPeriode != null) && (planifBean.getEcheance() != null)) {
            if (finPeriode.isAfter(planifBean.getEcheance().plusDays(7))) {
                getStyleClass().add("tropTard");
            }
        }
    }
}