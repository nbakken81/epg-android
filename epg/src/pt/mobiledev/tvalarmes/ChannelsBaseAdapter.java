package pt.mobiledev.tvalarmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import pt.mobiledev.tvalarmes.domain.Channel;

public class ChannelsBaseAdapter extends BaseAdapter {

    List<Channel> channels;
    LayoutInflater inflater;
    Context context;

    public ChannelsBaseAdapter(final Context context, List<Channel> channels) {
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
        Channel ch = channels.get(position);
        
        if (ch.getLogoResourceId(context) > 0) {
        	/* Tem imagem */
        	convertView = inflater.inflate(R.layout.channel_detail, null);
        	ImageView iv = (ImageView) convertView.findViewById(R.id.tvImage);
            iv.setImageResource(ch.getLogoResourceId(context));
        } else {
        	/* Mostra t’tulo */
        	convertView = inflater.inflate(R.layout.channel_detail_text, null);
        	TextView tv = (TextView) convertView.findViewById(R.id.tvTitle);
        	tv.setText(ch.getName());
        }
        return convertView;
    }
}
