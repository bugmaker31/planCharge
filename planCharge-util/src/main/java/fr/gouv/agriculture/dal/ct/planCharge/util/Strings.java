package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.NotNull;
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
}
