package com.example.plm_vvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plm_vvm.R;
import com.example.plm_vvm.adapter.CandidateAdapter;
import com.example.plm_vvm.adapter.EditCandidateAdapter;
import com.example.plm_vvm.model.Candidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditCandidateActivity extends AppCompatActivity {


    private RecyclerView candidateRV;
    private Button startBtn;
    private List<Candidate> list;
    private EditCandidateAdapter adapter;
    private EditText search;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_candidate);


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

        candidateRV = findViewById(R.id.candidates_rv);
        startBtn = findViewById(R.id.start);
        firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list = new ArrayList<>();
        adapter = new EditCandidateAdapter(this, list);
        candidateRV.setLayoutManager(new LinearLayoutManager(this));
        candidateRV.setAdapter(adapter);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            firebaseFirestore.collection("Candidates")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot snapshot : Objects.requireNonNull(task).getResult())
                                {
                                    list.add(new Candidate(
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
                                Toast.makeText(EditCandidateActivity.this, "Candidate Not Found.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }

    private void filter(String text){
        ArrayList<Candidate> filteredList = new ArrayList<>();
        for(Candidate candidate : list)
        {
            if(candidate.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(candidate);
            }
        }

        adapter.filterList(filteredList);

    }

}