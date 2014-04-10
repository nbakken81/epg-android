package pt.mobiledev.tvalarmes.util;

import android.content.Context;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static Date addDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        Date endDate = cal.getTime();
        return endDate;
    }

    public static Date subtractDays(int days) {
        return addDays(-days);
    }

    public static int getResourceId(Context context, String packageName, String resourceName) {
        int checkExistence = context.getResources()
                .getIdentifier(resourceName, packageName, context.getPackageName());
        return checkExistence;
    }
}
