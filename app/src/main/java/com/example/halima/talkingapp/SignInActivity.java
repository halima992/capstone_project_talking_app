package com.example.halima.talkingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private Button Login;
    private EditText Email,Passward;
    private TextView create_account;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth=FirebaseAuth.getInstance();
        Login=(Button)findViewById(R.id.button_sign_in);
        Email=(EditText)findViewById(R.id.login_email);
        Passward=(EditText)findViewById(R.id.login_passward);
        create_account=(TextView)findViewById(R.id.create_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginToMainActivity();
            }
        });

    }

    private void LoginToMainActivity() {
        String email=Email.getText().toString();
        String passward=Passward.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,getResources().getString(R.string.toast_enter_email),Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(passward)){
            Toast.makeText(this,getResources().getString(R.string.toast_enter_password)
                    ,Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(email,passward)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             goToMainActivity();
                         }
                         else{
                             String message= task.getException().getMessage();
                             Toast.makeText(SignInActivity.this,getResources().getString(R.string.toast_error_occurred)
                                     +message,Toast.LENGTH_SHORT).show();
                         }
                        }
                    });
        }



    }
    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            goToMainActivity();
        }
    }
    private void goToRegisterActivity() {
        Intent register = new Intent(SignInActivity.this, SingUpActivity.class);
        startActivity(register);

    }
    private void goToMainActivity(){
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();}

}

