package com.nantonelli.smu_now.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nantonelli.smu_now.Model.Comment;
import com.nantonelli.smu_now.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by ndantonelli on 12/12/15.
 * adapter for filling in the comments on an event
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {
    private static class ViewHolder{
        TextView comment;
        ImageView report;
    }
    private List<Comment> comments;
    private Context context;

    public CommentsAdapter(Context context, List<Comment> comments){
        super(context, 0, comments);
        this.comments = comments;
        this.context = context;
    }

    //refresh the comments in the list
    public void refresh(List<Comment> newComments){
        this.comments = newComments;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.component_comment, parent, false);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment_text);
            viewHolder.report = (ImageView) convertView.findViewById(R.id.comment_flag);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //will later be used to add flagging comment code
        viewHolder.comment.setText(comment.toString());
        viewHolder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.report.setAlpha(1.0f);
                viewHolder.report.setOnClickListener(null);
            }
        });

        return convertView;
    }
}
