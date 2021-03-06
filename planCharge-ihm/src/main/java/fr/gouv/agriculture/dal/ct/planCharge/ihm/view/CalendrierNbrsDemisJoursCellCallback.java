package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalendrierNbrsDemisJoursCellCallback<T extends AbstractCalendrierBean<AbstractDTO, T, FloatProperty>> implements Callback<TableColumn.CellDataFeatures<T, Float>, ObservableValue<Float>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalendrierNbrsDemisJoursCellCallback.class);

    //    Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();


    private int noSemaine;


    public CalendrierNbrsDemisJoursCellCallback(int noSemaine) {
        super();
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
        T nbrsJoursPeriodeBean = cell.getValue();
        LocalDate debutPeriode = planChargeBean.getDateEtat().plusDays((noSemaine - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
        if (!nbrsJoursPeriodeBean.containsKey(debutPeriode)) {
            nbrsJoursPeriodeBean.put(debutPeriode, new SimpleFloatProperty());
        }
        FloatProperty nbrJoursPeriodeProperty = nbrsJoursPeriodeBean.get(debutPeriode);
        return nbrJoursPeriodeProperty.asObject();
    }
}

