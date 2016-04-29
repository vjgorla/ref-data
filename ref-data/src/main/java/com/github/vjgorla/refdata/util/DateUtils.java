package com.github.vjgorla.refdata.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private DateUtils() {
    }
    
    public static boolean isAfterIgnoreTime(Date d1, Date d2) {
        return truncate(d1).after(truncate(d2));
    }
    
    public static Date truncate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    public static boolean isBetween(Date date, Date dateFrom, Date dateTo) {
        boolean between = true;
        if ((dateFrom != null && date.before(dateFrom))
                || (dateTo != null && date.after(dateTo))) {
            between = false;
        }
        return between;
    }
}
