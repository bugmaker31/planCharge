package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml.ReferentielsXmlWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Referentiels;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

/**
 * Created by frederic.danna on 22/04/2017.
 */
// Cf. http://code.makery.c h/library/javafx-8-tutorial/fr/part5/
@XmlRootElement(name = "planCharge")
public class PlanChargeXmlWrapper {

    private static final String VERSION_FORMAT = "1.0";

    @NotNull
//    @Autowired
//    private Contexte contexte = Contexte.instance();
    private ParametresMetiers params = ParametresMetiers.instance();

    @NotNull
    private String versionFormat = VERSION_FORMAT;

    /*
        @Inject
        @Property("application.version")
    */
    @NotNull
    private String versionApplication;

    @NotNull
    private Date dateEtat;

    //    @Autowired
    @NotNull
    private ReferentielsXmlWrapper referentielsXmlWrapper = new ReferentielsXmlWrapper();
    //    @Autowired
    @NotNull
    private PlanificationsXmlWrapper planificationsXmlWrapper = new PlanificationsXmlWrapper();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
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
        return referentielsXmlWrapper;
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
        return planificationsXmlWrapper;
    }

    public void setPlanifications(PlanificationsXmlWrapper planifications) {
        this.planificationsXmlWrapper = planifications;
    }

    public PlanChargeXmlWrapper init(@NotNull PlanCharge planCharge, @NotNull RapportSauvegarde rapport) throws PlanChargeDaoException {
        try {
            versionApplication = params.getParametrage("application.version");
        } catch (KernelException e) {
            throw new PlanChargeDaoException("Impossible de déterminer la version de l'application.", e);
        }

        dateEtat = Dates.asDate(planCharge.getDateEtat());
        referentielsXmlWrapper = referentielsXmlWrapper.init(planCharge.getReferentiels(), rapport);
        planificationsXmlWrapper = planificationsXmlWrapper.init(planCharge.getPlanifications(), rapport);

        return this;
    }

    public PlanCharge extract() throws DaoException {

        LocalDate dateEtatLocale = Dates.asLocalDate(dateEtat);
        assert dateEtatLocale != null;

        Referentiels referentiels = referentielsXmlWrapper.extract();

        Planifications planifications = planificationsXmlWrapper.extract();

        return new PlanCharge(
                dateEtatLocale,
                referentiels,
                planifications
        );
    }

}
