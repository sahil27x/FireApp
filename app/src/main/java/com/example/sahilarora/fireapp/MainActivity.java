package com.example.sahilarora.fireapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mDatabaseLike;

    private Query mQueryCurrentUser;


    private boolean mProcessLike = false;


private RecyclerView mBlogList;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mAuth=FirebaseAuth.getInstance();
            mAuthListner = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {

                        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                       loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    }
                    else
                    {
                    }
                }
            };

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
            mDatabaseUsers=FirebaseDatabase.getInstance().getReference().child("Users");
            mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("Likes");

           // String currentUserId = mAuth.getCurrentUser().getUid();

            mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Blog");
            //mQueryCurrentUser = mDatabaseCurrentUser.orderByChild("u_id").equalTo(currentUserId);




            mDatabaseUsers.keepSynced(true);
            mDatabase.keepSynced(true);
            mDatabase.keepSynced(true);

            mBlogList = (RecyclerView)findViewById(R.id.blog_list);
            mBlogList.setHasFixedSize(true);
            mBlogList.setLayoutManager(new LinearLayoutManager(this));

            checkUserExist();
        }


    @Override
    protected void onStart() {
        super.onStart();



        mAuth.addAuthStateListener(mAuthListner);

        FirebaseRecyclerAdapter<Blog,BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,R.layout.blog_row,BlogViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, final int position) {

                final String post_key = getRef(position).getKey();


                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getApplicationContext(),model.getImage());
                viewHolder.setUsername(model.getUsername());

                viewHolder.setLikeBtn(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Toast.makeText(MainActivity.this,post_key,Toast.LENGTH_LONG).show();

                        Intent singleBlogIntent = new Intent(MainActivity.this,BlogSingleActivity.class);
                        singleBlogIntent.putExtra("Blog_id",post_key);
                        startActivity(singleBlogIntent);

                    }
                });

                viewHolder.mlikebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessLike = true;


                           mDatabaseLike.addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {

                                   if(mProcessLike) {

                                       if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
                                           mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                           mProcessLike = false;
                                       } else {
                                           mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                           mProcessLike = false;
                                       }
                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {


                               }
                           }) ;


                    }
                });




            }
        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);
    }


    private void checkUserExist() {

        if (mAuth.getCurrentUser() != null) {

            final String user_id = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(user_id)) {

                        Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;
        ImageButton mlikebtn;
        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mlikebtn=(ImageButton)mView.findViewById(R.id.like_btn);

            mDatabaseLike=FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth=FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);


        }


        public void setLikeBtn(final String post_key){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())){

                        mlikebtn.setImageResource(R.drawable.ic_dislike);

                    }else
                    {
                        mlikebtn.setImageResource(R.drawable.ic_like);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        public void setTitle(String Title){
            TextView post_title =(TextView)mView.findViewById(R.id.posted_title);
            post_title.setText(Title);
        }

        public void setDesc(String Description){
            TextView post_desc = (TextView)mView.findViewById(R.id.posted_desc);
            post_desc.setText(Description);
        }

        public void setUsername(String username){
            TextView Postusername = (TextView)mView.findViewById(R.id.post_username);
            Postusername.setText(username);

        }


        public void setImage(Context ctx, String Image){
            ImageView post_image = (ImageView)mView.findViewById(R.id.posted_image);
            Picasso.with(ctx).load(Image).into(post_image);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        }

        if(item.getItemId()==R.id.action_profile){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        }

        if(item.getItemId()==R.id.action_chat){
            startActivity(new Intent(MainActivity.this,MessageActivity.class));
        }

        if(item.getItemId()==R.id.action_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }
}
