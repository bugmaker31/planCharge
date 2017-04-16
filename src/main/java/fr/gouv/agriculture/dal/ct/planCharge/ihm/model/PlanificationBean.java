package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanificationBean {

    private final TacheBean tache;

    public PlanificationBean(TacheBean tache) {
        this.tache = tache;
    }

    public TacheBean getTache() {
        return tache;
    }
}
