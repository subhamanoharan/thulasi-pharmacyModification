package com.cryptic.imed.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author sharafat
 */
public class DateUtils {
    public static final String SQLITE_DATE_FORMAT = "yyyy-MM-dd";

    public static String getSqliteFormattedDate(DateWithoutTime dateWithoutTime) {
        return dateWithoutTime.format(SQLITE_DATE_FORMAT).toString();
    }

    public static Date parseSqliteFormattedDateTime(String sqliteFormattedDateTime) throws IllegalArgumentException {
        try {
            return DateFormat.getInstance().parse(sqliteFormattedDateTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unrecognized date-time format: " + sqliteFormattedDateTime);
        }
    }
}
