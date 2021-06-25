package com.moises.tarifa.proyectote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class ProdAdapter extends RecyclerView.Adapter<ProdAdapter.MyViewHolder> {

    List<Productos> listProductos;
    Context cont;
    MyItemClickListener<Productos> itemClickListener;

    public ProdAdapter(List<Productos> listProductos, Context cont) {
        this.listProductos = listProductos;
        this.cont = cont;
    }
    public void insertItem(Productos prod){
        int posicion = listProductos.size();
        listProductos.add(prod);
        notifyItemInserted(posicion);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_mis_publicaciones, parent, false);
        return new MyViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Productos prodActual = listProductos.get(position);
        holder.getTxtNombre().setText(prodActual.getNombre());
        holder.getTxtPrecio().setText(prodActual.getPrecio());
        Glide
                .with(cont)
                .load(prodActual.getImagen())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(holder.getImagen());
        holder.getBtnModificar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemClickSelected(v,prodActual,position);
                }
            }
        });
        holder.getBtnEliminar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemClickSelected(v,prodActual,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProductos.size();
    }
    public void clear() {
        for(int i = listProductos.size()-1; i>=0; i--){
            notifyItemRemoved(i);
        }
        listProductos.clear();
    }
    public void addAll(List<Productos> listBuses) {
        this.listProductos = listProductos;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre;
        TextView txtPrecio;
        ImageView imagen;
        Button btnModificar;
        Button btnEliminar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtNombre = itemView.findViewById(R.id.textNombre);
            this.txtPrecio = itemView.findViewById(R.id.textPrecio);
            this.imagen = itemView.findViewById(R.id.imgPortada);
            this.btnModificar = itemView.findViewById(R.id.btnModificar);
            this.btnEliminar = itemView.findViewById(R.id.btnEliminar);

        }

        public Button getBtnModificar() {
            return btnModificar;
        }

        public void setBtnModificar(Button btnModificar) {
            this.btnModificar = btnModificar;
        }

        public Button getBtnEliminar() {
            return btnEliminar;
        }

        public void setBtnEliminar(Button btnEliminar) {
            this.btnEliminar = btnEliminar;
        }

        public TextView getTxtNombre() {
            return txtNombre;
        }

        public void setTxtNombre(TextView txtNombre) {
            this.txtNombre = txtNombre;
        }

        public TextView getTxtPrecio() {
            return txtPrecio;
        }

        public void setTxtPrecio(TextView txtPrecio) {
            this.txtPrecio = txtPrecio;
        }

        public ImageView getImagen() {
            return imagen;
        }

        public void setImagen(ImageView imagen) {
            this.imagen = imagen;
        }
    }

    public void setItemClickListener(MyItemClickListener<Productos> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
