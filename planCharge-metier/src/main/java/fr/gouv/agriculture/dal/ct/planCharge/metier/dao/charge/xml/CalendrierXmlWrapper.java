package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class CalendrierXmlWrapper {

    private Map<Date, Double> semaines;

    public CalendrierXmlWrapper() {
        super();
    }

    public CalendrierXmlWrapper(Map<LocalDate, Double> semaines) {
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