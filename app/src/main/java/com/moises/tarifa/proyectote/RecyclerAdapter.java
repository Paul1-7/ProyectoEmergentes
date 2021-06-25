package com.moises.tarifa.proyectote;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moises.tarifa.proyectote.Productos;
import com.moises.tarifa.proyectote.Productos;
import com.moises.tarifa.proyectote.R;

import java.util.ArrayList;

public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements View.OnClickListener{
    private static final String Tag = "recyclerView";

    private Context mContext;
    private ArrayList<Productos> list ;

    //porpiedad listener para el click
    private View.OnClickListener listener;

    public RecyclerAdapter(Context mContext, ArrayList<Productos> list) {
        this.mContext = mContext;
        if(list == null){
            this.list = new ArrayList<Productos>();
        }else
            this.list = list;
    }

    public void insertItem(Productos productos){
        int posicion = 0;
        if(!list.isEmpty())
            posicion = list.size();
        list.add(productos);
        notifyItemInserted(posicion);
    }



    public void updateItem(Productos productos,int position){
        list.set(position,productos);
        notifyItemChanged(position);
    }

    public void elminarItem(Productos productos,int position){
        list.remove(productos);
        notifyItemRemoved(position);
    }

    public void filtrar(ArrayList<Productos> filtroUsuarios){
        this.list = filtroUsuarios;
        notifyDataSetChanged();
    }


    //*************************************************************************** click
    public void setOnclickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    //implementacion de onlick para las fotos y el mapita
    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);


        }
    }
    //***************************************************************************

    public class  ViewHolder extends RecyclerView.ViewHolder {
        //widgets
        ImageView imageView;
        TextView textView, textView2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textViewy);
            textView2 = itemView.findViewById(R.id.textView2);

        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);


        //escuchador
        view.setOnClickListener(this);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getNombre());
        holder.textView2.setText(list.get(position).getPrecio());

        //Glide
        Glide.with(mContext)
                .load(list.get(position).getImagen())
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}