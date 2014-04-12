package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Program;

public class ProgramsActivity extends Activity {

    ListView lvPrograms;
    Context context = ProgramsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_list);

        String sigla = getIntent().getExtras().getString("sigla");
        List<Program> programs = EPGDao.getPrograms(context, sigla);

        lvPrograms = (ListView) findViewById(R.id.lvPrograms);
        lvPrograms.setAdapter(new ProgramsBaseAdapter(context, programs));

        for (Program program : programs) {
            Log.v(this.getCallingPackage(), (program.getTitle()));
        }
    }
}
