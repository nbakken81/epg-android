package pt.mobiledev.tvalarmes.util;

import android.content.Context;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static Date addHours(int hours) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, hours);
        Date endDate = cal.getTime();
        return endDate;
    }

    public static Date subtractHours(int hours) {
        return addHours(-hours);
    }

    public static int getResourceId(Context context, String packageName, String resourceName) {
        int checkExistence = context.getResources()
                .getIdentifier(resourceName, packageName, context.getPackageName());
        return checkExistence;
    }

    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static boolean relaxedStartsWith(String s1, String s2) {
        return removeDiacriticalMarks(s1.trim().toLowerCase()).startsWith(removeDiacriticalMarks(s2.trim().toLowerCase()));
    }

    public static boolean relaxedContains(String s1, String s2) {
        return removeDiacriticalMarks(s1.trim().toLowerCase()).contains(removeDiacriticalMarks(s2.trim().toLowerCase()));
    }
}
