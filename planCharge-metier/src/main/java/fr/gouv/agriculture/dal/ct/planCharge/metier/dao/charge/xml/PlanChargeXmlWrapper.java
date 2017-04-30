package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.Contexte;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml.ReferentielsXmlWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.Date;

/**
 * Created by frederic.danna on 22/04/2017.
 */
// Cf. http://code.makery.c h/library/javafx-8-tutorial/fr/part5/
@XmlRootElement(name = "planCharge")
public class PlanChargeXmlWrapper {

    private static final String VERSION_FORMAT = "1.0";

    @NotNull
    @Autowired
    private Contexte contexte;

    private String versionFormat = VERSION_FORMAT;

    private String versionApplication;

    private ReferentielsXmlWrapper referentiels;

    private Date dateEtat;

    private PlanificationsXmlWrapper planifications;

    public PlanChargeXmlWrapper() {
        super();
    }

    @XmlAttribute(name = "versionFormat", required = true)
    public String getVersionFormat() {
        return versionFormat;
    }

    public void setVersionFormat(String versionFormat) {
        this.versionFormat = versionFormat;
    }

    @XmlAttribute(name = "versionApplication", required = true)
    public String getVersionApplication() {
        return versionApplication;
    }

    public void setVersionApplication(String versionApplication) {
        this.versionApplication = versionApplication;
    }

    @XmlElement(name = "referentiels", required = true)
    public ReferentielsXmlWrapper getReferentiels() {
        return referentiels;
    }

    @XmlElement(name = "dateEtat", required = true)
    public Date getDateEtat() {
        return dateEtat;
    }

    public void setDateEtat(Date dateEtat) {
        this.dateEtat = dateEtat;
    }

    @XmlElement(name = "planifications", required = true)
    public PlanificationsXmlWrapper getPlanifications() {
        return planifications;
    }

    public void setPlanifications(PlanificationsXmlWrapper planifications) {
        this.planifications = planifications;
    }

    public void init(PlanCharge planCharge) throws PlanChargeDaoException {
        versionApplication = contexte.getApplicationVersion();

        this.dateEtat = Dates.asDate(planCharge.getDateEtat());
        this.planifications = new PlanificationsXmlWrapper(planCharge.getPlanifications());

        referentiels = new ReferentielsXmlWrapper(planCharge.getPlanifications());
    }
}
