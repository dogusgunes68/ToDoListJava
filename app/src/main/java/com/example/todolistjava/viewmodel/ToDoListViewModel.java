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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

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
    private ArrayList<ToDo> tempToDoList = new ArrayList<>();
    private ToDo tempToDo;

    public void showToDoList(ArrayList<ToDo> takenToDoList){
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

    public void  deleteToDo(String id,Context context){
        firestore.collection("ToDoList").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //getToDoListFromFirebase(context);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addToDoToFirebase(ToDo toDo, Context context){
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorMessage.setValue(true);
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getToDoByIdFromFirebase(Context context,String toDoId){
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
                                (Timestamp) value.get("toDoDate"),
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

        firestore.collection("ToDoList").whereEqualTo("toDoUserEmail",firebaseAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            ToDo toDo = new ToDo(document.get("toDoTitle").toString(),
                                    document.get("toDoContent").toString(),
                                    document.get("toDoUserEmail").toString(),
                                    (Timestamp) document.get("toDoDate"),
                                    document.get("toDoColor").toString());
                            toDo.setId(document.getId());
                            tempToDoList.add(toDo);
                        }
                    }else {
                        Toast.makeText(context,"Empty List",Toast.LENGTH_LONG).show();
                    }
                    showToDoList(tempToDoList);
                    tempToDoList.clear();

                }else {
                    errorMessage.setValue(true);
                    toDoLoading.setValue(false);
                    Toast.makeText(context,"there is an error",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void updateToDo(String toDoId,ToDo todo, Context context, View view){

        firestore.collection("ToDoList").document(toDoId).set(todo, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Saccessfuly!!!",Toast.LENGTH_LONG).show();
                Navigation.findNavController(view).navigate(R.id.action_editToDoListFragment_to_toDoListFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }







}
