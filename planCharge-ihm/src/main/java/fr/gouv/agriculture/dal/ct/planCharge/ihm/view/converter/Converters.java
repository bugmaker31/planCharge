package fr.gouv.agriculture.dal.ct.planCharge.ihm.view.converter;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.StatutBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import javafx.util.StringConverter;
import org.slf4j.Logger;

import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

public class Converters {

    private static final Pattern DECIMAL_SYMBOL = Pattern.compile("\\.");

    public static final FractionJoursStringConverter FRACTION_JOURS_STRING_CONVERTER = new FractionJoursStringConverter();
    public static final SommeFractionJoursStringConverter SOMME_FRACTION_JOURS_STRING_CONVERTER = new SommeFractionJoursStringConverter();
    public static final NbrsJoursStringConverter NBRS_JOURS_STRING_CONVERTER = new NbrsJoursStringConverter();
    public static final ChargeStringConverter CHARGE_STRING_CONVERTER = new ChargeStringConverter();
    public static final SommeChargeStringConverter SOMME_CHARGE_STRING_CONVERTER = new SommeChargeStringConverter();
    public static final RessourceBeanConverter RESSOURCE_BEAN_CONVERTER = new RessourceBeanConverter();
    public static final ProfilBeanConverter PROFIL_BEAN_CONVERTER = new ProfilBeanConverter();
    public static final ProjetAppliBeanConverter PROJET_APPLI_STRING_CONVERTER = new ProjetAppliBeanConverter();
    public static final StatutBeanConverter STATUT_STRING_CONVERTER = new StatutBeanConverter();


    private static class FractionJoursStringConverter extends StringConverter<Float> {

        @SuppressWarnings({"InstanceVariableNamingConvention", "NonConstantFieldWithUpperCaseName"})
        private /*static*/ final Logger LOGGER = getLogger(FractionJoursStringConverter.class);

        @SuppressWarnings({"InstanceVariableNamingConvention", "NonConstantFieldWithUpperCaseName"})
        private /*static*/ final DecimalFormat FORMATEUR = new DecimalFormat("#.###");

        /*static*/ {
            DecimalFormatSymbols formatSymbols = DecimalFormatSymbols.getInstance();
            formatSymbols.setDecimalSeparator(',');
            FORMATEUR.setDecimalFormatSymbols(formatSymbols);
        }

        @Null
        @Override
        public String toString(Float f) {
            if (f == null) {
                return null;
            }
            if (f == 0.0) {
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
                return 0.0f;
            }
            try {
                Matcher matcher = DECIMAL_SYMBOL.matcher(s);
                String str = matcher.replaceAll(FORMATEUR.getDecimalFormatSymbols().getDecimalSeparator() + "");
                return FORMATEUR.parse(str).floatValue();
            } catch (ParseException e) {
                LOGGER.error("Impossible de décoder une valeur décimale dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
                return null;
            }
        }
    }

    private static class SommeFractionJoursStringConverter extends StringConverter<Float> {

        @SuppressWarnings({"InstanceVariableNamingConvention", "NonConstantFieldWithUpperCaseName"})
        private /*static*/ final Logger LOGGER = getLogger(SommeFractionJoursStringConverter.class);

        @SuppressWarnings({"InstanceVariableNamingConvention", "NonConstantFieldWithUpperCaseName"})
        private /*static*/ final DecimalFormat FORMATEUR = new DecimalFormat("#");

        @Null
        @Override
        public String toString(Float f) {
            if (f == null) {
                return null;
            }
            if (f == 0.0) {
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
                return 0.0f;
            }
            try {
                return FORMATEUR.parse(s).floatValue();
            } catch (ParseException e) {
                LOGGER.error("Impossible de décoder une valeur décimale dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
                return null;
            }
        }
    }

    private static class ChargeStringConverter extends StringConverter<Double> {

        @SuppressWarnings("InstanceVariableNamingConvention")
        private /*static*/ final Logger LOGGER = getLogger(ChargeStringConverter.class);

        @SuppressWarnings({"InstanceVariableNamingConvention", "NonConstantFieldWithUpperCaseName"})
        private /*static*/ final DecimalFormat FORMATEUR = new DecimalFormat("#.###");

        {
            DecimalFormatSymbols formatSymbols = DecimalFormatSymbols.getInstance();
            formatSymbols.setDecimalSeparator(',');
            FORMATEUR.setDecimalFormatSymbols(formatSymbols);
        }

        @Null
        @Override
        public String toString(Double d) {
            if (d == null) {
                return null;
            }
            if (d == 0.0) {
                return "-";
            }
            return FORMATEUR.format(d);
        }

        @Null
        @Override
        public Double fromString(String s) {
            if ((s == null) || s.isEmpty()) {
                return null;
            }
            if (s.equals("-")) {
                return 0.0;
            }
            try {
                Matcher matcher = DECIMAL_SYMBOL.matcher(s);
                String str = matcher.replaceAll(FORMATEUR.getDecimalFormatSymbols().getDecimalSeparator() + "");
                return FORMATEUR.parse(str).doubleValue();
            } catch (ParseException e) {
                LOGGER.error("Impossible de décoder une valeur décimale dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
                return null;
            }
        }
    }

    private static class SommeChargeStringConverter extends StringConverter<Double> {

        @SuppressWarnings("InstanceVariableNamingConvention")
        private /*static*/ final Logger LOGGER = getLogger(SommeChargeStringConverter.class);

        @SuppressWarnings({"InstanceVariableNamingConvention", "NonConstantFieldWithUpperCaseName"})
        private /*static*/ final DecimalFormat FORMATEUR = new DecimalFormat("#");

        @Null
        @Override
        public String toString(Double d) {
            if (d == null) {
                return null;
            }
            if (d == 0.0) {
                return "-";
            }
            return FORMATEUR.format(d);
        }

        @Null
        @Override
        public Double fromString(String s) {
            if ((s == null) || s.isEmpty()) {
                return null;
            }
            if (s.equals("-")) {
                return 0.0;
            }
            try {
                return FORMATEUR.parse(s).doubleValue();
            } catch (ParseException e) {
                LOGGER.error("Impossible de décoder une valeur décimale dans la chaîne '" + s + "'.", e); // TODO FDA 2017/08 Trouver mieux que de loguer une erreur.
                return null;
            }
        }
    }

    private static class NbrsJoursStringConverter extends StringConverter<Integer> {

        @SuppressWarnings("InstanceVariableNamingConvention")
        private /*static*/ final Logger LOGGER = getLogger(NbrsJoursStringConverter.class);

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
    }
}