package pt.mobiledev.tvalarmes;

import java.util.ArrayList;
import java.util.List;

import pt.mobiledev.tvalarmes.domain.Alarm;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AlarmsBaseAdapater extends BaseAdapter {

	ArrayList<Alarm> alarms = new ArrayList<Alarm>();
	LayoutInflater inflater;
	Context context;

	public AlarmsBaseAdapater(Context context, List<Alarm> alarms) {
		this.alarms = new ArrayList<Alarm>(alarms);
		this.context = context;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return alarms.size();
	}

	@Override
	public Alarm getItem(int position) {
		return alarms.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.alarm_detail, null);
		detail(convertView, R.id.tvTitle, alarms.get(position).getProgram().getTitle());

		Button btnDelete = (Button) convertView.findViewById(R.id.btnDeleteAlarm);
		btnDelete.setTag(position);
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				alarms.remove(position);
				notifyDataSetChanged();
			}
		});

		return convertView;
	}

	private TextView detail(View v, int resId, String text) {
		TextView tv = (TextView) v.findViewById(resId);
		tv.setText(text);
		return tv;
	}
}
