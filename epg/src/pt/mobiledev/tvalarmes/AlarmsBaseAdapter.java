package pt.mobiledev.tvalarmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import pt.mobiledev.tvalarmes.dao.AlarmDao;
import pt.mobiledev.tvalarmes.domain.Alarm;
import pt.mobiledev.tvalarmes.domain.Channel;

public class AlarmsBaseAdapter extends ArrayAdapter<Alarm> {

    LayoutInflater inflater;
    AlarmDao alarmDao;

    public AlarmsBaseAdapter(Context context, int resId, List<Alarm> alarms, AlarmDao alarmDao) {
        super(context, resId, alarms);
        this.alarmDao = alarmDao;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alarm_detail, null);
        }
        detail(convertView, getItem(position));
        Button btnDelete = (Button) convertView.findViewById(R.id.btnDeleteAlarm);
        btnDelete.setTag(position);
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Alarm alarm = getItem(position);
                alarmDao.delete(alarm);  // Apagar da base de dados
                remove(alarm);  // Apagar da lista
                notifyDataSetChanged();  // Actualizar lista
            }
        });
        return convertView;
    }

    private void detail(View v, Alarm alarm) {
        TextView tv = (TextView) v.findViewById(R.id.tvTitle);
        tv.setText(alarm.getProgram().getTitle());
        ImageView pic = (ImageView) v.findViewById(R.id.tvImageAlarm);
        pic.setImageResource(new Channel(alarm.getProgram().getChannelId()).getLogoResourceId(getContext()));
    }
}
