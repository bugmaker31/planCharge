package fr.gouv.agriculture.dal.ct.planCharge.util;

import javax.validation.constraints.Null;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
}
