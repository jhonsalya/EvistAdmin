package com.example.jhonsalya.evistadmin.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jhonsalya.evistadmin.R;
import com.squareup.picasso.Picasso;

public class UserViewHolder extends RecyclerView.ViewHolder{
    View mView;
    public ImageView overflow;
    public UserViewHolder(View itemView){
        super(itemView);
        mView = itemView;

        overflow = (ImageView) itemView.findViewById(R.id.block);
    }

    public void setName(String name){
        TextView post_title = (TextView) mView.findViewById(R.id.user_name);
        post_title.setText(name);
    }

    public void setEmail(String email){
        TextView post_desc = (TextView) mView.findViewById(R.id.user_email);
        post_desc.setText(email);
    }

//    public void setImage(Context ctx, String image){
//        ImageView post_image = (ImageView) mView.findViewById(R.id.block);
//        Picasso.with(ctx).load(image).into(post_image);
//    }
}
