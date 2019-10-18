package siri_xlite.common;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeUtils {

    // public static DateTimeFormatter XSD_DATETIME = DateTimeFormatter.ofPattern("%Y-%M-%DT%h:%m:%s%z");

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(GregorianCalendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDateTime toLocalDateTime(long millis) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault());
    }

    public static GregorianCalendar toGregorianCalendar(LocalDateTime dateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(dateTime, ZoneId.systemDefault()));
    }

    public static GregorianCalendar toGregorianCalendar(String xsdDateTime) {
        return GregorianCalendar.from(ZonedDateTime.of(toLocalDateTime(xsdDateTime), ZoneId.systemDefault()));
    }

    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(String xsdDateTime) {
        return Date.from(toLocalDateTime(xsdDateTime).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(String xsdDateTime) {
        return LocalDateTime.parse(xsdDateTime, DateTimeFormatter.ISO_DATE_TIME);
    }

    public static long toEpochMilli(Time time) {
        LocalTime localTime = time.toLocalTime();
        return LocalDateTime.now().withHour(localTime.getHour()).withMinute(localTime.getMinute())
                .withSecond(localTime.getSecond()).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}
