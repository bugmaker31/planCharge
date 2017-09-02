package fr.gouv.agriculture.dal.ct.planCharge.ihm.view;

import javafx.util.StringConverter;
import org.slf4j.Logger;

import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.text.ParseException;

import static org.slf4j.LoggerFactory.getLogger;

public class Converters {

    @SuppressWarnings("OverlyComplexAnonymousInnerClass")
    public static final StringConverter<Float> HUITIEMES_JOURS_STRING_CONVERTER = new StringConverter<Float>() {

        @SuppressWarnings("InstanceVariableNamingConvention")
        private /*static*/ final Logger LOGGER = getLogger(Converters.class);

        @SuppressWarnings({"InstanceVariableNamingConvention", "NonConstantFieldWithUpperCaseName"})
        private /*static*/ final DecimalFormat FORMATEUR = new DecimalFormat("#.###");

        @Null
        @Override
        public String toString(Float f) {
            if (f == null) {
                return null;
            }
            if (f == 0) {
                return "-";
            }
            return FORMATEUR.format(f);
        }

        @Null
        @Override
        public Float fromString(String s) {
            if (s == null) {
                return null;
            }
            if (s.equals("-")) {
                return 0f;
            }
            try {
                return FORMATEUR.parse(s).floatValue();
            } catch (ParseException e) {
                LOGGER.error("Impossible de décoder une valeur décimale dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
                return null;
            }
        }
    };

    @SuppressWarnings("OverlyComplexAnonymousInnerClass")
    public static final StringConverter<Integer> NBRS_JOURS_STRING_CONVERTER = new StringConverter<Integer>() {

        @SuppressWarnings("InstanceVariableNamingConvention")
        private /*static*/ final Logger LOGGER = getLogger(Converters.class);

        @SuppressWarnings("InstanceVariableNamingConvention")
        private /*static*/ final DecimalFormat FORMATEUR = new DecimalFormat("#");

        @Null
        @Override
        public String toString(Integer anInt) {
            if (anInt == null) {
                return null;
            }
            if (anInt.equals(0)) {
                return "-";
            }
            return FORMATEUR.format(anInt);
        }

        @Null
        @Override
        public Integer fromString(String s) {
            if (s == null) {
                return null;
            }
            if (s.equals("-")) {
                return 0;
            }
            try {
                return FORMATEUR.parse(s).intValue();
            } catch (ParseException e) {
                LOGGER.error("Impossible de décoder une valeur décimale dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
                return null;
            }
        }
    };

}