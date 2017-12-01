package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision;

import javax.validation.constraints.Null;

public interface Revisable {

    @Null
    StatutRevision getStatutRevision();

    void setStatutRevision(@Null StatutRevision statutRevision);


    @Null
    ValidateurRevision getValidateurRevision();

    void setValidateurRevision(@Null ValidateurRevision validateurRevision);


    @Null
    String getCommentaireRevision();

    void setCommentaireRevision(@Null String commentaireRevision);


}
