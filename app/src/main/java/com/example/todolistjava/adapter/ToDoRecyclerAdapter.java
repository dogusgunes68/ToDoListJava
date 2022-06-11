package com.example.todolistjava.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistjava.R;
import com.example.todolistjava.model.ToDo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ToDoRecyclerAdapter extends RecyclerView.Adapter<ToDoRecyclerAdapter.ToDoViewHolder> implements Filterable {

    private List<ToDo> toDoList;
    private List<ToDo> toDoListAll;
    public ToDoRecyclerAdapter(ArrayList<ToDo> toDoList){
         this.toDoList = toDoList;
         this.toDoListAll = new ArrayList<>(toDoList);
    }

    @NonNull
    @Override
    public ToDoRecyclerAdapter.ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ToDoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoRecyclerAdapter.ToDoViewHolder holder, int position) {

        System.out.println("adapter color:"+toDoList.get(position).getColor());
        holder.toDoIdText.setText(toDoList.get(position).getId());
        holder.toDoTitleText.setText(toDoList.get(position).getToDoTitle());
        holder.toDoDateText.setText(toDoList.get(position).getDate().toDate().toString());
        holder.linearLayout.setBackgroundColor(Integer.parseInt(toDoList.get(position).getColor()));


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("toDoId", toDoList.get(position).getId());
                bundle.putBoolean("isFavorite",false);
                Navigation.findNavController(v).navigate(R.id.action_toDoListFragment_to_editToDoListFragment,bundle);
            }
        });

         // cardview background

    }

    @Override
    public int getItemCount() {

        if (toDoList.size() == 0){
            return 0;
        }else {
            return toDoList.size();
        }
    }

    public void setToDoList(List<ToDo> newToDoList){
        toDoList.clear();
        toDoList.addAll(newToDoList);
        toDoListAll.clear();
        toDoListAll.addAll(toDoList);
        notifyDataSetChanged();
    }

    public List<ToDo> getToDoList(){
        return toDoList;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ToDo> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()){
                filteredList.addAll(toDoListAll);
            }else {
                for (ToDo toDo : toDoListAll){
                    if (toDo.getToDoTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                        filteredList.add(toDo);
                        System.out.println(toDo.getToDoTitle());
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            toDoList.clear();
            toDoList.addAll((Collection<? extends ToDo>) results.values);
            notifyDataSetChanged();
        }
    };
    @Override
    public Filter getFilter() {
        return filter;
    }


    public class ToDoViewHolder extends RecyclerView.ViewHolder {
        TextView toDoIdText,toDoTitleText,toDoDateText;
        LinearLayout linearLayout;
        CardView cardView;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            toDoIdText = itemView.findViewById(R.id.toDoIdText);
            toDoTitleText = itemView.findViewById(R.id.rowTitleText);
            toDoDateText = itemView.findViewById(R.id.rowDateText);
            linearLayout = itemView.findViewById(R.id.toDoRowLinearLayout);
            cardView = itemView.findViewById(R.id.rowCardView);
        }
    }
}
