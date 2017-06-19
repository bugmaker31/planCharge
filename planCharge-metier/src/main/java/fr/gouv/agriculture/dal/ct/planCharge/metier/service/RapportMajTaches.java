package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

/**
 * Created by frederic.danna on 05/05/2017.
 */
public class RapportMajTaches implements RapportService {

    private final int nbrTachePlanifiees;
    private final int nbrTachesImportees;
    private int nbrTachesSupprimees;
    private int nbrTachesAjoutees;
    private int nbrTachesMisesAJour;

    public RapportMajTaches(int nbrTachePlanifiees, int nbrTachesImportees) {
        this.nbrTachePlanifiees = nbrTachePlanifiees;
        this.nbrTachesImportees = nbrTachesImportees;

        nbrTachesSupprimees = 0;
        nbrTachesAjoutees = 0;
        nbrTachesMisesAJour = 0;
    }

    public int getNbrTachePlanifiees() {
        return nbrTachePlanifiees;
    }

    public int getNbrTachesImportees() {
        return nbrTachesImportees;
    }

    public int getNbrTachesSupprimees() {
        return nbrTachesSupprimees;
    }

    public int getNbrTachesAjoutees() {
        return nbrTachesAjoutees;
    }

    public int getNbrTachesMisesAJour() {
        return nbrTachesMisesAJour;
    }

    public void incrNbrTachesSupprimees() {
        nbrTachesSupprimees++;
    }

    public void incrNbrTachesAjoutees() {
        nbrTachesAjoutees++;
    }

    public void incrNbrTachesMisesAJour() {
        nbrTachesMisesAJour++;
    }
}
