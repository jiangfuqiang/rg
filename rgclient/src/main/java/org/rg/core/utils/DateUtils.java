package org.rg.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jiang on 17/1/18.
 */
public class DateUtils {

    private static final String shortDate = "yyyy-MM-dd";
    private static final String longDate = "yyyy-MM-dd HH:mm";

    public static String parseShortDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(shortDate);
        if(date == null) {
            return simpleDateFormat.format(new Date());
        }
        return simpleDateFormat.format(date);
    }

    public static String parseLongDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(longDate);
        if(date == null) {
            return simpleDateFormat.format(new Date());
        }
        return simpleDateFormat.format(date);
    }

    public static Date parseStrToLongDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(longDate);
        try {
            return simpleDateFormat.parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
