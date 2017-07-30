package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
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

    @NotNull
    private List<ProfilXmlWrapper> profilsXmlWrapper = new ArrayList<>();

    //@AutoWired
    @NotNull
    private ProfilDao profilDao = ProfilDao.instance();


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ProfilsXmlWrapper() {
        super();
    }


    @XmlElement(name = "profil", required = true)
    @NotNull
    public List<ProfilXmlWrapper> getProfils() {
        return profilsXmlWrapper;
    }

    public void setProfils(List<ProfilXmlWrapper> profils) {
        this.profilsXmlWrapper = profils;
    }


    public ProfilsXmlWrapper init(List<ProfilXmlWrapper> profils) {
        this.profilsXmlWrapper.clear();
        this.profilsXmlWrapper.addAll(profils);
        return this;
    }

    @NotNull
    public Set<Profil> extract() throws DaoException {
        Set<Profil> profils = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        for (ProfilXmlWrapper profilWrapper : profilsXmlWrapper) {
            Profil profil = profilWrapper.extract();
            profils.add(profil);
            profilDao.createOrUpdate(profil);
        }
        return profils;
    }
}
