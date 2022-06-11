 package com.example.todolistjava.view;

 import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.todolistjava.R;
import com.example.todolistjava.adapter.ToDoRecyclerAdapter;
import com.example.todolistjava.databinding.FragmentToDoListBinding;
import com.example.todolistjava.model.ToDo;
import com.example.todolistjava.viewmodel.FavoritesViewModel;
import com.example.todolistjava.viewmodel.ToDoListViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


 public class ToDoListFragment extends Fragment {

    private ToDoRecyclerAdapter toDoRecyclerAdapter = new ToDoRecyclerAdapter(new ArrayList<>());
    private FragmentToDoListBinding fragmentBinding;
    private ToDoListViewModel toDoListViewModel;
    private FavoritesViewModel favoritesViewModel;
    private FirebaseAuth auth;
    private AlertDialog.Builder alertDialog;

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
        toDoListViewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        toDoListViewModel.getToDoListFromFirebase(getContext());
        auth = FirebaseAuth.getInstance();
        alertDialog = new AlertDialog.Builder(getContext());




        fragmentBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                    fragmentBinding.loadingProgressBar.setVisibility(View.VISIBLE);
                    fragmentBinding.errorMessageText.setVisibility(View.GONE);
                    fragmentBinding.toDoRecyclerView.setVisibility(View.GONE);
                    toDoListViewModel.getToDoListFromFirebase(getContext());
                    fragmentBinding.swipeRefreshLayout.setRefreshing(false);



            }
        });
        observeLiveData();
        fragmentBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_toDoListFragment_to_addToDoListFragment);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(fragmentBinding.toDoRecyclerView);

        search();


    }

    ToDo deletedToDo = null;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT :
                    delete(position);
                    break;
                case ItemTouchHelper.RIGHT :
                    addToFavorites(position);
                    break;
            }

        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(),R.color.green))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_star_24)
                    .create()
                    .decorate();
        }
    };



     private void addToFavorites(int position) {

         List<ToDo> toDoList = toDoRecyclerAdapter.getToDoList();
         ToDo toDo = toDoList.get(position);

         toDoList.remove(position);
         toDoRecyclerAdapter.notifyItemRemoved(position);
         toDoListViewModel.deleteToDo(toDo.getId(),getActivity());
         favoritesViewModel.addToDoToFirebase(toDo,getActivity());

     }

     private void delete(int position) {

         List<ToDo> toDoList = toDoRecyclerAdapter.getToDoList();
         deletedToDo = toDoList.get(position);
         toDoList.remove(position);
         toDoRecyclerAdapter.notifyItemRemoved(position);
         toDoListViewModel.deleteToDo(deletedToDo.getId(),getActivity());
         Snackbar.make(fragmentBinding.toDoRecyclerView,deletedToDo.getToDoTitle(),Snackbar.LENGTH_LONG)
                 .setAction("Undo", new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         toDoList.add(position,deletedToDo);
                         toDoListViewModel.addToDoToFirebase(deletedToDo,getContext());
                         toDoListViewModel.getToDoListFromFirebase(getContext());
                         toDoRecyclerAdapter.notifyItemInserted(position);

                     }
                 }).show();

     }

     public void search(){
        fragmentBinding.toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu);
                MenuItem searchItem = menu.findItem(R.id.search_item);
                SearchView searchView = (SearchView) searchItem.getActionView();
                menuInflater.inflate(R.menu.options_menu,menu);

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
                signOut(menuInflater,menu);
                goToFavorites(menuInflater,menu);
            }
          @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        });
    }

     @Override
     public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
         super.onCreateOptionsMenu(menu, inflater);



     }

     public void signOut(MenuInflater menuInflater, Menu menu){

        MenuItem signOutItem = menu.findItem(R.id.signOut);
        signOutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.signOut){
                    alertDialog.setMessage("Welcome to Alert Dialog") .setTitle("Javatpoint Alert Dialog");

                    alertDialog.setMessage("Do you want to sign out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    auth.signOut();
                                    Navigation.findNavController(getView()).navigate(R.id.action_toDoListFragment_to_loginFragment);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = alertDialog.create();
                    alert.setTitle("Sign Out");
                    alert.show();

                }
                return false;
            }
        });
    }

    public void goToFavorites(MenuInflater menuInflater, Menu menu){
         MenuItem item = menu.findItem(R.id.favorites);
         item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
             @Override
             public boolean onMenuItemClick(MenuItem item) {

                 if (item.getItemId() == R.id.favorites){
                     Navigation.findNavController(getView()).navigate(R.id.action_toDoListFragment_to_favoritesFragment);
                 }

                 return false;
             }
         });
    }

     public void observeLiveData(){
        toDoListViewModel.toDoList.observe(getViewLifecycleOwner(), new Observer<List<ToDo>>() {
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

        toDoListViewModel.errorMessage.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.toDoRecyclerView.setVisibility(View.INVISIBLE);
                    fragmentBinding.errorMessageText.setVisibility(View.VISIBLE);
                }else {

                    fragmentBinding.errorMessageText.setVisibility(View.INVISIBLE);
                }
            }
        });

        toDoListViewModel.toDoLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.toDoRecyclerView.setVisibility(View.INVISIBLE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.VISIBLE);
                }else {
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