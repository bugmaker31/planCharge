package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.CategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.SousCategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by frederic.danna on 26/04/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings({"DesignForExtension", "unused", "UseOfObsoleteDateTimeApi", "WeakerAccess", "PublicMethodWithoutLogging"})
public class TacheXmlWrapper {

    @NotNull
    private Integer idTache;
    @NotNull
    private String codeCategorie;
    @Null
    private String codeSousCategorie;
    @NotNull
    private String noTache;
    @NotNull
    private String noTicketIdal;
    @NotNull
    private String description;
    @NotNull
    private String codeProjetAppli;
    @NotNull
    private String codeStatut;
    @Null
    private Date debut;
    @NotNull
    private Date echeance;
    @NotNull
    private String codeImportance;
    @NotNull
    private double charge;
    @NotNull
    private String codeRessource;
    @NotNull
    private String codeProfil;

    //    @Autowired
    @NotNull
    private ProjetAppliDao projetAppliDao = ProjetAppliDao.instance();
    //    @Autowired
    @NotNull
    private StatutDao statutDao = StatutDao.instance();
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
     */
    public TacheXmlWrapper() {
        super();
    }

    public TacheXmlWrapper init(Tache tache) {
        idTache = tache.getId();
        codeCategorie = tache.getCategorie().getCode();
        codeSousCategorie = ((tache.getSousCategorie() == null) ? null : tache.getSousCategorie().getCode());
        noTache = tache.noTache();
        noTicketIdal = tache.getNoTicketIdal();
        description = tache.getDescription();
        codeProjetAppli = tache.getProjetAppli().getCode();
        codeStatut = tache.getStatut().getCode();
        debut = Dates.asDate(tache.getDebut());
        echeance = Dates.asDate(tache.getEcheance());
        codeImportance = tache.getImportance().getCodeInterne();
        charge = tache.getCharge();
        codeRessource = tache.getRessource().getCode();
        codeProfil = tache.getProfil().getCode();
        return this;
    }

    @XmlAttribute(name = "idTache", required = true)
    public Integer getIdTache() {
        return idTache;
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

    @XmlElement(name = "codeStatut", required = true)
    public String getCodeStatut() {
        return codeStatut;
    }

    @XmlElement(name = "debut", required = false)
    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
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

    public void setIdTache(Integer idTache) {
        this.idTache = idTache;
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

    public void setCodeStatut(String codeStatut) {
        this.codeStatut = codeStatut;
    }

    @XmlElement(name = "echeance", required = true)
    public Date getEcheance() {
        return echeance;
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
                    idTache,
                    CategorieTache.valeur(codeCategorie),
                    ((codeSousCategorie == null) ? null : SousCategorieTache.valeur(codeSousCategorie)),
                    noTicketIdal,
                    description,
                    projetAppliDao.load(codeProjetAppli),
                    statutDao.load(codeStatut),
                    Dates.asLocalDate(debut),
                    Dates.asLocalDate(echeance),
                    importanceDao.load(codeImportance),
                    charge,
                    ressourceDao.loadAny(codeRessource),
                    profilDao.load(codeProfil)
            );
        } catch (Exception e) {
            throw new DaoException("Impossible d'extraire la tâche n°" + idTache + " depuis le XML.", e);
        }
        return tache;
    }
}
