package com.example.vinayak.group57_inclass10;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ExpenseDetails extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ExpenseDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_details, container, false);
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
        Expense e = (Expense) getArguments().getSerializable("expense");

        TextView name,date,category,price;
        Button closeBtn;
        name = (TextView) getActivity().findViewById(R.id.detailNameId);
        date = (TextView) getActivity().findViewById(R.id.detailDateId);
        category = (TextView) getActivity().findViewById(R.id.detailCategoryId);
        price = (TextView) getActivity().findViewById(R.id.detailAmountId);
        closeBtn = (Button) getActivity().findViewById(R.id.buttonClose);

        name.setText(e.getName());
        date.setText(e.getDate());
        category.setText(e.getCategory());
        price.setText(e.getAmount());

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction();
    }
}
