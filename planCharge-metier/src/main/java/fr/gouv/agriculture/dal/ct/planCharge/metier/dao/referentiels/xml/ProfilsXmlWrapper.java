package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ProfilsXmlWrapper {

    private List<ProfilXmlWrapper> profilsXmlWrapper = new ArrayList<>();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ProfilsXmlWrapper() {
        super();
    }

    public ProfilsXmlWrapper init(List<ProfilXmlWrapper> profils) {
        this.profilsXmlWrapper.clear();
        this.profilsXmlWrapper.addAll(profils);
        return this;
    }

    @XmlElement(name = "profils", required = true)
    @NotNull
    public List<ProfilXmlWrapper> getProfils() {
        return profilsXmlWrapper;
    }

    public void setProfils(List<ProfilXmlWrapper> profils) {
        this.profilsXmlWrapper = profils;
    }


    @NotNull
    public Set<Profil> extract() {
        Set<Profil> profils = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        for (ProfilXmlWrapper profilWrapper : profilsXmlWrapper) {
            Profil imp = profilWrapper.extract();
            profils.add(imp);
        }
        return profils;
    }
}
