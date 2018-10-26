package com.example.jhonsalya.evistadmin.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jhonsalya.evistadmin.Common.Common;
import com.example.jhonsalya.evistadmin.Interface.ItemClickListener;
import com.example.jhonsalya.evistadmin.R;
import com.squareup.picasso.Picasso;


/*public class CategoryViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public CategoryViewHolder(View itemView, View mView) {
        super(itemView);
        this.mView = mView;
    }

    public void setImage(Context ctx, String image){
        ImageView post_image = (ImageView) mView.findViewById(R.id.category_image);
        Picasso.with(ctx).load(image).into(post_image);
    }

    public void setName(String name){
        TextView post_title = (TextView) mView.findViewById(R.id.category_name);
        post_title.setText(name);
    }
}*/

public class CategoryViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener
{
    private Context mContext;
    public TextView txtCategoryName;
    public ImageView image;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        txtCategoryName = (TextView)itemView.findViewById(R.id.category_name);
        image = (ImageView) itemView.findViewById(R.id.category_image);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select option");

        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}
