package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ProfilsXmlWrapper {

    private List<ProfilXmlWrapper> profils = new ArrayList<>();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ProfilsXmlWrapper() {
        super();
    }

    public ProfilsXmlWrapper init(List<ProfilXmlWrapper> profils) {
        this.profils.clear();
        this.profils.addAll(profils);
        return this;
    }

    @XmlElement(name = "profils", required = true)
    @NotNull
    public List<ProfilXmlWrapper> getProfils() {
        return profils;
    }

    public void setProfils(List<ProfilXmlWrapper> profils) {
        this.profils = profils;
    }
}
