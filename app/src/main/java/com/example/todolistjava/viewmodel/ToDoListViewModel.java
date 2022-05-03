package com.example.todolistjava.viewmodel;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.todolistjava.R;
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
import java.util.List;

public class ToDoListViewModel extends AndroidViewModel{

    public ToDoListViewModel(Application application){
        super(application);

    }

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public MutableLiveData<List<ToDo>> toDoList = new MutableLiveData<>();
    public MutableLiveData<ToDo> toDo = new MutableLiveData<>();
    public MutableLiveData<Boolean> toDoLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> errorMessage = new MutableLiveData<>();
    private List<ToDo> tempToDoList = new ArrayList<>();
    private ToDo tempToDo;

    public void showToDoList(List<ToDo> takenToDoList){
        toDoList.setValue(takenToDoList);
        toDoLoading.setValue(false);
        errorMessage.setValue(false);

    }

    public void showToDo(ToDo takenToDo){
        toDo.setValue(takenToDo);
        toDoLoading.setValue(false);
        errorMessage.setValue(false);
    }

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

    public void addToDoOnFirebase(ToDo toDo, Context context, View view){
        HashMap<String,Object> toDoMap = new HashMap<>();
        toDoMap.put("toDoTitle",toDo.getToDoTitle());
        toDoMap.put("toDoContent",toDo.getToDoContent());
        toDoMap.put("toDoDate",toDo.getDate());
        toDoMap.put("toDoUserEmail",toDo.getUserEmail());
        toDoMap.put("toDoColor",toDo.getColor());
        firestore.collection("ToDoList").add(toDoMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context,"Successfuly",Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).navigate(R.id.action_addToDoListFragment_to_toDoListFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorMessage.setValue(true);
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getToDoByIdFromFirebase(Context context,Long toDoId){
        toDoLoading.setValue(true);

        firestore.collection("ToDoList").document(String.valueOf(toDoId)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                    errorMessage.setValue(true);
                    toDoLoading.setValue(false);
                }else {
                    if (value != null){
                        tempToDo = new ToDo(
                                value.get("toDoTitle").toString(),
                                value.get("toDoContent").toString(),
                                value.get("toDoUserEmail").toString(),
                                value.get("toDoDate").toString(),
                                value.get("toDoColor").toString());

                    }else {
                        Toast.makeText(context,"Empty List",Toast.LENGTH_LONG).show();
                    }
                    showToDo(tempToDo);
                    tempToDo = new ToDo();
                }
            }
        });
    }

    public void getToDoListFromFirebase(Context context){

        toDoLoading.setValue(true);

        firestore.collection("ToDoList").whereEqualTo("toDoUserEmail",firebaseAuth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null){
                    errorMessage.setValue(true);
                    toDoLoading.setValue(false);
                    Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                }else {
                    if (!value.getDocuments().isEmpty()){
                        for (Object document : value.getDocuments().toArray()){
                            tempToDoList.add((ToDo) document);

                        }
                    }else {
                        Toast.makeText(context,"Empty List",Toast.LENGTH_LONG).show();
                    }
                    showToDoList(tempToDoList);
                    tempToDoList.clear();
                }


            }
        });

    }


}
