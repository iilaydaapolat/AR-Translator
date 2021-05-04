package com.example.artranslator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userOriginalTextFromFB;
    ArrayList<String> userTranslateTextFromFB;
    RecylerAdapter recylerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userOriginalTextFromFB = new ArrayList<>();
        userTranslateTextFromFB = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        getDataFromFireStrore();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recylerAdapter = new RecylerAdapter(userOriginalTextFromFB,userTranslateTextFromFB);
        recyclerView.setAdapter(recylerAdapter);
    }

    public void getDataFromFireStrore(){

        CollectionReference collectionReference = firebaseFirestore.collection("Documents");
        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Toast.makeText(UserProfile.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                }
                if (value != null){
                    for (DocumentSnapshot snapshot : value.getDocuments()){
                        Map<String,Object>data=snapshot.getData();
                        String originalText = (String) data.get("originalText");
                        String translateText = (String) data.get("translateText");

                        userOriginalTextFromFB.add(originalText);
                        userTranslateTextFromFB.add(translateText);

                        recylerAdapter.notifyDataSetChanged();

                    }
                }
            }
        });

    }

}