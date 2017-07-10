package com.dtodorov.zerobeat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dtodorov.androlib.services.StringResolver;
import com.dtodorov.zerobeat.R;

import java.util.List;

/**
 * Created by diman on 7/9/2017.
 */

public class LessonsAdapter extends ArrayAdapter<String>
{
    private StringResolver stringResolver;

    public LessonsAdapter(@NonNull Context context, @NonNull List<String> objects, StringResolver stringResolver)
    {
        super(context, 0, objects);
        this.stringResolver = stringResolver;
    }

    private class ViewHolder
    {
        TextView Name;
        TextView Description;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String description = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_lesson, parent, false);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.Description = (TextView) convertView.findViewById(R.id.description);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        String lessonName = stringResolver.getString(R.string.lesson) + " " + (position + 1);
        viewHolder.Name.setText(lessonName);
        viewHolder.Description.setText(description);

        // Return the completed view to render on screen
        return convertView;
    }
}
