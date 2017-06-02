package cn.yshstudio.originalproduct.chat.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by kongqing on 2017/3/30.
 */

public class EMFrameLayout extends FrameLayout {

    public EMFrameLayout(@NonNull Context context) {
        super(context);
    }

    public EMFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EMFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }
}
