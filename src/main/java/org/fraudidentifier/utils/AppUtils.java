package org.fraudidentifier.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class AppUtils {

    private AppUtils() {}

    /**
     * Accept a string representation of a date in ISO format
     * and parses to {@link LocalDateTime}.
     *
     * @param dateStr - ISO datetime YYYY-MM-ddTHH:mm:ss
     * @return A parsed {@link LocalDateTime}, or exception
     * @throws DateTimeParseException - this exception will be thrown if the date format is invalid
     */
    public static LocalDateTime toIsoDateTime(String dateStr)
            throws DateTimeParseException {
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
