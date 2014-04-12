package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AlarmsActivity extends Activity {

    ListView lvAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list);
    }

    public void goToChannels(View view) {
        Intent intent = new Intent(this, ChannelsActivity.class);
        startActivity(intent);
    }
}
