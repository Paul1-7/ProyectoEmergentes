package com.moises.tarifa.proyectote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ModificarPublicacion extends AppCompatActivity {

    DatabaseReference dbRef;
    String idProducto="";
    private ArrayList<Productos> productosList = new ArrayList<Productos>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_publicacion);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("productos");

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idProducto = extras.getString("producto_id");
        }


        //añadir texto a la pantalla
        TextView titulo = findViewById(R.id.textViewFormPublicacion);
        titulo.setText("Nueva Publicacion");

        //btn cancelar
        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ModificarPublicacion.this,MisPublicaciones.class);
                startActivity(i);
            }
        });

        //lista de BD
        Query query = dbRef.child("productos");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    Productos B = new Productos();
                    System.out.println("prrr "+ B.getId());
                    if(idProducto.equals(B.getId())){
                        productosList.add(B);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //guardar los datos
        TextInputEditText txtNombreProducto= findViewById(R.id.txtProducto);
        TextInputEditText txtPrecio= findViewById(R.id.txtPrecio);
        TextInputEditText txtDescripcion= findViewById(R.id.txtDescripcion);
        TextInputEditText txtImagen= findViewById(R.id.txtImagen);
        RadioButton disponible = findViewById(R.id.radio_button_disponible);
        RadioButton vendido = findViewById(R.id.radio_button_vendido);
        //producto
        if(!productosList.isEmpty()){
            Productos productoOld = productosList.get(0);
            txtNombreProducto.setText(productoOld.getNombre());
            txtPrecio.setText(productoOld.getPrecio());
            txtDescripcion.setText(productoOld.getDescripcion());
            txtImagen.setText(productoOld.getImagen());

        }


        //guardar publicacion
        Button btnGuardar = findViewById(R.id.btnEnviar);
        btnGuardar.setText("Guardar");
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                String nombreProducto = txtNombreProducto.getText().toString();
                String precio = txtPrecio.getText().toString();
                String descripcion = txtDescripcion.getText().toString();
                String imagen = txtImagen.getText().toString();
                String estado = "Disponible";
                if(disponible.isChecked()){
                    estado = "disponible";
                }else if(vendido.isChecked())
                    estado = "vendido";
                /*
*/

                //String estado = txtEstado.getText().toString();
                String id = dbRef.push().getKey();

                Productos productoNew = new Productos();
                productoNew.setNombre(nombreProducto);
                productoNew.setPrecio(precio);
                productoNew.setDescripcion(descripcion);
                productoNew.setImagen(imagen);
                productoNew.setEstado(estado);
                productoNew.setId(id);
                dbRef.child(id).setValue(productoNew);

                Intent i = new Intent(ModificarPublicacion.this,MisPublicaciones.class);
                startActivity(i);
            }
        });
    }
}