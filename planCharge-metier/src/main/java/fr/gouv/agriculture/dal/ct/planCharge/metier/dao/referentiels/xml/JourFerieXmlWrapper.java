package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import fr.gouv.agriculture.dal.ct.planCharge.util.xml.LocalDateXmlAdapter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class JourFerieXmlWrapper {

    @NotNull
    private LocalDate date;
    @Null
    private String description;

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public JourFerieXmlWrapper() {
        super();
    }

    public JourFerieXmlWrapper init(JourFerie jourFerie) {
        this.date = jourFerie.getDate();
        this.description = jourFerie.getDescription();
        return this;
    }

    @XmlAttribute(name="date", required = true)
    @NotNull
    public LocalDate getDate() {
        return date;
    }

    @XmlElement(name="description", required = true)
    @Null
    public String getDescription() {
        return description;
    }

    public void setDate(@NotNull LocalDate date) {
        this.date = date;
    }

    public void setDescription(@Null String description) {
        this.description = description;
    }


    public JourFerie extract() {
        return new JourFerie(date, description);
    }
}
