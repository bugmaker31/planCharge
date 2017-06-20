package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportChargementPlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportImportTaches;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportService;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by frederic.danna on 20/06/2017.
 */
public class RapportChargementAvecProgression implements RapportChargementPlanCharge {

    private StringProperty avancement = new SimpleStringProperty();

    public RapportChargementAvecProgression() {
        super();
    }

    public String getAvancement() {
        return avancement.get();
    }

    public StringProperty avancementProperty() {
        return avancement;
    }

    public void setAvancement(String avancement) {
        this.avancement.set(avancement);
    }
}
