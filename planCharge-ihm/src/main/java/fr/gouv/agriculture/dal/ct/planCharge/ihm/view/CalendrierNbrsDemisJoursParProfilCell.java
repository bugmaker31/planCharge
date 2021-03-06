package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite.NbrsJoursDispoParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter.Converters;
import javafx.beans.property.FloatProperty;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class CalendrierNbrsDemisJoursParProfilCell<T extends AbstractCalendrierParProfilBean<AbstractDTO, T, FloatProperty>>
        extends AbstractCalendrierParProfilCell<NbrsJoursDispoParProfilBean, Float> {

    private static final StringConverter<Integer> CONVERTER =  new IntegerStringConverter();

    public CalendrierNbrsDemisJoursParProfilCell(int noSemaine, @Null Runnable cantEditErrorDisplayer) {
        super(noSemaine, Converters.FRACTION_JOURS_STRING_CONVERTER, cantEditErrorDisplayer);
    }

    public CalendrierNbrsDemisJoursParProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine) {
        this(noSemaine, null);
    }

}

