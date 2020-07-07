package com.eflexsoft.allpostandmine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eflexsoft.allpostandmine.adapter.PostAdapter;
import com.eflexsoft.allpostandmine.placeholder.Post;
import com.eflexsoft.allpostandmine.placeholder.User;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    AppBarLayout outAppBar;
    AppBarLayout inAppBar;
    FirebaseUser user;
    CircleImageView pro_pics;
    TextView username;
    FloatingActionButton add;
    RecyclerView postRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = FirebaseAuth.getInstance().getCurrentUser();
        outAppBar = findViewById(R.id.out_app_bar);
        inAppBar = findViewById(R.id.login_app_bar);
        pro_pics = findViewById(R.id.pro_pic);
        username = findViewById(R.id.username);
        add = findViewById(R.id.add);
        postRecycle = findViewById(R.id.post_recycle_view);
        postRecycle.setHasFixedSize(true);
        postRecycle.setLayoutManager(new LinearLayoutManager(this));

        if (user != null) {
            inAppBar.setVisibility(VISIBLE);
            outAppBar.setVisibility(GONE);

            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Glide.with(MainActivity.this).load(user.getImageUrl()).into(pro_pics);
                        username.setText(user.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Query reference = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("date");

                final List<Post> posts = new ArrayList<>();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        posts.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);

                            if (post.getPosterId().equals(FirebaseAuth.getInstance().getUid())) {
                                posts.add(post);
                            }
                        }
                        postRecycle.setAdapter(new PostAdapter(posts, MainActivity.this));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } catch (Exception e) {
                Toast.makeText(this, "your are not logged in", Toast.LENGTH_SHORT).show();
            }

        } else {

            inAppBar.setVisibility(GONE);
            outAppBar.setVisibility(VISIBLE);
            Query reference = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("date");

            final List<Post> posts = new ArrayList<>();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    posts.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Post post = dataSnapshot.getValue(Post.class);
                        posts.add(post);
                    }

                    postRecycle.setAdapter(new PostAdapter(posts, MainActivity.this));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    public void login(View view) {
        startActivity(new Intent(this, LogInActivity.class));
    }


    public void singUp(View view) {
        startActivity(new Intent(this, SingUpActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user != null) {
            inAppBar.setVisibility(VISIBLE);
            outAppBar.setVisibility(GONE);
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Glide.with(MainActivity.this).load(user.getImageUrl()).into(pro_pics);
                        username.setText(user.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Query reference = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("date");

                final List<Post> posts = new ArrayList<>();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        posts.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Post post = dataSnapshot.getValue(Post.class);
                            assert post != null;
                            if (post.getPosterId().equals(FirebaseAuth.getInstance().getUid())) {
                                posts.add(post);
                            }
                            postRecycle.setAdapter(new PostAdapter(posts, MainActivity.this));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } catch (Exception e) {
                Toast.makeText(this, "your are not logged in", Toast.LENGTH_SHORT).show();
            }



        } else {

            inAppBar.setVisibility(GONE);
            outAppBar.setVisibility(VISIBLE);


            Query reference = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("date");

            final List<Post> posts = new ArrayList<>();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    posts.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Post post = dataSnapshot.getValue(Post.class);
                        posts.add(post);
                    }
                    postRecycle.setAdapter(new PostAdapter(posts, MainActivity.this));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void logout(View view) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        inAppBar.setVisibility(GONE);
        outAppBar.setVisibility(VISIBLE);

        Query reference = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("date");

        final List<Post> posts = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    posts.add(post);
                }
                postRecycle.setAdapter(new PostAdapter(posts, MainActivity.this));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void add(View view) {
        if (user != null) {
            startActivity(new Intent(this, AddPostActivity.class));
        } else {
            Toast.makeText(this, "sorry you are no logged in", Toast.LENGTH_SHORT).show();
        }
    }
}