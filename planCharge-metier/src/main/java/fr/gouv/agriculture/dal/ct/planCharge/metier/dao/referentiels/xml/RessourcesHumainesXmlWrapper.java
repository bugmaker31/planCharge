package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceHumaineDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class RessourcesHumainesXmlWrapper {

    @NotNull
    private List<RessourceHumaineXmlWrapper> ressourcesHumainesXmlWrapper = new ArrayList<>();

//    @AutoWired
    @NotNull
    private RessourceHumaineDao ressourceHumaineDao = RessourceHumaineDao.instance();


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public RessourcesHumainesXmlWrapper() {
        super();
    }


    @XmlElement(name = "ressource", required = true)
    @NotNull
    public List<RessourceHumaineXmlWrapper> getRessources() {
        return ressourcesHumainesXmlWrapper;
    }

    public void setRessources(List<RessourceHumaineXmlWrapper> ressources) {
        this.ressourcesHumainesXmlWrapper = ressources;
    }


    public RessourcesHumainesXmlWrapper init(List<RessourceHumaineXmlWrapper> ressources) {
        this.ressourcesHumainesXmlWrapper.clear();
        this.ressourcesHumainesXmlWrapper.addAll(ressources);
        return this;
    }

    public Set<RessourceHumaine> extract() throws DaoException {
        Set<RessourceHumaine> ressourcesHumaines = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        for (RessourceHumaineXmlWrapper ressourceHumaineXmlWrapper : ressourcesHumainesXmlWrapper) {
            RessourceHumaine ressourceHumaine = ressourceHumaineXmlWrapper.extract();
            ressourcesHumaines.add(ressourceHumaine);
            ressourceHumaineDao.createOrUpdate(ressourceHumaine);
        }
        return ressourcesHumaines;
    }
}
