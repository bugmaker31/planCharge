package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ProfilsXmlWrapper {

    private final List<ProfilXmlWrapper> profils;

    public ProfilsXmlWrapper(List<ProfilXmlWrapper> profils) {
        this.profils = profils;
    }

    @XmlElement(name="profils", required = true)
    @NotNull
    public List<ProfilXmlWrapper> getProfils() {
        return profils;
    }
}
