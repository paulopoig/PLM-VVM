package com.example.plm_vvm.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.plm_vvm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class VotingPresidentActivity extends AppCompatActivity {

    private CircleImageView image;
    private TextView name, position, party;
    private Button Btnvote;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_president);


        firebaseFirestore = FirebaseFirestore.getInstance();

        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        position = findViewById(R.id.post);
        party = findViewById(R.id.party);
        Btnvote = findViewById(R.id.btn_vote);

        String url = getIntent().getStringExtra("image");
        String nm = getIntent().getStringExtra("name");
        String pos = getIntent().getStringExtra("position");
        String part = getIntent().getStringExtra("party");
        String id = getIntent().getStringExtra("id");

        Glide.with(this).load(url).into(image);
        name.setText(nm);
        position.setText(pos); // post ----> Position
        party.setText(part);

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Btnvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String presidentVote = "voted.";

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("presidentVote", presidentVote);
                userMap.put("deviceIp", getDeviceIP());
                userMap.put(pos,id);

                firebaseFirestore.collection("Users")
                        .document(uid)
                        .update(userMap);

                Map<String, Object> presidentMap = new HashMap<>();
                presidentMap.put("deviceIp", getDeviceIP());
                presidentMap.put("candidatePost", pos);
                presidentMap.put("timestamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("President/"+id+"/Vote")
                        .document(uid)
                        .set(presidentMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Toast.makeText(VotingPresidentActivity.this, "Voted Counted Successfully.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(VotingPresidentActivity.this, PresidentResultActivity.class));
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(VotingPresidentActivity.this, "Vote Unsucessful.", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });


            }
        });

    }

    private String getDeviceIP() {

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface inf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = inf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(VotingPresidentActivity.this, "" + e, Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}