package com.example.vinayak.group57_inclass10;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesList extends Fragment {

    private OnFragmentInteractionListener mListener;
    ListView lv;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId = null;
    private ImageButton imageAdd;
    private Button logoutButton;
    TextView noExp;

    private ArrayList<Expense> expensesList;
    public ExpensesList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expenses_list, container, false);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignupInteraction();
        void onAddButtonClicked();
        void onCancelButtonClicked();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("demo","On activityCreated");
        lv = (ListView) getActivity().findViewById(R.id.listView);
        imageAdd = (ImageButton) getActivity().findViewById(R.id.imageAdd);
        logoutButton = (Button) getActivity().findViewById(R.id.buttonLogout);
        noExp = (TextView) getActivity().findViewById(R.id.textViewExp);
        mAuth = FirebaseAuth.getInstance();
        expensesList = new ArrayList<Expense>();

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAddButtonClicked();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser !=null) {
            Log.d("firebaseUser","Inside user");
            userId = firebaseUser.getUid();
            final DatabaseReference expenses = mDatabase.child("expenses");
            if(expensesList.size()==0){
                noExp.setVisibility(View.VISIBLE);
            }

            final ExpenseListDataAdapter adapter = new ExpenseListDataAdapter(getActivity(), R.layout.item_row_layout, expensesList);
            lv.setAdapter(adapter);

            expenses.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Expense expense = dataSnapshot.getValue(Expense.class);
                    expense.setId(dataSnapshot.getKey());
                    if(expense.getUser()!=null && expense.getUser().equals(mAuth.getCurrentUser().getEmail())){
                        expensesList.add(expense);
                        noExp.setVisibility(View.INVISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }
                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }
                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }
                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment f = new ExpenseDetails();
                    android.app.FragmentManager f1 = getFragmentManager();
                    Expense e = expensesList.get(position);
                    FragmentTransaction ft = f1.beginTransaction();
                    Bundle args = new Bundle();
                    args.putSerializable("expense", e);
                    f.setArguments(args);
                    ft.replace(R.id.container, f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Expense e = expensesList.get(position);
                    Log.d("KEY in expense",e.toString());
                    DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("expenses").child(e.getId());

                    mref.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            Toast.makeText(getActivity(), "Expense deleted successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                    expensesList.remove(e);
                    adapter.notifyDataSetChanged();
                    if(expensesList.size()==0){
                        noExp.setVisibility(View.VISIBLE);
                    }
                    return true;
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    mListener.onSignupInteraction();
                }
            });
        }
    }
}
