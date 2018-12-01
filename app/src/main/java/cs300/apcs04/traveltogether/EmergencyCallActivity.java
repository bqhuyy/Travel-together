package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EmergencyCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_call);
    }

    public void EmergencyCall(View view) {
        int id = view.getId();
        switch(id){
            case R.id.policeButton:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:113"));
                startActivity(intent);
                break;
            case R.id.AmbulanceButton:
                Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:115"));
                startActivity(intent1);
                break;
            case R.id.firefighterButton:
                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:114"));
                startActivity(intent2);
                break;
        }
    }
}
