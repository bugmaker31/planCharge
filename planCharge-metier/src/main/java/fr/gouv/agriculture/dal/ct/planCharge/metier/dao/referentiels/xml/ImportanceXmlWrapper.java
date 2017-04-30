package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ImportanceXmlWrapper {

    private String id;
    private String codeInterne;
    private String code;
    private Integer ordre;

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ImportanceXmlWrapper() {
        super();
    }

    public ImportanceXmlWrapper init(Importance importance) {
        this.id = importance.getIdentity();
        this.codeInterne = importance.getCodeInterne();
        this.code = importance.getCode();
        this.ordre = importance.getOrdre();
        return this;
    }

    @XmlAttribute(name="id", required = true)
    public String getId() {
        return id;
    }

    @XmlElement(name="codeInterne", required = true)
    @NotNull
    public String getCodeInterne() {
        return codeInterne;
    }

    @XmlElement(name="code", required = true)
    @NotNull
    public String getCode() {
        return code;
    }

    @XmlElement(name="ordre", required = true)
    @NotNull
    public Integer getOrder() {
        return ordre;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCodeInterne(String codeInterne) {
        this.codeInterne = codeInterne;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
}
