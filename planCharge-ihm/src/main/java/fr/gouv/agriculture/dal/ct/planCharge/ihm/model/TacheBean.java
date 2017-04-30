package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class TacheBean {

    @NotNull
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty noTicketIdal = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty projetAppli = new SimpleStringProperty();
    private ObjectProperty<LocalDate> debut = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    private ObjectProperty<LocalDate> echeance = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    private ObjectProperty<Importance> importance = new SimpleObjectProperty<>();
    private DoubleProperty charge = new SimpleDoubleProperty();
    private StringProperty ressource = new SimpleStringProperty();
    private StringProperty profil = new SimpleStringProperty();

    //    @Autowired
    @NotNull
    private ProjetAppliDao projetAppliDao = ProjetAppliDao.instance();
    //    @Autowired
    @NotNull
    private RessourceDao ressourceDao = RessourceDao.instance();
    //    @Autowired
    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();
    //    @Autowired
    @NotNull
    private ProfilDao profilDao = ProfilDao.instance();

    public TacheBean(@NotNull Tache tache) {
        this.id.set(tache.getId());
        this.noTicketIdal.set(tache.getNoTicketIdal());
        this.description.set(tache.getDescription());
        if (tache.getProjetAppli() != null) {
            this.projetAppli.set(tache.getProjetAppli().getCode());
        }
        this.debut.set(tache.getDebut());
        this.echeance.set(tache.getEcheance());
        if (tache.getImportance() != null) {
            this.importance.set(tache.getImportance());
        }
        this.charge.set(tache.getCharge());
        if (tache.getRessource() != null) {
            this.ressource.set(tache.getRessource().getTrigramme());
        }
        if (tache.getProfil() != null) {
            this.profil.set(tache.getProfil().getCode());
        }
    }

    public TacheBean(int id, String noTicketIdal, String description, ProjetAppli projetAppli, LocalDate debut, LocalDate echeance, Importance importance, double charge, Ressource ressource, Profil profil) {
        this.id.set(id);
        this.noTicketIdal.set(noTicketIdal);
        this.description.set(description);
        if (projetAppli != null) {
            this.projetAppli.set(projetAppli.getCode());
        }
        this.debut.set(debut);
        this.echeance.set(echeance);
        if (importance != null) {
            this.importance.set(importance);
        }
        this.charge.set(charge);
        if (ressource != null) {
            this.ressource.set(ressource.getTrigramme());
        }
        if (profil != null) {
            this.profil.set(profil.getCode());
        }
    }

    @NotNull
    public int getId() {
        return id == null ? null : id.get();
    }

    @NotNull
    public IntegerProperty idProperty() {
        return id;
    }

    @NotNull
    public String getNoTicketIdal() {
        return noTicketIdal.get();
    }

    @NotNull
    public StringProperty noTicketIdalProperty() {
        return noTicketIdal;
    }

    @NotNull
    public String getDescription() {
        return description.get();
    }

    @NotNull
    public StringProperty descriptionProperty() {
        return description;
    }

    @NotNull
    public String getProjetAppli() {
        return projetAppli.get();
    }

    @NotNull
    public StringProperty projetAppliProperty() {
        return projetAppli;
    }

    @NotNull
    public LocalDate getDebut() {
        return debut.get();
    }

    @NotNull
    public ObjectProperty<LocalDate> debutProperty() {
        return debut;
    }

    @NotNull
    public LocalDate getEcheance() {
        return echeance.get();
    }

    @NotNull
    public ObjectProperty<LocalDate> echeanceProperty() {
        return echeance;
    }

    @NotNull
    public Importance getImportance() {
        return importance.get();
    }

    @NotNull
    public ObjectProperty<Importance> importanceProperty() {
        return importance;
    }

    @NotNull
    public double getCharge() {
        return charge.get();
    }

    @NotNull
    public DoubleProperty chargeProperty() {
        return charge;
    }

    @NotNull
    public String getRessource() {
        return ressource.get();
    }

    @NotNull
    public StringProperty ressourceProperty() {
        return ressource;
    }

    @NotNull
    public String getProfil() {
        return profil.get();
    }

    @NotNull
    public StringProperty profilProperty() {
        return profil;
    }

    @NotNull
    public StringBinding noTacheProperty() {
        return idProperty() == null ? null : idProperty().asString(Tache.FORMAT_NO_TACHE);
    }

    @NotNull
    public String noTache() {
        return Tache.noTache(id.get());
    }

    @NotNull
    public boolean matcheNoTache(@NotNull String otherValue) {
        if (new String(getId() + "").contains(otherValue)) {
            return true; // matches
        }
        if (noTache().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheNoTicketIdal(@NotNull String otherValue) {
        if (getNoTicketIdal().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheDescription(@NotNull String otherValue) {
        if (getDescription().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheProjetAppli(@NotNull String otherValue) {
        if (getProjetAppli().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheImportance(@NotNull Importance otherValue) {
        if (getImportance().getCode().contains(otherValue.getCode())) {
            return true; // matches
        }
        return false; // does not match.
    }
    @NotNull
    public boolean matcheImportance(@NotNull String otherValue) {
        if (getImportance().getCode().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheDebut(@NotNull String otherValue) {
        if (getDebut().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheEcheance(@NotNull String otherValue) {
        if (getEcheance().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }


    @NotNull
    public boolean matcheRessource(@NotNull String otherValue) {
        if (getRessource().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheProfil(@NotNull String otherValue) {
        if (getProfil().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    // Pour les débug, uniquement.
    @Override
    @NotNull
    public String toString() {
        return ("[" + projetAppli.get() + "]")
                + " "
                + noTache()
                + " "
                + ("(" + (noTicketIdal.isEmpty().get() ? "N/A" : noTicketIdal.get()) + ")")
                + " "
                + ("<< " + description.get() + " >> ")
                ;
    }

    public Tache extract() {
        Tache tache;
        try {
            tache = new Tache(
                    id.get(),
                    noTicketIdal.get(),
                    description.get(),
                    projetAppliDao.load(projetAppli.get()),
                    debut.get(),
                    echeance.get(),
                    importance.get(),
                    charge.get(),
                    ressourceDao.load(ressource.get()),
                    profilDao.load(profil.get())
            );
        } catch (ModeleException | DaoException e) {
            throw new IhmException("Impossible d'extraire la tâche depuis le bean.", e);
        }
        return tache;
    }
}
