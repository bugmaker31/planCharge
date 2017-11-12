package fr.gouv.agriculture.dal.ct.planCharge.metier.constante;

import javax.validation.constraints.NotNull;

public enum StatutRevision {

    PLANIFIEE(100, "Planifiée"),
    REJETEE(150, "Rejetée"),

    REPORTEE(200, "Reportée"),
    PROLONGEE(250, "Prolongée"),

    TERMINEE(300, "Terminée"),
    ANNULEE_PAR_DEMANDEUR(350, "Annulée");


    private final int ordre;
    @NotNull
    private final String libelle;

    StatutRevision(int ordre, @NotNull String libelle) {
        this.ordre = ordre;
        this.libelle = libelle;
    }

    public int getOrdre() {
        return ordre;
    }

    @NotNull
    public String getLibelle() {
        return libelle;
    }
}
