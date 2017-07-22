package com.dtodorov.zerobeat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dtodorov.androlib.services.StringResolver;
import com.dtodorov.zerobeat.R;
import com.dtodorov.zerobeat.models.LessonModel;

import java.util.List;

/**
 * Created by diman on 7/9/2017.
 */

public class LessonsAdapter extends ArrayAdapter<LessonModel>
{
    private StringResolver stringResolver;

    public LessonsAdapter(@NonNull Context context, @NonNull List<LessonModel> objects, StringResolver stringResolver)
    {
        super(context, 0, objects);
        this.stringResolver = stringResolver;
    }

    private class ViewHolder
    {
        TextView tvName;
        TextView tvDescription;

        ViewHolder(View view)
        {
            tvName = (TextView) view.findViewById(R.id.lesson_name);
            tvDescription = (TextView) view.findViewById(R.id.lesson_description);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LessonModel model = getItem(position);

        ViewHolder holder;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.item_lesson, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        String lessonName = stringResolver.getString(R.string.lesson) + " " + (position + 1);
        holder.tvName.setText(lessonName);
        holder.tvDescription.setText(model.lessonDescription);

        return convertView;
    }
}
