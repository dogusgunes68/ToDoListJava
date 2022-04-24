package com.example.todolistjava.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.todolistjava.model.ToDo;
import com.example.todolistjava.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ToDoListViewModel extends ViewModel {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MutableLiveData<ArrayList<ToDo>> toDoList;
    private ArrayList<ToDo> list;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public void addUserOnFirebase(User user, Context context){

        HashMap<String,String> userMap = new HashMap<>();

        userMap.put("username",user.getName());
        userMap.put("usersurname",user.getSurname());
        userMap.put("useremail",user.getEmail());

        firestore.collection("Users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context,"REGISTERED",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void addToDoOnFirebase(ToDo toDo,Context context){
        firestore.collection("ToDoList").add(toDo).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context,"Successfuly",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getToDoListFromFirebase(Context context){

        firestore.collection("ToDoList").whereEqualTo("useremail",firebaseAuth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                }else {
                    if (!value.getDocuments().isEmpty()){
                        for (Object document : value.getDocuments().toArray()){
                            list.add((ToDo) document);
                        }
                    }else {
                        Toast.makeText(context,"Empty List",Toast.LENGTH_LONG).show();
                    }
                }
                toDoList.setValue(list);
                list.clear();
            }
        });

    }

}
