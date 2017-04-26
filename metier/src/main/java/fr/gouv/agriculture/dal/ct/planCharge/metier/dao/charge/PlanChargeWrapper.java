package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by frederic.danna on 22/04/2017.
 */
// Cf. http://code.makery.c h/library/javafx-8-tutorial/fr/part5/
@XmlRootElement(name = "planCharge")
public class PlanChargeWrapper {

    private static final String VERSION = "1.0";

    private String version = VERSION;

    private Date dateEtat;
    private List<PlanificationWrapper> planifications;

    @XmlAttribute(name = "version", required = true)
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XmlElement(name = "dateEtat", required = true)
    public Date getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(LocalDate dateEtat) {
        // Cf. http://stackoverflow.com/questions/22929237/convert-java-time-localdate-into-java-util-date-type
        this.dateEtat = Date.from(dateEtat.atStartOfDay(ZoneId.of("GMT")).toInstant());
    }

    @XmlElement(name = "planifications", required = true)
    public List<PlanificationWrapper> getPlanifications() {
        return planifications;
    }

    public void setPlanifications(Planifications planifications) {
        this.planifications = new ArrayList<>();
        for (Tache tache : planifications.taches()) {
            PlanificationWrapper planif = new PlanificationWrapper();
            planif.setTache(tache);
            this.planifications.add(planif);
        }
    }

}
