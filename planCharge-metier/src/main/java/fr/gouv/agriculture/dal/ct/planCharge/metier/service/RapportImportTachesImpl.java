package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

/**
 * Created by frederic.danna on 20/06/2017.
 */
public class RapportImportTachesImpl implements RapportImportTaches {

    private int nbrTachePlanifiees;
    private int nbrTachesImportees;
    private int nbrTachesSupprimees;
    private int nbrTachesAjoutees;
    private int nbrTachesMisesAJour;
    private String avancement;

    public int getNbrTachePlanifiees() {
        return nbrTachePlanifiees;
    }

    @Override
    public int getNbrTachesImportees() {
        return nbrTachesImportees;
    }

    @Override
    public int getNbrTachesSupprimees() {
        return nbrTachesSupprimees;
    }

    @Override
    public int getNbrTachesAjoutees() {
        return nbrTachesAjoutees;
    }

    @Override
    public int getNbrTachesMisesAJour() {
        return nbrTachesMisesAJour;
    }

    @Override
    public int getNbrTachesPlanifiees() {
        return nbrTachePlanifiees;
    }

    @Override
    public void incrNbrTachesPlanifiees() {
        nbrTachePlanifiees++;
    }

    @Override
    public void incrNbrTachesImportees() {
        nbrTachesImportees++;
    }

    @Override
    public void incrNbrTachesSupprimees() {
        nbrTachesSupprimees++;
    }

    @Override
    public void incrNbrTachesAjoutees() {
        nbrTachesAjoutees++;
    }

    @Override
    public void incrNbrTachesMisesAJour() {
        nbrTachesMisesAJour++;
    }

    @Override
    public void setAvancement(String avancement) {
        this.avancement = avancement;
    }
}
