package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.kernel.KernelException;
import fr.gouv.agriculture.dal.ct.kernel.ParametresMetiers;
import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.disponibilite.xml.DisponibilitesXmlWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml.ReferentielsXmlWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.disponibilite.Disponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Referentiels;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by frederic.danna on 22/04/2017.
 */
// Cf. http://code.makery.c h/library/javafx-8-tutorial/fr/part5/
@XmlRootElement(name = "planCharge")
public class PlanChargeXmlWrapper {

    public static final String VERSION_FORMAT = "1.0";

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
    private DisponibilitesXmlWrapper disponibilitesXmlWrapper = new DisponibilitesXmlWrapper();

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

    // Getters :

    @XmlAttribute(name = "versionFormat", required = true)
    @NotNull
    public String getVersionFormat() {
        return versionFormat;
    }

    @XmlAttribute(name = "versionApplication", required = true)
    @NotNull
    public String getVersionApplication() {
        return versionApplication;
    }

    @XmlElement(name = "dateEtat", required = true)
    @NotNull
    public Date getDateEtat() {
        return dateEtat;
    }

    @XmlElement(name = "referentiels", required = true)
    @NotNull
    public ReferentielsXmlWrapper getReferentiels() {
        return referentielsXmlWrapper;
    }

    @XmlElement(name = "disponibilites", required = true)
    @NotNull
    public DisponibilitesXmlWrapper getDisponibilites() {
        return disponibilitesXmlWrapper;
    }

    @XmlElement(name = "planifications", required = true)
    @NotNull
    public PlanificationsXmlWrapper getPlanifications() {
        return planificationsXmlWrapper;
    }

    // Setters :

    public void setVersionFormat(@NotNull String versionFormat) {
        this.versionFormat = versionFormat;
    }

    public void setVersionApplication(@NotNull String versionApplication) {
        this.versionApplication = versionApplication;
    }

    public void setDateEtat(@NotNull Date dateEtat) {
        this.dateEtat = dateEtat;
    }

    public void setReferentiels(@NotNull ReferentielsXmlWrapper referentielsXmlWrapper) {
        this.referentielsXmlWrapper = referentielsXmlWrapper;
    }

    public void setDisponibilitesXmlWrapper(@NotNull DisponibilitesXmlWrapper disponibilitesXmlWrapper) {
        this.disponibilitesXmlWrapper = disponibilitesXmlWrapper;
    }

    public void setPlanifications(@NotNull PlanificationsXmlWrapper planifications) {
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
        disponibilitesXmlWrapper = disponibilitesXmlWrapper.init(planCharge.getDisponibilites(), rapport);
        planificationsXmlWrapper = planificationsXmlWrapper.init(planCharge.getPlanifications(), rapport);

        return this;
    }

    public PlanCharge extract() throws DaoException {

        LocalDate dateEtatLocale = Dates.asLocalDate(dateEtat);
        assert dateEtatLocale != null;

        // Rq : Les méthodes "extract" alimentent les DAOs, donc il faut extraire les référentiels avant les données métier (qui utilisent les valeurs des référentiels).

        Referentiels referentiels = referentielsXmlWrapper.extract();

        Disponibilites disponibilites = disponibilitesXmlWrapper.extract();

        Planifications planifications = planificationsXmlWrapper.extract();

        return new PlanCharge(
                dateEtatLocale,
                referentiels,
                disponibilites,
                planifications
        );
    }

}
