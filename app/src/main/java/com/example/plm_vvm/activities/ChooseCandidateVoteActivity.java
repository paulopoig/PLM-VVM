package com.example.plm_vvm.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.plm_vvm.R;
import com.example.plm_vvm.extension.AllVicePresidentActivity;

public class ChooseCandidateVoteActivity extends AppCompatActivity {

    private ImageView presidentBtn, vicepresidentBtn, secretaryBtn, treasurerBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_candidate_vote);


        presidentBtn = findViewById(R.id.presidentvoteBtn);
        vicepresidentBtn = findViewById(R.id.vicepresidentvoteBtn);
        secretaryBtn = findViewById(R.id.secretaryvoteBtn);
        treasurerBtn = findViewById(R.id.treasurervoteBtn);

        presidentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseCandidateVoteActivity.this, AllPresidentActivity.class));
            }
        });

        vicepresidentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseCandidateVoteActivity.this, AllVicePresidentActivity.class));

            }
        });


        secretaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseCandidateVoteActivity.this, AllSecretaryActivity.class));

            }
        });

        treasurerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseCandidateVoteActivity.this, AllTreasurerActivity.class));

            }
        });


    }
}