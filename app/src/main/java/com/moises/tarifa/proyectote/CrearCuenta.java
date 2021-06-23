package com.moises.tarifa.proyectote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class CrearCuenta extends AppCompatActivity {

    EditText txtNombre;
    EditText txtApellido;
    EditText txtEmail;
    EditText txtPassword;
    EditText txtConfirmarPassword;

    String nombre="";
    String apellido="";
    String email = "";
    String password = "";
    String confirmarPassword = "";

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        txtApellido = findViewById(R.id.txtApellido);
        txtEmail = findViewById(R.id.txtProducto);
        txtNombre = findViewById(R.id.txtNombre);
        txtPassword = findViewById(R.id.txtPrecio);
        txtConfirmarPassword = findViewById(R.id.txtConfirmarCon);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Button button = findViewById(R.id.btnNuevaCuenta);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = txtNombre.getText().toString();
                apellido= txtApellido.getText().toString();
                email= txtEmail.getText().toString();
                password= txtPassword.getText().toString();
                confirmarPassword = txtConfirmarPassword.getText().toString();

                if( confirmarPassword.equals(password)){
                    registrar();
                }
                else
                    Toast.makeText(getApplicationContext(),"Las contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registrar(){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull  Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    map.put("apellido",apellido);
                    map.put("email",email);
                    map.put("password",password);

                    databaseReference.child("user").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Se registro correctamente",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CrearCuenta.this,InicioSesion.class));
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"no se pudo registrarr",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else
                {
                    Toast.makeText(getApplicationContext(),"no se pudo registrar",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}