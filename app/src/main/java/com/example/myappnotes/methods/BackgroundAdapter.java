package com.example.myappnotes.methods;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.myappnotes.R;

public class BackgroundAdapter extends BaseAdapter {

    public int[] imageArray = {R.drawable.background_gallery,R.drawable.background1, R.drawable.background2, R.drawable.background3,
            R.drawable.background4, R.drawable.background5, R.drawable.background6, R.drawable.background7,
            R.drawable.background8, R.drawable.background9, R.drawable.background10, R.drawable.background11};
    private Context mcontext;

    public BackgroundAdapter(Context context)
    {
        mcontext = context;
    }

    @Override
    public int getCount()
    {
        return imageArray.length;
    }

    @Override
    public Object getItem(int position)
    {
        return imageArray[position];
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent)
    {
        ImageView imageView = new ImageView(mcontext);
        imageView.setImageResource(imageArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340, 350));

        return imageView;
    }

}
