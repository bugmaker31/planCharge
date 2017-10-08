package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
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
public class CalendrierChargeCellCallback implements Callback<TableColumn.CellDataFeatures<PlanificationTacheBean, Double>, ObservableValue<Double>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendrierChargeCellCallback.class);

//    Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();


    private final int noSemaine;


    public CalendrierChargeCellCallback(int noSemaine) {
        super();
        this.noSemaine = noSemaine;
    }


    @Null
    @Override
    public ObservableValue<Double> call(@Null TableColumn.CellDataFeatures<PlanificationTacheBean, Double> param) {
        assert param != null;
        assert ((TableViewAvecCalendrier) param.getTableView()).getCalendrierColumns().indexOf(param.getTableColumn()) == (noSemaine - 1);
        PlanificationTacheBean planificationTacheBean = param.getValue();
        assert planificationTacheBean != null;
        if (planChargeBean.getDateEtat() == null) {
            LOGGER.warn("Date d'état non définie !?");
            return null;
        }
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays(((long) noSemaine - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7L);// TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        if (!planificationTacheBean.aChargePlanifiee(debutPeriode/*, finPeriode*/)) {
            planificationTacheBean.setChargePlanifiee(debutPeriode/*, finPeriode*/, 0.0);
        }
        try {
            DoubleProperty chargePeriodeProperty = planificationTacheBean.chargePlanifieeProperty(debutPeriode/*, finPeriode*/);
            assert chargePeriodeProperty != null;
            return chargePeriodeProperty.asObject();
        } catch (BeanException e) {
            LOGGER.error("Impossible de définir la charge planifiée pour " + planificationTacheBean + ".", e);
            return null;
        }
    }
}

