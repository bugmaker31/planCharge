package fr.gouv.agriculture.dal.ct.planCharge.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.RegleGestion;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels.RGRefJourFerieDateObligatoire;
import fr.gouv.agriculture.dal.ct.planCharge.metier.regleGestion.referentiels.RGRefJourFerieUniciteJour;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class JourFerieDTO extends AbstractDTO<JourFerie, LocalDate, JourFerieDTO> implements Comparable<JourFerieDTO> {


    private static class DateJourFerieComparator implements Comparator<JourFerieDTO> {

        @Override
        public int compare(@NotNull JourFerieDTO jf1, @NotNull JourFerieDTO jf2) {
            if ((jf1.getDate() == null) && (jf2.getDate() == null)) {
                return 0;
            }
            if ((jf1.getDate() == null) && (jf2.getDate() != null)) {
                return 1;
            }
            if ((jf1.getDate() != null) && (jf2.getDate() == null)) {
                return -1;
            }
            return jf1.getDate().compareTo(jf2.getDate());
        }

    }

    @SuppressWarnings("WeakerAccess")
    public static final DateJourFerieComparator COMPARATOR_DATE = new DateJourFerieComparator();

    @SuppressWarnings("WeakerAccess")
    public static final Comparator<JourFerieDTO> COMPARATOR_DEFAUT = COMPARATOR_DATE;


    @Null
    private LocalDate date;
    @Null
    private String description;


    private JourFerieDTO() {
        super();
    }

    public JourFerieDTO(@Null LocalDate date, @Null String description) {
        this();
        this.date = date;
        this.description = description;
    }


    @Null
    public LocalDate getDate() {
        return date;
    }

    @Null
    public String getDescription() {
        return description;
    }


    @SuppressWarnings("SuspiciousGetterSetter")
    @Null
    @Override
    public LocalDate getIdentity() {
        return date;
    }

    @NotNull
    public static JourFerieDTO from(@NotNull JourFerie entity) {
        return new JourFerieDTO().fromEntity(entity);
    }

    @Override
    public int compareTo(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull JourFerieDTO other) {
        return COMPARATOR_DEFAUT.compare(this, other);
    }

    @NotNull
    @Override
    public JourFerieDTO fromEntity(@NotNull JourFerie entity) {
        return new JourFerieDTO(entity.getDate(), entity.getDescription());
    }

    @NotNull
    @Override
    public JourFerie toEntity() {
        assert date != null : RGRefJourFerieDateObligatoire.INSTANCE.getCode() + " non respectée";
        return new JourFerie(date, description);
    }

    @NotNull
    @Override
    protected List<RegleGestion<JourFerieDTO>> getReglesGestion() {
        return Arrays.asList(
                RGRefJourFerieDateObligatoire.INSTANCE,
                RGRefJourFerieUniciteJour.INSTANCE
        );
    }


    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return (date == null ? "N/C" : date.format(DateTimeFormatter.ISO_DATE))
                + (" " + (description == null ? "N/C" : ('"' + description + '"')));
    }
}
