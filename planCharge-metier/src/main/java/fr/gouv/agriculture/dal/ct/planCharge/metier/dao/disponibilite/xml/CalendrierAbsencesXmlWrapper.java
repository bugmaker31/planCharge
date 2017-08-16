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
    private Map<Date, Double> calendrierXmlWrapper = new HashMap<>();


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

    @XmlElement(required = true)
    @NotNull
    public Map<Date, Double> getSemaines() {
        return calendrierXmlWrapper;
    }

    public void setSemaines(@NotNull Map<Date, Double> absences) {
        this.calendrierXmlWrapper = absences;
    }


    // Méthodes :

    @NotNull
    public CalendrierAbsencesXmlWrapper init(@NotNull Map<LocalDate, Double> calendrierAbsences, @NotNull RapportSauvegarde rapport) {
        calendrierXmlWrapper.clear();
        calendrierXmlWrapper.putAll(calendrierAbsences.keySet().stream()
                .collect(Collectors.toMap(Dates::asDate, calendrierAbsences::get))
        );
        return this;
    }

    @NotNull
    public Map<LocalDate, Double> extract() throws DaoException {
        return calendrierXmlWrapper.keySet().stream()
                .collect(Collectors.toMap(Dates::asLocalDate, calendrierXmlWrapper::get));
    }

    @Override
    public String toString() {
        return calendrierXmlWrapper.toString();
    }
}
