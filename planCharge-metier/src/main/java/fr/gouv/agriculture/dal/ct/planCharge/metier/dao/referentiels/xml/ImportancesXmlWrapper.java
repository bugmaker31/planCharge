package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;

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

    private List<ImportanceXmlWrapper> importancesXmlWrapper = new ArrayList<>();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ImportancesXmlWrapper() {
        super();
    }

//    @XmlElement(name = "importance", required = true)
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
    public Set<Importance> extract() {
        Set<Importance> importances = new TreeSet<>(); // TreeSet pour trier, juste pour faciliter le débogage.
        for (ImportanceXmlWrapper importanceWrapper : importancesXmlWrapper) {
            Importance imp = importanceWrapper.extract();
            importances.add(imp);
        }
        return importances;
    }
}
