package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision;

import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.ResponsablePriorisation;

public enum ValidateurRevision implements ResponsablePriorisation {

    // Titulaire :
    EAU,
    // Adjoint du titulaire :
    XPO,
    // Remplaçants :
    SCR,
    NBR,
    TSA;

    public String getTrigramme() {
        return name();
    }
}
