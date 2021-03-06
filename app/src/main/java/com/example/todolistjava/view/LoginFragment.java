package com.example.todolistjava.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.todolistjava.R;
import com.example.todolistjava.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding fragmentBinding;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentBinding = FragmentLoginBinding.bind(view);

        fragmentBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(v);
            }
        });

        fragmentBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterFragment(v);
            }
        });

        if (firebaseAuth.getCurrentUser() != null){
            Navigation.findNavController(this.getView()).navigate(R.id.action_loginFragment_to_toDoListFragment);
        }

    }

    public void login(View view){

        String email = fragmentBinding.emailText.getText().toString();
        String password = fragmentBinding.passwordText.getText().toString();
        if (email!=""||password!=""){
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    //go to to do list fragment
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_toDoListFragment);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(getContext(),"Fields cant be empty",Toast.LENGTH_LONG).show();
        }

    }

    public void goToRegisterFragment(View view){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentBinding = null;
    }

}