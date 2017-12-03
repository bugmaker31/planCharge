package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.xml;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.TypeChangement;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.StatutDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.revisable.xml.RevisionWrapper;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.CategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.SousCategorieTache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.ValidateurRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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

    @Null
    private TypeChangement typeChangement;

    @Null
    private RevisionWrapper revision;


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

    @XmlAttribute(required = true)
    public Integer getIdTache() {
        return idTache;
    }

    @XmlElement(required = true)
    public String getCodeCategorie() {
        return codeCategorie;
    }

    @XmlElement(required = true)
    public String getCodeSousCategorie() {
        return codeSousCategorie;
    }

    @XmlElement(required = true)
    public String getNoTache() {
        return noTache;
    }

    @XmlElement(required = true)
    public String getNoTicketIdal() {
        return noTicketIdal;
    }

    @XmlElement(required = true)
    public String getDescription() {
        return description;
    }

    @XmlElement(required = true)
    public String getCodeProjetAppli() {
        return codeProjetAppli;
    }

    @XmlElement(required = true)
    public String getCodeStatut() {
        return codeStatut;
    }

    @XmlElement(required = false)
    public Date getDebut() {
        return debut;
    }

    @XmlElement(required = true)
    public Date getEcheance() {
        return echeance;
    }

    @XmlElement(required = true)
    public String getCodeImportance() {
        return codeImportance;
    }

    @XmlElement(required = true)
    public double getCharge() {
        return charge;
    }

    @XmlElement(required = true)
    public String getCodeRessource() {
        return codeRessource;
    }

    @XmlElement(required = true)
    public String getCodeProfil() {
        return codeProfil;
    }

    @XmlElement(required = false)
    public TypeChangement getTypeChangement() {
        return typeChangement;
    }

    @XmlElement(required = false)
    public RevisionWrapper getRevision() {
        return revision;
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

    public void setTypeChangement(TypeChangement typeChangement) {
        this.typeChangement = typeChangement;
    }

    public void setRevision(RevisionWrapper revision) {
        this.revision = revision;
    }


    // XmlWrapper :

    @NotNull
    public TacheXmlWrapper init(@NotNull Tache tache) {
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

        typeChangement = tache.getTypeChangement();

        // Revisable
        //noinspection IfMayBeConditional
        if ((tache.getStatutRevision() == null) &&
                (tache.getValidateurRevision() == null) &&
                (tache.getCommentaireRevision() == null)
                ) {
            revision = null;
        } else {
            revision = new RevisionWrapper(
                    tache.getStatutRevision(),
                    tache.getValidateurRevision(),
                    tache.getCommentaireRevision()
            );
        }

        return this;
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
                    ressourceDao.load(codeRessource),
                    profilDao.load(codeProfil)
            );

            tache.setTypeChangement(typeChangement);

            // Revisable
            tache.setStatutRevision(Objects.value(revision, r -> Objects.value(r.getCodeStatutRevision(), StatutRevision::valueOfCode)));
            tache.setValidateurRevision(Objects.value(revision, r -> Objects.value(r.getTrigrammeValidateurRevision(), ValidateurRevision::valueOfTrigramme)));
            tache.setCommentaireRevision(Objects.value(revision, RevisionWrapper::getCommentaireRevision));
        } catch (Exception e) {
            throw new DaoException("Impossible d'extraire la tâche n°" + idTache + " depuis le XML.", e);
        }
        return tache;
    }
}
