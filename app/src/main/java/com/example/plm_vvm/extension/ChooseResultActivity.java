package com.example.plm_vvm.extension;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plm_vvm.R;
import com.example.plm_vvm.activities.PresidentResultActivity;

public class ChooseResultActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_result);


        ImageView presidentresultBtn = (ImageView) findViewById(R.id.presidentresultBtn);
        ImageView vicepresidentresultBtn = (ImageView) findViewById(R.id.vicepresidentresultBtn);
        ImageView secretaryresultBtn = (ImageView) findViewById(R.id.secretaryresultBtn);
        ImageView treasurerresultBtn = (ImageView) findViewById(R.id.treasurerresultBtn);

        presidentresultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseResultActivity.this, PresidentResultActivity.class));
            }
        });

        vicepresidentresultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseResultActivity.this, VicePresidentResultActivity.class));
            }
        });

        secretaryresultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseResultActivity.this, SecretaryResultActivity.class));
            }
        });

        treasurerresultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseResultActivity.this, TreasurerResultActivity.class));
            }
        });




    }
}