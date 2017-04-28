package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.Contexte;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.annotation.*;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by frederic.danna on 22/04/2017.
 */
// Cf. http://code.makery.c h/library/javafx-8-tutorial/fr/part5/
@XmlRootElement(name = "planCharge")
public class PlanChargeWrapper {

    private static final String VERSION_FORMAT = "1.0";

    @Autowired
    private Contexte contexte;

    @XmlAttribute(name = "versionFormat", required = true)
    private String versionFormat = VERSION_FORMAT;

    @XmlElement(name = "dateEtat", required = true)
    private Date dateEtat;
    @XmlElement(name = "planifications", required = true)
    private PlanificationsWrapper planifications;

    public PlanChargeWrapper() {
//        versionAppli = contexte.getApplicationVersion();
    }

    public PlanChargeWrapper(PlanCharge planCharge) throws PlanChargeDaoException {
        this();
        this.dateEtat = Date.from(planCharge.getDateEtat().atStartOfDay(ZoneId.of("GMT")).toInstant());// Cf. http://stackoverflow.com/questions/22929237/convert-java-time-localdate-into-java-util-date-type
        this.planifications = new PlanificationsWrapper(planCharge.getPlanifications());
    }

    public String getVersionFormat() {
        return versionFormat;
    }

    public void setVersionFormat(String versionFormat) {
        this.versionFormat = versionFormat;
    }

    @XmlAttribute(name = "versionAppli", required = true)
    public String getVersionAppli() {
//        return versionAppli;
        return contexte.getApplicationVersion();
    }

    public Date getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(Date dateEtat) {
        this.dateEtat = dateEtat;
    }

    public PlanificationsWrapper getPlanifications() {
        return planifications;
    }

    public void setPlanifications(PlanificationsWrapper planifications) {
        this.planifications = planifications;
    }
}
