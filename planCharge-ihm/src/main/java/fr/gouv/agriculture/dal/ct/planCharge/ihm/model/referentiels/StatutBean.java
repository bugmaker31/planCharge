package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.StatutDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by frederic.danna on 01/07/2017.
 */
public class StatutBean extends AbstractBean<StatutDTO, StatutBean> implements Comparable<StatutBean> {

    public static final StatutBean NOUVEAU = init(StatutDTO.NOUVEAU);
    public static final StatutBean EN_COURS = init(StatutDTO.EN_COURS);
    public static final StatutBean EN_ATTENTE = init(StatutDTO.EN_ATTENTE);
    public static final StatutBean RECURRENT = init(StatutDTO.RECURRENT);
    public static final StatutBean REPORTE = init(StatutDTO.REPORTE);
    public static final StatutBean ANNULE = init(StatutDTO.ANNULE);
    public static final StatutBean DOUBLON = init(StatutDTO.DOUBLON);
    public static final StatutBean TERMINE = init(StatutDTO.TERMINE);
    public static final StatutBean A_VENIR = init(StatutDTO.A_VENIR);
    //
    public static final StatutBean PROVISION = init(StatutDTO.PROVISION);

    public static final Set<StatutBean> VALUES = new HashSet<>(20);

    static {
        Collections.addAll(VALUES,
                NOUVEAU,
                EN_COURS,
                EN_ATTENTE,
                RECURRENT,
                REPORTE,
                ANNULE,
                DOUBLON,
                TERMINE,
                A_VENIR,
                //
                PROVISION
        );
    }


    @NotNull
    public static StatutBean valueOf(@NotNull String code) throws ModeleException {
        return VALUES.parallelStream()
                .filter(statut -> statut.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new ModeleException("Statut non géré : '" + code + "'."));
    }

    private static StatutBean init(@NotNull StatutDTO statut) {
        return new StatutBean(statut);
    }


    // Fields :

    @NotNull
    private final StringProperty code = new SimpleStringProperty();


    // Constructeurs :

    private StatutBean(StatutDTO statut) {
        super();
        code.setValue(statut.getCode());
    }


    // Getters/Setters :

    @NotNull
    public String getCode() {
        return code.get();
    }

    @NotNull
    public StringProperty codeProperty() {
        return code;
    }


    // Méthodes :


    // AbstractBean

    @NotNull
    public static StatutBean from(@NotNull StatutDTO statutDTO) throws BeanException {
        try {
            return valueOf(statutDTO.getCode());
        } catch (ModeleException e) {
            throw new BeanException("Statut non géré.", e);
        }
    }

    @NotNull
    public static StatutDTO to(@NotNull StatutBean statutBean) throws BeanException {
        try {
            return StatutDTO.valueOf(statutBean.getCode());
        } catch (ModeleException e) {
            throw new BeanException("Statut non géré.", e);
        }
    }


    @NotNull
    @Override
    public StatutBean fromDto(@NotNull StatutDTO dto) throws BeanException {
        return from(dto);
    }

    @NotNull
    public StatutDTO toDto() throws BeanException {
        return to(this);
    }

    public static StatutBean safeFrom(@NotNull StatutDTO dto) {
        try {
            return from(dto);
        } catch (BeanException e) {
//            TODO FDA 2017/11 Trouver mieux comme code.
            throw new RuntimeException(e);
        }
    }

    public static StatutDTO safeTo(@NotNull StatutBean bean) {
        try {
            return to(bean);
        } catch (BeanException e) {
//            TODO FDA 2017/11 Trouver mieux comme code.
            throw new RuntimeException(e);
        }
    }


    // General

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (!getClass().equals(o.getClass()))) return false;

        StatutBean that = (StatutBean) o;

        return getCode().equals(that.getCode());
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }


    @Override
    public int compareTo(@NotNull StatutBean o) {
        return getCode().compareTo(o.getCode());
    }

    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        //noinspection HardcodedFileSeparator
        return "Statut"
                + " " + Objects.value(getCode(), "N/C");
    }

}
