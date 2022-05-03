package com.example.todolistjava.adapter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistjava.R;

import java.util.HashMap;
import java.util.List;

public class WarningReyclerAdapter extends RecyclerView.Adapter<WarningReyclerAdapter.WarningViewHolder> {

    HashMap<String, String> warningList;
    List<String> keys;
    ImageView imageView;
    Dialog mDialog;

    public WarningReyclerAdapter(HashMap<String,String> warningList, List<String> keys, ImageView imageView, Dialog mDialog){
        this.warningList = warningList;
        this.keys = keys;
        this.imageView = imageView;
        this.mDialog=mDialog;
    }

    @NonNull
    @Override
    public WarningReyclerAdapter.WarningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WarningReyclerAdapter.WarningViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.warning_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WarningReyclerAdapter.WarningViewHolder holder, int position) {
        holder.warningTextView.setText(keys.get(position));
        holder.warningImageView.setBackgroundColor(Color.parseColor(warningList.get(keys.get(position))));

        holder.warningLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setBackground(holder.warningImageView.getBackground());
                mDialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return warningList.size();
    }

    public class WarningViewHolder extends RecyclerView.ViewHolder {

        LinearLayout warningLinearLayout;
        ImageView warningImageView;
        TextView warningTextView;

        public WarningViewHolder(@NonNull View itemView) {
            super(itemView);
            warningImageView = itemView.findViewById(R.id.rowWarningImageView);
            warningTextView = itemView.findViewById(R.id.rowWarningTextView);
            warningLinearLayout = itemView.findViewById(R.id.warningLinearLayout);
        }
    }
}
