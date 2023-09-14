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
import com.example.plm_vvm.adapter.PresidentResultAdapter;
import com.example.plm_vvm.model.President;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PresidentResultActivity extends AppCompatActivity {

    private RecyclerView presidentResultRV;
    private Button startBtn;
    private List<President> list;
    private PresidentResultAdapter adapter;
    private EditText search;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_president_result);


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

        presidentResultRV = findViewById(R.id.presidentresult_rv);
        firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list = new ArrayList<>();
        adapter = new PresidentResultAdapter(PresidentResultActivity.this, list);
        presidentResultRV.setLayoutManager(new LinearLayoutManager(PresidentResultActivity.this));
        presidentResultRV.setAdapter(adapter);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseFirestore.collection("President")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot snapshot : Objects.requireNonNull(task).getResult()) {
                                    list.add(new President(
                                            snapshot.getString("name"),
                                            snapshot.getString("party"),
                                            snapshot.getString("position"),
                                            snapshot.getString("image"),
                                            snapshot.getId() // It will get the document id.
                                    ));
                                }

                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(PresidentResultActivity.this, "Candidate Not Found.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        FirebaseFirestore.getInstance().collection("Users")
//                .document(uid)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                        String presidentVote = task.getResult().getString("presidentVote");
//
//                        //assert presidentVote != null;
//                        if (presidentVote != null) {
//                            if (presidentVote.equals("voted.")) {
//                                Toast.makeText(PresidentResultActivity.this, "Your Vote is Counted Already.", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(PresidentResultActivity.this, PresidentResultActivity.class));
//                                finish();
//                            }
//                        }
//                   }
//
//                });
//    }

    private void filter(String text) {
        ArrayList<President> filteredList = new ArrayList<>();
        for (President president : list) {
            if (president.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(president);
            }
        }

        adapter.filterList(filteredList);
    }

}