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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingUpActivity extends AppCompatActivity {
    EditText Email , Passward, Confirm;
    Button SignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_sign_up);
        Email=(EditText)findViewById(R.id.et_email);
        Passward=(EditText)findViewById(R.id.et_passward);
        Confirm=(EditText)findViewById(R.id.et_confirm_passward);
        SignUp=(Button)findViewById(R.id.button_sign_up);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewUser();
            }
        });



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

    private void addNewUser(){
        String email=Email.getText().toString();
        String passward=Passward.getText().toString();
        String confirm=Confirm.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,getResources().getString(R.string.toast_enter_email)
                    ,Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(passward)){
            Toast.makeText(this,getResources().getString(R.string.toast_enter_password)
                    ,Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirm)){
            Toast.makeText(this,getResources().getString(R.string.toast_confirm_password)
                    ,Toast.LENGTH_SHORT).show();
        }
        else if(!passward.equals(confirm)){
            Toast.makeText(this,getResources().getString(R.string.toast_error_match)
                    ,Toast.LENGTH_SHORT).show();

        }
        else{
            mAuth.createUserWithEmailAndPassword(email,passward)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                goToSetupActivity();
                            Toast.makeText(SingUpActivity.this,getResources().getString(R.string.toast_success_authenticate)
                                    ,Toast.LENGTH_SHORT).show();}

                           else{
                               String message= task.getException().getMessage();
                                Toast.makeText(SingUpActivity.this,getResources().getString(R.string.toast_error_occurred)
                                        +message,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }

    }
    private void goToSetupActivity() {
        Intent setup = new Intent(SingUpActivity.this,SetupActivity.class);
        setup.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setup);
        finish();

    }
    private void goToMainActivity()
    {
        Intent intent = new Intent(SingUpActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
