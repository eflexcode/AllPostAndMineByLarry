package com.eflexsoft.allpostandmine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingUpActivity extends AppCompatActivity {

    MaterialEditText username;
    MaterialEditText password;
    MaterialEditText email;
    CircleImageView imageView;
    Uri imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        imageView = findViewById(R.id.pro_pic);

    }

    public void finish(View view) {
        finish();
    }

    public void Continue(View view) {

        final IOSDialog dialog0 = new IOSDialog.Builder(this)
                .setTitle("Creating your account")
                .setTitleColorRes(R.color.gray)
                .build();

        final String getUsername = username.getText().toString();
        final String getPassword = password.getText().toString();
        final String getEmail = email.getText().toString();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        if (getEmail.isEmpty() || getPassword.isEmpty() || getUsername.isEmpty() || imageUrl == null) {
            Toast.makeText(this, "all filed are required", Toast.LENGTH_SHORT).show();
        } else if (getPassword.length() < 8) {
            Toast.makeText(this, "password to short at lest 8 characters", Toast.LENGTH_SHORT).show();
        } else {

            dialog0.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("profilePicture");
            final StorageReference reference = storageReference.child(String.valueOf(System.currentTimeMillis()));
            StorageTask uploadTask = reference.putFile(imageUrl);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        final String getdownloadurl = task.getResult().toString();

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("username", getUsername);
                                    map.put("id", FirebaseAuth.getInstance().getUid());
                                    map.put("imageUrl", getdownloadurl);

                                    DatabaseReference databaseReference = firebaseDatabase.getReference("Users")
                                            .child(FirebaseAuth.getInstance().getUid());

                                    databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SingUpActivity.this, "sing up successful", Toast.LENGTH_SHORT).show();
                                                dialog0.dismiss();
                                                finish();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SingUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            dialog0.dismiss();
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SingUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog0.dismiss();
                }
            });
        }
    }

    public void openGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUrl = data.getData();
            imageView.setImageURI(data.getData());
        }
    }
}