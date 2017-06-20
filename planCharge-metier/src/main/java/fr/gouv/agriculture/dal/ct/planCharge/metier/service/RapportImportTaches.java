package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

/**
 * Created by frederic.danna on 05/05/2017.
 */
public interface RapportImportTaches extends RapportService {

    int getNbrTachesPlanifiees();

    int getNbrTachesImportees();

    int getNbrTachesSupprimees();

    int getNbrTachesAjoutees();

    int getNbrTachesMisesAJour();

    void incrNbrTachesPlanifiees();

    void incrNbrTachesImportees();

    void incrNbrTachesSupprimees();

    void incrNbrTachesAjoutees();

    void incrNbrTachesMisesAJour();

    void setAvancement(String avancement);
}
