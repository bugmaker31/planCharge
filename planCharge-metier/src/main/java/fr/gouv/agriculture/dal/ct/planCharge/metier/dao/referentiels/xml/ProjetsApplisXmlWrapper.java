package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.ProjetAppli;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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


//    @XmlElement(name = "projetAppli", required = true)
    @NotNull
    public List<ProjetAppliXmlWrapper> getProjetsApplis() {
        return projetsApplis;
    }

    public void setProjetsApplis(List<ProjetAppliXmlWrapper> projetsApplis) {
        this.projetsApplis = projetsApplis;
    }


    public ProjetsApplisXmlWrapper init(List<ProjetAppliXmlWrapper> projetsApplis) {
        this.projetsApplis.clear();
        this.projetsApplis.addAll(projetsApplis);
        return this;
    }

    @NotNull
    public Set<ProjetAppli> extract() {
        Set<ProjetAppli> projetsApplis = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        // TODO FDA 2017/07
        return projetsApplis;
    }

}
