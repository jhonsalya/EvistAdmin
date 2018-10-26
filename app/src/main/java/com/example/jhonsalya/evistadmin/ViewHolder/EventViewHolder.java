package com.example.jhonsalya.evistadmin.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jhonsalya.evistadmin.R;
import com.squareup.picasso.Picasso;

public class EventViewHolder extends RecyclerView.ViewHolder{

    View mView;
    public ImageView overflow;
    public EventViewHolder(View itemView){
        super(itemView);
        mView = itemView;

        overflow = (ImageView) itemView.findViewById(R.id.overflow);
    }

    public void setTitle(String title){
        TextView post_title = (TextView) mView.findViewById(R.id.title);
        post_title.setText(title);
    }

    public void setDesc(String desc){
        TextView post_desc = (TextView) mView.findViewById(R.id.count);
        post_desc.setText(desc);
    }

    public void setImage(Context ctx, String image){
        ImageView post_image = (ImageView) mView.findViewById(R.id.thumbnail);
        Picasso.with(ctx).load(image).into(post_image);
    }
}