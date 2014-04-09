package pt.mobiledev.tvalarmes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.EPGDao;
import pt.mobiledev.tvalarmes.domain.Channel;

public class ChannelsActivity extends Activity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_channels);

        List<Channel> channels = EPGDao.getChannels();

        GridView gridview = (GridView) findViewById(R.id.gvChannels);
        gridview.setAdapter(new ChannelsBaseAdapter(this, channels));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(ChannelsActivity.this, "Clicked: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
