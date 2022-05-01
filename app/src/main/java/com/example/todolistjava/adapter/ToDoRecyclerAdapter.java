package com.example.todolistjava.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistjava.R;
import com.example.todolistjava.model.ToDo;

import java.util.List;

public class ToDoRecyclerAdapter extends RecyclerView.Adapter<ToDoRecyclerAdapter.ToDoViewHolder> {

    List<ToDo> toDoList;
    public ToDoRecyclerAdapter(List<ToDo> toDoList){
         this.toDoList = toDoList;
    }

    @NonNull
    @Override
    public ToDoRecyclerAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ToDoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoRecyclerAdapter.ToDoViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    public void setToDoList(List<ToDo> newToDoList){
        toDoList = newToDoList;
    }

    public void updateData(){
        //data update
    }

    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
