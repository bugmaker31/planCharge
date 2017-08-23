package fr.gouv.agriculture.dal.ct.planCharge.util.number;

import javax.validation.constraints.NotNull;

/**
 * A {@link Number number} whose value is between 0 and 100, included.
 */
public class Percentage extends Number {


    // Fields:

    @NotNull
    private Float percentage;


    // Constructors:

    /**
     *
     * @param max
     * @param value Must be lower or equal to {@code max} ({@code assert}'ed).
     */
    public Percentage(float max, float value) {
        super();

        assert max > 0;
        assert value >= 0;

        assert max >= value;

        percentage = (value * 100) / max;
    }

    /**
     *
     * @param value Must be between 0 and 100, included ({@code assert}'ed).
     */
    public Percentage(float value) {
        super();
        assert value >= 0;
        assert value <= 100;
        percentage = value;
    }


    // Getters/Setters:


    // Methods:

    /**
     * @see Float#parseFloat(String)
     */
    @NotNull
    public static Percentage parse(@NotNull String string) {
        float v = Float.parseFloat(string);
        return new Percentage(v);
    }

    // Implementation of Number / Delegation to Float#percentage:

    @Override
    public int intValue() {
        return percentage.intValue();
    }

    @Override
    public long longValue() {
        return percentage.longValue();
    }

    @Override
    public float floatValue() {
        //noinspection UnnecessaryUnboxing
        return percentage.floatValue();
    }

    @Override
    public double doubleValue() {
        return percentage.doubleValue();
    }


    // Facilities methods (only for developement/debugging ease):

    @Override
    public String toString() {
        return "Percentage{" + percentage + '}';
    }

}
