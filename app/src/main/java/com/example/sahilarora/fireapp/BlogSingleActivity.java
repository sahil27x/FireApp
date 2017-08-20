package com.example.sahilarora.fireapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BlogSingleActivity extends AppCompatActivity {

    private String mpost_key = null;

    private DatabaseReference mDatabase;

    private ImageView mBlogSingleImage;
    private TextView mBlogSingleTitle;
    private TextView mBlogSingleDesc;
    private Button mSingleRemovePost;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);


        mBlogSingleImage=(ImageView)findViewById(R.id.Single_post_image);
        mBlogSingleTitle=(TextView)findViewById(R.id.single_title);
        mBlogSingleDesc=(TextView)findViewById(R.id.single_desc);
        mSingleRemovePost=(Button)findViewById(R.id.single_remove_btn);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Blog");
        mAuth=FirebaseAuth.getInstance();

        final String mpost_key = getIntent().getExtras().getString("Blog_id");

      //  Toast.makeText(BlogSingleActivity.this,mpost_key,Toast.LENGTH_LONG).show();

        mDatabase.child(mpost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("Title").getValue();
                String post_desc= (String) dataSnapshot.child("Description").getValue();

                String post_image = (String)dataSnapshot.child("Image").getValue();
                String post_uid = (String)dataSnapshot.child("u_id").getValue();

                mBlogSingleTitle.setText(post_title);
                mBlogSingleDesc.setText(post_desc);

                Picasso.with(BlogSingleActivity.this).load(post_image).into(mBlogSingleImage);

               // Toast.makeText(BlogSingleActivity.this,post_uid,Toast.LENGTH_LONG).show();

                if(mAuth.getCurrentUser().getUid().equals(post_uid)){
                    mSingleRemovePost.setVisibility(View.VISIBLE);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mSingleRemovePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(mpost_key).removeValue();

                Intent mainintent = new Intent(BlogSingleActivity.this,MainActivity.class);
                startActivity(mainintent);


            }
        });


    }
}
