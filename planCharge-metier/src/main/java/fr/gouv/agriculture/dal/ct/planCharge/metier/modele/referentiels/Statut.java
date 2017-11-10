package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
    //
    public static final Set<Statut> VALUES = new HashSet<>(20);

    static {
        Collections.addAll(VALUES,
                NOUVEAU,
                EN_COURS,
                EN_ATTENTE,
                RECURRENT,
                REPORTE,
                ANNULE,
                DOUBLON,
                TERMINE,
                A_VENIR,
                //
                PROVISION
        );
    }

    @NotNull
    public static Statut valueOf(@NotNull String code) throws ModeleException {
        return VALUES.parallelStream()
                .filter(statut -> statut.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new ModeleException("Statut non géré : '" + code + "'."));
    }


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


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return code;
    }
}
