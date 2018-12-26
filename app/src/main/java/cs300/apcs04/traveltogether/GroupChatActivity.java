package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shrikanthravi.chatview.data.Message;
import com.shrikanthravi.chatview.widget.ChatView;

import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 1;
    String planID, mUser;
    LinearLayout activity_chat;
    FirebaseDatabase database;
    DatabaseReference messageRef, listOfMemberRef, userRef;
    ChatView chatView;
    HashMap<String, String> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        chatView  = (ChatView) findViewById(R.id.chatView);
        // Write a message to the database
        activity_chat = (LinearLayout)findViewById(R.id.activity_chat);

        Intent intent = getIntent();
        planID = intent.getStringExtra("planID");
        String planTitle = intent.getStringExtra("planTitle");
        mUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        messageRef = database.getReference("message/"+planID);
        listOfMemberRef = database.getReference("plan/"+planID+"/listOfMember");
        // Read from the database

        Toolbar toolbar = findViewById(R.id.toolbar_groupchat);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(planTitle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupChatActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
            }
        });

        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {
                messageRef.push().setValue(new ChatMessage(body,
                        FirebaseAuth.getInstance().getCurrentUser().getUid()));
            }
        });
        getData();
    }

    public void getData() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot messageSnapshot, String s) {
                final Iterator iterator = messageSnapshot.getChildren().iterator();
                String userId;
                while (iterator.hasNext()) {
                    final Message message = new Message();
                    message.setBody((String)((DataSnapshot)iterator.next()).getValue()); //message body
                    message.setTime((String)(DateFormat.format("h:mm a, MMM d, yyyy",(Long)((DataSnapshot)iterator.next()).getValue())));
                    userId = (String)((DataSnapshot)iterator.next()).getValue();
                    userRef = database.getReference("user/"+userId);
                    final String finalUserId = userId;
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot userSnapshot) {
                            if (mUser.equals(finalUserId)) {
                                message.setType(Message.RightSimpleMessage); //message
                                chatView.addMessage(message);
                            }
                            else {
                                message.setUserName(userSnapshot.child("name").getValue(String.class));
                                message.setType(Message.LeftSimpleMessage); //message type
                                chatView.addMessage(message);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                        });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        messageRef.addChildEventListener(childEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
    }
}
