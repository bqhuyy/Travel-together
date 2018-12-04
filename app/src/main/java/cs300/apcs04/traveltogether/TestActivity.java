package cs300.apcs04.traveltogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		//Bundle bundle = this.getIntent().getExtras();
		Intent i = getIntent();
		ArrayList<String> mMemList = new ArrayList<>();
		mMemList = i.getStringArrayListExtra("listMem");

		if(mMemList == null)
			Log.d("test", "nulllll");
		else {
			for (String id : mMemList) {
				Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
