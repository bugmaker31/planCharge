package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import fr.gouv.agriculture.dal.ct.ihm.view.NotEditableTextFieldTableCell;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.AbstractCalendrierParProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import javafx.util.StringConverter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public abstract class AbstractCalendrierParProfilCell<S extends AbstractCalendrierParProfilBean<?, ?, ?>, T> extends NotEditableTextFieldTableCell<S, T> {

    //    Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();


    private int noSemaine;


    protected AbstractCalendrierParProfilCell(int noSemaine, @NotNull StringConverter<T> stringConverter, @Null Runnable cantEditErrorDisplayer) {
        super(stringConverter, cantEditErrorDisplayer);
        this.noSemaine = noSemaine;
    }

    protected AbstractCalendrierParProfilCell(int noSemaine, @NotNull StringConverter<T> stringConverter) {
        this(noSemaine, stringConverter, null);
    }


    @NotNull
    protected PlanChargeBean getPlanChargeBean() {
        return planChargeBean;
    }

    protected int getNoSemaine() {
        return noSemaine;
    }

}

