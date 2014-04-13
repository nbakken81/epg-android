package pt.mobiledev.tvalarmes.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import pt.mobiledev.tvalarmes.domain.Alarm;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Corre uma notifição com o alarme sacado do intent
        Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
        Util.runNotification(context, alarm);
    }
}
