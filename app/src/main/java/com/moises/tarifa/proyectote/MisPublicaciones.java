package com.moises.tarifa.proyectote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MisPublicaciones extends AppCompatActivity {
    List<Productos> listPublicaciones;
    ProdAdapter adapter;
    DatabaseReference myRef;
    //RecyclerView.LayoutManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_publicaciones);

        BottomNavigationView btnNav = findViewById(R.id.bottomNav);
        //manager = new GridLayoutManager(this,2);
        btnNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itenSelect = item.getItemId();
                if(itenSelect == R.id.itemadd){
                    Intent intent = new Intent(MisPublicaciones.this, FormPublicacion.class);
                    startActivity(intent);
                }else if(itenSelect == R.id.itembuscar){
                    Intent intent = new Intent(MisPublicaciones.this, MainActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });


        //configurar con la base de dates firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("productos");

        listPublicaciones= new ArrayList<Productos>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new ProdAdapter(listPublicaciones,getApplicationContext());
        recyclerView.setAdapter(adapter);

       /* myRef.child("productos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    listPublicaciones.clear();
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String img = ds.child("imagen").getValue().toString();
                        String nombre = ds.child("nombre").getValue().toString();
                        String precio = ds.child("precio").getValue().toString();
                        listPublicaciones.add(new Productos(nombre,precio,img));
                    }
                    adapter = new ProdAdapter(listPublicaciones,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });*/


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for(DataSnapshot snap: snapshot.getChildren()){
                    Productos prod = snap.getValue(Productos.class);
                    adapter.insertItem(prod);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.setItemClickListener(new MyItemClickListener<Productos>() {
            @Override
            public void onItemClickSelected(View vista, Productos itemSel, int posicion) {
                if(vista.getId() == R.id.btnModificar){
                    mostrarFormModificar(vista,itemSel,posicion);
                }else if(vista.getId() == R.id.btnEliminar){
                    new MaterialAlertDialogBuilder(vista.getContext()).setTitle("ELIMINAR LA PUBLICACION SELECCIONADA")
                            .setMessage("¿Esta seguro de eliminar la publicacion seleccionada?")
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //myRef.child(itemSel.getId()).removeValue();
                                    Toast.makeText(getApplicationContext(),"elimina",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void mostrarFormModificar(View vista, Productos itemSel, int posicion) {

        View formProd = LayoutInflater.from(vista.getContext()).inflate(R.layout.activity_form_publicacion, null);

        TextInputEditText txtNombreProducto= formProd.findViewById(R.id.txtProducto);
        TextInputEditText txtPrecio= formProd.findViewById(R.id.txtPrecio);
        TextInputEditText txtDescripcion= formProd.findViewById(R.id.txtDescripcion);
        TextInputEditText txtImagen= formProd.findViewById(R.id.txtImagen);

        txtNombreProducto.setText(itemSel.getNombre());
        txtPrecio.setText(itemSel.getPrecio());
        txtDescripcion.setText(itemSel.getDescripcion());
        txtImagen.setText(itemSel.getImagen());;

        new MaterialAlertDialogBuilder(vista.getContext()).setTitle("Modificar Datos de la publicacion")
                .setView(formProd)
                .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextInputEditText txtNombreProducto= formProd.findViewById(R.id.txtProducto);
                        TextInputEditText txtPrecio= formProd.findViewById(R.id.txtPrecio);
                        TextInputEditText txtDescripcion= formProd.findViewById(R.id.txtDescripcion);
                        TextInputEditText txtImagen= formProd.findViewById(R.id.txtImagen);

                        String nombreProducto = txtNombreProducto.getText().toString();
                        String precio = txtPrecio.getText().toString();
                        String descripcion = txtDescripcion.getText().toString();
                        String imagen = txtImagen.getText().toString();

                        itemSel.setNombre(nombreProducto);
                        itemSel.setPrecio(precio);
                        itemSel.setDescripcion(descripcion);
                        itemSel.setImagen(imagen);
                        Toast.makeText(getApplicationContext(),"elimina",Toast.LENGTH_SHORT).show();
                        //myRef.child(itemSel.getId()).setValue(itemSel);
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
