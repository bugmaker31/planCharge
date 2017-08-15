package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
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
    private RessourcesHumainesXmlWrapper ressourcesHumainesXmlWrapper = new RessourcesHumainesXmlWrapper();


    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ReferentielsXmlWrapper() {
        super();
    }


    @XmlElement(required = true)
    @NotNull
    public JoursFeriesXmlWrapper getJoursFeries() {
        return joursFeriesXmlWrapper;
    }

    @XmlElement(required = true)
    @NotNull
    public ImportancesXmlWrapper getImportances() {
        return importancesXmlWrapper;
    }

    @XmlElement(required = true)
    @NotNull
    public ProfilsXmlWrapper getProfils() {
        return profilsXmlWrapper;
    }

    @XmlElement(required = true)
    @NotNull
    public ProjetsApplisXmlWrapper getProjetsApplis() {
        return projetsApplisXmlWrapper;
    }

    @XmlElement(required = true)
    @NotNull
    public StatutsXmlWrapper getStatuts() {
        return statutsXmlWrapper;
    }

    @XmlElement(required = true)
    @NotNull
    public RessourcesHumainesXmlWrapper getRessources() {
        return ressourcesHumainesXmlWrapper;
    }


    public void setJoursFeries(@NotNull JoursFeriesXmlWrapper joursFeriesXmlWrapper) {
        this.joursFeriesXmlWrapper = joursFeriesXmlWrapper;
    }

    public void setImportances(@NotNull ImportancesXmlWrapper importances) {
        this.importancesXmlWrapper = importances;
    }

    public void setProfils(@NotNull ProfilsXmlWrapper profils) {
        this.profilsXmlWrapper = profils;
    }

    public void setProjetsApplis(@NotNull ProjetsApplisXmlWrapper projetsApplis) {
        this.projetsApplisXmlWrapper = projetsApplis;
    }

    public void setStatuts(@NotNull StatutsXmlWrapper statutsXmlWrapper) {
        this.statutsXmlWrapper = statutsXmlWrapper;
    }

    public void setRessources(@NotNull RessourcesHumainesXmlWrapper ressourcesHumainesXmlWrapper) {
        this.ressourcesHumainesXmlWrapper = ressourcesHumainesXmlWrapper;
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
                referentiels.getImportances().stream()
                        .map(importance -> new ImportanceXmlWrapper().init(importance))
                        .collect(Collectors.toList())
        );
        rapport.setAvancement("Sauvegarde du référentiel des profils...");
        this.profilsXmlWrapper.init(
                referentiels.getProfils().stream()
                        .map(profil -> new ProfilXmlWrapper().init(profil))
                        .collect(Collectors.toList())
        );
        //noinspection HardcodedFileSeparator
        rapport.setAvancement("Sauvegarde du référentiel des projets/applis...");
        this.projetsApplisXmlWrapper.init(
                referentiels.getProjetsApplis().stream()
                        .map(projetAppli -> new ProjetAppliXmlWrapper().init(projetAppli))
                        .collect(Collectors.toList())
        );
        rapport.setAvancement("Sauvegarde du référentiel des statuts...");
        this.statutsXmlWrapper.init(
                referentiels.getStatuts().stream()
                        .map(statut -> new StatutXmlWrapper().init(statut))
                        .collect(Collectors.toList())
        );
        rapport.setAvancement("Sauvegarde du référentiel des ressources...");
        this.ressourcesHumainesXmlWrapper.init(
                referentiels.getRessourcesHumaines().stream()
                        .map(ressource -> new RessourceHumaineXmlWrapper().init(ressource))
                        .collect(Collectors.toList())
        );
        return this;
    }

    public Referentiels extract() throws DaoException {
        return new Referentiels(
                joursFeriesXmlWrapper.extract(),
                importancesXmlWrapper.extract(),
                profilsXmlWrapper.extract(),
                projetsApplisXmlWrapper.extract(),
                statutsXmlWrapper.extract(),
                ressourcesHumainesXmlWrapper.extract()
                );
    }
}
