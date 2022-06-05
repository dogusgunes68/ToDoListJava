package com.example.todolistjava.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistjava.R;
import com.example.todolistjava.model.ToDo;

import java.util.List;

public class FavoritesRecyclerAdapter extends RecyclerView.Adapter<FavoritesRecyclerAdapter.FavoriteViewHolder> {

    List<ToDo> favorites;
    public FavoritesRecyclerAdapter(List<ToDo> favorites){
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoritesRecyclerAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesRecyclerAdapter.FavoriteViewHolder holder, int position) {
        holder.toDoIdText.setText(favorites.get(position).getId());
        holder.toDoTitleText.setText(favorites.get(position).getToDoTitle());
        holder.todoDateText.setText(favorites.get(position).getDate());
        holder.toDoIdText.setText(favorites.get(position).getId().toString());
        holder.linearLayout.setBackgroundColor(Integer.parseInt(favorites.get(position).getColor()));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("toDoId", favorites.get(position).getId());
                Navigation.findNavController(v).navigate(R.id.action_favoritesFragment_to_editToDoListFragment,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView toDoIdText,toDoTitleText,todoDateText,uidText;
        LinearLayout linearLayout;
        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            uidText = itemView.findViewById(R.id.uidText);
            toDoIdText = itemView.findViewById(R.id.toDoIdText);
            toDoTitleText = itemView.findViewById(R.id.rowTitleText);
            todoDateText = itemView.findViewById(R.id.rowDateText);
            linearLayout = itemView.findViewById(R.id.toDoRowLinearLayout);
        }
    }

    public List<ToDo> getFavorites(){
        return favorites;
    }

    public void setFavorites(List<ToDo> newFavorites){
        favorites.clear();
        favorites.addAll(newFavorites);
        notifyDataSetChanged();
    }
}
