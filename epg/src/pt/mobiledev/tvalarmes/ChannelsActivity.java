package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Channel;
import pt.mobiledev.tvalarmes.util.Util;

public class ChannelsActivity extends Activity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.channel_list);
        // busca canais e ordena-os
        final List<Channel> channels = EPGDao.getChannels(this);
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
                intent.putExtra("sigla", channelsAdapter.getItem(position).getId());
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
}
