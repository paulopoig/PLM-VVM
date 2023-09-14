package com.example.plm_vvm.edit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.plm_vvm.R;
import com.example.plm_vvm.activities.PresidentResultActivity;
import com.example.plm_vvm.extension.ChooseResultActivity;

public class ChooseEditActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_edit);


        ImageView presidenteditBtn = (ImageView) findViewById(R.id.presidenteditBtn);
        ImageView vicepresidenteditBtn = (ImageView) findViewById(R.id.vicepresidenteditBtn);
        ImageView secretaryeditBtn = (ImageView) findViewById(R.id.secretaryeditBtn);
        ImageView treasurereditBtn = (ImageView) findViewById(R.id.treasurereditBtn);


        presidenteditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseEditActivity.this, AllPresidentEditActivity.class));
            }
        });

        vicepresidenteditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseEditActivity.this, AllVicePresidentEditActivity.class));
            }
        });

        secretaryeditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseEditActivity.this, AllSecretaryEditActivity.class));
            }
        });

        treasurereditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseEditActivity.this, AllTreasurerEditActivity.class));
            }
        });

    }
}