package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.disponibilite.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceHumaineDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.disponibilite.Disponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SuppressWarnings("ClassHasNoToStringMethod")
public class DisponibilitesXmlWrapper {


    // Fields:

    @NotNull
    private Map<String, CalendrierFloatXmlWrapper> nbrJoursAbsenceXmlWrapper = new HashMap<>();

    @NotNull
    private Map<String, CalendrierFloatXmlWrapper> pctagesDispoCTXmlWrapper = new HashMap<>();

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

    @SuppressWarnings("SuspiciousGetterSetter")
    @XmlElement(required = true)
    @NotNull
    public Map<String, CalendrierFloatXmlWrapper> getNbrsJoursAbsence() {
        return nbrJoursAbsenceXmlWrapper;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    @XmlElement(required = true)
    @NotNull
    public Map<String, CalendrierFloatXmlWrapper> getPctagesDispo() {
        return pctagesDispoCTXmlWrapper;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    public void setNbrJoursAbsence(@NotNull Map<String, CalendrierFloatXmlWrapper> absences) {
        this.nbrJoursAbsenceXmlWrapper = absences;
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    public void setPctagesDispoCT(@NotNull Map<String, CalendrierFloatXmlWrapper> pctagesDispoCTXmlWrapper) {
        this.pctagesDispoCTXmlWrapper = pctagesDispoCTXmlWrapper;
    }

    // Méthodes :

    @NotNull
    public DisponibilitesXmlWrapper init(@NotNull Disponibilites disponibilites, @NotNull RapportSauvegarde rapport) {
        rapport.setProgressionMax(disponibilites.getNbrsJoursAbsence().size());

        // Nbrs de jours d'absence :
        rapport.setAvancement("Sauvegarde des absences...");
        nbrJoursAbsenceXmlWrapper.clear();
        for (RessourceHumaine rsrcHum : disponibilites.getNbrsJoursAbsence().keySet()) {
            CalendrierFloatXmlWrapper calendrierWrapper = new CalendrierFloatXmlWrapper().init(
                    disponibilites.getNbrsJoursAbsence().get(rsrcHum),
                    rapport
            );
            nbrJoursAbsenceXmlWrapper.put(rsrcHum.getTrigramme(), calendrierWrapper);
        }

        // Pctages dispo CT :
        rapport.setAvancement("Sauvegarde des pourcentages de dispo. pour l'équipe (la CT)...");
        pctagesDispoCTXmlWrapper.clear();
        for (RessourceHumaine rsrcHum : disponibilites.getPctagesDispoCT().keySet()) {
            Map<LocalDate, Percentage> pctagesDispCTRessHum = disponibilites.getPctagesDispoCT().get(rsrcHum);
            CalendrierFloatXmlWrapper calendrierWrapper = new CalendrierFloatXmlWrapper().init(
                    pctagesDispCTRessHum.keySet().stream()
                            .collect(Collectors.<LocalDate, LocalDate, Float>toMap(debutPeriode -> debutPeriode, debutPeriode -> pctagesDispCTRessHum.get(debutPeriode).floatValue())),
                    rapport
            );
            pctagesDispoCTXmlWrapper.put(rsrcHum.getTrigramme(), calendrierWrapper);
        }

        return this;
    }

    @NotNull
    public Disponibilites extract() throws DaoException {

        // Nbrs de jours d'absence :
        Map<RessourceHumaine, Map<LocalDate, Float>> nbrJoursAbsence = new TreeMap<>(); // TreeMap au lieu de HashMap juste pour trier afin de faciliter le débogage.
        for (String trigrammeRsrcHum : nbrJoursAbsenceXmlWrapper.keySet()) {
            RessourceHumaine ressourceHumaine = ressourceHumaineDao.load(trigrammeRsrcHum);
            Map<LocalDate, Float> calendrier = nbrJoursAbsenceXmlWrapper.get(trigrammeRsrcHum).extract();
            nbrJoursAbsence.put(
                    ressourceHumaine,
                    calendrier.keySet().stream().collect(Collectors.toMap(debutPeriode -> debutPeriode, calendrier::get))
            );
        }

        // Pctages dispo CT :
        Map<RessourceHumaine, Map<LocalDate, Percentage>> pctagesDispoCT = new TreeMap<>(); // TreeMap au lieu de HashMap juste pour trier afin de faciliter le débogage.
        for (String trigrammeRsrcHum : pctagesDispoCTXmlWrapper.keySet()) {
            RessourceHumaine ressourceHumaine = ressourceHumaineDao.load(trigrammeRsrcHum);
            Map<LocalDate, Float> calendrier = pctagesDispoCTXmlWrapper.get(trigrammeRsrcHum).extract();
            pctagesDispoCT.put(
                    ressourceHumaine,
                    calendrier.keySet().stream().collect(Collectors.toMap(debutPeriode -> debutPeriode, key -> new Percentage(calendrier.get(key))))
            );
        }

        return new Disponibilites(nbrJoursAbsence, pctagesDispoCT);
    }
}
