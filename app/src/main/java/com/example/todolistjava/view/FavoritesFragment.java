package com.example.todolistjava.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.todolistjava.R;
import com.example.todolistjava.adapter.FavoritesRecyclerAdapter;
import com.example.todolistjava.databinding.FragmentFavoritesBinding;
import com.example.todolistjava.model.ToDo;
import com.example.todolistjava.viewmodel.FavoritesViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    private FavoritesViewModel favoritesViewModel;
    private FragmentFavoritesBinding fragmentBinding;
    private FavoritesRecyclerAdapter favoritesRecyclerAdapter = new FavoritesRecyclerAdapter(new ArrayList<>());
    List<ToDo> takenList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentBinding = FragmentFavoritesBinding.bind(view);
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        favoritesViewModel.getToDoListFromFirebase(getActivity());

        fragmentBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_favoritesFragment_to_toDoListFragment);
            }
        });

        observeFavorites();

    }


    public void observeFavorites(){
        favoritesViewModel.toDoList.observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDoList) {
                if (!toDoList.isEmpty()){
                    fragmentBinding.favoritesRecyclerView.setVisibility(View.VISIBLE);
                    favoritesRecyclerAdapter.setFavorites(toDoList);
                    for (ToDo toDo : toDoList){
                        System.out.println(toDo.getToDoTitle());
                    }
                    fragmentBinding.favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    fragmentBinding.favoritesRecyclerView.setAdapter(favoritesRecyclerAdapter);
                }else {
                    System.out.println("boş");
                }
            }
        });


        favoritesViewModel.toDoLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.loadingProgressBar.setVisibility(View.VISIBLE);
                    fragmentBinding.favoritesRecyclerView.setVisibility(View.INVISIBLE);
                }else {
                    fragmentBinding.loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        favoritesViewModel.errorMessage.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.errorMessageText.setVisibility(View.VISIBLE);
                    fragmentBinding.favoritesRecyclerView.setVisibility(View.INVISIBLE);
                }else {
                    fragmentBinding.errorMessageText.setVisibility(View.INVISIBLE);
                }
            }
        });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentBinding = null;
    }
}