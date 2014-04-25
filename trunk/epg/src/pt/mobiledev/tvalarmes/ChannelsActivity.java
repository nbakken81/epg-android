package pt.mobiledev.tvalarmes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.util.Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

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
        TextView channelSearch = (TextView) findViewById(R.id.channelSearch);
        channelSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if (cs.toString().trim().isEmpty()) {
                    channelsAdapter.setChannels(new ArrayList<Channel>(channels));
                } else {
                    channelsAdapter.getChannels().clear();
                    for (Channel channel : channels) {
                        if (Util.relaxedStartsWith(channel.getName(), cs.toString())) {
                            channelsAdapter.getChannels().add(0, channel);
                        } else if (Util.relaxedContains(channel.getName(), cs.toString())) {
                            channelsAdapter.getChannels().add(channel);
                        }
                    }
                }
                channelsGrid.invalidateViews();
            }

            public void afterTextChanged(Editable edtbl) {
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

        protected void onPostExecute(Integer result) { 
        	// Criar gridView
        	createGridView(channels);
        	progress.dismiss();
        }
    }
   
}
