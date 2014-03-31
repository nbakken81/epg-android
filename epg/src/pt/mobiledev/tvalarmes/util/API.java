package pt.mobiledev.tvalarmes.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class API {

    public static String formatDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        String encondedDate = dateFormatter.format(date);
        return encondedDate.replace(" ", "+").replace(":", "%3A");
    }
}
