package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDispoProfilBean;
import javafx.beans.property.FloatProperty;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class CalendrierNbrsDemisJoursParProfilCell<T extends AbstractCalendrierParProfilBean<AbstractDTO, T, FloatProperty>> extends AbstractCalendrierParProfilCell<NbrsJoursDispoProfilBean, Float> {

    private static final StringConverter<Integer> CONVERTER =  new IntegerStringConverter();

    public CalendrierNbrsDemisJoursParProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @Null Runnable cantEditErrorDisplayer) {
        super(planChargeBean, noSemaine, Converters.FRACTION_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
    }

    public CalendrierNbrsDemisJoursParProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        this(planChargeBean, noSemaine, null);
    }

}

