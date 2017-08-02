package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ReferentielsService;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.Copiable;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;
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
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class TacheBean extends AbstractBean<TacheDTO, TacheBean> implements Copiable<TacheBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TacheBean.class);

    @NotNull
    public static final String FORMAT_DATE = PlanChargeIhm.PATRON_FORMAT_DATE;
    @NotNull
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

    @SuppressWarnings("InstanceVariableNamingConvention")
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
    private ObjectProperty<ProjetAppliBean> projetAppli = new SimpleObjectProperty<>();
    @NotNull
    private  ObjectProperty<StatutBean> statut = new SimpleObjectProperty<>();
    @NotNull
    private ObjectProperty<LocalDate> debut = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    @NotNull
    private ObjectProperty<LocalDate> echeance = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    @NotNull
    private ObjectProperty<ImportanceBean> importance = new SimpleObjectProperty<>();
    @NotNull
    private ObjectProperty<RessourceBean> ressource = new SimpleObjectProperty<>();
    @NotNull
    private DoubleProperty charge = new SimpleDoubleProperty();
    @NotNull
    private ObjectProperty<ProfilBean> profil = new SimpleObjectProperty<>();

    //    @Autowired
    @NotNull
    private ReferentielsService referentielsService = ReferentielsService.instance();


    @SuppressWarnings("ConstructorWithTooManyParameters")
    public TacheBean(int id, @Null String codeCategorie, @Null String codeSousCategorie, @Null String noTicketIdal, @Null String description, @Null ProjetAppliBean projetAppli, @Null StatutBean statut, @Null LocalDate debut, @Null LocalDate echeance, @Null ImportanceBean importance, @Null Double charge, @Null RessourceBean ressource, @Null ProfilBean profil) {
        this.id.set(id);
        this.codeCategorie.set(codeCategorie);
        this.codeSousCategorie.set(codeSousCategorie);
        this.noTicketIdal.set(noTicketIdal);
        this.description.set(description);
        this.projetAppli.set(projetAppli);
        this.statut.set(statut);
        this.debut.set(debut);
        this.echeance.set(echeance);
        this.importance.set(importance);
        this.charge.set(charge);
        this.ressource.set(ressource);
        this.profil.set(profil);
    }

    TacheBean(@NotNull TacheBean tacheBean) {
        this(
                tacheBean.getId(),
                tacheBean.getCodeCategorie(),
                tacheBean.getCodeSousCategorie(),
                tacheBean.getNoTicketIdal(),
                tacheBean.getDescription(),
                tacheBean.getProjetAppli(),
                tacheBean.getStatut(),
                tacheBean.getDebut(),
                tacheBean.getEcheance(),
                tacheBean.getImportance(),
                tacheBean.getCharge(),
                tacheBean.getRessource(),
                tacheBean.getProfil()
        );
    }

    TacheBean(@NotNull TacheDTO tache) {
        this.id.set(tache.getId());
        this.codeCategorie.set(tache.getCategorie().getCode());
        if (tache.getSousCategorie() != null) {
            this.codeSousCategorie.set(tache.getSousCategorie().getCode());
        }
        this.noTicketIdal.set(tache.getNoTicketIdal());
        this.description.set(tache.getDescription());
        this.projetAppli.set(ProjetAppliBean.from(tache.getProjetAppli()));
        this.statut.set(StatutBean.from(tache.getStatut()));
        this.debut.set(tache.getDebut());
        this.echeance.set(tache.getEcheance());
        this.importance.set(ImportanceBean.from(tache.getImportance()));
        this.charge.set(tache.getCharge());
        this.ressource.set(RessourceBean.from(tache.getRessource()));
        this.profil.set(ProfilBean.from(tache.getProfil()));
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
            this.projetAppli.set(projetAppli.getCode());
        }
        this.debut.set(debut);
        this.echeance.set(echeance);
        if (importance != null) {
            this.importance.set(importance.getCode());
        }
        this.charge.set(charge);
        if (ressource != null) {
            this.ressource.set(ressource.getCode());
        }
        if (profil != null) {
            this.profil.set(profil.getCode());
        }
    }
