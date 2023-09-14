package com.example.plm_vvm.extension;

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
import com.example.plm_vvm.activities.PresidentResultActivity;
import com.example.plm_vvm.adapter.PresidentResultAdapter;
import com.example.plm_vvm.adapter.VicePresidentResultAdapter;
import com.example.plm_vvm.model.President;
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

public class VicePresidentResultActivity extends AppCompatActivity {


    private RecyclerView vicepresidentResultRV;
    private Button startBtn;
    private List<VicePresident> list;
    private VicePresidentResultAdapter adapter;
    private EditText search;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vice_president_result);


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

        vicepresidentResultRV = findViewById(R.id.vicepresidentresult_rv);
        firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        list = new ArrayList<>();
        adapter = new VicePresidentResultAdapter(VicePresidentResultActivity.this, list);
        vicepresidentResultRV.setLayoutManager(new LinearLayoutManager(VicePresidentResultActivity.this));
        vicepresidentResultRV.setAdapter(adapter);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseFirestore.collection("Vice-President")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (DocumentSnapshot snapshot : Objects.requireNonNull(task).getResult()) {
                                    list.add(new VicePresident(
                                            snapshot.getString("name"),
                                            snapshot.getString("party"),
                                            snapshot.getString("position"),
                                            snapshot.getString("image"),
                                            snapshot.getId() // It will get the document id.
                                    ));
                                }

                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(VicePresidentResultActivity.this, "Candidate Not Found.", Toast.LENGTH_SHORT).show();
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
        ArrayList<VicePresident> filteredList = new ArrayList<>();
        for (VicePresident vicePresident : list) {
            if (vicePresident.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(vicePresident);
            }
        }

        adapter.filterList(filteredList);
    }

}