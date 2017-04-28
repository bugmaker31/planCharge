package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import javafx.util.Pair;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class CalendrierWrapper {

    private List<Pair<Date, Double>> tableau;

    public CalendrierWrapper() {
        super();
    }

    public CalendrierWrapper(Map<LocalDate, Double> tableau) {
        this.tableau = new ArrayList<>(tableau.size());
        // TODO FDA 2017/04 Coder.
    }

    @XmlElement(name = "tableau", required = true)
    public List<Pair<Date, Double>> getTableau() {
        return tableau;
    }

    public void setTableau(List<Pair<Date, Double>> tableau) {
        this.tableau = tableau;
    }
}