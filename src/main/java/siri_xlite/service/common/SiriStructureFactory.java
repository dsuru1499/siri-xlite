package siri_xlite.service.common;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class SiriStructureFactory {

    public static String createXMLGregorianCalendar() {
        return createXMLGregorianCalendar(LocalDateTime.now());
    }

    public static String createXMLGregorianCalendar(long epochMilli) {
        LocalDateTime dateTime = Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return createXMLGregorianCalendar(dateTime);
    }

    public static String createXMLGregorianCalendar(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String createParticipantRef(String producerDomain, String producerName) {
        return producerDomain + ":" + producerName;
    }

    public static String createMessageIdentifier() {
        return UUID.randomUUID().toString();
    }

    public static String createDuration(long delay) {
        return Duration.ofMillis(delay).toString();
    }

}
