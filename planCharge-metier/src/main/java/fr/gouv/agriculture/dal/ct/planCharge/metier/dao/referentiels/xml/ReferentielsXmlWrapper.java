package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ReferentielsXmlWrapper {

    private ImportancesXmlWrapper importancesXmlWrapper = new ImportancesXmlWrapper();
    private ProfilsXmlWrapper profilsXmlWrapper = new ProfilsXmlWrapper();
    private ProjetsApplisXmlWrapper projetsApplisXmlWrapper = new ProjetsApplisXmlWrapper();
    private RessourcesXmlWrapper ressourcesXmlWrapper = new RessourcesXmlWrapper();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ReferentielsXmlWrapper() {
        super();
    }

    public ReferentielsXmlWrapper init(@NotNull Planifications planifications) {
        this.importancesXmlWrapper.init(
                planifications.taches().stream()
                        .map(Tache::getImportance)
                        .distinct()
                        .sorted()
                        .map(importance -> new ImportanceXmlWrapper().init(importance))
                        .collect(Collectors.toList())
        );
        this.profilsXmlWrapper.init(
                planifications.taches().stream()
                        .map(Tache::getProfil)
                        .distinct()
                        .sorted()
                        .map(profil -> new ProfilXmlWrapper().init(profil))
                        .collect(Collectors.toList())
        );
        this.projetsApplisXmlWrapper.init(
                planifications.taches().stream()
                        .map(Tache::getProjetAppli)
                        .distinct()
                        .sorted()
                        .map(projetAppli -> new ProjetAppliXmlWrapper().init(projetAppli))
                        .collect(Collectors.toList())
        );
        this.ressourcesXmlWrapper.init(
                planifications.taches().stream()
                        .map(Tache::getRessource)
                        .distinct()
                        .sorted()
                        .map(ressource -> new RessourceXmlWrapper().init(ressource))
                        .collect(Collectors.toList())
        );
        return this;
    }

    @XmlElement(name = "importances", required = true)
    public ImportancesXmlWrapper getImportances() {
        return importancesXmlWrapper;
    }

    @XmlElement(name = "profils", required = true)
    public ProfilsXmlWrapper getProfils() {
        return profilsXmlWrapper;
    }

    @XmlElement(name = "projetsApplis", required = true)
    public ProjetsApplisXmlWrapper getProjetsApplis() {
        return projetsApplisXmlWrapper;
    }

    @XmlElement(name = "ressources", required = true)
    public RessourcesXmlWrapper getRessources() {
        return ressourcesXmlWrapper;
    }

    public void setImportances(ImportancesXmlWrapper importances) {
        this.importancesXmlWrapper = importances;
    }

    public void setProfils(ProfilsXmlWrapper profils) {
        this.profilsXmlWrapper = profils;
    }

    public void setProjetsApplis(ProjetsApplisXmlWrapper projetsApplis) {
        this.projetsApplisXmlWrapper = projetsApplis;
    }

    public void setRessources(RessourcesXmlWrapper ressources) {
        this.ressourcesXmlWrapper = ressources;
    }

}