*/

    public int getId() {
        assert id != null;
        return id.get();
    }

    @NotNull
    public IntegerProperty idProperty() {
        return id;
    }

    @Null
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

    @Null
    public String getNoTicketIdal() {
        return noTicketIdal.get();
    }

    @NotNull
    public StringProperty noTicketIdalProperty() {
        return noTicketIdal;
    }

    @Null
    public String getDescription() {
        return description.get();
    }

    @NotNull
    public StringProperty descriptionProperty() {
        return description;
    }

    @Null
    public ProjetAppliBean getProjetAppli() {
        return projetAppli.get();
    }

    @NotNull
    public ObjectProperty<ProjetAppliBean> projetAppliProperty() {
        return projetAppli;
    }

    @Null
    public StatutBean getStatut() {
        return statut.get();
    }

    @NotNull
    public ObjectProperty<StatutBean> statutProperty() {
        return statut;
    }

    @Null
    public LocalDate getDebut() {
        return debut.get();
    }

    @NotNull
    public ObjectProperty<LocalDate> debutProperty() {
        return debut;
    }

    @Null
    public LocalDate getEcheance() {
        return echeance.get();
    }

    @NotNull
    public ObjectProperty<LocalDate> echeanceProperty() {
        return echeance;
    }

    @Null
    public ImportanceBean getImportance() {
        return importance.get();
    }

    @NotNull
    public ObjectProperty<ImportanceBean> importanceProperty() {
        return importance;
    }

    @Null
    public Double getCharge() {
        return charge.get();
    }

    @NotNull
    public DoubleProperty chargeProperty() {
        return charge;
    }

    @Null
    public RessourceBean getRessource() {
        return ressource.get();
    }

    @NotNull
    public ObjectProperty<RessourceBean> ressourceProperty() {
        return ressource;
    }

    @Null
    public ProfilBean getProfil() {
        return profil.get();
    }

    @NotNull
    public ObjectProperty<ProfilBean> profilProperty() {
        return profil;
    }


    @Null
    public StringBinding noTacheProperty() {
        return idProperty().asString(Tache.FORMAT_NO_TACHE);
    }

    @NotNull
    public String noTache() {
        return Tache.noTache(id.get());
    }


    @NotNull
    @Override
    public TacheDTO toDto() throws BeanException {
        try {
            return new TacheDTO(
                    getId(),
                    CategorieTacheDTO.valeur(getCodeCategorie()),
                    SousCategorieTacheDTO.valeur(getCodeSousCategorie()),
                    getNoTicketIdal(),
                    getDescription(),
                    ProjetAppliBean.to(getProjetAppli()),
                    StatutBean.to(getStatut()),
                    getDebut(),
                    getEcheance(),
                    ImportanceBean.to(getImportance()),
                    getCharge(),
                    RessourceBean.to(getRessource()),
                    ProfilBean.to(getProfil())
            );
        } catch (DTOException e) {
            throw new BeanException("Impossible de transformer la tâche de bean en DTO.", e);
        }
    }

    @NotNull
    @Override
    public TacheBean fromDto(@NotNull TacheDTO dto) throws BeanException {
        return new TacheBean(dto);
    }


