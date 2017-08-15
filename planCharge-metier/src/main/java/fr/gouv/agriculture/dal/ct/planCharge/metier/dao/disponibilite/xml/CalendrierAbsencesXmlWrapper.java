package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.disponibilite.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalendrierAbsencesXmlWrapper {


    // Fields:

    @NotNull
    private Map<Date, Integer> calendrierXmlWrapper = new HashMap<>();


    // Constructors:


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public CalendrierAbsencesXmlWrapper() {
        super();
    }


    // Getters/Setters :

    @XmlElement(name = "absence", required = true)
    @NotNull
    public Map<Date, Integer> getAbsences() {
        return calendrierXmlWrapper;
    }

    public void setAbsences(@NotNull Map<Date, Integer> absences) {
        this.calendrierXmlWrapper = absences;
    }


    // Méthodes :

    @NotNull
    public CalendrierAbsencesXmlWrapper init(@NotNull Map<LocalDate, Integer> calendrierAbsences, @NotNull RapportSauvegarde rapport) {
        calendrierXmlWrapper.clear();
        calendrierXmlWrapper.putAll(calendrierAbsences.keySet().stream()
                .collect(Collectors.toMap(Dates::asDate, calendrierAbsences::get))
        );
        return this;
    }

    @NotNull
    public Map<LocalDate, Integer> extract() throws DaoException {
        return calendrierXmlWrapper.keySet().stream()
                .collect(Collectors.toMap(Dates::asLocalDate, calendrierXmlWrapper::get));
    }

    @Override
    public String toString() {
        return calendrierXmlWrapper.toString();
    }
}
