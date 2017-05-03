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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by frederic.danna on 11/03/2017.
 */
public class TacheBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(TacheBean.class);

    @NotNull
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @NotNull
    private IntegerProperty id = new SimpleIntegerProperty();
    @NotNull
    private StringProperty codeCategorie = new SimpleStringProperty();
    @NotNull
    private StringProperty codeSousCategorie = new SimpleStringProperty();
    @NotNull
    private StringProperty noTicketIdal = new SimpleStringProperty();
    @NotNull
    private StringProperty description = new SimpleStringProperty();
    @NotNull
    private StringProperty codeProjetAppli = new SimpleStringProperty();
    @NotNull
    private ObjectProperty<LocalDate> debut = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    @NotNull
    private ObjectProperty<LocalDate> echeance = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    @NotNull
    private StringProperty codeImportance = new SimpleStringProperty();
    @NotNull
    private StringProperty codeRessource = new SimpleStringProperty();
    @NotNull
    private DoubleProperty charge = new SimpleDoubleProperty();
    @NotNull
    private StringProperty codeProfil = new SimpleStringProperty();

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

    public TacheBean(int id, String codeCategorie, String codeSousCategorie, String noTicketIdal, String description, String codeProjetAppli, LocalDate debut, LocalDate echeance, String codeImportance, double charge, String codeRessource, String codeProfil) {
        this.id.set(id);
        this.codeCategorie.set(codeCategorie);
        this.codeSousCategorie.set(codeSousCategorie);
        this.noTicketIdal.set(noTicketIdal);
        this.description.set(description);
        this.codeProjetAppli.set(codeProjetAppli);
        this.debut.set(debut);
        this.echeance.set(echeance);
        this.codeImportance.set(codeImportance);
        this.charge.set(charge);
        this.codeRessource.set(codeRessource);
        this.codeProfil.set(codeProfil);
    }

    public TacheBean(TacheBean tacheBean) {
        this(
                tacheBean.getId(),
                tacheBean.getCodeCategorie(),
                tacheBean.getCodeSousCategorie(),
                tacheBean.getNoTicketIdal(),
                tacheBean.getDescription(),
                tacheBean.getCodeProjetAppli(),
                tacheBean.getDebut(),
                tacheBean.getEcheance(),
                tacheBean.getCodeImportance(),
                tacheBean.getCharge(),
                tacheBean.getCodeRessource(),
                tacheBean.getCodeProfil()
        );
    }

    TacheBean(@NotNull Tache tache) {
        this.id.set(tache.getId());
        this.codeCategorie.set(tache.getCategorie().getCode());
        if (tache.getSousCategorie() != null) {
            this.codeSousCategorie.set(tache.getSousCategorie().getCode());
        }
        this.noTicketIdal.set(tache.getNoTicketIdal());
        this.description.set(tache.getDescription());
        if (tache.getProjetAppli() != null) {
            this.codeProjetAppli.set(tache.getProjetAppli().getCode());
        }
        this.debut.set(tache.getDebut());
        this.echeance.set(tache.getEcheance());
        if (tache.getImportance() != null) {
            this.codeImportance.set(tache.getImportance().getCode());
        }
        this.charge.set(tache.getCharge());
        if (tache.getRessource() != null) {
            this.codeRessource.set(tache.getRessource().getTrigramme());
        }
        if (tache.getProfil() != null) {
            this.codeProfil.set(tache.getProfil().getCode());
        }
    }

