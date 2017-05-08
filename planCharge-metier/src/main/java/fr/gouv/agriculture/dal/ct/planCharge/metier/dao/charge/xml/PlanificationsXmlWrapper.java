package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class PlanificationsXmlWrapper {

    private List<PlanificationXmlWrapper> planifications = new ArrayList<>();

/*
//    @Autowired
    @NotNull
    private PlanificationXmlWrapper planificationXmlWrapper = new PlanificationXmlWrapper();
*/

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public PlanificationsXmlWrapper() {
        super();
    }

    public PlanificationsXmlWrapper init(Planifications planifications) throws PlanChargeDaoException {
        try {
            this.planifications.clear();
            for (Tache tache : planifications.taches()) {
                PlanificationXmlWrapper planificationXmlWrapper = new PlanificationXmlWrapper().init(tache, planifications.calendrier(tache));
                this.planifications.add(planificationXmlWrapper);
            }
            this.planifications.sort(Comparator.comparing(p -> p.getTache().getIdTache()));
        } catch (TacheSansPlanificationException e) {
            throw new PlanChargeDaoException("Impossible de wrapper les planifications.", e);
        }
        return this;
    }

    @XmlElement(name = "planification", required = true)
    public List<PlanificationXmlWrapper> getPlanifications() {
        return planifications;
    }

    public void setPlanifications(List<PlanificationXmlWrapper> planifications) {
        this.planifications = planifications;
    }

    public Planifications extract() throws DaoException {
        Planifications planifications = new Planifications();
        for (PlanificationXmlWrapper pw : this.planifications) {
            Tache tache = pw.getTache().extract();
            Map<LocalDate, Double> calendrier = pw.getCalendrier().extract();
            planifications.ajouter(tache, calendrier);
        }
        return planifications;
    }
}
