package com.rw.colormemory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rw.com.rw.database.Person;

import java.util.List;

/**
 * Created by Rakhita on 2/18/2018.
 */

public class HighestScoresAdapter extends BaseAdapter {

    private Context mContext;
    private List<Person> mPersonList;

    public HighestScoresAdapter(Context context, List<Person> personList) {
        this.mContext = context;
        this.mPersonList = personList;
    }

    @Override
    public int getCount() {
        return mPersonList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.highest_score_list_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.textViewRank = view.findViewById(R.id.textViewRank);
            viewHolder.textViewName = view.findViewById(R.id.textViewName);
            viewHolder.textViewScore = view.findViewById(R.id.textViewScore);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        Person person = mPersonList.get(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.textViewRank.setText(String.valueOf(position + 1));
        holder.textViewName.setText(person.getName());
        holder.textViewScore.setText(String.valueOf(person.getScore()));

        return view;
    }

    static class ViewHolder {
        public TextView textViewRank;
        public TextView textViewName;
        public TextView textViewScore;
    }
}
