package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
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
import com.shrikanthravi.chatview.data.Message;
import com.shrikanthravi.chatview.widget.ChatView;

import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 1;
    String groupChatID = "123", planID = "abcd"; // get from plan intent
    RelativeLayout activity_chat;
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
        activity_chat = (RelativeLayout)findViewById(R.id.activity_chat);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupChatID = extras.getString("groupChatID"); //get groupChatID here
            planID = extras.getString("planID"); //get planID here
        }
        database = FirebaseDatabase.getInstance();
        messageRef = database.getReference("message/"+groupChatID);
        listOfMemberRef = database.getReference("plan/"+planID+"/listOfMember");
        userRef = database.getReference("user");
        // Read from the database

        chatView.setOnClickSendButtonListener(new ChatView.OnClickSendButtonListener() {
            @Override
            public void onSendButtonClick(String body) {
                messageRef.push().setValue(new ChatMessage(body,
                        FirebaseAuth.getInstance().getCurrentUser().getUid()));
            }
        });

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                if (userInfo.get(key) == "") {
                    userInfo.put(key, dataSnapshot.child("name").getValue(String.class));
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
        });


        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && (FirebaseAuth.getInstance().getCurrentUser() != null)) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && (FirebaseAuth.getInstance().getCurrentUser() != null)) {
                    DisplayMessages(dataSnapshot);
                }
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
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_chat,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
        }
        userInfo = new HashMap<String, String>();
        listOfMemberRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && (FirebaseAuth.getInstance().getCurrentUser() != null)) {
                    userInfo.put(dataSnapshot.child("id").getValue(String.class),"");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists() && (FirebaseAuth.getInstance().getCurrentUser() != null)) {
                    userInfo.put(dataSnapshot.child("id").getValue(String.class),"");
                }
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
        });
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        final Iterator iterator = dataSnapshot.getChildren().iterator();
        String userId;
        while (iterator.hasNext()) {
            final Message message = new Message();
            message.setBody((String)((DataSnapshot)iterator.next()).getValue()); //message body
            message.setTime((String)(DateFormat.format("h:mm a, MMM d, yyyy",(Long)((DataSnapshot)iterator.next()).getValue())));
            userId = (String)((DataSnapshot)iterator.next()).getValue();
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(userId)) {
                message.setType(Message.RightSimpleMessage); //message type
            }
            else {
                message.setUserName(userInfo.get(userId));
                message.setType(Message.LeftSimpleMessage); //message type
            }

//            message.setUserName((String)((DataSnapshot)iterator.next()).getValue()); //sender name
            //sender icon
            chatView.addMessage(message);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SIGN_IN_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(activity_chat,"Successfully signed in. Welcome!", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(activity_chat,"We couldn't sign you in. Please try again later", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chat_menu_sign_out:
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Snackbar.make(activity_chat,"You have been signed out.",Snackbar.LENGTH_SHORT).show();
                        finish();
                    }
                });
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat,menu);
        return true;
    }
}
