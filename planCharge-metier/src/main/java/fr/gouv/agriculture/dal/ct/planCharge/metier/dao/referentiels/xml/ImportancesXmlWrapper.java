package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ImportancesXmlWrapper {

    private List<ImportanceXmlWrapper> importances = new ArrayList<>();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ImportancesXmlWrapper() {
        super();
    }

    public ImportancesXmlWrapper init(List<ImportanceXmlWrapper> importances) {
        this.importances.clear();
        this.importances.addAll(importances);
        return this;
    }

    @XmlElement(name = "importance", required = true)
    @NotNull
    public List<ImportanceXmlWrapper> getImportances() {
        return importances;
    }

    public void setImportances(List<ImportanceXmlWrapper> importances) {
        this.importances = importances;
    }
}
