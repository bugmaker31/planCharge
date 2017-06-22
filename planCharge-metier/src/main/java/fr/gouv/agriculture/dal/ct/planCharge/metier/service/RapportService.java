package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

/**
 * Created by frederic.danna on 19/06/2017.
 */
public interface RapportService {

    void setAvancement(String avancement);

    String getAvancement();


    void setProgressionMax(int progressionMax);

    int getProgressionMax();


    void setProgressionCourante(int progressionCourante);
}
