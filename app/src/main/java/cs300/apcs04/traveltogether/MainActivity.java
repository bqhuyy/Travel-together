package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onBackPressed() {

        finish();
    }

    public void OnAccountButton(View view) {
        Intent intent = new Intent(this, ManageAccountActivity.class);
        startActivity(intent);
    }
}
