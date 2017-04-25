package fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.diff;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Tache;

import java.time.LocalDate;

/**
 * Created by frederic.danna on 20/03/2017.
 */
public class Difference {

    private Tache tache;
    private StatutDifference statut;
    private Double chargePrecedente;
    private Double chargeActuelle;
    private LocalDate echeancePrecedente;
    private LocalDate echeanceActuelle;

    public Difference(Tache tache, StatutDifference statut, Double chargePrecedente, Double chargeActuelle, LocalDate echeancePrecedente, LocalDate echeanceActuelle) {
        this.tache = tache;
        this.statut = statut;
        this.chargePrecedente = chargePrecedente;
        this.chargeActuelle = chargeActuelle;
        this.echeancePrecedente = echeancePrecedente;
        this.echeanceActuelle = echeanceActuelle;
    }

    public Tache getTache() {
        return tache;
    }

    public StatutDifference getStatut() {
        return statut;
    }

    public Double getChargePrecedente() {
        return chargePrecedente;
    }

    public Double getChargeActuelle() {
        return chargeActuelle;
    }

    public LocalDate getEcheancePrecedente() {
        return echeancePrecedente;
    }

    public LocalDate getEcheanceActuelle() {
        return echeanceActuelle;
    }
}
