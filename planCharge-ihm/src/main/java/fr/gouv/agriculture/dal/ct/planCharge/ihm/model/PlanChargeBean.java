package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 28/04/2017.
 */
public class PlanChargeBean {

    @Null
    private LocalDate dateEtat;

    @Null
    public LocalDate getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(@Null LocalDate dateEtat) {
        this.dateEtat = dateEtat;
    }

    @Null
    private ObservableList<PlanificationBean> planificationsBeans = FXCollections.observableArrayList();

    @Null
    public ObservableList<PlanificationBean> getPlanificationsBeans() {
        return planificationsBeans;
    }

/*
    public void setPlanificationsBeans(ObservableList<PlanificationBean> planificationsBeans) {
        this.planificationsBeans = planificationsBeans;
    }
*/

    public PlanChargeBean() {
        super();
    }
}
