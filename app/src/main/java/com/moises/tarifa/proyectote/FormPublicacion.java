package com.moises.tarifa.proyectote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FormPublicacion extends AppCompatActivity {

    DatabaseReference dbRef;
    RecyclerAdapter adapter;
    private ArrayList<Productos> productosList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_publicacion);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("productos");

        adapter = new RecyclerAdapter(getApplicationContext(),productosList);

        //añadir texto a la pantall
        TextView titulo = findViewById(R.id.textViewFormPublicacion);
        titulo.setText("Nueva Publicacion");

        Button btnGuardar = findViewById(R.id.btnEnviar);
        btnGuardar.setText("Guardar");
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //guardar los datos
                TextInputEditText txtNombreProducto= findViewById(R.id.txtProducto);
                TextInputEditText txtPrecio= findViewById(R.id.txtPrecio);
                TextInputEditText txtDescripcion= findViewById(R.id.txtDescripcion);
                TextInputEditText txtImagen= findViewById(R.id.txtImagen);
                //TextInputEditText txtEstado= findViewById(R.id.);

                String nombreProducto = txtNombreProducto.getText().toString();
                String precio = txtPrecio.getText().toString();
                String descripcion = txtDescripcion.getText().toString();
                String imagen = txtImagen.getText().toString();

                //dato del idUser recibido
                String idUser = "no hay prametro";
                Bundle extras = getIntent().getExtras();
                if(extras!=null){
                    idUser = extras.getString("idUser");
                }

                //String estado = txtEstado.getText().toString();
                String id = dbRef.push().getKey();

                Productos productoNew = new Productos();
                productoNew.setNombre(nombreProducto);
                productoNew.setPrecio(precio);
                productoNew.setDescripcion(descripcion);
                productoNew.setImagen(imagen);
                productoNew.setEstado("Disponible");
                productoNew.setId_user(idUser);
                adapter.insertItem(productoNew);
                dbRef.child(id).setValue(productoNew);

                Intent i = new Intent(FormPublicacion.this,MainActivity.class);
                startActivity(i);
            }
        });


    }
}