package fr.gouv.agriculture.dal.ct.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDate;

public class DatePickerTableCells {

    public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forTableColumn() {
        return p -> new DatePickerTableCell<>(PlanChargeIhm.PATRON_FORMAT_DATE);
    }

    public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forRequiredTableColumn() {
        return (TableColumn<S, LocalDate> p) -> {
            TableCell<S, LocalDate> datePickerCell = DatePickerTableCells.<S>forTableColumn().call(p);
//            PlanChargeIhm.decorateMandatoryColumns(datePickerCell);
            return datePickerCell;
        };
    }
}