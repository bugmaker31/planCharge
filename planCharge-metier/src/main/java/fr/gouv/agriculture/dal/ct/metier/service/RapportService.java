package fr.gouv.agriculture.dal.ct.metier.service;

/**
 * Created by frederic.danna on 19/06/2017.
 */
public interface RapportService {

    void setAvancement(String avancement);

    String getAvancement();


    void setProgressionMax(long progressionMax);

    long getProgressionMax();


    void setProgressionCourante(long progressionCourante);
}
