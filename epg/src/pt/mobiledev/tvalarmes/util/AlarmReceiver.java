package pt.mobiledev.tvalarmes.util;

import pt.mobiledev.tvalarmes.domain.Alarm;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// Corre uma notificação com o alarme sacado do intent
        Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
		Util.runNotification(context, alarm);
	}

}
