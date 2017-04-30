package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ImportanceXmlWrapper {

    private final Importance importance;

    public ImportanceXmlWrapper(Importance importance) {
        this.importance = importance;
    }

    @XmlAttribute(name="id", required = true)
    public String getId() {
        return importance.getIdentity();
    }

    @XmlElement(name="codeInterne", required = true)
    @NotNull
    public String getCodeInterne() {
        return importance.getCodeInterne();
    }

    @XmlElement(name="code", required = true)
    @NotNull
    public String getCode() {
        return importance.getCode();
    }

    @XmlElement(name="ordre", required = true)
    @NotNull
    public Integer getOrder() {
        return importance.getOrder();
    }
}
