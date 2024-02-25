package com.app.swipefeature.SwipeViewUtil.internal;


import android.view.animation.Interpolator;

import com.app.swipefeature.SwipeViewUtil.Direction;


public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
