package com.example.vinayak.group57_inclass10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vinayak on 11/7/2016.
 */
public class ExpenseListDataAdapter extends ArrayAdapter<Expense> {
    List<Expense> mData;
    Context mContext;
    int mResource;

    public ExpenseListDataAdapter(Context context, int resource, List<Expense> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        final Expense expense = mData.get(position);

        TextView venueNameTextView = (TextView) convertView.findViewById(R.id.expenseNameTextView);
        venueNameTextView.setText(expense.getName());

        TextView categoryNameTextView = (TextView) convertView.findViewById(R.id.expenseAmountTextView);
        categoryNameTextView.setText("$ "+expense.getAmount());

        return convertView;
    }
}
