package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Channel;

public class ChannelsActivity extends Activity {

    Context context = ChannelsActivity.this;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.channel_list);
        // busca canais
        final List<Channel> channels = EPGDao.getChannels();
        // os mais importantes 1º: são os que têm logos
        Collections.sort(channels, new Comparator<Channel>() {

            public int compare(Channel ch0, Channel ch1) {
                Boolean existsLogo0 = ch0.getLogoResourceId(context) > 0;
                Boolean existsLogo1 = ch1.getLogoResourceId(context) > 0;
                return existsLogo1.compareTo(existsLogo0);
            }
        });
        // prepara vista
        GridView gridview = (GridView) findViewById(R.id.gvChannels);
        gridview.setAdapter(new ChannelsBaseAdapter(this, channels));
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(ChannelsActivity.this, ProgramsActivity.class);
                intent.putExtra("sigla", channels.get(position).getId());
                startActivity(intent);
            }
        });
    }
}
