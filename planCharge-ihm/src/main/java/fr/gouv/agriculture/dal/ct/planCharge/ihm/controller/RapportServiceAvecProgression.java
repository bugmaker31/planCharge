package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportChargementPlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportService;
import javafx.beans.property.*;

/**
 * Created by frederic.danna on 20/06/2017.
 */
public class RapportServiceAvecProgression implements RapportService {

    private StringProperty avancement = new SimpleStringProperty();

    private IntegerProperty progressionMax = new SimpleIntegerProperty();
    private IntegerProperty progressionCourante = new SimpleIntegerProperty();

    public String getAvancement() {
        return avancement.get();
    }

    public StringProperty avancementProperty() {
        return avancement;
    }

    public void setAvancement(String avancement) {
        this.avancement.set(avancement);
    }

    public void setProgressionMax(int progressionMax) {
        this.progressionMax.set(progressionMax);
    }

    @Override
    public int getProgressionMax() {
        return progressionMax.get();
    }

    public IntegerProperty progressionMaxProperty() {
        return progressionMax;
    }

    public int getProgressionCourante() {
        return progressionCourante.get();
    }

    public IntegerProperty progressionCouranteProperty() {
        return progressionCourante;
    }

    public void setProgressionCourante(int progressionCourante) {
        this.progressionCourante.set(progressionCourante);
    }
}
