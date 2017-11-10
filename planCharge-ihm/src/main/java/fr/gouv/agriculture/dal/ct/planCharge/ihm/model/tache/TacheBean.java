package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.TypeChangement;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.ITache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.TacheService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import fr.gouv.agriculture.dal.ct.planCharge.util.Strings;
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
public class TacheBean extends AbstractBean<TacheDTO, TacheBean> implements Copiable<TacheBean>, ITache<TacheBean> {

    @SuppressWarnings("WeakerAccess")
    @NotNull
    public static final String FORMAT_DATE = PlanChargeIhm.PATRON_FORMAT_DATE;
    @SuppressWarnings("WeakerAccess")
    @NotNull
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(FORMAT_DATE);

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(TacheBean.class);


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
    private ObjectProperty<StatutBean> statut = new SimpleObjectProperty<>();
    @NotNull
    private ObjectProperty<LocalDate> debut = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    @NotNull
    private ObjectProperty<LocalDate> echeance = new SimpleObjectProperty<>(); // Cf. http://stackoverflow.com/questions/29174497/how-to-bind-unbind-a-date-type-attribute-to-a-datepicker-object
    @NotNull
    private ObjectProperty<ImportanceBean> importance = new SimpleObjectProperty<>();
    @NotNull
    private ObjectProperty<RessourceBean<?, ?>> ressource = new SimpleObjectProperty<>();
    @NotNull
    private DoubleProperty charge = new SimpleDoubleProperty();
    @NotNull
    private ObjectProperty<ProfilBean> profil = new SimpleObjectProperty<>();

    @Null
    private TypeChangement typeChangement;

    //    @Autowired
    @NotNull
    private TacheService tacheService = TacheService.instance();


    @SuppressWarnings("ConstructorWithTooManyParameters")
    public TacheBean(int id, @Null String codeCategorie, @Null String codeSousCategorie, @Null String noTicketIdal, @Null String description, @Null ProjetAppliBean projetAppli, @Null StatutBean statut, @Null LocalDate debut, @Null LocalDate echeance, @Null ImportanceBean importance, @Null Double charge, @Null RessourceBean ressource, @Null ProfilBean profil) {
        super();
        this.id.set(id);
        this.codeCategorie.setValue(codeCategorie);
        this.codeSousCategorie.setValue(codeSousCategorie);
        this.noTicketIdal.setValue(noTicketIdal);
        this.description.setValue(description);
        this.projetAppli.setValue(projetAppli);
        this.statut.setValue(statut);
        this.debut.setValue(debut);
        this.echeance.setValue(echeance);
        this.importance.setValue(importance);
        this.charge.setValue(charge);
        this.ressource.setValue(ressource);
        this.profil.setValue(profil);
    }

    protected TacheBean(@NotNull TacheBean tacheBean) {
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
        typeChangement = tacheBean.getTypeChangement();
    }

