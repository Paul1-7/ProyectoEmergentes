package com.moises.tarifa.proyectote;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String Tag = "RecyclerView";

    private Context mContext;
    private ArrayList<Messages> list;


    public RecyclerAdapter(Context mContext, ArrayList<Messages> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        //widgets
        ImageView imageView;
        TextView textView, textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textViewy);

        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
       return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getName());

        //Glide
        Glide.with(mContext)
                .load(list.get(position).getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
