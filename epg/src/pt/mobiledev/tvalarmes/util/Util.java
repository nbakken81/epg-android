package pt.mobiledev.tvalarmes.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Calendar;
import java.util.Date;

import pt.mobiledev.tvalarmes.AlarmsActivity;
import pt.mobiledev.tvalarmes.R;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.domain.Program;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

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
    
    public static String removeDiacriticalMarks(String string) {
        return Normalizer.normalize(string, Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    public static void createAlarm(Context context, Program program, int minutesBefore) {
    	AlarmManager alarmManager;
    	PendingIntent alarmIntent;
		// Criar alarme
		Alarm alarm = (Alarm) program;
		alarm.setMinutesBefore(minutesBefore);
    	// Iniciar Alarm Manager
    	alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    	//Criar Intent
		Intent intent = new Intent(context, AlarmReceiver.class);
		// Adicionar alarme ao intent
		intent.putExtra("alarm", alarm);
		
		alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		// Tocar o alarme dois segundos depois de ser criado
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		        SystemClock.elapsedRealtime() +
		        2 * 1000, alarmIntent);
    }
    
    public static void runNotification(Context context, Alarm alarm) {
    	String alarmText;
    	switch (alarm.getMinutesBefore()) {
		case 0:
			alarmText = "Está a começar.";
			break;
		case 1:
			alarmText = "Começa dentro de 1 minuto.";
		default:
			alarmText = "Começa dentro de " + alarm.getMinutesBefore() + " minutos.";
			break;
		}
    	
    	NotificationCompat.Builder mBuilder =
    	        new NotificationCompat.Builder(context)
    	        .setSmallIcon(R.drawable.axn)
    	        .setContentTitle(alarm.getTitle() )
    	        .setContentText(alarmText);
    	Intent resultIntent = new Intent(context, AlarmsActivity.class);
    	TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
    	stackBuilder.addParentStack(AlarmsActivity.class);
    	stackBuilder.addNextIntent(resultIntent);
    	PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    	mBuilder.setContentIntent(resultPendingIntent);
    	NotificationManager mNotificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.notify(1, mBuilder.build());
    }
}
