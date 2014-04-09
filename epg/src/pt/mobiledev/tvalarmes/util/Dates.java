package pt.mobiledev.tvalarmes.util;

import java.util.Calendar;
import java.util.Date;

public class Dates {

    public static Date addDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        Date endDate = cal.getTime();
        return endDate;
    }

    public static Date subtractDays(int days) {
        return addDays(-days);
    }
}
