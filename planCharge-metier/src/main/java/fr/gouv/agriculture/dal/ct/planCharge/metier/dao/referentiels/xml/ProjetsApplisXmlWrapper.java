package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ProjetsApplisXmlWrapper {

    private final List<ProjetAppliXmlWrapper> projetsApplis;

    public ProjetsApplisXmlWrapper(List<ProjetAppliXmlWrapper> projetsApplis) {
        this.projetsApplis = projetsApplis;
    }

    @XmlElement(name="projetAppli", required = true)
    @NotNull
    public List<ProjetAppliXmlWrapper> getProjetsApplis() {
        return projetsApplis;
    }
}
