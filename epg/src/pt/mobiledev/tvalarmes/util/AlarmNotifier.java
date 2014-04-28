package pt.mobiledev.tvalarmes.util;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import static java.util.Arrays.asList;
import java.util.Date;
import java.util.List;
import pt.mobiledev.tvalarmes.AlarmsActivity;
import pt.mobiledev.tvalarmes.R;
import pt.mobiledev.tvalarmes.dao.AlarmDao;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.domain.Program;

/**
 * @author diogomateus
 *
 */
public class AlarmNotifier {

    /**
     * Todos (para o background)
     *
     * @param context
     */
    public static void updateNotifications(Context context) {
        updateNotifications(context, null);
    }

    /**
     * Apenas atualiza as notificações de um canal (útil quando se mexe nesse
     * canal).
     */
    public static void updateNotifications(Context context, Channel channel) {
        AlarmDao alarmDao = new AlarmDao(context);
        List<Alarm> allAlarms = alarmDao.findAll();
        List<Channel> channels = channel == null ? alarmDao.getAllChannels() : asList(channel);
        List<Program> allPrograms = EPGDao.getAllPrograms(context, channels);
        findMatches(context, allAlarms, allPrograms);
    }

    /**
     * Agendamento da tarefa de actualização de alarmes em background
     */
    public static void backgroundTaskScheduler(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent backgroundIntent = new Intent(context, BackgroundTaskReceiver.class);
        PendingIntent backgroundPendingIntent = PendingIntent.getBroadcast(context, 1, backgroundIntent, 0); // criar Intent
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, AlarmManager.INTERVAL_HALF_DAY, AlarmManager.INTERVAL_HALF_DAY, backgroundPendingIntent);
    }

    static void findMatches(Context context, List<Alarm> allAlarms, List<Program> allPrograms) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE); // iniciar alarm Manager
        for (Program program : allPrograms) {
            for (Alarm alarm : allAlarms) {
                if (alarm.getProgram().equals(program)) {
                    Intent notificationIntent = new Intent(context, AlarmReceiver.class); // criar Intent
                    alarm.setProgram(program);
                    notificationIntent.putExtra("notification", alarm); // Adicionar alarme ao intent
                    PendingIntent alarmIntent = PendingIntent.getBroadcast(context, program.getId(), notificationIntent, 0);
                    // Agendamento
                    alarmManager.set(AlarmManager.RTC_WAKEUP, program.getStartDate().getTime() - 1000 * 60, alarmIntent);
                    System.out.println("VOU MARCAR O ALARME: " + alarm + " para as " + program.getStartDate());
                    // Fake Alarm
                    PendingIntent alarmIntent30 = PendingIntent.getBroadcast(context, program.getId() + 30, notificationIntent, 0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, new Date().getTime() + 50000, alarmIntent30);
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
            Alarm alarm = (Alarm) intent.getSerializableExtra("notification");
            List<Channel> channels = EPGDao.getChannels(context);
            for (Channel channel : channels) {
                if (channel.getId().equals(alarm.getProgram().getChannelId())) {
                    alarm.getProgram().setChannelName(channel.getName());
                    break;
                }
            }
            AlarmNotifier.runNotification(context, alarm);
        }
    }

    public static class BackgroundTaskReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotifications(context);
        }
    }

    public static void runNotification(Context context, Alarm alarm) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(alarm.getProgram().getTitle())
                .setContentText(alarm.getProgram().getChannelName() + "   "
                        + (alarm.getProgram().getSeason() > 0 ? "T" + alarm.getProgram().getSeason() + "  " : "")
                        + (alarm.getProgram().getEpisode() > 0 ? "E" + alarm.getProgram().getEpisode() : ""))
                .setAutoCancel(true);
        int smallIcon = Channel.getLogoResourceId(context, new Channel(alarm.getProgram().getChannelId()));
        if (smallIcon == 0) {
            smallIcon = R.drawable.tv;
        }
        mBuilder.setSmallIcon(smallIcon);
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
