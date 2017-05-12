package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

import java.time.LocalDate;

/**
 * Created by frederic.danna on 11/05/2017.
 *
 * @author frederic.danna
 */
public class ChargePlanifieeCellFactory extends TextFieldTableCell<PlanificationBean, Double> {

    private final TableColumn<PlanificationBean, Double> col;
    private final LocalDate dateEtat;

    public ChargePlanifieeCellFactory(TableColumn<PlanificationBean, Double> col, LocalDate dateEtat) {
        super();
        this.col = col;
        this.dateEtat = dateEtat;
        setConverter(new DoubleStringConverter());
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

        // FIXME FDA 2017/05 Il faut comparer les dates de début et d'échéance de la tache avec la période de la colonne (semaine ou trimestre), pas avec la date d'état.

        if (planifBean.getDebut() != null) {
            if (planifBean.getDebut().isBefore(dateEtat)) {
                getStyleClass().add("trop-tot");
            }
        }
        if (planifBean.getEcheance().isAfter(dateEtat)) {
            getStyleClass().add("trop-tard");
        }
    }
}