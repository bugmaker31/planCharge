package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import fr.gouv.agriculture.dal.ct.planCharge.util.Strings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class RessourceHumaineBean extends RessourceBean<RessourceHumaineBean, RessourceHumaineDTO> implements Comparable<RessourceHumaineBean> {

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


    public RessourceHumaineBean() {
        super();
    }

    private RessourceHumaineBean(@Null String trigramme) {
        super(trigramme);
    }

    private RessourceHumaineBean(@Null String trigramme, @Null String nom, @Null String prenom, @Null String societe, @Null LocalDate debutMission, @Null LocalDate finMission) {
        this(trigramme);
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.societe.set(societe);
        this.debutMission.set(debutMission);
        this.finMission.set(finMission);
    }

    @SuppressWarnings("StaticMethodNamingConvention")
    @NotNull
    public static RessourceHumaineDTO to(@NotNull RessourceHumaineBean ressourceHumaineBean) {
        return ressourceHumaineBean.toDto();
    }

    @NotNull
    public static RessourceHumaineBean from(@NotNull RessourceHumaineDTO dto) {
        return new RessourceHumaineBean().fromDto(dto);
    }

    @Null
    public String getTrigramme() {
        return getCode();
    }

    @NotNull
    public StringProperty trigrammeProperty() {
        return codeProperty();
    }

    public void setTrigramme(@Null String trigramme) {
        this.codeProperty().set(trigramme);
    }

    @Null
    public String getNom() {
        return nom.get();
    }

    public void setNom(@Null String nom) {
        this.nom.set(nom);
    }

    @NotNull
    public StringProperty nomProperty() {
        return nom;
    }

    @Null
    public String getPrenom() {
        return prenom.get();
    }

    public void setPrenom(@Null String prenom) {
        this.prenom.set(prenom);
    }

    @NotNull
    public StringProperty prenomProperty() {
        return prenom;
    }

    @Null
    public String getSociete() {
        return societe.get();
    }

    public void setSociete(@Null String societe) {
        this.societe.set(societe);
    }

    @NotNull
    public StringProperty societeProperty() {
        return societe;
    }

    @Null
    public LocalDate getDebutMission() {
        return debutMission.get();
    }

    public void setDebutMission(@Null LocalDate debutMission) {
        this.debutMission.set(debutMission);
    }

    @NotNull
    public ObjectProperty<LocalDate> debutMissionProperty() {
        return debutMission;
    }

    @Null
    public LocalDate getFinMission() {
        return finMission.get();
    }

    public void setFinMission(@Null LocalDate finMission) {
        this.finMission.set(finMission);
    }

    @NotNull
    public ObjectProperty<LocalDate> finMissionProperty() {
        return finMission;
    }


    @NotNull
    @Override
    public RessourceHumaineDTO toDto() {
        return new RessourceHumaineDTO(
                Strings.epure(codeProperty().get()),
                Strings.epure(nom.get()),
                Strings.epure(prenom.get()),
                Strings.epure(societe.get()),
                debutMission.get(), finMission.get()
        );
    }

    @NotNull
    @Override
    public RessourceHumaineBean fromDto(@NotNull RessourceHumaineDTO dto) {
        return new RessourceHumaineBean(dto.getCode(), dto.getNom(), dto.getPrenom(), dto.getSociete(), dto.getDebutMission(), dto.getFinMission());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        RessourceHumaineBean that = (RessourceHumaineBean) o;

        return (getTrigramme() != null) ? getTrigramme().equals(that.getTrigramme()) : (that.getTrigramme() == null);
    }

    @Override
    public int hashCode() {
        return (getTrigramme() != null) ? getTrigramme().hashCode() : 0;
    }


    @Override
    public int compareTo(@NotNull RessourceHumaineBean o) {
        return getTrigramme().compareTo(o.getTrigramme());
    }


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        //noinspection HardcodedFileSeparator
        return "RsrcHum"
                + " " + Objects.value(getTrigramme(), "N/C")
                + " " + Objects.value(getNom(), "N/C")
                + " " + Objects.value(getPrenom(), "N/C");
    }

}
