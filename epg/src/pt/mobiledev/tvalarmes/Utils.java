package pt.mobiledev.tvalarmes;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class Utils {

    final static String BASE_URL = "http://services.sapo.pt/EPG/";
    final static String GET_CHANNEL_FUNCTION = "GetChannelByDateInterval";
    final static int daysInterval = 1;
    final static String TAG = "TVAlarmes";

    /* URL Methods */
    public static String getProgramsURL(String channel) {
        return BASE_URL + GET_CHANNEL_FUNCTION + "?channelSigla=" + channel + "&startDate=" + getStartDate() + "&endDate=" + getEndDate();
    }

    /* Date Methods */
    public static String getStartDate() {
        /* Get current date minus "timeInterval" days */
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysInterval);
        Date startDate = cal.getTime();
        return encodeDate(startDate);
    }

    public static String getEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +daysInterval);
        Date endDate = cal.getTime();
        return encodeDate(endDate);
    }

    public static String encodeDate(Date date) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        String encondedDate = dateFormatter.format(date);
        encondedDate = encondedDate.replace(" ", "+");
        encondedDate = encondedDate.replace(":", "%3A");
        Log.v(TAG, encondedDate);
        return encondedDate;
    }
}
