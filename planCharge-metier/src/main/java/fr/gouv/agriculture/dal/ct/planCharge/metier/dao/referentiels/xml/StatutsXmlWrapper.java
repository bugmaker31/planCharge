package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.StatutDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class StatutsXmlWrapper {

    @NotNull
    private List<StatutXmlWrapper> statutsXmlWrapper = new ArrayList<>();

//    @AutoWired
    @NotNull
    private StatutDao statutDao = StatutDao.instance();


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public StatutsXmlWrapper() {
        super();
    }


    @XmlElement(name = "statut", required = true)
    @NotNull
    public List<StatutXmlWrapper> getStatuts() {
        return statutsXmlWrapper;
    }

    public void setStatuts(List<StatutXmlWrapper> statuts) {
        this.statutsXmlWrapper = statuts;
    }


    public StatutsXmlWrapper init(List<StatutXmlWrapper> statuts) {
        this.statutsXmlWrapper.clear();
        this.statutsXmlWrapper.addAll(statuts);
        //noinspection ReturnOfThis
        return this;
    }

    @NotNull
    public Set<Statut> extract() throws DaoException {
        Set<Statut> statuts = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        for (StatutXmlWrapper statutXmlWrapper : statutsXmlWrapper) {
            Statut statut = statutXmlWrapper.extract();
            statuts.add(statut);
            statutDao.createOrUpdate(statut);
        }
        return statuts;
    }
}
