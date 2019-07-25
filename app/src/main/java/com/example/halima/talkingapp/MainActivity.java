package com.example.halima.talkingapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public static final String SHARED_PREFS = "prefs";
    public static final String FULLNAME ="fullname";
    public static final String USERNAME ="username";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager=(ViewPager)findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentPost(),getResources().getString(R.string.users_feed));
        adapter.addFragment(new FragmentUsers(),getResources().getString(R.string.users));
        adapter.addFragment(new FragmentMyPost(),getResources().getString(R.string.my_post));
        mAuth =FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ActionBar actionBar= getSupportActionBar();
        actionBar.setElevation(0);
        ConnectionAsync task = new ConnectionAsync();
        task.execute();
    }


    @Override
    protected  void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            goToLoginActivity();

        }
        else{
            validateExistUser();
        }

    }

    private void validateExistUser() {
        final String user_id=mAuth.getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(user_id)){
                    goToSetupActivity();
                }
                else{
                    SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(FULLNAME,dataSnapshot.child(user_id).child("fullname").getValue().toString() );
                    editor.putString(USERNAME,dataSnapshot.child(user_id).child("username").getValue().toString() );
                    editor.apply();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void goToLoginActivity() {
        Intent login = new Intent(MainActivity.this, SignInActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
        finish();

    }
    private void goToSetupActivity(){
        Intent saveInformation = new Intent(MainActivity.this,SetupActivity.class);
        saveInformation.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(saveInformation);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mAuth.signOut();
                goToLoginActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }    }





    class ConnectionAsync extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(MainActivity.this,getResources().getString(R.string.toast_connected_to_internet), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,getResources().getString(R.string.toast_not_connected_to_internet), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (isConnected()) {
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) (new URL("http://www.google.com")
                            .openConnection());
                    urlConnection.setConnectTimeout(1500);
                    urlConnection.connect();
                    if(urlConnection.getResponseCode() == 200){
                        urlConnection.disconnect();
                        return true;
                    }
                    urlConnection.disconnect();
                } catch (Exception e) {
                    return false;

                }
            }
            return false;
        }
    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
