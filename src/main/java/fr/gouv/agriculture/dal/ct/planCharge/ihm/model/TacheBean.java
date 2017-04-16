package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.Tache;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class TacheBean {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Tache tache;
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty noTicketIdal = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty projetAppli = new SimpleStringProperty();
    private ObjectProperty<LocalDate> debut = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    private ObjectProperty<LocalDate> echeance = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    private StringProperty importance = new SimpleStringProperty();
    private DoubleProperty charge = new SimpleDoubleProperty();
    private StringProperty ressource = new SimpleStringProperty();
    private StringProperty profil = new SimpleStringProperty();

    public TacheBean(Tache tache) {
        this.tache = tache;

        this.id.set(tache.getId());
        this.noTicketIdal.set(tache.getNoTicketIdal());
        this.description.set(tache.getDescription());
        this.projetAppli.set(tache.getProjetAppli().getCode());
        this.debut.set(tache.getDebut());
        this.echeance.set(tache.getEcheance());
        this.importance.set(tache.getImportance().getCode());
        this.charge.set(tache.getCharge());
        this.ressource.set(tache.getRessource().getTrigramme());
        this.profil.set(tache.getProfil().getCode());
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNoTicketIdal() {
        return noTicketIdal.get();
    }

    public StringProperty noTicketIdalProperty() {
        return noTicketIdal;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getProjetAppli() {
        return projetAppli.get();
    }

    public StringProperty projetAppliProperty() {
        return projetAppli;
    }

    public LocalDate getDebut() {
        return debut.get();
    }

    public ObjectProperty<LocalDate> debutProperty() {
        return debut;
    }

    public LocalDate getEcheance() {
        return echeance.get();
    }

    public ObjectProperty<LocalDate> echeanceProperty() {
        return echeance;
    }

    public String getImportance() {
        return importance.get();
    }

    public StringProperty importanceProperty() {
        return importance;
    }

    public double getCharge() {
        return charge.get();
    }

    public DoubleProperty chargeProperty() {
        return charge;
    }

    public String getRessource() {
        return ressource.get();
    }

    public StringProperty ressourceProperty() {
        return ressource;
    }

    public String getProfil() {
        return profil.get();
    }

    public StringProperty profilProperty() {
        return profil;
    }

    public StringBinding noTacheProperty() {
        return idProperty().asString(Tache.FORMAT_NO_TACHE);
    }

    public String noTache() {
        return tache.noTache();
    }

    public boolean matcheNoTache(String otherValue) {
        if (new String(getId() + "").contains(otherValue)) {
            return true; // matches
        }
        if (noTache().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheNoTicketIdal(String otherValue) {
        if (getNoTicketIdal().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheDescription(String otherValue) {
        if (getDescription().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheProjetAppli(String otherValue) {
        if (getProjetAppli().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheImportance(String otherValue) {
        if (getImportance().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheDebut(String otherValue) {
        if (getDebut().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheEcheance(String otherValue) {
        if (getEcheance().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }


    public boolean matcheRessource(String otherValue) {
        if (getRessource().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheProfil(String otherValue) {
        if (getProfil().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    // Pour les débug, uniquement.
    @Override
    public String toString() {
        return tache.toString();
    }
}
