package pt.mobiledev.tvalarmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import pt.mobiledev.tvalarmes.domain.Channel;

public class ChannelsBaseAdapter extends BaseAdapter {

    List<Channel> channels;
    LayoutInflater inflater;
    Context context;

    public ChannelsBaseAdapter(Context context, List<Channel> channels) {
        this.channels = channels;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    @Override
    public Channel getItem(int position) {
        return channels.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.layout_list_channel, null);
        prepareDetail(convertView, R.id.tvTitle, channels.get(position).getName());
        return convertView;
    }

    private TextView prepareDetail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }
}
