package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.AlarmDao;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.util.AlarmNotifier;

public class AlarmsActivity extends Activity {

    ListView lvAlarms;
    Context context;
    List<Alarm> alarms;
    AlarmDao alarmDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list);
        context = getApplicationContext();
        alarmDao = new AlarmDao(this); // Sacar a base de dados
        AlarmNotifier.backgroundTaskScheduler(this); // Agendamento da tarefa de actualização de alarmes em background
    }

    @Override
    public void onResume() {
        super.onResume();
        lvAlarms = (ListView) findViewById(R.id.lvAlarms); // Listview de alarmes
        AlarmsBaseAdapter alarmsAdapter = new AlarmsBaseAdapter(context, R.layout.alarm_detail, alarmDao.findAll(), alarmDao);
        lvAlarms.setAdapter(alarmsAdapter);
        Button btnGoToChannels = (Button) findViewById(R.id.btnGoToChannels);
        if (alarmsAdapter.getCount() == 0) {
            btnGoToChannels.getLayoutParams().height = 90;
            btnGoToChannels.getLayoutParams().width = 90;
        } else {
            btnGoToChannels.getLayoutParams().height = 35;
            btnGoToChannels.getLayoutParams().width = 35;
        }
        lvAlarms.invalidateViews();
    }

    public void goToChannels(View view) {
        Intent intent = new Intent(this, ChannelsActivity.class);
        startActivity(intent);
    }
}
