package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.disponibilite.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceHumaineDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.disponibilite.Disponibilites;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;
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
    private Map<String, CalendrierXmlWrapper> nbrJoursAbsenceXmlWrapper = new HashMap<>();

    @NotNull
    private Map<String, CalendrierXmlWrapper> pctagesDispoCTXmlWrapper = new HashMap<>();

    @NotNull
    private Map<String, PctagesDispoProfilsXmlWrapper> pctagesDispoMaxProfilXmlWrapper = new HashMap<>();

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

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    @XmlElement(required = true)
    @NotNull
    public Map<String, CalendrierXmlWrapper> getNbrsJoursAbsence() {
        return nbrJoursAbsenceXmlWrapper;
    }

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    @XmlElement(required = true)
    @NotNull
    public Map<String, CalendrierXmlWrapper> getPctagesDispo() {
        return pctagesDispoCTXmlWrapper;
    }

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    @XmlElement(required = true)
    @NotNull
    public Map<String, PctagesDispoProfilsXmlWrapper> getPctagesDispoMaxProfil() {
        return pctagesDispoMaxProfilXmlWrapper;
    }


    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    public void setNbrJoursAbsence(@NotNull Map<String, CalendrierXmlWrapper> absences) {
        this.nbrJoursAbsenceXmlWrapper = absences;
    }

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    public void setPctagesDispoCT(@NotNull Map<String, CalendrierXmlWrapper> pctagesDispoCTXmlWrapper) {
        this.pctagesDispoCTXmlWrapper = pctagesDispoCTXmlWrapper;
    }

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    public void setPctagesDispoMaxProfil(@NotNull Map<String, PctagesDispoProfilsXmlWrapper> pctagesDispoMaxProfilXmlWrapper) {
        this.pctagesDispoMaxProfilXmlWrapper = pctagesDispoMaxProfilXmlWrapper;
    }

    // Méthodes :

    @NotNull
    public DisponibilitesXmlWrapper init(@NotNull Disponibilites disponibilites, @NotNull RapportSauvegarde rapport) {
        rapport.setProgressionMax(disponibilites.getNbrsJoursAbsence().size());

        // Nbrs de jours d'absence / rsrc :
        rapport.setAvancement("Sauvegarde des absences...");
        nbrJoursAbsenceXmlWrapper.clear();
        for (RessourceHumaine rsrcHum : disponibilites.getNbrsJoursAbsence().keySet()) {
            CalendrierXmlWrapper calendrierWrapper = new CalendrierXmlWrapper().init(
                    disponibilites.getNbrsJoursAbsence().get(rsrcHum),
                    rapport
            );
            nbrJoursAbsenceXmlWrapper.put(rsrcHum.getTrigramme(), calendrierWrapper);
        }

        // Pctages dispo CT / rsrc :
        rapport.setAvancement("Sauvegarde des pourcentages de dispo. pour l'équipe (la CT)...");
        pctagesDispoCTXmlWrapper.clear();
        for (RessourceHumaine rsrcHum : disponibilites.getPctagesDispoCT().keySet()) {
            Map<LocalDate, Percentage> pctagesDispoCTRessHum = disponibilites.getPctagesDispoCT().get(rsrcHum);
            CalendrierXmlWrapper calendrierWrapper = new CalendrierXmlWrapper().init(
                    pctagesDispoCTRessHum.keySet().stream()
                            .collect(Collectors.toMap(debutPeriode -> debutPeriode, debutPeriode -> pctagesDispoCTRessHum.get(debutPeriode).floatValue())),
                    rapport
            );
            pctagesDispoCTXmlWrapper.put(rsrcHum.getTrigramme(), calendrierWrapper);
        }

        // Pctages dispo max / rsrc / profil :
        rapport.setAvancement("Sauvegarde des pourcentages de dispo. max par ressource et profil...");
        pctagesDispoMaxProfilXmlWrapper.clear();
        for (RessourceHumaine rsrcHum : disponibilites.getPctagesDispoMaxRsrcProfil().keySet()) {
            Map<Profil, Map<LocalDate, Percentage>> pctagesDispoMaxRessHum = disponibilites.getPctagesDispoMaxRsrcProfil().get(rsrcHum);
            pctagesDispoMaxProfilXmlWrapper.put(rsrcHum.getTrigramme(), new PctagesDispoProfilsXmlWrapper().init(pctagesDispoMaxRessHum, rapport));
        }

        return this;
    }

    @NotNull
    public Disponibilites extract() throws DaoException {

        // Nbrs de jours d'absence / rsrc :
        Map<RessourceHumaine, Map<LocalDate, Float>> nbrJoursAbsence = new TreeMap<>(); // TreeMap au lieu de HashMap juste pour trier afin de faciliter le débogage.
        for (String trigrammeRsrcHum : nbrJoursAbsenceXmlWrapper.keySet()) {
            RessourceHumaine ressourceHumaine = ressourceHumaineDao.load(trigrammeRsrcHum);
            Map<LocalDate, Float> calendrier = nbrJoursAbsenceXmlWrapper.get(trigrammeRsrcHum).extract();
            nbrJoursAbsence.put(
                    ressourceHumaine,
                    calendrier.keySet().stream().collect(Collectors.toMap(debutPeriode -> debutPeriode, calendrier::get))
            );
        }

        // Pctages dispo CT / rsrc :
        Map<RessourceHumaine, Map<LocalDate, Percentage>> pctagesDispoCT = new TreeMap<>(); // TreeMap au lieu de HashMap juste pour trier afin de faciliter le débogage.
        for (String trigrammeRsrcHum : pctagesDispoCTXmlWrapper.keySet()) {
            RessourceHumaine ressourceHumaine = ressourceHumaineDao.load(trigrammeRsrcHum);
            Map<LocalDate, Float> calendrier = pctagesDispoCTXmlWrapper.get(trigrammeRsrcHum).extract();
            pctagesDispoCT.put(
                    ressourceHumaine,
                    calendrier.keySet().stream().collect(Collectors.toMap(debutPeriode -> debutPeriode, debutPeriode -> new Percentage(calendrier.get(debutPeriode))))
            );
        }

        // Pctages dispo max. / rsrc / profil :
        Map<RessourceHumaine, Map<Profil, Map<LocalDate, Percentage>>> pctagesDispoMaxProfil = new TreeMap<>(); // TreeMap au lieu de HashMap juste pour trier afin de faciliter le débogage.
        for (String trigrammeRsrcHum : pctagesDispoMaxProfilXmlWrapper.keySet()) {
            RessourceHumaine ressourceHumaine = ressourceHumaineDao.load(trigrammeRsrcHum);
            Map<Profil, Map<LocalDate, Percentage>> pctagesDispoProfils = pctagesDispoMaxProfilXmlWrapper.get(trigrammeRsrcHum).extract();
            pctagesDispoMaxProfil.put(ressourceHumaine, pctagesDispoProfils);
        }

        return new Disponibilites(nbrJoursAbsence, pctagesDispoCT, pctagesDispoMaxProfil);
    }
}
