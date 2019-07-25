package com.example.halima.talkingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentUsers extends Fragment {


    private DatabaseReference usersReference;
    private FirebaseAuth mAuth;
    private String user_id;
    @BindView(R.id.all_user)
    RecyclerView mRecyclerView;


    public FragmentUsers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.users_fragment,container,false);
        ButterKnife.bind(this,view);

        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        return view;
    }
    private void DisplayAllUsers() {
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<Users>()
                     .setQuery(usersReference,Users.class)
                        .build();
        FirebaseRecyclerAdapter<Users,usersViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, usersViewHolder>(options){

                    @NonNull
                    @Override
                    public usersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_item,viewGroup,false);
                        usersViewHolder viewholder = new usersViewHolder(view);
                        return viewholder;                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final usersViewHolder holder, int position, @NonNull Users model) {
                        String usersID = getRef(position).getKey();
                        usersReference.child(usersID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String FullName = dataSnapshot.child("fullname").getValue().toString();
                                    String userName = dataSnapshot.child("username").getValue().toString();
                                    holder.mName.setText(FullName);
                                    holder.mUsername.setText(userName);

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                };
        mRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    public static class usersViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.username)
        TextView mUsername;
        @BindView(R.id.name)
        TextView mName;
        public usersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }
    @Override
    public void onStart() {
        super.onStart();
        DisplayAllUsers();
    }}


