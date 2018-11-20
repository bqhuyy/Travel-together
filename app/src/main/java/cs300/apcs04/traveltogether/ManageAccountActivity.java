package cs300.apcs04.traveltogether;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ManageAccountActivity extends AppCompatActivity {

    private static final String TAG = "ManageAccountActivity";

    FirebaseUser mUser;

    String mName;
    String mEmail;
    Uri mPhotoUrl;
    boolean mEmailVerified;
    String mUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        if(checkCurrentUser())
        {
            getUserProfile();
            displayInfo();
        }
    }

    public boolean checkCurrentUser(){
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null)
            return true;
        else
            return false;
    }

    public void getUserProfile(){
        if(mUser!=null){
            mName = mUser.getDisplayName();
            mEmail = mUser.getEmail();
            mPhotoUrl = mUser.getPhotoUrl();
            mEmailVerified = mUser.isEmailVerified();
            mUserID = mUser.getUid();
        }
    }

    public void displayInfo(){
        EditText editText = findViewById(R.id.editUserName);
        editText.setText(mUser.getDisplayName());
    }

    public void updateProfile(View v){
        //get user input
        String newUsername= ((EditText)findViewById(R.id.editUserName)).getText().toString();
        Uri newPhoto = mPhotoUrl;

        //change request
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newUsername)
                //.setPhotoUri(newPhoto)
                .build();
        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "User profile updated");
                            getUserProfile();
                        }
                    }
                });
    }

    public void updateEmail(){
        String newEmail = mEmail;

        mUser.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "User email address updated.");
                            mEmail = mUser.getEmail();
                            sendEmailVerification();
                        }
                    }
                });
    }

    public void updatePassword(){
        String newPass = "238jr9wef00";

        mUser.updatePassword(newPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }

    public void sendEmailVerification(){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        mUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }

    public void sendPasswordReset(){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(mEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
    }
}
