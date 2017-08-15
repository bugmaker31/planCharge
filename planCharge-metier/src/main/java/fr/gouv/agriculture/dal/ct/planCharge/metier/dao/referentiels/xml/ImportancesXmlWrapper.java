package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ImportancesXmlWrapper {

    @NotNull
    private List<ImportanceXmlWrapper> importancesXmlWrapper = new ArrayList<>();

    //@AutoWired
    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ImportancesXmlWrapper() {
        super();
    }


    @XmlElement(required = true)
    @NotNull
    public List<ImportanceXmlWrapper> getImportances() {
        return importancesXmlWrapper;
    }

    public void setImportances(List<ImportanceXmlWrapper> importances) {
        this.importancesXmlWrapper = importances;
    }


    public ImportancesXmlWrapper init(List<ImportanceXmlWrapper> importances) {
        this.importancesXmlWrapper.clear();
        this.importancesXmlWrapper.addAll(importances);
        return this;
    }

    @NotNull
    public Set<Importance> extract() throws DaoException {
        Set<Importance> importances = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        for (ImportanceXmlWrapper importanceWrapper : importancesXmlWrapper) {
            Importance importance = importanceWrapper.extract();
            importances.add(importance);
            importanceDao.createOrUpdate(importance);
        }
        return importances;
    }
}
