package pt.mobiledev.tvalarmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.AlarmDao;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.domain.Channel;

public class AlarmsBaseAdapter extends BaseAdapter {

    ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    LayoutInflater inflater;
    Context context;
    AlarmDao db;

    public AlarmsBaseAdapter(Context context, List<Alarm> alarms, AlarmDao db) {
        this.alarms = new ArrayList<Alarm>(alarms);
        this.context = context;
        this.db = db;
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
        detail(convertView, alarms.get(position));
        Button btnDelete = (Button) convertView.findViewById(R.id.btnDeleteAlarm);
        btnDelete.setTag(position);
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Alarm alarm = alarms.get(position);
                db.delete(alarm);  // Apagar da base de dados
                alarms.remove(position);  // Apagar da lista
                notifyDataSetChanged();  // Actualizar lista
            }
        });
        return convertView;
    }

    private void detail(View v, Alarm alarm) {
        TextView tv = (TextView) v.findViewById(R.id.tvTitle);
        tv.setText(alarm.getProgram().getTitle());
        ImageView pic = (ImageView) v.findViewById(R.id.tvImageAlarm);
        pic.setImageResource(new Channel(alarm.getProgram().getChannelId()).getLogoResourceId(context));
    }
}
