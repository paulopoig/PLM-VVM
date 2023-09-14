package com.example.plm_vvm.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plm_vvm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    private CircleImageView userProfile;
    private EditText userName, userPassword, userEmail, userStudentID;
    private Button signUpBtn;
    private Uri mainUri = null;
    private FirebaseAuth mAuth;

    public static final String PREFERENCES = "prefKey";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Password = "passwordKey";
    public static final String StudentId = "studentIdKey";
    public static final String Image = "imageKey";
    public static final String UploadData = "uploaddata";

    SharedPreferences sharedPreferences;

    StorageReference reference;

    FirebaseFirestore firebaseFirestore;

    String name, password, email, studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        reference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        findViewById(R.id.have_acc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userProfile = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        userPassword = findViewById(R.id.user_password);
        userEmail = findViewById(R.id.user_email);
        userStudentID = findViewById(R.id.user_student_id);
        signUpBtn = findViewById(R.id.signup_btn);

        mAuth = FirebaseAuth.getInstance();

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(SignUpActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(SignUpActivity.this,
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                    else{
                        cropImage();
                    }
                }
                else{
                    cropImage();
                }
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 name = userName.getText().toString().trim();
                 password = userPassword.getText().toString().trim();
                 email = userEmail.getText().toString().trim();
                 studentId = userStudentID.getText().toString().trim();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        && !TextUtils.isEmpty(studentId)){

                    createUser(email, password);
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Please Enter you Credentials.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "User Successfully Created.", Toast.LENGTH_SHORT).show();

                    verifyEmail();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Fail to Create User, Please Try Again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, "An Error has Occur.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verifyEmail() {

        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        SharedPreferences.Editor pref = sharedPreferences.edit();

                        pref.putString(Name, name);
                        pref.putString(Password, password);
                        pref.putString(Email, email);
                        pref.putString(StudentId, studentId);
                        pref.putString(Image,mainUri.toString());
                        pref.commit();


//                        boolean bol = sharedPreferences.getBoolean(UploadData, false);
//                        if(bol)
//                        {
//                            // If email is verified and data is already uploaded, then we do not  need to upload/reupload it again.
//                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                            finish();
//                        }

                        String name = sharedPreferences.getString(Name, null);
                        String password = sharedPreferences.getString(Password, null);
                        String email = sharedPreferences.getString(Email, null);
                        String studentId = sharedPreferences.getString(StudentId, null);
                        String image = sharedPreferences.getString(Image, null);
                        // First, we sent email for verification
                        // Store the data in shared preference if user verify email
                        // and Login will upload the data to Firestore and save image in  Firebase.


                        if (name != null && password != null && email != null && studentId != null && image != null) {
                            String uid = mAuth.getUid();

                            StorageReference imagePath = reference.child("image_profile").child(uid + ".jpg");
                            imagePath.putFile(Uri.parse(image)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("name", name);
                                                map.put("email", email);
                                                map.put("password", password);
                                                map.put("studentId", studentId);
                                                map.put("image", uri.toString());
                                                map.put("uid", uid);

                                                firebaseFirestore.collection("Users")
                                                        .document(uid)
                                                        .set(map)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {

                                                            sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                                                            SharedPreferences.Editor pref = sharedPreferences.edit();
                                                            pref.putBoolean(UploadData, true);
                                                            pref.commit();

//                                                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                                                                    finish();

                                                                } else {
                                                                    Toast.makeText(SignUpActivity.this, "Data Not Stored.", Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        });

                                            }
                                        });
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }



                        // Email will be sent
                        Toast.makeText(SignUpActivity.this, "Email Sent to User", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    }
                    else
                    {
                        mAuth.signOut();
                        finish();
                    }
                }
            });
        }
    }

    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainUri = result.getUri();
                userProfile.setImageURI(mainUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}