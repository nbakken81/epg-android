package pt.mobiledev.tvalarmes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import pt.mobiledev.tvalarmes.domain.Program;

public class ProgramsBaseAdapter extends BaseAdapter {

    List<Program> programs;
    LayoutInflater inflater;
    Context context;

    public ProgramsBaseAdapter(Context context, List<Program> programs) {
        this.programs = new ArrayList<Program>(programs);
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return programs.size();
    }

    @Override
    public Program getItem(int position) {
        return programs.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//		if (convertView == null) {
//			convertView = inflater.inflate(R.layout.layout_list_program, null);
//		} else {
//			
//		}
        convertView = inflater.inflate(R.layout.program_detail, null);
        detail(convertView, R.id.tvTitle, programs.get(position).getTitle());
        return convertView;
    }

    private TextView detail(View v, int resId, String text) {
        TextView tv = (TextView) v.findViewById(resId);
        tv.setText(text);
        return tv;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }
}
