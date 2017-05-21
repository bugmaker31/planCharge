package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 20/03/2017.
 * @author frederic.danna
 */
public class Difference {

    @NotNull
    private Tache tache;
    @NotNull
    private StatutDifference statut;
    @NotNull
    private Double chargePrecedente;
    @NotNull
    private Double chargeActuelle;
    @NotNull
    private LocalDate echeancePrecedente;
    @NotNull
    private LocalDate echeanceActuelle;

    public Difference(@NotNull Tache tache, @NotNull StatutDifference statut, @NotNull Double chargePrecedente, @NotNull Double chargeActuelle, @NotNull LocalDate echeancePrecedente, @NotNull LocalDate echeanceActuelle) {
        this.tache = tache;
        this.statut = statut;
        this.chargePrecedente = chargePrecedente;
        this.chargeActuelle = chargeActuelle;
        this.echeancePrecedente = echeancePrecedente;
        this.echeanceActuelle = echeanceActuelle;
    }

    @NotNull
    public Tache getTache() {
        return tache;
    }

    @NotNull
    public StatutDifference getStatut() {
        return statut;
    }

    @NotNull
    public Double getChargePrecedente() {
        return chargePrecedente;
    }

    @NotNull
    public Double getChargeActuelle() {
        return chargeActuelle;
    }

    @NotNull
    public LocalDate getEcheancePrecedente() {
        return echeancePrecedente;
    }

    @NotNull
    public LocalDate getEcheanceActuelle() {
        return echeanceActuelle;
    }
}
