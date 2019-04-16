package com.amishgarg.wartube;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import retrofit2.http.GET;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PicassoUtil {

    public static void loadImagePicasso(String url, ImageView imageView)
    {
        Context context = imageView.getContext();
        @SuppressLint("ResourceAsColor") ColorDrawable colorDrawable = new ColorDrawable(R.color.blue_grey_500);

        if(!url.equals("")) {
            Picasso.get().load(url)
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }else
        {
            Picasso.get().load(R.color.blue_grey_900)
                    .fit()
                    .centerCrop()
                    .into(imageView);

        }

    }

}
