package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ProjetsApplisXmlWrapper {

    private List<ProjetAppliXmlWrapper> projetsApplis = new ArrayList<>();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ProjetsApplisXmlWrapper() {
        super();
    }

    public ProjetsApplisXmlWrapper init(List<ProjetAppliXmlWrapper> projetsApplis) {
        this.projetsApplis.clear();
        this.projetsApplis.addAll(projetsApplis);
        return this;
    }

    @XmlElement(name = "projetAppli", required = true)
    @NotNull
    public List<ProjetAppliXmlWrapper> getProjetsApplis() {
        return projetsApplis;
    }

    public void setProjetsApplis(List<ProjetAppliXmlWrapper> projetsApplis) {
        this.projetsApplis = projetsApplis;
    }
}
