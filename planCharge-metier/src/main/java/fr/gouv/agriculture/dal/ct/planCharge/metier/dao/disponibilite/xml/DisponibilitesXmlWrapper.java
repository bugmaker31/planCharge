package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.disponibilite.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceHumaineDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.disponibilite.Disponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("ClassHasNoToStringMethod")
public class DisponibilitesXmlWrapper {


    // Fields:

    @NotNull
    private Map<String, CalendrierAbsencesXmlWrapper> absencesXmlWrapper = new HashMap<>();

    //@Autowired
    @NotNull
    private RessourceHumaineDao ressourceHumaineDao = RessourceHumaineDao.instance();


    // Constructors:


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public DisponibilitesXmlWrapper() {
        super();
    }


    // Getters/Setters :

    @XmlElement(required = true)
    @NotNull
    public Map<String, CalendrierAbsencesXmlWrapper> getAbsences() {
        return absencesXmlWrapper;
    }

    public void setAbsences(@NotNull Map<String, CalendrierAbsencesXmlWrapper> absences) {
        this.absencesXmlWrapper = absences;
    }


    // Méthodes :

    @NotNull
    public DisponibilitesXmlWrapper init(@NotNull Disponibilites disponibilites, @NotNull RapportSauvegarde rapport) {
        rapport.setProgressionMax(disponibilites.getAbsences().size());

        // Absences :
        rapport.setAvancement("Sauvegarde des absences...");
        absencesXmlWrapper.clear();
        for (RessourceHumaine rsrcHum : disponibilites.getAbsences().keySet()) {
            CalendrierAbsencesXmlWrapper calendrierWrapper = new CalendrierAbsencesXmlWrapper();
            absencesXmlWrapper.put(rsrcHum.getTrigramme(), calendrierWrapper.init(disponibilites.getAbsences().get(rsrcHum), rapport));
        }

        return this;
    }

    @NotNull
    public Disponibilites extract() throws DaoException {

        // Absences :
        Map<RessourceHumaine, Map<LocalDate, Integer>> absences = new TreeMap<>(); // TreeMap au lieu de HashMap juste pour trier afin de faciliter le débogage.
        for (String trigrammeRsrcHum : absencesXmlWrapper.keySet()) {
            RessourceHumaine ressourceHumaine = ressourceHumaineDao.load(trigrammeRsrcHum);
            Map<LocalDate, Integer> calendrier = absencesXmlWrapper.get(trigrammeRsrcHum).extract();
            absences.put(ressourceHumaine, calendrier);
        }

        return new Disponibilites(absences);
    }
}
