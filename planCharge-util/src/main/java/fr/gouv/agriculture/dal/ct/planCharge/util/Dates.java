package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.*;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * Created by frederic.danna on 28/04/2017.
 */
public abstract class Dates {

    // Cf. http://stackoverflow.com/questions/22929237/convert-java-time-localdate-into-java-util-date-type

    @Null
    public static Date asDate(@Null LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Null
    public static Date asDate(@Null LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Null
    public static LocalDate asLocalDate(@Null Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Null
    public static LocalDateTime asLocalDateTime(@Null Date date) {
        if (date == null) {
            return null;
        }
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static boolean isWeekEnd(@NotNull LocalDate jour) {
        return (jour.getDayOfWeek() == DayOfWeek.SATURDAY) || (jour.getDayOfWeek() == DayOfWeek.SUNDAY);
    }

    public static int weekNumber(@NotNull LocalDate date) {
        // Cf . https://stackoverflow.com/questions/26012434/get-week-number-of-localdate-java-8
        TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int weekNumber = date.get(woy);
        return weekNumber;
    }

    @NotNull
    public static LocalDate max(@NotNull LocalDate date1, @NotNull LocalDate date2) {
        return (date1.compareTo(date2) > 0) ? date1 : date2;
    }

    @NotNull
    public static LocalDate min(@NotNull LocalDate date1, @NotNull LocalDate date2) {
        return (date1.compareTo(date2) < 0) ? date1 : date2;
    }


    @NotNull
    public static LocalDate max(@NotNull Set<LocalDate> dates) {
        if (dates.size() == 1) {
            return dates.iterator().next();
        } else {
            LocalDate firstDate = dates.iterator().next();
            dates.remove(firstDate);
            return max(firstDate, max(dates));
        }
    }
}
