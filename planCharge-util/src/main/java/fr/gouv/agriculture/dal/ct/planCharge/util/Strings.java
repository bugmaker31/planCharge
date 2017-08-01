package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.text.DecimalFormat;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by frederic.danna on 14/07/2017.
 */
public class Strings {

    public static boolean estExpressionReguliere(@NotNull String string) {
        //noinspection UnusedCatchParameter
        try {
            //noinspection ResultOfMethodCallIgnored
            Pattern.compile(string);
            return true;
        } catch (PatternSyntaxException e) {
            return false;
        }
    }

    // Cf. https://stackoverflow.com/questions/801935/formatting-file-sizes-in-java-jstl
    private static final int ONE_KILO = 1024;

    @Null
    public static String epure(@Null String s) {
        if (s == null) {
            return null;
        }
        String ns = s.trim();
        if (ns.isEmpty()) {
            ns = null;
        }
        return ns;
    }

    public static boolean isEmpty(@Null String s) {
        return epure(s) == null;
    }

    public static String humanReadable(long number, @NotNull DecimalFormat decimalFormat) {
        long absNumber = Math.abs(number);
        double result;
        String suffix;
        //noinspection IfStatementWithTooManyBranches
        if (absNumber < ONE_KILO) {
            result = number;
            suffix = "";
        } else if (absNumber < (ONE_KILO * ONE_KILO)) {
            result = number / ONE_KILO;
            suffix = "K";
        } else if (absNumber < (ONE_KILO * ONE_KILO * ONE_KILO)) {
            result = number / (ONE_KILO * ONE_KILO);
            suffix = "M";
        } else if (absNumber < (ONE_KILO * ONE_KILO * ONE_KILO * ONE_KILO)) {
            result = number / (ONE_KILO * ONE_KILO * ONE_KILO);
            suffix = "M";
        } else {
            result = number / (ONE_KILO * ONE_KILO * ONE_KILO * ONE_KILO);
            suffix = "G";
        }
        return decimalFormat.format(result) + suffix;
    }
}
