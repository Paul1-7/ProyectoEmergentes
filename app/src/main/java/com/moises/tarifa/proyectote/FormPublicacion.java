package com.moises.tarifa.proyectote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class FormPublicacion extends AppCompatActivity {

    DatabaseReference dbRef;
    RecyclerAdapter adapter;

    int SACAR_FOTO = 22;
    int BUSCAR_ARCHIVO= 56;
    FirebaseStorage storage;
    StorageReference storageReference;

    private ArrayList<Productos> productosList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_publicacion);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("productos");

        adapter = new RecyclerAdapter(getApplicationContext(),productosList);


        //añadir texto a la pantalla
        TextView titulo = findViewById(R.id.textViewFormPublicacion);
        titulo.setText("Nueva Publicacion");


        //storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //imageView = findViewById(R.id.imageView);
        //toma foto con la camara
        Button button = findViewById(R.id.btnTomarFoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intentFoto.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intentFoto,SACAR_FOTO);
                }
            }
        });
        //buscar archivo en telefono
        Button buttoBuscar = findViewById(R.id.btnSubirFoto);
        buttoBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccione una imagen"),BUSCAR_ARCHIVO);
            }
        });

        //btn cancelar
        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FormPublicacion.this,MainActivity.class);
                startActivity(i);
            }
        });

        //guardar publicacion
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

                RadioButton disponible = findViewById(R.id.radio_button_disponible);
                RadioButton vendido = findViewById(R.id.radio_button_vendido);

                String nombreProducto = txtNombreProducto.getText().toString();
                String precio = txtPrecio.getText().toString();
                String descripcion = txtDescripcion.getText().toString();
                String imagen = txtImagen.getText().toString();
                String estado = "";
                if(disponible.isChecked()){
                    estado = "disponible";
                }else if(vendido.isChecked())
                    estado = "vendido";

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
                productoNew.setEstado(estado);
                productoNew.setId_user(idUser);
                adapter.insertItem(productoNew);
                dbRef.child(id).setValue(productoNew);

                Intent i = new Intent(FormPublicacion.this,MainActivity.class);
                i.putExtra("idUser",idUser);
                startActivity(i);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //si esta correcto guarda la imagen
        if(requestCode == SACAR_FOTO && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap image = (Bitmap) bundle.get("data");
            //imageView.setImageBitmap(image);
            String nombreArchivo = UUID.randomUUID().toString()+".jpg";
            StorageReference fotoReference = storageReference.child("productos/"+nombreArchivo);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            byte[] datos = outputStream.toByteArray();

            //subir a firebase
            UploadTask uploadTask= fotoReference.putBytes(datos);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   Toast.makeText(getApplicationContext(),"la foto se subio correctamente",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Se produjo un error",Toast.LENGTH_SHORT).show();
                }
            });
        }//esto es para subir desde el telefono a firebase
        else if(requestCode == BUSCAR_ARCHIVO && resultCode == RESULT_OK){
            Uri rutaArchivo = data.getData();
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Cargando imagen al servidor");
            progressDialog.show();
            String nombreArchivo = UUID.randomUUID().toString()+".jpg";
            StorageReference storageReference = storage.getReference("productos/"+nombreArchivo);
            storageReference.putFile(rutaArchivo).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double p = (snapshot.getBytesTransferred()/snapshot.getTotalByteCount())*100.0;
                    progressDialog.setMessage("Subiendo... "+ (int)p+"%");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(FormPublicacion.this,"Carga de archivos completa",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(FormPublicacion.this,"Se produjo un error al subir la imagen",Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}