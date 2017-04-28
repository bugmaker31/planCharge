package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by frederic.danna on 26/04/2017.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CalendrierWrapper {

//    @XmlElement(name="calendrier", required = true)
    private SortedMap<Date, Double> tableau;

    public CalendrierWrapper() {
    }

    public CalendrierWrapper(Map<LocalDate, Double> tableau) {
        this.tableau = new TreeMap<>(/*calendrier.size()*/);
        // TODO FDA 2017/04 Coder.
    }

    public SortedMap<Date, Double> getTableau() {
        return tableau;
    }

    public void setTableau(SortedMap<Date, Double> tableau) {
        this.tableau = tableau;
    }
}