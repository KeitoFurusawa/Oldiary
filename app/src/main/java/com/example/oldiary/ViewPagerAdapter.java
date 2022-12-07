package com.example.oldiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;

    int images[] = {
        R.drawable.app,
        R.drawable.diary,
        R.drawable.connect,
        R.drawable.lets_start
    };

    int heading[] = {
        R.string.heading1,
        R.string.heading2,
        R.string.heading3,
        R.string.heading4
    };

    int desc[] = {
        R.string.desc1,
        R.string.desc2,
        R.string.desc3,
        R.string.desc4
    };

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);

        ImageView slideTitleImage = (ImageView) view.findViewById(R.id.titleImage);
        TextView slideHeading = (TextView) view.findViewById(R.id.textTitle);
        TextView slideDescription = (TextView) view.findViewById(R.id.textdesc);

        slideTitleImage.setImageResource(images[position]);
        slideHeading.setText(heading[position]);
        slideDescription.setText(desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

}
