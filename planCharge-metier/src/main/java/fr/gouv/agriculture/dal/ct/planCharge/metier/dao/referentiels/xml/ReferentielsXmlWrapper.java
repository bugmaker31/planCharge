package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Referentiels;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportSauvegarde;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class ReferentielsXmlWrapper {

    private JoursFeriesXmlWrapper joursFeriesXmlWrapper = new JoursFeriesXmlWrapper();

    private ImportancesXmlWrapper importancesXmlWrapper = new ImportancesXmlWrapper();
    private ProfilsXmlWrapper profilsXmlWrapper = new ProfilsXmlWrapper();
    private ProjetsApplisXmlWrapper projetsApplisXmlWrapper = new ProjetsApplisXmlWrapper();
    private StatutsXmlWrapper statutsXmlWrapper = new StatutsXmlWrapper();
    private RessourcesXmlWrapper ressourcesXmlWrapper = new RessourcesXmlWrapper();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ReferentielsXmlWrapper() {
        super();
    }

    @SuppressWarnings("OverlyCoupledMethod")
    public ReferentielsXmlWrapper init(@NotNull Referentiels referentiels, @NotNull RapportSauvegarde rapport) {

        rapport.setAvancement("Sauvegarde du référentiel des jours fériés...");
        this.joursFeriesXmlWrapper.init(
                referentiels.getJoursFeries().stream()
                        .map(jourFerie -> new JourFerieXmlWrapper().init(jourFerie))
                        .collect(Collectors.toList())
        );

        rapport.setAvancement("Sauvegarde du référentiel des importances...");
        this.importancesXmlWrapper.init(
/*
                planCharge.getPlanifications().taches().stream()
                        .map(Tache::getImportance)
                        .distinct()
                        .sorted()
                        .map(importance -> new ImportanceXmlWrapper().init(importance))
                        .collect(Collectors.toList())
*/
                referentiels.getImportances().stream()
                        .map(importance -> new ImportanceXmlWrapper().init(importance))
                        .collect(Collectors.toList())
        );
        rapport.setAvancement("Sauvegarde du référentiel des profils...");
        this.profilsXmlWrapper.init(
/*
                planCharge.getPlanifications().taches().stream()
                        .map(Tache::getProfil)
                        .distinct()
                        .sorted()
                        .map(profil -> new ProfilXmlWrapper().init(profil))
                        .collect(Collectors.toList())
*/
                referentiels.getProfils().stream()
                        .map(profil -> new ProfilXmlWrapper().init(profil))
                        .collect(Collectors.toList())
        );
        //noinspection HardcodedFileSeparator
        rapport.setAvancement("Sauvegarde du référentiel des projets/applis...");
        this.projetsApplisXmlWrapper.init(
/*
                planCharge.getPlanifications().taches().stream()
                        .map(Tache::getProjetAppli)
                        .distinct()
                        .sorted()
                        .map(projetAppli -> new ProjetAppliXmlWrapper().init(projetAppli))
                        .collect(Collectors.toList())
*/
                referentiels.getProjetsApplis().stream()
                        .map(projetAppli -> new ProjetAppliXmlWrapper().init(projetAppli))
                        .collect(Collectors.toList())
        );
        rapport.setAvancement("Sauvegarde du référentiel des statuts...");
        this.statutsXmlWrapper.init(
/*
                planCharge.getPlanifications().taches().stream()
                        .map(Tache::getStatut)
                        .distinct()
                        .sorted()
                        .map(statut -> new StatutXmlWrapper().init(statut))
                        .collect(Collectors.toList())
*/
                referentiels.getStatuts().stream()
                        .map(statut -> new StatutXmlWrapper().init(statut))
                        .collect(Collectors.toList())
        );
        rapport.setAvancement("Sauvegarde du référentiel des ressrouces...");
        this.ressourcesXmlWrapper.init(
/*
                planCharge.getPlanifications().taches().stream()
                        .map(Tache::getRessource)
                        .distinct()
                        .sorted()
                        .map(ressource -> new RessourceXmlWrapper().init(ressource))
                        .collect(Collectors.toList())
*/
                referentiels.getRessources().stream()
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

    @XmlElement(name = "statuts", required = true)
    public StatutsXmlWrapper getStatuts() {
        return statutsXmlWrapper;
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

    @NotNull
    public Referentiels extract() {
        return null;
    }
}
