package pt.mobiledev.tvalarmes;

import java.util.ArrayList;
import java.util.SortedSet;

import pt.mobiledev.tvalarmes.domain.Program;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProgramsBaseAdapter extends BaseAdapter {

    ArrayList<Program> programs = new ArrayList<Program>();
    LayoutInflater inflater;
    Context context;

    public ProgramsBaseAdapter(Context context, SortedSet<Program> programs) {
        this.programs.addAll(programs);
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
}
