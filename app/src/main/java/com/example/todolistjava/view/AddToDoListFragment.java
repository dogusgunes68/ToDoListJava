package com.example.todolistjava.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistjava.R;
import com.example.todolistjava.adapter.ColorRecyclerAdapter;
import com.example.todolistjava.adapter.WarningReyclerAdapter;
import com.example.todolistjava.databinding.FragmentAddToDoListBinding;
import com.example.todolistjava.model.ToDo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

        fragmentBinding.warningImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWarningPopup();
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

    public void showWarningPopup(){
        HashMap<String, String> warningList = new HashMap<>();
        warningList.put("Very Important","#DF0808");
        warningList.put("Important","#DDCB00");
        warningList.put("Not Neccessary","#5EC606");

        List<String> keys = new ArrayList<>();

        for (String key : warningList.keySet()){
            keys.add(key);
        }

        myDialog.setContentView(R.layout.warning_popup);
        RecyclerView warningRecyclerView = myDialog.findViewById(R.id.warningRecyclerView);
        TextView closeText = myDialog.findViewById(R.id.warningCloseTextView);
        WarningReyclerAdapter adapter = new WarningReyclerAdapter(warningList,keys,fragmentBinding.warningImageView,myDialog);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        warningRecyclerView.setLayoutManager(linearLayoutManager);
        warningRecyclerView.setAdapter(adapter);

        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }
}