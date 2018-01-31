package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by frederic.danna on 14/07/2017.
 */
@SuppressWarnings("FinalClass")
public final class Strings {

    private Strings() {
        super();
    }

    public static boolean estExpressionReguliere(@NotNull String str) {
        //noinspection UnusedCatchParameter
        try {
            //noinspection ResultOfMethodCallIgnored
            Pattern.compile(str);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    @Null
    public static String epure(@Null String s) {
        if (s == null) {
            return null;
        }
        String epuredString = s.trim();
        if (epuredString.isEmpty()) {
            //noinspection AssignmentToNull
            epuredString = null;
        }
        return epuredString;
    }

    public static boolean isEmpty(@Null String s) {
        return epure(s) == null;
    }


    // Cf. https://stackoverflow.com/questions/801935/formatting-file-sizes-in-java-jstl
    private static final long ONE_KILO = 1024L;

    public static String humanReadable(long number, @NotNull DecimalFormat decimalFormat) {
        double result;
        String suffix;
        CALCUL_FORMAT : {
            long absNumber = Math.abs(number);
            if (absNumber < ONE_KILO) {
                result = (double) number;
                suffix = "";
                break CALCUL_FORMAT;
            }
            if (absNumber < (ONE_KILO * ONE_KILO)) {
                result = (double) (number / ONE_KILO);
                suffix = "K";
                break CALCUL_FORMAT;
            }
            if (absNumber < (ONE_KILO * ONE_KILO * ONE_KILO)) {
                result = (double) (number / (ONE_KILO * ONE_KILO));
                suffix = "M";
                break CALCUL_FORMAT;
            }
            if (absNumber < (ONE_KILO * ONE_KILO * ONE_KILO * ONE_KILO)) {
                result = (double) (number / (ONE_KILO * ONE_KILO * ONE_KILO));
                suffix = "M";
                break CALCUL_FORMAT;
            }
            result = (double) (number / (ONE_KILO * ONE_KILO * ONE_KILO * ONE_KILO));
            suffix = "G";
        }
        //noinspection StringConcatenation,StringConcatenationMissingWhitespace
        return decimalFormat.format(result) + suffix;
    }
}
