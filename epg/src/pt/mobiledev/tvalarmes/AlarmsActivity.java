package pt.mobiledev.tvalarmes;

import java.util.List;

import pt.mobiledev.tvalarmes.dao.AlarmDao;
import pt.mobiledev.tvalarmes.domain.Alarm;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AlarmsActivity extends Activity {

    ListView lvAlarms;
    Context context;
    List<Alarm> alarms;
    AlarmDao db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list);

        context = getApplicationContext();        
        db = new AlarmDao(this); // Sacar a base de dados
    }

    @Override
    public void onResume() {
        super.onResume();
        lvAlarms = (ListView) findViewById(R.id.lvAlarms); // Listview de alarmes
        AlarmsBaseAdapter alarmsAdapter = new AlarmsBaseAdapter(context, db.findAll(), db);
        lvAlarms.setAdapter(alarmsAdapter);
    }

    public void goToChannels(View view) {
        Intent intent = new Intent(this, ChannelsActivity.class);
        startActivity(intent);
    }
}
