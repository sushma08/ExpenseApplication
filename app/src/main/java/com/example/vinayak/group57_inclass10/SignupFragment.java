package com.example.vinayak.group57_inclass10;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SignupFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private EditText editTextName,editTextEmail,editTextPass;
    private Button buttonSignup, buttonCancel;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("Demo","AFragment: OnAttach");
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "should implement IFragmentTextChanged");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editTextEmail = (EditText) getActivity().findViewById(R.id.etSignupEmail);
        editTextName = (EditText) getActivity().findViewById(R.id.editTextName);
        editTextPass = (EditText) getActivity().findViewById(R.id.etSignupPwd);
        buttonSignup = (Button) getActivity().findViewById(R.id.buttonSignup);
        buttonCancel = (Button) getActivity().findViewById(R.id.buttonCancel);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignup();
            }
        });


    }

    private void onSignup() {
        final String name = editTextName.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String password = editTextPass.getText().toString();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Log.d("demo", "In OnSignup " + task.isSuccessful());
                            Toast.makeText(getActivity(), "User Signed up: " + name, Toast.LENGTH_SHORT).show();
                            User user = new User();
                            user.setFullName(name);
                            user.setEmail(email);
                            user.setPassword(password);
                            String uid = mAuth.getCurrentUser().getUid();
                            mDatabase.child("users").child(uid).child("fullname").setValue(user.getFullName());
                            mDatabase.child("users").child(uid).child("email").setValue(user.getEmail());
                            mDatabase.child("users").child(uid).child("password").setValue(user.getPassword());
                            mListener.onSignupInteraction();
                        } else {
                            Log.d("demo", task.getException().toString());
                        }
                    }
                });
    }

    public interface OnFragmentInteractionListener {
        void onSignupInteraction();
    }
}
