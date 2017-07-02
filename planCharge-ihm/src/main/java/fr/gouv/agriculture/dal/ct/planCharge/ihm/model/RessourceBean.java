package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class RessourceBean {

    @NotNull
    private StringProperty trigramme = new SimpleStringProperty();
    @NotNull
    private StringProperty nom = new SimpleStringProperty();
    @NotNull
    private StringProperty prenom = new SimpleStringProperty();
    @NotNull
    private StringProperty societe = new SimpleStringProperty();
    @NotNull
    private ObjectProperty<LocalDate> debutMission = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object;
    @NotNull
    private ObjectProperty<LocalDate> finMission = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object;


    public RessourceBean() {
        super();
    }

    public RessourceBean(@NotNull Ressource ressource) {
        this.trigramme.set(ressource.getTrigramme());
        this.nom.set(ressource.getNom());
        this.prenom.set(ressource.getPrenom());
        this.societe.set(ressource.getSociete());
        this.debutMission.set(ressource.getDebutMission());
        this.finMission.set(ressource.getFinMission());
    }


    @NotNull
    public String getTrigramme() {
        return trigramme.get();
    }

    @NotNull
    public StringProperty trigrammeProperty() {
        return trigramme;
    }

    @NotNull
    public String getNom() {
        return nom.get();
    }

    @NotNull
    public StringProperty nomProperty() {
        return nom;
    }

    @NotNull
    public String getPrenom() {
        return prenom.get();
    }

    @NotNull
    public StringProperty prenomProperty() {
        return prenom;
    }

    @NotNull
    public String getSociete() {
        return societe.get();
    }

    @NotNull
    public StringProperty societeProperty() {
        return societe;
    }

    @NotNull
    public LocalDate getDebutMission() {
        return debutMission.get();
    }

    @NotNull
    public ObjectProperty<LocalDate> debutMissionProperty() {
        return debutMission;
    }

    @NotNull
    public LocalDate getFinMission() {
        return finMission.get();
    }

    @NotNull
    public ObjectProperty<LocalDate> finMissionProperty() {
        return finMission;
    }


    @NotNull
    public Ressource extract() {
        return new Ressource(trigramme.get(), nom.get(), prenom.get(), societe.get(), debutMission.get(), finMission.get());
    }


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return trigramme.get() + " : " + nom.get() + " " + prenom.get();
    }
}
