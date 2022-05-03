package com.example.todolistjava.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolistjava.R;
import com.example.todolistjava.databinding.FragmentToDoListBinding;
import com.example.todolistjava.model.ToDo;
import com.example.todolistjava.viewmodel.ToDoListViewModel;

import java.util.List;

public class ToDoListFragment extends Fragment {


    private FragmentToDoListBinding fragmentBinding;
    private ToDoListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentBinding = FragmentToDoListBinding.bind(view);
        viewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);

        //viewModel.getToDoListFromFirebase(getContext());

        fragmentBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_toDoListFragment_to_addToDoListFragment);
            }
        });

    }

    public void observeLiveData(){
        viewModel.toDoList.observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                if (!toDos.isEmpty()) {
                    fragmentBinding.toDoRecyclerView.setVisibility(View.VISIBLE);
                    //rec adapter
                }
            }
        });

        viewModel.errorMessage.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.toDoRecyclerView.setVisibility(View.INVISIBLE);
                    fragmentBinding.floatingActionButton.setVisibility(View.INVISIBLE);
                    fragmentBinding.errorMessageText.setVisibility(View.VISIBLE);
                }else {
                    fragmentBinding.toDoRecyclerView.setVisibility(View.VISIBLE);
                    fragmentBinding.floatingActionButton.setVisibility(View.VISIBLE);
                    fragmentBinding.errorMessageText.setVisibility(View.INVISIBLE);
                }
            }
        });

        viewModel.toDoLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.toDoRecyclerView.setVisibility(View.INVISIBLE);
                    fragmentBinding.floatingActionButton.setVisibility(View.INVISIBLE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.VISIBLE);
                }else {
                    fragmentBinding.toDoRecyclerView.setVisibility(View.VISIBLE);
                    fragmentBinding.floatingActionButton.setVisibility(View.VISIBLE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

}