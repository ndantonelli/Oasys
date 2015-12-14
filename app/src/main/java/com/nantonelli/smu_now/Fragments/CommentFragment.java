package com.nantonelli.smu_now.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nantonelli.smu_now.Adapters.CommentsAdapter;
import com.nantonelli.smu_now.Adapters.OasysRestfulAPI;
import com.nantonelli.smu_now.Model.Comment;
import com.nantonelli.smu_now.Model.Event;
import com.nantonelli.smu_now.OasysApplication;
import com.nantonelli.smu_now.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ndantonelli on 9/28/15.
 */
public class CommentFragment extends Fragment {

    private Event event;
    private ListView commentList;
    private EditText newComment;
    private TextView postComment;
    @Inject
    OasysRestfulAPI service;
    @Inject
    Picasso picasso;
    private CommentsAdapter adapter;

    public static CommentFragment newInstance(Event event){
        CommentFragment c = new CommentFragment();
        c.event = event;
        return c;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        ((OasysApplication)getActivity().getApplication()).getObjectGraph().inject(this);
        ButterKnife.bind(v);
        newComment = (EditText) v.findViewById(R.id.comment_box);
        postComment = (TextView) v.findViewById(R.id.post_button);
        commentList = (ListView) v.findViewById(R.id.comments);
        adapter = new CommentsAdapter(getContext(), new ArrayList<Comment>(), picasso);
        commentList.setAdapter(adapter);
        if(event != null)
            service.getComments(event.getEvent_id(), new Callback<List<Comment>>() {
                @Override
                public void success(List<Comment> comments, Response response) {
                    adapter.clear();
                    adapter.addAll(comments);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comment = newComment.getText().toString().trim();
                if (comment.length() > 0) {
                    newComment.setText("");
                    service.postComment(OasysApplication.getInstance().uid, event.getEvent_id(), comment, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            adapter.add(new Comment(comment, OasysApplication.getInstance().username));
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }
        });
        return v;
    }

}