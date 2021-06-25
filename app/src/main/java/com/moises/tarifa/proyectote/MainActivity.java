package com.moises.tarifa.proyectote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.moises.tarifa.proyectote.DetallePublicacion;
import com.moises.tarifa.proyectote.Productos;
import com.moises.tarifa.proyectote.R;
import com.moises.tarifa.proyectote.RecyclerAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    //navbotton
    BottomNavigationView navBotton;

    RecyclerView recyclerView   ;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private AppBarConfiguration mAppBarConfiguration;
    //firebase
    public  DatabaseReference myRef;

    public ArrayList<Productos> productosList;

    private RecyclerAdapter recyclerAdapter;

    //id del usuario
    String idUser="";

    //Toolbar
    Toolbar tool;

    //buscador
    EditText etBuscador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //--------------------------------------------------buscador

        etBuscador = findViewById(R.id.etBuscador);
        etBuscador.setVisibility(View.GONE);
        etBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filtrar(s.toString());
            }
        });

        /*
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //IMPLEMENTAMOS INICIO
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        //

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_user, R.id.nav_Notificaciones, R.id.nav_publicaciones)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //IMPLEMENTADO
        updateNavHeader();
*/


        //--------------------------------------------------------------------------------------
        //actionbar


        //navbotton
        navBotton = findViewById(R.id.navBotton);

        navBotton.setOnNavigationItemSelectedListener(this);


        //menus
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //firebase
        myRef=FirebaseDatabase.getInstance().getReference();
        //arrya
        productosList = new ArrayList<>();

        //recibiendo dato del id
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            idUser = extras.getString("idUser");
        }

        //limpio array
        ClearAll();
        //info de base de datos
        GetDataFromFirebase();





    }

    private void GetDataFromFirebase() {
        Query query = myRef.child("productos");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                ClearAll();
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    Productos B = new Productos();
                    B.setNombre(snapshot.child("nombre").getValue().toString());
                    B.setPrecio(snapshot.child("precio").getValue().toString()+" Bs.");
                    B.setImagen(snapshot.child("imagen").getValue().toString());

                    productosList.add(B);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), productosList);
                recyclerView.setAdapter(recyclerAdapter);
                //recyclerAdapter.notifyDataSetChanged();


                recyclerAdapter.setOnclickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //pansando informacion por medio de intent a main activity
                        Intent i = new Intent(getApplicationContext(), DetallePublicacion.class);
                        i.putExtra("id_producto", productosList.get(recyclerView.getChildAdapterPosition(v)).getId());
                        startActivity(i);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ClearAll(){
        if(productosList != null){
            productosList.clear();
        }
        productosList =new ArrayList<>();
    }

    //******************************************************************* menu actionbar
    // metodo para mostrar y ocultar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return  true;

    }

    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id=item.getItemId();
//        if(id==R.id.nav_logOut){
//        }
//        return false;
//    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //PARA ACTUALIZAR LOS DATOS EN EL HEADER
    public void updateNavHeader(){
        NavigationView navigationView= (NavigationView) findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        TextView navUsername=headerView.findViewById(R.id.nav_username);
        TextView navUserEmail=headerView.findViewById(R.id.nav_user_email);
        //ImageView navUseFoto=headerView.findViewById(R.id.nav_user_foto);

        navUserEmail.setText(currentUser.getEmail());
        navUsername.setText(currentUser.getDisplayName());

    }
*/
    //Metodo para asignar las funciones correspondientes a las opciones
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.salir) {
            Toast.makeText(this, "salir", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.menus) {
            Toast.makeText(this, "pagina siguiente", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,DetallePublicacion.class);
            i.putExtra("idUser",idUser);
            startActivity(i);
        }  return super.onOptionsItemSelected(item);
    }

    //******************************************************************* menu navbar
    @SuppressLint("WrongConstant")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id== R.id.itembuscar){
            if(etBuscador.getVisibility() == 8)
                etBuscador.setVisibility(View.VISIBLE);
            else if(etBuscador.getVisibility() == 0){
                etBuscador.setVisibility(View.GONE);
            }
            return true;
        }else if(id== R.id.itemadd){
            Intent i = new Intent(this, FormPublicacion.class);
            i.putExtra("idUser",idUser);
            startActivity(i);

            return true;
        }else if(id==R.id.itemMisPublicaciones){
            Intent i = new Intent(this, MisPublicaciones.class);
            startActivity(i);
        } {

            return true;
        }

    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int id=item.getItemId();
//        if(id==R.id.nav_logOut){
//        }
//        return false;
//    }
    //******************************************************************* metodo filtrar
    public void filtrar(String texto){
        ArrayList<Productos> filtrarLista= new ArrayList<>();
        for(Productos men : productosList){
            if(men.getNombre().toLowerCase().contains(texto.toLowerCase())){
                filtrarLista.add(men);
            }
        }
        recyclerAdapter.filtrar(filtrarLista);
    }
    //******************************************************************* metodo filtrar

}