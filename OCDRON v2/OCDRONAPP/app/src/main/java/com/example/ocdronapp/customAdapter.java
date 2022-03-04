package com.example.ocdronapp;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customAdapter extends RecyclerView.Adapter<customAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList id, phLevel,phScale,date,time;

    Animation translate_anim;

    customAdapter(Activity activity, Context context, ArrayList id, ArrayList phLevel, ArrayList phScale, ArrayList  date, ArrayList time){
        this.activity = activity;
        this.context = context;
        this.id = id;
        this.phLevel = phLevel;
        this.phScale = phScale;
        this.date = date;
        this.time = time;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.phdatacard,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.id_txt.setText(String.valueOf(id.get(position)));
        holder.phLevel_txt.setText(String.valueOf(phLevel.get(position)));
        holder.phScale_txt.setText(String.valueOf(phScale.get(position)));
        holder.date_txt.setText(String.valueOf(date.get(position)));
        holder.time_txt.setText(String.valueOf(time.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id_txt, phLevel_txt, phScale_txt,date_txt,time_txt;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_txt = itemView.findViewById(R.id.id);
            phLevel_txt = itemView.findViewById(R.id.phlevel);
            phScale_txt = itemView.findViewById(R.id.phScale);
            date_txt = itemView.findViewById(R.id.date);
            time_txt = itemView.findViewById(R.id.time);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
