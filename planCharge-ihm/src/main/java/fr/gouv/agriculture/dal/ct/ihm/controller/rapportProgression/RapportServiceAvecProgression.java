package fr.gouv.agriculture.dal.ct.ihm.controller.rapportProgression;

import fr.gouv.agriculture.dal.ct.metier.service.RapportService;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by frederic.danna on 20/06/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public class RapportServiceAvecProgression implements RapportService {

    private StringProperty avancement = new SimpleStringProperty();

    private LongProperty progressionMax = new SimpleLongProperty();
    private LongProperty progressionCourante = new SimpleLongProperty();

    public String getAvancement() {
        return avancement.get();
    }

    public StringProperty avancementProperty() {
        return avancement;
    }

    public void setAvancement(String avancement) {
        this.avancement.set(avancement);
    }

    public void setProgressionMax(long progressionMax) {
        this.progressionMax.set(progressionMax);
    }

    @Override
    public long getProgressionMax() {
        return progressionMax.get();
    }

    public LongProperty progressionMaxProperty() {
        return progressionMax;
    }

    public long getProgressionCourante() {
        return progressionCourante.get();
    }

    public LongProperty progressionCouranteProperty() {
        return progressionCourante;
    }

    public void setProgressionCourante(long progressionCourante) {
        this.progressionCourante.set(progressionCourante);
    }
}
