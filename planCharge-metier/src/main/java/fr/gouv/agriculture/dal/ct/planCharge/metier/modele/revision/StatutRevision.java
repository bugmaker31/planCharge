package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision;

import javax.validation.constraints.NotNull;

public enum StatutRevision {

    TACHE_PLANIFIEE(100, "Planifiée"),
    TACHE_REJETEE(150, "Rejetée"),

    TACHE_REPORTEE(200, "Reportée"),
    TACHE_PROLONGEE(250, "Prolongée"),

    TACHE_TERMINEE(300, "Terminée"),
    TACHE_ANNULEE(350, "Annulée");


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
