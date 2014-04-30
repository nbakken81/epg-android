package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.AlarmDao;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.domain.Program;
import pt.mobiledev.tvalarmes.util.AlarmNotifier;

public class ProgramsActivity extends Activity {

    ListView lvPrograms;
    Context context = ProgramsActivity.this;
    final AlarmDao alarmsDao = new AlarmDao(context); // adiciona à base de dados
    Channel selectedChannel;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_list);
        selectedChannel = (Channel) getIntent().getExtras().get("channel");
        GetProgramsTask getPrograsmTask = new GetProgramsTask(context, selectedChannel);
        getPrograsmTask.execute();
        // Mostrar progress dialog
        progress = new ProgressDialog(this);
        progress.show();
    }

    /**
     * Criação do popup
     *
     * @param program
     */
    public void showPopup(final Program program) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_setalarm);
        dialog.setTitle(program.getTitle());
        dialog.show();
        // Botão Criar
        Button createAlarm = (Button) dialog.findViewById(R.id.btnCreateAlarm);
        createAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {  // Criar alarme
                Alarm alarm = new Alarm(program, false);
                alarmsDao.add(alarm);
                AlarmNotifier.updateNotifications(context, new Channel(alarm.getProgram().getChannelId()));
                dialog.dismiss();
                ProgramsActivity.this.finish();
            }
        });
        // Botão Cancelar
        Button cancelAlarm = (Button) dialog.findViewById(R.id.btnCancelAlarm);
        cancelAlarm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void createListView(final List<Program> programs) {
        // limpar já existentes; é escusado apanhar com programas já na lista...
        List<Alarm> alarmsPerChannel = alarmsDao.findByChannel(selectedChannel);
        List<Program> alarmsPerChannelP = new ArrayList<Program>();
        for (Alarm alarm : alarmsPerChannel) {
            alarmsPerChannelP.add(alarm.getProgram());
        }
        programs.removeAll(alarmsPerChannelP);

        lvPrograms = (ListView) findViewById(R.id.lvPrograms);
        final ProgramsBaseAdapter programsAdapter = new ProgramsBaseAdapter(context, 0, programs);
        lvPrograms.setAdapter(programsAdapter);

        // info canal
        ImageView logoChannel = (ImageView) findViewById(R.id.programsLogo);
        logoChannel.setImageResource(selectedChannel.getLogoResourceId(context));
        TextView textLogo = (TextView) findViewById(R.id.channelName);
        textLogo.setText("(" + selectedChannel.getName() + ")");
        // On Item Click Listener
        lvPrograms.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // Mostra popup de criação de alarme
                showPopup(programs.get(position));
            }
        });

        // prepara pesquisa rápida
        final AutoCompleteTextView programSearch = (AutoCompleteTextView) findViewById(R.id.programSearch);
        programSearch.setAdapter(programsAdapter);
        programSearch.setOnItemClickListener(lvPrograms.getOnItemClickListener());
    }

    private class GetProgramsTask extends AsyncTask<Void, Void, Integer> {

        List<Program> programs;
        Channel selectedChannel;
        Context context;

        public GetProgramsTask(Context context, Channel selectedChannel) {
            this.selectedChannel = selectedChannel;
            this.context = context;
        }

        protected Integer doInBackground(Void... params) {
            programs = EPGDao.getAvailablePrograms(context, selectedChannel);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            createListView(programs);
            progress.dismiss();
        }
    }
}
