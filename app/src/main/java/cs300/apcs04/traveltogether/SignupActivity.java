package cs300.apcs04.traveltogether;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupEmailPassword";

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private EditText mUsernameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private DatabaseReference userRef;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        database = FirebaseDatabase.getInstance();
        mUsernameField = findViewById(R.id.signup_username);
        mEmailField = findViewById(R.id.signup_email);
        mPasswordField = findViewById(R.id.signup_password);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.signup_button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(mUsernameField.getText().toString(), mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
        findViewById(R.id.signup_login_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void createAccount(final String username, final String email, final String password) {
        Log.d(TAG, "createAccount: " + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createAccountWithEmail:success");
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                            currentUser.updateProfile(profileChangeRequest);
//                            Toast.makeText(SignupActivity.this,currentUser.getUid(),Toast.LENGTH_LONG).show();
                            userRef = database.getReference("user");
                            userRef.child(currentUser.getUid()).setValue(new User(username,email));
                            goToMainActivity();
                        } else {
                            //If sign in fail, display a message to the user
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            hideProgressDialog();
                            Toast.makeText(SignupActivity.this, "Please check your information again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void goToMainActivity() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required!");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required!");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        String username = mUsernameField.getText().toString();
        if (TextUtils.isEmpty(username)) {
            mUsernameField.setError("Required!");
            valid = false;
        } else {
            mUsernameField.setError(null);
        }

        return valid;
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        super.onBackPressed();
    }
}
