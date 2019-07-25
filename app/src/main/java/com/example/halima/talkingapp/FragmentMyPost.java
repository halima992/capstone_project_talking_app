package com.example.halima.talkingapp;

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

public class FragmentMyPost extends Fragment {
    private DatabaseReference  myPostreference;
    private FirebaseAuth mAuth;
    private String user_id;
@BindView(R.id.my_post)
 RecyclerView mRecyclerView;
    public FragmentMyPost() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view;

        view=inflater.inflate(R.layout.my_post_fragment,container,false);
           ButterKnife.bind(this,view);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        myPostreference = FirebaseDatabase.getInstance().getReference().child("MyPost").child(user_id);
        return view;
    }
    private void DisplayMyPost() {
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<FriendlPosts>()
                        .setQuery(myPostreference, FriendlPosts.class)
                        .build();
        FirebaseRecyclerAdapter<FriendlPosts, myPostViewHolder>adapter =
                new FirebaseRecyclerAdapter<FriendlPosts, myPostViewHolder>(options){

                    @NonNull
                    @Override
                    public myPostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_item,viewGroup,false);
                        myPostViewHolder messageViewHolder = new myPostViewHolder(view);
                        return messageViewHolder;                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final myPostViewHolder holder, int position, @NonNull FriendlPosts model) {
                        String messageID = getRef(position).getKey();
                        myPostreference.child(messageID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("uid").getValue().toString().equals(user_id)){
                                    String FullName = dataSnapshot.child("fullname").getValue().toString();
                                    String userName = dataSnapshot.child("username").getValue().toString();
                                    String userPost =dataSnapshot.child("post").getValue().toString() ;
                                    holder.mName.setText(FullName);
                                    holder.mPost.setText(userPost);
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
    public static class myPostViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.username)
        TextView mUsername;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.post)
        TextView mPost;
        public myPostViewHolder(View itemView) {
            super(itemView);
       ButterKnife.bind(this,itemView);
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        DisplayMyPost();
    }

}