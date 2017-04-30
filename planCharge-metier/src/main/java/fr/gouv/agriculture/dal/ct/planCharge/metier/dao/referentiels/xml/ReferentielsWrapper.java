package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.xml.bind.annotation.XmlElement;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ReferentielsWrapper {

    private final ImportancesWrapper importances;

    public ReferentielsWrapper(Planifications planifications) {
        this.importances = new ImportancesWrapper(
                planifications.taches().stream()
                        .map(Tache::getImportance)
                        .distinct()
                        .map(tache -> new ImportanceWrapper(tache))
                        .collect(Collectors.toList())
        );
    }

    @XmlElement(name = "importances", required = true)
    public ImportancesWrapper getImportances() {
        return importances;
    }
}
