package pt.mobiledev.tvalarmes.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import java.util.List;
import pt.mobiledev.tvalarmes.AlarmsActivity;
import pt.mobiledev.tvalarmes.dao.AlarmDao;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.domain.Program;

public class AlarmNotifier {

    public static void updateNotifications(Context context) {
        AlarmDao alarmDao = new AlarmDao(context);
        List<Alarm> allAlarms = alarmDao.findAll();
        List<Program> allPrograms = EPGDao.getAllPrograms(context, alarmDao.getAllChannels());
        findMatches(allAlarms, allPrograms);

//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // iniciar alarm Manager
//        Intent intent = new Intent(context, AlarmReceiver.class); // criar Intent
//        intent.putExtra("alarm", alarm); // Adicionar alarme ao intent
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        long milliseconds = alarm.getProgram().getStartDate().getTime();  // Agendar alarme
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 2000, alarmIntent); // teste TODO apagar
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, milliseconds, alarmIntent);
    }

    static void findMatches(List<Alarm> allAlarms, List<Program> allPrograms) {
        for(Program program : allPrograms) {
            for(Alarm alarm : allAlarms) {
                if (alarm.getProgram().equals(program)) {
                    
                }
            }
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {

        /**
         * Corre uma notifição com o alarme sacado do intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Alarm alarm = (Alarm) intent.getSerializableExtra("alarm");
            AlarmNotifier.runNotification(context, alarm);
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
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(Channel.getLogoResourceId(context, new Channel(alarm.getProgram().getChannelId())))
                .setContentTitle(alarm.getProgram().getTitle())
                .setContentText(alarm.getProgram().getId() + "")
                .setAutoCancel(true);
        Intent resultIntent = new Intent(context, AlarmsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AlarmsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(alarm.getProgram().getId(), mBuilder.build());
    }
}
