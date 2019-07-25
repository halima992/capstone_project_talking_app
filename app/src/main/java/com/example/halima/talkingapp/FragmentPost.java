package com.example.halima.talkingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentPost extends Fragment {
    private String Post;
    @BindView(R.id.postText)
     EditText mEditeText;
    @BindView(R.id.sendButton)
     FloatingActionButton mSendButton;
    public static final int POST_LENGTH_LIMIT = 140;
    private DatabaseReference usersReference, postReference, myPostReference;
    private FirebaseAuth mAuth;
    private String user_id;
    private String recentDate, recentTime, postName;
    @BindView(R.id.all_users_posts)
     RecyclerView mRecyclerView;
    public FragmentPost() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.post_fragment,container,false);

        ButterKnife.bind(this,view);

        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);


        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        postReference = FirebaseDatabase.getInstance().getReference().child("Post");
        myPostReference =FirebaseDatabase.getInstance().getReference().child("MyPost");
        mEditeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mEditeText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(POST_LENGTH_LIMIT)});

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send messages on click
                Post = mEditeText.getText().toString();
                  if(TextUtils.isEmpty(Post))
                { Toast.makeText(getContext(),getResources().getString(R.string.toast_enter_post), Toast.LENGTH_SHORT).show();
                }
                  else{
                      SavingPostInformation();
                  }
                // Clear input box
                mEditeText.setText("");
            }
        });
        return view;
    }


    private void SavingPostInformation()
    {     calenderInformation();
        usersReference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String FullName = dataSnapshot.child("fullname").getValue().toString();
                    String UserName=  dataSnapshot.child("username").getValue().toString();

                    HashMap messageMap = new HashMap();
                    messageMap.put("uid", user_id);
                    messageMap.put("post", Post);
                    messageMap.put("fullname", FullName);
                    messageMap.put("username", UserName);
                    messageMap.put("time", ServerValue.TIMESTAMP);
                    postReference.child(user_id + postName).updateChildren(messageMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                     Toast.makeText(getContext(),getResources().getString(R.string.toast_add_new_post), Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), getResources().getString(R.string.toast_error_update_post)
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    myPostReference.child(user_id).child(user_id + postName).updateChildren(messageMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getContext(),getResources().getString(R.string.toast_update_user_posts)
                                                , Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), getResources().getString(R.string.toast_error_update_post)
                                                , Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void calenderInformation(){

        Calendar calenderDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        recentDate = currentDate.format(calenderDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        recentTime = currentTime.format(calenderDate.getTime());

        postName = recentDate + recentTime;

    }
    private void DisplayTimeline() {
        final Query query= postReference.orderByChild("time");
    FirebaseRecyclerOptions options =
            new FirebaseRecyclerOptions.Builder<FriendlPosts>()
            .setQuery(query, FriendlPosts.class)
            .build();
    FirebaseRecyclerAdapter<FriendlPosts,PostsViewHolder>adapter =
            new FirebaseRecyclerAdapter<FriendlPosts, PostsViewHolder>(options) {
        @Override
        protected void onBindViewHolder(@NonNull final PostsViewHolder holder, int position, @NonNull FriendlPosts model) {
            String messageID = getRef(position).getKey();
            postReference.child(messageID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild("post")){
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

        @NonNull
        @Override
        public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_item,viewGroup,false);
            PostsViewHolder postsViewHolder = new PostsViewHolder(view);
            return postsViewHolder;
        }
    };
    mRecyclerView.setAdapter(adapter);
    adapter.startListening();
    }
public static class PostsViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.username)
    TextView mUsername;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.post)
    TextView mPost;
    public PostsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }

}

    @Override
    public void onStart() {
        super.onStart();
        DisplayTimeline();
    }
}
