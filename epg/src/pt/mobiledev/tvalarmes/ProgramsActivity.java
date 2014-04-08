package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;
import java.util.ArrayList;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Program;

public class ProgramsActivity extends Activity {

    ListView lvPrograms;
    Context context = ProgramsActivity.this;

    static String TAG = "TVAlarmes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programs);
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