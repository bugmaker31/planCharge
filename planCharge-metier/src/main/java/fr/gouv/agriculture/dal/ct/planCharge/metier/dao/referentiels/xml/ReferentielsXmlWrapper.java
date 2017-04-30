package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import javax.xml.bind.annotation.XmlElement;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ReferentielsXmlWrapper {

    private final ImportancesXmlWrapper importances;
    private final ProfilsXmlWrapper profils;
    private final ProjetsApplisXmlWrapper projetsApplis;
    private final RessourcesXmlWrapper ressources;

    public ReferentielsXmlWrapper(Planifications planifications) {
        this.importances = new ImportancesXmlWrapper(
                planifications.taches().stream()
                        .map(Tache::getImportance)
                        .distinct()
                        .sorted()
                        .map(importance -> new ImportanceXmlWrapper(importance))
                        .collect(Collectors.toList())
        );
        this.profils = new ProfilsXmlWrapper(
                planifications.taches().stream()
                        .map(Tache::getProfil)
                        .distinct()
                        .sorted()
                        .map(profil -> new ProfilXmlWrapper(profil))
                        .collect(Collectors.toList())
        );
        this.projetsApplis = new ProjetsApplisXmlWrapper(
                planifications.taches().stream()
                        .map(Tache::getProjetAppli)
                        .distinct()
                        .sorted()
                        .map(projetAppli -> new ProjetAppliXmlWrapper(projetAppli))
                        .collect(Collectors.toList())
        );
        this.ressources = new RessourcesXmlWrapper(
                planifications.taches().stream()
                        .map(Tache::getRessource)
                        .distinct()
                        .sorted()
                        .map(ressource -> new RessourceXmlWrapper(ressource))
                        .collect(Collectors.toList())
        );
    }

    @XmlElement(name = "importances", required = true)
    public ImportancesXmlWrapper getImportances() {
        return importances;
    }

    @XmlElement(name = "profils", required = true)
    public ProfilsXmlWrapper getProfils() {
        return profils;
    }

    @XmlElement(name = "projetsApplis", required = true)
    public ProjetsApplisXmlWrapper getProjetsApplis() {
        return projetsApplis;
    }

    @XmlElement(name = "ressources", required = true)
    public RessourcesXmlWrapper getRessources() {
        return ressources;
    }
}
