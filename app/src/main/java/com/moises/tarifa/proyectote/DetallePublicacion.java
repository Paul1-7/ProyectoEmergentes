package com.moises.tarifa.proyectote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DetallePublicacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_publicacion);


        //id producto
        String id_producto = "no hay producto";
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            id_producto = extras.getString("id_producto");
        }
        System.out.println("id porud "+id_producto);

        //pasando el nombre del producto y la descripcion
        TextView textView = findViewById(R.id.txtDetallePublNim);
        TextView Des = findViewById(R.id.desc);
        textView.setText(id_producto);


    }
}