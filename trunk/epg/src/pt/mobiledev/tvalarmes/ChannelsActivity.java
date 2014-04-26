package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.util.Util;

public class ChannelsActivity extends Activity {

    ProgressDialog progress;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.channel_list);
        GetChannelsTask getChannelsTask = new GetChannelsTask(this);
        getChannelsTask.execute();
        // Mostrar progress dialog
        progress = new ProgressDialog(this);
        progress.show();
    }

    private void createGridView(final List<Channel> channels) {
        Collections.sort(channels, new Comparator<Channel>() {
            public int compare(Channel ch1, Channel ch2) {
                Boolean hasLogo1 = ch1.getLogoResourceId(ChannelsActivity.this) > 0;
                Boolean hasLogo2 = ch2.getLogoResourceId(ChannelsActivity.this) > 0;
                return hasLogo2.compareTo(hasLogo1);
            }
        });
        // prepara grelha de canais
        final GridView channelsGrid = (GridView) findViewById(R.id.gvChannels);
        final ChannelsBaseAdapter channelsAdapter = new ChannelsBaseAdapter(this, channels);
        channelsGrid.setAdapter(channelsAdapter);
        channelsGrid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(ChannelsActivity.this, ProgramsActivity.class);
                intent.putExtra("channel", channelsAdapter.getItem(position));
                startActivity(intent);
            }
        });
        // prepara pesquisa rapida
        List<String> CHANNELS = new ArrayList<String>(channels.size());
        for (Channel ch : channels) {
            CHANNELS.add(ch.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CHANNELS);
        final AutoCompleteTextView channelSearch = (AutoCompleteTextView) findViewById(R.id.channelSearch);
        channelSearch.setAdapter(adapter);
        channelSearch.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> av, View view, int i, long l) {
                String textAC = channelSearch.getText().toString();
                if (textAC.trim().isEmpty()) {
                    channelsAdapter.setChannels(new ArrayList<Channel>(channels));
                    return;
                }
                channelsAdapter.getChannels().clear();
                for (Channel channel : channels) {
                    if (Util.relaxedStartsWith(channel.getName(), textAC)) {
                        channelsAdapter.getChannels().add(0, channel);
                    } else if (Util.relaxedContains(channel.getName(), textAC)) {
                        channelsAdapter.getChannels().add(channel);
                    }
                }
                channelsGrid.invalidateViews();
            }
        });
    }

    private class GetChannelsTask extends AsyncTask<Void, Void, Integer> {

        List<Channel> channels;
        Context context;

        public GetChannelsTask(Context context) {
            this.context = context;
        }

        protected Integer doInBackground(Void... params) {
            // Sacar canais em background
            channels = EPGDao.getChannels(context);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Criar gridView
            createGridView(channels);
            progress.dismiss();
        }
    }

}
