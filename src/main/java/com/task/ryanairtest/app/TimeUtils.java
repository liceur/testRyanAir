package com.task.ryanairtest.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtils {

    private static final Logger logger = LogManager
            .getLogger(TimeUtils.class);

    private static final String ERROR_TO_CONVERT_STRING_TO_DATE = "Error to convert String to Date";
    private static final String PATTERN_DATE = "yyyy-MM-dd'T'HH:mm";

    public static String toISO8601UTC(Date date) {
        DateFormat df = new SimpleDateFormat(PATTERN_DATE);
        return df.format(date);
    }

    public static Date fromISO8601UTC(String dateStr) {
        DateFormat df = new SimpleDateFormat(PATTERN_DATE);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getYearOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static Integer getMonthOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static Integer getDayOfDate(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getDateFromScheduleString(Integer year, Integer month, Integer day, String hourMinute) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date d = null;
        try {
            d = df.parse(year + "-" + month + "-" + day + " " + hourMinute);
            Calendar gc = new GregorianCalendar();
            gc.setTime(d);
            return gc.getTime();
        } catch (ParseException e) {
            logger.error(ERROR_TO_CONVERT_STRING_TO_DATE);
            throw new ParseException(ERROR_TO_CONVERT_STRING_TO_DATE, e.getErrorOffset());
        }
    }


    public static Date addHours(Date date, Integer hours) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        LocalDateTime ldt = LocalDateTime.of(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
        );

        return ldt;
    }


}
