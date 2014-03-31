package pt.mobiledev.tvalarmes.view;

import pt.mobiledev.tvalarmes.domain.Program;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.R;

public class MainActivity extends Activity {

    ListView lvPrograms;
    Context context = MainActivity.this;

    static String TAG = "TVAlarmes";

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        }

        ArrayList<Program> programs = EPGDao.getPrograms("RTP2");

        lvPrograms = (ListView) findViewById(R.id.lvPrograms);
        lvPrograms.setAdapter(new ProgramsBaseAdapter(context, programs));

        for (Program program : programs) {
            Log.v(TAG, (program.getTitle()));
        }
    }
}
