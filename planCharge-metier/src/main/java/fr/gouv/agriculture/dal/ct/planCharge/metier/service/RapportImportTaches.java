package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.service.RapportService;

/**
 * Created by frederic.danna on 20/06/2017.
 */
public interface RapportImportTaches extends RapportService {

    int getNbrTachesImportees();

    int getNbrTachesSupprimees();

    int getNbrTachesAjoutees();

    int getNbrTachesMisesAJour();

    int getNbrTachesPlanifiees();

    void incrNbrTachesPlanifiees();

    void incrNbrTachesImportees();

    void incrNbrTachesSupprimees();

    void incrNbrTachesAjoutees();

    void incrNbrTachesMisesAJour();
}