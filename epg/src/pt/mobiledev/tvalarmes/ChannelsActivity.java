package pt.mobiledev.tvalarmes;

import java.util.ArrayList;

import pt.mobiledev.tvalarmes.domain.Channel;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

/**
 *
 * @author lmsoares
 */
public class ChannelsActivity extends Activity {
	
	Context context = ChannelsActivity.this;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_channels);
        
        ArrayList<Channel> channels = new ArrayList<Channel>();
        Channel rtp1 = new Channel("id");
        rtp1.setName("RTP 1");
        channels.add(rtp1);
        
        Channel rtp2 = new Channel("id");
        rtp2.setName("RTP 2");
        channels.add(rtp2);
        
        GridView gridview = (GridView) findViewById(R.id.gvChannels);
        gridview.setAdapter(new ChannelsBaseAdapter(context, channels));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(context, "Clicked: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
