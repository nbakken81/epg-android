package pt.mobiledev.tvalarmes;

import java.util.ArrayList;
import java.util.List;

import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Program;
import pt.mobiledev.tvalarmes.util.Util;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
        
        // On Item Click Listener
        lvPrograms.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	// Mostra popup de criação de alarme
            	showPopup(programs.get(position));
            }
        });
    }
    
    public void showPopup(final Program program) {
    	// Criação do popup
    	final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_setalarm);
        dialog.setTitle("Criação de Alarme");
        TextView text = (TextView) dialog.findViewById(R.id.tvAlarmPopup);
        text.setText("Tem a certeza que deseja criar um alarme para " + program.getTitle() + "?");
        dialog.show();
        // Textbox para escolher os minutos de antecedência
        final EditText et = (EditText) findViewById(R.id.editTextMinutesBefore);
        // Botão criar
        Button createAlarm = (Button) dialog.findViewById(R.id.btnCreateAlarm);
        createAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {                
            	// Criar alarme
            	Util.createAlarm(context, program, 0, false);
            	dialog.dismiss();
            }
        });
        // Botão cancelar
        Button cancelAlarm = (Button) dialog.findViewById(R.id.btnCancelAlarm);
        cancelAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
