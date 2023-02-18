package com.example.tabipapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tabipapp.R;
import com.example.tabipapp.UI.TriyajActivity;
import com.example.tabipapp.Model.TriyajBilgileri;

import java.util.List;


public class DurumAdapter2 extends RecyclerView.Adapter<DurumAdapter2.MyView> {

    // List with String type
    private List<TriyajBilgileri> list;
    Context context;

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

    public DurumAdapter2(Context context, List<TriyajBilgileri> horizontalList, String ucusNo) {
        this.list = horizontalList;
        this.durum = durum;
        this.context = context;
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
        }  else  {
             itemView
                    = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_durum3,
                            parent,
                            false);
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TriyajActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(i);
            }
        });

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
        holder.textView.setText(durum);
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return list.size();
    }
}
