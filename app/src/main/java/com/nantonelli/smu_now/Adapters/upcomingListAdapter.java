package com.nantonelli.smu_now.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.nantonelli.smu_now.Model.Comment;
import com.nantonelli.smu_now.Model.Event;
import com.nantonelli.smu_now.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ndantonelli on 12/13/15.
 */
public class upcomingListAdapter extends ArrayAdapter<Event> {
    private static class ViewHolder{
        RadioButton button;
        TextView title;
        TextView time;
    }
    private List<Event> events;
    private int selectedPosition = 0;

    public upcomingListAdapter(Context context, List<Event> events){
        super(context, 0, events);
        this.events = events;
    }

    public void addAll(List<Event> events){
        clear();
        super.addAll(events);
        selectedPosition = 0;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Event event = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.component_radio_listview, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title_event);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time_event);
            viewHolder.button = (RadioButton) convertView.findViewById(R.id.event_radio);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.button.setChecked(position == selectedPosition);
        viewHolder.button.setTag(position);
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = (Integer) v.getTag();
                notifyDataSetChanged();
            }
        });
        viewHolder.title.setText(event.getEvent_name());
        viewHolder.time.setText(event.getTime_start());
        return convertView;
    }

    public Event getSelectedEvent(){
        return events.get(selectedPosition);
    }
}
