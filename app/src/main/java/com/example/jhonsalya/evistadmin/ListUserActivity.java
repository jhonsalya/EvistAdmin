package com.example.jhonsalya.evistadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jhonsalya.evistadmin.Model.User;
import com.example.jhonsalya.evistadmin.ViewHolder.UserViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListUserActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    RecyclerView recycler_user;

    FirebaseRecyclerAdapter<User,UserViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        database = FirebaseDatabase.getInstance();
        user = database.getInstance().getReference().child("Users");

        // yang ini buat cek user
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());

        //load user
        recycler_user = (RecyclerView) findViewById(R.id.recycler_user);
        recycler_user.setHasFixedSize(true);
        recycler_user.setLayoutManager(new LinearLayoutManager(this));

        loadUser();
    }

    private void loadUser(){
        adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class,
                R.layout.user_card,
                UserViewHolder.class,
                user
                ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                final String post_key = getRef(position).getKey().toString();
                viewHolder.setName(model.getName());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setReported(model.getReported());
                //viewHolder.setImage(getApplicationContext(),model.getImage());

                viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(ListUserActivity.this, "User Blocked Not Yet", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListUserActivity.this);
                        alertDialog.setMessage("Block This User?");
                        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                blockUser(post_key);
                            }
                        });

                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ListUserActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        });
                        alertDialog.show();
                    }
                });
            }
        };
        Log.d("TAG","" + adapter.getItemCount());
        recycler_user.setAdapter(adapter);
    }

    public void blockUser(final String post_key){
        final String statusValue = "2";

        final DatabaseReference newPost = user.child(post_key);

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newPost.child("status").setValue(statusValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            deleteEvent(post_key);
                            Toast.makeText(ListUserActivity.this, "User Blocked", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteEvent(final String post_key) {
        final DatabaseReference drTest = FirebaseDatabase.getInstance().getReference().child("EventApp");

        drTest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child("uid").getValue(String.class).equals(post_key)){
                        drTest.child(snapshot.getRef().getKey().toString()).removeValue();
                        //Toast.makeText(ListUserActivity.this, snapshot.getRef().getKey().toString(), Toast.LENGTH_SHORT).show();
                    }
                    //snapshot.child(post_key).getRef().setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
