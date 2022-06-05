package com.example.todolistjava.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.todolistjava.R;
import com.example.todolistjava.databinding.FragmentRegisterBinding;
import com.example.todolistjava.model.User;
import com.example.todolistjava.viewmodel.ToDoListViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding fragmentBinding;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    ToDoListViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentBinding = FragmentRegisterBinding.bind(view);

        viewModel = ViewModelProviders.of(this).get(ToDoListViewModel.class);

        fragmentBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });

        fragmentBinding.haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });


    }

    public void addUser(String name,String surname,String email){

        //create user collection
        User user = new User(name,surname,email);
        viewModel.addUserOnFirebase(user,getContext());

    }

    public void register(View view){


        firebaseAuth = FirebaseAuth.getInstance();
        String name = fragmentBinding.nameText.getText().toString();
        String surName = fragmentBinding.surnameText.getText().toString();
        String email = fragmentBinding.emailText.getText().toString();
        String password = fragmentBinding.passwordText.getText().toString();
        String confirmPassword = fragmentBinding.confirmPasswordText.getText().toString();
        if (password.equals(confirmPassword)) {

            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
                    addUser(name, surName, email);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(getContext(),"Passwords didn't matched",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentBinding = null;
    }
}