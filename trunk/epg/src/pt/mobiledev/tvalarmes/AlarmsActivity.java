package pt.mobiledev.tvalarmes;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

public class AlarmsActivity extends Activity {

    ListView lvPrograms;
    Context context = AlarmsActivity.this;

    static String TAG = "TVAlarmes";

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        }
        // exibe ecrã de notificações...
    }
}
