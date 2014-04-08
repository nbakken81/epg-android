package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class AlarmsActivity extends Activity {

    ListView lvPrograms;
    Context context = AlarmsActivity.this;

    static String TAG = "TVAlarmes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

    }
    
    public void goToChannels(View view) {
    	Intent intent = new Intent(this, ChannelsActivity.class);
        startActivity(intent);
    }
}
