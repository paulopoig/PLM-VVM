package com.example.plm_vvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.plm_vvm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {


    public static final String PREFERENCES  = "prefKey";
    SharedPreferences sharedPreferences;
    public static final String isLogIn = "islogin";

    private CircleImageView circleImg;
    private TextView nameTxt, studentIdTxt;
    private String uid;
    private FirebaseFirestore firebaseFirestore;
    private ImageView enlistBtn, voteBtn, tallyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseFirestore = FirebaseFirestore.getInstance();
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        circleImg = findViewById(R.id.circle_image);
        nameTxt = findViewById(R.id.name);
        studentIdTxt = findViewById(R.id.student_id);
        enlistBtn = findViewById(R.id.enlist_btn);
        voteBtn = findViewById(R.id.start_vote_btn);
        tallyBtn = findViewById(R.id.tally_btn);

        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor pref = sharedPreferences.edit();
        pref.putBoolean(isLogIn, true);
        pref.commit();

        findViewById(R.id.log_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                pref.putBoolean(isLogIn, false);
                pref.commit();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
                // return true;
            }
        });

        firebaseFirestore.collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    String name = task.getResult().getString("name");
                    String studentId = task.getResult().getString("studentId");
                    String image = task.getResult().getString("image");

                    if(name.equals("ADMIN"))
                    {
                        enlistBtn.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        voteBtn.setVisibility(View.VISIBLE);
                    }
//                    else
//                    {
//                        enlistBtn.setVisibility(View.GONE);
//                    }

                    nameTxt.setText(name + "!");
                    studentIdTxt.setText(studentId);
                    Glide.with(HomeActivity.this).load(image).into(circleImg);

                }
                else
                {
                    Toast.makeText(HomeActivity.this, "User Not Found.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        enlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, Create_Candidate_Activity.class));
            }
        });

        voteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, AllCandidateActivity.class));
            }
        });

        tallyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ResultActivity.class));
            }
        });
    }
}