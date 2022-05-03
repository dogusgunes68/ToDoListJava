package com.example.todolistjava.view;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todolistjava.R;
import com.example.todolistjava.adapter.ColorRecyclerAdapter;
import com.example.todolistjava.databinding.FragmentAddToDoListBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddToDoListFragment extends Fragment {

    private FragmentAddToDoListBinding fragmentBinding;
    Dialog myDialog;

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

        fragmentBinding.colorsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorsPopup();
            }
        });

    }

    public void showColorsPopup(){

        List<String> colorList = new ArrayList();
        colorList.addAll(Arrays.asList(new String[]{
                "#FFFF0000","#1DE9B6",
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


}