package pt.mobiledev.tvalarmes;

import java.util.List;

import pt.mobiledev.tvalarmes.dao.DatabaseHandler;
import pt.mobiledev.tvalarmes.domain.Alarm;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")
public class AlarmsActivity extends Activity {

	ListView lvAlarms;
	Context context;
	List<Alarm> alarms;
	DatabaseHandler db;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_list);
		context = getApplicationContext();
		// Sacar a base de dados
		db = new DatabaseHandler(this);


	}

	@Override
	public void onResume() {
		super.onResume();
		List<Alarm> alarms = db.getAllAlarms();  
		// Listview de alarmes
		lvAlarms = (ListView) findViewById(R.id.lvAlarms);
		AlarmsBaseAdapater alarmsAdapter = new AlarmsBaseAdapater(context, alarms, db);
		lvAlarms.setAdapter(alarmsAdapter);

	}

	public void goToChannels(View view) {
		Intent intent = new Intent(this, ChannelsActivity.class);
		startActivity(intent);
	}
}
