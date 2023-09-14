package com.example.plm_vvm.extension;

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
import com.example.plm_vvm.activities.HomeActivity;
import com.example.plm_vvm.adapter.VicePresidentAdapter;
import com.example.plm_vvm.model.VicePresident;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllVicePresidentActivity extends AppCompatActivity {

    private RecyclerView vicepresidentRV;
    private Button startBtn;
    private List<VicePresident> list;
    private VicePresidentAdapter adapter;
    private EditText search;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_vice_president);


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

        vicepresidentRV = findViewById(R.id.vicepresidents_rv);
        startBtn = findViewById(R.id.start);
        firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list = new ArrayList<>();
        adapter = new VicePresidentAdapter(this, list);
        vicepresidentRV.setLayoutManager(new LinearLayoutManager(this));
        vicepresidentRV.setAdapter(adapter);

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            firebaseFirestore.collection("Vice-President")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful())
                            {
                                for(DocumentSnapshot snapshot : Objects.requireNonNull(task).getResult())
                                {
                                    list.add(new VicePresident(
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
                                Toast.makeText(AllVicePresidentActivity.this, "Candidate Not Found.", Toast.LENGTH_SHORT).show();
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

                        String vicepresidentVote = task.getResult().getString("vicepresidentVote");

                        //assert presidentVote != null;
                        if (vicepresidentVote != null) {
                            if (vicepresidentVote.equals("voted.")) {
                                Toast.makeText(AllVicePresidentActivity.this, "Your Vote is Already Counted.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AllVicePresidentActivity.this, HomeActivity.class));
                                finish();
                            }
                        }
                    }
                });
    }

    private void filter(String text){
        ArrayList<VicePresident> filteredList = new ArrayList<>();
        for(VicePresident vicePresident : list)
        {
            if(vicePresident.getName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(vicePresident);
            }
        }

        adapter.filterList(filteredList);

    }
}