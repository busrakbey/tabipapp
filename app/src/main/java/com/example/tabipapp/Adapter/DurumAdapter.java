package com.example.tabipapp.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tabipapp.R;

import java.util.List;


public class DurumAdapter extends RecyclerView.Adapter<DurumAdapter.MyView> {

    // List with String type
    private List<Integer> list;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        TextView textView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);

            // initialise TextView with id
            textView = (TextView) view
                    .findViewById(R.id.textview);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    String durum = "";

    public DurumAdapter(List<Integer> horizontalList, String durum) {
        this.list = horizontalList;
        this.durum = durum;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType) {

        // Inflate item.xml using LayoutInflator
        View itemView = null;
        if (durum.equalsIgnoreCase("1")) {
             itemView
                    = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_durum,
                            parent,
                            false);
        } else  {
             itemView
                    = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_durum3,
                            parent,
                            false);
        }



        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position) {

        // Set the text of each item of
        // Recycler view with the list items
      //  holder.textView.setImageResource(list.get(position));
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return list.size();
    }
}
