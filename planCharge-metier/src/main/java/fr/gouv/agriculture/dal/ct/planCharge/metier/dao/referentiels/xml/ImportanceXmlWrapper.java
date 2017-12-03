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
    private Integer noOrdre;

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ImportanceXmlWrapper() {
        super();
    }


    @XmlAttribute(required = true)
    @NotNull
    public String getId() {
        return id;
    }

    @XmlElement(required = true)
    @NotNull
    public String getCodeInterne() {
        return codeInterne;
    }

    @XmlElement(required = true)
    @NotNull
    public String getCode() {
        return code;
    }

    @XmlElement(required = true)
    @NotNull
    public Integer getNoOrdre() {
        return noOrdre;
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

    public void setNoOrdre(Integer noOrdre) {
        this.noOrdre = noOrdre;
    }

    // XmlWrapper :

    public ImportanceXmlWrapper init(@NotNull Importance importance) {
        this.id = importance.getIdentity();
        this.codeInterne = importance.getCodeInterne();
        this.noOrdre = importance.getNoOrdre();
        this.code = importance.getCode();
        return this;
    }

    @NotNull
    public Importance extract() {
        return new Importance(noOrdre, code);
    }
}
