package com.example.todolistjava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistjava.R;
import com.example.todolistjava.model.ToDo;

import java.util.ArrayList;
import java.util.List;

public class ToDoRecyclerAdapter extends RecyclerView.Adapter<ToDoRecyclerAdapter.ToDoViewHolder> {

    ArrayList<ToDo> toDoList;
    public ToDoRecyclerAdapter(ArrayList<ToDo> toDoList){
         this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public ToDoRecyclerAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ToDoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoRecyclerAdapter.ToDoViewHolder holder, int position) {

        holder.toDoIdText.setText(toDoList.get(position).getId());
        holder.toDoTitleText.setText(toDoList.get(position).getToDoTitle());
        holder.toDoDateText.setText(toDoList.get(position).getDate());
        holder.linearLayout.setBackgroundColor(Integer.parseInt(toDoList.get(position).getColor()));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_toDoListFragment_to_addToDoListFragment);
            }
        });

         // cardview background

    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public void setToDoList(ArrayList<ToDo> newToDoList){
        toDoList.clear();
        toDoList.addAll(newToDoList);
        notifyDataSetChanged();
    }

    public void updateData(){
        //data update
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView toDoIdText,toDoTitleText,toDoDateText;
        LinearLayout linearLayout;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoIdText = itemView.findViewById(R.id.toDoIdText);
            toDoTitleText = itemView.findViewById(R.id.rowTitleText);
            toDoDateText = itemView.findViewById(R.id.rowDateText);
            linearLayout = itemView.findViewById(R.id.toDoRowLinearLayout);
        }
    }
}
