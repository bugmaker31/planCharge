package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 24/06/2017.
 */
public class Statut extends AbstractEntity<String, Statut> {

    public static final Statut NOUVEAU = new Statut("10-Nouveau");
    public static final Statut EN_COURS = new Statut("20-En cours");
    public static final Statut EN_ATTENTE = new Statut("50-En attente");
    public static final Statut RECURRENT = new Statut("80-Récurrente");
    public static final Statut REPORTE = new Statut("85-Reportée");
    public static final Statut ANNULE = new Statut("90-Annulée");
    public static final Statut DOUBLON = new Statut("92-Doublon");
    public static final Statut TERMINE = new Statut("95-Terminé");
    public static final Statut A_VENIR = new Statut("97-A venir");
    //
    public static final Statut PROVISION = RECURRENT;

    @NotNull
    private final String code;


    public Statut(@NotNull String code) {
        super();
        this.code = code;
    }


    @NotNull
    public String getCode() {
        return code;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @NotNull
    @Override
    public String getIdentity() {
        return code;
    }


    @Override
    public int compareTo(@NotNull Statut o) {
        return code.compareTo(o.getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statut statut = (Statut) o;

        return code.equals(statut.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return code;
    }
}
