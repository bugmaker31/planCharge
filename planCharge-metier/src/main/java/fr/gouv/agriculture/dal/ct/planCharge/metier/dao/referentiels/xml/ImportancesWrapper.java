package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ImportancesWrapper {

    private final List<ImportanceWrapper> importances;

    public ImportancesWrapper(List<ImportanceWrapper>  importances) {
        this.importances = importances;
    }

    @XmlElement(name="importance", required = true)
    @NotNull
    public List<ImportanceWrapper> getImportances() {
        return importances;
    }
}
