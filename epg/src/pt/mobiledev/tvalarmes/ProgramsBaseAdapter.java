package pt.mobiledev.tvalarmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import pt.mobiledev.tvalarmes.domain.Program;

public class ProgramsBaseAdapter extends ArrayAdapter<Program> {

    LayoutInflater inflater;

    public ProgramsBaseAdapter(Context context, int resource, List<Program> programs) {
        super(context, resource, programs);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.program_detail, null);
        }
        detail(convertView, R.id.tvTitle, getItem(position).getTitle());
        return convertView;
    }

    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }
}
