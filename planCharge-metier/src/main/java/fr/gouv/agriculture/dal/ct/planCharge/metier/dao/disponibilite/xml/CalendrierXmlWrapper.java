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

@SuppressWarnings({"ClassHasNoToStringMethod", "UseOfObsoleteDateTimeApi"})
public class CalendrierXmlWrapper {


    // Fields:

    @NotNull
    private Map<Date, Float> calendrierXmlWrapper = new HashMap<>();


    // Constructors:


    /**
     * Constructeur vide (appelé notamment par JAXB).
     */
    CalendrierXmlWrapper() {
        super();
    }


    // Getters/Setters :

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    @XmlElement(required = true)
    @NotNull
    public Map<Date, Float> getSemaines() {
        return calendrierXmlWrapper;
    }

    @SuppressWarnings({"SuspiciousGetterSetter", "unused"})
    public void setSemaines(@NotNull Map<Date, Float> absences) {
        this.calendrierXmlWrapper = absences;
    }


    // Méthodes :

    @NotNull
    public CalendrierXmlWrapper init(@NotNull Map<LocalDate, Float> calendrier, @NotNull RapportSauvegarde rapport) {
        calendrierXmlWrapper.clear();
        calendrierXmlWrapper.putAll(calendrier.keySet().stream()
                .collect(Collectors.toMap(Dates::asDate, calendrier::get))
        );
        return this;
    }

    @NotNull
    public Map<LocalDate, Float> extract() throws DaoException {
        return calendrierXmlWrapper.keySet().stream()
                .collect(Collectors.toMap(Dates::asLocalDate, debutPeriode -> calendrierXmlWrapper.get(debutPeriode)));
    }

    @Override
    public String toString() {
        return calendrierXmlWrapper.toString();
    }
}
