package com.dtodorov.zerobeat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dtodorov.zerobeat.R;
import com.dtodorov.zerobeat.models.CardModel;

/**
 * Created by diman on 7/16/2017.
 */

public class CardsAdapter extends ArrayAdapter<CardModel>
{
    public CardsAdapter(Context context)
    {
        super(context, R.layout.item_course);
    }

    private class ViewHolder
    {
        ImageView imageView;
        TextView tvTitle;
        TextView tvSubtitle;

        ViewHolder(View view)
        {
            imageView = (ImageView) view.findViewById(R.id.item_course_image);
            tvTitle = (TextView) view.findViewById(R.id.item_course_title);
            tvSubtitle = (TextView) view.findViewById(R.id.item_course_summary);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.item_course, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        CardModel model = getItem(position);

        holder.imageView.setImageResource(model.getImageId());
        holder.tvTitle.setText(model.getTitle());
        holder.tvSubtitle.setText(model.getSubtitle());

        return convertView;
    }
}