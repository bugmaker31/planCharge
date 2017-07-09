package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;

import javax.validation.constraints.NotNull;
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


    @XmlElement(name = "planification", required = true)
    public List<PlanificationXmlWrapper> getPlanifications() {
        return planifications;
    }

    public void setPlanifications(List<PlanificationXmlWrapper> planifications) {
        this.planifications = planifications;
    }


    public PlanificationsXmlWrapper init(@NotNull Planifications planifs, @NotNull RapportSauvegarde rapport) throws PlanChargeDaoException {
        try {
            this.planifications.clear();
            rapport.setProgressionMax(planifs.taches().size());
            int cptTache = 0;
            for (Tache tache : planifs.taches()) {
                rapport.setAvancement("Sauvegarde de la tâche " + tache.noTache() + "...");
                //noinspection ValueOfIncrementOrDecrementUsed
                rapport.setProgressionCourante(++cptTache);
                PlanificationXmlWrapper planificationXmlWrapper = new PlanificationXmlWrapper().init(tache, planifs.calendrier(tache));
                this.planifications.add(planificationXmlWrapper);
            }
            this.planifications.sort(Comparator.comparing(p -> p.getTache().getIdTache()));
        } catch (TacheSansPlanificationException e) {
            throw new PlanChargeDaoException("Impossible de wrapper les planifications.", e);
        }
        return this;
    }

    public Planifications extract() throws DaoException {
        Planifications planifs = new Planifications();
        for (PlanificationXmlWrapper pw : this.planifications) {
            Tache tache = pw.getTache().extract();
            Map<LocalDate, Double> calendrier = pw.getCalendrier().extract();
            planifs.ajouter(tache, calendrier);
        }
        return planifs;
    }
}
