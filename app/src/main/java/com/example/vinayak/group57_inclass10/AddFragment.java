package com.example.vinayak.group57_inclass10;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private DatabaseReference mDatabase;
    Spinner categories;
    String currentCategory;
    Expense expense;
    String user;
    private FirebaseAuth mAuth;
    private Button cancelButton;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cancelButton = (Button) getActivity().findViewById(R.id.buttonCnl);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser fireUser = mAuth.getCurrentUser();
        if(fireUser!=null){
            user = fireUser.getEmail();
            addExpence(getView());
        }else{

        }
    }

    public void addExpence(View view){
        categories = (Spinner) getActivity().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerData = ArrayAdapter.createFromResource(getActivity(),R.array.category_spinner,android.R.layout.simple_spinner_item);
        spinnerData.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        categories.setAdapter(spinnerData);
        categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        currentCategory = "Groceries";
                        break;
                    case 1:
                        currentCategory = "Invoice";
                        break;
                    case 2:
                        currentCategory = "Transportation";
                        break;
                    case 3:
                        currentCategory = "Shopping";
                        break;
                    case 4:
                        currentCategory = "Rent";
                        break;
                    case 5:
                        currentCategory = "Trips";
                        break;
                    case 6:
                        currentCategory = "Utilities";
                        break;
                    case 7:
                        currentCategory = "Others";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button addButton= (Button) getActivity().findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExpence(v);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelButtonClicked();
            }
        });
    }

    public void createExpence(View view){
        TextView expName = (TextView)getActivity().findViewById(R.id.expense_name);
        TextView expAmt = (TextView)getActivity().findViewById(R.id.amount);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String d = sdf.format(date);

        expense = new Expense();

        expense.setUser(user);
        expense.setName(expName.getText().toString());
        expense.setDate(d);
        expense.setCategory(currentCategory);
        expense.setAmount(expAmt.getText().toString());
        Log.d("create",expense.toString());

        if(validateExpence()){
            String key = mDatabase.child("expenses").push().getKey();
            expense.setId(key);
            mDatabase.child("expenses").child(key).child("name").setValue(expense.getName());
            mDatabase.child("expenses").child(key).child("date").setValue(expense.getDate());
            mDatabase.child("expenses").child(key).child("category").setValue(expense.getCategory());
            mDatabase.child("expenses").child(key).child("amount").setValue(expense.getAmount());
            mDatabase.child("expenses").child(key).child("user").setValue(expense.getUser());

            mListener.onAddExpenseFragmentInteraction(expense);
        }
    }

    public boolean validateExpence(){
        if(expense.getName().length()<=0){
            Toast.makeText(getActivity(), "Expense name shouldn't be empty", Toast.LENGTH_SHORT).show();return false;
        }
        else if(expense.getAmount().length()<=0){
            Toast.makeText(getActivity(),"Expense amount shouldn't be empty",Toast.LENGTH_SHORT).show();return false;
        }
        else if(expense.getCategory().length()<=0){
            Toast.makeText(getActivity(),"Expense category shouldn't be empty",Toast.LENGTH_SHORT).show();return false;
        }
        return true;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onAddExpenseFragmentInteraction(Expense expense);
        void onCancelButtonClicked();
    }
}
