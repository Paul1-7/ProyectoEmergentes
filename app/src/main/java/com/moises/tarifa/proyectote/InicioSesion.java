package com.moises.tarifa.proyectote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InicioSesion extends AppCompatActivity {

    int RC_SIGN_IN = 100;
    SignInButton button;
    private FirebaseAuth mAuth;
    public DatabaseReference myRef;
    public ArrayList<Usuarios> usuariosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);


        mAuth = FirebaseAuth.getInstance();

        //inicio sesion
        EditText textEmail = findViewById(R.id.txtProducto);
        EditText textPassword = findViewById(R.id.txtPrecio);
        Button inicioSesion = findViewById(R.id.btnAcceder);
        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textEmail.getText().toString();
                String password = textPassword.getText().toString();
                //comprobamos si es correcto
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // envia el id
                            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentFirebaseUser != null){
                                Intent intent = new Intent(InicioSesion.this, MainActivity.class);
                                intent.putExtra("idUser",currentFirebaseUser.getUid());
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(getApplicationContext(), "No se ouede obtener los datos del usuario", Toast.LENGTH_SHORT).show();



                        } else
                            Toast.makeText(getApplicationContext(), "Usuario y/o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // crear cuenta
        TextView crearCuenta = findViewById(R.id.txtCrearCuenta);
        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mAuth.signOut();
                Intent intent = new Intent(InicioSesion.this, CrearCuenta.class);
                startActivity(intent);
            }
        });

        //google
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        button = (SignInButton) findViewById(R.id.btnGoogle);


    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                 firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
               Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }
                    }
                });
    }
    */
    }
}
