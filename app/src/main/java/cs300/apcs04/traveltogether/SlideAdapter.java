package cs300.apcs04.traveltogether;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    // list of images
    public int[] list_images = {
            R.drawable.hieu,
            R.drawable.huy,
            R.drawable.khanh,
            R.drawable.quyen,
            R.drawable.tuan
    };

    //list of titles
    public String[] list_titles = {
            "OCCUPATION",
            "OCCUPATION",
            "OCCUPATION",
            "OCCUPATION",
            "OCCUPATION"
    };

    //list of descriptions
    public String[] list_descriptions = {
            "Description 1",
            "Description 2",
            "Description 3",
            "Description 4",
            "Description 5"
    };

    //list of background colours
    public int[] list_colors = {
            Color.rgb(115, 153, 0),
            Color.rgb(239, 85, 85),
            Color.rgb(110, 49, 89),
            Color.rgb(1, 188, 212),
            Color.rgb(111, 111, 111)
    };

    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide, container, false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slidelinearlayout);
        ImageView imgslide = (ImageView) view.findViewById(R.id.slideimg);
        TextView txttitle = (TextView) view.findViewById(R.id.txttitle);
        TextView description = (TextView) view.findViewById(R.id.txtdescription);
        layoutslide.setBackgroundColor(list_colors[position]);
        imgslide.setImageResource(list_images[position]);
        txttitle.setText(list_titles[position]);
        description.setText(list_descriptions[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}