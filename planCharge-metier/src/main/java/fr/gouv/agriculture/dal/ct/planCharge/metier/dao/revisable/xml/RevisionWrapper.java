package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.revisable.xml;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.StatutRevision;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision.ValidateurRevision;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;

import javax.validation.constraints.Null;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@SuppressWarnings("ClassHasNoToStringMethod")
public class RevisionWrapper {

    @Null
    private String codeStatutRevision;
    @Null
    private String trigrammeValidateurRevision;
    @Null
    private String commentaireRevision;


    /**
     * Constructeur vide (appelé notamment par JAXB).
     */
    @SuppressWarnings("RedundantNoArgConstructor")
    public RevisionWrapper() {
        super();
    }

    public RevisionWrapper(@Null StatutRevision statutRevision, @Null ValidateurRevision validateurRevision, @Null String commentaireRevision) {
        this();
        codeStatutRevision = Objects.value(statutRevision, StatutRevision::getCode);
        trigrammeValidateurRevision = Objects.value(validateurRevision, ValidateurRevision::getTrigramme);
        this.commentaireRevision = commentaireRevision;
    }


    @XmlAttribute(required = false)
    @Null
    public String getCodeStatutRevision() {
        return codeStatutRevision;
    }

    public void setNoOrdreStatutRevision(@Null String codeStatutRevision) {
        this.codeStatutRevision = codeStatutRevision;
    }

    @XmlAttribute(required = false)
    @Null
    public String getTrigrammeValidateurRevision() {
        return trigrammeValidateurRevision;
    }

    public void setTrigrammeValidateurRevision(@Null String trigrammeValidateurRevision) {
        this.trigrammeValidateurRevision = trigrammeValidateurRevision;
    }

    @XmlElement(required = false)
    @Null
    public String getCommentaireRevision() {
        return commentaireRevision;
    }

    public void setCommentaireRevision(@Null String commentaireRevision) {
        this.commentaireRevision = commentaireRevision;
    }

}
