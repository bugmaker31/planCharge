package fr.gouv.agriculture.dal.ct.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.function.BiConsumer;

public class DatePickerCells {

    public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forTableColumn(@NotNull BiConsumer<S, LocalDate> dateSetter) {
        return p -> new DatePickerCell<S>(PlanChargeIhm.FORMAT_DATE, dateSetter);
    }

    public static <S> Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>> forRequiredTableColumn(@NotNull BiConsumer<S, LocalDate> dateSetter) {
        return (TableColumn<S, LocalDate> p) -> {
            TableCell<S, LocalDate> datePickerCell = forTableColumn(dateSetter).call(p);
//            PlanChargeIhm.symboliserChampObligatoire(datePickerCell);
            return datePickerCell;
        };
    }
}