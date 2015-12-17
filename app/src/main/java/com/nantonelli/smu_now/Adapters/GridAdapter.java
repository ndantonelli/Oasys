package com.nantonelli.smu_now.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nantonelli.smu_now.Fragments.TestFrag;
import com.nantonelli.smu_now.Model.Event;
import com.nantonelli.smu_now.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by ndantonelli on 10/20/15.
 * Grid adapter used for all event gridviews on the main pages
 */
public class GridAdapter extends BaseAdapter {

    //prevent continuously refinding the views
    private static class ViewHolder{
        ImageView image;
        TextView text;
    }
    private Context mContext;
    private List<Event> events;
    private Picasso picasso;

    public GridAdapter(Context context, List<Event> events, Picasso picasso){
        this.picasso = picasso;
        this.events = events;
        mContext = context;
    }

    public void refresh(List<Event> events){
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHold;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            vHold = new ViewHolder();
            convertView = inflater.inflate(R.layout.component_grid_view, parent, false);
            vHold.text= (TextView) convertView.findViewById(R.id.title);
            vHold.image= (ImageView)convertView.findViewById(R.id.back_img);
            convertView.setTag(vHold);
        } else {
            vHold = (ViewHolder)convertView.getTag();
        }
        vHold.text.setText(events.get(position).getEvent_name());
        picasso.load(events.get(position).getPhoto_url()).priority(Picasso.Priority.HIGH).into(vHold.image);
        return convertView;
    }
}
