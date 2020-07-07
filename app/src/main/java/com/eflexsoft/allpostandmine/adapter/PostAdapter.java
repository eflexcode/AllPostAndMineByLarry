package com.eflexsoft.allpostandmine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eflexsoft.allpostandmine.R;
import com.eflexsoft.allpostandmine.placeholder.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    List<Post> postList;

    Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posts_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = postList.get(position);
        holder.body.setText(post.getBody());
        holder.title.setText(post.getTitle());
        holder.date.setText(post.getDate());
        Glide.with(context).load(post.getImage()).into(holder.postImage);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView postImage;
        TextView title;
        TextView body;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.post_image);
            title = itemView.findViewById(R.id.post_tile);
            body = itemView.findViewById(R.id.post_body);
            date = itemView.findViewById(R.id.post_date);
        }
    }
}
