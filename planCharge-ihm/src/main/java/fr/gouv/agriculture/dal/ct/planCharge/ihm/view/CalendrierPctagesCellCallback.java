package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalendrierPctagesCellCallback<T extends AbstractCalendrierBean<AbstractDTO, T, PercentageProperty>> implements Callback<TableColumn.CellDataFeatures<T, Percentage>, ObservableValue<Percentage>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendrierPctagesCellCallback.class);

    @NotNull
    private PlanChargeBean planChargeBean;
    private int noSemaine;

    public CalendrierPctagesCellCallback(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        super();
        this.planChargeBean = planChargeBean;
        this.noSemaine = noSemaine;
    }

    @Null
    @Override
    public ObservableValue<Percentage> call(TableColumn.CellDataFeatures<T, Percentage> cell) {
        if (cell == null) {
            return null;
        }
        T pctagesDispoBean = cell.getValue();
        if (planChargeBean.getDateEtat() == null) {
            LOGGER.warn("Date d'état non définie !?");
            return null;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        if (!pctagesDispoBean.containsKey(debutPeriode)) {
            pctagesDispoBean.put(debutPeriode, new PercentageProperty(0));
        }
        PercentageProperty pctageDispoProperty = pctagesDispoBean.get(debutPeriode);
        return pctageDispoProperty;
    }
}

