package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button btn_Note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void OnAccountButton(View view) {
        Intent intent = new Intent(this, ManageAccountActivity.class);
        startActivity(intent);
    }

    public void OnSignOutButton(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
