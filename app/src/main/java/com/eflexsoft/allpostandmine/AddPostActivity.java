package com.eflexsoft.allpostandmine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    EditText title;
    EditText body;
    ImageView postImage;
    Button postBtn;
    Uri imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        title = findViewById(R.id.title_edit_text);
        body = findViewById(R.id.post_body);
        postImage = findViewById(R.id.post_image);
        postBtn = findViewById(R.id.post);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.isEmpty()) {
                    postBtn.setBackgroundResource(R.drawable.btn_back);
                    postBtn.setTextColor(Color.parseColor("#000"));
                } else {
                    postBtn.setBackgroundResource(R.drawable.btn_back_green);
                    postBtn.setTextColor(Color.parseColor("#067A37"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.isEmpty()) {
                    postBtn.setBackgroundResource(R.drawable.btn_back);
                    postBtn.setTextColor(Color.parseColor("#000"));
                } else {
                    postBtn.setBackgroundResource(R.drawable.btn_back_green);
                    postBtn.setTextColor(Color.parseColor("#067A37"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void finish(View view) {
        finish();
    }

    public void Post(View view) {

        final IOSDialog dialog0 = new IOSDialog.Builder(this)
                .setTitle("Adding post")
                .setTitleColorRes(R.color.gray)
                .build();

        final String getTitle = title.getText().toString();
        final String getBody = body.getText().toString();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        if (getBody.isEmpty() || getTitle.isEmpty() || imageUrl == null) {
            Toast.makeText(this, "all filed are required or you haven, t piked an image", Toast.LENGTH_LONG).show();
        } else {

            dialog0.show();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("PostsImage");
            final StorageReference reference = storageReference.child(FirebaseAuth.getInstance().getUid()).child(String.valueOf(System.currentTimeMillis()));
            UploadTask uploadTask = reference.putFile(imageUrl);
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
                        Calendar c = Calendar.getInstance();
                        String date = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
                        Map<String,Object> postMap = new HashMap<>();
                        postMap.put("posterId",FirebaseAuth.getInstance().getUid());
                        postMap.put("title",getTitle);
                        postMap.put("body",getBody);
                        postMap.put("image",getdownloadurl);
                        postMap.put("date",date);

                        DatabaseReference databaseReference = firebaseDatabase.getReference("Posts");

                        databaseReference.
                                push().setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AddPostActivity.this, "post successful", Toast.LENGTH_SHORT).show();
                                    dialog0.dismiss();
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog0.dismiss();
                            }
                        });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            postImage.setImageURI(data.getData());
        }
    }
}
