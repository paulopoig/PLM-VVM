package com.example.plm_vvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plm_vvm.R;
import com.example.plm_vvm.adapter.PresidentAdapter;
import com.example.plm_vvm.adapter.SecretaryAdapter;
import com.example.plm_vvm.model.President;
import com.example.plm_vvm.model.Secretary;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllSecretaryActivity extends AppCompatActivity {

    private RecyclerView secretaryRV;
    private Button startBtn;
    private List<Secretary> list;
    private SecretaryAdapter adapter;
    private EditText search;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_secretary);


        // Search Code
        search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());
            }
        });



        secretaryRV = findViewById(R.id.secretaries_rv);
        startBtn = findViewById(R.id.start);
        firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list = new ArrayList<>();
        adapter = new SecretaryAdapter(this, list);
        secretaryRV.setLayoutManager(new LinearLayoutManager(this));
        secretaryRV.setAdapter(adapter);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            firebaseFirestore.collection("Secretary")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot snapshot : Objects.requireNonNull(task).getResult())
                                {
                                    list.add(new Secretary(
                                            snapshot.getString("name"),
                                            snapshot.getString("party"),
                                            snapshot.getString("position"),
                                            snapshot.getString("image"),
                                            snapshot.getId() // It will get the document id.
                                    ));
                                }

                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(AllSecretaryActivity.this, "Candidate Not Found.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        String secretaryVote = task.getResult().getString("secretaryVote");

                        //assert presidentVote != null;
                        if (secretaryVote != null) {
                            if (secretaryVote.equals("voted.")) {
                                Toast.makeText(AllSecretaryActivity.this, "Your Vote is Already Counted.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AllSecretaryActivity.this, HomeActivity.class));
                                finish();
                            }
                        }
                    }
                });
    }

    private void filter(String text){
        ArrayList<Secretary> filteredList = new ArrayList<>();
        for(Secretary secretary : list)
        {
            if(secretary.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(secretary);
            }
        }

        adapter.filterList(filteredList);

    }
}