    protected TacheBean(@NotNull TacheDTO tacheDTO) {
        //noinspection RedundantTypeArguments
        this(
                tacheDTO.getId(),
                Objects.value(tacheDTO.getCategorie(), CategorieTacheDTO::getCode),
                Objects.value(tacheDTO.getSousCategorie(), SousCategorieTacheDTO::getCode),
                tacheDTO.getNoTicketIdal(),
                tacheDTO.getDescription(),
                Objects.value(tacheDTO.getProjetAppli(), ProjetAppliBean::from),
                Objects.<StatutDTO, StatutBean>value(tacheDTO.getStatut(), StatutBean::safeFrom),
                tacheDTO.getDebut(),
                tacheDTO.getEcheance(),
                Objects.value(tacheDTO.getImportance(), ImportanceBean::from),
                tacheDTO.getCharge(),
                Objects.value(tacheDTO.getRessource(), RessourceBean::from),
                Objects.value(tacheDTO.getProfil(), ProfilBean::from)
        );
        typeChangement = tacheDTO.getTypeChangement();
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
    public RessourceBean<?, ?> getRessource() {
        return ressource.get();
    }

    @NotNull
    public ObjectProperty<RessourceBean<?, ?>> ressourceProperty() {
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
    public TypeChangement getTypeChangement() {
        return typeChangement;
    }


    public void setDebut(@NotNull LocalDate debut) {
        this.debut.set(debut);
    }

    public void setEcheance(@NotNull LocalDate echeance) {
        this.echeance.set(echeance);
    }


    public void setTypeChangement(@Null TypeChangement typeChangement) {
        this.typeChangement = typeChangement;
    }


    @Null
    public StringBinding noTacheProperty() {
        return idProperty().asString(Tache.FORMAT_NO_TACHE);
    }

    @NotNull
    public String noTache() {
        return Tache.noTache(getId());
    }


    public boolean estATraiter() throws IhmException {
        TacheDTO tacheDTO = toDto();
        return tacheService.estATraiter(tacheDTO);
    }

    // ITache

    public boolean estProvision() {
        TacheDTO tacheDTO = toDto();
        return tacheDTO.estProvision();
    }

    @Override
    public int compareTo(@Null TacheBean o) {
        TacheDTO tacheDTO = toDto();
        TacheDTO autreTacheDTO = (o == null) ? null : o.toDto();
        return tacheDTO.compareTo(autreTacheDTO);
    }


    @NotNull
    @Override
    public TacheDTO toDto() /*throws BeanException*/ {
/*
        try {
*/
            TacheDTO tacheDTO = new TacheDTO(
                    getId(),
                    (getCodeCategorie() == null) ? null : CategorieTacheDTO.valeurOuNull(getCodeCategorie()),
                    (getCodeSousCategorie() == null) ? null : SousCategorieTacheDTO.valeurOuNull(getCodeSousCategorie()),
                    getNoTicketIdal(),
                    getDescription(),
                    (getProjetAppli() == null) ? null : ProjetAppliBean.to(getProjetAppli()),
                    (getStatut() == null) ? null : StatutBean.safeTo(getStatut()),
                    getDebut(),
                    getEcheance(),
                    (getImportance() == null) ? null : ImportanceBean.to(getImportance()),
                    getCharge(),
                    (getRessource() == null) ? null : RessourceBean.to(getRessource()),
                    (getProfil() == null) ? null : ProfilBean.to(getProfil())
            );

            tacheDTO.setTypeChangement(getTypeChangement());

            return tacheDTO;
/*
        } catch (DTOException | BeanException e) {
            throw new BeanException("Impossible de transformer la tâche de Bean en DTO.", e);
        }
*/
    }

    @NotNull
    @Override
    public TacheBean fromDto(@NotNull TacheDTO dto) {
        return from(dto);
    }

    public static TacheBean from(@NotNull TacheDTO dto) {
        TacheBean tacheBean = new TacheBean(dto);
        tacheBean.setTypeChangement(dto.getTypeChangement());
        return tacheBean;
    }


    public boolean matcheGlobal(@Null String filtreGlobal) {

        // If filter text is empty, display all data.
        if ((filtreGlobal == null) || filtreGlobal.isEmpty()) {
            return true;
        }

        // Compare column values with filter text.
        if (matcheCategorie(filtreGlobal)) {
            return true; // Filter matches
        }
        if (matcheSousCategorie(filtreGlobal)) {
            return true; // Filter matches
        }
        if (matcheNoTache(filtreGlobal)) {
            return true; // Filter matches
        }
        if (!noTicketIdalProperty().isEmpty().get() && matcheNoTicketIdal(filtreGlobal)) {
            return true; // Filter matches
        }
        if (!descriptionProperty().isEmpty().get()) {
            try {
                if (Strings.estExpressionReguliere(filtreGlobal) && matcheDescription(filtreGlobal)) {
                    return true; // Filter matches
                }
            } catch (IhmException e) {
                LOGGER.error("Impossible de filtrer sur la description '" + filtreGlobal + "' pour la tâche n° " + getId() + ".", e);
            }
        }
        if (!debutProperty().isNull().get() && matcheDebut(filtreGlobal)) {
            return true; // Filter matches
        }
        if (!echeanceProperty().isNull().get() && matcheEcheance(filtreGlobal)) {
            return true; // Filter matches
        }
        if (!projetAppliProperty().isNull().get() && matcheProjetAppli(filtreGlobal)) {
            return true; // Filter matches
        }
        if (!importanceProperty().isNull().get() && matcheImportance(filtreGlobal)) {
            return true; // Filter matches
        }
        if (!ressourceProperty().isNull().get() && matcheRessource(filtreGlobal)) {
            return true; // Filter matches
        }
        //noinspection RedundantIfStatement
        if (!profilProperty().isNull().get() && matcheProfil(filtreGlobal)) {
            return true; // Filter matches
        }
        return false; // Does not match.
    }

    public boolean matcheNoTache(@NotNull String otherValue) {
        if ((String.valueOf(getId())).contains(otherValue)) {
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

    public boolean matcheDescription(@NotNull String otherValue) throws BeanException {
        if (getDescription() == null) {
            return false;
        }
        Pattern patron;
        try {
            patron = Pattern.compile(otherValue);
        } catch (PatternSyntaxException e) {
            throw new BeanException("Impossible de filtrer les descriptions des tâches à partir d'une expression régulière incorrecte : '" + otherValue + "'.", e);
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

    public boolean matcheStatut(@NotNull String otherValue) {
        if (getStatut() == null) {
            return false;
        }
        if (getStatut().getCode().contains(otherValue)) {
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


/*
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
*/


    @Override
    public boolean equals(@Null Object o) {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof TacheBean)) return false;
        TacheBean tacheBean = (TacheBean) o;
        return getId() == tacheBean.getId();
    }

    @Override
    public int hashCode() {
        return new Integer(getId()).hashCode();
    }


    // Implémentation de Copiable :

    @NotNull
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
        return "Tâche"
                + " " + noTache()
                + " "
                + ("/" + (noTicketIdal.isNull().get() ? "N/A" : noTicketIdal.get()))
                //+ " " + (codeCategorie.get() + (codeSousCategorie.get() == null ? "" : ("::" + codeSousCategorie.get())))
                + " " + ("[" + (projetAppli.isNull().get() ? "N/A" : projetAppli.get()) + "]")
                + " "
                + ("<< " + (description.isNull().get() ? "N/A" : description.get()) + " >> ")
                ;
    }

}
