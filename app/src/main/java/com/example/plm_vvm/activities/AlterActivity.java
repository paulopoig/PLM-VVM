package com.example.plm_vvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.plm_vvm.R;
import com.example.plm_vvm.model.Candidate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AlterActivity extends AppCompatActivity {

    private CircleImageView image;
    private String name, position, party;
    private EditText editName, editPosition, editParty;
    private ImageView deleteBtn;
    private Button updateBtn;

   // private Button Btnvote;

    private Candidate candidate;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);


        candidate = (Candidate) getIntent().getSerializableExtra("candidate");

        firebaseFirestore = FirebaseFirestore.getInstance();

        image = findViewById(R.id.image);
        editName = findViewById(R.id.name);
        editPosition = findViewById(R.id.post);
        editParty = findViewById(R.id.party);
        deleteBtn = findViewById(R.id.btn_delete);
        updateBtn = findViewById(R.id.btn_update);
        //Btnvote = findViewById(R.id.btn_vote);





        String url = getIntent().getStringExtra("image");
//        String nm = getIntent().getStringExtra("name");
//        String pos = getIntent().getStringExtra("post");
//        String part = getIntent().getStringExtra("party");
//        String id = getIntent().getStringExtra("id");

        Glide.with(this).load(url).into(image);
//        editName.setText(nm);
//        editPosition.setText(pos); // post ----> Position
//        editParty.setText(part);

        editName.setText(candidate.getName());
        editParty.setText(candidate.getParty());
        editPosition.setText(candidate.getPosition());
//
//        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();




        updateBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                name = editName.getText().toString().trim();
                position = editPosition.getText().toString().trim();
                party = editParty.getText().toString().trim();

                Candidate c = new Candidate(name, party, position);

                if(TextUtils.isEmpty(name))
                {
                    editName.setError("Please Enter Candidate Name");
                }
                else if(TextUtils.isEmpty(party)){
                    editParty.setError("Please Enter Candidate Party");
                }
                else if(TextUtils.isEmpty(position)){
                    editPosition.setError("Please Enter Candidate Position");
                }
                else{

                    firebaseFirestore.collection("Candidates")
                            .document(candidate.getId())
                            .update(
                                    "name", c.getName(),
                                    "party", c.getParty(),
                                    "position", c.getPosition()
                            )
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(AlterActivity.this, HomeActivity.class));
                                    Toast.makeText(AlterActivity.this, "Candidate Updated", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                    //updateCandidate(candidate, name, party, position);
                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =  new  AlertDialog.Builder(AlterActivity.this);
                builder.setTitle("This Candidate Will Be Deleted. Proceed?");
                builder.setMessage("Deleting a Candidate will Delete it Permanently.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCandidate();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog ad = builder.create();
                ad.show();

            }
        });


    }

    private void deleteCandidate() {

        firebaseFirestore.collection("Candidates")
                .document(candidate.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(AlterActivity.this, HomeActivity.class));
                            Toast.makeText(AlterActivity.this, "Candidate Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                });

    }
//
//    private void updateCandidate(Candidate candidate, String name, String party, String position) {
//
//        Candidate updatedCandidate = new Candidate(name, party, position);
//
//        firebaseFirestore.collection("Candidates").
//                document(candidate.getId()).
//                set(updatedCandidate).
//                addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Toast.makeText(AlterActivity.this, "Candidate Has Been Updated...", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Toast.makeText(AlterActivity.this, "Fail to Update Candidate", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//


    }


//}