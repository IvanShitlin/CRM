package com.foxminded.hipsterfox.service.amocrm.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AmocrmTimeConverter {

    public static final String AMOCRM_DATE_TIME_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";

    public static ZonedDateTime toZoneDateTime(String amocrmDateTime) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(amocrmDateTime) * 1000), ZoneId.of("UTC"));
    }

    public static String toAmocrmRequestFormat(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AMOCRM_DATE_TIME_FORMAT);
        return dateTime.withZoneSameInstant(ZoneId.of("UTC")).format(formatter);
    }
}
