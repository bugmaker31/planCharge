package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.revision;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Map;
import java.util.HashMap;

public enum StatutRevision {

    TACHE_PLANIFIEE("PLANIFEE", 100, "Planifiée"),
    TACHE_REJETEE("REJETEE", 150, "Rejetée"),

    TACHE_REPORTEE("REPORTEE", 200, "Reportée"),
    TACHE_PROLONGEE("PROLONGEE", 250, "Prolongée"),

    TACHE_TERMINEE("TERMINEE", 900, "Terminée"),
    TACHE_ANNULEE("ANNULEE", 910, "Annulée");

    private static final Map<String, StatutRevision> VALUES = new HashMap<>(10);

    static {
        for (StatutRevision statutRevision : values()) {
            VALUES.put(statutRevision.getCode(), statutRevision);
        }
    }


    @NotNull
    private final String code;

    private final int noOrdre;

    @NotNull
    private final String libelle;


    StatutRevision(@NotNull String code, int noOrdre, @NotNull String libelle) {
        this.code = code;
        this.noOrdre = noOrdre;
        this.libelle = libelle;
    }


    @NotNull
    public String getCode() {
        return code;
    }

    public int getNoOrdre() {
        return noOrdre;
    }

    @NotNull
    public String getLibelle() {
        return libelle;
    }


    @Null
    public static StatutRevision valueOfCode(@NotNull String code) {
        return VALUES.get(code);
    }


    @Override
    public String toString() {
        return "[" + code + "] " + noOrdre + ") " + libelle;
    }
}
