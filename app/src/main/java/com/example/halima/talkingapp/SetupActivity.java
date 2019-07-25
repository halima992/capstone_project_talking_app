package com.example.halima.talkingapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {

    private EditText Username, Name;
    private Button SaveButton;
    private FirebaseAuth mAuth;
    private DatabaseReference firebaseUserDatabase;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        firebaseUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        Username = (EditText) findViewById(R.id.user_id);
        Name = (EditText) findViewById(R.id.user_name);
        SaveButton = (Button) findViewById(R.id.button_save);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveUserInformation();
            }
        });

    }

    private void SaveUserInformation() {
        String username = Username.getText().toString();
        String name = Name.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this,getResources().getString(R.string.toast_enter_user_name), Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getResources().getString(R.string.toast_enter_full_name), Toast.LENGTH_SHORT).show();
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("username", username);
            hashMap.put("fullname", name);
            firebaseUserDatabase.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        goToMainActivity();
                        Toast.makeText(SetupActivity.this,getResources().getString(R.string.toast_create_account),Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String message =  task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, getResources().getString(R.string.toast_error_occurred) + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void goToMainActivity()
    {
        Intent intent = new Intent(SetupActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}