package pt.mobiledev.tvalarmes.util;

import java.util.Calendar;
import java.util.Date;

public class Dates {

    public static Date subtractDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -days);
        Date startDate = cal.getTime();
        return startDate;
    }

    public static Date addDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +days);
        Date endDate = cal.getTime();
        return endDate;
    }
}
