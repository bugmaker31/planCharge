package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision;

import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.ResponsablePriorisation;

import javax.validation.constraints.NotNull;

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


    public static ValidateurRevision valueOfTrigramme(@NotNull String trigramme) {
        return valueOf(trigramme);
    }
}
