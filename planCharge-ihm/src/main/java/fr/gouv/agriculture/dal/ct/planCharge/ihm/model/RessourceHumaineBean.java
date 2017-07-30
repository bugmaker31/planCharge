package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceHumaineDTO;
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
public class RessourceHumaineBean extends AbstractBean<RessourceHumaineDTO, RessourceHumaineBean> {

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


    public RessourceHumaineBean() {
        super();
    }

    public RessourceHumaineBean(@Null String trigramme) {
        super();
        this.trigramme.set(trigramme);
    }

    public RessourceHumaineBean(@Null String trigramme, @Null String nom, @Null String prenom, @Null String societe, @Null LocalDate debutMission, @Null LocalDate finMission) {
        this(trigramme);
        this.nom.set(nom);
        this.prenom.set(prenom);
        this.societe.set(societe);
        this.debutMission.set(debutMission);
        this.finMission.set(finMission);
    }


    @Null
    public String getTrigramme() {
        return trigramme.get();
    }

    @NotNull
    public StringProperty trigrammeProperty() {
        return trigramme;
    }

    @Null
    public String getNom() {
        return nom.get();
    }

    @NotNull
    public StringProperty nomProperty() {
        return nom;
    }

    @Null
    public String getPrenom() {
        return prenom.get();
    }

    @NotNull
    public StringProperty prenomProperty() {
        return prenom;
    }

    @Null
    public String getSociete() {
        return societe.get();
    }

    @NotNull
    public StringProperty societeProperty() {
        return societe;
    }

    @Null
    public LocalDate getDebutMission() {
        return debutMission.get();
    }

    @NotNull
    public ObjectProperty<LocalDate> debutMissionProperty() {
        return debutMission;
    }

    @Null
    public LocalDate getFinMission() {
        return finMission.get();
    }

    @Null
    public ObjectProperty<LocalDate> finMissionProperty() {
        return finMission;
    }


    public void setTrigramme(@Null String trigramme) {
        this.trigramme.set(trigramme);
    }

    public void setNom(@Null String nom) {
        this.nom.set(nom);
    }

    public void setPrenom(@Null String prenom) {
        this.prenom.set(prenom);
    }

    public void setSociete(@Null String societe) {
        this.societe.set(societe);
    }

    public void setDebutMission(@Null LocalDate debutMission) {
        this.debutMission.set(debutMission);
    }

    public void setFinMission(@Null LocalDate finMission) {
        this.finMission.set(finMission);
    }


    @NotNull
    @Override
    public RessourceHumaineDTO toDto() {
        return new RessourceHumaineDTO(trigramme.get(), nom.get(), prenom.get(), societe.get(), debutMission.get(), finMission.get());
    }

    @NotNull
    @Override
    public RessourceHumaineBean fromDto(@NotNull RessourceHumaineDTO dto) {
        return new RessourceHumaineBean(dto.getCode(), dto.getNom(), dto.getPrenom(), dto.getSociete(), dto.getDebutMission(), dto.getFinMission());
    }

    @NotNull
    public static RessourceHumaineDTO toDTO(@NotNull RessourceHumaineBean ressourceHumaineBean) {
        return ressourceHumaineBean.toDto();
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


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return trigramme.get() + " : " + nom.get() + " " + prenom.get();
    }

}
