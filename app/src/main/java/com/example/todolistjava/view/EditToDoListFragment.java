package com.example.todolistjava.view;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistjava.R;
import com.example.todolistjava.adapter.ColorRecyclerAdapter;
import com.example.todolistjava.databinding.FragmentEditToDoListBinding;
import com.example.todolistjava.model.ToDo;
import com.example.todolistjava.viewmodel.FavoritesViewModel;
import com.example.todolistjava.viewmodel.ToDoListViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class EditToDoListFragment extends Fragment {

    private FragmentEditToDoListBinding fragmentBinding;
    private ToDoListViewModel toDoListViewModel;
    private FavoritesViewModel favoritesViewModel;
    Dialog myDialog;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_to_do_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentBinding = FragmentEditToDoListBinding.bind(view);
        auth = FirebaseAuth.getInstance();
        myDialog = new Dialog(getContext());

        toDoListViewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);
        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        System.out.println(getArguments().getBoolean("isFavorite"));

        if (getArguments().getBoolean("isFavorite") == true){
            favoritesViewModel.getToDoByIdFromFirebase(getContext(),getArguments().getString("toDoId"));
            fragmentBinding.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String color = "#FFFFFFFF";
                    Drawable background = fragmentBinding.relativeLayout.getBackground();
                    if (background instanceof ColorDrawable){
                        color = String.valueOf(((ColorDrawable) background).getColor());
                    }
                    ToDo toDo = new ToDo(fragmentBinding.toDoTitleText.getText().toString(),fragmentBinding.toDoContentText.getText().toString(),
                            auth.getCurrentUser().getEmail(), Timestamp.now(),color);
                    favoritesViewModel.updateToDo(getArguments().getString("toDoId"),toDo,getContext(),v);
                }
            });
            observeFavorite();
        }else {
            toDoListViewModel.getToDoByIdFromFirebase(getContext(),getArguments().getString("toDoId"));


            fragmentBinding.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String color = "#FFFFFFFF";
                    Drawable background = fragmentBinding.relativeLayout.getBackground();
                    if (background instanceof ColorDrawable){
                        color = String.valueOf(((ColorDrawable) background).getColor());
                    }
                    ToDo toDo = new ToDo(fragmentBinding.toDoTitleText.getText().toString(),fragmentBinding.toDoContentText.getText().toString(),
                            auth.getCurrentUser().getEmail(), Timestamp.now(),color);
                    toDoListViewModel.updateToDo(getArguments().getString("toDoId"),toDo,getContext(),v);
                }
            });
            observeToDo();
        }



    }
    public void showColorsPopup(){

        List<String> colorList = new ArrayList();
        colorList.addAll(Arrays.asList(new String[]{
                "#FFD2C8","#1DE9B6",
                "#FF00FF00","#D500F9",
                "#00E5FF","#FF9100",
                "#FFFFFF00","#FFFFFFFF",
                "#FF888888","#FFCCCCCC"}));
        myDialog.setContentView(R.layout.custom_popup);
        RecyclerView colorRecyclerView = myDialog.findViewById(R.id.colorsRecyclerView);
        TextView closeText = myDialog.findViewById(R.id.closeTextView);
        ColorRecyclerAdapter adapter = new ColorRecyclerAdapter(colorList,myDialog,fragmentBinding.relativeLayout);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3, GridLayoutManager.VERTICAL,false);
        colorRecyclerView.setLayoutManager(gridLayoutManager);
        colorRecyclerView.setAdapter(adapter);

        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    public void observeFavorite(){
        favoritesViewModel.toDo.observe(getViewLifecycleOwner(), new Observer<ToDo>() {
            @Override
            public void onChanged(ToDo toDo) {
                if (toDo!=null){
                    fragmentBinding.relativeLayout.setVisibility(View.VISIBLE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.GONE);
                    fragmentBinding.errorMessageText.setVisibility(View.GONE);
                    fragmentBinding.relativeLayout.setBackgroundColor(Integer.parseInt(toDo.getColor()));
                    fragmentBinding.toDoTitleText.setText(toDo.getToDoTitle());
                    fragmentBinding.toDoContentText.setText(toDo.getToDoContent());
                }
            }
        });

        favoritesViewModel.toDoLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.relativeLayout.setVisibility(View.GONE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.VISIBLE);
                    fragmentBinding.errorMessageText.setVisibility(View.GONE);
                }else {
                    fragmentBinding.loadingProgressBar.setVisibility(View.GONE);
                }
            }
        });

        favoritesViewModel.errorMessage.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.errorMessageText.setVisibility(View.VISIBLE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.GONE);
                    fragmentBinding.relativeLayout.setVisibility(View.GONE);
                }else {
                    fragmentBinding.errorMessageText.setVisibility(View.GONE);
                }
            }
        });
    }

    public void observeToDo(){
        toDoListViewModel.toDo.observe(getViewLifecycleOwner(), new Observer<ToDo>() {
            @Override
            public void onChanged(ToDo toDo) {
                if (toDo!=null){
                    fragmentBinding.relativeLayout.setVisibility(View.VISIBLE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.GONE);
                    fragmentBinding.errorMessageText.setVisibility(View.GONE);
                    fragmentBinding.relativeLayout.setBackgroundColor(Integer.parseInt(toDo.getColor()));
                    fragmentBinding.toDoTitleText.setText(toDo.getToDoTitle());
                    fragmentBinding.toDoContentText.setText(toDo.getToDoContent());
                }
            }
        });

        toDoListViewModel.toDoLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.relativeLayout.setVisibility(View.GONE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.VISIBLE);
                    fragmentBinding.errorMessageText.setVisibility(View.GONE);
                }else {
                    fragmentBinding.loadingProgressBar.setVisibility(View.GONE);
                }
            }
        });

        toDoListViewModel.errorMessage.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    fragmentBinding.errorMessageText.setVisibility(View.VISIBLE);
                    fragmentBinding.loadingProgressBar.setVisibility(View.GONE);
                    fragmentBinding.relativeLayout.setVisibility(View.GONE);
                }else {
                    fragmentBinding.errorMessageText.setVisibility(View.GONE);
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