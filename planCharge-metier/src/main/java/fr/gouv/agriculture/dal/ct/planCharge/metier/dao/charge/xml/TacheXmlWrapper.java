package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.CategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.SousCategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * Created by frederic.danna on 26/04/2017.
 */
public class TacheXmlWrapper {

    private Integer id;
    private String categorie;
    private String sousCategorie;
    private String noTache;
    private String noTicketIdal;
    private String description;
    private String idProjetAppli;
    private Date debut;
    private Date echeance;
    private String idImportance;
    private double charge;
    private String idRessource;
    private String idProfil;

    //    @Autowired
    @NotNull
    private ProjetAppliDao projetAppliDao = ProjetAppliDao.instance();
    //    @Autowired
    @NotNull
    private RessourceDao ressourceDao = RessourceDao.instance();
    //    @Autowired
    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();
    //    @Autowired
    @NotNull
    private ProfilDao profilDao = ProfilDao.instance();

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public TacheXmlWrapper() {
        super();
    }

    public TacheXmlWrapper init(Tache tache) {
        id = tache.getId();
        categorie = tache.getCategorie().getCode();
        sousCategorie = (tache.getSousCategorie() == null ? null : tache.getSousCategorie().getCode());
        noTache = tache.noTache();
        noTicketIdal = tache.getNoTicketIdal();
        description = tache.getDescription();
        idProjetAppli = tache.getProjetAppli().getIdentity();
        debut = Dates.asDate(tache.getDebut());
        echeance = Dates.asDate(tache.getEcheance());
        idImportance = tache.getImportance().getIdentity();
        charge = tache.getCharge();
        idRessource = tache.getRessource().getIdentity();
        idProfil = tache.getProfil().getIdentity();
        return this;
    }

    @XmlAttribute(name = "id", required = true)
    public Integer getId() {
        return id;
    }

    @XmlElement(name = "categorie", required = true)
    public String getCategorie() {
        return categorie;
    }

    @XmlElement(name = "sousCategorie", required = true)
    public String getSousCategorie() {
        return sousCategorie;
    }

    @XmlElement(name = "noTache", required = true)
    public String getNoTache() {
        return noTache;
    }

    @XmlElement(name = "noTicketIdal", required = true)
    public String getNoTicketIdal() {
        return noTicketIdal;
    }

    @XmlElement(name = "description", required = true)
    public String getDescription() {
        return description;
    }

    @XmlElement(name = "idProjetAppli", required = true)
    public String getIdProjetAppli() {
        return idProjetAppli;
    }

    @XmlElement(name = "debut", required = false)
    public Date getDebut() {
        return debut;
    }

    @XmlElement(name = "echeance", required = true)
    public Date getEcheance() {
        return echeance;
    }

    @XmlElement(name = "idImportance", required = true)
    public String getIdImportance() {
        return idImportance;
    }

    @XmlElement(name = "charge", required = true)
    public double getCharge() {
        return charge;
    }

    @XmlElement(name = "idRessource", required = true)
    public String getIdRessource() {
        return idRessource;
    }

    @XmlElement(name = "idProfil", required = true)
    public String getIdProfil() {
        return idProfil;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setSousCategorie(String sousCategorie) {
        this.sousCategorie = sousCategorie;
    }

    public void setNoTache(String noTache) {
        this.noTache = noTache;
    }

    public void setNoTicketIdal(String noTicketIdal) {
        this.noTicketIdal = noTicketIdal;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIdProjetAppli(String idProjetAppli) {
        this.idProjetAppli = idProjetAppli;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public void setIdImportance(String idImportance) {
        this.idImportance = idImportance;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public void setIdRessource(String idRessource) {
        this.idRessource = idRessource;
    }

    public void setIdProfil(String idProfil) {
        this.idProfil = idProfil;
    }

    @NotNull
    public Tache extract() throws DaoException {
        Tache tache;
        try {
            tache = new Tache(
                    id,
                    CategorieTache.valeur(categorie),
                    (sousCategorie == null ? null : SousCategorieTache.valeur(sousCategorie)),
                    noTicketIdal,
                    description,
                    projetAppliDao.load(idProjetAppli),
                    Dates.asLocalDate(debut),
                    Dates.asLocalDate(echeance),
                    importanceDao.load(idImportance),
                    charge,
                    ressourceDao.load(idRessource),
                    profilDao.load(idProfil)
            );
        } catch (ModeleException e) {
            throw new DaoException("Impossible d'extraire la tâche depuis le XML.", e);
        }
        return tache;
    }
}
