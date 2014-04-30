package pt.mobiledev.tvalarmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.List;
import pt.mobiledev.tvalarmes.domain.Channel;

public class ChannelsBaseAdapter extends ArrayAdapter<Channel> {

    LayoutInflater inflater;

    public ChannelsBaseAdapter(final Context context, int resource, List<Channel> channels) {
        super(context, resource, channels);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Channel ch = getItem(position);
        boolean hasImage = ch.getLogoResourceId(getContext()) > 0;
        if (convertView == null) {  // Inicializar View
            convertView = inflater.inflate(R.layout.channel_detail, null);
        }
        // Alterar image/texto da View
        ImageView iv = (ImageView) convertView.findViewById(R.id.tvImage);
        if (hasImage) {
            iv.setImageResource(ch.getLogoResourceId(getContext()));
        }
        return convertView;
    }
}
