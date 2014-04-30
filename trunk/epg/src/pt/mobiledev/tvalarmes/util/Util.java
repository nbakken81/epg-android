package pt.mobiledev.tvalarmes.util;

import android.content.Context;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static Date addHours(int hours) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, hours);
        return cal.getTime();
    }

    public static Date subtractHours(int hours) {
        return addHours(-hours);
    }

    public static int getResourceId(Context context, String packageName, String resourceName) {
        int checkExistence = context.getResources()
                .getIdentifier(resourceName, packageName, context.getPackageName());
        return checkExistence;
    }
}
