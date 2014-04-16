package pt.mobiledev.tvalarmes.util;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Calendar;
import java.util.Date;
import pt.mobiledev.tvalarmes.AlarmsActivity;
import pt.mobiledev.tvalarmes.R;
import pt.mobiledev.tvalarmes.domain.Alarm;

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

    public static boolean relaxedStartsWith(String s1, String s2) {
        return removeDiacriticalMarks(s1.trim().toLowerCase()).startsWith(removeDiacriticalMarks(s2.trim().toLowerCase()));
    }

    public static boolean relaxedContains(String s1, String s2) {
        return removeDiacriticalMarks(s1.trim().toLowerCase()).contains(removeDiacriticalMarks(s2.trim().toLowerCase()));
    }

    public static void scheduleAlarm(Context context, Alarm alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // iniciar alarm Manager
        Intent intent = new Intent(context, AlarmReceiver.class); // criar Intent
        intent.putExtra("alarm", alarm); // Adicionar alarme ao intent
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        // Agendar alarme
        long milliseconds = alarm.getProgram().getStartDate().getTime();
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, milliseconds, alarmIntent);
    }

    public static class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Corre uma notifição com o alarme sacado do intent
            Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
            Util.runNotification(context, alarm);
        }
    }
    
    public static class BackgroundTaskReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Sacar a lista de todos os alarmes
        	// Sacar a programação dos canais com alarmes
        	// Confirmar se existe algum alarme a adicionar
        	// Para correr cenas em background na app usa-se o asynctask, mas acho neste caso não é necessário.
        }
    }


    public static void runNotification(Context context, Alarm alarm) {
        String alarmText;
        switch (alarm.getMinutesBefore()) {
            case 0:
                alarmText = "Está a começar.";
                break;
            case 1:
                alarmText = "Começa dentro de 1 minuto.";
                break;
            default:
                alarmText = "Começa dentro de " + alarm.getMinutesBefore() + " minutos.";
                break;
        }
        NotificationCompat.Builder mBuilder
                = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.axn)
                .setContentTitle(alarm.getProgram().getTitle())
                .setContentText(alarmText);
        Intent resultIntent = new Intent(context, AlarmsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AlarmsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