/*
    public TacheBean(int id, CategorieTache categorie, SousCategorieTache sousCategorie, String noTicketIdal, String description, ProjetAppli projetAppli, LocalDate debut, LocalDate echeance, Importance importance, double charge, Ressource ressource, Profil profil) {
        this.id.set(id);
        if (categorie != null) {
            this.codeCategorie.set(categorie.getCode());
        }
        if (sousCategorie != null) {
            this.codeSousCategorie.set(sousCategorie.getCode());
        }
        this.noTicketIdal.set(noTicketIdal);
        this.description.set(description);
        if (projetAppli != null) {
            this.codeProjetAppli.set(projetAppli.getCode());
        }
        this.debut.set(debut);
        this.echeance.set(echeance);
        if (importance != null) {
            this.codeImportance.set(importance.getCode());
        }
        this.charge.set(charge);
        if (ressource != null) {
            this.codeRessource.set(ressource.getTrigramme());
        }
        if (profil != null) {
            this.codeProfil.set(profil.getCode());
        }
    }
*/

    @NotNull
    public int getId() {
        assert id != null;
        return id.get();
    }

    @NotNull
    public IntegerProperty idProperty() {
        return id;
    }

    @NotNull
    public String getCodeCategorie() {
        return codeCategorie.get();
    }

    @NotNull
    public StringProperty codeCategorieProperty() {
        return codeCategorie;
    }

    @Null
    public String getCodeSousCategorie() {
        return codeSousCategorie.get();
    }

    @NotNull
    public StringProperty codeSousCategorieProperty() {
        return codeSousCategorie;
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
    public String getCodeProjetAppli() {
        return codeProjetAppli.get();
    }

    @NotNull
    public StringProperty codeProjetAppliProperty() {
        return codeProjetAppli;
    }

    @Null
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
    public String getCodeImportance() {
        return codeImportance.get();
    }

    @NotNull
    public StringProperty codeImportanceProperty() {
        return codeImportance;
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
    public String getCodeRessource() {
        return codeRessource.get();
    }

    @NotNull
    public StringProperty codeRessourceProperty() {
        return codeRessource;
    }

    @NotNull
    public String getCodeProfil() {
        return codeProfil.get();
    }

    @NotNull
    public StringProperty codeProfilProperty() {
        return codeProfil;
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
    public boolean matcheCategorie(@NotNull String otherValue) {
        if (getCodeCategorie() == null) {
            return false;
        }
        if (getCodeCategorie().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheSousCategorie(@NotNull String otherValue) {
        if (getCodeSousCategorie() == null) {
            return false;
        }
        if (getCodeSousCategorie().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheNoTicketIdal(@NotNull String otherValue) {
        if (getNoTicketIdal() == null) {
            return false;
        }
        if (getNoTicketIdal().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheDescription(@NotNull String otherValue) throws IhmException {
        if (getDescription() == null) {
            return false;
        }
        Pattern patron;
        try {
            patron = Pattern.compile(otherValue);
        } catch (PatternSyntaxException e) {
            throw new IhmException("Patron de filtre incorrect : '" + otherValue + "'.", e);
        }
        Matcher matcher = patron.matcher(getDescription());
        if (matcher.find()) {
            return true; // matches
        }

        return false; // does not match.
    }

    @NotNull
    public boolean matcheProjetAppli(@NotNull String otherValue) {
        if (getCodeProjetAppli() == null) {
            return false;
        }
        if (getCodeProjetAppli().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheImportance(@NotNull Importance otherValue) {
        if (getCodeImportance() == null) {
            return false;
        }
        if (getCodeImportance().contains(otherValue.getCode())) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheImportance(@NotNull String otherValue) {
        if (getCodeImportance() == null) {
            return false;
        }
        if (getCodeImportance().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheDebut(@NotNull String otherValue) {
        if (getDebut() == null) {
            return false;
        }
        if (getDebut().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheEcheance(@NotNull String otherValue) {
        if (getEcheance() == null) {
            return false;
        }
        if (getEcheance().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }


    @NotNull
    public boolean matcheRessource(@NotNull String otherValue) {
        if (getCodeRessource() == null) {
            return false;
        }
        if (getCodeRessource().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    @NotNull
    public boolean matcheProfil(@NotNull String otherValue) {
        if (getCodeProfil() == null) {
            return false;
        }
        if (getCodeProfil().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    // Pour les débug, uniquement.
    @Override
    @NotNull
    public String toString() {
        return (codeCategorie.get() + (codeSousCategorie.get() == null ? "" : ("::" + codeSousCategorie.get())))
                + " "
                + ("[" + codeProjetAppli.get() + "]")
                + " "
                + noTache()
                + " "
                + ("(" + (noTicketIdal.isEmpty().get() ? "N/A" : noTicketIdal.get()) + ")")
                + " "
                + ("<< " + description.get() + " >> ")
                ;
    }

    public Tache extract() throws IhmException {
        Tache tache;
        try {
            tache = new Tache(
                    id.get(),
                    (codeCategorie.isEmpty().get() ? null : CategorieTache.valeur(codeCategorie.get())),
                    (codeSousCategorie.isEmpty().get() ? null : SousCategorieTache.valeur(codeSousCategorie.get())),
                    noTicketIdal.get(),
                    description.get(),
                    (codeProjetAppli.isEmpty().get() ? null : projetAppliDao.load(codeProjetAppli.get())),
                    debut.get(),
                    echeance.get(),
                    (codeImportance.isEmpty().get() ? null : importanceDao.loadByCode(codeImportance.get())),
                    charge.get(),
                    (codeRessource.isEmpty().get() ? null : ressourceDao.load(codeRessource.get())),
                    (codeProfil.isEmpty().get() ? null : profilDao.load(codeProfil.get()))
            );
        } catch (ModeleException | DaoException e) {
            throw new IhmException("Impossible d'extraire la tâche depuis le bean.", e);
        }
        return tache;
    }
}
