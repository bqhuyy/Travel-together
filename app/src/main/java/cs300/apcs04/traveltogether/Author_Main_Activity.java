package cs300.apcs04.traveltogether;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Author_Main_Activity extends AppCompatActivity {
    private ViewPager viewPager;
    private SlideAdapter myadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_main);
        viewPager = (ViewPager) findViewById(R.id.AuthorViewPager);
        myadapter = new SlideAdapter(this);
        viewPager.setAdapter(myadapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean lastpagechange = false;

            @Override
            public void onPageScrolled(int position, float v, int i1) {
                int lastIdx = myadapter.getCount() - 1;
                // End of intent
                if (lastpagechange && position == lastIdx){
                    Toast.makeText(Author_Main_Activity.this, "End of author section", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
                }
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int lastIdx = myadapter.getCount() - 1;
                int currItem = viewPager.getCurrentItem();
                if (currItem == lastIdx && state == 1){
                    lastpagechange = true;
                } else {
                    lastpagechange = false;
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right); //finish
    }
}
