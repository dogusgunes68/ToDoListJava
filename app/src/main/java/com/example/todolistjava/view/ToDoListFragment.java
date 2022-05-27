 package com.example.todolistjava.view;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.todolistjava.R;
import com.example.todolistjava.adapter.ToDoRecyclerAdapter;
import com.example.todolistjava.databinding.FragmentToDoListBinding;
import com.example.todolistjava.model.ToDo;
import com.example.todolistjava.viewmodel.ToDoListViewModel;

import java.util.ArrayList;
import java.util.List;


 public class ToDoListFragment extends Fragment {

    private ToDoRecyclerAdapter toDoRecyclerAdapter = new ToDoRecyclerAdapter(new ArrayList<>());

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
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentBinding = FragmentToDoListBinding.bind(view);
        viewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);

        viewModel.getToDoListFromFirebase(getContext());


        observeLiveData();

        fragmentBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_toDoListFragment_to_addToDoListFragment);
            }
        });

        fragmentBinding.toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu);
                MenuItem searchItem = menu.findItem(R.id.search_item);
                SearchView searchView = (SearchView) searchItem.getActionView();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        toDoRecyclerAdapter.getFilter().filter(query);

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        toDoRecyclerAdapter.getFilter().filter(newText);
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });


    }



     public void observeLiveData(){
        viewModel.toDoList.observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
            @Override
            public void onChanged(List<ToDo> toDos) {
                if (!toDos.isEmpty()) {
                    fragmentBinding.toDoRecyclerView.setVisibility(View.VISIBLE);
                    toDoRecyclerAdapter.setToDoList(toDos);

                    fragmentBinding.toDoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    fragmentBinding.toDoRecyclerView.setAdapter(toDoRecyclerAdapter);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentBinding = null;
    }
}