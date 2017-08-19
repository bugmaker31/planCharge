package fr.gouv.agriculture.dal.ct.ihm.view;

import fr.gouv.agriculture.dal.ct.planCharge.util.number.Percentage;
import javafx.util.StringConverter;

import javax.validation.constraints.Null;
import java.text.DecimalFormat;

public class PercentageStringConverter extends StringConverter<Percentage> {

    private static final DecimalFormat FORMAT = new DecimalFormat("##0");
    private static final javafx.util.converter.PercentageStringConverter PSC = new javafx.util.converter.PercentageStringConverter(FORMAT);

    @Null
    @Override
    public String toString(@Null Percentage pc) {
        if (pc == null) {
            return null;
        }
        return PSC.toString(pc.floatValue());
    }

    @Null
    @Override
    public Percentage fromString(@Null String string) {
        if (string == null) {
            return null;
        }
        return Percentage.parse(string);
    }
}
