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
import com.example.plm_vvm.adapter.SecretaryAdapter;
import com.example.plm_vvm.adapter.TreasurerAdapter;
import com.example.plm_vvm.model.Secretary;
import com.example.plm_vvm.model.Treasurer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllTreasurerActivity extends AppCompatActivity {

    private RecyclerView treasurerRV;
    private Button startBtn;
    private List<Treasurer> list;
    private TreasurerAdapter adapter;
    private EditText search;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_treasurer);



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



        treasurerRV = findViewById(R.id.treasurers_rv);
        startBtn = findViewById(R.id.start);
        firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list = new ArrayList<>();
        adapter = new TreasurerAdapter(this, list);
        treasurerRV.setLayoutManager(new LinearLayoutManager(this));
        treasurerRV.setAdapter(adapter);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            firebaseFirestore.collection("Treasurer")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot snapshot : Objects.requireNonNull(task).getResult())
                                {
                                    list.add(new Treasurer(
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
                                Toast.makeText(AllTreasurerActivity.this, "Candidate Not Found.", Toast.LENGTH_SHORT).show();
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

                        String treasurerVote = task.getResult().getString("treasurerVote");

                        //assert presidentVote != null;
                        if (treasurerVote != null) {
                            if (treasurerVote.equals("voted.")) {
                                Toast.makeText(AllTreasurerActivity.this, "Your Vote is Already Counted.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AllTreasurerActivity.this, HomeActivity.class));
                                finish();
                            }
                        }
                    }
                });
    }

    private void filter(String text){
        ArrayList<Treasurer> filteredList = new ArrayList<>();
        for(Treasurer treasurer : list)
        {
            if(treasurer.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(treasurer);
            }
        }

        adapter.filterList(filteredList);

    }
}