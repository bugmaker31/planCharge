package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import javafx.beans.property.DoubleProperty;

import java.time.LocalDate;
import java.util.SortedMap;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanificationBean {

    private final TacheBean tache;
    private final SortedMap<LocalDate, DoubleProperty> matrice;

    public PlanificationBean(TacheBean tache, SortedMap<LocalDate, DoubleProperty> matrice) {
        this.tache = tache;
        this.matrice = matrice;
    }

    public TacheBean getTache() {
        return tache;
    }

    public SortedMap<LocalDate, DoubleProperty> getMatrice() {
        return matrice;
    }
}
