package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.CategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.SousCategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
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
    private String codeCategorie;
    private String codeSousCategorie;
    private String noTache;
    private String noTicketIdal;
    private String description;
    private String codeProjetAppli;
    private Date debut;
    private Date echeance;
    private String codeImportance;
    private double charge;
    private String codeRessource;
    private String codeProfil;

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
        codeCategorie = tache.getCategorie().getCode();
        codeSousCategorie = (tache.getSousCategorie() == null ? null : tache.getSousCategorie().getCode());
        noTache = tache.noTache();
        noTicketIdal = tache.getNoTicketIdal();
        description = tache.getDescription();
        codeProjetAppli = tache.getProjetAppli().getIdentity();
        debut = Dates.asDate(tache.getDebut());
        echeance = Dates.asDate(tache.getEcheance());
        codeImportance = tache.getImportance().getIdentity();
        charge = tache.getCharge();
        codeRessource = tache.getRessource().getIdentity();
        codeProfil = tache.getProfil().getIdentity();
        return this;
    }

    @XmlAttribute(name = "id", required = true)
    public Integer getId() {
        return id;
    }

    @XmlElement(name = "codeCategorie", required = true)
    public String getCodeCategorie() {
        return codeCategorie;
    }

    @XmlElement(name = "codeSousCategorie", required = true)
    public String getCodeSousCategorie() {
        return codeSousCategorie;
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

    @XmlElement(name = "codeProjetAppli", required = true)
    public String getCodeProjetAppli() {
        return codeProjetAppli;
    }

    @XmlElement(name = "debut", required = false)
    public Date getDebut() {
        return debut;
    }

    @XmlElement(name = "echeance", required = true)
    public Date getEcheance() {
        return echeance;
    }

    @XmlElement(name = "codeImportance", required = true)
    public String getCodeImportance() {
        return codeImportance;
    }

    @XmlElement(name = "charge", required = true)
    public double getCharge() {
        return charge;
    }

    @XmlElement(name = "codeRessource", required = true)
    public String getCodeRessource() {
        return codeRessource;
    }

    @XmlElement(name = "codeProfil", required = true)
    public String getCodeProfil() {
        return codeProfil;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCodeCategorie(String codeCategorie) {
        this.codeCategorie = codeCategorie;
    }

    public void setCodeSousCategorie(String codeSousCategorie) {
        this.codeSousCategorie = codeSousCategorie;
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

    public void setCodeProjetAppli(String codeProjetAppli) {
        this.codeProjetAppli = codeProjetAppli;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public void setCodeImportance(String codeImportance) {
        this.codeImportance = codeImportance;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public void setCodeRessource(String codeRessource) {
        this.codeRessource = codeRessource;
    }

    public void setCodeProfil(String codeProfil) {
        this.codeProfil = codeProfil;
    }

    @NotNull
    public Tache extract() throws DaoException {
        Tache tache;
        try {
            tache = new Tache(
                    id,
                    CategorieTache.valeur(codeCategorie),
                    (codeSousCategorie == null ? null : SousCategorieTache.valeur(codeSousCategorie)),
                    noTicketIdal,
                    description,
                    projetAppliDao.load(codeProjetAppli),
                    Dates.asLocalDate(debut),
                    Dates.asLocalDate(echeance),
                    importanceDao.load(codeImportance),
                    charge,
                    ressourceDao.load(codeRessource),
                    profilDao.load(codeProfil)
            );
        } catch (ModeleException e) {
            throw new DaoException("Impossible d'extraire la tâche depuis le XML.", e);
        }
        return tache;
    }
}
