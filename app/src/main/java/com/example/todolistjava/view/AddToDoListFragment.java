package com.example.todolistjava.view;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todolistjava.R;
import com.example.todolistjava.adapter.ColorRecyclerAdapter;
import com.example.todolistjava.databinding.FragmentAddToDoListBinding;
import com.example.todolistjava.model.ToDo;
import com.example.todolistjava.viewmodel.ToDoListViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddToDoListFragment extends Fragment {

    private FragmentAddToDoListBinding fragmentBinding;
    Dialog myDialog;
    private ToDoListViewModel viewModel;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_to_do_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentBinding = FragmentAddToDoListBinding.bind(view);
        myDialog = new Dialog(getContext());

        firebaseAuth = FirebaseAuth.getInstance();

        viewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);

        fragmentBinding.colorsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorsPopup();
            }
        });

        fragmentBinding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDo(v);
            }
        });

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

    public void addToDo(View view){
        String toDoTitle = fragmentBinding.toDoTitleText.getText().toString();
        String toDoContent = fragmentBinding.toDoContentText.getText().toString();
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        String date = Timestamp.now().toDate().toString();

        String color = "#FFFFFFFF";
        Drawable background = fragmentBinding.relativeLayout.getBackground();
        if (background instanceof ColorDrawable)
            color = String.valueOf(((ColorDrawable) background).getColor());

        ToDo toDo = new ToDo(toDoTitle,toDoContent,userEmail,date,color);

        viewModel.addToDoToFirebase(toDo,getContext(),view);
        Navigation.findNavController(view).navigate(R.id.action_addToDoListFragment_to_toDoListFragment);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentBinding = null;
    }


}