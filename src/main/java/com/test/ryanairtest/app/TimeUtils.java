package com.test.ryanairtest.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
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

    public static String toISO8601UTC(Date date) {
        //TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        //df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date fromISO8601UTC(String dateStr) {
        //TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        //df.setTimeZone(tz);

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
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE)
        );

        return ldt;
    }


}
