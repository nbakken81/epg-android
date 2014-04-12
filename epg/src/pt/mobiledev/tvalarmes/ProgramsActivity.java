package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Program;
import pt.mobiledev.tvalarmes.util.Util;

public class ProgramsActivity extends Activity {

    ListView lvPrograms;
    Context context = ProgramsActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_list);

        String sigla = getIntent().getExtras().getString("sigla");
        final List<Program> programs = EPGDao.getPrograms(context, sigla);

        lvPrograms = (ListView) findViewById(R.id.lvPrograms);
        final ProgramsBaseAdapter programsAdapter = new ProgramsBaseAdapter(context, programs);
        lvPrograms.setAdapter(programsAdapter);

        // prepara pesquisa rapida
        TextView programsSearch = (TextView) findViewById(R.id.programSearch);
        programsSearch.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (cs.toString().trim().isEmpty()) {
                    programsAdapter.setPrograms(new ArrayList<Program>(programs));
                } else {
                    programsAdapter.getPrograms().clear();
                    for (Program program : programs) {
                        String progName = Util.removeDiacriticalMarks(program.getTitle().toLowerCase());
                        if (progName.startsWith(cs.toString())) {
                            programsAdapter.getPrograms().add(0, program);
                        } else if (progName.contains(cs.toString())) {
                            programsAdapter.getPrograms().add(program);
                        }
                    }
                }
                lvPrograms.invalidateViews();
            }

            public void afterTextChanged(Editable edtbl) {
            }
        });
    }
}