/* planCharge-52 Filtre global inopérant -> Incompatible avec TableFilter. Désactivé le temps de rendre compatible (TableFilter préféré).
    public boolean matcheNoTache(@NotNull String otherValue) {
        if ((getId() + "").contains(otherValue)) {
            return true; // matches
        }
        if (noTache().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheCategorie(@NotNull String otherValue) {
        if (getCodeCategorie() == null) {
            return false;
        }
        if (getCodeCategorie().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheSousCategorie(@NotNull String otherValue) {
        if (getCodeSousCategorie() == null) {
            return false;
        }
        if (getCodeSousCategorie().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheNoTicketIdal(@NotNull String otherValue) {
        if (getNoTicketIdal() == null) {
            return false;
        }
        if (getNoTicketIdal().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheDescription(@NotNull String otherValue) throws IhmException {
        if (getDescription() == null) {
            return false;
        }
        Pattern patron;
        try {
            patron = Pattern.compile(otherValue);
        } catch (PatternSyntaxException e) {
            throw new IhmException("Impossible de filtrer les descriptions des tâches à partir d'une expression régulière incorrecte : '" + otherValue + "'.", e);
        }
        Matcher matcher = patron.matcher(getDescription());
        if (matcher.find()) {
            return true; // matches
        }

        return false; // does not match.
    }

    public boolean matcheProjetAppli(@NotNull String otherValue) {
        if (getProjetAppli() == null) {
            return false;
        }
        if (getProjetAppli().getCode() == null) {
            return false;
        }
        if (getProjetAppli().getCode().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheImportance(@NotNull Importance otherValue) {
        if (getImportance() == null) {
            return false;
        }
        if (getImportance().getCode().contains(otherValue.getCode())) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheImportance(@NotNull String otherValue) {
        if (getImportance() == null) {
            return false;
        }
        if (getImportance().getCode().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheDebut(@NotNull String otherValue) {
        if (getDebut() == null) {
            return false;
        }
        if (getDebut().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheEcheance(@NotNull String otherValue) {
        if (getEcheance() == null) {
            return false;
        }
        if (getEcheance().format(DATE_FORMATTER).contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheRessource(@NotNull String otherValue) {
        if (getRessource() == null) {
            return false;
        }
        if (getRessource().getCode().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }

    public boolean matcheProfil(@NotNull String otherValue) {
        if (getProfil() == null) {
            return false;
        }
        if (getProfil().getCode() == null) {
            return false;
        }
        if (getProfil().getCode().contains(otherValue)) {
            return true; // matches
        }
        return false; // does not match.
    }
*/


    public TacheDTO extract() throws BeanException {
        TacheDTO tache;
        try {
            tache = new TacheDTO(
                    id.get(),
                    (codeCategorie.isEmpty().get() ? null : CategorieTacheDTO.valeur(codeCategorie.get())),
                    (codeSousCategorie.isEmpty().get() ? null : SousCategorieTacheDTO.valeur(codeSousCategorie.get())),
                    noTicketIdal.get(),
                    description.get(),
                    ProjetAppliBean.to(projetAppli.get()),
                    StatutBean.to(statut.get()),
                    debut.get(),
                    echeance.get(),
                    ImportanceBean.to(importance.get()),
                    charge.get(),
                    RessourceBean.to(ressource.get()),
                    ProfilBean.to(profil.get())
            );
        } catch (DTOException e) {
            throw new BeanException("Impossible d'extraire la tâche depuis le bean.", e);
        }
        return tache;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TacheBean)) return false;

        TacheBean tacheBean = (TacheBean) o;

        return getId() == tacheBean.getId();
    }

    @Override
    public int hashCode() {
        return new Integer(getId()).hashCode();
    }


    @Override
    public TacheBean copier() throws CopieException {
        return new TacheBean(this);
    }

    public void copier(@NotNull TacheBean original) throws CopieException {
        this.idProperty().set(original.idProperty().get());
        this.codeCategorieProperty().set(original.codeCategorieProperty().get());
        this.codeSousCategorieProperty().set(original.codeSousCategorieProperty().get());
        this.noTicketIdalProperty().set(original.noTicketIdalProperty().get());
        this.descriptionProperty().set(original.descriptionProperty().get());
        this.projetAppliProperty().set(original.projetAppliProperty().get());
        this.debutProperty().set(original.debutProperty().get());
        this.echeanceProperty().set(original.echeanceProperty().get());
        this.chargeProperty().set(original.chargeProperty().get());
        this.ressourceProperty().set(original.ressourceProperty().get());
        this.profilProperty().set(original.profilProperty().get());
    }


    // Pour les débug, uniquement.
    @Override
    @NotNull
    public String toString() {
        return /*(codeCategorie.get() + (codeSousCategorie.get() == null ? "" : ("::" + codeSousCategorie.get())))
                + " "
                +*/ ("[" + (projetAppli.isNull().get() ? "N/A" : projetAppli.get()) + "]")
                + " "
                + noTache()
                + " "
                + ("(" + (noTicketIdal.isNull().get() ? "N/A" : noTicketIdal.get()) + ")")
                + " "
                + ("<< " + (description.isNull().get() ? "N/A" : description.get()) + " >> ")
                ;
    }
}
