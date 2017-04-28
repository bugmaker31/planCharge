package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class CalendrierWrapper {

    private Map<Date, Double> semaines;

    public CalendrierWrapper() {
        super();
    }

    public CalendrierWrapper(Map<LocalDate, Double> semaines) {
        this.semaines = new HashMap<>(semaines.size());
        semaines.keySet().stream().forEach(
                date -> this.semaines.put(Dates.asDate(date), semaines.get(date))
        );
    }

    @XmlElement(name = "semaines", required = true)
    public Map<Date, Double> getSemaine() {
        return semaines;
    }

    public void setSemaine(Map<Date, Double> tableau) {
        this.semaines = tableau;
    }
}