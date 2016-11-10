package com.example.vinayak.group57_inclass10;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginActivity extends Fragment {
    EditText eEmail,ePwd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private OnLoginFragmentInteractionListener mListener;
    public static String TAG = "Login Status";

    public LoginActivity() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        eEmail = (EditText) getActivity().findViewById(R.id.editTextEmail);
        ePwd = (EditText) getActivity().findViewById(R.id.editTextPwd);
        mAuth = FirebaseAuth.getInstance();
        Button buttonLogin= (Button) getActivity().findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogIn(v);
            }
        });



        Button buttonCreate =(Button) getActivity().findViewById(R.id.buttonCreate);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                onCreateAcc(v);
            }
        });
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onLogIn(View view){
        String email = eEmail.getText().toString();
        String pwd = ePwd.getText().toString();
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            mListener.goToExpenseList();
                        }else{
                            Toast.makeText(getActivity(), "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = eEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            eEmail.setError("Required.");
            valid = false;
        } else {
            eEmail.setError(null);
        }

        String password = ePwd.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ePwd.setError("Required.");
            valid = false;
        } else {
            ePwd.setError(null);
        }

        return valid;
    }

    public void onCreateAcc(View view){
        mListener.onClickSignUp();
    }

    public interface OnLoginFragmentInteractionListener {
        void onClickSignUp();
        void goToExpenseList();
    }
}
