package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalendrierFractionsJoursCellCallback<T extends AbstractCalendrierBean<AbstractDTO, T, FloatProperty>> implements Callback<TableColumn.CellDataFeatures<T, Float>, ObservableValue<Float>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendrierFractionsJoursCellCallback.class);

    @NotNull
    private PlanChargeBean planChargeBean;
    private int noSemaine;

    public CalendrierFractionsJoursCellCallback(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        super();
        this.planChargeBean = planChargeBean;
        this.noSemaine = noSemaine;
    }

    @Null
    @Override
    public ObservableValue<Float> call(TableColumn.CellDataFeatures<T, Float> cell) {
        if (cell == null) {
            return null;
        }
        if (planChargeBean.getDateEtat() == null) {
            LOGGER.warn("Date d'état non définie !?");
            return null;
        }
        T fractionJoursPeriodeBean = cell.getValue();
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1) * 7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        if (!fractionJoursPeriodeBean.containsKey(debutPeriode)) {
            fractionJoursPeriodeBean.put(debutPeriode, new SimpleFloatProperty());
        }
        FloatProperty fractionJourPeriodeProperty = fractionJoursPeriodeBean.get(debutPeriode);
        return fractionJourPeriodeProperty.asObject();
    }
}

