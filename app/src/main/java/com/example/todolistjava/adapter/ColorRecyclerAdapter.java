package com.example.todolistjava.adapter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistjava.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ColorRecyclerAdapter extends RecyclerView.Adapter<ColorRecyclerAdapter.ViewHolder> {

    List<String> colorList;
    Dialog myDialog;
    RelativeLayout relativeLayout;

    public ColorRecyclerAdapter(List<String> colorList, Dialog myDialog, RelativeLayout relativeLayout){
        this.colorList = colorList;
        this.myDialog = myDialog;
        this.relativeLayout = relativeLayout;
    }

    @NonNull
    @Override
    public ColorRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.color_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorRecyclerAdapter.ViewHolder holder, int position) {
        holder.colorImageView.setBackgroundColor(Color.parseColor(colorList.get(position)));
        holder.colorImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               relativeLayout.setBackgroundColor(Color.parseColor(colorList.get(position)));
               myDialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView colorImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            colorImageView = itemView.findViewById(R.id.rowColorImageView);
        }
    }

    public void setRelativeLayout(RelativeLayout relativeLayout){
        this.relativeLayout = relativeLayout;
    }
}
