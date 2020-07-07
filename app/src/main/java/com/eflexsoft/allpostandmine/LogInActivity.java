package com.eflexsoft.allpostandmine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gmail.samehadar.iosdialog.IOSDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LogInActivity extends AppCompatActivity {
    MaterialEditText password;
    MaterialEditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        password = findViewById(R.id.password);
        email = findViewById(R.id.email);

    }

    public void finish(View view) {
        finish();
    }

    public void login(View view) {
        final IOSDialog dialog0 = new IOSDialog.Builder(this)
                .setTitle("logging in")
                .setTitleColorRes(R.color.gray)
                .build();

        final String getPassword = password.getText().toString();
        final String getEmail = email.getText().toString();

        if (getEmail.isEmpty() || getPassword.isEmpty()){
            Toast.makeText(this, "all filed are required", Toast.LENGTH_SHORT).show();
        } else if (getPassword.length() < 8) {
            Toast.makeText(this, "password to short at lest 8 characters", Toast.LENGTH_SHORT).show();
        } else {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            dialog0.show();
            firebaseAuth.signInWithEmailAndPassword(getEmail,getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        dialog0.dismiss();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LogInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog0.dismiss();
                }
            });
        }
    }
}
