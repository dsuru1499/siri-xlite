package siri_xlite.common;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

public interface DateTimeUtils {

    static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    static LocalDateTime toLocalDateTime(GregorianCalendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    static LocalDateTime toLocalDateTime(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    static GregorianCalendar toGregorianCalendar(LocalDateTime dateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(dateTime, ZoneId.systemDefault()));
    }

    static LocalTime toLocalTime(Date date) {
        return LocalTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    static Date toDate(LocalTime time) {
        Instant instant = time.atDate(LocalDate.of(1970, 1, 1)).atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    static Date fromXsdDateTime(String text) {
        return Date.from(fromLocalDateTime(text).atZone(ZoneId.systemDefault()).toInstant());
    }

    static LocalDateTime fromLocalDateTime(String text) {
        return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
    }

    static long toEpochMilli(Time time) {
        LocalTime localTime = time.toLocalTime();
        return LocalDateTime.now().withHour(localTime.getHour()).withMinute(localTime.getMinute())
                .withSecond(localTime.getSecond()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    static String toRFC1123(Date date) {
        ZonedDateTime time = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("GMT"));
        return DateTimeFormatter.RFC_1123_DATE_TIME.format(time);
    }

    static Date fromRFC1123(String text) {
        LocalDateTime dateTime = LocalDateTime.parse(text, DateTimeFormatter.RFC_1123_DATE_TIME);
        return Date.from(dateTime.atZone(ZoneId.of("GMT")).toInstant());
    }
}
