package pt.mobiledev.tvalarmes;

import java.util.ArrayList;
import java.util.List;

import pt.mobiledev.tvalarmes.domain.Channel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelsBaseAdapter extends BaseAdapter {

	List<Channel> channels;
	LayoutInflater inflater;
	Context context;

	public ChannelsBaseAdapter(final Context context, List<Channel> channels) {
		this.channels = new ArrayList<Channel>(channels);
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
		boolean hasImage = ch.getLogoResourceId(context) > 0;
		if(convertView == null) {
			// Inicializar View
			convertView = inflater.inflate(R.layout.channel_detail, null);
		} 
		// Alterar image/texto da View
		ImageView iv = (ImageView) convertView.findViewById(R.id.tvImage);
		TextView tv = (TextView) convertView.findViewById(R.id.tvTitle);
		if (hasImage) {
			/* Tem imagem */
			iv.setImageResource(ch.getLogoResourceId(context));
			tv.setVisibility(View.GONE);
			iv.setVisibility(1);
		} else {
			/* Mostra título */
			tv.setText(ch.getName());
			iv.setVisibility(View.GONE);
			tv.setVisibility(1);
		}

		return convertView;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}
}
