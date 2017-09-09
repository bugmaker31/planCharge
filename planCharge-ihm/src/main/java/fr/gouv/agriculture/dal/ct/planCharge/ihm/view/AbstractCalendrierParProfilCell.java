package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.EditableAwareTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import javafx.util.StringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public abstract class AbstractCalendrierParProfilCell<S extends AbstractCalendrierParProfilBean<?, ?, ?>, T> extends EditableAwareTextFieldTableCell<S, T> {

    @NotNull
    private PlanChargeBean planChargeBean;
    private int noSemaine;

    protected AbstractCalendrierParProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
        super(stringConverter, cantEditErrorDisplayer);
        this.planChargeBean = planChargeBean;
        this.noSemaine = noSemaine;
    }

    protected AbstractCalendrierParProfilCell(@NotNull PlanChargeBean planChargeBean, int noSemaine, @NotNull StringConverter<T> stringConverter) {
        this(planChargeBean, noSemaine, stringConverter, null);
    }

    @NotNull
    protected PlanChargeBean getPlanChargeBean() {
        return planChargeBean;
    }

    protected int getNoSemaine() {
        return noSemaine;
    }

}

