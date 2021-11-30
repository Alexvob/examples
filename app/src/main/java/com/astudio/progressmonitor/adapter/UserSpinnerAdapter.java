package com.astudio.progressmonitor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.astudio.progressmonitor.user.User;

import java.util.List;
import java.util.Objects;

public class UserSpinnerAdapter extends ArrayAdapter<User> {


    private List<User> users;


    public UserSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        this.users = objects;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
        return initView(position, convertView, parent);
    }


    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
    {   // This view starts when we click the spinner.
        return initView(position, convertView, parent);
    }


    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    //.inflate(android.R.layout.simple_list_item_2, parent, false);
                    .inflate(android.R.layout.simple_list_item_2, null);
        }

        User user = users.get(position);

        ((TextView) convertView.findViewById(android.R.id.text1))
                .setText(Objects.requireNonNull(user).getName());
        return convertView;
    }


}
