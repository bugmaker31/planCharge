package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;
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

    @NotNull
    private List<ProjetAppliXmlWrapper> projetsApplisXmlWrapper = new ArrayList<>();

    @NotNull
    private ProjetAppliDao projetAppliDao = ProjetAppliDao.instance();


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ProjetsApplisXmlWrapper() {
        super();
    }


    @XmlElement(name = "projetAppli", required = true)
    @NotNull
    public List<ProjetAppliXmlWrapper> getProjetsApplis() {
        return projetsApplisXmlWrapper;
    }

    public void setProjetsApplis(List<ProjetAppliXmlWrapper> projetsApplis) {
        this.projetsApplisXmlWrapper = projetsApplis;
    }


    public ProjetsApplisXmlWrapper init(List<ProjetAppliXmlWrapper> projetsApplis) {
        this.projetsApplisXmlWrapper.clear();
        this.projetsApplisXmlWrapper.addAll(projetsApplis);
        return this;
    }

    @NotNull
    public Set<ProjetAppli> extract() throws DaoException {
        Set<ProjetAppli> projetsApplis = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        for (ProjetAppliXmlWrapper projetAppliXmlWrapper : projetsApplisXmlWrapper) {
            ProjetAppli projetAppli = projetAppliXmlWrapper.extract();
            projetsApplis.add(projetAppli);
            projetAppliDao.createOrUpdate(projetAppli);
        }
        return projetsApplis;
    }

}
