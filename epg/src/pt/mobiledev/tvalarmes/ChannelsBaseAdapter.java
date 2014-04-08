package pt.mobiledev.tvalarmes;

import java.util.ArrayList;

import pt.mobiledev.tvalarmes.domain.Channel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChannelsBaseAdapter extends BaseAdapter {

    ArrayList<Channel> channels = new ArrayList<Channel>();
    LayoutInflater inflater;
    Context context;

    public ChannelsBaseAdapter(Context context, ArrayList<Channel> channels) {
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
        detail(convertView, R.id.tvTitle, channels.get(position).getName());
        return convertView;
        
    }

    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }
}
