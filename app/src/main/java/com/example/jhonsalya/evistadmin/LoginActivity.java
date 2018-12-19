package com.example.jhonsalya.evistadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jhonsalya.evistadmin.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail;
    private EditText loginPass;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = (EditText) findViewById(R.id.editEmail);
        loginPass = (EditText) findViewById(R.id.editPassword);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void loginButtonClicked(View view){
        //Toast.makeText(LoginActivity.this, "Register Success, Please Login", Toast.LENGTH_LONG).show();
        final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();

        String email = loginEmail.getText().toString().trim();
        String pass = loginPass.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExists();
                        mDialog.dismiss();
                    }
                    else{
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            // log error here
                            Toast.makeText(LoginActivity.this, "Collision", Toast.LENGTH_LONG).show();
                        } catch (FirebaseNetworkException e) {
                            // log error here
                            Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            // log error here
                            Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            Log.e("Some Tag", e.getMessage(), e);
                        }
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Please Fill All Form", Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        }
    }

    public void checkUserExists(){
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(user_id).getValue(User.class);
                if(user.getStatus().equals("1")){
                    if(dataSnapshot.hasChild(user_id)){
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "You Are Not Admin", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}