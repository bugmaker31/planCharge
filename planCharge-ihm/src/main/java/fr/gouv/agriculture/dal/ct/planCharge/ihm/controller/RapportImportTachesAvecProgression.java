package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.metier.service.RapportImportTaches;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by frederic.danna on 20/06/2017.
 */
public class RapportImportTachesAvecProgression implements RapportImportTaches {

    private IntegerProperty nbrTachePlanifiees = new SimpleIntegerProperty();
    private IntegerProperty nbrTachesImportees = new SimpleIntegerProperty();
    private IntegerProperty nbrTachesSupprimees = new SimpleIntegerProperty();
    private IntegerProperty nbrTachesAjoutees = new SimpleIntegerProperty();
    private IntegerProperty nbrTachesMisesAJour = new SimpleIntegerProperty();
    private StringProperty avancement = new SimpleStringProperty();

    public RapportImportTachesAvecProgression() {
        super();
    }

    @Override
    public int getNbrTachesPlanifiees() {
        return nbrTachePlanifiees.get();
    }

    public IntegerProperty nbrTachePlanifieesProperty() {
        return nbrTachePlanifiees;
    }

    @Override
    public int getNbrTachesImportees() {
        return nbrTachesImportees.get();
    }

    public IntegerProperty nbrTachesImporteesProperty() {
        return nbrTachesImportees;
    }

    @Override
    public int getNbrTachesSupprimees() {
        return nbrTachesSupprimees.get();
    }

    public IntegerProperty nbrTachesSupprimeesProperty() {
        return nbrTachesSupprimees;
    }

    @Override
    public int getNbrTachesAjoutees() {
        return nbrTachesAjoutees.get();
    }

    public IntegerProperty nbrTachesAjouteesProperty() {
        return nbrTachesAjoutees;
    }

    public IntegerProperty nbrTachesMisesAJourProperty() {
        return nbrTachesMisesAJour;
    }

    @Override
    public int getNbrTachesMisesAJour() {
        return nbrTachesMisesAJour.get();
    }

    @Override
    public void incrNbrTachesPlanifiees() {
        nbrTachePlanifiees.set(nbrTachePlanifiees.get() + 1);
    }

    @Override
    public void incrNbrTachesImportees() {
        nbrTachesImportees.set(nbrTachesImportees.get() + 1);
    }

    @Override
    public void incrNbrTachesSupprimees() {
        nbrTachesSupprimees.set(nbrTachesSupprimees.get() + 1);
    }

    @Override
    public void incrNbrTachesAjoutees() {
        nbrTachesAjoutees.set(nbrTachesAjoutees.get() + 1);
    }

    @Override
    public void incrNbrTachesMisesAJour() {
        nbrTachesMisesAJour.set(nbrTachesMisesAJour.get() + 1);
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
