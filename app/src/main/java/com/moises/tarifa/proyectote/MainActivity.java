package com.moises.tarifa.proyectote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    //declaramos al recycler
    RecyclerView recyclerView;

    private DatabaseReference myRef;

    private ArrayList<Messages> messagesList;

    private RecyclerAdapter recyclerAdapter;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);

    //firebase
        myRef = FirebaseDatabase.getInstance().getReference();

    //Array list
        messagesList = new ArrayList<>();

        //limpiar array
        ClearAll();

        //metodo para info de firebase
        GetDataFromFirebase();


    }

    private void GetDataFromFirebase() {
        Query  query = myRef.child("message");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                ClearAll();
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    Messages B = new Messages();
                    B.setName(snapshot.child("name").getValue().toString());
                    B.setImage(snapshot.child("image").getValue().toString());

                    messagesList.add(B);
                }
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(),messagesList);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ClearAll(){
        if(messagesList != null){
            messagesList.clear();


            if(recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        messagesList = new ArrayList<>();
    }

}