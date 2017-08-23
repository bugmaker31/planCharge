package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.disponibilite.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PctagesDispoProfilsXmlWrapper {


    // Fields:

    @NotNull
    private Map<String, CalendrierXmlWrapper> disponibilitesProfilsXmlWrapper = new HashMap<>();

    //@Autowired
    @NotNull
    private ProfilDao profilDao = ProfilDao.instance();


    // Constructors:


    /**
     * Constructeur vide (appelé notamment par JAXB).
     */
    PctagesDispoProfilsXmlWrapper() {
        super();
    }


    // Getters/Setters :

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    @XmlElement(required = true)
    @NotNull
    public Map<String, CalendrierXmlWrapper> getProfils() {
        return disponibilitesProfilsXmlWrapper;
    }

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    public void setProfils(@NotNull Map<String, CalendrierXmlWrapper> disponibilitesProfilsXmlWrapper) {
        this.disponibilitesProfilsXmlWrapper = disponibilitesProfilsXmlWrapper;
    }


    // Méthodes :

    @NotNull
    public PctagesDispoProfilsXmlWrapper init(@NotNull Map<Profil, Map<LocalDate, Percentage>> disponibilitesProfils, @NotNull RapportSauvegarde rapport) {
        disponibilitesProfilsXmlWrapper.clear();
        for (Profil profil : disponibilitesProfils.keySet()) {
            Map<LocalDate, Percentage> pctagesDispoRsrcProfil = disponibilitesProfils.get(profil);
            CalendrierXmlWrapper calendrierWrapper = new CalendrierXmlWrapper().init(
                    pctagesDispoRsrcProfil.keySet().stream()
                            .collect(Collectors.toMap(debutPeriode -> debutPeriode, debutPeriode -> pctagesDispoRsrcProfil.get(debutPeriode).floatValue())),
                    rapport
            );
            disponibilitesProfilsXmlWrapper.put(profil.getCode(), calendrierWrapper);
        }
        return this;
    }

    @NotNull
    public Map<Profil, Map<LocalDate, Percentage>> extract() throws DaoException {
        Map<Profil, Map<LocalDate, Percentage>> disponibilitesProfils = new TreeMap<>(); // TreeMap pour trier, juste pour faciliter le débogage.
        for (String codeProfil : disponibilitesProfilsXmlWrapper.keySet()) {
            Profil profil = profilDao.load(codeProfil);
            CalendrierXmlWrapper calendrierXmlWrapper = disponibilitesProfilsXmlWrapper.get(codeProfil);
            Map<LocalDate, Float> calendrier = calendrierXmlWrapper.extract();
            disponibilitesProfils.put(
                    profil,
                    calendrier.keySet().stream()
                            .collect(Collectors.toMap(debutPeriode -> debutPeriode, debutPeriode -> new Percentage(calendrier.get(debutPeriode))))
            );
        }
        return disponibilitesProfils;
    }

    @Override
    public String toString() {
        return disponibilitesProfilsXmlWrapper.toString();
    }

}
