package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ImportancesXmlWrapper {

    private final List<ImportanceXmlWrapper> importances;

    public ImportancesXmlWrapper(List<ImportanceXmlWrapper>  importances) {
        this.importances = importances;
    }

    @XmlElement(name="importance", required = true)
    @NotNull
    public List<ImportanceXmlWrapper> getImportances() {
        return importances;
    }
}
