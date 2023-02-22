package com.example.tabipapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tabipapp.R;
import com.example.tabipapp.UI.TriyajActivity;
import com.example.tabipapp.Model.TriyajBilgileri;
import com.example.tabipapp.UI.VideoCallActivity;

import java.util.List;


public class TriyajAdapter extends RecyclerView.Adapter<TriyajAdapter.MyViewHolder> {

    private List<TriyajBilgileri> data;
    private Context mContext;


    public TriyajAdapter(Context mContext, List<TriyajBilgileri> data) {
        this.data = data;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_durum, parent, false);
        return new TriyajAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final TriyajBilgileri myListData = data.get(position);
        holder.ucus_no.setText(myListData.getUcusNo() + "\n" + myListData.getKoltukNo());
        if (myListData.getTriyajDurum() != null && myListData.getTriyajDurum().equalsIgnoreCase("Kırmızı"))
            holder.layout.setBackgroundResource(R.drawable.kirmizi_yuvarlak_cerceve);
        else if (myListData.getTriyajDurum() != null && myListData.getTriyajDurum().equalsIgnoreCase("Yeşil"))
            holder.layout.setBackgroundResource(R.drawable.yesil_yuvarlak_cerceve);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, TriyajActivity.class);
                i.putExtra("id", myListData.getId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(i);
            }
        });


        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(mContext, VideoCallActivity.class);
                i.putExtra("roomName" , myListData.getUcusNo());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(i);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(TriyajBilgileri item, int position) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    public List<TriyajBilgileri> getData() {
        return data;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        public TextView ucus_no;
        CardView cardView;
        LinearLayout layout;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.ucus_no = (TextView) itemView.findViewById(R.id.textview);
            this.cardView = (CardView) itemView.findViewById(R.id.cardview);
            layout = (LinearLayout) itemView.findViewById(R.id.linear);

        }
    }


}
