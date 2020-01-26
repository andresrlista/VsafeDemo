package com.anlisoft.vsafe.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.anlisoft.vsafe.R;
import com.anlisoft.vsafe.animations.interpolators.BounceInterpolator;


public class BounceViewAnimation {

    public static void startBounceAnimation(Context context, View view, double amplitude, double frequency) {
        try {
            Animation animationView = AnimationUtils.loadAnimation(context, R.anim.bounce_anim);
            BounceInterpolator interpolator = new BounceInterpolator(amplitude, frequency);
            animationView.setInterpolator(interpolator);
            view.startAnimation(animationView);
        } catch (Exception e) {
            Toast.makeText(context, "Error inesperado: 4001", Toast.LENGTH_SHORT).show();
        }

    }
}
